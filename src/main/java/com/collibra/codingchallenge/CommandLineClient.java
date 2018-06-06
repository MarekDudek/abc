package com.collibra.codingchallenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.err;

public final class CommandLineClient
{
    public static void main(String[] args)
    {
        new CommandLineClient().run();
    }

    private void run()
    {
        err.println("connecting to server...");
        try (
                final Socket client = new Socket(TcpCommon.SERVER_HOST, TcpCommon.COLLIBRA_PORT);
                final PrintWriter output = new PrintWriter(client.getOutputStream(), true);
                final BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        )
        {
            final BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;
            String clientMessage;

            while ((serverMessage = input.readLine()) != null)
            {
                System.out.println("server > " + serverMessage);
                System.out.print("client > ");
                clientMessage = commandLine.readLine();
                output.println(clientMessage);
            }

        } catch (final Exception e)
        {
            err.println(e.getMessage());
            System.exit(1);
        }
    }
}
