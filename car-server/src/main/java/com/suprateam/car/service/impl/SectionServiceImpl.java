package com.suprateam.car.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suprateam.car.constants.NATCATInputs;
import com.suprateam.car.constants.QuestionConstant;
import com.suprateam.car.constants.SectionConstant;
import com.suprateam.car.dto.*;
import com.suprateam.car.model.*;
import com.suprateam.car.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.synth.SynthMenuUI;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SectionServiceImpl {

    SectionRepository sectionRepository;

    AnswerRepository answerRepository;

    CompanyRepository companyRepository;

    UserRepository userRepository;

    SurveyParamRepository surveyParamRepository;

    SurveyUserRepository surveyUserRepository;

    OccupancyRepository occupancyRepository;

    RatesRepository ratesRepository;

    ClientRepository clientRepository;

    MediaRepository mediaRepository;

    StorageService storageService;

    ProposalAnswerRepository proposalAnswerRepository;


    @Autowired
    public SectionServiceImpl(ProposalAnswerRepository proposalAnswerRepository, StorageService storageService, MediaRepository mediaRepository, SectionRepository sectionRepository, AnswerRepository answerRepository, CompanyRepository companyRepository, UserRepository userRepository, SurveyParamRepository surveyParamRepository, SurveyUserRepository surveyUserRepository, OccupancyRepository occupancyRepository, RatesRepository ratesRepository, ClientRepository clientRepository) {
        this.sectionRepository = sectionRepository;
        this.storageService = storageService;
        this.mediaRepository = mediaRepository;
        this.answerRepository = answerRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.surveyParamRepository = surveyParamRepository;
        this.surveyUserRepository = surveyUserRepository;
        this.occupancyRepository = occupancyRepository;
        this.ratesRepository = ratesRepository;
        this.clientRepository = clientRepository;
        this.proposalAnswerRepository = proposalAnswerRepository;
    }

    @Transactional
    public SurveyUser saveSection(AnswersDto answersDto) {

        log.info("--------------------- answerDTO" + answersDto);

        if (answersDto == null || answersDto.getSectionDtoList().size() == 0) return null;

        String email = getCurrentUserLogged();

        SmeUser smeUser = userRepository.findByEmailIgnoreCase(email);

        String companyID = getCompanyName(smeUser);

        String surveyID = getSurveyIDTest(companyID);

        List<String> natCatQ = new ArrayList<>();

        Map<String, SectionDto> sList = formatSection(answersDto.getSectionDtoList());

        List<ProposalAnswerDto> answerList = new ArrayList<>();

//        Company company = saveCompany(sList.get(SectionConstant.S1),answerList);
        Company company = companyRepository.findFirstByCompanyID(companyID);

        SurveyParameters surveyParameters = surveyParamRepository.findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCaseAndActiveTrue(surveyID, companyID);


        SurveyUser surveyUser = new SurveyUser();
        surveyUser.setSurveyID(surveyID);
        surveyUser.setCompanyID(companyID);

        surveyUser = surveyUserRepository.save(surveyUser);

        Client client = new Client();

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), QuestionConstant.S2Q1));
        client.setName(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), QuestionConstant.S2Q1, surveyUser).getTextAnswers());

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), "Company Seniority"));
        client.setAnnualRevenue(isNumber(getQuestionAnsweredByKey(sList.get(SectionConstant.S1),
                "Company Seniority", surveyUser).getTextAnswers()));

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), "Financial"));
        client.setFinancialStability(getQuestionAnsweredByKey(sList.get(SectionConstant.S1),
                "Financial", surveyUser).getTextAnswers());

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), "Number of employees"));
        client.setEmployeesNumber(isNumber(getQuestionAnsweredByKey(sList.get(SectionConstant.S1),
                "Number of employees", surveyUser).getTextAnswers()));

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S1), "Number of years in business"));
        client.setNumberOfYearsInBusiness(isNumber(getQuestionAnsweredByKey(sList.get(SectionConstant.S1)
                , "Number of years in business", surveyUser).getTextAnswers()));

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S2),"risk and"));
        client.setSumInsured(calculiSumInsuredTotal(sList.get(SectionConstant.S2), surveyUser));

        Client saveClient = clientRepository.save(client);

        Occupancy occupancy = saveOccupancy(sList.get(SectionConstant.S4), surveyUser);

        surveyUser.setUser(smeUser);
        surveyUser.setDateCreate(new Date());
        surveyUser.setDateTreatment(new Date());
        surveyUser.setSurveyID(surveyID);
        surveyUser.setCompanyID(companyID);

//        surveyUser.setSurveyParameters(surveyParameters);

        surveyUser.setBy("SYSTEM");
        surveyUser.setCompanyName(company.getName());
        surveyUser.setClient(saveClient);
        //hh
        surveyUser.setRiskLocation(saveRiskOfTheRisk(sList.get(SectionConstant.S2), surveyUser));
        if (occupancy != null)
            surveyUser.setOccupancy(occupancy.getUsTitle());

        surveyUser.setSumInsuredTotal(calculiSumInsuredTotal(sList.get(SectionConstant.S2), surveyUser));
        surveyUser.setQualityScore(calculiQualityScore(sList, companyID, surveyID, surveyUser));

        if (occupancy != null)
            surveyUser.setDecisionFLEXA(getDecisionFLEXA(sList, occupancy, surveyParameters, surveyUser));
        surveyUser.setDecisionNATCAT(getDecisionNATCAT(sList, occupancy, surveyUser, natCatQ));
        surveyUser.setDecision(getDecision(surveyUser.getDecisionFLEXA(), surveyUser.getDecisionNATCAT()));

