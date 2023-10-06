package com.abdou.daar.kmp;

import java.util.ArrayList;
import java.util.List;

public class KMP {
    private final String pattern ;
    private int[] matchTable;
    public KMP(String pattern){
        this.pattern = pattern;
        this.matchTable = buildPartialMatchTable(pattern);
    }

    private int[] buildPartialMatchTable(String pattern) {
        int len = pattern.length();
        int[] table = new int[len];
        int j = 0;

        for (int i = 1; i < len; ) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                table[i] = j + 1;
                j++;
                i++;
            } else {
                if (j != 0) {
                    j = table[j - 1];
                } else {
                    table[i] = 0;
                    i++;
                }
            }
        }

        return table;
    }

    public  List<Integer> searchKMP(String text) {
        List<Integer> positions = new ArrayList<>();
        int textLength = text.length();
        int patternLength = pattern.length();
        int i = 0; // Index for text
        int j = 0; // Index for pattern

        while (i < textLength) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
                if (j == patternLength) {
                    // Pattern found at index i - patternLength
                    positions.add(i - patternLength);
                    j = matchTable[j - 1];
                }
            } else {
                if (j != 0) {
                    j = matchTable[j - 1];
                } else {
                    i++;
                }
            }
        }

        return positions;
    }
}
