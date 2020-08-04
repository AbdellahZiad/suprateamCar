package com.suprateam.car.repository;

import com.suprateam.car.model.SmeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<SmeUser,Long> {

    boolean existsByEmailIgnoreCase(String email);

    SmeUser findByEmailIgnoreCase(String email);

    @Modifying
    @Query(value = "UPDATE SmeUser s SET s.active=false where s.id= :id")
    public int desactivateUserById(@Param("id") Long id);
}
