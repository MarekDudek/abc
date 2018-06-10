package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.GraphCommand;
import com.collibra.codingchallenge.parsing.GraphCommandParser;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.collibra.codingchallenge.CollibraConstants.CLIENT_TIMEOUT;
import static com.collibra.codingchallenge.CollibraConstants.COLLIBRA_PORT;

public final class GraphServer {

    private final GraphManager graphManager = new GraphManager();

    public static void main(final String[] ignored) {
        LOGGER.info("Starting Collibra Graph Server");
        new GraphServer().start(COLLIBRA_PORT, CLIENT_TIMEOUT);
        LOGGER.info("Collibra Graph Server finished"); // FIXME: handle graceful closing
    }

    private void start(final int port, final int timeout) {
        final ExecutorService pool = Executors.newFixedThreadPool(50);
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(pool);
        try (final ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try {
                    LOGGER.info("Waiting for clients ...");
                    final Socket client = server.accept();
                    client.setSoTimeout(timeout);
                    final Callable<Boolean> task = new ClientHandler(client);
                    final ListenableFuture<Boolean> result = service.submit(task);
                    result.addListener(new ResultReporter(result), service);
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
    private class ClientHandler implements Callable<Boolean> {

        private final Socket socket;

        @Override
        public Boolean call() {
            try (final Protocol protocol = new Protocol(socket)) {
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
                LOGGER.error("Error while executing protocol - {}", e.getMessage());
                return false;
            }
            return true;
        }
    }

    @RequiredArgsConstructor
    private class ResultReporter implements Runnable {

        final ListenableFuture<Boolean> result;

        @Override
        public void run() {
            try {
                final boolean succeeded = result.get();
                LOGGER.info("Handling the client succeeded? {}", succeeded);
            } catch (final Exception e) {
                LOGGER.error("Error while reporting result - {}", e.getMessage());
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServer.class);
}
