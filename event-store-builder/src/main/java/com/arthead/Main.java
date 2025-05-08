package com.arthead;

import com.arthead.application.usecases.listenandsave.MessageReceiver;
import jakarta.jms.JMSException;

import java.util.List;


public class Main {
    public static void main(String[] args) throws JMSException {
        List<String> topics = List.of("github.Information", "github.Repositories", "crypto.Coins", "crypto.Quotes");
        MessageReceiver receiver = new MessageReceiver(topics,args[0]);
        receiver.start();
    }
}