package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.Builder;

@Builder
class CloserThan implements GraphCommand {
    private final int weight;
    private final String node;
}
