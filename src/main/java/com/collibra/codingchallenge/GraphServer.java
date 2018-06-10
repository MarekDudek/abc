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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.collibra.codingchallenge.CollibraConstants.CLIENT_TIMEOUT;
import static com.collibra.codingchallenge.CollibraConstants.COLLIBRA_PORT;

public final class GraphServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServer.class);

    private final GraphManager graphManager = new GraphManager();

    public static void main(final String[] ignored) {
        LOGGER.info("Starting Collibra Graph Server");
        new GraphServer().start(COLLIBRA_PORT, CLIENT_TIMEOUT);
        LOGGER.info("Collibra Graph Server finished"); // FIXME: handle graceful closing
    }

    private void start(final int port, final int timeout) {
        final ExecutorService service = Executors.newFixedThreadPool(50);
        try (final ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try {
                    LOGGER.info("Waiting for clients ...");
                    final Socket client = server.accept();
                    client.setSoTimeout(timeout);
                    final Callable<Void> task = new ClientHandler(client);
                    service.submit(task);
                } catch (final IOException e) {
                    LOGGER.error("Error while handling client - {}", e.getMessage());
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Could not start the server - {}", e.getMessage());
            System.exit(1);
        }
    }

    @RequiredArgsConstructor
    private class ClientHandler implements Callable<Void> {

        private final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

        private final Socket socket;
        private final UUID sessionID = UUID.randomUUID(); // FIXME: move to protocol

        @Override
        public Void call() {
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
            return null;
        }
    }
}
