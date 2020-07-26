package com.suprateam.car.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Lob;

@Data
@Builder
public class MediaDto {

    private Long id;

    private String path;

    private String name;

    private String mediaType;

    private String sectionTag;

    @Lob
    private byte[] mediaContent;
}
