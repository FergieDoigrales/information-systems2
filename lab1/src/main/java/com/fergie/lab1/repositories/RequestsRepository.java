package com.fergie.lab1.repositories;

import com.fergie.lab1.models.RoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsRepository extends JpaRepository<RoleRequest, Long> {
}
