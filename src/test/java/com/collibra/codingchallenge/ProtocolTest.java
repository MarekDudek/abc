package com.collibra.codingchallenge;

import org.junit.Test;

import static com.collibra.codingchallenge.Protocol.clientName;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ProtocolTest {

    @Test
    public void client_hi() {
        // when
        final String name = clientName("HI, I'M Marek");
        // then
        assertThat(name, is("Marek"));
    }

    @Test
    public void client_hi_with_dash() {
        // when
        final String name = clientName("HI, I'M double-barrelled");
        // then
        assertThat(name, is("double-barrelled"));
    }
}