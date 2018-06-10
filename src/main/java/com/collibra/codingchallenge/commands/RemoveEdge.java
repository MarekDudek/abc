package com.collibra.codingchallenge.commands;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RemoveEdge implements GraphCommand {
    public final String start;
    public final String end;
}
