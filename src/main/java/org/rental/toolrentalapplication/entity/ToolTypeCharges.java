package org.rental.toolrentalapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rental.toolrentalapplication.enums.ToolType;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ToolTypeCharges {

    @Id
    @Column(name = "tool_type")
    @Enumerated(EnumType.STRING)
    private ToolType toolType;

    @Column(name = "daily_charge")
    private double dailyCharge;

    @Column(name = "weekday_charge")
    private boolean weekdayCharge;

    @Column(name = "weekend_charge")
    private boolean weekendCharge;

    @Column(name = "holiday_charge")
    private boolean holidayCharge;
}
