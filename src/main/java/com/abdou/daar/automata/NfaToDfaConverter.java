package com.abdou.daar.automata;

import java.util.*;

public class NfaToDfaConverter {
    public static AutomataState convertNFAToDFA(AutomataState nfaStartState) {
        // Initialize DFA states and transitions
        Set<Set<AutomataState>> dfaStates = new HashSet<>();
        Map<Set<AutomataState>, AutomataState> dfaStateMap = new HashMap<>();
        Set<Character> alphabet = computeAlphabet(nfaStartState);

        // Initialize the first DFA state with the epsilon closure of the NFA start state
        Set<AutomataState> nfaStartClosure = epsilonClosure(nfaStartState);
        dfaStates.add(nfaStartClosure);
        AutomataState dfaStartState = new AutomataState(isFinalState(nfaStartClosure));
        dfaStateMap.put(nfaStartClosure, dfaStartState);

        Queue<Set<AutomataState>> unprocessedStates = new LinkedList<>();
        unprocessedStates.add(nfaStartClosure);

        // Create the equivalent DFA states and transitions
        while (!unprocessedStates.isEmpty()) {
            Set<AutomataState> currentNFAStates = unprocessedStates.poll();
            AutomataState currentDFAState = dfaStateMap.get(currentNFAStates);

            for (char symbol : alphabet) {
                Set<AutomataState> nextNFAStates = new HashSet<>();
                for (AutomataState nfaState : currentNFAStates) {
                    if (nfaState.getTransitions().containsKey(symbol)) {
                        nextNFAStates.addAll(epsilonClosure(nfaState.getTransitions().get(symbol).getToNode()));
                    }
                }

                if (!nextNFAStates.isEmpty()) {
                    if (!dfaStates.contains(nextNFAStates)) {
                        dfaStates.add(nextNFAStates);
                        AutomataState nextDFAState = new AutomataState(isFinalState(nextNFAStates));
                        dfaStateMap.put(nextNFAStates, nextDFAState);
                        unprocessedStates.add(nextNFAStates);
                        currentDFAState.addTransition(symbol, new AutomataTransition(nextDFAState));
                    } else {
                        AutomataState existingDFAState = dfaStateMap.get(nextNFAStates);
                        currentDFAState.addTransition(symbol, new AutomataTransition(existingDFAState));
                    }
                }
            }
        }

        return dfaStartState;
    }
    public static Set<AutomataState> epsilonClosure(AutomataState state) {
        Set<AutomataState> closure = new HashSet<>();
        Stack<AutomataState> stack = new Stack<>();
        stack.push(state);

        while (!stack.isEmpty()) {
            AutomataState currentState = stack.pop();
            closure.add(currentState);

            for (AutomataTransition transition : currentState.getEpsilonTransitions()) {
                AutomataState nextState = transition.getToNode();
                if (!closure.contains(nextState)) {
                    stack.push(nextState);
                }
            }
        }

        return closure;
    }

    public static boolean isFinalState(Set<AutomataState> states) {
        for (AutomataState state : states) {
            if (state.isFinal()) {
                return true;
            }
        }
        return false;
    }

    public static Set<Character> computeAlphabet(AutomataState startState) {
        Set<Character> alphabet = new HashSet<>();

        Queue<AutomataState> queue = new LinkedList<>();
        Set<AutomataState> visited = new HashSet<>();

        queue.add(startState);

        while (!queue.isEmpty()) {
            AutomataState currentState = queue.poll();
            visited.add(currentState);

            for (char symbol : currentState.getTransitions().keySet()) {
                alphabet.add(symbol);
                AutomataTransition transition = currentState.getTransitions().get(symbol);
                AutomataState nextState = transition.getToNode();

                if (!visited.contains(nextState)) {
                    queue.add(nextState);
                }
            }

            for (AutomataTransition epsilonTransition : currentState.getEpsilonTransitions()) {
                AutomataState epsilonNextState = epsilonTransition.getToNode();
                if (!visited.contains(epsilonNextState)) {
                    queue.add(epsilonNextState);
                }
            }
        }

        return alphabet;
    }

    public static void printDFA(AutomataState startState) {
        Set<AutomataState> visited = new HashSet<>();
        Stack<AutomataState> stack = new Stack<>();
        stack.push(startState);

        while (!stack.isEmpty()) {
            AutomataState currentState = stack.pop();
            visited.add(currentState);

            System.out.println("State: " + currentState.id + (currentState.isFinal() ? " (Final)" : ""));

            for (char symbol : currentState.getTransitions().keySet()) {
                AutomataTransition transition = currentState.getTransitions().get(symbol);
                AutomataState nextState = transition.getToNode();
                System.out.println("  -> '" + symbol + "' -> " + nextState.id);

                if (!visited.contains(nextState)) {
                    stack.push(nextState);
                }
            }
        }
    }
}
