package com.assignment.gds.contract.smartcard;

import lombok.Data;

@Data
public class SwipeOutRequest {
    private Long smartCardId;
    private String startStop;
    private String lastStop;
}
