package com.akkademy.db;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleValueDBTest {
    static ActorSystem system = ActorSystem.create("SingleValue", ConfigFactory.load(("singlevalue")));

    TestActorRef<SingleValueDB> actorRef = TestActorRef.
            create(system, Props.create(SingleValueDB.class));

    @Test
    public void itShouldStoreFirstStringReceived() {
        actorRef.tell("First Value", ActorRef.
                noSender());

        SingleValueDB singleValueDb = actorRef.underlyingActor();
        assertEquals(singleValueDb.value, "First Value");
    }

    @Test
    public void itShouldStoreSecondStringReceived() {
        actorRef.tell("First Value", ActorRef.
                noSender());

        actorRef.tell("Second Value", ActorRef.
                noSender());

        SingleValueDB singleValueDb = actorRef.underlyingActor();
        assertEquals(singleValueDb.value, "Second Value");
    }
}
