package com.abdou.daar;

import com.abdou.daar.automata.AutomatBuilder;
import com.abdou.daar.automata.NfaAutomata;
import com.abdou.daar.parser.RegexParser;
import com.abdou.daar.parser.RegexTree;
import com.abdou.daar.parser.RegexTreeNode;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    //REGEX
    private static String regEx;

    public static void main(String[] arg) {
        System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
//        if (arg.length!=0) {
//            regEx = arg[0];
//        } else {
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("  >> Please enter a regEx: ");
//            regEx = scanner.next();
//        }
        regEx = "(a|b)*c";
        System.out.println("  >> Parsing regEx \"" + regEx + "\".");
        System.out.println("  >> ...");


        try {
            RegexParser parser = new RegexParser(regEx);
            RegexTreeNode tree = parser.parse();
            NfaAutomata automata = AutomatBuilder.automatafromRegexTree(tree);
            System.out.println(automata);
        } catch (Exception e) {
            System.out.println("  >> Error: " + e.getMessage());
        }


        System.out.println("  >> ...");
        System.out.println("  >> Parsing completed.");
        System.out.println("Goodbye Mr. Anderson.");
    }

}