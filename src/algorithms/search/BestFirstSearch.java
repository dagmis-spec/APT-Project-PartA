package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Best First Search implementation.
 * Uses a heap-based priority queue and prefers states with lower path cost.
 */
public class BestFirstSearch extends ASearchingAlgorithm {
    private static final int DIRECT_MOVE_COST = 10;
    private static final int DIAGONAL_MOVE_COST = 15;

    /**
     * Solves a searchable problem using a priority queue ordered by state cost.
     *
     * @param searchable the problem to solve.
     * @return a solution from the start state to the goal state, or an empty solution if no path exists.
     */
    @Override
    public Solution solve(ISearchable searchable) {
        resetNumberOfNodesEvaluated();

        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();
        startState.setCost(0);

        // Java PriorityQueue is implemented as a heap.
        PriorityQueue<AState> priorityQueue = new PriorityQueue<>((state1, state2) ->
                Double.compare(state1.getCost(), state2.getCost()));

        // Stores the lowest known cost for each discovered state.
        HashMap<AState, Double> bestCosts = new HashMap<>();

        // Stores states that were already evaluated with their lowest known cost.
        HashSet<AState> evaluatedStates = new HashSet<>();

        priorityQueue.add(startState);
        bestCosts.put(startState, startState.getCost());

        while (!priorityQueue.isEmpty()) {
            AState currentState = priorityQueue.poll();

            if (evaluatedStates.contains(currentState)) {
                continue;
            }

            evaluatedStates.add(currentState);
            numberOfNodesEvaluated++;

            // Once the goal is reached, rebuild the path using cameFrom pointers.
            if (currentState.equals(goalState)) {
                return backTraceSolution(currentState);
            }

            ArrayList<AState> possibleStates = searchable.getAllPossibleStates(currentState);
            for (AState possibleState : possibleStates) {
                double newCost = currentState.getCost() + getMoveCost(currentState, possibleState);

                if (!bestCosts.containsKey(possibleState) || newCost < bestCosts.get(possibleState)) {
                    // Save the better path and reinsert the state with its improved cost.
                    possibleState.setCost(newCost);
                    possibleState.setCameFrom(currentState);
                    bestCosts.put(possibleState, newCost);
                    priorityQueue.add(possibleState);
                }
            }
        }

        // No path was found.
        return new Solution(new ArrayList<>());
    }

    /**
     * Calculates the movement cost between two neighboring maze states.
     * Direct moves cost 10, diagonal moves cost 15.
     */
    private int getMoveCost(AState fromState, AState toState) {
        MazeState fromMazeState = (MazeState) fromState;
        MazeState toMazeState = (MazeState) toState;
        Position fromPosition = fromMazeState.getPosition();
        Position toPosition = toMazeState.getPosition();

        int rowDifference = Math.abs(fromPosition.getRowIndex() - toPosition.getRowIndex());
        int columnDifference = Math.abs(fromPosition.getColumnIndex() - toPosition.getColumnIndex());

        if (rowDifference == 1 && columnDifference == 1) {
            return DIAGONAL_MOVE_COST;
        }

        return DIRECT_MOVE_COST;
    }

    /**
     * @return the algorithm name used by the test runner.
     */
    @Override
    public String getName() {
        return "Best First Search";
    }
}
