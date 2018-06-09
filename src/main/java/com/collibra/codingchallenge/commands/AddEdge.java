package com.collibra.codingchallenge.commands;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class AddEdge implements GraphCommand {
    public final String from;
    public final String to;
    public final int weight;
}
