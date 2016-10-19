package com.so;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteCentralTestWithSpies {

    private final QuotesCentral quotesCentral = new QuotesCentral();

    private class SpyMessageServer extends MessageServer {
        int methodCalls = 0;

        public int getNumberOfCalls() {
            return methodCalls;
        }

        @Override
        public boolean publish(List<Quote> quotes) {
            System.out.println("stubbed publish");
            methodCalls++;
            return true;
        }

        @Override
        public void connect() {
            System.out.println("stubbed connect");
            methodCalls++;
        }

        @Override
        public void disconnect() {
            System.out.println("stubbed disconnect");
            methodCalls++;
        }
    }

    private class SpyQuoteProvider extends QuoteProvider {

        @Override
        public List<Quote> generateQuotes() {
            return QuoteGenerator.QUOTES;
        }
    }

    private SpyMessageServer messageServer = new SpyMessageServer();
    private SpyQuoteProvider quoteProvider = new SpyQuoteProvider();

    @Before
    public void setup() {
        quotesCentral.setMessageServer(messageServer);
        quotesCentral.setQuoteProvider(quoteProvider);
    }

    @Test
    public void testFindByAuthor() {
        String author = "Mark Twain";
        List<Quote> result = quotesCentral.findByAuthor(author);
        assertThat(result).as("There should be 2 QUOTES").hasSize(2);
        assertThat(result).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
    }

    @Test
    public void testPublishQuotesByAuthor() {
        String author = "Mark Twain";
        quotesCentral.publishQuotesByAuthor(author);
        assertThat(messageServer.getNumberOfCalls()).as("Should be 3 calls").isEqualTo(3);
    }
}
