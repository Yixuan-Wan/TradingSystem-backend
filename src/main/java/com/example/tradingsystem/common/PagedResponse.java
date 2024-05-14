package com.example.tradingsystem.common;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {
    private int totalPages;
    private int currentPage;
    private List<T> data;

    public PagedResponse(int totalPages, int currentPage, List<T> data) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.data = data;
    }

}
