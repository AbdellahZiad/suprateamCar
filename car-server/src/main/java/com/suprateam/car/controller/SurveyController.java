package com.suprateam.car.controller;

import com.suprateam.car.dto.SurveyUserDto;
import com.suprateam.car.model.SurveyUser;
import com.suprateam.car.service.impl.SurveyUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {


    SurveyUserServiceImpl surveyUserService;

    @Autowired
    public SurveyController(SurveyUserServiceImpl surveyUserService) {
        this.surveyUserService = surveyUserService;
    }



    @ApiOperation(value = "Update status Survey")
    @GetMapping("/filter")
    public ResponseEntity<?>  filterSurveyUser(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC, page = 0) Pageable pageable,
                                               @RequestBody SurveyUserDto filter) {
        return ResponseEntity.ok(surveyUserService.filterSurveyUser(pageable, filter));
    }

    @ApiOperation(value = "Get All Survey")
    @GetMapping("")
    ResponseEntity<?> getAllSurveyDetails() {
        return ResponseEntity.ok(surveyUserService.getSurveyDetails());
    }

    @ApiOperation(value = "Get All Template")
    @GetMapping("/template")
    ResponseEntity<?> getAllTemplate() {
        return ResponseEntity.ok(surveyUserService.getAllTemplate());
    }

    @ApiOperation(value = "Get All Survey User")
    @GetMapping("/surveys")
    ResponseEntity<?> getAllSurveyUser() {
        return ResponseEntity.ok(surveyUserService.getAllSurveyUser());
    }

    @ApiOperation(value = "Delete Survey")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        return new ResponseEntity<String>(surveyUserService.deleteSurvey(id), HttpStatus.OK);

    }

    @ApiOperation(value = "Delete Survey")
    @GetMapping("/reload/{id}")
    ResponseEntity<?> reloadSurvey(@PathVariable Long id) {
        return new ResponseEntity<String>(surveyUserService.reload(id), HttpStatus.OK);

    }

    @ApiOperation(value = "Update status Survey")
    @GetMapping("/update/{status}/{id}")
    public ResponseEntity<?>  updateSurveyStatus(
            @PathVariable String status,@PathVariable Long id)throws IOException {
        return ResponseEntity.ok(surveyUserService.updateSurveyStatus(status,id));
    }



}
