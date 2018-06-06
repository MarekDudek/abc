package com.collibra.codingchallenge;

import lombok.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.UUID;

import static com.collibra.codingchallenge.Messages.clientName;
import static java.lang.String.format;

@Builder
final class BasicProtocol
{
    private final BufferedReader client;
    private final PrintWriter server;

    private final UUID sessionID;
    private final long started;

    private String name;

    void exchangeGreetings() throws IOException
    {
        server.println(format("HI, I'M %s", sessionID));
        final String introduction = client.readLine();
        name = clientName(introduction);
        server.println(format("HI %s", name));
    }

    void apologise()
    {
        server.println("SORRY, I DIDN'T UNDERSTAND THAT");
    }

    void sayGoodBye()
    {
        final long finished = System.currentTimeMillis();
        final long duration = finished - started;
        server.println(format("BYE %s, WE SPOKE FOR %d MS", name, duration));
    }

    Iterable<String> requests()
    {
        return () -> new Iterator<String>()
        {
            private String request;

            @Override
            public boolean hasNext()
            {
                try
                {
                    request = client.readLine();
                    return !(request == null || request.equals("BYE MATE!"));
                }
                catch (final IOException ignored)
                {
                    return false;
                }
            }

            @Override
            public String next()
            {
                return request;
            }
        };
    }
}
