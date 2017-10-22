package com.akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akkademy.db.AkkademyDb;
import com.typesafe.config.ConfigFactory;

public class Main {

    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("AkkademyDbSystem", ConfigFactory.load(("akkademydb")));
        ActorRef actor = system.actorOf(Props.create(AkkademyDb.class), "AkkademyDbActor");
        System.out.println("args = [" + actor.path() + "]");
    }
}