package com.suprateam.car.repository;

import com.suprateam.car.model.Media;
import com.suprateam.car.model.SurveyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    boolean existsByName(String name);
    List<Media> findBySurveyUser(SurveyUser surveyUser);
}
