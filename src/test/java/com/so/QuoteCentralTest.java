package com.so;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class QuoteCentralTest {

    private final QuotesCentral quotesCentral = new QuotesCentral();

    @Mock
    private MessageServer messageServer;
    @Mock
    private QuoteProvider quoteProvider;

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Before
    public void setup() {
        quotesCentral.setMessageServer(messageServer);
        quotesCentral.setQuoteProvider(quoteProvider);
        //stub method calls
        Mockito.when(quoteProvider.generateQuotes()).thenReturn(QuoteGenerator.QUOTES);
    }

    @Test
    public void testFindByAuthor() {
        String author = "Mark Twain";
        List<Quote> result = quotesCentral.findByAuthor(author);
        assertThat(result).as("There should be 2 QUOTES").hasSize(2);
        assertThat(result).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
        //verify interactions
        Mockito.verify(quoteProvider).generateQuotes();
    }

    @Test
    public void testPublishQuotesByAuthor() {
        ArgumentCaptor<List<Quote>> quotesCaptor = ArgumentCaptor.forClass(List.class);
        String author = "Mark Twain";

        //stub method calls
        Mockito.doNothing().when(messageServer).connect();
        Mockito.doNothing().when(messageServer).disconnect();
        Mockito.when(messageServer.publish(any(List.class))).thenReturn(true);

        quotesCentral.publishQuotesByAuthor(author);

        //verify interactions
        Mockito.verify(messageServer).connect();
        Mockito.verify(messageServer).publish(quotesCaptor.capture());
        assertThat(quotesCaptor.getValue()).as("There should be 2 QUOTES").hasSize(2);
        assertThat(quotesCaptor.getValue()).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
        Mockito.verify(messageServer).disconnect();
    }
}
