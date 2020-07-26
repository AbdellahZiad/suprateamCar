package com.suprateam.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<com.suprateam.car.model.Question, Long> {

    com.suprateam.car.model.Question findFirstByQuestionIDAndSectionID(String questionID, String sectionID);
}
