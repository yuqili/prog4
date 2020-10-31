package assignment;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static assignment.Board.Action.*;
import static assignment.Board.Result.*;
import static assignment.JTetris.*;
import static org.junit.jupiter.api.Assertions.*;

class TetrisBoardTest {
    private TetrisBoard board;
    Piece piece;
    Point point;

    @BeforeEach
    void setUp() {
        board = new TetrisBoard(WIDTH, HEIGHT + TOP_SPACE);
        piece = new TetrisPiece(Piece.PieceType.T);
        point = new Point(4, 10);
    }

    @Test
    void testMoveLeft_whenPieceInNormalPosition_shouldReturnSUCCESS() {
        board.nextPiece(piece, point);

        assertEquals(SUCCESS, board.move(LEFT));
    }

    @Test
    void testMoveLeft_whenPieceAtLeftMost_shouldReturnOUTBOUNDS() {
        board.nextPiece(piece, new Point(0, 10));

        assertEquals(OUT_BOUNDS, board.move(LEFT));
    }

    @Test
    void testMoveLeft_whenPieceIsBesidePlacedPiece_shouldReturnOUTBOUNDS() {
        Pair<Point, Piece> placedPiece = new Pair<>(new Point(4, 12), piece);
        board.fillBoardPieces(new Pair[]{placedPiece});
        board.nextPiece(piece, point);

        assertEquals(OUT_BOUNDS, board.move(LEFT));
    }

    @Test
    void testMove() {
    }

    @Test
    void getCurrentPiece() {
        board.nextPiece(piece, point);

        assertEquals(piece, board.getCurrentPiece());
    }

    @Test
    void getCurrentPiecePosition() {
        board.nextPiece(piece, point);

        assertEquals(point, board.getCurrentPiecePosition());
    }

    @Test
    void testNextPiece_withNormalPosition() {
        board.nextPiece(piece, point);

        assertEquals(piece, board.getCurrentPiece());
        assertEquals(point, board.getCurrentPiecePosition());
    }

    @Test
    void testNextPiece_withClashedPosition() {
        Pair<Point, Piece> placedPiece = new Pair<>(new Point(5, 11), piece);
        board.fillBoardPieces(new Pair[]{placedPiece});

        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            board.nextPiece(piece, point);
        });
    }

    @Test
    void testEquals() {
    }

    @Test
    void getLastResult() {
    }

    @Test
    void getLastAction() {
    }

    @Test
    void getRowsCleared() {
    }

    @Test
    void getWidth() {
    }

    @Test
    void getHeight() {
    }

    @Test
    void getMaxHeight() {
    }

    @Test
    void dropHeight() {
    }

    @Test
    void getColumnHeight() {
    }

    @Test
    void getRowWidth() {
    }

    @Test
    void getGrid() {
    }
}