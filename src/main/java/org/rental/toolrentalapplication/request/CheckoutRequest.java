package org.rental.toolrentalapplication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private String toolCode;
    private int rentalDays;
    private int discountPercent;
    private LocalDate checkoutDate;
}
