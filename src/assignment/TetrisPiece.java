package assignment;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {
    private PieceType type;
    private int rotationIndex;
    private Point[] body;
    private int[] skirt;

    private static Map<PieceType, Piece[]> tetrisMap;

    static {
        tetrisMap = new HashMap<>();

        TetrisPiece[] T_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.T),
                new TetrisPiece(PieceType.T, 1, new Point[]{new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)}),
                new TetrisPiece(PieceType.T, 2, new Point[]{new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)}),
                new TetrisPiece(PieceType.T, 3, new Point[]{new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)})
        };
        tetrisMap.put(PieceType.T, T_Pieces);

        TetrisPiece[] SQUARE_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.SQUARE)
        };
        tetrisMap.put(PieceType.SQUARE, SQUARE_Pieces);

        TetrisPiece[] STICK_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.STICK),
                new TetrisPiece(PieceType.STICK, 1, new Point[]{new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3)}),
                new TetrisPiece(PieceType.STICK, 2, new Point[]{new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)}),
                new TetrisPiece(PieceType.STICK, 3, new Point[]{new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)})
        };
        tetrisMap.put(PieceType.STICK, STICK_Pieces);

        TetrisPiece[] LEFT_L_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.LEFT_L),
                new TetrisPiece(PieceType.LEFT_L, 1, new Point[]{new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)}),
                new TetrisPiece(PieceType.LEFT_L, 2, new Point[]{new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)}),
                new TetrisPiece(PieceType.LEFT_L, 3, new Point[]{new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)})
        };
        tetrisMap.put(PieceType.LEFT_L, LEFT_L_Pieces);

        TetrisPiece[] RIGHT_L_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.RIGHT_L),
                new TetrisPiece(PieceType.RIGHT_L, 1, new Point[]{new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2)}),
                new TetrisPiece(PieceType.RIGHT_L, 2, new Point[]{new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)}),
                new TetrisPiece(PieceType.RIGHT_L, 3, new Point[]{new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2)})
        };
        tetrisMap.put(PieceType.RIGHT_L, RIGHT_L_Pieces);

        TetrisPiece[] LEFT_DOG_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.LEFT_DOG),
                new TetrisPiece(PieceType.LEFT_DOG, 1, new Point[]{new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)}),
                new TetrisPiece(PieceType.LEFT_DOG, 2, new Point[]{new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)}),
                new TetrisPiece(PieceType.LEFT_DOG, 3, new Point[]{new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)})
        };
        tetrisMap.put(PieceType.LEFT_DOG, LEFT_DOG_Pieces);

        TetrisPiece[] RIGHT_DOG_Pieces = new TetrisPiece[] {
                new TetrisPiece(PieceType.RIGHT_DOG),
                new TetrisPiece(PieceType.RIGHT_DOG, 1, new Point[]{new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)}),
                new TetrisPiece(PieceType.RIGHT_DOG, 2, new Point[]{new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)}),
                new TetrisPiece(PieceType.RIGHT_DOG, 3, new Point[]{new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)})
        };
        tetrisMap.put(PieceType.RIGHT_DOG, RIGHT_DOG_Pieces);
    }

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        this(type, 0, type.getSpawnBody());
    }

    public TetrisPiece(PieceType type, int rotationIndex, Point[] body) {
        this.type = type;
        this.rotationIndex = rotationIndex;
        this.body = body;

        calculateSkirt();
    }

    private void calculateSkirt() {
        skirt = new int[type.getBoundingBox().width];
        Arrays.fill(skirt, Integer.MAX_VALUE);
        for(int i = 0; i < body.length; i++) {
            if (body[i].y < skirt[body[i].x])
                skirt[body[i].x] = body[i].y;
        }
    }

    @Override
    public PieceType getType() {
        return type;
    }

    @Override
    public int getRotationIndex() {
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        Piece[] typePieces = tetrisMap.get(type);
        int index = (rotationIndex + 1) % typePieces.length;
        return typePieces[index];
    }

    @Override
    public Piece counterclockwisePiece() {
        Piece[] typePieces = tetrisMap.get(type);
        int index = (rotationIndex - 1 + typePieces.length) % typePieces.length;
        return typePieces[index];
    }

    @Override
    public int getWidth() {
        return type.getBoundingBox().width;
    }

    @Override
    public int getHeight() {
        return type.getBoundingBox().height;
    }

    @Override
    public Point[] getBody() {
        return body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        return this.type == otherPiece.getType() && this.rotationIndex == otherPiece.getRotationIndex();
    }
}
