package com.abdou.daar.automata;

import com.abdou.daar.helpers.JsonToDot;

import java.util.*;

public class AutomataState {
    private static int idCounter = 0;
    private boolean isFinal;
    public final String id ;
    private final ArrayList<AutomataTransition> epsilonTransitions;
    private final HashMap<Character, AutomataTransition> transitions;

    public AutomataState(boolean isFinal) {
        this.isFinal = isFinal;
        this.epsilonTransitions = new ArrayList<>();
        this.transitions = new HashMap<>();
        this.id = "q"+idCounter++;
    }

    public void setIsFinal(boolean end) {
        this.isFinal = end;
    }


    public boolean isFinal() {
        return isFinal;
    }

    public ArrayList<AutomataTransition> getEpsilonTransitions() {
        return epsilonTransitions;
    }

    public HashMap<Character, AutomataTransition> getTransitions() {
        return transitions;
    }

    public void addTransition(char ch, AutomataTransition transition) {
        this.transitions.put(ch, transition);
    }

    public void addEpsilonTransition(AutomataTransition transition) {
        this.epsilonTransitions.add(transition);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> visited = new HashSet<>();

        // Use a queue for breadth-first traversal
        Queue<AutomataState> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            AutomataState currentState = queue.poll();

            // Skip if already visited
            if (visited.contains(currentState.id)) {
                continue;
            }

            visited.add(currentState.id);

            sb.append("State: ").append(currentState.id);
            if (currentState.isFinal()) {
                sb.append(" (Final)");
            }
            sb.append("\n");
            System.out.println();

            // Print transitions
            for (char ch : currentState.getTransitions().keySet()) {
                AutomataTransition transition = currentState.getTransitions().get(ch);
                AutomataState nextState = transition.getToNode();
                if(nextState == null){
                    continue;
                }
                sb.append("  -> '").append(ch).append("' -> ").append(nextState.id).append("\n");
                queue.add(nextState);
            }

            // Print epsilon transitions
            for (AutomataTransition epsilonTransition : currentState.getEpsilonTransitions()) {
                AutomataState epsilonNextState = epsilonTransition.getToNode();
                sb.append("  -> ε -> ").append(epsilonNextState.id).append("\n");
                queue.add(epsilonNextState);
            }

        }

        return sb.toString();
    }

    public String toDot(){
        return JsonToDot.jsonToDot(this.toString());
    }
}
