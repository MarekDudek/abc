package com.collibra.codingchallenge;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MessagesTest
{
    @Test
    public void server_hi()
    {
        // given
        final UUID uuid = UUID.fromString("7d128be4-00b2-454e-8249-1a64c9979a17");
        // when
        final String message = Messages.serverSessionHi(uuid);
        // then
        assertThat(message, is("HI, I'M 7d128be4-00b2-454e-8249-1a64c9979a17"));
    }

    @Test
    public void client_hi()
    {
        // when
        final String name = Messages.clientName("HI, I'M Marek");
        // then
        assertThat(name, is("Marek"));
    }

    @Test
    public void client_hi_with_dash()
    {
        // when
        final String name = Messages.clientName("HI, I'M double-barrelled");
        // then
        assertThat(name, is("double-barrelled"));
    }
}