//        listAnswers.add(getQuestionAnsweredByKey(sList.get(SectionConstant.S2), QuestionConstant.S3Q2));
        String zipCode = getQuestionAnsweredByKey(sList.get(SectionConstant.S2), QuestionConstant.S3Q2, surveyUser).getTextAnswers();
        surveyUser.setRiskRegion(zipCode);
        if (occupancy != null)
            surveyUser.setPricingRateFLEXA(getPricingRateFLEXA(surveyUser, occupancy, zipCode));

//        surveyUser.setScoringDiscountLoadingFLEXA(getScoringDiscountLoadingFLEXA(sList));
        surveyUser.setPricingRateNATCAT(getPricingRateNATCAT(occupancy, zipCode, surveyUser.getDecision(), natCatQ));
        surveyUser.setLoadingOnYearsBusiness(getNumberYearsOfBusiness(client.getNumberOfYearsInBusiness(), surveyUser.getDecision()));

        surveyUser.setDiscountLoading(calculiDiscountLoading(sList, companyID, surveyID, surveyUser));

        surveyUser.setFinalPrice(getFinalPrice(surveyUser));

        surveyUser.setStatusSystem(surveyUser.getDecision());

        if (smeUser != null)
            surveyUser.setAgent(smeUser.getFullName());

        if (occupancy != null)
            surveyUser.setReferralReason(getReferralReason(surveyUser, occupancy.getCapacityLimit()));
        surveyUser.setDeclinedReason(getDeclinedReason(surveyUser.getDecision(), occupancy));

        surveyUser.setSurveyParameters(surveyParameters);
//        answerRepository.saveAll(listAnswers);
//        surveyUser.setAnswerList(listAnswers);

        SurveyUser saveSurveyUser = surveyUserRepository.save(surveyUser);


//        List<Answer> listAnswers = listAnswers.stream().filter(Objects::nonNull)
//                .map(answer -> {
//                    answer.setSurveyUser(saveSurveyUser);
//                    return answer;
//                }).collect(Collectors.toList());
        surveyParameters.getSurveyUserList().add(saveSurveyUser);

