 package com.cse123group10.chess

class Board {
    // creates all pieces
    var piecebox = mutableSetOf<Pieces>()
    //
    init{
        piecebox.removeAll(piecebox)
        startUp()
    }
    // add pieces to correct locations
    fun startUp(){
        piecebox.removeAll(piecebox)
        // add white pieces
        piecebox.add(Pieces(0,7,Player.white, Type.rook,R.drawable.whiterook))
        piecebox.add(Pieces(7,7,Player.white, Type.rook,R.drawable.whiterook))
        piecebox.add(Pieces(1,7,Player.white, Type.knight,R.drawable.whiteknight))
        piecebox.add(Pieces(6,7,Player.white, Type.knight,R.drawable.whiteknight))
        piecebox.add(Pieces(2,7,Player.white, Type.bishop,R.drawable.whitebishop))
        piecebox.add(Pieces(5,7,Player.white, Type.bishop,R.drawable.whitebishop))
        piecebox.add(Pieces(3,7,Player.white, Type.queen,R.drawable.whitequeen))
        piecebox.add(Pieces(4,7,Player.white, Type.king,R.drawable.whiteking))
        for (i in 0..7){
            piecebox.add(Pieces(i,6,Player.white, Type.pawn,R.drawable.whitepawn))
        }
        // add black pieces
        piecebox.add(Pieces(0,0,Player.black, Type.rook,R.drawable.blackrook))
        piecebox.add(Pieces(7,0,Player.black, Type.rook,R.drawable.blackrook))
        piecebox.add(Pieces(1,0,Player.black, Type.knight,R.drawable.blackknight))
        piecebox.add(Pieces(6,0,Player.black, Type.knight,R.drawable.blackknight))
        piecebox.add(Pieces(2,0,Player.black, Type.bishop,R.drawable.blackbishop))
        piecebox.add(Pieces(5,0,Player.black, Type.bishop,R.drawable.blackbishop))
        piecebox.add(Pieces(3,0,Player.black, Type.queen,R.drawable.blackqueen))
        piecebox.add(Pieces(4,0,Player.black, Type.king,R.drawable.blackking))
        for (i in 0..7){
            piecebox.add(Pieces(i,1,Player.black, Type.pawn,R.drawable.blackpawn))
        }
    }
    // see if piece is at a certain location
    fun pieceAt(col: Int, row: Int): Pieces? {
        for (piece in piecebox){
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }
    // creates a printout of the board in the terminal(logcat)
    override fun toString(): String {
        var board = " \n"
        for (row in 7 downTo 0) {
            board += "${row+1}|"
            for (col in 0..7) {
                val piece = pieceAt(col, row)
                val color = piece?.player
                if (piece != null){
                    board += when(piece.type){
                        Type.king -> {
                            if (piece.player == Player.white) " K" else " k"
                        }
                        Type.queen -> {
                            if (piece.player == Player.white) " Q" else " q"
                        }
                        Type.knight -> {
                            if (piece.player == Player.white) " N" else " n"
                        }
                        Type.bishop -> {
                            if (piece.player == Player.white) " B" else " b"
                        }
                        Type.rook -> {
                            if (piece.player == Player.white) " R" else " r"
                        }
                        Type.pawn -> {
                            if (piece.player == Player.white) " P" else " p"
                        }
                    }
                }else {
                    board += " O"
                }
            }
            board += "\n"
        }
        board += "   ---------------\n"
        board += "   A B C D E F G H\n"
        return board
    }
}