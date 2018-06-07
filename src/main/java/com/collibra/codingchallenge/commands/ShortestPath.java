package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.Builder;

@Builder
class ShortestPath implements GraphCommand {
    private final String fromNode;
    private final String toNode;
}
