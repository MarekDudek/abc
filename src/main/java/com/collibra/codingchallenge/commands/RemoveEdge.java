package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RemoveEdge implements GraphCommand {
    private final String fromNode;
    private final String toNode;
}
