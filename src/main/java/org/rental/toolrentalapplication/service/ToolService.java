package org.rental.toolrentalapplication.service;

import org.rental.toolrentalapplication.dtos.RentalAgreementDTO;
import org.rental.toolrentalapplication.entity.Tool;
import org.rental.toolrentalapplication.request.CheckoutRequest;
import org.rental.toolrentalapplication.request.ToolRequest;

import java.util.Optional;

public interface ToolService {
    Tool createTool(ToolRequest toolRequest);

    Optional<Tool> getToolByCode(String toolCode);

    RentalAgreementDTO checkoutTool(CheckoutRequest checkoutRequest);
}
