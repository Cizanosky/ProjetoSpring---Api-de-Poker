package api.poker.application.exceptions;

public class MatchInProgressException extends RuntimeException{
    public MatchInProgressException(){}

    public MatchInProgressException(String message){
        super(message);
    }
}
