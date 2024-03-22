package org.rental.toolrentalapplication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rental.toolrentalapplication.enums.ToolType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolRequest {
    private String toolCode;
    private ToolType toolType;
    private String brand;
}
