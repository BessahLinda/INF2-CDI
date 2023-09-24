package dev.miage.inf2.course.cdi.exception;

public class SaleNotPermittedException extends RuntimeException {
    public SaleNotPermittedException(String msg) {
        super(msg);
    }

}
