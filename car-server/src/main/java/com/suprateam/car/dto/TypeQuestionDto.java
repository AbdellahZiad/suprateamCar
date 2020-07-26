package com.suprateam.car.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeQuestionDto {

    private Long id;

    private String label;
}
