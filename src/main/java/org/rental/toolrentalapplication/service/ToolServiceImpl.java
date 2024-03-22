package org.rental.toolrentalapplication.service;

import org.apache.commons.lang3.StringUtils;
import org.rental.toolrentalapplication.entity.RentalAgreement;
import org.rental.toolrentalapplication.dtos.RentalAgreementDTO;
import org.rental.toolrentalapplication.entity.Tool;
import org.rental.toolrentalapplication.entity.ToolTypeCharges;
import org.rental.toolrentalapplication.repository.RentalAgreementRepository;
import org.rental.toolrentalapplication.repository.ToolRepository;
import org.rental.toolrentalapplication.repository.ToolTypeChargesRepository;
import org.rental.toolrentalapplication.request.CheckoutRequest;
import org.rental.toolrentalapplication.request.ToolRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static org.rental.toolrentalapplication.dtos.RentalAgreementDTO.dateFormat;

@Service
public class ToolServiceImpl implements ToolService {

    private static final Logger logger = LoggerFactory.getLogger(ToolServiceImpl.class);

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolTypeChargesRepository toolTypeChargesRepository;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Override
    public Tool createTool(ToolRequest toolRequest) {
        validateTool(toolRequest);
        if (toolRepository.existsByToolCode(toolRequest.getToolCode())) {
            throw new IllegalArgumentException("Tool with the same code already exists.");
        }
        Tool tool = new Tool();
        tool.setToolCode(toolRequest.getToolCode());
        Optional<ToolTypeCharges> toolTypeCharges = toolTypeChargesRepository.findByToolType(toolRequest.getToolType());
        if (toolTypeCharges.isEmpty()) {
            throw new IllegalArgumentException("Tool type is not Present.");
        }
        tool.setToolTypeCharges(toolTypeCharges.get());
        tool.setBrand(toolRequest.getBrand());

        return toolRepository.save(tool);

    }

    @Override
    public Optional<Tool> getToolByCode(String toolCode) {
        if (!toolRepository.existsByToolCode(toolCode)) {
            throw new IllegalArgumentException("Tool code is not Present.");
        }
        return toolRepository.findById(toolCode);
    }

    @Override
    public RentalAgreementDTO checkoutTool(CheckoutRequest checkoutRequest) {
        logger.info("Received checkout request: {}", checkoutRequest);

        validateCheckoutRequest(checkoutRequest);
        logger.info("Checkout request is valid");

        // Find the tool by ID
        Tool tool = toolRepository.findById(checkoutRequest.getToolCode()).orElseThrow(() -> {
            String errorMessage = "Tool not found for code: " + checkoutRequest.getToolCode();
            logger.error(errorMessage);
            return new IllegalArgumentException(errorMessage);
        });

        logger.info("Found tool for checkout: {}", tool);

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setToolCode(tool.getToolCode());
        rentalAgreement.setToolType(tool.getToolTypeCharges().getToolType());
        rentalAgreement.setBrand(tool.getBrand());
        rentalAgreement.setRentalDays(checkoutRequest.getRentalDays());

        rentalAgreement.setCheckoutDate(checkoutRequest.getCheckoutDate());
        rentalAgreement.setDueDate(checkoutRequest.getCheckoutDate().plusDays(checkoutRequest.getRentalDays()));

        double dailyRentalCharge = tool.getToolTypeCharges().getDailyCharge();
        rentalAgreement.setDailyRentalCharge(dailyRentalCharge);
        logger.info("Daily rental charge for the tool: {}", dailyRentalCharge);

        int chargeDays = calculateChargeDays(tool, checkoutRequest.getCheckoutDate(), checkoutRequest.getCheckoutDate().plusDays(checkoutRequest.getRentalDays()));
        rentalAgreement.setChargeDays(chargeDays);
        logger.info("Calculated charge days: {}", chargeDays);

        double preDiscountCharge = Double.parseDouble(decimalFormat.format(dailyRentalCharge * chargeDays));
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);
        logger.info("Pre-discount charge: {}", preDiscountCharge);

