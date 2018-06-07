package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class AddNode implements GraphCommand {
    private final String node;
}
