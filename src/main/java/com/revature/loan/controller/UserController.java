package com.revature.loan.controller;

import com.revature.loan.dto.UserAuthRequestDTO;
import com.revature.loan.service.UserService;
import io.javalin.http.Context;
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles POST /register with a JSON body:
     * {
     *    "username": "someName",
     *    "email":"email",
     *    "password": "somePass"
     * }
     */
    public void register(Context ctx) {
        // Parse request JSON into our DTO
        UserAuthRequestDTO req = ctx.bodyAsClass(UserAuthRequestDTO.class);

        if (req.getUsername() == null || req.getPassword() == null || req.getEmail()==null) {
            ctx.status(400).json("{\"error\":\"Missing username, password or email\"}");
            return;
        }

        boolean success = userService.registerUser(req.getUsername(), req.getPassword(),req.getEmail());
        if (success) {
            ctx.status(201).json("{\"message\":\"User registered successfully\"}");
        } else {
            ctx.status(409).json("{\"error\":\"Username already exists\"}");
        }
    }
}
