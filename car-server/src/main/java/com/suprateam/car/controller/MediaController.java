package com.suprateam.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suprateam.car.dto.MediaTypeDto;
import com.suprateam.car.dto.MediaTypeDtoList;
import com.suprateam.car.service.impl.MediaServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/media")
public class MediaController {




    MediaServiceImpl mediaService;



    @Autowired
    public MediaController(MediaServiceImpl mediaService) {
        this.mediaService = mediaService;
    }

    @ApiOperation(value = "Add Media ")
    @PostMapping(value = "/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> saveMedia(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files)throws IOException {
        return ResponseEntity.ok(mediaService.saveMedia(files,id));
    }

    
    @ApiOperation(value = "Get All Medias")
    @GetMapping("/{id}")
    public ResponseEntity<?>  getAllMedias(@PathVariable Long id)throws IOException {
        return ResponseEntity.ok(mediaService.getAllMedias(id));
    }

    @ApiOperation(value = "Get Media by id")
    @GetMapping("m/{id}")
    public ResponseEntity<?>  getMediaById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(mediaService.getMediaById(id));
    }

}
