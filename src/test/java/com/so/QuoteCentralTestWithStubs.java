package com.so;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteCentralTestWithStubs {

    private final QuotesCentral quotesCentral = new QuotesCentral();

    private MessageServer messageServer = new MessageServer(){

        @Override
        public boolean publish(List<Quote> quotes) {
            System.out.println("stubbed publish");
            return true;
        }

        @Override
        public void connect() {
            System.out.println("stubbed connect");
        }

        @Override
        public void disconnect() {
            System.out.println("stubbed disconnect");
        }
    };

    private QuoteProvider quoteProvider = new QuoteProvider(){
        @Override
        public List<Quote> generateQuotes() {
            return QuoteGenerator.QUOTES;
        }
    };

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
        boolean result = quotesCentral.publishQuotesByAuthor(author);
        assertThat(result).as("Should be published").isTrue();
    }
}
