 package com.cse123group10.chess

 import kotlin.math.abs

 // this module contains the chess logic portion of the code
// it handles placing and mocing pieces
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
    fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTo: Int){
        val origPiece = pieceAt(xOrig,yOrig)
        if (origPiece != null){
            val piece = pieceAt(xTo,yTo)
            if(piece != null){
                if (origPiece.player != piece.player) {
                    if(origPiece.type == Type.pawn && attackPawn(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        killPiece(origPiece,piece,xTo,yTo)
                    }else if(origPiece.type == Type.rook && moveRook(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        killPiece(origPiece,piece,xTo,yTo)
                    }else if(origPiece.type == Type.king){
                        if(moveKing(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            killPiece(origPiece,piece,xTo,yTo)
                        }
                    }else if(origPiece.type == Type.knight){
                        if(moveKnight(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            killPiece(origPiece,piece,xTo,yTo)
                        }
                    }else if(origPiece.type == Type.bishop){
                        if(moveBishop(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            killPiece(origPiece,piece,xTo,yTo)
                        }
                    }else if(origPiece.type == Type.queen){
                        if(moveQueen(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            killPiece(origPiece,piece,xTo,yTo)
                        }
                    }
                }else{
                    return
                }
            }else{
                if(origPiece.type == Type.pawn){
                    if(movePawn(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }else if(origPiece.type == Type.rook){
                    if(moveRook(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }else if(origPiece.type == Type.king){
                    if(moveKing(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }else if(origPiece.type == Type.queen){
                    if(moveQueen(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }else if(origPiece.type == Type.bishop){
                    if(moveBishop(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }else if(origPiece.type == Type.knight){
                    if(moveKnight(xOrig,yOrig,xTo,yTo,origPiece.player)){
                        movePiece(origPiece,xTo,yTo)
                    }
                }
            }
        }
    }
    private fun killPiece(movingPiece: Pieces, deadPiece: Pieces, xTo: Int, yTo: Int){
        piecebox.remove(deadPiece)
        piecebox.remove(movingPiece)
        piecebox.add(Pieces(xTo,yTo,movingPiece.player,movingPiece.type,movingPiece.id))
    }
    private fun movePiece(movingPiece: Pieces, xTo: Int, yTo: Int){
        piecebox.remove(movingPiece)
        piecebox.add(Pieces(xTo,yTo,movingPiece.player,movingPiece.type,movingPiece.id))
    }
    // sees if pawn is able move forward
    private fun movePawn(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if (color == Player.white) {
            if(yOrig == 6 && xOrig==xTo){
                if(yTo == 5 && pieceAt(xTo,yTo)==null){
                    return true
                }else if(yTo == 4 && pieceAt(xTo,5)==null && pieceAt(xTo,4)==null){
                    return true
                }
            }else if(xOrig==xTo && yOrig-1 == yTo){
                return pieceAt(xTo,yTo)==null
            }
        }else if(color == Player.black){
            if(yOrig == 1 && xOrig==xTo){
                if(yTo == 2 && pieceAt(xTo,yTo)==null){
                    return true
                }else if(yTo == 3 && pieceAt(xTo,2)==null && pieceAt(xTo,3)==null){
                    return true
                }
            } else if(xOrig==xTo && yOrig+1 == yTo){
                return pieceAt(xTo,yTo)==null
            }
        }
        return false
    }
    // checks to see if pawn is able to attack
    private fun attackPawn(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if (color == Player.white) {
            if(xOrig-1==xTo || xOrig+1==xTo){
                if(yOrig-1 == yTo){
                    return pieceAt(xTo,yTo)!=null
                }
            }
        }else if(color == Player.black){
            if(xOrig-1==xTo || xOrig+1==xTo){
                if(yOrig+1 == yTo){
                    return pieceAt(xTo,yTo)!=null
                }
            }
        }
        return false
    }
    //
    private  fun moveRook(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if(xOrig == xTo || yOrig == yTo){
            return true
        }
        return false
    }
    private fun moveBishop(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if(xOrig==xTo||yOrig==yTo){
            return false
        }
        var deltaX = abs(xOrig-xTo)
        var deltaY = abs(yOrig-yTo)
        if(deltaX==deltaY){
            return true
        }
        return false
    }
    private fun moveQueen(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        return moveBishop(xOrig,yOrig,xTo,yTo,color)||moveRook(xOrig,yOrig,xTo,yTo,color)
    }
    private fun moveKing(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if((xOrig+1==xTo || xOrig-1 == xTo || xOrig == xTo) && (yOrig+1==yTo||yOrig-1==yTo || yOrig == yTo)){
            return true
        }
        return false
    }
    private fun moveKnight(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if((xOrig+2==xTo || xOrig-2==xTo) && (yOrig+1==yTo||yOrig-1==yTo)){
            return true
        }
        if((xOrig+1==xTo || xOrig-1==xTo) && (yOrig+2==yTo||yOrig-2==yTo)){
            return true
        }
        return false
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