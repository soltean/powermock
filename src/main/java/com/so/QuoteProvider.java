package com.so;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteProvider {

    private static final String FILE_NAME = "quotes.txt";

    public List<Quote> generateQuotes() {
        try {
            return Files.lines(Paths.get(ClassLoader.getSystemResource(FILE_NAME).toURI()))
                    .map(line -> line.split(","))
                    .map(arr -> new Quote(arr[0], arr[1], Integer.parseInt(arr[2])))
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
