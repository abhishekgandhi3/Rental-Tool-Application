package org.rental.toolrentalapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.rental.toolrentalapplication.entity.RentalAgreement;
import org.rental.toolrentalapplication.entity.Tool;
import org.rental.toolrentalapplication.entity.ToolTypeCharges;
import org.rental.toolrentalapplication.enums.ToolType;
import org.rental.toolrentalapplication.repository.RentalAgreementRepository;
import org.rental.toolrentalapplication.repository.ToolRepository;
import org.rental.toolrentalapplication.repository.ToolTypeChargesRepository;
import org.rental.toolrentalapplication.request.CheckoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ToolServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ToolServiceImplTest {

    @MockBean
    private RentalAgreementRepository rentalAgreementRepository;

    @MockBean
    private ToolRepository toolRepository;

    @MockBean
    private ToolTypeChargesRepository toolTypeChargesRepository;

    @Autowired
    private ToolService toolService;

    @Test
    void testCheckoutTool1() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.JACK_HAMMER, 2.99, true, false, false);
        Tool tool = new Tool("JAKR", toolTypeCharges, "Ridgid");
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));
        assertThrows(IllegalArgumentException.class, () -> toolService.checkoutTool(request));
    }

    @Test
    void testCheckoutTool2() {
        CheckoutRequest request = new CheckoutRequest("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.LADDER, 1.99, true, true, false);
        Tool tool = new Tool("LADW", toolTypeCharges, "Werner");
        when(toolRepository.findById("LADW")).thenReturn(Optional.of(tool));
        assertEquals(3.58, toolService.checkoutTool(request).getFinalCharge());
    }

    @Test
    void testCheckoutTool3() {
        CheckoutRequest request = new CheckoutRequest("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.CHAINSAW, 1.49, true, false, true);
        Tool tool = new Tool("CHNS", toolTypeCharges, "Stihl");
        when(toolRepository.findById("CHNS")).thenReturn(Optional.of(tool));
        assertEquals(3.35, toolService.checkoutTool(request).getFinalCharge());
    }

    @Test
    void testCheckoutTool4() {
        CheckoutRequest request = new CheckoutRequest("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.JACK_HAMMER, 2.99, true, false, false);
        Tool tool = new Tool("JAKD", toolTypeCharges, "DeWalt");
        when(toolRepository.findById("JAKD")).thenReturn(Optional.of(tool));
        assertEquals(8.97, toolService.checkoutTool(request).getFinalCharge());

    }

    @Test
    void testCheckoutTool5() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.JACK_HAMMER, 2.99, true, false, false);
        Tool tool = new Tool("JAKR", toolTypeCharges, "Ridgid");
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));
        assertEquals(14.95, toolService.checkoutTool(request).getFinalCharge());
    }

    @Test
    void testCheckoutTool6() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        ToolTypeCharges toolTypeCharges = new ToolTypeCharges(ToolType.JACK_HAMMER, 2.99, true, false, false);
        Tool tool = new Tool("JAKR", toolTypeCharges, "Ridgid");
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));
        assertEquals(1.49, toolService.checkoutTool(request).getFinalCharge());
    }
}
