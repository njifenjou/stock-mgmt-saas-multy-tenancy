package com.jenkins.saas.service.impl;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.entity.Product;
import com.jenkins.saas.entity.StockMvt;
import com.jenkins.saas.exceptions.DuplicateResourceException;
import com.jenkins.saas.mappers.StockMvtMapper;
import com.jenkins.saas.repositories.ProductRepository;
import com.jenkins.saas.repositories.StockMvtRepository;
import com.jenkins.saas.requests.StockMvmtRequest;
import com.jenkins.saas.responses.StockMvmtResponse;
import com.jenkins.saas.service.StockMvtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockServiceImpl implements StockMvtService {

    final StockMvtRepository stockMvtRepository;
    final StockMvtMapper stockMvtMapper;
    final ProductRepository productRepository;

    @Override
    public void create(final StockMvmtRequest request) {
        CheckProductExistById(request.getProductId());

        final StockMvt entity = stockMvtMapper.toEntity(request);
        stockMvtRepository.save(entity);

    }

    @Override
    public void update(final String id, final StockMvmtRequest request) {
        final Optional<StockMvt> stockMvt = stockMvtRepository.findById(id);
        if (stockMvt.isEmpty()) {
            log.debug("StockMvt not found");
            throw new EntityNotFoundException("StockMvt not found");
        }
        CheckProductExistById(request.getProductId());

        final StockMvt stockMvtToUpdate = this.stockMvtMapper.toEntity(request);
        stockMvtToUpdate.setId(id);
        stockMvtRepository.save(stockMvtToUpdate);

    }

    @Override
    public PageResponse<StockMvmtResponse> findAll(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = stockMvtRepository.findAll(pageRequest);
        final Page<StockMvmtResponse> stockMvmtResponses = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvmtResponses);
    }


    @Override
    public StockMvmtResponse findById(final String id) {

        return this.stockMvtRepository.findById(id)
                .map(this.stockMvtMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt not found"));
    }

    @Override
    public void delete(final String id) {
        final StockMvt stockMvt = this.stockMvtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StockMvt not found"));
        this.stockMvtRepository.deleteById(id);

    }


    private void CheckProductExistById(final String productId) {
        final Optional<Product> product = this.productRepository.findById(productId);
        log.debug("product does not exist");
        throw new DuplicateResourceException("product does not exist");
    }

}
