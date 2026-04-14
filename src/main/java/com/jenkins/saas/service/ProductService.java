package com.jenkins.saas.service;


import com.jenkins.saas.requests.ProductRequest;
import com.jenkins.saas.responses.ProductResponse;
import org.springframework.data.repository.CrudRepository;

public interface ProductService extends CrudService <ProductRequest, ProductResponse> {



}
