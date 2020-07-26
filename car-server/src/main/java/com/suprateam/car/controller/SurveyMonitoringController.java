package com.suprateam.car.controller;


import com.suprateam.car.service.impl.SurveyUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class SurveyMonitoringController {


     SurveyUserServiceImpl surveyUserService;

     @Autowired
    public SurveyMonitoringController(SurveyUserServiceImpl surveyUserService) {
        this.surveyUserService = surveyUserService;
    }

    @GetMapping("details/{idSurvey}")
    public ResponseEntity<?> getDetailsSurveyMonitory(@PathVariable Long idSurvey)
    {
        return ResponseEntity.ok(surveyUserService.getSurveyUserDetails(idSurvey));
    }

}
