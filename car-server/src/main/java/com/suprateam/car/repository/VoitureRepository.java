package com.suprateam.car.repository;

import com.suprateam.car.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long>, JpaSpecificationExecutor<Voiture> {

}
