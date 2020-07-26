package com.suprateam.car.repository;

import com.suprateam.car.model.TypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeQuestionRepository extends JpaRepository<TypeQuestion, Long> {

    boolean existsByLabel(String label);
    TypeQuestion findFirstByLabelIgnoreCase(String label);

}
