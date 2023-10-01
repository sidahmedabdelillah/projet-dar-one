package com.abdou.daar.automata;

import java.util.ArrayList;
import java.util.HashMap;

public class AutomataState {
    private boolean isFinal;
    public final String id = java.util.UUID.randomUUID().toString();
    private ArrayList<AutomataTransition> epsilonTransitions;
    private HashMap<Character, AutomataTransition> transitions;

    public AutomataState(boolean isFinal) {
        this.isFinal = isFinal;
        this.epsilonTransitions = new ArrayList<AutomataTransition>();
        this.transitions = new HashMap<Character, AutomataTransition>();
    }

    public void setIsFinal(boolean end) {
        this.isFinal = end;
    }

    public void addTransition(char ch, AutomataTransition transition) {
        this.transitions.put(ch, transition);
    }

    public void addEpsilonTransition(AutomataTransition transition) {
        this.epsilonTransitions.add(transition);
    }
}
