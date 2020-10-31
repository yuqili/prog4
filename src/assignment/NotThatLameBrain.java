package assignment;

import java.util.*;

import static assignment.Board.Action.*;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class NotThatLameBrain implements Brain {
    private final int AGGREGATEHEIGHT_WEIGHT = -510066;
    private final int COMPLETELINES_WEIGHT = 760666;
    private final int HOLES_WEIGHT = -356630;
    private final int BUMPINESS_WEIGHT = -184483;

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        int best = Integer.MIN_VALUE;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        enumerateHorizonOptions(currentBoard, LEFT, LEFT);
        enumerateHorizonOptions(currentBoard, RIGHT, RIGHT);

        Board clockwise90 = currentBoard.testMove(CLOCKWISE);
        options.add(clockwise90.testMove(Board.Action.DROP));
        firstMoves.add(CLOCKWISE);
        enumerateHorizonOptions(clockwise90, LEFT, CLOCKWISE);
        enumerateHorizonOptions(clockwise90, RIGHT, CLOCKWISE);

        Board clockwise180 = clockwise90.testMove(CLOCKWISE);
        options.add(clockwise180.testMove(Board.Action.DROP));
        firstMoves.add(CLOCKWISE);
        enumerateHorizonOptions(clockwise180, LEFT, CLOCKWISE);
        enumerateHorizonOptions(clockwise180, RIGHT, CLOCKWISE);

        Board clockwise270 = clockwise180.testMove(CLOCKWISE);
        options.add(clockwise270.testMove(Board.Action.DROP));
        firstMoves.add(CLOCKWISE);
        enumerateHorizonOptions(clockwise270, LEFT, CLOCKWISE);
        enumerateHorizonOptions(clockwise270, RIGHT, CLOCKWISE);
    }

    private void enumerateHorizonOptions(Board currentBoard, Board.Action action, Board.Action recordAction) {
        Board next = currentBoard.testMove(action);
        while (next.getLastResult() == Board.Result.SUCCESS) {
            options.add(next.testMove(Board.Action.DROP));
            firstMoves.add(recordAction);
            next.move(action);
        }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private int scoreBoard(Board newBoard) {
        int aggregateHeight = 0;
        int holes = 0;
        int bumpiness = 0;
        int previousColumnHeight = 0;
        int maxHeight = newBoard.getMaxHeight();
        int width = newBoard.getWidth();

        for (int i = 0; i < maxHeight; i++) {
            holes += (width - newBoard.getRowWidth(i));
        }

        for(int i = 0; i < newBoard.getWidth(); i++) {
            int currentColumnHeight = newBoard.getColumnHeight(i);
            holes -= (maxHeight - currentColumnHeight);
            aggregateHeight += currentColumnHeight;
            if (i > 0) {
                bumpiness += Math.abs(currentColumnHeight - previousColumnHeight);
            }
            previousColumnHeight = currentColumnHeight;
        }

        int completeLines = newBoard.getRowsCleared();


        return AGGREGATEHEIGHT_WEIGHT * aggregateHeight
                + COMPLETELINES_WEIGHT * completeLines
                + HOLES_WEIGHT * holes
                + BUMPINESS_WEIGHT * bumpiness;
    }

}
