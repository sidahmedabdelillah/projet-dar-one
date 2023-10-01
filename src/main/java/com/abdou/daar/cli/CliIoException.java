package com.abdou.daar.cli;

public class CliIoException extends Exception{
    public CliIoException(String file) {
        super("Cant read file: " + file);
    }
}
