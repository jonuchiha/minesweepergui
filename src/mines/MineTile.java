package mines;

public class MineTile
{
    private boolean mined;
    private int minedNeighbours;
    private boolean revealed;
    private boolean marked;

    public MineTile()
    {
        mined = false;
        minedNeighbours = 0;
        revealed = false;
        marked = false;
        
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public int getMinedNeighbours() {
        return minedNeighbours;
    }
    
    public void incMinedNeighbours() {
        this.minedNeighbours++;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    
    
    /**
     * This method returns the current state of a tile 
     * If the tile is marked it will return an x
     * If the tile is revealed and mined it will return an *
     * If the tile is revealed an not mined it will return the neighbour value
     * This allows us to display the MineTile's different states on the minefield visually
     * @return 
     */
    @Override
    public String toString()
    {
        String output = " ";
        
        if(marked) {
            output = "x";
        }
        
        else if(revealed) {
            if(mined) {
                output = "*";
            }
            else {
                output = "" + minedNeighbours;
            }
        }
        return output;
    }
}

