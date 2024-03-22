package org.rental.toolrentalapplication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rental.toolrentalapplication.dtos.RentalAgreementDTO;

@Data
@AllArgsConstructor
public class CheckoutResponse {
    private int statusCode;
    private String statusMessage;
    private RentalAgreementDTO rentalAgreement;
}
