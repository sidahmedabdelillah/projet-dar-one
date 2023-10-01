package com.abdou.daar;

import com.abdou.daar.automata.AutomatBuilder;
import com.abdou.daar.automata.NfaAutomata;
import com.abdou.daar.cli.CLI;
import com.abdou.daar.cli.CommandLine;
import com.abdou.daar.parser.RegexParser;
import com.abdou.daar.parser.RegexTree;
import com.abdou.daar.parser.RegexTreeNode;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    //REGEX
    private static String regEx;

    public static void main(String[] arg) {
        int exitCode = new CommandLine(new CLI()).execute(arg);

        return;
    }

}