package com.ds.quackbooks.exceptions;

public class APIException extends RuntimeException{

    public APIException(){}

    public APIException(String message){
        super(message);
    }
}
