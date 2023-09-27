package cucerdariancatalin.chess.game

import android.util.Log
import cucerdariancatalin.chess.R
import cucerdariancatalin.chess.board.ChessSquare
import cucerdariancatalin.chess.TAG
import java.lang.Math.abs

object ChessGameModel {
    var pieceBox = mutableSetOf<ChessPiece>()

    init {
        reset()
    }

    fun canKnightMove(from: ChessSquare, to: ChessSquare): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    fun canRookMove(from: ChessSquare, to: ChessSquare): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)
        ) {
            return true
        }
        return false
    }

    private fun isClearHorizontallyBetween(from: ChessSquare, to: ChessSquare): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(ChessSquare(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearVerticallyBetween(from: ChessSquare, to: ChessSquare): Boolean {
        if (from.col != to.col) return false

        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(ChessSquare(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    fun isClearDiagonally(from: ChessSquare, to: ChessSquare): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i

            if (pieceAt(ChessSquare(nextCol, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    fun canBishopMove(from: ChessSquare, to: ChessSquare): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    fun canQueenMove(from: ChessSquare, to: ChessSquare): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    fun canKingMove(from: ChessSquare, to: ChessSquare): Boolean {
        if (canQueenMove(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    fun canMove(from: ChessSquare, to: ChessSquare): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return false
        }
        val movingPiece = pieceAt(from) ?: return false

        // Add logic for PAWN here

        when (movingPiece.rank) {
            ChessPieceType.KNIGHT -> return canKnightMove(from, to)
            ChessPieceType.ROOK -> return canRookMove(from, to)
            ChessPieceType.BISHOP -> return canBishopMove(from, to)
            ChessPieceType.QUEEN -> return canQueenMove(from, to)
            ChessPieceType.KING -> return canKingMove(from, to)
            ChessPieceType.PAWN -> {
                // TODO: Implement pawn movement logic
                Log.d(TAG, "Pawn movement not implemented yet.")
                return false
            }
        }
    }

    fun movePiece(from: ChessSquare, to: ChessSquare) {
        if (canMove(from, to)) {
            movePiece(from.col, from.row, to.col, to.row)
        }
    }

    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return
        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            pieceBox.remove(it)
        }
        pieceBox.remove(movingPiece)
        pieceBox.add(movingPiece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        pieceBox.removeAll(pieceBox)
        for (i in 0..1) {
            pieceBox.add(ChessPiece(0 + i * 7, 0, ChessPlayer.WHITE, ChessPieceType.ROOK, R.drawable.rook_white))
            pieceBox.add(ChessPiece(0 + i * 7, 7, ChessPlayer.BLACK, ChessPieceType.ROOK, R.drawable.rook_black))

            pieceBox.add(
                ChessPiece(
                    1 + i * 5,
                    0,
                    ChessPlayer.WHITE,
                    ChessPieceType.KNIGHT,
                    R.drawable.knight_white
                )
            )
            pieceBox.add(
                ChessPiece(
                    1 + i * 5,
                    7,
                    ChessPlayer.BLACK,
                    ChessPieceType.KNIGHT,
                    R.drawable.knight_black
                )
            )
            pieceBox.add(
                ChessPiece(
                    2 + i * 3,
                    0,
                    ChessPlayer.WHITE,
                    ChessPieceType.BISHOP,
                    R.drawable.bishop_white
                )
            )
            pieceBox.add(
                ChessPiece(
                    2 + i * 3,
                    7,
                    ChessPlayer.BLACK,
                    ChessPieceType.BISHOP,
                    R.drawable.bishop_black
                )
            )
            for (i in 0..7) {
                pieceBox.add(ChessPiece(i, 1, ChessPlayer.WHITE, ChessPieceType.PAWN, R.drawable.pawn_white))
                pieceBox.add(ChessPiece(i, 6, ChessPlayer.BLACK, ChessPieceType.PAWN, R.drawable.pawn_black))
            }
            pieceBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white))
            pieceBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessPieceType.QUEEN, R.drawable.queen_black))
            pieceBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessPieceType.KING, R.drawable.king_white))
            pieceBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black))
        }
    }

    fun pieceAt(chessSquare: ChessSquare): ChessPiece? {
        return pieceAt(chessSquare.col, chessSquare.row)
    }

    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in pieceBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            for (col in 0..7) {
                val piece = pieceAt(col, row)
                if (piece == null) {
                    desc += " ."
                } else {
                    val white = piece.player == ChessPlayer.WHITE
                    desc += " "
                    desc += when (piece.rank) {
                        ChessPieceType.KING -> {
                            if (white) "k" else "K"
                        }

                        ChessPieceType.QUEEN -> {
                            if (white) "q" else "Q"
                        }

                        ChessPieceType.BISHOP -> {
                            if (white) "b" else "B"
                        }

                        ChessPieceType.ROOK -> {
                            if (white) "r" else "R"
                        }

                        ChessPieceType.KNIGHT -> {
                            if (white) "n" else "N"
                        }

                        ChessPieceType.PAWN -> {
                            if (white) "p" else "P"
                        }
                    }
                }
            }
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }
}