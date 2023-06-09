package com.lcwd.electronicstore.exception;

public class BadApiRequestException extends RuntimeException{

    public BadApiRequestException(String message){

        super(message);
    }

    public BadApiRequestException(){

        super("Bad request!!");
    }
}
