package com.collibra.codingchallenge;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MainTest
{
    @Test
    public void test()
    {
        assertThat(2 + 2, is(4));
    }
}
