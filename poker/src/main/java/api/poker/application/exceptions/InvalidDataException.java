package api.poker.application.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(){}

    public InvalidDataException(String message){
        super(message);
    }
}
