package com.collibra.codingchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public final class TcpSocketServer {

    public static void main(String[] args) throws IOException {
        LOG.info("Creating server socket ...");
        final ServerSocket serverSocket = new ServerSocket(CollibraConstants.COLLIBRA_PORT);
        LOG.info("... created");

        while (true) {
            LOG.info("Accepting client connection..");
            final Socket socket = serverSocket.accept();
            LOG.info(
                    "... accepted, address: {}, port: {}, local address: {}, local port: {}",
                    socket.getInetAddress(),
                    socket.getPort(),
                    socket.getLocalAddress(),
                    socket.getLocalPort()
            );


            final InputStream inputStream = socket.getInputStream();
            final InputStreamReader reader = new InputStreamReader(inputStream);
            final BufferedReader buffered = new BufferedReader(reader);

            final OutputStream outputStream = socket.getOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            LOG.info("Reading from socket input stream ...");
            final String line = buffered.readLine();
            LOG.info("... read '{}'", line);
            final String capitalized = line.toUpperCase();

            LOG.info("Writing '{}' to output stream ...", capitalized);
            dataOutputStream.writeBytes(capitalized + "\n");
            LOG.info("... written.");
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(TcpSocketServer.class);
}
