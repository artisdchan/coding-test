package com.interview.dto;

import java.util.List;

public class PagedResponse<T> {
    private List<T> content;
    private PaginationMeta pagination;

    public PagedResponse() {}

    public PagedResponse(List<T> content, PaginationMeta pagination) {
        this.content = content;
        this.pagination = pagination;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PaginationMeta getPagination() {
        return pagination;
    }

    public void setPagination(PaginationMeta pagination) {
        this.pagination = pagination;
    }
}
