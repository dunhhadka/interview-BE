package org.example.interview.user.application.model;

import lombok.Setter;

@Setter
public class PagingRequest {
    protected int limit = 10;
    protected int page = 1;

    public int getLimit() {
        if (limit <= 0) return 10;
        return Math.min(limit, 50);
    }

    public int getPage() {
        if (page <= 0) return 1;
        return page;
    }
}
