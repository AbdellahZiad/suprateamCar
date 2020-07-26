package com.suprateam.car.service.impl;

import com.suprateam.car.dto.MediaDto;
import com.suprateam.car.dto.MediaTypeDto;
import com.suprateam.car.dto.MediaTypeDtoList;
import com.suprateam.car.model.Media;
import com.suprateam.car.model.SurveyUser;
import com.suprateam.car.repository.MediaRepository;
import com.suprateam.car.repository.SurveyParamRepository;
import com.suprateam.car.repository.SurveyUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MediaServiceImpl {


    @Value("${sme.application-folder}")
    private String rootFolder;

    SurveyParamRepository surveyParamRepository;

    MediaRepository mediaRepository;

    SurveyUserRepository surveyUserRepository;

    StorageService storageService;


    @Autowired
    public MediaServiceImpl(SurveyParamRepository surveyParamRepository, MediaRepository mediaRepository, SurveyUserRepository surveyUserRepository, StorageService storageService) {
        this.surveyParamRepository = surveyParamRepository;
        this.mediaRepository = mediaRepository;
        this.surveyUserRepository = surveyUserRepository;
        this.storageService = storageService;
    }

    @Transactional
    public String saveMedia( List<MultipartFile> files,Long id) {

        if (files == null) return null;

        SurveyUser surveyUser = surveyUserRepository.getOne(id);
        List<Media> mediaList = mediaRepository.findBySurveyUser(surveyUser);

        files
                .forEach(file-> storageService.storeMedia(file,getMediaTypeFromMultipartFile(file,mediaList)));


        return "OK";
    }

    private String getMediaTypeFromMultipartFile(MultipartFile file, List<Media> mediaList) {

        if (file == null || mediaList == null) return "";
        return mediaList.stream().filter(Objects::nonNull)
                .filter(media -> media.getName().trim().toLowerCase()
                        .contains(file.getOriginalFilename().trim().toLowerCase())).findFirst().get().getMediaType();
    }

    private MultipartFile getMultipartFile(MediaTypeDto mediaTypeDto, MultipartFile[] files) {
        return files != null && mediaTypeDto.getFilename() != null ?
                Arrays.asList(files)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(multipartFile ->
                                multipartFile.getOriginalFilename() != null && multipartFile
                                        .getOriginalFilename().trim()
                                        .toLowerCase().contains(mediaTypeDto.getFilename().trim().toLowerCase()))
                        .findFirst().orElse(null) : null;

    }

    private String getMediaType(String mediaType) {
        if (mediaType == null) return "Others";
        if (mediaType.toLowerCase().contains("im")) return "Images";
        if (mediaType.toLowerCase().contains("vi")) return "Videos";
        if (mediaType.toLowerCase().contains("au")) return "Audio";


        return "Others";

    }

    @Transactional
    public List<MediaDto> getAllMedias(Long id) {
        SurveyUser surveyUser = surveyUserRepository.getOne(id);

        List<Media> mediaList = mediaRepository.findBySurveyUser(surveyUser);
        List<MediaDto> mediaDtoList = new ArrayList<>();

        mediaList.stream()
                .map(media -> {
                    try {
                        return mediaDtoList.add(MediaDto
                                .builder()
                                .id(media.getId())
                                .name(media.getName())
                                .mediaType(media.getMediaType())
                                .path(media.getPath())
//                                .mediaContent(storageService.load(media.getName(), media.getMediaType()))
                                .sectionTag(media.getSectionTag()).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

        return mediaDtoList;

    }

    private String getExtension(String fileName) {
        if (fileName == null) return null;

        int index = fileName.lastIndexOf(".");
        return fileName.substring(index);
    }

    @Transactional
    public MediaDto getMediaById(Long id) throws Exception {
        Media media = mediaRepository.getOne(id);
        MediaDto mediaDto = MediaDto.builder()
                .id(media.getId())
                .mediaContent(storageService.load(media.getName(), media.getMediaType()))
                .build();
        return mediaDto;
    }
}
