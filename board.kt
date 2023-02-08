class ChessBoard {
    private val board = Array(8) { Array(8) { Piece() } }

    fun initBoard() {
        for (i in 0..7) {
            board[1][i] = Pawn(PieceColor.BLACK)
            board[6][i] = Pawn(PieceColor.WHITE)
        }

        board[0][0] = Rook(PieceColor.BLACK)
        board[0][7] = Rook(PieceColor.BLACK)
        board[7][0] = Rook(PieceColor.WHITE)
        board[7][7] = Rook(PieceColor.WHITE)

        board[0][1] = Knight(PieceColor.BLACK)
        board[0][6] = Knight(PieceColor.BLACK)
        board[7][1] = Knight(PieceColor.WHITE)
        board[7][6] = Knight(PieceColor.WHITE)

        board[0][2] = Bishop(PieceColor.BLACK)
        board[0][5] = Bishop(PieceColor.BLACK)
        board[7][2] = Bishop(PieceColor.WHITE)
        board[7][5] = Bishop(PieceColor.WHITE)

        board[0][3] = Queen(PieceColor.BLACK)
        board[0][4] = King(PieceColor.BLACK)
        board[7][3] = Queen(PieceColor.WHITE)
        board[7][4] = King(PieceColor.WHITE)
    }

    fun movePiece(fromX: Int, fromY: Int, toX: Int, toY: Int) {
        if (board[fromX][fromY].isValidMove(fromX, fromY, toX, toY)) {
            board[toX][toY] = board[fromX][fromY]
            board[fromX][fromY] = Piece()
        } else {
            println("Invalid move")
        }
    }
}

abstract class Piece {
    open fun isValidMove(fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean = false
}

class Pawn(private val color: PieceColor) : Piece() {
    override fun isValidMove(fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return if (color == PieceColor.BLACK) {
            if (fromX == 6 && fromX - toX in 1..2 && fromY == toY) {
                true
            } else fromX - toX == 1 && fromY == toY
        } else {
            if (fromX == 1 && toX - fromX in 1..2 && fromY == toY) {
                true
            } else toX - fromX == 1 && fromY == toY
        }
    }
}
