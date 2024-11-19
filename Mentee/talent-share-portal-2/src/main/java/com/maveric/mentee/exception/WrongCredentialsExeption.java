package com.maveric.mentee.exception;

public class WrongCredentialsExeption  extends RuntimeException{
    public WrongCredentialsExeption(String message) {
        super(message);
    }
}
