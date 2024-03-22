package org.rental.toolrentalapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.rental.toolrentalapplication.entity.RentalAgreement;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
}
