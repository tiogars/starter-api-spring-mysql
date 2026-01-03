package fr.tiogars.starter.sample.models;

import java.util.List;

/**
 * Represents the filter model for MUI DataGrid filtering.
 * Based on MUI X DataGrid v7 GridFilterModel structure.
 */
public class FilterModel {
    private List<FilterItem> items;
    private String logicOperator = "and";

    public FilterModel() {
    }

    public List<FilterItem> getItems() {
        return items;
    }

    public void setItems(List<FilterItem> items) {
        this.items = items;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }
}
