package com.collibra.codingchallenge;

import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import static com.collibra.codingchallenge.TcpCommon.CLIENT_TIMEOUT;
import static com.collibra.codingchallenge.TcpCommon.COLLIBRA_PORT;

public final class GraphServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServer.class);

    public static void main(final String[] ignored)
    {
        LOGGER.info("Starting Collibra Graph Server");

        try
        {
            new GraphServer().start(COLLIBRA_PORT, CLIENT_TIMEOUT);

        } catch (final IOException e)
        {
            LOGGER.error("I/O error: " + e.getMessage());
            System.exit(1);
        }

        LOGGER.info("Collibra Graph Server finished.");
    }

    private void start(final int port, final int timeout) throws IOException
    {
        final ServerSocket server = new ServerSocket(port);

        while (true)
        {
            LOGGER.info("Waiting for clients ...");

            final Socket client = server.accept();
            final long started = System.currentTimeMillis();

            final ClientHandler handler =
                    ClientHandler.builder().
                            socket(client).
                            started(started).
                            build();

            final Thread thread = new Thread(handler);
            thread.start();
        }
    }

    @Builder
    private static class ClientHandler implements Runnable
    {
        private final Socket socket;
        private final long started;
        private final UUID sessionID = UUID.randomUUID();

        @Override
        public void run()
        {
            LOGGER.info("Client {} running on port {} started", sessionID, socket.getPort());

            try
            {
                final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            } catch (final IOException e)
            {
                LOGGER.error("Client {} on port {} failed", sessionID, socket.getPort());
            }

            LOGGER.info("Client {} running on port {} finished", sessionID, socket.getPort());
        }
    }
}
