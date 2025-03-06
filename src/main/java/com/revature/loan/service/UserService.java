package com.revature.loan.service;

import com.revature.loan.dao.UserDao;
import com.revature.loan.model.User;
import jakarta.servlet.http.HttpSession;
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

    public boolean loginUser(String username, String email ,String rawPassword){

        User checkinUser = userDao.getUserByUsername(username);
        //If user get data and password is the same with data base return true
        if(checkinUser!=null && BCrypt.checkpw(rawPassword, checkinUser.getPassword())){
            return true;
        }
        //comparing if checkingUser was null or password was incorrect return false
        return false;



    }


}
