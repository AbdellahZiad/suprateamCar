package com.suprateam.car.service.impl;

import com.suprateam.car.dto.AnswersDto;
import com.suprateam.car.dto.SurveyDetailsDto;
import com.suprateam.car.dto.SurveyUserDto;
import com.suprateam.car.model.Answer;
import com.suprateam.car.model.SurveyParameters;
import com.suprateam.car.model.SurveyUser;
import com.suprateam.car.repository.AnswerRepository;
import com.suprateam.car.repository.MediaRepository;
import com.suprateam.car.repository.SurveyParamRepository;
import com.suprateam.car.repository.SurveyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.suprateam.car.util.SurveyUserSpecification.filterKeyword;

@Service
public class SurveyUserServiceImpl {

    SurveyParamRepository surveyParamRepository;

    SectionServiceImpl sectionService;

    SurveyUserRepository surveyUserRepository;

    AnswerRepository answerRepository;

    MediaRepository mediaRepository;

    StorageService storageService;

    @Autowired
    public SurveyUserServiceImpl(StorageService storageService, MediaRepository mediaRepository, AnswerRepository answerRepository, SurveyParamRepository surveyParamRepository, SectionServiceImpl sectionService, SurveyUserRepository surveyUserRepository) {
        this.surveyParamRepository = surveyParamRepository;
        this.sectionService = sectionService;
        this.surveyUserRepository = surveyUserRepository;
        this.answerRepository = answerRepository;
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    public List<AnswersDto> getSurveyDetails() {
        List<AnswersDto> answersDtoList = new ArrayList<>();

        List<SurveyParameters> surveyParameterList = surveyParamRepository.findAll();

        for (SurveyParameters surveyParameters : surveyParameterList) {
            if (surveyParameters.getSurveyID() != null && surveyParameters.getCompanyID() != null && surveyParameters.isActive()) {
                surveyParameters.setStatus("Referral");
                answersDtoList.add(sectionService
                        .getSurveyUser(surveyParameters.getSurveyID(), surveyParameters.getCompanyID()));

            }
        }

        return answersDtoList;
    }

    public List<SurveyParameters> getAllTemplate() {
        List<SurveyParameters> surveyParameters = surveyParamRepository
                .findAll()
                .stream()
                .filter(SurveyParameters::isActive)
                .collect(Collectors
                        .toList());

        return surveyParameters.stream()
                .sorted((f1, f2) -> Long.compare(f2.getReload(), f1.getReload())).collect(Collectors.toList());
//        return surveyParam1Repository.findAll();
    }


    @Transactional
    public String deleteSurvey(Long id) {
        surveyParamRepository.desactivateSurveyParametersById(id);
        return "Ok";
    }


    public List<SurveyUser> getAllSurveyUser() {
        return surveyUserRepository.findAll();
    }

    public String reload(Long id) {
        SurveyParameters surveyParameters = surveyParamRepository.getOne(id);
        surveyParameters.setReload(surveyParamRepository
                .findAll()
                .stream()
                .map(SurveyParameters::getReload)
                .max(Comparator.naturalOrder())
                .orElse(surveyParameters.getId()) + 1);

        surveyParamRepository.save(surveyParameters);
        return "OK";

    }

    @Transactional
    public String updateSurveyStatus(String decision, Long id) {
        SurveyUser surveyUser = surveyUserRepository.getOne(id);
        if (decision != null && decision.toLowerCase().contains("declined")) {
            surveyUser.setDeclinedReason("Outside of appetite for this product");
            surveyUser.setReferralReason("Outside of appetite for this product");
            surveyUser.setStatusApp("Declined");
            surveyUser.setStatusSystem("Declined");
            surveyUser.setDecision("Declined");
            surveyUser.setBy("UNDERWRITER");
        } else if (decision != null && decision.toLowerCase().contains("accepted")) {
            surveyUser.setDeclinedReason("");
            surveyUser.setReferralReason("");
            surveyUser.setStatusApp("Accepted");
            surveyUser.setStatusSystem("Accepted");
            surveyUser.setDecision("Accepted");
            surveyUser.setBy("UNDERWRITER");
        }
        surveyUserRepository.save(surveyUser);

        return "OK";

    }

    @Transactional
    public List<SurveyDetailsDto> getSurveyUserDetails(Long id) {

        SurveyUser surveyUser = surveyUserRepository.getOne(id);
        List<Answer> answerList = answerRepository.findBySurveyUser(surveyUser);

        return generateSurveyDetails(answerList);
//     return generateExampleSurveyDetails();

    }

    private List<SurveyDetailsDto> generateSurveyDetails(List<Answer> answerList) {

        List<SurveyDetailsDto> surveyDetailsDto = new ArrayList<>();

        List<SurveyDetailsDto> surveyDetailsDtoOutput = new ArrayList<>();

        surveyDetailsDto = getSectionFromAnswers(answerList);

        for (SurveyDetailsDto surveyD : surveyDetailsDto) {
            SurveyDetailsDto out = new SurveyDetailsDto();
            List<Answer> listAnswer = getListAnswerBySectionName(surveyD.getSectionName(), answerList);
            out.setSectionName(surveyD.getSectionName());
            out.setAnswer(listAnswer);
            surveyDetailsDtoOutput.add(out);
        }

        return surveyDetailsDtoOutput;
    }

    private List<Answer> getListAnswerBySectionName(String sectionName, List<Answer> answerList) {

        return answerList.stream().filter(Objects::nonNull).filter(answer ->
                answer.getSectionName() != null && answer.getSectionName().toLowerCase()
                        .contains(sectionName.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());

    }

    private List<SurveyDetailsDto> getSectionFromAnswers(List<Answer> answerList) {
        List<SurveyDetailsDto> surveyDetailsDto = new ArrayList<>();

        for (Answer answer : answerList) {
            if (!checkSectionIsExist(answer.getSectionName(), surveyDetailsDto)) {
                SurveyDetailsDto surveyDetailsDto1 = new SurveyDetailsDto();
                surveyDetailsDto1.setSectionName(answer.getSectionName());
                surveyDetailsDto.add(surveyDetailsDto1);
            }

        }

        return surveyDetailsDto;
    }

    private boolean checkSectionIsExist(String sectionName, List<SurveyDetailsDto> surveyDetailsDto) {
        if (surveyDetailsDto == null || surveyDetailsDto.size() == 0) return false;
        for (SurveyDetailsDto s : surveyDetailsDto) {
            if (s != null && s.getSectionName() != null && s.getSectionName().toLowerCase()
                    .contains(sectionName != null ? sectionName.toLowerCase() : "")) return true;
        }
        return false;
    }


    private List<SurveyDetailsDto> generateExampleSurveyDetails() {

        List<Answer> listAnswer3 = new ArrayList<>();
        SurveyDetailsDto surveyDetailsDto3 = new SurveyDetailsDto();
        surveyDetailsDto3.setSectionName("S3");
        Answer answer3 = new Answer();
        answer3.setTextQuestion("Q1");
        answer3.setTextAnswers("Answer1");
        listAnswer3.add(answer3);
        surveyDetailsDto3.setAnswer(listAnswer3);


        List<Answer> listAnswer = new ArrayList<>();
        SurveyDetailsDto surveyDetailsDto = new SurveyDetailsDto();
        surveyDetailsDto.setSectionName("Company Information");
        Answer answer = new Answer();
        answer.setTextQuestion("Company Name");
        answer.setTextAnswers("Abdel");
        listAnswer.add(answer);
        surveyDetailsDto.setAnswer(listAnswer);


        List<Answer> listAnswer2 = new ArrayList<>();
        SurveyDetailsDto surveyDetailsDto2 = new SurveyDetailsDto();
        surveyDetailsDto2.setSectionName("Risk And Guarantes");
        Answer answer2 = new Answer();
        answer2.setTextQuestion("Location of the risk ");
        answer2.setTextAnswers("Avenue Hassan 2 Azilal");
        listAnswer2.add(answer2);
        surveyDetailsDto2.setAnswer(listAnswer2);


        List<SurveyDetailsDto> detailsDto = new ArrayList<>();
        detailsDto.add(surveyDetailsDto);
        detailsDto.add(surveyDetailsDto2);
        detailsDto.add(surveyDetailsDto3);

        return detailsDto;


    }

    public Page<SurveyUser> filterSurveyUser(Pageable pageable, SurveyUserDto filter) {
        return doFilter(filter, pageable);

    }

    private Page<SurveyUser> doFilter(SurveyUserDto filter, Pageable pageable) {
        if (filter != null)
            return surveyUserRepository.findAll(filterKeyword(filter), pageable);
        else
            return surveyUserRepository.findAll(pageable);

    }


}
