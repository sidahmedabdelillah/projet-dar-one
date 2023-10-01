package com.abdou.daar.cli;

import com.abdou.daar.parser.RegexParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

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

    public String call() throws  Exception {
        String pattern = this.pattern;
        String file1 = this.files[0];

        RegexParser parser = new RegexParser(pattern);


        System.out.println("pattern: " + pattern);
        System.out.println("file: " + file1);

        try{
            String content = new String(Files.readAllBytes(Paths.get(file1)));
            System.out.println(content);

        }catch(IOException e){
            throw new CliIoException(file1);
        }
        return this.files.toString();
    }
}
