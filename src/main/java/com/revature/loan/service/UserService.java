package com.revature.loan.service;

import com.revature.loan.dao.UserDao;
import com.revature.loan.model.User;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private String hashedPassword(String password){
        //Hashing the password with bcrypt
        String hashedPass=BCrypt.hashpw(password,BCrypt.gensalt(5));
        return hashedPass;
    }

    public boolean registerUser(String username, String email ,String rawPassword) {
        // Typically, you'd hash this with BCrypt or Argon2
        String hashed = hashedPassword(rawPassword);

        if (userDao.getUserByUsername(username) != null) {
            logger.warn("Try to register with name already store: {}", username);
            return false; // username exists
        }

        User newUser = new User();
        newUser.setName(username);
        newUser.setPassword(hashed);
        newUser.setEmail(email);
        userDao.createUser(newUser);
        logger.info("User was registered: {}", username);
        return true;
    }

    public User loginUser(String username, String email ,String rawPassword){

        User checkinUser = userDao.getUserByUsername(username);
        //If user get data and password is the same with data base return true
        if(checkinUser!=null && BCrypt.checkpw(rawPassword, checkinUser.getPassword())){
            logger.info("Loggin succesfull: {}", username);
            return checkinUser;
        }
        //comparing if checkingUser was null or password was incorrect return null
        logger.warn("Login falied: {}", username);
        return null;

    }

    public User getUserById(int id){
        User userByID =userDao.getUserByID(id);
        if(userByID!=null){
            return userByID;
        }

        return null;
    }

    public User updateUser(int id, String name, String email){
        User updateUser = userDao.updateUser(id,name, email);

        if(updateUser!=null){
            return updateUser;
        }
        return null;
    }




}