//        answerRepository.saveAll(answerList1);

        surveyUser.setStatusApp(surveyUser.getDecision());
        surveyParameters.setSurveyID(surveyID);

        surveyParamRepository.save(surveyParameters);

        saveMedia(answersDto.getMediaTypeDtoList(),surveyUser);
        surveyUser = surveyUserRepository.save(surveyUser);
        return surveyUser;
    }

    public String saveMedia(List<MediaTypeDto> mediaTypeDtoList, SurveyUser surveyUser) {

        if (mediaTypeDtoList == null) return null;

        for (MediaTypeDto mediaTypeDto : mediaTypeDtoList) {
            Media media = new Media();
            String fileName = mediaTypeDto.getFilename();
            media.setExtension(getExtension(fileName));
            media.setMediaType(getMediaType(mediaTypeDto.getMediaType()));
            media.setName(fileName);
            media.setSectionTag(mediaTypeDto.getSectionTag());
            media.setPath(fileName);
            media.setSurveyUser(surveyUser);
            mediaRepository.save(media);
        }


        return "OK";
    }

    private String getExtension(String fileName) {
        if (fileName == null) return null;

        int index = fileName.lastIndexOf(".");
        return fileName.substring(index);
    }
    private Double getDiscountLoading(Map<String, SectionDto> sList) {

        Random r = new Random();

        return r.nextInt(400 - 70) + 70 + 0D;

    }
    private String getMediaType(String mediaType) {
        if (mediaType == null) return "Others";
        if (mediaType.toLowerCase().contains("im")) return "Images";
        if (mediaType.toLowerCase().contains("vi")) return "Videos";
        if (mediaType.toLowerCase().contains("au")) return "Audio";


        return "Others";

    }
    private String getSurveyIDTest(String companyID) {
        return getSurveyID(sectionRepository.findByLastSurveyTrueAndCompanyIDIgnoreCase(companyID));
    }

    private String getDeclinedReason(String decision, Occupancy occupancy) {
        return "Outside of appetite for this product";
    }

    private boolean isHighRisk(String riskAssessment) {
        return riskAssessment != null && riskAssessment.toLowerCase().contains("high");
    }

    private String getReferralReason(SurveyUser surveyUser, Double capacityLimit) {

        return "Further information required, refer to underwriter";
    }

    private boolean isReferral(String decision) {
        return decision != null && decision.toLowerCase().contains("ref");
    }

    private String getFinalPrice(SurveyUser surveyUser) {

        if (isAccepted(surveyUser.getDecision())) {
            Double pricingRateFLEXA = surveyUser.getPricingRateFLEXA();
            String pricingRateNATCAT = surveyUser.getPricingRateNATCAT();
            Double scoringDiscountLoadingFLEXA = surveyUser.getDiscountLoading();
            String numberYearsOfBusiness = surveyUser.getLoadingOnYearsBusiness();
            if (
                    isNumberStr(pricingRateNATCAT) &&
                            isNumberStr(String.valueOf(scoringDiscountLoadingFLEXA)) &&
                            isNumberStr(numberYearsOfBusiness))

//                  [(Pricing Rate FLEXA value) * (1+ Scoring Discount/Loading FLEXA)+
//                    Pricing Rate NAT CAT ] * (SUM Insured Total) * (1+ Loading less 3 years business value)

                return ((isNumber(pricingRateFLEXA) * (1 + isNumber(scoringDiscountLoadingFLEXA)) + isNumber(pricingRateNATCAT)) * surveyUser.getSumInsuredTotal())
                        * (1 + getPercent(numberYearsOfBusiness)) + "";

        }

        return "Not Applicable";

    }

    private Double getPercent(String numberYearsOfBusiness) {
        return numberYearsOfBusiness != null ? Double.parseDouble(deletePercentageChar(numberYearsOfBusiness)) / 100 : 0D;
    }

    private String getNumberYearsOfBusiness(Double numberOfYearsInBusiness, String decision) {
        if (isAccepted(decision) && numberOfYearsInBusiness != null) {
            if (numberOfYearsInBusiness <= 1) return "15%";
            if (numberOfYearsInBusiness <= 2) return "10%";
            if (numberOfYearsInBusiness <= 3) return "5%";
            if (numberOfYearsInBusiness > 3) return "0%";
        }
        return "Not Applicable";

    }

    private String getPricingRateNATCAT(Occupancy occupancy, String zipCode, String decision, List<String> natCatQ) {

        if (!isAccepted(decision)) return "Not Applicable";
        String natCatWS = natCatQ.get(0);
        String natCatFLOOD = natCatQ.get(1);
        String natCarEQ = natCatQ.get(2);

        if (!isNotValid(natCatWS) || !isNotValid(natCatFLOOD) || !isNotValid(natCarEQ)) {
            double sum = 0D;
            List<Rates> ratesList = getRatesByZipCode(occupancy, zipCode);
            for (Rates rates : ratesList) {
                if (isNatCatWS(rates.getNatCatCode()) && !isNotValid(natCatWS))
                    sum = sum + rates.getRate();
                else if (isNatCatFLOOD(rates.getNatCatCode()) && !isNotValid(natCatFLOOD))
                    sum = sum + rates.getRate();
                else if (isNatCatEQ(rates.getNatCatCode()) && !isNotValid(natCarEQ))
                    sum = sum + rates.getRate();
            }
            return sum + "";
        }
        return "0";
    }

    private boolean isNatCatWS(String ws) {
        return ws!=null && ws.toLowerCase().contains("ws");
    }

    private boolean isNatCatFLOOD(String flood) {
        return flood!=null && flood.toLowerCase().contains("flood");
    }


    private boolean isNatCatEQ(String eq) {
        return eq!=null && eq.toLowerCase().contains("eq");
    }


    private boolean isNotApplicable(String pricingRateFLEXA) {
        return pricingRateFLEXA != null && pricingRateFLEXA.toLowerCase().contains("not app");
    }


    private Double getPricingRateFLEXA(SurveyUser surveyUser, Occupancy occupancy, String zipCode) {
        if (isAccepted(surveyUser.getDecision()))
            return isNumber(occupancy.getBaseRate());

        return null;
    }

    private List<Rates> getRatesByZipCode(Occupancy occupancy, String zipCode) {
        if (occupancy != null && occupancy.getRatesList() != null && zipCode != null)
            return occupancy
                    .getRatesList()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(rates ->
                            rates.getRiskLocationAndNatCatType() != null &&
                                    rates.getRiskLocationAndNatCatType().getZipCode().toLowerCase().contains(zipCode.toLowerCase())
                                    && rates.getTypeOfOccupancy().toLowerCase().trim().contains(occupancy.getTypeOfOccupancy().toLowerCase().trim())
                    )
                    .collect(Collectors.toList());

        return new ArrayList<>();
    }

    private boolean isAccepted(String decision) {
//        return decision!=null&& decision.toLowerCase().contains("acc");
        return true;
    }

    private String getDecision(String decisionFLEXA, String decisionNATCAT) {
        if (isDeclined(decisionFLEXA)) return "Declined";

        else if (decisionFLEXA != null && decisionFLEXA.toLowerCase().contains("accepted") &&
                (decisionNATCAT != null && (decisionNATCAT.toLowerCase().contains("not req") ||
                        decisionNATCAT.toLowerCase().contains("accepted"))))
            return "Accepted";
        return "Referral";
    }

    private String getDecisionNATCAT(Map<String, SectionDto> sList, Occupancy occupancy, SurveyUser surveyUser, List<String> natCatQ) {


        SectionDto sectionRiskAndG = sList.get(SectionConstant.S2);
        String natCatWS = getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q4, surveyUser).getTextAnswers();
        String natCatFLOOD = getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q5, surveyUser).getTextAnswers();
        String natCarEQ = getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q6, surveyUser).getTextAnswers();

        natCatQ.add(natCatWS);
        natCatQ.add(natCatFLOOD);
        natCatQ.add(natCarEQ);
