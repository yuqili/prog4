package assignment;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import static assignment.Piece.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

class TetrisPieceTest {
    private Piece piece;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        piece = new TetrisPiece(T);
    }

    @org.junit.jupiter.api.Test
    void getType() {
        assertEquals(T, piece.getType());
    }

    @org.junit.jupiter.api.Test
    void getRotationIndex() {
        assertEquals(0, piece.getRotationIndex());
    }

    @org.junit.jupiter.api.Test
    void clockwisePiece() {
        Piece cwPiece = piece.clockwisePiece();

        assertEquals(T, cwPiece.getType());
        assertEquals(1, cwPiece.getRotationIndex());
    }

    @org.junit.jupiter.api.Test
    void counterclockwisePiece() {
        Piece ccwPiece = piece.counterclockwisePiece();

        assertEquals(T, ccwPiece.getType());
        assertEquals(3, ccwPiece.getRotationIndex());
    }

    @org.junit.jupiter.api.Test
    void getWidth() {
        assertEquals(T.getBoundingBox().width, piece.getWidth());
    }

    @org.junit.jupiter.api.Test
    void getHeight() {
        assertEquals(T.getBoundingBox().height, piece.getHeight());
    }

    @org.junit.jupiter.api.Test
    void getBody() {
        List<Point> body = Arrays.asList(piece.getBody());

        assertTrue(body.contains(new Point(0, 1)));
        assertTrue(body.contains(new Point(1, 1)));
        assertTrue(body.contains(new Point(2, 1)));
        assertTrue(body.contains(new Point(1, 2)));
    }

    @org.junit.jupiter.api.Test
    void getSkirt() {
        int[] skirt = piece.getSkirt();

        assertEquals(1, skirt[0]);
        assertEquals(1, skirt[1]);
        assertEquals(1, skirt[2]);
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Piece samePiece = new TetrisPiece(T);
        assertTrue(piece.equals(samePiece));

        Piece sameTypeDifferentRotation = piece.clockwisePiece();
        assertFalse(piece.equals(sameTypeDifferentRotation));

        Piece differentType = new TetrisPiece(STICK);
        assertFalse(piece.equals(differentType));
    }
}