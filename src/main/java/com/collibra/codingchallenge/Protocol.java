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

import static com.collibra.codingchallenge.Messages.*;
import static java.lang.String.format;

@RequiredArgsConstructor
final class Protocol implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);

    private final Socket socket;
    private final UUID sessionID;

    private BufferedReader client;
    private PrintWriter server;
    private long started;

    private String name;

    void initialize() throws IOException {
        client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        server = new PrintWriter(socket.getOutputStream(), true);
        started = System.currentTimeMillis();
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(client);
        IOUtils.closeQuietly(server);
    }

    void exchangeFormalities() throws IOException {

        final String firstMessage = format(SERVER_FIRST_MESSAGE, sessionID);
        LOGGER.info("Server first message is '{}'", firstMessage);
        server.println(firstMessage);

        final String clientResponse = client.readLine();
        LOGGER.info("Client response was '{}'", clientResponse);

        name = Messages.nodeName(clientResponse).orElseGet(
                () -> {
                    LOGGER.warn("Unable to parse client's name");
                    return "anonymous";
                });

        final String serverResponse = format(SERVER_RESPONSE, name);
        LOGGER.info("Server response is '{}'", serverResponse);
        server.println(serverResponse);
    }

    void seeOff() {

        final long finished = System.currentTimeMillis();
        final long duration = finished - started;

        final String farewell = format(SERVER_FAREWELL, name, duration);
        LOGGER.info("Server farewell is '{}'", farewell);
        server.println(farewell);
    }

    void notSupported() {
        final String apology = SERVER_APOLOGY;
        LOGGER.info("Server apology is '{}'", apology);
        server.println(apology);
    }

    Iterable<String> requests() {

        return () -> new Iterator<String>() {

            private String request;

            @Override
            public boolean hasNext() {
                try {
                    request = client.readLine();
                    LOGGER.debug("Client request was '{}'", request);
                    return !(request == null || Messages.clientEndedSession(request));
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

    void respond(final String response) {
        LOGGER.debug("Server response is '{}'", response);
        server.println(response);
    }
}
