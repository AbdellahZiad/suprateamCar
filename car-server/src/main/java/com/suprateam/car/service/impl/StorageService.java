//package com.suprateam.car.service.impl;
//
//import com.suprateam.car.dto.FileInfo;
//import com.suprateam.car.exception.APIException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.compress.utils.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.PostConstruct;
//import java.io.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Random;
//import java.util.UUID;
//
//@Service
//@Slf4j
//public class StorageService {
//
//
//    @Value("${sme.application-folder}")
//    private String rootFolder;
//
//    SurveyParamRepository surveyParamRepository;
//
//
//    @PostConstruct
//    public void initIt() {
//        log.info("Create sub folders");
//        String[] subFolders = {"Inputs", "Exports", "Images", "Videos", "Record", "Others"};
//        for (String subfolder : subFolders) {
//            File file = new File(rootFolder + '/' + subfolder);
//            if (!file.exists())
//                file.mkdir();
//        }
//    }
//
//    MediaRepository mediaRepository;
//
//    @Autowired
//    public StorageService(MediaRepository mediaRepository, SurveyParamRepository surveyParamRepository) {
//        this.mediaRepository = mediaRepository;
//        this.surveyParamRepository = surveyParamRepository;
//    }
//
//    FileInfo store(MultipartFile multipartFile, String subFolder) throws Exception {
//
//        if (multipartFile == null) return null;
//        String originalFileName = multipartFile.getOriginalFilename();
//        log.info("Receive uploaded file : {0}"+ originalFileName);
////        String extension = getExtension(originalFileName);
//        String filename;
//
//        assert originalFileName != null;
//        Random r = new Random();
//        int result = r.nextInt(1000);
//        filename = generateFilename(originalFileName);
//        filename = filename.replaceAll(" ", "_");
//
//        if (surveyParamRepository.existsByTemplateNameIgnoreCase(filename))
//            throw new APIException(
//                            "Template Name = "+ filename + " is already exist");
//        try (
//                InputStream inputStream = multipartFile.getInputStream();
//                OutputStream outputStream = new FileOutputStream(new File(
//                        rootFolder + '/' + subFolder + '/' + filename))
//        ) {
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//        }
//
//        return new FileInfo(filename);
//
//    }
//
//    private String generateFilename(String originalFileName) {
//        originalFileName = originalFileName.replaceAll(" ","_");
//        DateTimeFormatter newPattern2 =  DateTimeFormatter.ofPattern("dd_MM_yyyy_ss");
//
//        return originalFileName.substring(0,originalFileName.indexOf('.')) +"_"+LocalDateTime.now().format(newPattern2)+".xlsx";
//    }
//
//
//    private String getExtension(String fileName) {
//        if (fileName == null) return null;
//
//        int index = fileName.lastIndexOf(".");
//        return fileName.substring(index);
//    }
//
//    byte[] load(String fileName,String source) throws Exception {
//        try (InputStream inputStream = new FileInputStream(getFile(fileName,source))) {
//            return IOUtils.toByteArray(inputStream);
//        }
//    }
//
//    File getFile(String fileName,String source) {
//        return new File(rootFolder +getPathFile(source) + "/"+ fileName);
//    }
//
//    private String generateFileSystem(String originalFileName) {
//        originalFileName = originalFileName.replaceAll(" ","_");
//        DateTimeFormatter newPattern2 =  DateTimeFormatter.ofPattern("dd_MM_yyyy_ss");
//
//        return LocalDateTime.now().format(newPattern2) + originalFileName;
//    }
//    public String storeMedia(MultipartFile file, String mediaType) {
//        try {
//            // Vérification de l'existence :
//            if (file.isEmpty()) {
//                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
//            }
//            // Vérification de la nature et traitement du fichier uploadé :
////            String ext = getExtension(file.getOriginalFilename());
////            String[] extAutorise = {".mp4", ".avi", ".ogg", ".ogv", ".jpg", ".jpeg", ".png", ".gif", ".mp3"};
//            String fileNameTarget = "";
////            if (ext != null && ArrayUtils.contains(extAutorise, ext.toLowerCase())) {
//
//
//            String path = getPathFile(mediaType);
//            //Définir le fichier destination :
//            fileNameTarget = file.getOriginalFilename();
////            fileNameTarget = generateFileSystem(fileNameTarget);
//
////                File dir = storageLocation.toFile();
//            String serverFile = rootFolder + path + File.separator + fileNameTarget;
//            try {
//                try (InputStream is = file.getInputStream();
//                     BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))
//                ) {
//                    int i;
//                    while ((i = is.read()) != -1) {
//                        stream.write(i);
//                    }
//                    stream.flush();
//                }
//            } catch (IOException e) {
//                System.out.println("error : " + e.getMessage());
//            }
////            }
//            return fileNameTarget;
//        } catch (Exception e) {
//            throw new RuntimeException("FAIL!");
//        }
//    }
//
//    private String renameFile(String fileNameTarget) {
//        if (mediaRepository.existsByName(fileNameTarget))
//            fileNameTarget = getNameFileNotExistInDB(fileNameTarget, true);
//        fileNameTarget = UUID.randomUUID() + " " + fileNameTarget;
//        fileNameTarget = fileNameTarget.replaceAll(" ", "_");
//        return fileNameTarget;
//
//    }
//
//    private String getNameFileNotExistInDB(String fileNameTarget, boolean check) {
//        Random r = new Random();
//        while (check) {
//            if (mediaRepository.existsByName(fileNameTarget)) {
//                int result = r.nextInt(100);
//                fileNameTarget = result + fileNameTarget;
//                check = true;
//            } else
//                break;
//
//        }
//
//        return fileNameTarget;
//    }
//
//    private String getPathFile(String mediaType) {
//        if (mediaType == null) return "/Others";
//
//        if (mediaType.toLowerCase().contains("inp")) return "/Inputs";
//        if (mediaType.toLowerCase().contains("im")) return "/Images";
//        if (mediaType.toLowerCase().contains("vi")) return "/Videos";
//        if (mediaType.toLowerCase().contains("au")) return "/Record";
//
//
//        return "/Others";
//    }
//
//}
