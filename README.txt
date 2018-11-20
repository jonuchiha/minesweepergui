Displaying the Data: 
To start off I have a 2D array of buttons the number of which gets created is determined by
the row and columns from the current minefield object which is stored in a variable that is used to call
it's methods, so if I make a 5x5 minefield then a 5x5 button grid will be made. 
Since the game is tied to the minefield object if I ever want to make a new minefield I 
can just create a new object and the game will automatically adjust itself. 

Next I have 2 actionlisteners one for left and right click which both fetch data from the minefield object.
With the left click (stepping) the GUI is kept up to date since with every click it's constantly
recalcuating the sate of the minefield and adjusting the GUI accordingly for example if you step on a tile
that has no neighbours it will also check to see if all the surround none neighbouring tiles next to it 
have been revealed and automatically reveal them. While also checking to see if a tile has been marked 
or not since it won't change the state of a tile that has been marked. It is also constantly checking
on each click to see if the step method returns false which means a mine has been hit and the end game
scenario can begin.

Then we have the right click (marking) which also does the same, every time you go to mark a tile 
it checks the current state of the minefield and displays data accordingly for example if it realises
that a tile has already been revealed/selected it won't allow you to mark it. This fetched data from
the mark method in the minefield object to determine is something is marked/revealed or not 
as well as toggle it on and off.

Both methods keep track of the win game scenario whenever you step on something it is checking to see if 
all none-mined minetiles have been revealed as that means you won or whether the user has flagged all the mines
correctly as that also means the user has won the game.

I have also made use of a dynamic frame sizes where the frame will readjust itself depending on how big the 
the minefield grid is this ensure the frame size is suited to the grid size so all the content is displayed 
correctly and to proporsion.


Editing the Data: 
When the user alters data in the grid by clicking buttons either the step or mark method is called
depending on if the user is trying to flag or reveal a tile. The grid will then change to reflect 
what changes have happened when the method is called. A variety of validation has been added here
to best reflect the real game so ensuring the user can't step on a marked tile or mark a tile that 
has already been revealed.

When the user alters the data in the in the grid via custom grid size modification the current GUI frame is 
disposed off and a new one is generated with the choosen parameters from the user.
What happens when a user enters valid data is that a new minefield is created in the background, 
and the for loops that generates the buttons grid relies on the minefield row and col sizes so when
a new frame is generated it displays correctly with the new parameters.

However there is strict validation in place to prevent errors, if the user tries
to input more mines than the grid size they will encounter an error message 
and nothing will be executed the same follows for if they try to make a grid size
too small or too big or try to enter non numeric characters into the text fields.

Finally when game has been lost or won all buttons on the grid get disabbled which prevents
the user from being able to alter the current grid data any further. However they still have access to all the 
menu bar features as well as starting a new game again with custom parameters.


Optional Extras:
See - "Optional Features.docx".