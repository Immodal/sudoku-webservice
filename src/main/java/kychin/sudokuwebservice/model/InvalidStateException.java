package kychin.sudokuwebservice.model;

public class InvalidStateException extends Exception{
    public InvalidStateException(String errorMessage) {
        super(errorMessage);
    }
}
