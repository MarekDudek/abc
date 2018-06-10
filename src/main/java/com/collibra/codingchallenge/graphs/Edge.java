package com.collibra.codingchallenge.graphs;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Edge {
    public final int weight;
    public final String from;
    public final String to;
}
