package fr.tiogars.starter.sample.models;

/**
 * Represents a single filter item for MUI DataGrid filtering.
 * Based on MUI X DataGrid v7 GridFilterItem structure.
 */
public class FilterItem {
    private String field;
    private String operator;
    private Object value;

    public FilterItem() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
