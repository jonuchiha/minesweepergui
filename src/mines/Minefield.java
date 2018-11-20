package mines;
import java.util.Random;

public class Minefield {
    MineTile[][] mt;
    private int noofmines;
    private int maxmines;
    private int maxrow;
    private int maxcol;
    private Random rand;
    
    Minefield(int maxrow, int maxcol, int maxmines) {
        mt = new MineTile[maxrow][maxcol];
        for(int i = 0; i < maxrow; i++){
            for(int j = 0; j < maxcol; j++){ 
                mt[i][j] = new MineTile();
            }
        }
        noofmines = 0;
        rand = new Random();
        this.maxrow = maxrow;
        this.maxcol = maxcol;
        this.maxmines = maxmines;
    }
    
    /**
     * Mark a tile - toggle between if it is marked or not
     * @param row
     * @param col 
     */
    public void mark(int row, int col) {
      
        if (row < maxrow && col < maxcol && row > -1 && col > -1) {
            if(mt[row][col].isMarked()) {
                mt[row][col].setMarked(false);
            }
            else {
                mt[row][col].setMarked(true);
            }
        }
    }
    
    /**
     * Recursive function that reveals all neighbouring 0's tiles as well and stops when it reaches a value greater than 0
     * @param row
     * @param col 
     */
    public void revealTile(int row, int col) {
        if (mt[row][col].getMinedNeighbours() > 0 && !mt[row][col].isRevealed()) {
            mt[row][col].setRevealed(true);
        }
        else if (mt[row][col].getMinedNeighbours() == 0 && !mt[row][col].isRevealed()) {
            mt[row][col].setRevealed(true);
            step(row, col);
        }
    }
    /**
     * Checks to see if the chosen title is a 0 or greater than 0
     * If the tile is greater than 0 only that tile is revealed
     * However it the tile is 0 then it is revealed and revealTile() is called to reveal all the neighbouring 0's tiles as well
     * @param row
     * @param col
     * @return 
     */
    public boolean step(int row, int col) {
        if(mt[row][col].isMined()) {
            return false;
        }
        
        else {
            if (mt[row][col].getMinedNeighbours() > 0){
                mt[row][col].setRevealed(true);
            }
            
            else if (mt[row][col].getMinedNeighbours() == 0) {
                mt[row][col].setRevealed(true);
                
                if(row < maxrow-1) { revealTile(row+1, col); }//right

                if(row > 0) { revealTile(row-1, col); }//left

                if(col < maxcol-1) { revealTile(row, col+1); }//top middle
                
                if(col > 0) { revealTile(row, col-1); }//bottom middle

                if(row > 0 && col > 0) { revealTile(row-1, col-1); }//top left

                if(row > 0 && col < maxcol-1) { revealTile(row-1, col+1); }//top right

                if(row < maxrow-1 && col > 0) { revealTile(row+1, col-1); }//bottom left

                if(row < maxrow-1 && col < maxcol-1) { revealTile(row+1, col+1); }//bottom right
            } 
            return true;
        }
    }
    
