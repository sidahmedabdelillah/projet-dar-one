package com.abdou.daar.automata;

public class NfaAutomata {
    private AutomataState start;
    private AutomataState end;

    public NfaAutomata(AutomataState start, AutomataState end) {
        this.start = start;
        this.end = end;
    }

    AutomataState getStart() {
        return this.start;
    }

    AutomataState getEnd() {
        return this.end;
    }

}
