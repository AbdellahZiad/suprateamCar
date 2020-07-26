package com.suprateam.car.repository;

import com.suprateam.car.model.RiskLocationAndNatCatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskLocationAndNatCTypeRepository extends JpaRepository<RiskLocationAndNatCatType, Long> {

}
