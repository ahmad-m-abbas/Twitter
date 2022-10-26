package org.example.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message){
        super("User "+ message + "does not exist") ;
    }
}
