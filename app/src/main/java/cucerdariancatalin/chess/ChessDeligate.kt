package cucerdariancatalin.chess

interface ChessDeligate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)
}