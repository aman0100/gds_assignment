package com.assignment.gds.contract.smartcard;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SmartCardResponse {
    private Float balance;
    private String message;
}
