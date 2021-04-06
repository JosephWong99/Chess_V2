 package com.cse123group10.chess

 import android.util.Log
 import kotlin.math.abs
/*TODO
  add en passant - spencer
  */


 // this module contains the chess logic portion of the code
// it handles placing and mocing pieces
class Board {
    // creates all pieces
     var whiterookr = 0
     var whiterookl = 0
     var blackrookl = 0
     var blackrookr = 0
     var blackkingmove = 0
     var whitekingmove = 0
     var castleMoveWhite = 0
     var castleMoveBlack = 0
    var piecebox = mutableSetOf<Pieces>()
     var turn: Player = Player.white
     var whiteKingX = 4
     var whiteKingY = 7
     var blackKingX = 4
     var blackKingY = 0
    //
    init{
        piecebox.removeAll(piecebox)
        startUp()
    }
    // add pieces to correct locations
    fun startUp(){
        piecebox.removeAll(piecebox)
        turn = Player.white
        whiteKingX = 4
        whiteKingY = 7
        blackKingX = 4
        blackKingY = 0
        whiterookr = 0
        whiterookl = 0
        blackrookl = 0
        blackrookr = 0
        blackkingmove = 0
        whitekingmove = 0
        castleMoveWhite = 0
        castleMoveBlack = 0
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
    fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTon: Int){
        val origPiece = pieceAt(xOrig,yOrig)
        // used to prevent pieces from going off the board
        var yTo: Int
        if (yTon < 0){
            yTo = 0
        }else{
            yTo = yTon
        }
        if (origPiece != null){
            if(origPiece.player == turn){
                val piece = pieceAt(xTo,yTo)
                if(piece != null){
                    if (origPiece.player != piece.player) {
                        if(origPiece.type == Type.pawn && attackPawn(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            if(promotion(xTo,yTo, origPiece.player)){
                                piecebox.remove(piece)
                                piecebox.remove(origPiece)
                                if (origPiece.player == Player.white){
                                    piecebox.add(Pieces(xTo,yTo,origPiece.player,Type.queen,R.drawable.whitequeen))
                                }else {
                                    piecebox.add(Pieces(xTo,yTo,origPiece.player,Type.queen,R.drawable.blackqueen))
                                }
                            }else{
                                killPiece(origPiece,piece,xTo,yTo)
                            }
                            turn = changeTurn(turn)
                        }else if(origPiece.type == Type.rook && moveRook(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            killPiece(origPiece,piece,xTo,yTo)
                            turn = changeTurn(turn)
                        }else if(origPiece.type == Type.king){
                            if(moveKing(xOrig,yOrig,xTo,yTo,origPiece.player)){
                                killPiece(origPiece,piece,xTo,yTo)
                                if(origPiece.player == Player.white){
                                    whiteKingX = xTo
                                    whiteKingY = yTo
                                }else{
                                    blackKingX = xTo
                                    blackKingY = yTo
                                }
                                turn = changeTurn(turn)
                            }
                        }else if(origPiece.type == Type.knight){
                            if(moveKnight(xOrig,yOrig,xTo,yTo,origPiece.player)){
                                killPiece(origPiece,piece,xTo,yTo)
                                turn = changeTurn(turn)
                            }
                        }else if(origPiece.type == Type.bishop){
                            if(moveBishop(xOrig,yOrig,xTo,yTo,origPiece.player)){
                                killPiece(origPiece,piece,xTo,yTo)
                                turn = changeTurn(turn)
                            }
                        }else if(origPiece.type == Type.queen){
                            if(moveQueen(xOrig,yOrig,xTo,yTo,origPiece.player)){
                                killPiece(origPiece,piece,xTo,yTo)
                                turn = changeTurn(turn)
                            }
                        }
                        // check if king is in danger after a piece is moved
                        if(origPiece.player == Player.white){
                            if(kingCheck(blackKingX,blackKingY,Player.black)){
                                checkMoveFlag = 1
                            }
                        }else{
                            if(kingCheck(whiteKingX,whiteKingY,Player.white)){
                                checkMoveFlag = 1
                            }
                        }
                    }else{
                        return
                    }
            // moving to a blank square
                }else{
                    if(origPiece.type == Type.pawn){
                        if(movePawn(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            if(promotion(xTo,yTo, origPiece.player)){
                                piecebox.remove(piece)
                                piecebox.remove(origPiece)
                                if (origPiece.player == Player.white){
                                    piecebox.add(Pieces(xTo,yTo,origPiece.player,Type.queen,R.drawable.whitequeen))
                                }else {
                                    piecebox.add(Pieces(xTo,yTo,origPiece.player,Type.queen,R.drawable.blackqueen))
                                }
                            }else{
                                movePiece(origPiece,xTo,yTo)
                            }
                            turn = changeTurn(turn)
                        }
                    }else if(origPiece.type == Type.rook){
                        if(moveRook(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            movePiece(origPiece,xTo,yTo)
                            turn = changeTurn(turn)
                        }
                    }else if(origPiece.type == Type.king){
                        if(moveKing(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            movePiece(origPiece,xTo,yTo)
                            //
                            if(xTo == 2 && origPiece.player == Player.white){
                                if(castleMoveWhite == 1){
                                    castleMoveWhite = 2
                                    pieceAt(0,7)?.let { movePiece(it,3,7) }
                                }
                            }else if(xTo == 2 && origPiece.player == Player.black){
                                if(castleMoveBlack == 1){
                                    castleMoveBlack = 2
                                    pieceAt(0,0)?.let { movePiece(it,3,0) }
                                }
                            } else if(xTo == 6 && origPiece.player == Player.white){
                                if(castleMoveWhite == 1){
                                    castleMoveWhite = 2
                                    pieceAt(7,7)?.let { movePiece(it,5,7) }
                                }
                            } else if(xTo == 6 && origPiece.player == Player.black){
                                if(castleMoveBlack == 1){
                                    castleMoveBlack = 2
                                    pieceAt(7,0)?.let { movePiece(it,5,0) }
                                }
                            }
                            if(origPiece.player == Player.white){
                                whiteKingX = xTo
                                whiteKingY = yTo
                            }else{
                                blackKingX = xTo
                                blackKingY = yTo
                            }
                            turn = changeTurn(turn)
                        }
                    }else if(origPiece.type == Type.queen){
                        if(moveQueen(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            movePiece(origPiece,xTo,yTo)
                            turn = changeTurn(turn)
                        }
                    }else if(origPiece.type == Type.bishop){
                        if(moveBishop(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            movePiece(origPiece,xTo,yTo)
                            turn = changeTurn(turn)
                        }
                    }else if(origPiece.type == Type.knight){
                        if(moveKnight(xOrig,yOrig,xTo,yTo,origPiece.player)){
                            movePiece(origPiece,xTo,yTo)
                            turn = changeTurn(turn)
                        }
                    }
                    // check if king is in danger after a piece is moved
                    if(origPiece.player == Player.white){
                        if(kingCheck(blackKingX,blackKingY,Player.black)){
                            checkMoveFlag = 1
                        }
                    }else{
                        if(kingCheck(whiteKingX,whiteKingY,Player.white)){
                            checkMoveFlag = 1
                        }
                    }
                }
            }
        }
    }
     // keep track of which players turn it is
     private fun changeTurn(current: Player): Player{
         if(current == Player.white){
             return Player.black
         }else{
             return Player.white
         }
     }

     // remove a piece a
    private fun killPiece(movingPiece: Pieces, deadPiece: Pieces, xTo: Int, yTo: Int){
        piecebox.remove(deadPiece)
        piecebox.remove(movingPiece)
        piecebox.add(Pieces(xTo,yTo,movingPiece.player,movingPiece.type,movingPiece.id))
    }
    private fun movePiece(movingPiece: Pieces, xTo: Int, yTo: Int){
        piecebox.remove(movingPiece)
        piecebox.add(Pieces(xTo,yTo,movingPiece.player,movingPiece.type,movingPiece.id))
    }
     // promotion, all pieces will automatically convert to queen
     private fun promotion(xTo: Int, yTo: Int, color: Player): Boolean{
         if (color == Player.white && yTo==0){
             return true
         }
         if (color == Player.black && yTo==7){
             return true
         }
         return false
     }
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
    private  fun moveRook(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        if(HorizontalCheck(xOrig,xTo,yOrig,yTo,color) || VerticalCheck(xOrig,xTo,yOrig,yTo,color)){
            if(color == Player.white && xOrig == 7 && yOrig == 7){
                whiterookr = 1
                return true
            }
            else if(color == Player.white && xOrig == 7 && yOrig ==0){
                whiterookl = 1
                return true
            }
            else if(color == Player.black && xOrig == 0 && yOrig == 7){
                blackrookr = 1
                return true
            }
            else if(color == Player.black && xOrig == 0 && yOrig == 0){
                blackrookl = 1
                return true
            }
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
            if(deltaX == 1){
                return true
            }
            if(diagCheck(xOrig,yOrig,xTo,yTo,color)){
                return true
            }
        }
        return false
    }
    private fun moveQueen(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
        return moveBishop(xOrig,yOrig,xTo,yTo,color)||moveRook(xOrig,yOrig,xTo,yTo,color)
    }
     private fun moveKing(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
         if((xOrig+1==xTo || xOrig-1 == xTo || xOrig == xTo) && (yOrig+1==yTo||yOrig-1==yTo || yOrig == yTo)){
             if(!kingCheck(xTo,yTo,color)){
                 if (color == Player.white){
                     whitekingmove = 1
                     return true
                 }
                 else if (color == Player.black){
                     blackkingmove = 1
                     return true
                 }

             }else{
                 Log.d(debug_TAG,"king in check")
                 checkFlag = 1;
             }
         }
         if(castling(xOrig, xTo, yOrig, yTo, color)){
             if(!kingCheck(xTo,yTo,color)){
                 if(color == Player.white){
                     castleMoveWhite = 1
                 }else if(color == Player.black){
                     castleMoveBlack = 1
                 }
                 return true
             }else{
                 Log.d(debug_TAG,"king in check")
                 checkFlag = 1;
             }
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
     private  fun diagCheck(xOrig: Int, yOrig: Int, xTo: Int,yTo: Int, color: Player): Boolean{
         var deltaX = abs(xOrig-xTo)
         // north west
         if(xOrig-xTo>0&&yOrig-yTo>0) {
             for (i in 1..(deltaX-1)) {
                 if (pieceAt(xOrig - i, yOrig - i) != null) {
                     return false
                 }
             }
         }else
         // north east
         if(xOrig-xTo<0&&yOrig-yTo>0) {
             for (i in 1..(deltaX-1)) {
                 if (pieceAt(xOrig + i, yOrig - i) != null) {
                     return false
                 }
             }
         }else
         // south west
         if(xOrig-xTo>0&&yOrig-yTo<0) {
             for (i in 1..(deltaX-1)) {
                 if (pieceAt(xOrig - i, yOrig + i) != null) {
                     return false
                 }
             }
         }else
         // south east
         if(xOrig-xTo<0&&yOrig-yTo<0) {
             for (i in 1..(deltaX-1)) {
                 if (pieceAt(xOrig + i, yOrig + i) != null) {
                     return false
                 }
             }
         }
         return true
     }
     // created by spencer
     private fun HorizontalCheck (xOrig: Int, xTo: Int, yOrig: Int, yTo: Int, color: Player ): Boolean {
         if (xOrig != xTo) {
             return false
         }
         val gap = abs(yOrig - yTo) - 1
         if (gap == 0) return true
         for (i in 1..gap){
             val nextCol = if (yTo > yOrig) yOrig + i else yOrig - i
             if (pieceAt(xOrig, nextCol ) != null) {
                 return false
             }
         }
         return true
     }
    // created by spencer
     private fun VerticalCheck (xOrig: Int, xTo: Int, yOrig: Int, yTo: Int, color: Player ): Boolean {
         if (yOrig != yTo) {
             return false
         }
         val gap = abs(xOrig - xTo) - 1
         if (gap == 0) return true
         for (i in 1..gap){
             val nextRow = if (xTo > xOrig) xOrig + i else xOrig - i
             if (pieceAt(nextRow, yOrig) != null) {
                 return false
             }
         }
         return true
     }
     // created by spencer
     private fun checkCastlingl (xOrig: Int, xTo: Int, yOrig: Int, yTo: Int, color: Player ): Boolean {
         //have three variables that check your king move, your rook moves, king cant be in check
         // no piece can be between king and rook
         if (color == Player.white) {
             if (HorizontalCheck(0,3,7,7,color)) {
                 if ((whitekingmove == 1) && (whiterookl == 1))
                     return false
             }
         }
         if (color == Player.black){
             if (HorizontalCheck(0,3,0,0,color)) {
                 if ((blackkingmove == 1) && (blackrookl == 1))
                     return false
             }
         }
         return true
     }
     // created by spencer
     private fun checkCastlingr (xOrig: Int, xTo: Int, yOrig: Int, yTo: Int, color: Player ): Boolean {
         if (color == Player.white) {
             if (HorizontalCheck(7,5,7,7,color)) {
                 if ((whitekingmove == 1) && (whiterookr == 1))
                     return false
             }
         }
         if (color == Player.black){
             if (HorizontalCheck(7,5,0,0,color)) {
                 if ((blackkingmove == 1) && (blackrookr == 1))
                     return false
             }
         }
         return true
     }
     // created by spencer
     private fun castling (xOrig: Int, xTo: Int, yOrig: Int, yTo: Int, color: Player ): Boolean {
         if (color == Player.white){
             if  (xOrig+2==xTo && checkCastlingr(xOrig, xTo, yOrig, yTo, color)) {
                 return true
             }
             if (xOrig-2==xTo && checkCastlingl(xOrig, xTo, yOrig, yTo, color)) {
                 return true
             }
         }
         if (color == Player.black){
             if (xOrig+2==xTo && checkCastlingr(xOrig, xTo, yOrig, yTo, color)) {
                 return true
             }
             if (xOrig-2==xTo && checkCastlingl(xOrig, xTo, yOrig, yTo, color)) {
                 return true
             }
         }
         return false
     }
     // assumes valid king move, checks if king is in danger(mate)
     // returns true if king is in mate
    private fun kingCheck(x: Int, y: Int, color: Player ): Boolean{
        var piece = kingCheckVertical(x,y,color)
         if(piece != null){
             return true
         }
         piece = kingCheckHorizontal(x,y,color)
         if(piece != null){
             return true
         }
         piece = kingCheckDiag(x,y,color)
         if(piece != null){
             return true
         }
         piece = kingCheckL(x,y,color)
         if(piece != null){
             return true
         }
//         piece = kingCheckPawn(x,y,color)
//         if(piece != null){
//             return true
//         }
        return false
    }
     // returns a rook or queen that is in the same col as y
     private  fun  kingCheckVertical(x: Int, y: Int, color: Player ): Pieces?{
         for(i in (y-1) downTo 0){
             val piece = pieceAt(x,i)
             if(piece != null){
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.rook)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         for (i in (y+1)..7){
             val piece = pieceAt(x,i)
             if(piece != null){
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.rook)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         return null
     }
     // returns a rook or queen that is in the same row as x
     private  fun  kingCheckHorizontal(x: Int, y: Int, color: Player ): Pieces?{
         for(i in (x-1) downTo 0){
             val piece = pieceAt(i,y)
             if(piece != null){
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.rook)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         for (i in (x+1)..7){
             val piece = pieceAt(i,y)
             if(piece != null){
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.rook)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         return null
     }
     // returns a bishop or queen that is in the same diagonal
     private  fun  kingCheckDiag(x: Int, y: Int, color: Player ): Pieces?{
         // north west
         var col = x
         var row = y
         for (i in (x-1) downTo 0) {
             col -= 1
             row -= 1
             val piece = pieceAt(col,row)
             if(col<0 || row<0) break
             if (piece != null) {
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.bishop)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         // north east
         col = x
         row = y
         for (i in (x+1)..7) {
             col += 1
             row -= 1
             val piece = pieceAt(col,row)
             if(col>8 || row<0) break
             if (piece != null) {
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.bishop)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         // south west
         col = x
         row = y
         for (i in (y-1) downTo 0) {
             col -= 1
             row += 1
             val piece = pieceAt(col,row)
             if(col<0 || row>8) break
             if (piece != null) {
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.bishop)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         // south east
         col = x
         row = y
         for (i in (y+1)..7) {
             col += 1
             row += 1
             val piece = pieceAt(col,row)
             if(col>8 || row>8) break
             if (piece != null) {
                 if(piece.type==Type.king && color == piece.player) continue
                 if((piece.type==Type.queen || piece.type==Type.bishop)&& piece.player!=color){
                     return piece
                 }else{
                     break
                 }
             }
         }
         return null
     }
     // returns a knight that is able to attack the king
     private  fun  kingCheckL(x: Int, y: Int, color: Player ): Pieces?{
         var piece = pieceAt(x+2,y+1)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x+2,y-1)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x-2,y+1)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x-2,y-1)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x+1,y+2)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x+1,y-2)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x-1,y+2)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }
         piece = pieceAt(x-1,y-2)
         if(piece!=null) {
             if (piece.type == Type.knight && color != piece.player) {
                 return piece
             }
         }

         return null
     }
     // returns a pawn that is able to attack the king
     private  fun  kingCheckPawn(x: Int, y: Int, color: Player ): Pieces?{
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