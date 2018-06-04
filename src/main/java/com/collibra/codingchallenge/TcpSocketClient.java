package com.collibra.codingchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public final class TcpSocketClient
{
    public static void main(String[] args) throws IOException
    {
        LOG.info("Creating socket ...");
        final Socket socket = new Socket("localhost", 6789);
        LOG.info("... created.");

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