//
//        String zipCode = getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q2).getTextAnswers();
//
//        List<Rates> ratesList = occupancy.getRatesList();
//
//        Rates rates = getRateByZipCode(ratesList,zipCode);
//        if (rates == null) return "Not Requested";
//
        String natCatInputWS = "No";
        String natCatInputFLOOD = "No";
        String natCatInputEQ = "No";

        if (checkNATCATAdditionalIsNO(sectionRiskAndG, surveyUser))
            return "Not Requested";
        if (
                (isNotValid(natCatWS) || isNotValid(natCatInputWS)) &&
                        (isNotValid(natCatFLOOD) || isNotValid(natCatInputFLOOD)) &&
                        (isNotValid(natCarEQ) || isNotValid(natCatInputEQ))
        )
            return "Accepted";


        return "Not Requested";


    }

    private Rates getRateByZipCode(List<Rates> ratesList, String zipCode) {
        if (ratesList == null || zipCode == null) return null;
        return ratesList.stream().filter(Objects::nonNull).filter(rates -> rates.getZipCodeID() != null &&
                rates.getZipCodeID().toLowerCase().contains(zipCode.toLowerCase())).findFirst().orElse(null);
    }

    private boolean isNotValid(String natCatWS) {
        return natCatWS != null && natCatWS.toLowerCase().contains("n");
    }

    private List<NATCATInputs> getStaticNATCAT(String zipCode) {

        List<NATCATInputs> inputsList = new ArrayList<>();

        inputsList.add(new NATCATInputs(zipCode, "NO", "WS"));
        inputsList.add(new NATCATInputs(zipCode, "NO", "FLOOD"));
        inputsList.add(new NATCATInputs(zipCode, "NO", "EQ"));
        return inputsList;
    }

    private String getDecisionFLEXA(Map<String, SectionDto> sList,
                                    Occupancy occupancy,
                                    SurveyParameters surveyParameters,
                                    SurveyUser surveyUser
    ) {

        String rAssessment = occupancy.getRiskAssessment();
        Double cLimit = occupancy.getCapacityLimit();
        Double thresholdMin = surveyParameters.getThresholdMin();
        Double thresholdMax = surveyParameters.getThresholdMax();
        Double qualityScore = surveyUser.getQualityScore();

        List<String> listSensitiveFactory = getListSensitiveFactor(sList, surveyUser.getSurveyID(), surveyUser.getCompanyID(), surveyUser);

        if ((rAssessment.toLowerCase().contains("high risk") && cLimit == 0) || surveyUser.getQualityScore() < thresholdMin ||
                isDeclined(listSensitiveFactory))
            return "Declined";
        else if (surveyUser.getSumInsuredTotal() > cLimit) return "Referral";
        else if ((thresholdMin <= qualityScore && qualityScore <= thresholdMax)
                || isReferral(listSensitiveFactory))
            return "Referral";

        return "Accepted";
    }

    private boolean isReferral(List<String> listSensitiveFactory) {
        return listSensitiveFactory.stream().anyMatch(sen -> sen != null && sen.toLowerCase().contains("referral"));
    }

    private boolean isDeclined(List<String> sList) {
        return sList.stream().anyMatch(sen -> sen != null && sen.toLowerCase().contains("declined"));
    }

    private boolean isDeclined(String status) {
        return status != null && status.toLowerCase().contains("declined");
    }

    private List<String> getListSensitiveFactor(Map<String, SectionDto> sList, String surveyID, String companyID, SurveyUser surveyUser) {


        List<String> listSensitiveFactor = new ArrayList<>();

        SectionDto qConstruction = sList.get(SectionConstant.S5);


        listSensitiveFactor.add(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q1, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q2, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q3, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q4, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q5, surveyUser,true).getSensitiveFactor());

        SectionDto qPreventionProtection = sList.get(SectionConstant.S6);

        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q1, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q2, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q3, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q4, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q5, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q6, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q7, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q8, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q9, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q10, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q11, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q12, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q13, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q14, surveyUser,true).getSensitiveFactor());


        SectionDto qExposure = sList.get(SectionConstant.S7);

        listSensitiveFactor.add(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q1, surveyUser,true).getSensitiveFactor());
        listSensitiveFactor.add(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q2, surveyUser,true).getSensitiveFactor());


        return listSensitiveFactor;
    }


    private boolean checkNATCATAdditionalIsNO(SectionDto sectionRiskAndG, SurveyUser surveyUser) {
        return
                getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q4, surveyUser).getTextAnswers().toLowerCase().contains("n") &&
                        getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q5, surveyUser).getTextAnswers().toLowerCase().contains("n") &&
                        getQuestionAnsweredByKey(sectionRiskAndG, QuestionConstant.S3Q6, surveyUser).getTextAnswers().toLowerCase().contains("n");


    }


//    private String getSensitiveFactorFromPAnswer(String answerText) {
//        ProposalAnswer proposalAnswer = proposalAnswerRepository.findFirstByTextAnswerIgnoreCase(answerText);
//        return proposalAnswer != null ? proposalAnswer.getSectionID() : "";
//    }

    private String checkDecision(Map<String, SectionDto> sList) {

        return "Accepted";
    }

    private Double calculiQualityScore(Map<String, SectionDto> sList, String companyID, String surveyID, SurveyUser surveyUser) {

//        List<ProposalAnswer> proposalAnswerList = proposalAnswerRepository
//                .findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCase(surveyID,companyID);

        SectionDto qConstruction = sList.get(SectionConstant.S5);
        Double sumScoreSQConstruction = sum(
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q1, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q2, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q3, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q4, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q5, surveyUser).getScore()));

        SectionDto qPreventionProtection = sList.get(SectionConstant.S6);
        Double sumScorePreventionProtection = sum(
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q1, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q2, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q3, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q4, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q5, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q6, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q7, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q8, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q9, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q10, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q11, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q12, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q13, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q14, surveyUser).getScore()));


        SectionDto qExposure = sList.get(SectionConstant.S7);
        Double sumScoreSQExposure = sum(
                isNumber(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q1, surveyUser).getScore()),
                isNumber(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q2, surveyUser).getScore()));
        return sum(sumScoreSQConstruction, sumScorePreventionProtection, sumScoreSQExposure);
    }

