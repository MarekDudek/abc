package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.GraphCommand;
import com.collibra.codingchallenge.parsing.GraphCommandParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;

import static com.collibra.codingchallenge.CollibraConstants.CLIENT_TIMEOUT;
import static com.collibra.codingchallenge.CollibraConstants.COLLIBRA_PORT;

public final class GraphServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServer.class);

    private final GraphManager graphManager = new GraphManager();

    public static void main(final String[] ignored) {
        LOGGER.info("Starting Collibra Graph Server");
        try {
            new GraphServer().start(COLLIBRA_PORT, CLIENT_TIMEOUT);
        } catch (final IOException e) {
            LOGGER.error("I/O error: " + e.getMessage());
            System.exit(1);
        }
        LOGGER.info("Collibra Graph Server finished"); // FIXME: handle graceful closing
    }

    private void start(final int port, final int timeout) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        while (true) { // FIXME: handle closing of server
            LOGGER.info("Waiting for clients ...");
            final Socket client = server.accept();
            client.setSoTimeout(timeout);
            final ClientHandler handler = new ClientHandler(client);
            new Thread(handler).start();
        }
    }

    @RequiredArgsConstructor
    private class ClientHandler implements Runnable {

        private final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

        private final Socket socket;
        private final UUID sessionID = UUID.randomUUID(); // FIXME: move to protocol

        @Override
        public void run() {
            LOGGER.info("Client {} on {} started", sessionID, socket);
            Protocol protocol = null; // FIXME: use try-with-resources to simplify
            try {
                protocol = new Protocol(socket, sessionID);
                protocol.initialize();
                protocol.exchangeFormalities();
                for (final String request : protocol.requests()) {
                    final Optional<GraphCommand> command = GraphCommandParser.parse(request);
                    if (command.isPresent()) {
                        final String response = graphManager.handle(command.get());
                        protocol.respond(response);
                    } else {
                        protocol.notSupportedCommand();
                    }
                }
            } catch (final IOException e) {
                LOGGER.error("Client {} on {} failed with message '{}'", sessionID, socket, e.getMessage());
            } finally {
                if (protocol != null) {
                    try {
                        protocol.seeOff();
                    } catch (final IOException e) {
                        LOGGER.error("Error while closing protocol: {}", e.getMessage());
                    }
                    protocol.close();
                }
            }
            LOGGER.info("Client {} on {} finished", sessionID, socket);
        }
    }
}
