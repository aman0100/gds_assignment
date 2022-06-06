package com.assignment.gds.controller;

import com.assignment.gds.contract.user.SignupRequest;
import com.assignment.gds.entity.User;
import com.assignment.gds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public CompletionStage<ResponseEntity<User>> add(@RequestBody SignupRequest signupRequest) {
        return userService.add(signupRequest)
            .thenApply(ResponseEntity::ok);
    }
}
