package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable {

    private Maze maze;

    public SearchableMaze(Maze maze){
        this.maze = maze; //TODO: maybe use a copy constructor
    }

    @Override
    public AState getStartState() {
        MazeState state = new MazeState(maze.getStartPosition());
        return (AState) state;
    }

    @Override
    public AState getGoalState() {
        MazeState state = new MazeState(maze.getGoalPosition());
        return (AState) state;
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {
        return null;
    }
}
