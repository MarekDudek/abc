package com.collibra.codingchallenge.graphs;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class Edge {
    public final String fromName;
    public final String toName;
    public final int weight;
}
