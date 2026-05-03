package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Breadth First Search implementation.
 * Explores states level by level using a queue.
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {
    /**
     * Solves a searchable problem using BFS.
     *
     * @param searchable the problem to solve.
     * @return a solution from the start state to the goal state, or an empty solution if no path exists.
     */
    @Override
    public Solution solve(ISearchable searchable) {
        resetNumberOfNodesEvaluated();

        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        // Queue controls BFS order: first discovered states are evaluated first.
        Queue<AState> queue = new LinkedList<>();

        // Keeps track of states already discovered so they are not processed twice.
        HashSet<AState> visitedStates = new HashSet<>();

        queue.add(startState);
        visitedStates.add(startState);

        while (!queue.isEmpty()) {
            AState currentState = queue.poll();
            numberOfNodesEvaluated++;

            // Once the goal is reached, rebuild the path using cameFrom pointers.
            if (currentState.equals(goalState)) {
                return backTraceSolution(currentState);
            }

            ArrayList<AState> possibleStates = searchable.getAllPossibleStates(currentState);
            for (AState possibleState : possibleStates) {
                if (!visitedStates.contains(possibleState)) {
                    // Save the parent state so the final solution path can be reconstructed.
                    possibleState.setCameFrom(currentState);
                    visitedStates.add(possibleState);
                    queue.add(possibleState);
                }
            }
        }

        // No path was found.
        return new Solution(new ArrayList<>());
    }

    /**
     * @return the algorithm name used by the test runner.
     */
    @Override
    public String getName() {
        return "Breadth First Search";
    }
}
