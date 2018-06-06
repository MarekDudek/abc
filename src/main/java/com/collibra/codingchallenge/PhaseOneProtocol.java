package com.collibra.codingchallenge;

import java.util.UUID;

import static com.collibra.codingchallenge.PhaseOneProtocol.State.WAITING_FOR_BYE;
import static com.collibra.codingchallenge.PhaseOneProtocol.State.WAITING_FOR_NAME;

final class PhaseOneProtocol
{
    enum State
    {
        WAITING_FOR_NAME,
        WAITING_FOR_BYE
    }

    private final long start = System.currentTimeMillis();
    private State state;
    private String name;

    String initMessage()
    {
        state = WAITING_FOR_NAME;
        final UUID uuid = UUID.randomUUID();
        return Messages.serverSessionHi(uuid);
    }

    String respond(final String request)
    {
        if (state == WAITING_FOR_NAME)
        {
            state = WAITING_FOR_BYE;
            name = Messages.parseClientHiMessage(request);
            if (name != null)
            {
                return Messages.serverNameHi(name);
            }
        }
        if (state == WAITING_FOR_BYE)
        {
            final boolean properBye = Messages.parseClientBye(request);
            if (properBye)
            {
                final long end = System.currentTimeMillis();
                return Messages.serverBye(name, end - start);
            }
        }
        return Messages.didNotUnderstand();
    }

    boolean closeMessage(final String request)
    {
        return Messages.parseClientBye(request);
    }
}
