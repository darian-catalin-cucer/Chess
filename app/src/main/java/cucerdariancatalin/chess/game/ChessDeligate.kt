package cucerdariancatalin.chess.game

import cucerdariancatalin.chess.board.ChessSquare

interface ChessDeligate {
    fun pieceAt(chessSquare: ChessSquare) : ChessPiece?
    fun movePiece(from: ChessSquare, to: ChessSquare)
}