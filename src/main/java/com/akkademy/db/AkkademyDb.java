package com.akkademy.db;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import message.GetRequest;
import message.KeyNotFoundException;
import message.SetRequest;

import java.util.HashMap;
import java.util.Map;

public class AkkademyDb extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    protected final Map<String, Object> map = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SetRequest.class, message -> {
                    log.info("Received set request – key: {} value:{}", message.getKey(), message.getValue());
                    map.put(message.getKey(), message.getValue());
                    getSender().tell(message, getSelf());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received get request – key: {} ", message.key);
                    Object value = map.get(message.key);
                    Object response = (value != null) ? value
                            : new Status.Failure(new KeyNotFoundException(message.key));
                    getSender().tell(response, self());
                })
                .matchAny(o -> sender().tell(new Status.Failure(new ClassNotFoundException()), self()))
                .build();
    }
}