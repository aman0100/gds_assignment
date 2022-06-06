package com.assignment.gds.controller;

import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.gds.contract.smartcard.SmartCardResponse;
import com.assignment.gds.contract.smartcard.SwipeInRequest;
import com.assignment.gds.contract.smartcard.SwipeOutRequest;
import com.assignment.gds.service.SmartCardService;

@RestController
@RequestMapping("/api/v1/smart-card")
public class SmartCardController {
    private final SmartCardService smartCardService;

    @Autowired
    SmartCardController(SmartCardService smartCardService) {
        this.smartCardService = smartCardService;
    }

    @PostMapping("/swipe-in")
    public CompletionStage<ResponseEntity<SmartCardResponse>> swipeIn(@RequestBody SwipeInRequest swipeInRequest) {
        return smartCardService.swipeIn(swipeInRequest)
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/swipe-out")
    public CompletionStage<ResponseEntity<SmartCardResponse>> swipeOut(@RequestBody SwipeOutRequest swipeOutRequest) {
        return smartCardService.swipeOut(swipeOutRequest)
            .thenApply(ResponseEntity::ok);
    }
}
