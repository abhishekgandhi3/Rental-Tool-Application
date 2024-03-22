package org.rental.toolrentalapplication.dtos;

import lombok.*;
import org.rental.toolrentalapplication.enums.ToolType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalAgreementDTO {

    private String toolCode;

    private ToolType toolType;

    private String brand;

    private int rentalDays;

    private String checkoutDate;

    private String dueDate;

    private double dailyRentalCharge;

    private int chargeDays;

    private double preDiscountCharge;

    private int discountPercent;

    private double discountAmount;

    private double finalCharge;

    public static String dateFormat(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter);
    }

}
