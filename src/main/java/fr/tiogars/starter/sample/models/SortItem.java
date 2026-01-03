package fr.tiogars.starter.sample.models;

/**
 * Represents a single sort item for MUI DataGrid sorting.
 * Based on MUI X DataGrid v7 GridSortItem structure.
 */
public class SortItem {
    private String field;
    private String sort;

    public SortItem() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
