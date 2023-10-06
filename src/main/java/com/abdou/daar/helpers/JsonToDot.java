package com.abdou.daar.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonToDot {
    private static class TransitionNode {
        String id;
        List<Transition> transitions;

        public TransitionNode(String id, List<Transition> transitions) {
            this.id = id;
            this.transitions = transitions;
        }
    }

    private static class Transition {
        String label;
        String to;

        public Transition(String label, String to) {
            this.label = label;
            this.to = to;
        }
    }

    public static String jsonToDot(String data) {
        String[] stateArray = data.split("State: ");
        List<TransitionNode> transitions = new ArrayList<>();
        for (String state : stateArray) {
            String[] lines = state.split("\n");
            String id = lines[0];
            if(id.isEmpty()) continue;
            List<Transition> transitionList = new ArrayList<>();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                String[] parts = line.split(" -> ");
                if (parts.length == 2) {
                    String cost = parts[0].trim();
                    String to = parts[1].trim();
                    String[] filtered = Arrays.stream(cost.split("->")).filter(s ->  !s.isEmpty()).toArray(String[]::new);// cost.split("->");
                    if (filtered.length > 0 && !filtered[0].isEmpty()) {
                        String label = filtered[0].trim();
                        transitionList.add(new Transition(label, to));
                    }
                }
            }

            transitions.add(new TransitionNode(id, transitionList));
        }


        StringBuilder dotCode = new StringBuilder("digraph G {\n");

        for (TransitionNode node : transitions) {
            for (Transition transition : node.transitions) {
                dotCode.append("  \"").append(node.id).append("\" -> \"").append(transition.to).append("\" [label=\"").append(transition.label).append("\"];\n");
            }
        }

        dotCode.append("}\n");
        return dotCode.toString();
    }


}
