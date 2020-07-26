package com.suprateam.car.service.impl;


import com.suprateam.car.constants.YN;
import com.suprateam.car.dto.FileInfo;
import com.suprateam.car.exception.APIException;
import com.suprateam.car.model.*;
import com.suprateam.car.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Slf4j
public class UploadSurveyFromFileExcel {

    @Value("${sme.application-folder}")
    private String rootFolder;

    SurveyParamRepository surveyParamRepository;

    UserRepository userRepository;

    SectionRepository sectionRepository;

    StorageService storageService;

    QuestionRepository questionRepository;

    AnswerRepository answerRepository;

    CompanyRepository companyRepository;

    TypeQuestionRepository typeQuestionRepository;

    RatesRepository ratesRepository;

    RiskLocationAndNatCTypeRepository riskLocationAndNatCTypeRepository;

    NatCatRepository natCatRepository;

    OccupancyRepository occupancyRepository;

    ProposalAnswerRepository proposalAnswerRepository;

    SectionServiceImpl sectionService;


    @Autowired
    public UploadSurveyFromFileExcel(    UserRepository userRepository,SurveyParamRepository surveyParamRepository, SectionRepository sectionRepository, StorageService storageService, QuestionRepository questionRepository, AnswerRepository answerRepository, CompanyRepository companyRepository, TypeQuestionRepository typeQuestionRepository, RatesRepository ratesRepository, RiskLocationAndNatCTypeRepository riskLocationAndNatCTypeRepository, NatCatRepository natCatRepository, OccupancyRepository occupancyRepository, ProposalAnswerRepository proposalAnswerRepository, SectionServiceImpl sectionService) {
        this.surveyParamRepository = surveyParamRepository;
        this.sectionRepository = sectionRepository;
        this.storageService = storageService;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.companyRepository = companyRepository;
        this.typeQuestionRepository = typeQuestionRepository;
        this.ratesRepository = ratesRepository;
        this.riskLocationAndNatCTypeRepository = riskLocationAndNatCTypeRepository;
        this.natCatRepository = natCatRepository;
        this.occupancyRepository = occupancyRepository;
        this.proposalAnswerRepository = proposalAnswerRepository;
        this.sectionService = sectionService;
        this.userRepository = userRepository;
    }


    @Transactional
    public String uploadFileExcel(MultipartFile reapExcelDataFile, String loadedBy) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());

        SurveyParameters surveyParameters = readWorkbook(workbook);

        FileInfo fileInfo = storageService.store(reapExcelDataFile, "Inputs");

        surveyParameters.setTemplateName(fileInfo.getName());

        surveyParameters.setLoadedOn(new Date());

        surveyParameters.setLoadedBy(getLoadedBy());

        if (isEmpty(surveyParameters.getStatusUploadTemplate()))
            surveyParameters.setStatusUploadTemplate("done");


        surveyParameters = surveyParamRepository.save(surveyParameters);

        surveyParameters.setReload(surveyParameters.getId());

        return surveyParamRepository.save((surveyParameters)).getStatusUploadTemplate();


//        List<SurveyParameters> surveyParameterList = surveyParamRepository.findAll();
//
//        List<AnswersDto> answersDtoList = new ArrayList<>();

//        for (SurveyParameters surveyParameters : surveyParameterList) {
//            if (surveyParameters.getSurveyID() != null && surveyParameters.getCompanyID() != null )
//                answersDtoList.add(sectionService.getSurveyUser(surveyParameters.getSurveyID(), surveyParameters.getCompanyID()));
//
//        }
//
//
//        return answersDtoList;

    }

    private String getLoadedBy() {
        return getCurrentAgent()!=null?getCurrentAgent().getFullName():"";
    }

    private SmeUser getCurrentAgent() {
        String currentEmailLogged = getCurrentUserLogged();
//        SmeUser smeUser = ;

        return userRepository.findByEmailIgnoreCase(currentEmailLogged);

    }

    private String getCurrentUserLogged() {

//        if (!isSecurityContextHolderNotNull()) return "";
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return  ((UserDetails)principal).getUsername();
//        } else {
//            return principal.toString();
//        }
        return "";
//        return isSecurityContextHolderNotNull()? ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername():"";
    }
    private boolean isEmpty(String statusUploadTemplate) {
        return statusUploadTemplate == null || statusUploadTemplate.isEmpty();
    }


