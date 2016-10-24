package com.so;

import java.util.List;
import java.util.stream.Collectors;

public class QuotesCentral {

    private MessageServer messageServer = new MessageServer();
    private QuoteProvider quoteProvider = QuoteProvider.getInstance();

    public List<Quote> getQuotes() {
        return quoteProvider.generateQuotes();
    }

    public List<Quote> findByAuthor(final String author) {
        return getQuotes().stream().filter(q -> q.getAuthor().equals(author)).collect(Collectors.toList());
    }

    private boolean publishQuotes(List<Quote> quotes) {
        try {
            messageServer.connect();
            return messageServer.publish(quotes);
        } finally {
            messageServer.disconnect();
        }
    }

    public boolean publishQuotesByAuthor(String name) {
        return publishQuotes(findByAuthor(name));
    }

}
