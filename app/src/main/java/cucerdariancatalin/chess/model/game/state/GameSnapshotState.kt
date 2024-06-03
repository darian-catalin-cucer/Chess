package com.github.zsoltk.chesso.model.game.state

import android.os.Parcelable
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.game.Resolution
import com.github.zsoltk.chesso.model.move.AppliedMove
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.move.Capture
import com.github.zsoltk.chesso.model.move.MoveEffect
import com.github.zsoltk.chesso.model.move.targetPositions
import com.github.zsoltk.chesso.model.piece.King
import com.github.zsoltk.chesso.model.piece.Piece
import com.github.zsoltk.chesso.model.piece.Set
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSnapshotState(
    val board: Board = Board(),
    val toMove: Set = Set.WHITE,
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val move: AppliedMove? = null,
    val lastMove: AppliedMove? = null,
    val castlingInfo: CastlingInfo = CastlingInfo.from(board),
    val capturedPieces: List<Piece> = emptyList()
) : Parcelable {

    val score: Int =
        board.pieces.values.sumOf {
            it.value * if (it.set == WHITE) 1 else -1
        }

    private val allLegalMoves by lazy {
        board.pieces.flatMap { (_, piece) ->
            piece
                .pseudoLegalMoves(this, false)
                .applyCheckConstraints()
        }
    }

    fun toRepetitionRelevantState(): RepetitionRelevantState =
        RepetitionRelevantState(
            board = board,
            toMove = toMove,
            castlingInfo = castlingInfo
        )

    fun hasCheck(): Boolean =
        hasCheckFor(toMove)

    fun hasCheckFor(set: Set): Boolean {
        val kingsPosition: Position = board.find<King>(set).firstOrNull()?.position ?: return false

        return hasCheckFor(kingsPosition)
    }

    fun hasCheckFor(position: Position): Boolean =
        board.pieces.any { (_, piece) ->
            val otherPieceCaptures: List<BoardMove> = piece.pseudoLegalMoves(this, true)
                .filter { it.preMove is Capture }

            position in otherPieceCaptures.targetPositions()
        }

    fun legalMovesTo(position: Position): List<BoardMove> =
        allLegalMoves.filter { it.to == position }

    fun legalMovesFrom(position: Position): List<BoardMove> =
        board[position]
            .piece
            ?.pseudoLegalMoves(this, false)
            ?.applyCheckConstraints()
            ?: emptyList()

    @JvmName("applyCheckConstraints1")
    fun applyCheckConstraints(moves: List<BoardMove>): List<BoardMove> =
        moves.applyCheckConstraints()

    fun List<BoardMove>.applyCheckConstraints(): List<BoardMove> =
        filter { move ->
            // Any move made should result in no check (clear current if any, and not cause a new one)
            val newGameState = derivePseudoGameState(move)
            !newGameState.hasCheckFor(move.piece.set)
        }


    fun calculateAppliedMove(boardMove: BoardMove, statesSoFar: List<GameSnapshotState>): GameStateTransition {
        val tempNewGameState = derivePseudoGameState(boardMove)
        val validMoves = with(tempNewGameState) {
             board.pieces(toMove).filter { (position, _) ->
                tempNewGameState.legalMovesFrom(position).isNotEmpty()
            }
        }
        val isCheck = tempNewGameState.hasCheck()
        val isCheckNoMate = validMoves.isNotEmpty() && isCheck
        val isCheckMate = validMoves.isEmpty() && isCheck
        val isStaleMate = validMoves.isEmpty() && !isCheck
        val insufficientMaterial = tempNewGameState.board.pieces.hasInsufficientMaterial()
        val threefoldRepetition = (statesSoFar + tempNewGameState).hasThreefoldRepetition()

        val appliedMove = AppliedMove(
            boardMove = boardMove.applyAmbiguity(this),
            effect = when {
                isCheckNoMate -> MoveEffect.CHECK
                isCheckMate -> MoveEffect.CHECKMATE
                isStaleMate -> MoveEffect.DRAW
                insufficientMaterial -> MoveEffect.DRAW
                threefoldRepetition -> MoveEffect.DRAW
                else -> null
            },
        )

        return GameStateTransition(
            move = appliedMove,
            fromSnapshotState = this.copy(
                move = appliedMove
            ),
            toSnapshotState = tempNewGameState.copy(
                resolution = when {
                    isCheckMate -> Resolution.CHECKMATE
                    isStaleMate -> Resolution.STALEMATE
                    threefoldRepetition -> Resolution.DRAW_BY_REPETITION
                    insufficientMaterial -> Resolution.INSUFFICIENT_MATERIAL
                    else -> Resolution.IN_PROGRESS
                },
                move = null,
                lastMove = appliedMove,
                castlingInfo = castlingInfo.apply(boardMove),
                capturedPieces = (boardMove.preMove as? Capture)?.let { capturedPieces + it.piece } ?: capturedPieces
            )
        )
    }

    fun derivePseudoGameState(boardMove: BoardMove): GameSnapshotState {
        val updatedBoard = board
            .apply(boardMove.preMove)
            .apply(boardMove.move)
            .apply(boardMove.consequence)

        return copy(
            board = updatedBoard,
            toMove = toMove.opposite(),
            move = null,
            lastMove = AppliedMove(
                boardMove = boardMove,
                effect = null
            )
        )
    }
}
