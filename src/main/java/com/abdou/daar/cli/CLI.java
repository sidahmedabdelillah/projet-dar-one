package com.abdou.daar.cli;

import com.abdou.daar.automata.*;
import com.abdou.daar.helpers.DotExcuter;
import com.abdou.daar.helpers.StringHelper;
import com.abdou.daar.kmp.KMP;
import com.abdou.daar.parser.RegexParser;
import com.abdou.daar.parser.RegexTreeNode;
import com.abdou.daar.parser.UnexpectedCharException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@CommandLine.Command(name = "egrep", description = "an egrep clone", mixinStandardHelpOptions = true, version = "egrep version jumia")
public class CLI implements Callable<String> {
    @CommandLine.Parameters(index = "0", description = "the pattern to search for")
    private String pattern;
    @CommandLine.Parameters(index = "1..", description = "the files to search in")
    private String[] files;
    @CommandLine.Option(names = { "-i" , "--ignore-case"}, description = "ignore case distinctions")
    private boolean ignoreCase;

    @CommandLine.Option(names = { "--force-dfa"}, description = "force using DFA algorithm")
    private boolean forceKmp = false;

    @CommandLine.Option(names= {"-l" , "lines"} , description = "Show line number of found pattern")
    private Boolean showLine = false;

    @CommandLine.Option(names = { "-d", "--dfa"}, description = "generate a visualisation of the dfa save it and open it")
    private Boolean showDfa = false;

    @CommandLine.Option(names = { "-n", "--nfa"}, description = "generate a visualisation of the nfa save it and open it")
    private Boolean showNfa = false;

    private void searchWithDfa(String[] lines) throws UnexpectedCharException, IOException, InterruptedException {
        System.out.println("Searching using DFA");
        RegexParser parser = new RegexParser(pattern);
        RegexTreeNode tree = parser.parse();

        NfaAutomata nfa = AutomatBuilder.automatafromRegexTree(tree);
        AutomataState dfa = NfaToDfaConverter.convertNFAToDFA(nfa.getStart());

        int lineCount = 0;
        for(String line : lines){
            int[] match = DfaMatcher.matchWithPos(dfa, line);
            if(match != null){
                // print line from start to end
                if(showLine){
                    System.out.println("Found match on line " + StringHelper.formatTextInRed(String.valueOf(lineCount + 1)));
                }
                System.out.println(StringHelper.formatSubstringInGreen(line, match[0], match[1] + 1));
                //System.out.println(StringHelper.formatSubstringInGreen(line, match[0], match[1]));
            }
            lineCount++;
        }

        if(this.showDfa){
            DotExcuter.displayDotGraph(dfa.toDot(), "dfa.png");
        }

        if(this.showNfa){
            DotExcuter.displayDotGraph(nfa.getStart().toDot(), "nfa.png");
        }
    }

    private void searchWithKMP(String[] lines){
        System.out.println("Searching using KMP");

        int lineCount = 0;
        KMP kmpMatcher = new KMP(pattern);
        for(String line : lines){
            List<Integer> match = kmpMatcher.searchKMP(line);
            //int[] match = DfaMatcher.matchWithPos(dfa, line);
            if(match.size() > 0){
                // print line from start to end
                if(showLine){
                    System.out.println("Found match on line " + StringHelper.formatTextInRed(String.valueOf(lineCount + 1)));
                }
                System.out.println(StringHelper.formatSubstringInGreen(line, match.get(0), match.get(0) + pattern.length()));
                //System.out.println(StringHelper.formatSubstringInGreen(line, match[0], match[1]));
            }
            lineCount++;
        }
    }

    private boolean isRegexPattern(){
        char[] specialCharacters = {'*', '+', '|', '(', ')'};

        for (char specialChar : specialCharacters) {
            if (this.pattern.indexOf(specialChar) != -1) {
                return true;
            }
        }

        return false;
    }

    public String call() throws  Exception {

        String file1 = this.files[0];
        if(ignoreCase){
            this.pattern = this.pattern.toLowerCase();
        }

        try{
            String content = new String(Files.readAllBytes(Paths.get(file1)));
            if(this.ignoreCase){
                content = content.toLowerCase();
            }
            String[] lines = content.split("\n");

            if(isRegexPattern() | forceKmp){
                searchWithDfa(lines);
            }else{
                searchWithKMP(lines);
            }

        }catch(IOException e){
            throw new CliIoException(file1);
        }


        return "";
    }
}
