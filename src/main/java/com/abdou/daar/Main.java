package com.abdou.daar;

import com.abdou.daar.automata.*;
import com.abdou.daar.cli.CLI;
import com.abdou.daar.cli.CommandLine;
import com.abdou.daar.parser.RegexParser;
import com.abdou.daar.parser.RegexTreeNode;
import com.abdou.daar.parser.UnexpectedCharException;

import javax.swing.*;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    //REGEX
    private static String regEx;

    public static void main(String[] arg)
    {
        try {


        int exitCode = new CommandLine(new CLI()).execute(arg);
        System.exit(exitCode);
        String regEx = "a|(b(c*))d";
        RegexParser parser = new RegexParser(regEx);
        RegexTreeNode tree = parser.parse();
        NfaAutomata auto = AutomatBuilder.automatafromRegexTree(tree);

        System.out.println(auto.getStart().toString());
        System.out.println("*************");
        // NfaVisualization visual = new NfaVisualization(auto.getStart());
        AutomataState dfaStart = NfaToDfaConverter.convertNFAToDFA(auto.getStart());
        System.out.println(dfaStart.toString());

        System.out.println("ssbcccd -> " + Arrays.toString(DfaMatcher.matchDFA(dfaStart, "bcccd")));
        System.out.println("ssbcccxdd -> " + Arrays.toString(DfaMatcher.matchDFA(dfaStart, "bcccxdd")));
        System.out.println("ssbcd -> " + Arrays.toString(DfaMatcher.matchDFA(dfaStart, "bcd")));



        return;
        } catch (UnexpectedCharException e) {
            System.out.println(e.getMessage());
        }
    }

}