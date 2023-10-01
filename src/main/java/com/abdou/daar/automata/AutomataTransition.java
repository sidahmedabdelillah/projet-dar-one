package com.abdou.daar.automata;

public class AutomataTransition{

    private final AutomataState to;
    private boolean isFinal;
    public AutomataTransition(AutomataState to) {
        this.to = to;
    }

    public AutomataTransition(AutomataState to, boolean isFinal) {
        this.to = to;
        this.isFinal = isFinal;
    }

    public String toString(){
        return "{" + "\"to\": " +
                this.to +
                " ,\"isFinal\":" +
                this.isFinal +
                "}";
    }

    public AutomataState getToNode() {
        return to;
    }
    public boolean isFinal(){
        return this.isFinal;
    }
}
