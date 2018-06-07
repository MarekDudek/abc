package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CloserThan implements GraphCommand {
    private final int weight;
    private final String node;
}
