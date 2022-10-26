package org.example.exceptions;

public class TweetNotFoundException extends Exception{
    public TweetNotFoundException(String message){
        super("Tweet " + message + " does not exits");
    }
}
