package com.abdou.daar.parser;

public class UnvalidRegexException extends Exception {
    public UnvalidRegexException(String expression) {
        super("The expression \"" + expression + "\" is not a valid regular expression.");
    }
}