//    private Double getScoreFromProposalAnswerList(String textAnswers, String sectionID, String answerId) {
//        if (textAnswers == null || sectionID == null || answerId == null) return 0.0;
//        ProposalAnswer proposalAnswer = proposalAnswerRepository.findFirstByTextAnswerIgnoreCaseAndSectionIDIgnoreCaseAndAnswerIDIgnoreCase(textAnswers, sectionID, answerId);
//        return proposalAnswer != null && proposalAnswer.getScore() != null ? proposalAnswer.getScore() : 0.0;
//    }

    private String saveRiskOfTheRisk(SectionDto sectionDto, SurveyUser surveyUser) {

//        listAnswers.add(getQuestionAnsweredByKey(sectionDto, QuestionConstant.S3Q1));
        return getQuestionAnsweredByKey(sectionDto, QuestionConstant.S3Q1, surveyUser).getTextAnswers();
    }

    private Occupancy saveOccupancy(SectionDto sectionOcp, SurveyUser surveyUser) {

//        listAnswers.add(getQuestionAnsweredByKey(sectionOcp, QuestionConstant.S5Q1));
        String ocpAnswer = getQuestionAnsweredByKey(sectionOcp, QuestionConstant.S5Q1, surveyUser).getTextAnswers();

        return occupancyRepository.findFirstByUsTitleIgnoreCase(ocpAnswer);

    }

    private String getSurveyIDFromSection(AnswersDto answersDto) {
        return answersDto.getSurveyID();
    }

    private SmeUser getCurrentAgent() {
       String currentEmailLogged = getCurrentUserLogged();
//        SmeUser smeUser = ;

        return userRepository.findByEmailIgnoreCase(currentEmailLogged);

    }

    private String getCurrentUserLogged() {

        if (!isSecurityContextHolderNotNull()) return "";

        return "";
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return  ((UserDetails)principal).getUsername();
//        } else {
//            return principal.toString();
//        }


//       return isSecurityContextHolderNotNull()? ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername():"";
    }

    private boolean isSecurityContextHolderNotNull() {
//        return SecurityContextHolder.getContext()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null;
        return true;
    }


    private Double calculiSumInsuredTotal(SectionDto sectionRiskAnd, SurveyUser surveyUser) {
//        answerList.add(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Building"));
//        answerList.add(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Plant"));
//        answerList.add(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Stocks"));
        return sum(
                isNumber(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Building", surveyUser).getTextAnswers()),
                isNumber(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Plant", surveyUser).getTextAnswers()),
                isNumber(getQuestionAnsweredByKey(sectionRiskAnd, "Sum Insured: Stocks", surveyUser).getTextAnswers()));
    }

    private Answer getQuestionAnsweredByKey(SectionDto sList, String questionID, SurveyUser surveyUser) {

        for (QuestionDto questionDto : sList.getQuestionList()) {
            if (questionDto.getText().toLowerCase().contains(questionID.toLowerCase())) {
                return formatAnswerFromQDto(questionDto, sList.getName(), surveyUser,false);
            }
        }
        return new Answer();
    }

    private Answer getQuestionAnsweredByKey(SectionDto sList, String questionID, SurveyUser surveyUser,boolean noSaveAnswer) {

        for (QuestionDto questionDto : sList.getQuestionList()) {
            if (questionDto.getText().toLowerCase().contains(questionID.toLowerCase())) {
                return formatAnswerFromQDto(questionDto, sList.getName(), surveyUser,noSaveAnswer);
            }
        }
        return new Answer();
    }

    private Answer formatAnswerFromQDto(QuestionDto qDto, String name, SurveyUser surveyUser,boolean noSaveAnswer) {
        Answer answer = new Answer();

        if (isNumberStr(qDto.getAnswer()) && !isTypeQuestionTextOrAddress(qDto.getTypeQuestion())) {

            Long id = Long.valueOf(qDto.getAnswer());
            ProposalAnswer proposalAnswer;
            if (id != null) {
                proposalAnswer = proposalAnswerRepository.getOne(id);
                if (proposalAnswer != null) {
                    answer.setTextAnswers(proposalAnswer.getTextAnswer());
                    answer.setTextQuestion(qDto.getText());
                    answer.setAnswerId(proposalAnswer.getAnswerID());
                    answer.setScore(proposalAnswer.getScore());
                    answer.setDiscountLoading(proposalAnswer.getDiscountLoading());
                    answer.setSectionID(proposalAnswer.getSectionID());
                    answer.setQuestionId(proposalAnswer.getQuestionID());
                    answer.setSurveyId(proposalAnswer.getSurveyID());
                    answer.setDiscountLoading(proposalAnswer.getDiscountLoading());
                    answer.setSensitiveFactor(proposalAnswer.getSensitiveFactor());

                }
            }
        } else {
//            ProposalAnswer p = proposalAnswerRepository
//                    .findFirstByQuestionIDIgnoreCaseAndSectionIDIgnoreCaseAndSurveyIDIgnoreCase(qDto.getQuestionID(),
//                            qDto.getSectionID(),surveyUser.getSurveyID());
            answer.setTextAnswers(qDto.getAnswer());
            answer.setTextQuestion(qDto.getText());
            answer.setSectionID(qDto.getSectionID());
            answer.setQuestionId(qDto.getQuestionID());
            answer.setSurveyId(surveyUser.getSurveyID());
        }

        answer.setSectionName(name);
        answer.setSurveyUser(surveyUser);
//        answer.setScore(proposalAnswer.getScore());

        if (!noSaveAnswer)
        return answerRepository.save(answer);

        return answer;
    }

    private boolean isTypeQuestionTextOrAddress(String typeQuestion) {
        return typeQuestion != null && (typeQuestion.toLowerCase().contains("text") || typeQuestion.toLowerCase().contains("address")
                || typeQuestion.toLowerCase().contains("number"));
    }

    private void saveAnswer(QuestionDto questionDto) {
        Answer answer = new Answer();
//        answer.setTextQuestion(questionDto.getText());
//        answer.setAnswerId(questionDto.getChosenAnswer().getAnswerId());
//        answer.setTextAnswers(questionDto.getAnswer().getTextAnswers());
//        answer.setQuestionId(questionDto.getAnswer().getQuestionId());
//        answer.setSurveyId(questionDto.getAnswer().getSurveyId());
//        answer.setScore(questionDto.getAnswer().getScore());

        answerRepository.save(answer);
    }

    private Map<String, SectionDto> formatSection(List<SectionDto> sectionDtoList) {
        Map<String, SectionDto> dtoList = new HashMap<>();

        SectionDto sectionCompany = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S1)).findFirst().orElse(new SectionDto());

        SectionDto sectionRisk = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S2)).findFirst().orElse(new SectionDto());

        SectionDto sectionClaimsExp = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S3)).findFirst().orElse(new SectionDto());

        SectionDto sectionOccupancy = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S4)).findFirst().orElse(new SectionDto());

        SectionDto sectionQualityRConstruction = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S5)).findFirst().orElse(new SectionDto());

        SectionDto sectionQualityRPrevention = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S6)).findFirst().orElse(new SectionDto());

        SectionDto sectionQualityRExposure = sectionDtoList.stream().filter(Objects::nonNull)
                .filter(sDto -> sDto.getName().toLowerCase().contains(SectionConstant.S7)).findFirst().orElse(new SectionDto());

