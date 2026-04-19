package com.jenkins.saas.requests;

import com.jenkins.saas.entity.TypeMvt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvmtRequest {
    @NotBlank(message = "description name should not be empty")
    private TypeMvt typeMvt;
    @Positive(message = "quantity should be positive number ")
    private Integer quantity;
    @PastOrPresent(message = "Date should be in the past or present")
    private LocalDateTime dateMvt;

    private String comment;
    @NotBlank(message = "product ID name should not be empty")
    private String productId;
}
