package com.akkademy.db;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.typesafe.config.ConfigFactory;
import message.SetRequest;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AkkademyDbTest {
    static ActorSystem system = ActorSystem.create("AkkademyDb", ConfigFactory.load(("akkademydb")));

    @Test
    public void itShouldPlaceKeyValueFromSetMessageIntoMap() {
        TestActorRef<AkkademyDb> actorRef = TestActorRef.
                create(system, Props.create(AkkademyDb.class));

        actorRef.tell(new SetRequest("key", "value"), ActorRef.
                noSender());

        AkkademyDb akkademyDb = actorRef.underlyingActor();
        assertEquals(akkademyDb.map.get("key"), "value");
    }
}