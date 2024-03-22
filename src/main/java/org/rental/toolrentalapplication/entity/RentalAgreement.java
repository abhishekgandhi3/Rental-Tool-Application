package org.rental.toolrentalapplication.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.rental.toolrentalapplication.enums.ToolType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table
public class RentalAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_code")
    private String toolCode;

    @Column(name = "tool_type")
    @Enumerated(EnumType.STRING)
    private ToolType toolType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "rental_days")
    private int rentalDays;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "daily_rental_charge")
    private double dailyRentalCharge;

    @Column(name = "charge_days")
    private int chargeDays;

    @Column(name = "pre_discount_charge")
    private double preDiscountCharge;

    @Column(name = "discount_percent")
    private int discountPercent;

    @Column(name = "discount_amount")
    private double discountAmount;

    @Column(name = "final_charge")
    private double finalCharge;
}