        double discountAmount = Double.parseDouble(decimalFormat.format(preDiscountCharge * (checkoutRequest.getDiscountPercent() / 100.0)));
        rentalAgreement.setDiscountPercent(checkoutRequest.getDiscountPercent());
        rentalAgreement.setDiscountAmount(discountAmount);
        logger.info("Discount amount: {}", discountAmount);

        double finalCharge = Double.parseDouble(decimalFormat.format(preDiscountCharge - discountAmount));
        rentalAgreement.setFinalCharge(finalCharge);
        logger.info("Final charge after discount: {}", finalCharge);

        rentalAgreementRepository.save(rentalAgreement);
        logger.info("Rental agreement saved successfully");

        RentalAgreementDTO rentalAgreementDTO = convertToDto(rentalAgreement);
        rentalAgreementDTO.setCheckoutDate(dateFormat(rentalAgreement.getCheckoutDate()));
        rentalAgreementDTO.setDueDate(dateFormat(rentalAgreement.getDueDate()));
        return rentalAgreementDTO;
    }

    private void validateCheckoutRequest(CheckoutRequest checkoutRequest) {
        if (checkoutRequest.getRentalDays() <= 0 ) {
            throw new IllegalArgumentException("Rental day count must be greater than 0.");
        }
        if (checkoutRequest.getDiscountPercent() < 0 || checkoutRequest.getDiscountPercent() > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        if (StringUtils.isBlank(checkoutRequest.getToolCode())) {
            throw new IllegalArgumentException("Tool code must be provided");
        }
    }

    public void validateTool(ToolRequest tool) {
        if (StringUtils.isBlank(tool.getToolCode())) {
            throw new IllegalArgumentException("Tool code must be provided.");
        }
        if (tool.getToolType() == null) {
            throw new IllegalArgumentException("Tool type  must be provided.");
        }
        if (StringUtils.isBlank(tool.getBrand())) {
            throw new IllegalArgumentException("Brand must be provided.");
        }
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1); // Start from the day after checkout, including dueDate
        while (!currentDate.isAfter(dueDate)) {
            if (isChargeableDay(tool, currentDate)) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return chargeDays;
    }

    private boolean isChargeableDay(Tool tool, LocalDate date) {
        boolean isWeekDay = isWeekDay(date);
        boolean isHoliday = isHoliday(date);
        if (isWeekDay) {
            if (isHoliday && !tool.getToolTypeCharges().isHolidayCharge()) {
                return false;
            }
            return tool.getToolTypeCharges().isWeekdayCharge();
        } else {
            return tool.getToolTypeCharges().isWeekendCharge();
        }
    }

    private boolean isWeekDay(LocalDate date) {
        return !date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private boolean isHoliday(LocalDate date) {
        // Labor Day (First Monday in September) as holiday
        boolean isLaborDay = (date.getMonthValue() == 9 && date.getDayOfWeek().equals(DayOfWeek.MONDAY) && date.getDayOfMonth() <= 7);
        if (isLaborDay) {
            return true;
        }
        // Independence Day 4th July (on weekdays) as holiday
        // If Independence Day is on Saturday then Friday is holiday
        // If Independence Day is on Sunday then Monday is holiday
        boolean isWeekDay = isWeekDay(date);
        boolean isIndependenceDay = (date.getMonthValue() == 7 && date.getDayOfMonth() == 4 && isWeekDay)
                || (date.getMonthValue() == 7 && date.getDayOfMonth() == 3 && date.getDayOfWeek().equals(DayOfWeek.FRIDAY))
                || (date.getMonthValue() == 7 && date.getDayOfMonth() == 5 && date.getDayOfWeek().equals(DayOfWeek.MONDAY));
        return isIndependenceDay;
    }

    public RentalAgreementDTO convertToDto(RentalAgreement rentalAgreement) {
        RentalAgreementDTO rentalAgreementDTO = new RentalAgreementDTO();
        BeanUtils.copyProperties(rentalAgreement, rentalAgreementDTO);
        return rentalAgreementDTO;
    }

}
