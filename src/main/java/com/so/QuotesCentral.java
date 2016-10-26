package com.so;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuotesCentral {

    private MessageServer messageServer = null;
    private final QuoteProvider quoteProvider = QuoteProvider.getInstance();

    public List<Quote> getQuotes() {
        return quoteProvider.generateQuotes();
    }

    public List<Quote> findByAuthor(final String author) {
        return getQuotes().stream().filter(q -> q.getAuthor().equals(author)).collect(Collectors.toList());
    }

    private MessageServer getMessageServer() {
        return Optional.ofNullable(messageServer).orElseGet(() -> messageServer = new MessageServer());
    }

    private boolean publishQuotes(List<Quote> quotes) {
        try {
            getMessageServer().connect();
            return getMessageServer().publish(quotes);
        } finally {
            getMessageServer().disconnect();
        }
    }

    public boolean publishQuotesByAuthor(String name) {
        return publishQuotes(findByAuthor(name));
    }

}
