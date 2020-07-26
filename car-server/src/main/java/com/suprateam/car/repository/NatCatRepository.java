package com.suprateam.car.repository;

import com.suprateam.car.model.NatCatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NatCatRepository extends JpaRepository<NatCatType, Long> {

}
