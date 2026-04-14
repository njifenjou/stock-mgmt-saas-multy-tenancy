package com.jenkins.saas.requests;

import com.jenkins.saas.entity.TypeMvt;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvmtRequest {
    private TypeMvt typeMvt;
    private Integer quantity;
    private LocalDateTime dateMvt;
    private String comment;
    private String productId;
}
