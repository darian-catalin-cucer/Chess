package cucerdariancatalin.chess

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cucerdariancatalin.chess.board.ChessBoardView
import cucerdariancatalin.chess.board.ChessSquare
import cucerdariancatalin.chess.game.ChessDeligate
import cucerdariancatalin.chess.game.ChessGameModel
import cucerdariancatalin.chess.game.ChessPiece

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDeligate {

    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chessBoardView = findViewById<ChessBoardView>(R.id.chess_view)
        chessBoardView.chessDeligate = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            ChessGameModel.reset()
            chessBoardView.invalidate()
        }
    }

    override fun pieceAt(chessSquare: ChessSquare): ChessPiece? {
        return ChessGameModel.pieceAt(chessSquare)
    }

    override fun movePiece(from: ChessSquare, to: ChessSquare) {
        ChessGameModel.movePiece(from, to)
        chessBoardView.invalidate()
    }
}