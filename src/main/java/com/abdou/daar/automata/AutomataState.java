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

    public String toString() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"id\":");
        json.append("\"").append(this.id).append("\"").append(",");
        json.append("\"isFinal\":");
        json.append(this.isFinal);
        json.append(",\"epsilonTransitions\":");
        // for each epsilon transition
        json.append("[");
        for (AutomataTransition transition : this.epsilonTransitions) {
            json.append(transition.toString());
            json.append(",");
        }
        if (this.epsilonTransitions.size() > 0) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("],");
        json.append("\"transitions\":");
        json.append("{");
        // for each transition
        for (char ch : this.transitions.keySet()) {
            json.append("\"").append(ch).append("\"").append(":");
            json.append(this.transitions.get(ch).toString());
            json.append(",");
        }
        if(!this.transitions.isEmpty()){
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}}");
        return json.toString();
    }


}
