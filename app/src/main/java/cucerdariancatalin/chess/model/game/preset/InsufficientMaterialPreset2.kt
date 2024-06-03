package com.github.zsoltk.chesso.model.game.preset

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.board.Position.*
import cucerdariancatalin.chess.model.game.controller.GameController
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.piece.Bishop
import com.github.zsoltk.chesso.model.piece.King
import com.github.zsoltk.chesso.model.piece.Rook
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import com.github.zsoltk.chesso.ui.base.ChessoTheme
import com.github.zsoltk.chesso.ui.app.Preset

object InsufficientMaterialPreset2 : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    g8 to Rook(BLACK),
                    g2 to King(WHITE),
                    d5 to Bishop(WHITE),
                    c2 to Bishop(BLACK),
                )
            )
            reset(
                GameSnapshotState(
                    board = board,
                    toMove = WHITE
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsufficientMaterialPreset2Preview() {
    ChessoTheme {
        Preset(InsufficientMaterialPreset2)
    }
}

