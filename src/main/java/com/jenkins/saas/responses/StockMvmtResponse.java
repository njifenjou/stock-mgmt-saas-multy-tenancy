package com.jenkins.saas.responses;

import com.jenkins.saas.entity.TypeMvt;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvmtResponse {
    private TypeMvt typeMvt;
    private Integer quantity;
    private LocalDateTime dateMvt;
    private String comment;
    
}
