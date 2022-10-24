package org.example.exceptions;

public class TextOverflowException extends Exception{
    public TextOverflowException(String message){
        super("The text for the tweet is too large");
    }
}