//        dtoList.put(SectionConstant.S1, sectionAgent);
        dtoList.put(SectionConstant.S1, sectionCompany);
        dtoList.put(SectionConstant.S2, sectionRisk);
        dtoList.put(SectionConstant.S3, sectionClaimsExp);
        dtoList.put(SectionConstant.S4, sectionOccupancy);
        dtoList.put(SectionConstant.S5, sectionQualityRConstruction);
        dtoList.put(SectionConstant.S6, sectionQualityRPrevention);
        dtoList.put(SectionConstant.S7, sectionQualityRExposure);


        return dtoList;
    }

    private Client getClientFromSectionCompany(SectionDto sectionCompany) {

        List<QuestionDto> questionDtoList = sectionCompany.getQuestionList();
        Client client = new Client();

        for (QuestionDto questionDto : questionDtoList) {
            if (questionDto != null) {
                Answer answer = questionDto.getProposalAnswers() != null ? questionDto.getProposalAnswers()
                        .get(0).getAnswer() : new Answer();
                answer.setQuestionId(questionDto.getQuestionID());

                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("client name")) {
                    client.setName(answer.getTextAnswers());
                    continue;
                }
                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("client address")) {
//                    client.setAddress(answer.getTextAnswers());
                    continue;
                }
                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("annual revenu")) {
                    client.setName(questionDto.getText());
                }
                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("annual revenu")) {
                    client.setName(questionDto.getText());
                }
                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("annual revenu")) {
                    client.setName(questionDto.getText());
                }
                if (questionDto.getText() != null && questionDto.getText().toLowerCase().contains("annual revenu")) {
                    client.setName(questionDto.getText());
                }

            }

        }

        return client;

    }

    @Transactional
    public AnswersDto getSurveyUser(String surveyID, String companyID) {

        List<Section> sectionList = new ArrayList<>();

        SmeUser smeUser = userRepository.findByEmailIgnoreCase(getCurrentUserLogged());

        companyID = getCompanyName(smeUser);

        if (surveyID != null)
            sectionList = sectionRepository.findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCase(surveyID, companyID);
        else {

            sectionList = sectionRepository.findByLastSurveyTrueAndCompanyIDIgnoreCase(companyID);
            surveyID = getSurveyID(sectionList);
        }

//        sectionList = sectionRepository.findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCase(surveyID, companyID);

        AnswersDto answersDto = new AnswersDto();
        answersDto.setCompanyID(companyID);
        answersDto.setSurveyID(surveyID);

        List<SectionDto> sectionDtoList = new ArrayList<>();

        for (Section section : sectionList) {
            if (!section.getCompanyID().toLowerCase().contains(companyID.toLowerCase())) continue;
            SectionDto sectionDto = new SectionDto();
            List<QuestionDto> questionDtoList = new ArrayList<>();
            sectionDto.setId(section.getId());
            sectionDto.setName(section.getName());
            sectionDto.setIconSection(section.getIconSection());
            sectionDto.setSectionID(section.getSectionID());
            questionDtoList = formatQuestionDto(section);
            sectionDto.setQuestionList(questionDtoList);
            sectionDtoList.add(sectionDto);
        }
        answersDto.setSectionDtoList(sectionDtoList);

        return answersDto;
    }

    private String getCompanyName(SmeUser smeUser) {
        return smeUser.getCompany()!=null? smeUser.getCompany().getCompanyID():"";
    }

    private String getSurveyID(List<Section> sectionList) {
        if (sectionList != null && sectionList.size() > 0) return sectionList.get(0).getSurveyID();
        return null;
    }

    private List<QuestionDto> formatQuestionDto(Section section) {

        List<QuestionDto> questionDtoList = new ArrayList<>();
        if (section.getQuestionList() != null) {
            List<Question> questionList = orderListByIDQq(section.getQuestionList());
            for (Question question : questionList) {
                QuestionDto questionDto = new QuestionDto();
                SubQuestionDto subQuestionDto;
                if (CheckTypeQuestionIsAddress(question.getTypeQuestion()))
                    questionDto.setSubQuestions(createSubQAddress(question.getText()));

                List<ProposalAnswerDto> proposalAnswerDtoList = formatProposalAnswerDto(question);
                questionDto.setId(question.getId());
                questionDto.setQuestionID(question.getQuestionID());
                questionDto.setSectionID(question.getSectionID());
                questionDto.setOcp(question.isOcp());
                questionDto.setDescription(question.getDescription());
                questionDto.setText(question.getText());
                questionDto.setTypeQuestion(question.getTypeQuestion() != null ? question.getTypeQuestion().getLabel() : "");
                questionDto.setProposalAnswers(proposalAnswerDtoList);
//            questionDto.setOccupancy(question.getOccupancy());


                questionDtoList.add(questionDto);
            }
        }
        return questionDtoList;
    }

    private List<SubQuestionDto> createSubQAddress(String questionText) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        if (questionText.toLowerCase().contains("client"))
            json = getAddressCompany();
        else
            json = getAddressRisk();
        try {
            return Arrays.asList(mapper.readValue(json, SubQuestionDto[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getAddressRisk() {
        return " [\n" +
                "            {\n" +
                "              \"id\": 12,\n" +
                "              \"text\": \"Street number\",\n" +
                "              \"description\": null,\n" +
                "              \"sectionID\": \"S3\",\n" +
                "              \"questionID\": \"Q1\",\n" +
                "              \"answer\": \"\",\n" +
                "              \"ocp\": false,\n" +
                "              \"typeQuestion\": \"text\",\n" +
                "              \"proposalAnswers\": [\n" +
                "                {\n" +
                "                  \"id\": 14,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": null,\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q1\",\n" +
                "                  \"answerID\": \"A1\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 13,\n" +
                "              \"text\": \"Street name\",\n" +
                "              \"description\": null,\n" +
                "              \"sectionID\": \"S3\",\n" +
                "              \"questionID\": \"Q2\",\n" +
                "              \"answer\": \"\",\n" +
                "              \"ocp\": false,\n" +
                "              \"typeQuestion\": \"text\",\n" +
                "              \"proposalAnswers\": [\n" +
                "                {\n" +
                "                  \"id\": 15,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": null,\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q2\",\n" +
                "                  \"answerID\": \"A1\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 14,\n" +
                "              \"text\": \"Zip Code\",\n" +
                "              \"description\": null,\n" +
                "              \"answer\": \"\",\n" +
                "              \"sectionID\": \"S3\",\n" +
                "              \"questionID\": \"Q3\",\n" +
                "              \"ocp\": false,\n" +
                "              \"typeQuestion\": \"text\",\n" +
                "              \"proposalAnswers\": [\n" +
                "                {\n" +
                "                  \"id\": 16,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": null,\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q3\",\n" +
                "                  \"answerID\": \"A1\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 15,\n" +
                "              \"text\": \"City\",\n" +
                "              \"description\": null,\n" +
                "              \"sectionID\": \"S3\",\n" +
                "              \"answer\": \"\",\n" +
                "              \"questionID\": \"Q4\",\n" +
                "              \"ocp\": false,\n" +
                "              \"typeQuestion\": \"text\",\n" +
                "              \"proposalAnswers\": [\n" +
                "                {\n" +
                "                  \"id\": 17,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": null,\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q4\",\n" +
                "                  \"answerID\": \"A1\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\": 16,\n" +
                "              \"text\": \"Country\",\n" +
                "              \"description\": null,\n" +
                "              \"answer\": \"\",\n" +
                "              \"sectionID\": \"S3\",\n" +
                "              \"questionID\": \"Q5\",\n" +
                "              \"ocp\": false,\n" +
                "              \"typeQuestion\": \"select\",\n" +
                "              \"proposalAnswers\": [\n" +
                "                {\n" +
                "                  \"id\": 18,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": \"France\",\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q5\",\n" +
                "                  \"answerID\": \"A1\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                },\n" +
                "                {\n" +
                "                  \"id\": 19,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": \"United Kingdom\",\n" +
                "                  \"sectionID\": \"S3\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q5\",\n" +
                "                  \"answerID\": \"A2\",\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                },\n" +
                "                {\n" +
                "                  \"id\": 10,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": \"China\",\n" +
                "                  \"sectionID\": \"S2\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q6\",\n" +
                "                  \"answerID\": null,\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                },\n" +
                "                {\n" +
                "                  \"id\": 11,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": \"India\",\n" +
                "                  \"sectionID\": \"S2\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q6\",\n" +
                "                  \"answerID\": null,\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                },\n" +
                "                {\n" +
                "                  \"id\": 12,\n" +
                "                  \"answer\": null,\n" +
                "                  \"score\": null,\n" +
                "                  \"textAnswer\": \"USA\",\n" +
                "                  \"sectionID\": \"S2\",\n" +
                "                  \"killed\": false,\n" +
                "                  \"questionID\": \"Q6\",\n" +
                "                  \"answerID\": null,\n" +
                "                  \"description\": null,\n" +
                "                  \"sensitiveFactor\": null\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n";
    }

    private String getAddressCompany() {
        return
                "[\n" +
                        "            {\n" +
                        "              \"id\": 3,\n" +
                        "              \"text\": \"Street number\",\n" +
                        "              \"description\": null,\n" +
                        "              \"sectionID\": \"S2\",\n" +
                        "              \"questionID\": \"Q2\",\n" +
                        "              \"answer\": \"\",\n" +
                        "              \"ocp\": false,\n" +
                        "              \"typeQuestion\": \"text\",\n" +
                        "              \"proposalAnswers\": [\n" +
                        "                {\n" +
                        "                  \"id\": 4,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": null,\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q2\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": 4,\n" +
                        "              \"text\": \"Street name\",\n" +
                        "              \"description\": null,\n" +
                        "              \"answer\": \"\",\n" +
                        "              \"sectionID\": \"S2\",\n" +
                        "              \"questionID\": \"Q3\",\n" +
                        "              \"ocp\": false,\n" +
                        "              \"typeQuestion\": \"text\",\n" +
                        "              \"proposalAnswers\": [\n" +
                        "                {\n" +
                        "                  \"id\": 5,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": null,\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q3\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": 5,\n" +
                        "              \"text\": \"Zip Code\",\n" +
                        "              \"description\": null,\n" +
                        "              \"sectionID\": \"S2\",\n" +
                        "              \"answer\": \"\",\n" +
                        "              \"questionID\": \"Q4\",\n" +
                        "              \"ocp\": false,\n" +
                        "              \"typeQuestion\": \"text\",\n" +
                        "              \"proposalAnswers\": [\n" +
                        "                {\n" +
                        "                  \"id\": 6,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": null,\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q4\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": 6,\n" +
                        "              \"text\": \"City\",\n" +
                        "              \"description\": null,\n" +
                        "              \"sectionID\": \"S2\",\n" +
                        "              \"questionID\": \"Q5\",\n" +
                        "              \"answer\": \"\",\n" +
                        "              \"ocp\": false,\n" +
                        "              \"typeQuestion\": \"text\",\n" +
                        "              \"proposalAnswers\": [\n" +
                        "                {\n" +
                        "                  \"id\": 7,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": null,\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q5\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": 7,\n" +
                        "              \"text\": \"Country\",\n" +
                        "              \"description\": null,\n" +
                        "              \"sectionID\": \"S2\",\n" +
                        "              \"answer\": \"\",\n" +
                        "              \"questionID\": \"Q6\",\n" +
                        "              \"ocp\": false,\n" +
                        "              \"typeQuestion\": \"select\",\n" +
                        "              \"proposalAnswers\": [\n" +
                        "                {\n" +
                        "                  \"id\": 8,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": \"France \",\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q6\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"id\": 9,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": \"United Kingdom\",\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q6\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": \"test\",\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"id\": 10,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": \"China\",\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q6\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"id\": 11,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": \"India\",\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q6\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"id\": 12,\n" +
                        "                  \"answer\": null,\n" +
                        "                  \"score\": null,\n" +
                        "                  \"textAnswer\": \"USA\",\n" +
                        "                  \"sectionID\": \"S2\",\n" +
                        "                  \"killed\": false,\n" +
                        "                  \"questionID\": \"Q6\",\n" +
                        "                  \"answerID\": null,\n" +
                        "                  \"description\": null,\n" +
                        "                  \"sensitiveFactor\": null\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            }\n" +
                        "          ]";
    }

    private boolean CheckTypeQuestionIsAddress(TypeQuestion typeQuestion) {
        return typeQuestion != null && typeQuestion.getLabel().toLowerCase().contains("address");
    }


    private List<ProposalAnswerDto> formatProposalAnswerDto(Question question) {
        List<ProposalAnswerDto> answerDtoList = new ArrayList<>();
        List<ProposalAnswer> orderProposalAnswerList = orderListByID(question.getProposalAnswers());

        for (ProposalAnswer prAnswer : orderProposalAnswerList) {
            ProposalAnswerDto proposalAnswerDto = new ProposalAnswerDto();
            proposalAnswerDto.setId(prAnswer.getId());
            proposalAnswerDto.setSectionID(prAnswer.getSectionID());
            proposalAnswerDto.setQuestionID(prAnswer.getQuestionID());
            proposalAnswerDto.setAnswerID(prAnswer.getAnswerID());
            proposalAnswerDto.setTextAnswer(prAnswer.getTextAnswer());
            proposalAnswerDto.setKilled(prAnswer.isKilled());
            proposalAnswerDto.setDescription(prAnswer.getDescription());
            proposalAnswerDto.setScore(prAnswer.getScore());
            proposalAnswerDto.setAnswer(prAnswer.getAnswer());
            answerDtoList.add(proposalAnswerDto);
        }


        return answerDtoList;
    }

    private List<ProposalAnswer> orderListByID(List<ProposalAnswer> proposalAnswers) {
        proposalAnswers.sort(Comparator.comparing(o -> o != null ? o.getId() : null));
        return proposalAnswers;
    }

    private List<Question> orderListByIDQq(List<Question> proposalAnswers) {
        proposalAnswers.sort(Comparator.comparing(o -> o != null ? o.getId() : null));
        return proposalAnswers;
    }


    private Double isNumber(Object valueFromCell) {

        if (valueFromCell == null) return 0D;

        if (valueFromCell instanceof String) {
            if (isNumberStr((String) valueFromCell))
                return Double.valueOf(deletePercentageChar((String) valueFromCell));
            else return 0D;
        }

        if (valueFromCell instanceof Double) return (double) valueFromCell;


        return 0.1;
    }

    private boolean isNumberStr(String valueFromCell) {

        valueFromCell = deletePercentageChar(valueFromCell);
        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String deletePercentageChar(String valueFromCell) {
        return valueFromCell != null ? valueFromCell.replaceAll("%", "").trim() : "";

    }

    private Double sum(Double... listDouble) {
        double sum = 0D;
        int i = 0;
        while (i < listDouble.length) {
            sum = sum + listDouble[i];
            i++;
        }
        return sum;

    }

    private Double calculiDiscountLoading(Map<String, SectionDto> sList, String companyID, String surveyID, SurveyUser surveyUser) {

        SectionDto qConstruction = sList.get(SectionConstant.S5);
        Double sumScoreSQConstruction = sum(
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q1, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q2, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q3, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q4, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qConstruction, QuestionConstant.S6Q5, surveyUser,true).getDiscountLoading()));

        SectionDto qPreventionProtection = sList.get(SectionConstant.S6);
        Double sumScorePreventionProtection = sum(
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q1, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q2, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q3, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q4, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q5, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q6, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q7, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q8, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q9, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q10, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q11, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q12, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q13, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qPreventionProtection, QuestionConstant.S7Q14, surveyUser,true).getDiscountLoading()));

        SectionDto qExposure = sList.get(SectionConstant.S7);
        Double sumScoreSQExposure = sum(
                isNumber(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q1, surveyUser,true).getDiscountLoading()),
                isNumber(getQuestionAnsweredByKey(qExposure, QuestionConstant.S8Q2, surveyUser,true).getDiscountLoading()));


        Double sumDiscountLoading = sum(sumScoreSQConstruction, sumScorePreventionProtection, sumScoreSQExposure);

        if (sumDiscountLoading <= -0.25) return -0.25;


        return sumDiscountLoading;
    }

//    private Double getDiscountLoadingFromProposalAnswerList(String textAnswers, String sectionID, String answerId) {
//        if (textAnswers == null || sectionID == null || answerId == null) return 0D;
//        ProposalAnswer proposalAnswer = proposalAnswerRepository.findFirstByTextAnswerIgnoreCaseAndSectionIDIgnoreCaseAndAnswerIDIgnoreCase(textAnswers, sectionID, answerId);
//        return proposalAnswer != null && proposalAnswer.getDiscountLoading() != null ? proposalAnswer.getDiscountLoading() : 0D;
//    }

}
