package com.abdou.daar.parser;

public class UnexpectedCharException extends Exception {
    public UnexpectedCharException(char expected, char got, int pos) {
        super("Expected Character: " + expected + " got character: " + got + " on position: " + pos);
    }

    public UnexpectedCharException(char expected, String got, int pos) {
        super("Expected Character: " + expected + " got : " + got + " on position: " + pos);
    }
}
