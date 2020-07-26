package com.suprateam.car.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MediaTypeDtoList {
    private Long idSurvey;
    private List<MediaTypeDto> mediaTypeDto;
//    private List<MultipartFile> files;
}