    /**
     * Checks to see if all the the mines have been correctly marked
     * A second check is used to see if any tiles have been marked incorrectly
     * When only the correct tiles have all been marked will the method return true
     * @return 
     */
    public boolean areAllMinesRevealed() {
        int count = 0;
        int count2 = 0;
        int count3 = 0;
        for(int i = 0; i < maxrow; i++){
            for(int j = 0; j < maxcol; j++){
                    
                if(mt[i][j].isMarked() && mt[i][j].isMined()) {
                    count++;
                }
                
                else if (mt[i][j].isMarked() && !mt[i][j].isMined()) {
                    count2++;
                }
                
                if(mt[i][j].isRevealed() && !mt[i][j].isMarked()) {
                    count3++;
                }
            }
        }
        return count3 == (maxrow*maxcol)-maxmines || count == maxmines && count2 == 0;
    }
    /**
     * This method allows you to place a mine on a tile
     * If the tile is already mined then it will ignore the request
     * However if the time has not been mined then it will mine it and increment the number of mines in the game
     * When a mine has been place it will then also increment the value of its 8 surrounding neighbours by 1 
     * This allows for the correct number displayed on the neighbouring tiles each time the function is called.
     * @param row
     * @param col
     * @return 
     */
    public boolean mineTile(int row, int col) {
        
        if(!mt[row][col].isMined() && noofmines < maxmines) {
            mt[row][col].setMined(true);
            noofmines++;
                        
            if(row < maxrow-1 && !mt[row+1][col].isMined()) {
                mt[row+1][col].incMinedNeighbours();
            }//right

            if(row > 0 && !mt[row-1][col].isMined()) {
                mt[row-1][col].incMinedNeighbours();
            }//left

            if(col < maxcol-1 && !mt[row][col+1].isMined()) {
                mt[row][col+1].incMinedNeighbours();
            }//top middle
            
            if(col > 0 && !mt[row][col-1].isMined()) {
                mt[row][col-1].incMinedNeighbours();
            }//bottom middle

            if(row > 0 && col > 0 && !mt[row-1][col-1].isMined()) {
                mt[row-1][col-1].incMinedNeighbours();
            }//top left

            if(row > 0 && col < maxcol-1 && !mt[row-1][col+1].isMined()) {
                mt[row-1][col+1].incMinedNeighbours();
            }//top right

            if(row < maxrow-1 && col > 0 && !mt[row+1][col-1].isMined()) {
                mt[row+1][col-1].incMinedNeighbours();
            }//bottom left

            if(row < maxrow-1 && col < maxcol-1 && !mt[row+1][col+1].isMined()) {
                mt[row+1][col+1].incMinedNeighbours();
            }//bottom right    
            
            return true;
        }
        
        else {
            return false;
        }
        
    } 
    
    /**
     * This method generates random row and column coordinates and then passes them off into the mineTile method as parameters to place a mine
     * There is also an if statement to check the number of mines is within the size of the grid to avoid an endless loop
     * The way random is set up is that it will only generate numbers from 0 till size of the row/col that ensures that every random number
     * generated is within the acceptable array range
     * This is done in a while loop that keeps repeating until the maximum amount of mines has been entered
     * Some validation is used here, firstly to make sure that the coordinate (0,0) is never mined
     * And secondly to check that mineTile returns true before it increments i in the loop this ensures that a unique tile has been mined
     * As well as ensuring all of the max number of mines are placed on the grid
     */
    public void populate() {
        int randrow;
        int randcol;
        int k = 0;
        
        while(k < maxmines) {
            randrow = rand.nextInt(maxrow);
            randcol = rand.nextInt(maxcol); 
            if(randrow != 0 || randcol != 0){
                if (mineTile(randrow, randcol)) {
                    k++;
                }
            }
        }
        
    }
    
    /**
     * This method stores and displays the output of the complete minefield grid
     * If a tile is mined it will print an asterisk in it's place to indicate this
     * However it is not mined it will display a number dictating the number of mines in its surrounding 9 titles 
     * Some formatting has been displayed to make the grid display better as well as displaying the format original asked for in the brief under it.
     * @return 
     */
    @Override
    public String toString() {
        String output = "";
        
        for(int i = 0; i < maxrow; i++){
            System.out.print(" | ");
            for (int j = 0; j < maxcol; j++) {
              
                if(mt[i][j].isMined()) {
                    System.out.print("*" + " | ");
                    output = output + "*";
                }
                
                if(!mt[i][j].isMined()){
                    
                    System.out.print( mt[i][j].getMinedNeighbours() + " | ");
                    output = output + mt[i][j].getMinedNeighbours();
                }
                
            }
            System.out.println();
            output = output + "\n";
        }
        return output;
    }
    
    
    public MineTile[][] getMt() {
        return mt;
    }

    public int getMaxrow() {
        return maxrow;
    }

    public int getMaxcol() {
        return maxcol;
    }

    public int getNoofmines() {
        return noofmines;
    }

    public int getMaxmines() {
        return maxmines;
    }
    
    
    
}
