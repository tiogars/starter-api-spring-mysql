package fr.tiogars.starter.sample.models;

import java.util.List;

/**
 * Response model for Sample search operations.
 * Compatible with MUI X DataGrid v7 data structure.
 * 
 * Contains:
 * - rows: The list of Sample objects for the current page
 * - rowCount: The total number of rows matching the search criteria
 */
public class SampleSearchResponse {
    private List<Sample> rows;
    private long rowCount;

    public SampleSearchResponse() {
    }

    public SampleSearchResponse(List<Sample> rows, long rowCount) {
        this.rows = rows;
        this.rowCount = rowCount;
    }

    public List<Sample> getRows() {
        return rows;
    }

    public void setRows(List<Sample> rows) {
        this.rows = rows;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }
}
