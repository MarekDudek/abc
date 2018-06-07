package com.collibra.codingchallenge;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;

import static com.collibra.codingchallenge.TcpCommon.CLIENT_TIMEOUT;
import static com.collibra.codingchallenge.TcpCommon.COLLIBRA_PORT;

public final class GraphServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServer.class);

    public static void main(final String[] ignored) {
        LOGGER.info("Starting Collibra Graph Server");
        try {
            new GraphServer().start(COLLIBRA_PORT, CLIENT_TIMEOUT);
        } catch (final IOException e) {
            LOGGER.error("I/O error: " + e.getMessage());
            System.exit(1);
        }
        LOGGER.info("Collibra Graph Server finished");
    }

    private void start(final int port, final int timeout) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        while (true) {
            LOGGER.info("Waiting for clients ...");
            final Socket client = server.accept();
            final long started = System.currentTimeMillis();
            client.setSoTimeout(timeout);
            final ClientHandler handler = new ClientHandler(client, started);
            new Thread(handler).start();
        }
    }

    @RequiredArgsConstructor
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final long started;
        private final UUID sessionID = UUID.randomUUID();

        @Override
        public void run() {
            LOGGER.info("Client {} on {} started", sessionID, socket);
            Protocol protocol = null;
            try {
                protocol = new Protocol(socket, sessionID, started).initialized();
                protocol.exchangeGreetings();
                for (final String request : protocol.requests()) {
                    final Optional<GraphCommand> command = GraphCommandParser.parse(request);
                    if (command.isPresent()) {

                    } else {
                        protocol.apologise();
                    }
                }
            } catch (final IOException e) {
                LOGGER.error("Client {} on {} failed with message '{}'", sessionID, socket, e.getMessage());
            } finally {
                if (protocol != null) {
                    protocol.sayGoodBye();
                    protocol.close();
                }
            }
            LOGGER.info("Client {} on {} finished", sessionID, socket);
        }
    }
}
