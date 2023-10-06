package com.abdou.daar.helpers;

public class StringHelper {
    public static String formatTextInGreen(String text) {
        String greenColorCode = "\u001B[32m";
        String resetColorCode = "\u001B[0m";

        return greenColorCode + text + resetColorCode;
    }

    public static String formatTextInRed(String text) {
        // ANSI escape code for red color
        String redColorCode = "\u001B[31m";
        // ANSI escape code to reset color
        String resetColorCode = "\u001B[0m";

        // Format the text in red and reset the color
        return redColorCode + text + resetColorCode;
    }

    public static String formatSubstringInGreen(String text, int startPos, int endPos) {
        // Validate input positions
        if (startPos < 0 || startPos >= text.length() || endPos <= startPos || endPos > text.length()) {
            throw new IllegalArgumentException("Invalid positions start " + startPos + " end " + endPos );
        }



        // Create a StringBuilder to modify the text

        // Append the text before the green part

        return text.substring(0, startPos) +

                // Append the green-colored part
                formatTextInGreen(text.substring(startPos, endPos)) +

                // Append the text after the green part
                text.substring(endPos);
    }
}
