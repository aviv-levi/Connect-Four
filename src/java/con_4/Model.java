
package con_4;

import java.util.Arrays;
import java.util.Random;

public class Model{ // model
    
    public enum Player{RED,YELLOW,EMPTY}
    private Player[][] board ;
    private int[] tops;
    Random rnd;
    
    public Model()
    {
        
    }
    public void startGame()
    {
        this.board = new Player[6][7];
        this.tops=new int[7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.board[i][j] = Player.EMPTY;
            }
        }
        for (int i = 0; i < 7; i++) {
            this.tops[i] =0;
        }
        rnd = new Random();
    }
    public boolean isLegal(int col)
    {
        if (col<0 && col>7) {
            return false;
        }
        if (tops[col] ==  6) {
            return false;
        }
        return true;
    }
    public int doMove(int col,Player p)
    {
        board[tops[col]][col]=p;
        tops[col]++;
        return tops[col]-1;
    }
    public void undoMove(int col)
    {
        tops[col]--;
        board[tops[col]][col]=Player.EMPTY;
    }
    public boolean CheckWin(int col, Player player) {
 
        int ROW = 6;
        int COL =7;
        for (int r = 0; r < 6; r++) { // iterate rows, bottom to top
            for (int c = 0; c < 7; c++) { // iterate columns, left to right
                if (player == Player.EMPTY)
                    continue; // don't check empty slots
                    if (player == board[r][c])
                    {
                        if (c + 3 < COL &&
                            player == board[r][c+1] && // look right
                            player == board[r][c+2] &&
                            player == board[r][c+3])
                            return true;
                        if (r + 3 < ROW) {
                            if (player == board[r+1][c] && // look up
                                player == board[r+2][c] &&
                                player == board[r+3][c])
                                return true;
                            if (c + 3 < COL &&
                                player == board[r+1][c+1] && // look up & right
                                player == board[r+2][c+2] &&
                                player == board[r+3][c+3])
                                return true;
                            if (c - 3 >= 0 &&
                                player == board[r+1][c-1] && // look up & left
                                player == board[r+2][c-2] &&
                                player == board[r+3][c-3])
                                return true;
                    }
                }
            }
        }
        return false;
    }
    
    public AiTurn startAi(Player current , boolean maximizer,int lastcol,int depth)  
    {
        Player opponent = Player.values()[1-current.ordinal()];
        boolean win = CheckWin(lastcol, opponent);
        if ( win || BoardFull() || depth == 0 ) {
            
             AiTurn t = new AiTurn();
             t.setCol(lastcol);
             if (!win || BoardFull()) {
                t.setScore(0);
            }
            else if(!maximizer)
            {
                 if (win) {
                    t.setScore(100);
                 }
                 else
                 {
                    t.setScore(-100);  
                 }
             }
             else
             {
                 if (win) {
                    t.setScore(-100);
                 }
                 else
                 {
                    t.setScore(100);  
                 }
             }
            return t;
        }
        AiTurn best = new AiTurn();
        best.setCol(-1);
        if (maximizer) {
            best.setScore(Integer.MIN_VALUE);
        }
        else{
            best.setScore(Integer.MAX_VALUE);
        }
        
        for (int i = 0; i < tops.length; i++) {
            
            if (tops[i] < 6) {
                doMove(i, current);
                AiTurn move = startAi(opponent, !maximizer, i, depth-1);
                if (maximizer) {
                    if (move.getScore() > best.getScore()) {
                        best.setCol(i);
                        //best.setRow(tops[i]);
                        best.setScore(move.getScore());
                    }
                }
                else
                {
                     if (move.getScore() < best.getScore()) {
                        best.setCol(i);
                        //best.setRow(tops[i]);
                        best.setScore(move.getScore());
                    }
                }
                undoMove(i);
            }
        }
        return best;
    }
        
        
    private boolean BoardFull()
    {
        return Arrays.stream(tops).equals(6);
    }
   
}
