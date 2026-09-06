package com.etec.tourtripapi.category.dto.response;

import com.etec.tourtripapi.common.response.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategoryResponse extends BaseResponse {
    private Long categoryId;
    private String name;
    private String description;
}
