package com.collibra.codingchallenge;

import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.UUID;

import static com.collibra.codingchallenge.Messages.clientName;
import static java.lang.String.format;

@Builder
final class Protocol {
    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);

    private final BufferedReader client;
    private final PrintWriter server;

    private final UUID sessionID;
    private final long started;

    private String name;

    void exchangeGreetings() throws IOException {
        final String serverIntro = format("HI, I'M %s", sessionID);
        LOGGER.info("server introduction - '{}'", serverIntro);
        server.println(serverIntro);

        final String clientIntro = client.readLine();
        LOGGER.info("client introduction - '{}'", clientIntro);

        name = clientName(clientIntro);

        final String greeting = format("HI %s", name);
        LOGGER.info("server greeting - '{}'", greeting);
        server.println(greeting);
    }

    void apologise() {
        final String apology = "SORRY, I DIDN'T UNDERSTAND THAT";
        LOGGER.info("apology - '{}'", apology);
        server.println(apology);
    }

    void sayGoodBye() {
        final long finished = System.currentTimeMillis();
        final long duration = finished - started;

        final String goodBye = format("BYE %s, WE SPOKE FOR %d MS", name, duration);
        LOGGER.info("good bye - '{}'", goodBye);
        server.println(goodBye);
    }

    Iterable<String> requests() {
        return () -> new Iterator<String>() {
            private String request;

            @Override
            public boolean hasNext() {
                try {
                    request = client.readLine();
                    LOGGER.info("request - '{}'", request);
                    return !(request == null || request.equals("BYE MATE!"));
                } catch (final IOException ignored) {
                    return false;
                }
            }

            @Override
            public String next() {
                return request;
            }
        };
    }
}