//    private boolean isSecurityContextHolderNotNull() {
//        return SecurityContextHolder.getContext()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null;
//    }


    private SurveyParameters readWorkbook(XSSFWorkbook workbook) {

//        AnswersDto answersDto = new AnswersDto();
        SurveyParameters surveyParameters = new SurveyParameters();
        List<Section> sectionList = new ArrayList<>();
        List<Question> questionList = new ArrayList<>();
        List<Occupancy> occupancyList = new ArrayList<>();
        Company company = new Company();
        int i = 1;
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            String sheetName = sheet.getSheetName();
            switch (sheetName.toLowerCase()) {
                case "company":
                    company = saveCompany(sheet);
                    i++;
                    break;
                case "survey":
                    surveyParameters = saveSurvey(sheet, company);
                    i++;
                    break;
                case "section":
                    sectionRepository.updateLastSurvey(company.getCompanyID());
                    sectionList = saveSection(sheet, surveyParameters);
                    i++;
                    break;
                case "questions":
                    questionList = saveQuestion(sheet, surveyParameters, sectionList);
                    i++;
                    break;
                case "answers":
                    saveProposalAnswers(sheet, questionList,sectionList);
                    i++;
                    break;
                case "ocp":
                    occupancyList = saveOccupancy(sheet, questionList);
                    i++;
                    break;
//                case "natcat rates":
//                    ratesList = saveRates(sheet);
//                    break;.
                case "natcat inputs":
                    i++;
                        saveNatCateAndRiskLocation(sheet, sheetIterator.next(), occupancyList);
                    break;

            }

        }
        if (i != 8) {
            surveyParameters.setStatusUploadTemplate("Incompatible file, you must upload an Excel file and respect the basic template");
            throw
                    new APIException(
                            "Incompatible file, you must upload an Excel file and respect the basic template \n \n"
                    + "-------------------------------------  \n"
                                    +msgIncompatibleFile()
                    + " -------------------------------------");

        }
        return surveyParameters;

//        answersDto.setSurveyID(surveyParameters.getSurveyID());
//        answersDto.setCompanyID(surveyParameters.getCompanyID());
//        List<SectionDto> sectionDtoList = formatSectionDto(questionList,sectionList,surveyParameters.getSurveyID());
//        answersDto.setSectionDtoList(sectionDtoList);
//        return answersDto;
    }

    private String msgIncompatibleFile() {

        return "Excel template must contain sheets : \n" +
                "- Company            \n" +
                "- Survey             \n" +
                "- Section            \n" +
                "- Question           \n" +
                "- Answers            \n" +
                "- Ocp (for occupancy)\n" +
                "- NatCat inputs      \n" +
                "- Nat Cat rates).    \n" ;
//                "You can download an example of the approved file";
    }

    private void saveNatCateAndRiskLocation(Sheet sheet, Sheet sheetRates, List<Occupancy> occupancyList) {


        List<RiskLocationAndNatCatType> riskLocationAndNatCatTypeList = new ArrayList<>();
        for (Row row : sheet) {
            RiskLocationAndNatCatType riskLocationAndNatCatType = new RiskLocationAndNatCatType();

            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        riskLocationAndNatCatType.setZipCodeID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        riskLocationAndNatCatType.setZipCode((String) getValueFromCell(cell));
                        break;
                    case 2:
                        riskLocationAndNatCatType.setNatCatWS(isValid((String) getValueFromCell(cell)));
                        break;
                    case 3:
                        riskLocationAndNatCatType.setNatCatFLOOD(isValid((String) getValueFromCell(cell)));
                        break;
                    case 4:
                        riskLocationAndNatCatType.setNatCatEQ(isValid((String) getValueFromCell(cell)));
                        break;
                }


            }
            riskLocationAndNatCatTypeList.add(riskLocationAndNatCatType);


        }

        List<RiskLocationAndNatCatType> riskLocationAndNatCatTypes = riskLocationAndNatCTypeRepository.saveAll(riskLocationAndNatCatTypeList);
        saveRates(sheetRates, riskLocationAndNatCatTypes, occupancyList);
    }

    private RiskLocationAndNatCatType getRiskLocationAndNatCatTypeByZipCode(List<RiskLocationAndNatCatType> riskLocationAndNatCatTypes, String zipCodeID) {
        if (riskLocationAndNatCatTypes == null || zipCodeID == null) return null;
        return riskLocationAndNatCatTypes.stream().filter(Objects::nonNull)
                .filter(risk -> risk.getZipCodeID()
                        .toLowerCase().contains(zipCodeID.toLowerCase())).findFirst().orElse(null);
//                .collect(Collectors.toList());
    }

    private String isValid(String yn) {
        return yn.toLowerCase().contains("y") ? YN.YES.getValue()
                : YN.NO.getValue();
    }

    private void saveRates(Sheet sheet, List<RiskLocationAndNatCatType> riskLocationAndNatCatTypes, List<Occupancy> occupancyList) {

        List<Rates> rates = new ArrayList<>();
        for (Row row : sheet) {
            Rates rate = new Rates();

            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        rate.setZipCodeID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        rate.setNatCatCode((String) getValueFromCell(cell));
                        break;
                    case 2:
                        rate.setTypeOfOccupancy((String) getValueFromCell(cell));
                        break;
                    case 3:
                        rate.setRate(isNumber(getValueFromCell(cell)));
                        break;
                    case 4:
                        rate.setKey((String) getValueFromCell(cell));
                        break;
                }


            }
            RiskLocationAndNatCatType riskLocationAndNatCatType =
                    getRiskLocationAndNatCatTypeByZipCode(riskLocationAndNatCatTypes, rate.getZipCodeID());
            Occupancy occupancy = getOccupancyByTypeOfOccupancy(occupancyList, rate.getTypeOfOccupancy());

            if (occupancy != null)
                rate.setOccupancy(occupancy);

            if (riskLocationAndNatCatType != null)
                rate.setRiskLocationAndNatCatType(riskLocationAndNatCatType);

            rates.add(rate);

        }


        ratesRepository.saveAll(rates);

    }

    private Occupancy getOccupancyByTypeOfOccupancy(List<Occupancy> occupancyList, String typeOfOccupancy) {
        if (typeOfOccupancy == null || occupancyList == null) return null;
        return occupancyList
                .stream()
                .filter(Objects::nonNull)
                .filter(occupancy -> occupancy.getTypeOfOccupancy() != null &&
                        occupancy.getTypeOfOccupancy().toLowerCase()
                                .contains(typeOfOccupancy.toLowerCase()))
                .findFirst().orElse(null);
    }

