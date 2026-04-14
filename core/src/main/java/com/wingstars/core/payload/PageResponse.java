package com.wingstars.core.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
    private List<T> items;

    private boolean first;
    private boolean last;
    private boolean empty;

    private boolean hasNext;
    private boolean hasPrevious;
}
