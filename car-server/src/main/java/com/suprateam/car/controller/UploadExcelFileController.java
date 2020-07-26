package com.suprateam.car.controller;


import com.suprateam.car.service.impl.UploadSurveyFromFileExcel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadExcelFileController {


    UploadSurveyFromFileExcel uploadFileExcel;

    @Autowired
    public UploadExcelFileController(UploadSurveyFromFileExcel uploadFileExcel) {
        this.uploadFileExcel = uploadFileExcel;
    }

    @ApiOperation(value = "Upload File")
    @PostMapping("/upload/{loadedBy}")
    ResponseEntity<?> uploadFileExcel(@RequestParam("file") MultipartFile reapExcelDataFile,@PathVariable String loadedBy) throws Exception {
        return ResponseEntity.ok(uploadFileExcel.uploadFileExcel(reapExcelDataFile,loadedBy));
    }


    @ApiOperation(value = "get Uploaded File ")
    @GetMapping("export/{fileId}")
    ResponseEntity<?> getUploadedFile(@PathVariable Long fileId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String filename = uploadFileExcel.getFileName(fileId);
        headers.add("Content-Disposition", "attachment; filename="+filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(uploadFileExcel.getUploadedFile(fileId));

//        return ResponseEntity.ok(uploadFileExcel.getUploadedFile(docId));


    }


}
