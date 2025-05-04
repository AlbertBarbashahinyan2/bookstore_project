package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Role;
import com.example.demospring1.service.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
