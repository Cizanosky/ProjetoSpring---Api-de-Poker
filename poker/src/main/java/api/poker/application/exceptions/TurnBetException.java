package api.poker.application.exceptions;

public class TurnBetException extends RuntimeException{
    public TurnBetException(){}

    public TurnBetException(String message){
        super(message);
    }
}
