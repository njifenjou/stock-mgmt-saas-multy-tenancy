package com.jenkins.saas.service;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.responses.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CrudService<I, O> {
    void create(final I request);

    void update(final String id, final I request);

    PageResponse<O> findAll(final int page, final int size);

    O findById(final String id);

    void delete(final String id);
}
