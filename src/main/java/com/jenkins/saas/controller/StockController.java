package com.jenkins.saas.controller;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.requests.ProductRequest;
import com.jenkins.saas.requests.StockMvmtRequest;
import com.jenkins.saas.responses.StockMvmtResponse;
import com.jenkins.saas.service.StockMvtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockMvtService stockMvtService;

    @PostMapping
    public ResponseEntity<Void> createStock(
            @RequestBody
            @Valid final StockMvmtRequest request) {
        stockMvtService.create(request);
        return ResponseEntity.ok().build();
    }


    @PutMapping
    public ResponseEntity<Void> updateStock(
            @RequestBody
            @Valid final StockMvmtRequest request,
            @PathVariable("stock-mvt-id")
            @NotNull(message = "stockmvmt ID cannot be null") final String id) {
        this.stockMvtService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stock-mvt-id")
    public ResponseEntity<StockMvmtResponse> findStockById(
            @PathVariable("stock-mvt-id")
            @NotNull(message = "stock-mvmt ID cannot be null") final String id) {
        return ResponseEntity.ok(this.stockMvtService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<StockMvmtResponse>> findAllStocks(
            @RequestParam(name = "page", defaultValue = "0") final int page,
            @RequestParam(name = "size", defaultValue = "10") final int size) {
        return ResponseEntity.ok(this.stockMvtService.findAll(page, size));
    }

    @DeleteMapping("/stock-mvt-id")
    public ResponseEntity<Void> deleteStockById(

            @PathVariable("stock-mvt-id")
            @NotNull(message = "stock-mvt ID cannot be null") final String id) {
        this.stockMvtService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
