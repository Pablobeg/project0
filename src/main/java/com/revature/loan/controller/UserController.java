package com.revature.loan.controller;

import com.revature.loan.dto.UserAuthRequestDTO;
import com.revature.loan.dto.UserUpdateDTO;
import com.revature.loan.model.User;
import com.revature.loan.service.UserService;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

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

        boolean success = userService.registerUser(req.getUsername(), req.getEmail(), req.getPassword());
        if (success) {
            ctx.status(201).json("{\"message\":\"User registered successfully\"}");
        } else {
            ctx.status(409).json("{\"error\":\"Username already exists\"}");
        }
    }
    /**
     * Handles Post /auth/login with a JSON body:
     * {
     *    "username": "someName",
     *    "email":"someEmail",
     *    "password": "somePass"
     * }
     */

    public void login(Context ctx){
        UserAuthRequestDTO requestUser = ctx.bodyAsClass(UserAuthRequestDTO.class);
        if (requestUser.getUsername() == null || requestUser.getPassword() == null || requestUser.getEmail()==null) {
            ctx.status(400).json("{\"error\":\"Missing username, password or email\"}");
        }

        // Check credentials. dbUser makes it clear we got this data from the db after verifying with the requestUser.
        User check= userService.loginUser(requestUser.getUsername(), requestUser.getEmail(), requestUser.getPassword());
        if (check ==null) {
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
            return;
        }



        // If valid, start a session
        HttpSession session = ctx.req().getSession(true);

        session.setAttribute("user_id", check.getId());  // Guardar el ID del usuario en la sesión
        session.setAttribute("username", check.getName());
        session.setAttribute("role", check.getRol());
        ctx.status(200).json(requestUser);
    }

    public void logout(Context ctx) {
        HttpSession session = ctx.req().getSession(false); // Obtener la sesión sin crear una nueva

        if (session != null) {
            session.invalidate(); // Close the session if there is one
            ctx.status(200).json("{\"message\":\"Logged out\"}");
        } else {
            ctx.status(400).json("{\"error\":\"No active session\"}"); // If there is not session active
        }
    }

    private static boolean checkSession(Context ctx) {
        HttpSession session = ctx.req().getSession(false); // Don't creat a new session if doesn't exist
        return session != null && session.getAttribute("user_id") != null;
    }

    /**
     * Handles get /users/{id}
     */

    public void getUser(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id user from url
        String id= ctx.pathParam("id");
        int userId= Integer.parseInt(id);
        //Getting id user, role from session actual
        HttpSession session= ctx.req().getSession(false);
        int sessionUserId= (int) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        boolean isAdmin = role != null && role.equals("admin");

        //Checking if the current user is admin and if not block them

        if(!isAdmin && sessionUserId != userId){
            ctx.status(403).json("{\\\"error\\\":\\\"Unauthorized access\\\"}");
            return;
        }

        User user =userService.getUserById(userId);
        if(user==null){
            ctx.status(404).json("{\"error\":\"User not found\"}");

        }else{
            UserAuthRequestDTO userDTO = new UserAuthRequestDTO();
            userDTO.setUsername(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());

            ctx.json(userDTO);
        }




    }

    /**
     * Handles put /users/{id} with a JSON body:
     * {
     *    "username": "someName",
     *    "email":"someEmail",
     * }
     */


    public void updateUser(Context ctx){
        if (!checkSession(ctx)){
            ctx.status(400).json("{\"error\":\"No active session\"}");
            return;
        }
        //Getting id user from url
        String id= ctx.pathParam("id");
        int userId= Integer.parseInt(id);
        //Getting id user, role from session actual
        HttpSession session= ctx.req().getSession(false);
        int sessionUserId= (int) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");
        boolean isAdmin = role != null && role.equals("admin");



        //Checking if the current user is admin and if not block them

        if(!isAdmin && sessionUserId != userId){
            ctx.status(403).json("{\"error\":\"Unauthorized access\"}");
            return;
        }

        //Body data to object
        UserUpdateDTO userUpdate = ctx.bodyAsClass(UserUpdateDTO.class);

        if (userUpdate.getName() == null || userUpdate.getEmail() == null) {
            ctx.status(400).json("{\"error\":\"Missing username or email\"}");
            return;
        }

        //Checking if user exist
        User user = userService.getUserById(userId);
        if(user==null){
            ctx.status(404).json("{\"error\":\"User not found\"}");
            return;
        }else{

            //Passing data to user object
            User newUser=userService.updateUser(userId, userUpdate.getName(), userUpdate.getEmail());

            ctx.json(newUser);
        }
    }




}
