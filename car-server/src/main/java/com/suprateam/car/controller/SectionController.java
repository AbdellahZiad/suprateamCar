package com.suprateam.car.controller;

import com.suprateam.car.dto.AnswersDto;
import com.suprateam.car.dto.MediaTypeDto;
import com.suprateam.car.service.impl.SectionServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/section")
public class SectionController {


    SectionServiceImpl sectionService;

    @Autowired
    public SectionController(SectionServiceImpl sectionService) {
        this.sectionService = sectionService;
    }

    @ApiOperation(value = "Get Questionnaire by surveyID and companyID")
    @GetMapping("/{companyID}")
    ResponseEntity<?> getQuestionnaire(@PathVariable String companyID) {
        return ResponseEntity.ok(sectionService.getSurveyUser(null,companyID));
    }


    @ApiOperation(value = "Save Questionnaire Answers")
    @PostMapping("")
    ResponseEntity<?> saveQuestionnaireAnswers(@RequestBody AnswersDto answersDto) {
        return ResponseEntity.ok(sectionService.saveSection(answersDto));
    }







}
