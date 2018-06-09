package com.collibra.codingchallenge.commands;

import com.collibra.codingchallenge.GraphCommand;

import java.util.function.Function;

import static com.collibra.codingchallenge.Utils.error;
import static com.google.common.base.Preconditions.checkNotNull;

public final class GraphCommands {

    public static <RESULT> RESULT match
            (
                    final GraphCommand command,
                    final Function<AddNode, RESULT> addNode,
                    final Function<AddEdge, RESULT> addEdge,
                    final Function<RemoveNode, RESULT> removeNode,
                    final Function<RemoveEdge, RESULT> removeEdge,
                    final Function<ShortestPath, RESULT> shortestPath,
                    final Function<CloserThan, RESULT> closerThan
            ) {

        checkNotNull(command);

        if (command instanceof AddNode)
            return addNode.apply((AddNode) command);
        if (command instanceof AddEdge)
            return addEdge.apply((AddEdge) command);
        if (command instanceof RemoveNode)
            return removeNode.apply((RemoveNode) command);
        if (command instanceof RemoveEdge)
            return removeEdge.apply((RemoveEdge) command);
        if (command instanceof ShortestPath)
            return shortestPath.apply((ShortestPath) command);
        if (command instanceof CloserThan)
            return closerThan.apply((CloserThan) command);

        return error("this cannot happen");
    }
}
