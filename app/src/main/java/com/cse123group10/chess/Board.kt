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
        piecebox.add(Pieces(1,1,Player.white, Type.rook))
        piecebox.add(Pieces(8,1,Player.white, Type.rook))
        piecebox.add(Pieces(2,1,Player.white, Type.knight))
        piecebox.add(Pieces(7,1,Player.white, Type.knight))
        piecebox.add(Pieces(3,1,Player.white, Type.bishop))
        piecebox.add(Pieces(6,1,Player.white, Type.bishop))
        piecebox.add(Pieces(4,1,Player.white, Type.queen))
        piecebox.add(Pieces(5,1,Player.white, Type.king))
        for (i in 1..8){
            piecebox.add(Pieces(i,2,Player.white, Type.pawn))
        }
        // add black pieces
        piecebox.add(Pieces(1,8,Player.white, Type.rook))
        piecebox.add(Pieces(8,8,Player.white, Type.rook))
        piecebox.add(Pieces(2,8,Player.white, Type.knight))
        piecebox.add(Pieces(7,8,Player.white, Type.knight))
        piecebox.add(Pieces(3,8,Player.white, Type.bishop))
        piecebox.add(Pieces(6,8,Player.white, Type.bishop))
        piecebox.add(Pieces(4,8,Player.white, Type.queen))
        piecebox.add(Pieces(5,8,Player.white, Type.king))
        for (i in 1..8){
            piecebox.add(Pieces(i,7,Player.white, Type.pawn))
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
        for (row in 8 downTo 1) {
            board += "$row|"
            for (col in 1..8) {
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