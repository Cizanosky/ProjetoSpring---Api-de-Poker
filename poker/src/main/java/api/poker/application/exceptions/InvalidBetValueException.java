package api.poker.application.exceptions;

public class InvalidBetValueException extends RuntimeException{
    public InvalidBetValueException(){}

    public InvalidBetValueException(String message){
        super(message);
    }
}
