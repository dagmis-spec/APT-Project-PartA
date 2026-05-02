package algorithms.search;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Base class for search algorithms.
 * Holds shared behavior that BFS, DFS, and Best First Search can reuse.
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    /*
     * Counts how many states were removed from the algorithm's data structure
     * and evaluated during the latest search.
     */
    protected int numberOfNodesEvaluated;

    public ASearchingAlgorithm() {
        this.numberOfNodesEvaluated = 0;
    }

    /**
     * @return the number of states evaluated during the latest call to solve.
     */
    @Override
    public int getNumberOfNodesEvaluated() {
        return numberOfNodesEvaluated;
    }

    /**
     * Resets the counter before starting a new search.
     */
    protected void resetNumberOfNodesEvaluated() {
        numberOfNodesEvaluated = 0;
    }

    /**
     * Builds the final solution path by walking backward from the goal state
     * through each state's cameFrom pointer until reaching the start state.
     *
     * @param goalState the state where the search finished.
     * @return a Solution ordered from start state to goal state.
     */
    protected Solution backTraceSolution(AState goalState) {
        ArrayList<AState> solutionPath = new ArrayList<>();
        AState currentState = goalState;

        while (currentState != null) {
            solutionPath.add(currentState);
            currentState = currentState.getCameFrom();
        }

        Collections.reverse(solutionPath);
        return new Solution(solutionPath);
    }
}
