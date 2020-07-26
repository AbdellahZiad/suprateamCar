package com.suprateam.car.repository;

import com.suprateam.car.model.Answer;
import com.suprateam.car.model.SurveyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findBySurveyUser(SurveyUser surveyUser);
}
