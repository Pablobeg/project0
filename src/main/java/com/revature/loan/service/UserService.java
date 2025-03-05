package com.revature.loan.service;

import com.revature.loan.dao.UserDao;
import com.revature.loan.model.User;
import org.mindrot.jbcrypt.BCrypt;


public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private String hashedPassword(String password){
        //Hashing the password with bcrypt
        String hashedPass=BCrypt.hashpw(password,BCrypt.gensalt(12));
        return hashedPass;
    }

    public boolean registerUser(String username, String email ,String rawPassword) {
        // Typically, you'd hash this with BCrypt or Argon2
        String hashed = hashedPassword(rawPassword);

        if (userDao.getUserByUsername(username) != null) {
            return false; // username exists
        }

        User newUser = new User();
        newUser.setName(username);
        newUser.setPassword(hashed);
        newUser.setEmail(email);
        userDao.createUser(newUser);
        return true;
    }


}
