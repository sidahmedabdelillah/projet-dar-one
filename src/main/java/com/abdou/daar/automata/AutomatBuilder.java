package com.abdou.daar.automata;

import com.abdou.daar.parser.RegexTreeNode;

public class AutomatBuilder {
    static NfaAutomata fromEpsilon() {
        AutomataState start = new AutomataState(false);
        AutomataState end = new AutomataState(true);
        start.addEpsilonTransition(new AutomataTransition(end));
        return new NfaAutomata(start, end);
    }

    static NfaAutomata nfaAutomatafromSymbol(char symbol) {
        AutomataState start = new AutomataState(false);
        AutomataState end = new AutomataState(true);
        start.addTransition(symbol, new AutomataTransition(end));
        return new NfaAutomata(start, end);
    }

    static NfaAutomata nfaAutomataConcat(NfaAutomata first, NfaAutomata second) {
        first.getEnd().addEpsilonTransition(new AutomataTransition(second.getStart()));

        first.getEnd().setIsFinal(false);

        return new NfaAutomata(first.getStart(), second.getEnd());
    }

    static NfaAutomata nfaAutomataUnion(NfaAutomata first, NfaAutomata second) {
        AutomataState start = new AutomataState(false);

        start.addEpsilonTransition(new AutomataTransition(first.getStart()));
        start.addEpsilonTransition(new AutomataTransition(second.getStart()));

        AutomataState end = new AutomataState(true);

        first.getEnd().addEpsilonTransition(new AutomataTransition(end));
        first.getEnd().setIsFinal(false);
        second.getEnd().addEpsilonTransition(new AutomataTransition(end));
        second.getEnd().setIsFinal(false);

        return new NfaAutomata(start, end);
    }

    static NfaAutomata nfaAutomataClosure(NfaAutomata nfaAutomata) {
        AutomataState start = new AutomataState(false);
        AutomataState end = new AutomataState(true);

        start.addEpsilonTransition(new AutomataTransition(end));
        start.addEpsilonTransition(new AutomataTransition(nfaAutomata.getStart()));

        nfaAutomata.getEnd().addEpsilonTransition(new AutomataTransition(end));
        nfaAutomata.getEnd().addEpsilonTransition(new AutomataTransition(nfaAutomata.getStart()));

        nfaAutomata.getEnd().setIsFinal(false);

        return new NfaAutomata(start, end);
    }

    static NfaAutomata oneOrMoreAutomata(NfaAutomata nfa) {
        AutomataState start = new AutomataState(false);
        AutomataState end = new AutomataState(true);

        start.addEpsilonTransition(new AutomataTransition(nfa.getStart()));
        nfa.getEnd().addEpsilonTransition(new AutomataTransition(nfa.getEnd()));
        nfa.getEnd().addEpsilonTransition(new AutomataTransition(nfa.getStart()));

        nfa.getEnd().setIsFinal(false);

        return new NfaAutomata(start, end);
    }

    static NfaAutomata zeroOrMoreAutomata(NfaAutomata nfa) {
        AutomataState start = new AutomataState(false);
        AutomataState end = new AutomataState(true);

        start.addEpsilonTransition(new AutomataTransition(end));
        start.addEpsilonTransition(new AutomataTransition(nfa.getStart()));

        nfa.getEnd().addEpsilonTransition(new AutomataTransition(end));

        nfa.getEnd().setIsFinal(false);

        return new NfaAutomata(start, end);
    }

    public static NfaAutomata automatafromRegexTree(RegexTreeNode tree) {
        if (tree.isExpression()) {
            NfaAutomata term = automatafromRegexTree(tree.getFirstChild());
            if (tree.getChildrenSize() == 3) {
                return nfaAutomataUnion(term, automatafromRegexTree(tree.getNthChild(2)));
            }

            return term;
        }

        if (tree.isTerm()) {
            NfaAutomata factor = automatafromRegexTree(tree.getFirstChild());
            if (tree.getChildrenSize() == 2) {
                return nfaAutomataConcat(factor, automatafromRegexTree(tree.getNthChild(1)));
            }
            return factor;
        }

        if (tree.isFactor()) {
            NfaAutomata atom = automatafromRegexTree(tree.getFirstChild());

            if (tree.getChildrenSize() == 2) {
                char meta = tree.getNthChild(1).getValue();
                return switch (meta) {
                    case '*' -> nfaAutomataClosure(atom);
                    // Todo complete this
                    case '?' -> zeroOrMoreAutomata(atom);
                    case '+' -> oneOrMoreAutomata(atom);
                    default -> throw new RuntimeException("Invalid meta character  " + meta);
                };
            }
            return atom;
        }

        if (tree.isAtom()) {
            if (tree.getChildrenSize() == 3) {
                return automatafromRegexTree(tree.getNthChild(1));
            }
            return automatafromRegexTree(tree.getFirstChild());
        }

        if (tree.isCharacter()) {
            if (tree.getChildrenSize() == 2) {
                return nfaAutomatafromSymbol(tree.getNthChild(1).getValue());
            }

            return nfaAutomatafromSymbol(tree.getFirstChild().getValue());
        }

        throw new RuntimeException("Invalid regex tree");
    }
}
