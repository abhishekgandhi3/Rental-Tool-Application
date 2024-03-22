package org.rental.toolrentalapplication.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.rental.toolrentalapplication.dtos.RentalAgreementDTO;
import org.rental.toolrentalapplication.entity.Tool;
import org.rental.toolrentalapplication.request.CheckoutRequest;
import org.rental.toolrentalapplication.request.ToolRequest;
import org.rental.toolrentalapplication.response.CheckoutResponse;
import org.rental.toolrentalapplication.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);

    @Autowired
    private ToolService toolService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    @Hidden
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @PostMapping
    public ResponseEntity<Tool> createTool(@RequestBody ToolRequest toolRequest) {
        Tool createdTool = toolService.createTool(toolRequest);
        logger.info("Tool created: {}", createdTool);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTool);
    }

    @GetMapping("/{toolCode}")
    public ResponseEntity<Optional<Tool>> getToolByCode(@PathVariable String toolCode) {
        Optional<Tool> tool = toolService.getToolByCode(toolCode);
        if (tool != null) {
            logger.info("Tool found: {}", tool);
            return ResponseEntity.ok(tool);
        } else {
            logger.warn("Tool with code {} not found", toolCode);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutTool(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            logger.info("Checking out tool with request: {}", checkoutRequest);
            RentalAgreementDTO rentalAgreement = toolService.checkoutTool(checkoutRequest);
            return ResponseEntity.ok(new CheckoutResponse(HttpStatus.OK.value(), messageSource.getMessage("api.response.checkout.tool.creation.successful", null, Locale.getDefault()), rentalAgreement));
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to checkout tool: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new CheckoutResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            logger.error("An error occurred while checking out tool", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CheckoutResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null));
        }
    }
}
