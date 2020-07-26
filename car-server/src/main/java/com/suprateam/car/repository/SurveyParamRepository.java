package com.suprateam.car.repository;

import com.suprateam.car.model.SurveyParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyParamRepository extends JpaRepository<SurveyParameters, Long> {

    SurveyParameters findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCaseAndActiveTrue(String surveyID, String companyID);

    boolean existsByTemplateNameIgnoreCase(String templateName);

    @Modifying
    @Query(value = "UPDATE SurveyParameters k SET k.active=false where k.id= :id")
    public int desactivateSurveyParametersById(@Param("id") Long id);


}
