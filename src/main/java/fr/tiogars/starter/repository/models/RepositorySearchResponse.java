package fr.tiogars.starter.repository.models;

import java.util.List;

public class RepositorySearchResponse {
    private List<Repository> items;
    private long total;

    public List<Repository> getItems() { return items; }
    public void setItems(List<Repository> items) { this.items = items; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
