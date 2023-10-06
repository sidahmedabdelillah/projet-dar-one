package com.abdou.daar.helpers;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DotExcuter {
    public static void displayDotGraph(String dotData, String fileName ) throws IOException, InterruptedException {


        String tempDir = System.getProperty("java.io.tmpdir");
        String dotFileName = java.util.UUID.randomUUID().toString();

        Path tempFilePath = Paths.get(tempDir, dotFileName);

        // write the string in the tempFilePath
        Files.writeString(tempFilePath, dotData);

        // Specify the output image file path

        Path imagePath = Paths.get(fileName);

        // Generate the image using the dot command
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", tempFilePath.toString(), "-o", fileName);
        Process process = processBuilder.start();

        process.waitFor();

        Desktop.getDesktop().open(imagePath.toFile());
    }
}
