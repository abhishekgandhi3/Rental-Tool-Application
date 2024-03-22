package org.rental.toolrentalapplication.repository;

import org.rental.toolrentalapplication.entity.ToolTypeCharges;
import org.rental.toolrentalapplication.enums.ToolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolTypeChargesRepository extends JpaRepository<ToolTypeCharges, String> {
    Optional<ToolTypeCharges> findByToolType(ToolType string);
}
