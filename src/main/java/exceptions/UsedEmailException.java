package exceptions;

public class UsedEmailException extends Exception {
    public UsedEmailException(String message) {
        super("The following emails is already in use: " + message);
    }
}