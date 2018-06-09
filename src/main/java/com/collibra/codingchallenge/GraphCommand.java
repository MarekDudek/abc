package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.*;

import java.util.function.Function;

import static com.collibra.codingchallenge.utils.Utils.error;
import static com.google.common.base.Preconditions.checkNotNull;

public interface GraphCommand {

    static <T> T match
            (
                    final GraphCommand command,
                    final Function<AddNode, T> addNode,
                    final Function<AddEdge, T> addEdge,
                    final Function<RemoveNode, T> removeNode,
                    final Function<RemoveEdge, T> removeEdge,
                    final Function<ShortestPath, T> shortestPath,
                    final Function<CloserThan, T> closerThan
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

