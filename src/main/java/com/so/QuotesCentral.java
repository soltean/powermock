package com.so;

import java.util.List;
import java.util.stream.Collectors;

public class QuotesCentral {

    private final QuoteProvider quoteProvider = new QuoteProvider();
    private final List<Quote> quotes = quoteProvider.generateQuotes();
    private final MessageServer messageServer = new MessageServer();

    public List<Quote> getQuotes() {
        return quotes;
    }

    public List<Quote> findByAuthor(final String author) {
        return getQuotes().stream().filter(q -> q.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public List<Quote> findByRating(int rating) {
        return getQuotes().stream().filter(q -> q.getRating() == rating).collect(Collectors.toList());
    }

    public Quote getQuoteOfTheDay() {
        return quoteProvider.randomQuote();
    }

    public void publishQuotes(List<Quote> quotes) {
        messageServer.connect();
        messageServer.publish(quotes);
        messageServer.disconnect();
    }

}