//    private List<SectionDto> formatSectionDto(List<Question> questionList, List<Section> sectionList, String surveyID) {
//
//        List<SectionDto> sectionDtoList = new ArrayList<>();
//        for (Section section : sectionList)
//        {
////            if (section.getSectionID().toLowerCase().contains(SectionConstant.S1))
////                sectionDtoList.set(0,formatSDtoFromSection(section,questionList));
//
//
//        }
//
//
//    }

//    private SectionDto formatSDtoFromSection(Section section, List<Question> questionList) {
//        SectionDto sectionDto = new SectionDto();
//        for (Question question:questionList)
//        {
//            if (question.getSectionID().contains(section.getSectionID()))
//            {
//
//            }
//
//        }
//    }

    private void saveProposalAnswers(Sheet sheet, List<Question> questionList, List<Section> sectionList) {

        List<ProposalAnswer> proposalAnswerList = new ArrayList<>();

        for (Row row : sheet) {
            ProposalAnswer proposalAnswer = new ProposalAnswer();
            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        proposalAnswer.setSectionID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        proposalAnswer.setQuestionID((String) getValueFromCell(cell));
                        break;
                    case 2:
                        proposalAnswer.setAnswerID((String) getValueFromCell(cell));
                        break;
                    case 3:
                        proposalAnswer.setTextAnswer((String) getValueFromCell(cell));
                        break;
//                    case 4:
//                        proposalAnswer.setKilled(isKilled((String) getValueFromCell(cell)));
//                        break;
                    case 4:
                        proposalAnswer.setDescription((String) getValueFromCell(cell));
                        break;
                    case 5:
                        proposalAnswer.setScore((Double) getValueFromCell(cell));
                        break;
                    case 6:
                        proposalAnswer.setDiscountLoading(isNumber(getValueFromCell(cell)));
                        break;
                    case 7:
                        proposalAnswer.setSensitiveFactor((String) getValueFromCell(cell));
                        break;
                }

            }


            Question question = findQuestionByQAndSection(questionList, proposalAnswer.getQuestionID(), proposalAnswer.getSectionID());
            if (question != null) {
                proposalAnswer.setQuestion(question);
                proposalAnswerList.add(proposalAnswer);
            }
            proposalAnswer.setCompanyID(getCompanyID(sectionList));
            proposalAnswer.setSurveyID(getSurveyID(sectionList));
            proposalAnswerRepository.save(proposalAnswer);

        }

