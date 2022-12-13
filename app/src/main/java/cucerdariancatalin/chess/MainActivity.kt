package cucerdariancatalin.chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() , ChessDeligate{


    private  lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        chessView=findViewById<ChessView>(R.id.chess_view)
        chessView.chessDeligate=this
        findViewById<Button>(R.id.reset_button).setOnClickListener{
            ChessModel.reset()
            chessView.invalidate()
        }


    }

    override fun pieceAt(square: Square): ChessPiece? {
        return ChessModel.pieceAt(square)
    }

    override fun movePiece(from: Square, to: Square) {


        ChessModel.movePiece(from,to)
        chessView.invalidate()

    }
}