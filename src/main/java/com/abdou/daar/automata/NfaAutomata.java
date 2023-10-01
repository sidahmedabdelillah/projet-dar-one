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

    public String toString() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"start\":");
        json.append(this.start.toString());
        json.append(",\"end\":");
        json.append(this.end.toString());
        json.append("}");
        return json.toString();
    }

}
