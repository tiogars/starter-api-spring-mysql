package fr.tiogars.starter.sample.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.FilterItem;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.models.SortItem;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.persistence.criteria.Predicate;

/**
 * Service for searching Sample entities with MUI DataGrid compatible interface.
 * Supports pagination, sorting, and filtering on all Sample entity fields.
 */
@Service
public class SampleSearchService {
    private final SampleRepository sampleRepository;

    public SampleSearchService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    /**
     * Search samples with pagination, sorting, and filtering.
     * 
     * @param request The search request containing pagination, sort, and filter criteria
     * @return SampleSearchResponse with rows and total count
     */
    public SampleSearchResponse search(SampleSearchRequest request) {
        // Build sorting
        Sort sort = buildSort(request.getSortModel());
        
        // Build pageable
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
        
        // Build specification for filtering
        Specification<SampleEntity> specification = buildSpecification(request.getFilterModel());
        
        // Execute query
        Page<SampleEntity> page = sampleRepository.findAll(specification, pageable);
        
        // Convert to models
        List<Sample> samples = page.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        
        return new SampleSearchResponse(samples, page.getTotalElements());
    }

    /**
     * Build Spring Data Sort from MUI DataGrid sort model.
     */
    private Sort buildSort(List<SortItem> sortModel) {
        if (sortModel == null || sortModel.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }
        
        List<Sort.Order> orders = sortModel.stream()
                .map(item -> {
                    Sort.Direction direction = "desc".equalsIgnoreCase(item.getSort()) 
                            ? Sort.Direction.DESC 
                            : Sort.Direction.ASC;
                    return new Sort.Order(direction, item.getField());
                })
                .collect(Collectors.toList());
        
        return Sort.by(orders);
    }

