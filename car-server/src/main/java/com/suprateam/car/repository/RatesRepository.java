package com.suprateam.car.repository;

import com.suprateam.car.model.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {

    java.util.List<Rates> findByZipCodeIDIgnoreCase(String zipCode);

}
