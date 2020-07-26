package com.suprateam.car.repository;

import com.suprateam.car.model.ProposalAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalAnswerRepository extends JpaRepository<ProposalAnswer, Long> {

    List<ProposalAnswer> findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCase(String surveyID,String companyID);

    ProposalAnswer findFirstByTextAnswerIgnoreCaseAndSectionIDIgnoreCaseAndQuestionIDIgnoreCase(String textAnswer,String sectionID,String questionID);
    ProposalAnswer findFirstByTextAnswerIgnoreCaseAndSectionIDIgnoreCaseAndAnswerIDIgnoreCase(String textAnswer,String sectionID,String AnswerID);
    ProposalAnswer findFirstByTextAnswerIgnoreCase(String s);
}
