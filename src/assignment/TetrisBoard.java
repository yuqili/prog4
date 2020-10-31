package assignment;


import javafx.util.Pair;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
    private Piece currentPiece;
    private Point currentPiecePosition;
    private int width;
    private int height;
    private List<Piece>[] boardPieces;
    private Result lastResult;
    private Action lastAction;
    private int rowsCleared;
    private int[] columnHeight;
    private List<Integer> rowWidth;
    private int maxHeight;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        columnHeight = new int[width];
        rowWidth = new ArrayList<>(height);
        for(int i = 0; i < height; i++)
            rowWidth.add(0);
        boardPieces = new LinkedList[width];
        for(int i = 0; i < width; i++) {
            boardPieces[i] = new LinkedList<>();
            for (int j = 0; j < height; j++) {
                boardPieces[i].add(null);
            }
        }
    }

    public TetrisBoard() {}

    /**
     * Check whether piece can move deltaX and deltaY from current piece position
     *
     * @param piece piece to move
     * @param position piece's current position
     * @param dx delta X
     * @param dy delta Y
     * @return true when the piece can take the move, otherwise false
     */
    private boolean canMove(Piece piece, Point position, int dx, int dy) {
        Point[] body = piece.getBody();
        for (int i = 0; i < body.length; i++) {
            int newX = position.x + body[i].x + dx;
            int newY = position.y + body[i].y + dy;
            if (newX < 0 || newX >= width || newY < 0 || boardPieces[newX].get(newY) != null)
                return false;
        }
        return true;
    }

    private boolean canMove(Piece piece, int dx, int dy) {
        return canMove(piece, currentPiecePosition, dx, dy);
    }

    @Override
    public Result move(Action act) {
        Result result = Result.SUCCESS;
        switch(act) {
            case DOWN:
                if (canMove(currentPiece, 0, -1)) {
                    currentPiecePosition.y--;
                } else {
                    result = Result.PLACE;
                    doPlace();
                }
                break;
            case LEFT:
                if (canMove(currentPiece, -1, 0)) {
                    currentPiecePosition.x--;
                } else
                    result = Result.OUT_BOUNDS;
                break;
            case RIGHT:
                if (canMove(currentPiece, 1, 0)) {
                    currentPiecePosition.x++;
                } else
                    result = Result.OUT_BOUNDS;
                break;
            case DROP:
                int dy = 0;
                while(canMove(currentPiece, 0, -1 * dy - 1)) {
                    dy++;
                }
                currentPiecePosition.y -= dy;
                result = Result.PLACE;
                doPlace();
                break;
            case CLOCKWISE:
                switch(currentPiece.getType()) {
                    case STICK:
                        result = kickWall(Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()], currentPiece.clockwisePiece());
                        break;
                    case T:
                    case LEFT_L:
                    case RIGHT_L:
                    case LEFT_DOG:
                    case RIGHT_DOG:
                        result = kickWall(Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()], currentPiece.clockwisePiece());
                        break;
                    default:
                }
                break;
            case COUNTERCLOCKWISE:
                switch(currentPiece.getType()) {
                    case STICK:
                        result = kickWall(Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()], currentPiece.counterclockwisePiece());
                        break;
                    case T:
                    case LEFT_L:
                    case RIGHT_L:
                    case LEFT_DOG:
                    case RIGHT_DOG:
                        result = kickWall(Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()], currentPiece.counterclockwisePiece());
                        break;
                    default:
                }
            default:
        }

        lastAction = act;
        lastResult = result;

        return result;
    }

    private Result kickWall(Point[] wallKicks, Piece piece) {
        for(int i = 0; i < wallKicks.length; i++) {
            if (canMove(piece, wallKicks[i].x, wallKicks[i].y)) {
                currentPiece = piece;
                currentPiecePosition.x += wallKicks[i].x;
                currentPiecePosition.y += wallKicks[i].y;
                return Result.SUCCESS;
            }
        }
        return Result.OUT_BOUNDS;
    }

    private void doPlace() {
        doFill();
        doClear();
    }

    private void doFill() {
        Point[] body = currentPiece.getBody();
        for (int i = 0; i < body.length; i++) {
            int x = currentPiecePosition.x + body[i].x;
            int y = currentPiecePosition.y + body[i].y;
            boardPieces[x].set(y, currentPiece);
            rowWidth.set(y, rowWidth.get(y)+1);
            if (y + 1 > columnHeight[x]) columnHeight[x] = y + 1;
            if (columnHeight[x] > maxHeight) maxHeight = columnHeight[x];
        }
    }

    private void doClear() {
        rowsCleared = 0;
        for (int i = height - 1; i >= 0; i--) {
            if (rowWidth.get(i) == width) {
                rowWidth.remove(i);
                rowWidth.add(0);
                for(int j = 0; j < width; j++) {
                    boardPieces[j].remove(i);
                    boardPieces[j].add(null);
                    columnHeight[j]--;
                }
                rowsCleared++;
                maxHeight--;
            }
        }
    }

//    private void doClear() {
//        for (int i = currentPiece.getHeight(); i > 0; i--) {
//            int currentY = currentPiecePosition.y + i - 1;
//            if (currentY >= 0) {
//                boolean clear = true;
//                for (int j = 0; j < width; j++) {
//                    if (boardPieces[j].get(currentY) == null) {
//                        clear = false;
//                        break;
//                    }
//                }
//                if (clear) {
//                    rowsCleared++;
//                    for (int j = 0; j < width; j++) {
//                        boardPieces[j].remove(currentY);
//                        boardPieces[j].add(null);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public Board testMove(Action act) {
        TetrisBoard board = new TetrisBoard();
        board.width = this.width;
        board.height = this.height;
        board.currentPiece = this.currentPiece;
        board.currentPiecePosition = new Point(this.currentPiecePosition.x, this.currentPiecePosition.y);
        board.rowWidth = new ArrayList<>(rowWidth);
        board.maxHeight = maxHeight;
        board.columnHeight = new int[width];
        board.boardPieces = new LinkedList[width];
        for(int i = 0; i < width; i++) {
            board.columnHeight[i] = columnHeight[i];
            board.boardPieces[i] = new LinkedList<>(this.boardPieces[i]);
        }

        board.move(act);

        return board;
    }

    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
        return currentPiecePosition;
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        if (canMove(p, spawnPosition, 0, 0)) {
            currentPiece = p;
            currentPiecePosition = spawnPosition;
        } else {
            throw new IllegalArgumentException("Could not place the piece at its spawn position");
        }
    }

    /**
     * For test only
     */
    List<Piece>[] fillBoardPieces(Pair<Point, Piece>[] pieces) {
        if (pieces != null) {
            for (int i = 0; i < pieces.length; i++) {
                int x = pieces[i].getKey().x;
                int y = pieces[i].getKey().y;
                boardPieces[x].set(y, pieces[i].getValue());
            }
        }

        return boardPieces;
    }

    @Override
    public boolean equals(Object other) { return false; }

    @Override
    public Result getLastResult() { return lastResult; }

    @Override
    public Action getLastAction() { return lastAction; }

    @Override
    public int getRowsCleared() { return rowsCleared; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        int dy = 0;
        Point point = new Point(x, height - piece.getHeight());
        while(canMove(currentPiece, point,0, -1 * dy - 1)) {
            dy++;
        }
        return dy;
    }

    @Override
    public int getColumnHeight(int x) {
        return columnHeight[x];
    }

    @Override
    public int getRowWidth(int y) {
        return rowWidth.get(y);
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        Piece piece = boardPieces[x].get(y);
        return piece == null ? null : piece.getType();
    }
}
