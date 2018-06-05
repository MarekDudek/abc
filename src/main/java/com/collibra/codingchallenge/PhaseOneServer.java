package com.collibra.codingchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

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
        final UUID uuid = UUID.randomUUID();

        final BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        final PrintWriter output = new PrintWriter(client.getOutputStream(), true);

        output.println(Messages.serverSessionHi(uuid)); // 1

        final String clientHi = input.readLine(); // 2
        LOG.info("client > '{}'", clientHi);

        final String name = Messages.parseClientHiMessage(clientHi);
        output.println(Messages.serverNameHi(name)); // 3

        final String clientBye = input.readLine();
        LOG.info("client > '{}'", clientBye);

        final boolean bye = Messages.parseClientBy(clientBye);
        LOG.info("was it proper bye? {}", bye);

        final long end = System.currentTimeMillis();
        output.println(Messages.serverBye(name, end - start));
    }

    private static final Logger LOG = LoggerFactory.getLogger(PhaseOneServer.class);
}
