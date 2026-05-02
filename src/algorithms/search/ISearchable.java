package algorithms.search;

import java.util.ArrayList;

/**
 * Represents a problem that can be searched by a generic search algorithm.
 * The algorithm does not need to know the concrete problem type, such as a maze.
 */
public interface ISearchable {
    /**
     * @return the state where the search should begin.
     */
    AState getStartState();

    /**
     * @return the target state that the search algorithm should reach.
     */
    AState getGoalState();

    /**
     * Finds every valid state that can be reached directly from the given state.
     *
     * @param state the current state being expanded by the search algorithm.
     * @return a list of neighboring states that can be explored next.
     */
    ArrayList<AState> getAllPossibleStates(AState state);
}
