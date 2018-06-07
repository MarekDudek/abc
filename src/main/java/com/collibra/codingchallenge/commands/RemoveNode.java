package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.Builder;

@Builder
class RemoveNode implements GraphCommand {
    private final String node;
}
