package com.akkademy.db;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import message.*;

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
                .match(SetIfNotExists.class, message -> {
                    log.info("Received set if not exists request – key: {} value:{}", message.getKey(), message.getValue());
                    Object value = map.get(message.getKey());
                    if(value == null) {
                        map.put(message.getKey(), message.getValue());
                    } else {
                        getSender().tell(new Status.Failure(new KeyExistsException(message.getKey())), self());
                    }
                    getSender().tell(message, getSelf());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received get request – key: {} ", message.key);
                    Object value = map.get(message.key);
                    Object response = (value != null) ? value
                            : new Status.Failure(new KeyNotFoundException(message.key));
                    getSender().tell(response, self());
                })
                .match(Delete.class, message -> {
                    log.info("Received delete request – key: {} ", message.key);
                    Object value = map.get(message.key);
                    if(value != null) {
                        map.remove(message.key);
                        getSender().tell(message, getSelf());
                    } else {
                        getSender().tell(new Status.Failure(new KeyNotFoundException(message.key)), self());
                    }

                })
                .matchAny(o -> sender().tell(new Status.Failure(new ClassNotFoundException()), self()))
                .build();
    }
}