package com.suprateam.car.repository;

import com.suprateam.car.model.SurveyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyUserRepository extends JpaRepository<SurveyUser, Long>, JpaSpecificationExecutor<SurveyUser> {



    @Modifying
    @Query(value = "UPDATE SurveyUser t SET t.decision=:decision, t.by ='SYSTEM' where t.id=:id")
    public void updateSurveyUserStatus(@Param("decision") String decision, @Param("id") Long id);
}
