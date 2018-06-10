package com.collibra.codingchallenge;

import com.collibra.codingchallenge.parsing.Messages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.UUID;

import static com.collibra.codingchallenge.parsing.Messages.*;
import static java.lang.String.format;

@RequiredArgsConstructor
final class Protocol implements AutoCloseable {

    private final UUID sessionID = UUID.randomUUID();
    private final Socket socket;

    private BufferedReader client;
    private BufferedWriter server;

    private long started;
    private String name;

    void initialize() throws IOException {
        LOGGER.info("Client {} on {} started", sessionID, socket);
        client = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        server = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(socket.getOutputStream())));
        started = System.currentTimeMillis();
    }

    void exchangeFormalities() throws IOException {

        final String firstMessage = format(SERVER_FIRST_MESSAGE, sessionID);
        LOGGER.info("Server first message is '{}'", firstMessage);
        sendServerMessage(firstMessage);

        final String clientResponse = client.readLine();
        LOGGER.info("Client response was '{}'", clientResponse);

        name = Messages.nodeName(clientResponse).orElseGet(
                () -> {
                    LOGGER.error("Unable to parse client's name in session {}", sessionID);
                    return "anonymous";
                });

        final String serverResponse = format(SERVER_RESPONSE, name);
        LOGGER.info("Server response is '{}'", serverResponse);
        sendServerMessage(serverResponse);
    }

    void notSupportedCommand() throws IOException {
        final String message = SERVER_NOT_SUPPORTED_COMMAND;
        LOGGER.info("Server 'not supported command' message is '{}'", message);
        sendServerMessage(message);
    }

    Iterable<String> requests() {
        return () -> new Iterator<String>() {

            private String request;

            @Override
            public boolean hasNext() {
                try {
                    request = client.readLine();
                    LOGGER.debug("Client request was '{}'", request);
                    return request != null && !Messages.clientEndedSession(request);
                } catch (final IOException e) {
                    LOGGER.error("Error while reading from client in session {} - {}", sessionID, e.getMessage());
                    return false;
                }
            }

            @Override
            public String next() {
                return request;
            }
        };
    }

    void respond(final String response) throws IOException {
        LOGGER.debug("Server response is '{}'", response);
        sendServerMessage(response);
    }

    private void seeOff() throws IOException {

        final long finished = System.currentTimeMillis();
        final long duration = finished - started;

        final String farewell = format(SERVER_FAREWELL, name, duration);
        LOGGER.info("Server farewell is '{}'", farewell);
        sendServerMessage(farewell);
    }

    @Override
    public void close() {
        try {
            seeOff();
        } catch (final IOException e) {
            LOGGER.error("Error while seeing off the client in session {} - {}", sessionID, e.getMessage());
        }
        IOUtils.closeQuietly(client);
        IOUtils.closeQuietly(server);
        LOGGER.info("Client {} on {} finished", sessionID, socket);
    }

    private static final Object SEPARATOR = System.getProperty("line.separator");

    private void sendServerMessage(final String message) throws IOException {
        server.write(message + SEPARATOR);
        server.flush();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);
}
