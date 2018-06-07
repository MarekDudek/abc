package com.collibra.codingchallenge;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.UUID;

import static com.collibra.codingchallenge.Messages.clientName;
import static java.lang.String.format;

@RequiredArgsConstructor
final class Protocol implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);

    private final Socket socket;
    private final UUID sessionID;
    private final long started;

    private BufferedReader client;
    private PrintWriter server;

    private String name;

    Protocol initialized() throws IOException {
        client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        server = new PrintWriter(socket.getOutputStream(), true);
        return this;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(client);
        IOUtils.closeQuietly(server);
    }

    void greetEachOther() throws IOException {

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

    void sayGoodBye() {

        final long finished = System.currentTimeMillis();
        final long duration = finished - started;

        final String goodBye = format("BYE %s, WE SPOKE FOR %d MS", name, duration);
        LOGGER.info("good bye - '{}'", goodBye);
        server.println(goodBye);
    }

    void apologise() {

        final String apology = "SORRY, I DIDN'T UNDERSTAND THAT";
        LOGGER.info("apology - '{}'", apology);
        server.println(apology);
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
