package com.akkademy.db;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SingleValueDB extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    protected String value = "";

    private void setValue(String newValue) {
        this.value = newValue;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, message -> {
            log.info("Received new value:{}", message);
            setValue(message);
        }).matchAny(o -> {
            log.info("Invalid message received");
        }).build();
    }

}
