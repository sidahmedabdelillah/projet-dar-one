package com.abdou.daar.automata;

public class DfaMatcher {
    public static int[] matchDFA(AutomataState startState, String input){
        if(match(startState, input)){
            return new int[]{0, input.length()};
        }
        return null;
    }

    public static int[] matchWithPos(AutomataState startState, String inputString) {
        for (int i = 0; i < inputString.length(); i++) {
            AutomataState currentState = startState;
            for (int j = i; j < inputString.length(); j++) {
                char c = inputString.charAt(j);
                AutomataTransition transition = currentState.getTransitions().get(c);
                if (transition == null) {
                    // No transition for this character, move to the next starting position
                    break;
                }
                currentState = transition.getToNode();
                if (currentState.isFinal()) {
                    // If a final state is reached, the input is accepted
                    return new int[]{i,j};
                }
            }
        }
        return null; // No match found
    }


public static boolean match(AutomataState startState, String inputString) {
        for (int i = 0; i < inputString.length(); i++) {
            AutomataState currentState = startState;
            for (int j = i; j < inputString.length(); j++) {
                char c = inputString.charAt(j);
                AutomataTransition transition = currentState.getTransitions().get(c);
                if (transition == null) {
                    // No transition for this character, move to the next starting position
                    break;
                }
                currentState = transition.getToNode();
                if (currentState.isFinal()) {
                    // If a final state is reached, the input is accepted
                    return true;
                }
            }
        }
        return false; // Input is not accepted for any starting position
    }
}
