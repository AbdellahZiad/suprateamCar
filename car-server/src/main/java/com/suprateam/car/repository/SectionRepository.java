package com.suprateam.car.repository;

import com.suprateam.car.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    Section findFirstBySectionIDIgnoreCase(String sectionID);

    List<Section> findBySurveyIDIgnoreCaseAndCompanyIDIgnoreCase(String surveyID,String companyID);

    List<Section> findByLastSurveyTrueAndCompanyIDIgnoreCase(String companyID);

    @Modifying
    @Query(value = "UPDATE Section s SET s.lastSurvey=false where s.lastSurvey= true and s.companyID =:companyID")
    void updateLastSurvey(@Param("companyID") String companyID);
}
