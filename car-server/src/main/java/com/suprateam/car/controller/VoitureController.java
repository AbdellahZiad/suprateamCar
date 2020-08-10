package com.suprateam.car.controller;

import com.suprateam.car.model.Voiture;
import com.suprateam.car.service.impl.VoitureServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voiture")
public class VoitureController {


    @Autowired
    private VoitureServiceImpl voitureService;


    @ApiOperation(value = "Filter data")
    @GetMapping("/search")
    public ResponseEntity<?> filterSurveyVoitureMobil(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC, page = 0) Pageable pageable,
                                                      @RequestParam String filter) {
        return ResponseEntity.ok(voitureService.filter(pageable, filter));
    }

    @ApiOperation(value = "Add Voiture")
    @PostMapping("/add")
    ResponseEntity<?> addVoiture(@RequestBody Voiture VoitureDto) {
        return ResponseEntity.ok(voitureService.saveVoiture(VoitureDto));
    }


    @ApiOperation(value = "Delete Voiture")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteVoiture(@PathVariable Long id) {
        return new ResponseEntity<String>(voitureService.deleteVoiture(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All Voiture")
    @GetMapping("")
    ResponseEntity<?> getAllVoiture() {
        return ResponseEntity.ok(voitureService.getAllVoiture());
    }


}