    /**
     * Build JPA Specification from MUI DataGrid filter model.
     * Supports filtering on all Sample entity fields.
     */
    private Specification<SampleEntity> buildSpecification(fr.tiogars.starter.sample.models.FilterModel filterModel) {
        return (root, query, criteriaBuilder) -> {
            if (filterModel == null || filterModel.getItems() == null || filterModel.getItems().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            List<Predicate> predicates = new ArrayList<>();
            
            for (FilterItem item : filterModel.getItems()) {
                String field = item.getField();
                String operator = item.getOperator();
                Object value = item.getValue();
                
                if (field == null || operator == null || value == null) {
                    continue;
                }
                
                Predicate predicate = buildPredicate(root, criteriaBuilder, field, operator, value);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
            
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            // Apply logic operator (and/or)
            if ("or".equalsIgnoreCase(filterModel.getLogicOperator())) {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    /**
     * Build individual predicate based on field, operator, and value.
     */
    private Predicate buildPredicate(jakarta.persistence.criteria.Root<SampleEntity> root, 
                                     jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                     String field, String operator, Object value) {
        switch (field) {
            case "id":
                return buildNumericPredicate(root.get("id"), criteriaBuilder, operator, value);
            case "name":
            case "description":
            case "createdBy":
            case "updatedBy":
                return buildStringPredicate(root.get(field), criteriaBuilder, operator, value);
            case "active":
                return buildBooleanPredicate(root.get("active"), criteriaBuilder, operator, value);
            case "createdAt":
            case "updatedAt":
                return buildDatePredicate(root.get(field), criteriaBuilder, operator, value);
            default:
                return null;
        }
    }

    /**
     * Build predicate for numeric fields.
     */
    private Predicate buildNumericPredicate(jakarta.persistence.criteria.Path<Long> path,
                                           jakarta.persistence.criteria.CriteriaBuilder cb,
                                           String operator, Object value) {
        Long numValue = convertToLong(value);
        if (numValue == null) {
            return null;
        }
        
        switch (operator) {
            case "=":
            case "equals":
                return cb.equal(path, numValue);
            case "!=":
            case "not":
                return cb.notEqual(path, numValue);
            case ">":
            case "greaterThan":
                return cb.greaterThan(path, numValue);
            case ">=":
            case "greaterThanOrEqual":
                return cb.greaterThanOrEqualTo(path, numValue);
            case "<":
            case "lessThan":
                return cb.lessThan(path, numValue);
            case "<=":
            case "lessThanOrEqual":
                return cb.lessThanOrEqualTo(path, numValue);
            default:
                return null;
        }
    }

    /**
     * Build predicate for string fields.
     */
    private Predicate buildStringPredicate(jakarta.persistence.criteria.Path<String> path,
                                          jakarta.persistence.criteria.CriteriaBuilder cb,
                                          String operator, Object value) {
        String strValue = value.toString();
        
        switch (operator) {
            case "contains":
                return cb.like(cb.lower(path), "%" + strValue.toLowerCase() + "%");
            case "equals":
                return cb.equal(cb.lower(path), strValue.toLowerCase());
            case "startsWith":
                return cb.like(cb.lower(path), strValue.toLowerCase() + "%");
            case "endsWith":
                return cb.like(cb.lower(path), "%" + strValue.toLowerCase());
            case "isEmpty":
                return cb.or(cb.isNull(path), cb.equal(cb.length(path), 0));
            case "isNotEmpty":
                return cb.and(cb.isNotNull(path), cb.greaterThan(cb.length(path), 0));
            default:
                return null;
        }
    }

    /**
     * Build predicate for boolean fields.
     */
    private Predicate buildBooleanPredicate(jakarta.persistence.criteria.Path<Boolean> path,
                                           jakarta.persistence.criteria.CriteriaBuilder cb,
                                           String operator, Object value) {
        Boolean boolValue = convertToBoolean(value);
        if (boolValue == null) {
            return null;
        }
        
        return cb.equal(path, boolValue);
    }

    /**
     * Build predicate for date fields.
     */
    private Predicate buildDatePredicate(jakarta.persistence.criteria.Path<Date> path,
                                        jakarta.persistence.criteria.CriteriaBuilder cb,
                                        String operator, Object value) {
        Date dateValue = convertToDate(value);
        if (dateValue == null) {
            return null;
        }
        
        switch (operator) {
            case "is":
            case "equals":
                return cb.equal(path, dateValue);
            case "not":
                return cb.notEqual(path, dateValue);
            case "after":
            case ">":
                return cb.greaterThan(path, dateValue);
            case "onOrAfter":
            case ">=":
                return cb.greaterThanOrEqualTo(path, dateValue);
            case "before":
            case "<":
                return cb.lessThan(path, dateValue);
            case "onOrBefore":
            case "<=":
                return cb.lessThanOrEqualTo(path, dateValue);
            default:
                return null;
        }
    }

    /**
     * Convert value to Long.
     */
    private Long convertToLong(Object value) {
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            // Invalid number format, return null to skip this filter
            return null;
        }
    }

    /**
     * Convert value to Boolean.
     */
    private Boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String str = value.toString().toLowerCase();
        if ("true".equals(str) || "1".equals(str)) {
            return true;
        }
        if ("false".equals(str) || "0".equals(str)) {
            return false;
        }
        return null;
    }

    /**
     * Convert value to Date.
     */
    private Date convertToDate(Object value) {
        try {
            if (value instanceof Date) {
                return (Date) value;
            }
            if (value instanceof Number) {
                return new Date(((Number) value).longValue());
            }
            // Try parsing as timestamp
            long timestamp = Long.parseLong(value.toString());
            return new Date(timestamp);
        } catch (NumberFormatException e) {
            // Invalid timestamp format, return null to skip this filter
            return null;
        }
    }

    /**
     * Convert SampleEntity to Sample model.
     */
    private Sample toModel(SampleEntity entity) {
        Sample model = new Sample();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        if (entity.getTags() != null) {
            model.setTags(
                entity.getTags()
                    .stream()
                    .map(tag -> new fr.tiogars.starter.sample.models.SampleTag(tag.getId(), tag.getName(), tag.getDescription()))
                    .collect(java.util.stream.Collectors.toSet())
            );
        }
        return model;
    }
}
