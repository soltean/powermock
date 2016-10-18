package com.so;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class QuoteCentralTest {

    @Mock
    private MessageServer messageServer;
    @Mock
    private QuoteProvider quoteProvider;

    private final QuotesCentral quotesCentral = new QuotesCentral();
    private final List<Quote> quotes = Arrays.asList(new Quote("To eat or to sleep", "Mark Twain", 4));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        quotesCentral.setMessageServer(messageServer);
        quotesCentral.setQuoteProvider(quoteProvider);

        //stub method calls
        Mockito.doNothing().when(messageServer).connect();
        Mockito.doNothing().when(messageServer).disconnect();
        Mockito.doNothing().when(messageServer).publish(any(List.class));
        Mockito.when(quoteProvider.generateQuotes()).thenReturn(quotes);
    }

    @Test
    public void testFindByAuthor() {
        String author = "Mark Twain";
        List<Quote> result = quotesCentral.findByAuthor(author);
        assertThat(result).as("The author should be " + author).extracting("author").isEqualTo(author);
        //verify interactions
        Mockito.verify(quoteProvider).generateQuotes();
    }

    @Test
    public void testPublishQuotesByAuthor() {
        ArgumentCaptor<List<Quote>> quotesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

        String author = "Mark Twain";
        quotesCentral.publishQuotesByAuthor(author);
        //verify interactions
        Mockito.verify(messageServer).connect();
        Mockito.verify(quotesCentral).findByAuthor(nameCaptor.capture());
        assertThat(nameCaptor.getValue()).as("The author should be " + author).isEqualTo(author);
        Mockito.verify(messageServer).publish(quotesCaptor.capture());
        assertThat(quotesCaptor.getValue()).as("The author should be " + author).extracting("author").isEqualTo(author);
        Mockito.verify(messageServer).disconnect();
    }
}
