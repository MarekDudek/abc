package com.collibra.codingchallenge.commands;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ShortestPath implements GraphCommand {
    private final String fromNode;
    private final String toNode;
}
