package com.akkademy.pong;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import scala.concurrent.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class JavaPongActorTest {

    static ActorSystem actorSystem = ActorSystem.create("PongActor", ConfigFactory.load(("pongactor")));
    TestActorRef actorRef = TestActorRef.create(actorSystem, Props.create(JavaPongActor.class));

    public CompletableFuture<String> askPong(String message) {
        Future sFuture = ask(actorRef, message, 1000);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) toJava(sFuture);
        return jFuture;
    }

    @Test
    public void itShouldReplyPongToPing() throws Exception {
        assert(askPong("Ping").get(1000, TimeUnit.MILLISECONDS).equals("Pong"));
    }

    @Test (expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        askPong("Unknown").get(1000, TimeUnit.MILLISECONDS);
    }
}