package fr.tiogars.starter.sample.models;

import java.util.List;

/**
 * Request model for Sample search operations.
 * Compatible with MUI X DataGrid v7 data structure.
 * 
 * This model supports:
 * - Pagination (page, pageSize)
 * - Sorting (sortModel)
 * - Filtering (filterModel)
 */
public class SampleSearchRequest {
    private int page = 0;
    private int pageSize = 10;
    private List<SortItem> sortModel;
    private FilterModel filterModel;

    public SampleSearchRequest() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SortItem> getSortModel() {
        return sortModel;
    }

    public void setSortModel(List<SortItem> sortModel) {
        this.sortModel = sortModel;
    }

    public FilterModel getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(FilterModel filterModel) {
        this.filterModel = filterModel;
    }
}