//        proposalAnswerRepository.saveAll(proposalAnswerList);
    }

    private String getCompanyID(List<Section> sectionList) {
        if (sectionList!=null && sectionList.size()>0)
            return sectionList.get(0).getCompanyID();

        return "";
    }
    private String getSurveyID(List<Section> sectionList) {
        if (sectionList!=null && sectionList.size()>0)
            return sectionList.get(0).getSurveyID();

        return "";
    }

    private Question findQuestionByQAndSection(List<Question> questionList, String questionID, String sectionID) {
        if (sectionID == null) return null;
        for (Question question : questionList) {
            if (checkQuestionIsNotEmpty(question) && question.getQuestionID().toLowerCase().contains(questionID.toLowerCase()) && question.getSectionID()
                    .toLowerCase().contains(sectionID.toLowerCase()))
                return question;
        }
        return null;
    }

    private boolean checkQuestionIsNotEmpty(Question question) {
        return question != null && question.getSectionID() != null && question.getQuestionID() != null;
    }

    private boolean isKilled(String valueFromCell) {
        return valueFromCell.contains("y");
    }

    private List<Occupancy> saveOccupancy(Sheet sheet, List<Question> questionList) {

        List<Occupancy> occupancyList = new ArrayList<>();
        for (Row row : sheet) {
            Occupancy occupancy = new Occupancy();

            if (checkIfRowIsEmpty(row)) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        occupancy.setSectionID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        occupancy.setQuestionID((String) getValueFromCell(cell));
                        break;
                    case 2:
                        occupancy.setAnswerID((String) getValueFromCell(cell));
                        break;
                    case 3:
                        occupancy.setSeqNo(isNumber(getValueFromCell(cell)));
                        break;
                    case 4:
                        occupancy.setUsCode(isNumber(getValueFromCell(cell)));
                        break;
                    case 5:
                        occupancy.setUsTitle((String) getValueFromCell(cell));
                        break;
                    case 6:
                        occupancy.setRiskAssessment((String) getValueFromCell(cell));
                        break;
                    case 7:
                        occupancy.setCapacityLimit(isNumber(getValueFromCell(cell)));
                        break;
                    case 8:
                        occupancy.setBaseRate(isNumber(getValueFromCell(cell)));
                        break;
                    case 9:
                        occupancy.setAdditionalQuestion((String) getValueFromCell(cell));
                        break;
                    case 10:
                        occupancy.setTypeOfOccupancy((String) getValueFromCell(cell));
                        break;
                }

            }
            Question question = findQuestionByQAndSection(questionList, occupancy.getQuestionID(), occupancy.getSectionID());
            occupancy = occupancyRepository.save(occupancy);

            if (question != null) {
                occupancy.setQuestion(question);
                occupancyList.add(occupancy);
            }
        }

        return occupancyList;


    }

    private Double isNumber(Object valueFromCell) {
        if (valueFromCell instanceof String) {
            if (isNumberStr((String) valueFromCell))
                return Double.valueOf((String) valueFromCell);
            else return null;
        }

        return (Double) valueFromCell;
    }

    private boolean isNumberStr(String valueFromCell) {

        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Sheet getSheetOccupancy(Iterator<Sheet> sheetIterator) {
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            if (sheet.getSheetName().toLowerCase().contains("oc"))
                return sheet;
        }
        return null;
    }

    private boolean isOccupancy(Question question) {
        return question != null && question.getTypeQuestion() != null && question.getTypeQuestion().getLabel() != null && question.getTypeQuestion().getLabel().contains("oc");
    }

    private List<Question> saveQuestion(Sheet sheet,
                                        SurveyParameters surveyParameters,
                                        List<Section> sectionList) {
        List<Question> questionList = new ArrayList<>();

        for (Row row : sheet) {
            Question question = new Question();
            question.setSurveyParameters(surveyParameters);

            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        question.setSectionID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        question.setQuestionID((String) getValueFromCell(cell));
                        break;
                    case 2:
                        question.setText((String) getValueFromCell(cell));
                        break;
                    case 3:
                        question.setTypeQuestion(getTypeQuestion((String) getValueFromCell(cell)));
                        break;
                    case 4:
                        question.setDescription((String) getValueFromCell(cell));
                        break;
            }

            }
            question.setSection(getSection(sectionList, question.getSectionID()));
            if (isOccupancy(question)) {
                question.setOcp(true);
            }
            question = questionRepository.save(question);
            questionList.add(question);
        }

        return questionList;
