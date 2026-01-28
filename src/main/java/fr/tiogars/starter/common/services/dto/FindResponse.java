package fr.tiogars.starter.common.services.dto;

import java.util.List;

/**
 * Standard API response wrapper for find operations.
 * @param <T> Model type
 */
public class FindResponse<T> {
    private final List<T> data;
    private final int count;

    public FindResponse(List<T> data) {
        this.data = data != null ? List.copyOf(data) : List.of();
        this.count = this.data.size();
    }

    public List<T> getData() {
        return data;
    }

    public int getCount() {
        return count;
    }
}