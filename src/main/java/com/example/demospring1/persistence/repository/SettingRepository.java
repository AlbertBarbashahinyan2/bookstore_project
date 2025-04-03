package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    @Query(""" 
            SELECT s.name
            FROM Setting s
            """)
    List<String> findAllSettingNames();

    Setting findByName(String name);
}
