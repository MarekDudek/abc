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

    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);

    private static final Object SEPARATOR = System.getProperty("line.separator");

    private final Socket socket;
    private final UUID sessionID;

    private BufferedReader client;
    private BufferedWriter server;

    private long started;

    private String name;

    void initialize() throws IOException {
        client = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        server = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(socket.getOutputStream())));
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
        sendToClient(firstMessage);

        final String clientResponse = client.readLine();
        LOGGER.info("Client response was '{}'", clientResponse);

        name = Messages.nodeName(clientResponse).orElseGet(
                () -> {
                    LOGGER.warn("Unable to parse client's name");
                    return "anonymous";
                });

        final String serverResponse = format(SERVER_RESPONSE, name);
        LOGGER.info("Server response is '{}'", serverResponse);
        sendToClient(serverResponse);
    }

    void seeOff() throws IOException {

        final long finished = System.currentTimeMillis();
        final long duration = finished - started;

        final String message = format(SERVER_FAREWELL, name, duration);
        LOGGER.info("Server farewell is '{}'", message);
        sendToClient(message);
    }

    void notSupportedCommand() throws IOException {
        final String message = SERVER_NOT_SUPPORTED_COMMAND;
        LOGGER.info("Server 'not supported command' message is '{}'", message);
        sendToClient(message);
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

    void respond(final String response) throws IOException {
        LOGGER.debug("Server response is '{}'", response);
        sendToClient(response);
    }


    private void sendToClient(final String message) throws IOException {
        server.write(message + SEPARATOR);
        server.flush();
    }
}
