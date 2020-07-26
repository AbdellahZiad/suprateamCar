package com.suprateam.car.repository;

import com.suprateam.car.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findFirstByCompanyID(String companyID);
    Optional<Company> findFirstByNameIgnoreCase(String name);


}
