package uz.nt.minirailway.exception;

public class WrongSearchException extends RuntimeException{
    public WrongSearchException(String message) {
        super(message);
    }
}
