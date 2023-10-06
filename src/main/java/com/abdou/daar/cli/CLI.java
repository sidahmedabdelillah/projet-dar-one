package com.abdou.daar.cli;

import com.abdou.daar.automata.*;
import com.abdou.daar.helpers.DotExcuter;
import com.abdou.daar.helpers.StringHelper;
import com.abdou.daar.parser.RegexParser;
import com.abdou.daar.parser.RegexTreeNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

    @CommandLine.Option(names = { "-f", "--file"}, description = "take the pattern from a file")
    private String patternFile;

    @CommandLine.Option(names= {"-l" , "lines"} , description = "Show line number of found pattern")
    private Boolean showLine = false;

    @CommandLine.Option(names = { "-d", "--dfa"}, description = "generate a visualisation of the dfa save it and open it")
    private Boolean showDfa = false;

    @CommandLine.Option(names = { "-n", "--nfa"}, description = "generate a visualisation of the nfa save it and open it")
    private Boolean showNfa = false;

    public String call() throws  Exception {
        String pattern = this.pattern;
        String file1 = this.files[0];

        if(ignoreCase){
            pattern = pattern.toLowerCase();
        }

        RegexParser parser = new RegexParser(pattern);

        RegexTreeNode tree = parser.parse();

        NfaAutomata auto = AutomatBuilder.automatafromRegexTree(tree);
        AutomataState dfaStart = NfaToDfaConverter.convertNFAToDFA(auto.getStart());

        try{
            String content = new String(Files.readAllBytes(Paths.get(file1)));
            String[] lines = content.split("\n");

            int lineCount = 0;
            for(int idx=0 ;idx < lines.length ; idx++){
                String line = lines[idx];
                if(ignoreCase){
                    line = line.toLowerCase();
                }
                int[] match = DfaMatcher.matchWithPos(dfaStart, line);
                if(match != null){
                    // print line from start to end
                    if(showLine){
                        System.out.println("Found match on line " + StringHelper.formatTextInRed(String.valueOf(idx + 1)));
                    }
                    System.out.println(StringHelper.formatSubstringInGreen(line, match[0], match[1] + 1));
                    //System.out.println(StringHelper.formatSubstringInGreen(line, match[0], match[1]));
                }
            }

        }catch(IOException e){
            throw new CliIoException(file1);
        }

        if(this.showDfa){
            DotExcuter.displayDotGraph(dfaStart.toDot(), "dfa.png");
        }

        if(this.showNfa){
            DotExcuter.displayDotGraph(auto.getStart().toDot(), "nfa.png");
        }


        return "";
    }
}
