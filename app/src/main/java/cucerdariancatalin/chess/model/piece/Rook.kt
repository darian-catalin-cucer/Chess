package com.github.zsoltk.chesso.model.piece

import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import cucerdariancatalin.chess.R
import kotlinx.parcelize.Parcelize

@Parcelize
class Rook(override val set: Set) : Piece {

    override val value: Int = 5

    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.rook_light
            BLACK -> R.drawable.rook_dark
        }

    override val symbol: String = when (set) {
        WHITE -> "♖"
        BLACK -> "♜"
    }

    override val textSymbol: String = "R"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnapshotState, directions)

    companion object {
        val directions = listOf(
            0 to -1,
            0 to 1,
            -1 to 0,
            1 to 0,
        )
    }
}
