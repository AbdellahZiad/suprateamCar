package com.suprateam.car.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MediaTypeDto {
//    private Long idSurvey;
    private String mediaType;
    private String sectionTag;
    private String filename;


//    private List<MultipartFile> multipartFileList;
}
