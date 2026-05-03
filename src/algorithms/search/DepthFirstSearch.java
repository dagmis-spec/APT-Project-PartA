package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Depth First Search implementation.
 * Explores one branch as deeply as possible before backtracking.
 */
public class DepthFirstSearch extends ASearchingAlgorithm {
    /**
     * Solves a searchable problem using DFS.
     *
     * @param searchable the problem to solve.
     * @return a solution from the start state to the goal state, or an empty solution if no path exists.
     */
    @Override
    public Solution solve(ISearchable searchable) {
        resetNumberOfNodesEvaluated();

        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        // Stack controls DFS order: last discovered states are evaluated first.
        Stack<AState> stack = new Stack<>();

        // Keeps track of states already discovered so they are not processed twice.
        HashSet<AState> visitedStates = new HashSet<>();

        stack.push(startState);
        visitedStates.add(startState);

        while (!stack.isEmpty()) {
            AState currentState = stack.pop();
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
                    stack.push(possibleState);
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
        return "Depth First Search";
    }
}
