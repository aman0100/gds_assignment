package com.assignment.gds.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.gds.contract.smartcard.SmartCardResponse;
import com.assignment.gds.contract.smartcard.SwipeInRequest;
import com.assignment.gds.contract.smartcard.SwipeOutRequest;
import com.assignment.gds.entity.EStops;
import com.assignment.gds.entity.ETransactionType;
import com.assignment.gds.entity.SmartCard;
import com.assignment.gds.repository.SmartCardRepository;

@Service
public class SmartCardService {  
    private static final Integer NIGHT_TIME_TRAVEL_PRICE = 60;
    private static final Integer DAY_TIME_TRAVEL_PRICE = 80;
    private static final Integer LONG_TRAVEL_DISCOUNT_PERCENTAGE = 20;
    private static final Integer WEEKEND_TRAVEL_DISCOUNT_PERCENTAGE = 10;
    private static final Integer MAX_FARE_AMOUNT = 10;

    private final SmartCardRepository smartCardRepository;

    @Autowired
    SmartCardService(SmartCardRepository smartCardRepository) {
        this.smartCardRepository = smartCardRepository;
    }

    public CompletableFuture<Optional<SmartCard>> findById(Long id) {
        return CompletableFuture.completedFuture(smartCardRepository.findById(id));
    }

    public CompletableFuture<SmartCard> updateBalance(SmartCard smartCard, float transactionAmount, ETransactionType transactionType) {
        switch (transactionType) {
            case DEBIT -> smartCard.setBalance(smartCard.getBalance() - transactionAmount);
            case CREDIT -> smartCard.setBalance(smartCard.getBalance() + transactionAmount);
        }

        return CompletableFuture.completedFuture(smartCardRepository.save(smartCard));
    }

    public CompletableFuture<SmartCardResponse> swipeIn(SwipeInRequest swipeInRequest) {
        return findById(swipeInRequest.getSmartCardId())
            .thenApply(smartCard -> smartCard.orElseThrow(() -> new RuntimeException("Invalid Smart Card")))
            .thenApply(smartCard -> {
                if(smartCard.getBalance() < 10)
                    throw new RuntimeException("Balance is less than 10 Dollars.");
                
                return getResponse(smartCard, "You may enter. Enjoy the ride!");
            });
    }

    public CompletableFuture<SmartCardResponse> swipeOut(SwipeOutRequest swipeOutRequest) {
        return findById(swipeOutRequest.getSmartCardId())
            .thenApply(smartCard -> smartCard.orElseThrow(() -> new RuntimeException("Invalid Smart Card")))
            .thenCombine(isValidStop(swipeOutRequest.getStartStop(), swipeOutRequest.getLastStop()), (smartCard, isValidStop) -> {
                if(Boolean.FALSE.equals(isValidStop))
                    throw new RuntimeException("Invalid Stop.");

                return smartCard;
            })
            .thenCompose(smartCard -> {
                float farePrice = getFareAmount(swipeOutRequest.getStartStop(), swipeOutRequest.getLastStop());
                if(smartCard.getBalance() < farePrice)
                    throw new RuntimeException("Balance is less than the service charge. Block the door!");

                return updateBalance(smartCard, farePrice, ETransactionType.DEBIT);
            })
            .thenApply(smartCard -> getResponse(smartCard, "You may leave!"));
    }

    private CompletableFuture<Boolean> isValidStop(String startStop, String lastStop) {
        try {
            EStops.valueOf(startStop.toUpperCase());
            EStops.valueOf(lastStop.toUpperCase());
            return CompletableFuture.completedFuture(true);
        }
        catch(Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    private Float getFareAmount(String startStop, String lastStop) {
        int startStopNumber = EStops.valueOf(startStop.toUpperCase()).getStopNumber();
        int lastStopNumber = EStops.valueOf(lastStop.toUpperCase()).getStopNumber();
        int totalStopsTravelled = lastStopNumber - startStopNumber;
        float fareAmount = 0;

        int currentHour = LocalDateTime.now().getHour(); // 0 - 23
        int currentDay = LocalDate.now().getDayOfWeek().getValue(); // 1 to 7

        if(currentHour == 23 || currentHour <= 6)
            fareAmount = NIGHT_TIME_TRAVEL_PRICE * totalStopsTravelled;
        else
            fareAmount = DAY_TIME_TRAVEL_PRICE * totalStopsTravelled;

        if(totalStopsTravelled > 5)
            fareAmount = fareAmount - ((fareAmount * LONG_TRAVEL_DISCOUNT_PERCENTAGE) / 100);

        if(currentDay == 6 || currentDay == 7)
            fareAmount = fareAmount - ((fareAmount * WEEKEND_TRAVEL_DISCOUNT_PERCENTAGE) / 100);

        if(fareAmount > MAX_FARE_AMOUNT)
            fareAmount = 10;

        return fareAmount;
    }

    private SmartCardResponse getResponse(SmartCard smartCard, String message) {
        return SmartCardResponse.builder()
            .balance(smartCard.getBalance())
            .message(message)
            .build();
    }
}
