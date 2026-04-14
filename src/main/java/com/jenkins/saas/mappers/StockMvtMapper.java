package com.jenkins.saas.mappers;

import com.jenkins.saas.entity.Product;
import com.jenkins.saas.entity.StockMvt;
import com.jenkins.saas.requests.StockMvmtRequest;
import com.jenkins.saas.responses.StockMvmtResponse;
import org.springframework.stereotype.Component;

@Component
public class StockMvtMapper {
    public StockMvt toEntity(final StockMvmtRequest request) {
        return StockMvt.builder()
                .dateMvt(request.getDateMvt())
                .comment(request.getComment())
                .typeMvt(request.getTypeMvt())
                .quantity(request.getQuantity())
                .product(Product.builder()
                        .id(request.getProductId())
                        .build())
                .build();
    }

    public StockMvmtResponse toResponse(final StockMvt entity) {
        return StockMvmtResponse.builder()
                .dateMvt(entity.getDateMvt())
                .comment(entity.getComment())
                .typeMvt(entity.getTypeMvt())
                .quantity(entity.getQuantity())

                .build();

    }
}
