package com.collibra.codingchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.collibra.codingchallenge.TcpCommon.COLLIBRA_PORT;

public final class PhaseOneServer
{
    public static void main(String[] args) throws IOException
    {
        LOG.info("start");
        new PhaseOneServer().run();
        LOG.info("end");
    }

    private void run() throws IOException
    {
        final ServerSocket server = new ServerSocket(COLLIBRA_PORT);

        LOG.info("waiting for client");
        final Socket client = server.accept();
        final long start = System.currentTimeMillis();
        LOG.info("accepted");

        final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        final PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        final PhaseOneProtocol protocol = new PhaseOneProtocol();

        out.println(protocol.initMessage());

        String request;
        while ((request = in.readLine()) != null)
        {
            LOG.info("client > " + request);
            final String response = protocol.respond(request);
            out.println(response);
            LOG.info("server > " + response);
            if (protocol.closeMessage(request))
            {
                break;
            }
        }

        client.close();
    }

    private static final Logger LOG = LoggerFactory.getLogger(PhaseOneServer.class);
}
