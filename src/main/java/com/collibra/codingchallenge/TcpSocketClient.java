package com.collibra.codingchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static com.collibra.codingchallenge.CollibraConstants.COLLIBRA_PORT;
import static com.collibra.codingchallenge.CollibraConstants.SERVER_HOST;

public final class TcpSocketClient {

    public static void main(String[] args) throws IOException {
        LOG.info("Creating socket ...");
        final Socket socket = new Socket(SERVER_HOST, COLLIBRA_PORT);
        LOG.info("... created, address: {}, port: {}, local address: {}, local port: {}, socket local address: {}, socket remote address: {}",
                socket.getInetAddress(),
                socket.getPort(),
                socket.getLocalAddress(),
                socket.getLocalPort(),
                socket.getLocalSocketAddress(),
                socket.getRemoteSocketAddress()
        );

        final InputStream inputStream = socket.getInputStream();
        final InputStreamReader reader = new InputStreamReader(inputStream);
        final BufferedReader buffered = new BufferedReader(reader);

        final OutputStream outputStream = socket.getOutputStream();
        final DataOutputStream dataOutput = new DataOutputStream(outputStream);

        LOG.info("Writing line ...");
        final String line = "some line\n";
        dataOutput.writeBytes(line);
        LOG.info("... written.");

        LOG.info("Reading from input stream ...");
        final String response = buffered.readLine();
        LOG.info("... read '{}'", response);

        LOG.info("Closing socket ...");
        socket.close();
        LOG.info("... closed.");
    }

    private static final Logger LOG = LoggerFactory.getLogger(TcpSocketClient.class);
}
