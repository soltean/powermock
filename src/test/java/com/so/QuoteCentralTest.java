package com.so;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@PrepareForTest({QuoteProvider.class, QuotesCentral.class})
public class QuoteCentralTest {

    private QuotesCentral quotesCentral;

    @Mock
    private MessageServer messageServer;
    @Mock
    private QuoteProvider quoteProvider;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setup() throws Exception {
        PowerMockito.mockStatic(QuoteProvider.class);
        Mockito.when(QuoteProvider.getInstance()).thenReturn(quoteProvider);
        Mockito.when(quoteProvider.generateQuotes()).thenReturn(QuoteGenerator.QUOTES);

        quotesCentral = new QuotesCentral();

        PowerMockito.verifyStatic();
        QuoteProvider.getInstance();
    }

    @Test
    public void testFindByAuthor() {
        String author = "Mark Twain";
        List<Quote> result = quotesCentral.findByAuthor(author);
        assertThat(result).as("There should be 2 quotes").hasSize(2);
        assertThat(result).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
        //verify interactions
        Mockito.verify(quoteProvider).generateQuotes();
    }

    @Test
    public void testPublishQuotesByAuthor() throws Exception {
        ArgumentCaptor<List> quotesCaptor = ArgumentCaptor.forClass(List.class);
        String author = "Mark Twain";

        //stub method calls
        PowerMockito.whenNew(MessageServer.class).withNoArguments().thenReturn(messageServer);
        Mockito.doNothing().when(messageServer).connect();
        Mockito.doNothing().when(messageServer).disconnect();
        Mockito.when(messageServer.publish(any(List.class))).thenReturn(true);

        quotesCentral.publishQuotesByAuthor(author);

        //verify interactions
        PowerMockito.verifyNew(MessageServer.class).withNoArguments();
        Mockito.verify(messageServer).connect();
        Mockito.verify(messageServer).publish(quotesCaptor.capture());
        assertThat(quotesCaptor.getValue()).as("There should be 2 quotes").hasSize(2);
        assertThat(quotesCaptor.getValue()).as("The author should be " + author).extracting("author").allMatch(s -> s.equals(author));
        Mockito.verify(messageServer).disconnect();
    }
}
