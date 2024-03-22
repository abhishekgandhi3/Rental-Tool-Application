package org.rental.toolrentalapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tool {
    @Id
    @Column(name = "tool_code")
    private String toolCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tool_type_charges")
    private ToolTypeCharges toolTypeCharges;

    @Column(name = "brand")
    private String brand;
}
