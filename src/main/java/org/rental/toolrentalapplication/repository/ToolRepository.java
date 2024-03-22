package org.rental.toolrentalapplication.repository;

import org.rental.toolrentalapplication.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, String> {

    boolean existsByToolCode(String toolCode);

}
