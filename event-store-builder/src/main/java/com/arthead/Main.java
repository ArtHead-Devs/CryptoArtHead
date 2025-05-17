package com.arthead;

import com.arthead.eventstorebuilder.application.usecases.eventstorermanager.MessageReceiver;
import jakarta.jms.JMSException;

import java.util.List;


public class Main {
    public static void main(String[] args) throws JMSException {
        List<String> topics = List.of(args).subList(1, args.length);
        MessageReceiver receiver = new MessageReceiver(topics,args[0]);
        receiver.start();
    }
}