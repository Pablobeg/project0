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
        String hashedPass=BCrypt.hashpw(password,BCrypt.gensalt(5));
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

    public User loginUser(String username, String email ,String rawPassword){

        User checkinUser = userDao.getUserByUsername(username);
        //If user get data and password is the same with data base return true
        if(checkinUser!=null && BCrypt.checkpw(rawPassword, checkinUser.getPassword())){
            return checkinUser;
        }
        //comparing if checkingUser was null or password was incorrect return null
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
