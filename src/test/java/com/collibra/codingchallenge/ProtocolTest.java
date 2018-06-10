package com.collibra.codingchallenge;

import org.junit.Test;

import java.util.Optional;

import static com.collibra.codingchallenge.parsing.Messages.nodeName;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ProtocolTest {

    @Test
    public void client_hi() {
        // when
        final Optional<String> name = nodeName("HI, I'M Marek");
        // then
        assertThat(name, is(Optional.of("Marek")));
    }

    @Test
    public void client_hi_with_dash() {
        // when
        final Optional<String> name = nodeName("HI, I'M double-barrelled");
        // then
        assertThat(name, is(Optional.of("double-barrelled")));
    }
}
