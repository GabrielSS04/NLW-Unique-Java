package com.project.nlwjava.exceptions;

public class AttendeeAlreadyInEventException extends RuntimeException{

    public AttendeeAlreadyInEventException(String text){
        super(text);
    }
}
