package com.assignment.gds.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.gds.contract.user.SignupRequest;
import com.assignment.gds.entity.SmartCard;
import com.assignment.gds.entity.User;
import com.assignment.gds.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompletableFuture<User> add(SignupRequest signupRequest) {
        return CompletableFuture.completedFuture(new SmartCard())
            .thenApply(smartCard -> getUser(signupRequest, smartCard))
            .thenApply(userRepository::save);
    }

    private User getUser(SignupRequest signupRequest, SmartCard smartCard) {
        return User.builder()
            .username(signupRequest.getUsername())
            .smartCard(smartCard)
            .build();
    }
}