//        return questionRepository.saveAll(questionList);
    }


    private Section getSection(List<Section> sectionList, String sectionID) {

        if (sectionID == null) return null;
        for (Section section : sectionList) {

            if (section != null && section.getSectionID() != null &&
                    section.getSectionID().toLowerCase()
                            .contains(sectionID.toLowerCase()))

                return section;
        }
        return new Section();
    }

    private TypeQuestion getTypeQuestion(String typeQ) {
        TypeQuestion typeQuestion = new TypeQuestion();
        typeQuestion.setLabel(typeQ);
        if (typeQuestionRepository.existsByLabel(typeQ))
            return typeQuestionRepository.findFirstByLabelIgnoreCase(typeQ);

        return typeQuestionRepository.save(typeQuestion);
    }


    private List<Section> saveSection(Sheet sheet, SurveyParameters surveyParameters) {

        List<Section> sectionList = new ArrayList<>();

        for (Row row : sheet) {
            Section section = new Section();

            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
//                    case 0:
//                        section.setSurveyID((String) getValueFromCell(cell));
//                        break;
                    case 0:
                        section.setSectionID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        section.setName((String) getValueFromCell(cell));
                        break;
                    case 2:
                        section.setIconSection(addPath((String) getValueFromCell(cell)));
                        break;
                }


            }
            section.setSurveyID(surveyParameters.getSurveyID());
            section.setLastSurvey(true);
            section.setCompanyID(surveyParameters.getCompanyID());
            sectionList.add(section);
        }

        return sectionRepository.saveAll(sectionList);
    }

    private String addPath(String iconName) {
        return iconName;
    }

    private Company saveCompany(Sheet sheet) {

        Company company = new Company();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        company.setCompanyID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        company.setName((String) getValueFromCell(cell));
                        break;
                    case 2:
                        company.setAddress((String) getValueFromCell(cell));
                        break;
                }

            }
        }

       if (!companyRepository.findFirstByNameIgnoreCase(company.getName()).isPresent())
           company = companyRepository.save(company);
       else
       {
           Company company1 = companyRepository.findFirstByNameIgnoreCase(company.getName()).get();
           company.setId(company1.getId());
           company = companyRepository.save(company);
       }




        List<SmeUser> smeUserList = userRepository.findByCompanyNameIgnoreCase(company.getName());

        Company finalCompany = company;

        smeUserList.forEach(smeUser -> {
            smeUser.setCompany(finalCompany);
            userRepository.save(smeUser);
        });
        return company;

    }

    private SurveyParameters saveSurvey(Sheet sheet, Company company) {

        SurveyParameters surveyParameters = new SurveyParameters();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cellItr = row.cellIterator();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                int index = cell.getColumnIndex();
                switch (index) {
                    case 0:
                        surveyParameters.setSurveyID((String) getValueFromCell(cell));
                        break;
                    case 1:
                        surveyParameters.setCompanyID((String) getValueFromCell(cell));
                        break;
                    case 2:
                        surveyParameters.setThresholdMin((Double) getValueFromCell(cell));
                        break;
                    case 3:
                        surveyParameters.setThresholdMax((Double) getValueFromCell(cell));
                        break;
                }

            }
        }

        surveyParameters.setCompanyID(company.getCompanyID());
        surveyParameters.setCompany(company);

        if (surveyParamRepository
                .findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCaseAndActiveTrue(surveyParameters.getSurveyID(),
                        surveyParameters.getCompanyID()) != null) {
            surveyParameters.setStatusUploadTemplate("Duplicate ! " +
                    "The Survey : ( Survey ID = " + surveyParameters.getSurveyID() + "\n" +
                    "And Company Name = " + surveyParameters.getCompanyID() + ") " +
                    " is Already Exist !!!");

            throw new APIException("Duplicate," +
                    "the survey : ( surveyID = " + surveyParameters.getSurveyID() + " " +
                    "&& company name = " + surveyParameters.getCompanyID() + ") " +
                    " \n is already exist !!! \n"
                    + "-------------------------------- ----- \n"
                    +msgIsAlreadyExist()
                    + "\n -------------------------------------");

        }
        return surveyParamRepository.save(surveyParameters);
    }

    private String msgIsAlreadyExist() {

        return "To create a new survey," +
                " please create a new surveyID in the Excel template";
    }

    private Object getValueFromCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && org.apache.commons.lang3.StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }


    public byte[] getUploadedFile(Long docId) {
        SurveyParameters surveyParameters = surveyParamRepository.getOne(docId);
        try {
            return storageService.load(surveyParameters.getTemplateName(),"Inputs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileName(Long surID) {
        return surveyParamRepository.getOne(surID).getTemplateName();
    }
}
