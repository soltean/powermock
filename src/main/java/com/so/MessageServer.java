package com.so;

import java.util.List;

public enum MessageServer {
    INSTANCE;

    public void connect() {
        System.out.println("Connected to server");
    }

    public void disconnect() {
        System.out.println("Disconnected from server");
    }

    public void publish(List<Quote> quotes) {
        System.out.println("Publishing quotes " + quotes);
    }

}
