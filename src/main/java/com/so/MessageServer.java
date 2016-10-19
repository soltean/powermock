package com.so;

import java.util.List;
import java.util.Optional;

public class MessageServer {

    public void connect() {
        System.out.println("Connected to server");
    }

    public void disconnect() {
        System.out.println("Disconnected from server");
    }

    public boolean publish(List<Quote> quotes) {
        System.out.println("Publishing quotes " + quotes);
        return Optional.ofNullable(quotes).isPresent();
    }

}
