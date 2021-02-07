
package con_4;


public class AiTurn {

    private int col;
    private int score;
    
    public AiTurn(){}

    public AiTurn(int col, int score) {
        this.col = col;
        this.score = score;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCol() {
        return this.col;
    }

    public int getScore() {
        return this.score;
    }
        
}
