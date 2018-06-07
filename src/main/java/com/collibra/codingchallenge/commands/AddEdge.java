package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.Builder;

@Builder
class AddEdge implements GraphCommand {
    private final String fromNode;
    private final String toNode;
    private final int weight;
}
