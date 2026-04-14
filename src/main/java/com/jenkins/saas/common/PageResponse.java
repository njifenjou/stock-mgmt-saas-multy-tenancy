package com.jenkins.saas.common;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean isFirst;
    private boolean isLast;

    public static <T> PageResponse<T> of(final Page<T>  page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())

                .build();
    }
}
