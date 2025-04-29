package com.arthead;

import com.arthead.controller.broker.MessageReceiver;
import jakarta.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        MessageReceiver receiver = new MessageReceiver(args[0]);
        receiver.start();
    }
}