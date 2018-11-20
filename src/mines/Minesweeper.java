package mines;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.*;


public class Minesweeper extends JFrame {
    Minefield mf;
    private JToggleButton[][] grid;
    private JTextField mineText;
    private JTextField rowText;
    private JTextField colText;
    private JButton start;
    private int row;
    private int col;
    private int mines;
    private boolean check;
    private int checking;
    private final JTextField userfield = new JTextField(20);
    private final JTextField passfield = new JPasswordField(20);
    private String username;
    private String password;
    private ArrayList<String> unamepass;
    private ArrayList<String> userscore;
    private ArrayList<String> highscore;
    private final String path = "login.txt";
    private final String path2 = "score.txt";
    private JMenu menu;
    private JMenu menu2;
    private JMenu menu3;
    private JMenu menu4;
    private JMenuBar bar;
    private JMenuItem off;
    private JMenuItem display;
    private JMenuItem clear;
    private JFrame f; //minesweeper game frame
    private JFrame l; //login frame
    private JFrame s; //sign up frame
    private JFrame d; //high scores frame
    private JFrame settings; // user settings frame
    private long sTime; //Stores the current system time in milliseconds when the game starts
    private long eTime; //Stores the current system time in milliseconds when the game has been one
    private long time; //Stores the amount of seconds that pass each seconds to be displayed on the timer in game
    private Timer t;
    private JLabel title;
    ImageIcon flagimg = new ImageIcon(getClass().getClassLoader().getResource("images/flag.png")); //loads the flag image for marking
    ImageIcon mine1img = new ImageIcon(getClass().getClassLoader().getResource("images/mine1.png")); //loads the mine image 
    ImageIcon mine2img = new ImageIcon(getClass().getClassLoader().getResource("images/mine2.png")); //loads the second mine image to be displayed on the mine tile the user steps on
    ImageIcon flag = new ImageIcon(flagimg.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)); //rescales the images to fit the size of the buttons
    ImageIcon mine1 = new ImageIcon(mine1img.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
    ImageIcon mine2 = new ImageIcon(mine2img.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
    
    public static void main(String[] args) {
	Minesweeper ms = new Minesweeper(15, 10, 20);
        ms.logIn();
    }
    
    /**
     * The constructor that initialises the key fields of the program. 
     * Creates a new minefield grid with the specified parameters.
     * @param row
     * @param col
     * @param mines 
     */
    public Minesweeper(int row, int col, int mines) {
	super("Minesweeper");
        unamepass = new ArrayList<>();
        userscore = new ArrayList<>();
        highscore = new ArrayList<>();
        mf = new Minefield(row, col, mines);
        grid = new JToggleButton[row][col];
        this.row = row;
        this.col = col;
        this.mines = mines;
        check = false;
        checking = 0;
        mf.populate();
        mf.toString();
        sTime = 0;
        eTime = 0;
        time = 000;
        
    }
    
    /**
     * Starts a new minesweeper game with the specified parameters
     * @param row
     * @param col
     * @param mines 
     */
    public void newGame(int row, int col, int mines) {
        mf = new Minefield(row, col, mines);
        grid = new JToggleButton[row][col];
        this.row = row;
        this.col = col;
        this.mines = mines;
        check = false;
        checking = 0;
        System.out.println("NEW");
        mf.populate();
        mf.toString();
        sTime = 0;
        eTime = 0;
        time = 000;
        if(t != null) { //shuts down the timer thread if it's still running
            t.cancel();
        }
        createWindow();
    }
    
    /**
     * Reads in the login data from the text file
     * @throws IOException 
     */
    public void openFile() throws IOException {
        unamepass.clear();
        FileReader fr = new FileReader(path);
        File fil = new File(path);
        fil.createNewFile();
        try (BufferedReader textReader = new BufferedReader(fr)) {
            for (int i = 0; i < readLines(); i++) {
                unamepass.add(textReader.readLine());
            }
        }
        
    }
    
    /**
     * Reads in the high score data from the text file
     * @throws IOException 
     */
    public void openFile2() throws IOException {
        userscore.clear();
        highscore.clear();
        File fil = new File(path2);
        fil.createNewFile();
        FileReader fr = new FileReader(path2);
        try (BufferedReader textReader = new BufferedReader(fr)) {
            for (int i = 0; i < readLines2(); i+=2) {
                userscore.add(textReader.readLine());
                highscore.add(textReader.readLine());
            }
        }
    } 
    
    /**
     * Counts the amount of lines that are in the high score text file
     * @return number of lines in the high scores text file.
     * @throws IOException 
     */
    public int readLines2() throws IOException {   
        String aLine;
        int numberOfLines = 0;
        
        FileReader file_to_read = new FileReader(path2);
        try (BufferedReader bf = new BufferedReader(file_to_read)) {
            while ((aLine = bf.readLine()) != null) {
                numberOfLines++;
            }
        }
        
        return numberOfLines;
    }
    /**
     * Counts the amount of lines that are in the login text file 
     * @return number of lines in the login text file.
     * @throws IOException 
     */
    public int readLines() throws IOException {   
        String aLine;
        int numberOfLines = 0;
        
        FileReader file_to_read = new FileReader(path);
        try (BufferedReader bf = new BufferedReader(file_to_read)) {
            while ((aLine = bf.readLine()) != null) {
                numberOfLines++;
            }
        }
        
        return numberOfLines;
    }
    
    /**
     * This method appends to the login text file, this allows for you to write new users to the file.
     * @param u
     * @param p 
     */
    public void append(String u, String p) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            out.println(u);
            out.println(p);
        }catch (IOException e) {
            System.err.println(e);
        } 
    }
    
    /**
     * Writes the username and passwords stored in the array to a text file that can later be read from.
     */
    public void writeUser() {
        try (PrintWriter writer = new PrintWriter("login.txt", "UTF-8")) {
            for(int i = 0; i < unamepass.size(); i+=2) {
                writer.println(unamepass.get(i));
                writer.println(unamepass.get(i+1));
            }
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * This method makes use of the bubble sorting algorithm to order the the high scores
     * from the high scores text file in ascending order to be displayed in the high scores GUI.
     */
    public void sort() {  
        int size = highscore.size();  
         for(int i = 0; i < size; i++){  
            for(int j = 1; j < (size-i); j++){  
                if(Integer.valueOf(highscore.get(j-1)) > Integer.valueOf(highscore.get(j))){  

                    String temp1 = highscore.get(j-1);  
                    highscore.set(j-1, highscore.get(j));  
                    highscore.set(j, temp1);   

                    String temp2 = userscore.get(j-1);  
                    userscore.set(j-1, userscore.get(j));  
                    userscore.set(j, temp2); 
               }  
            }  
        }  
    } 
    
    /**
     * Writes the high scores stored in the array to a text file that can later be read from.
     */
    public void writeScore() {
        try (PrintWriter writer = new PrintWriter("score.txt", "UTF-8")) {
            for(int i = 0; i < userscore.size(); i++) {
                writer.println(userscore.get(i));
                writer.println(highscore.get(i));
            }
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Clears the high scores arrays and writes this to the scores text file
     * effectively clearing out all the scores stored in the text file.
     */
    public void clearScore() {
        userscore.clear();
        highscore.clear();
        writeScore();
    }
    
    /**
     * Creates the GUI and the elements within it to display the high scores.
     */
    public void displayScore() {
        d = new JFrame("Highscores");
        d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sort(); //calls the sorting method to bubble sort the data into ascending order.
        
        JPanel logtitle = new JPanel();
        Font titleFont = new Font("Impact", Font.PLAIN, 25);
        JLabel title = new JLabel(" Top Minesweeper Scores ", SwingConstants.CENTER);
        title.setFont(titleFont);
	logtitle.add(title);
        
        JPanel dis = new JPanel();
        dis.setLayout(new GridBagLayout());
        GridBagConstraints sd = new GridBagConstraints();
        Font tiles = new Font("Lucida Calligraphy", Font.BOLD, 15);
        sd.fill = GridBagConstraints.HORIZONTAL;
        sd.gridy = 0;
        sd.gridx = 0;
        JLabel un = new JLabel("Usernames", SwingConstants.CENTER);
        un.setFont(tiles);
        dis.add(un, sd);
        sd.gridx = 1;
        JLabel ts = new JLabel("     "+ "Times", SwingConstants.CENTER);
        ts.setFont(tiles);
        dis.add(ts, sd);
        
        //Loop to iterate over the arrays and display all the results within them
        for(int i = 0; i < userscore.size(); i++) {
            long score = Long.parseLong(highscore.get(i));
            sd.insets = new Insets(10,0,0,0);
            sd.gridy = i+1;
            sd.gridx = 0;
            JLabel n = new JLabel(userscore.get(i), SwingConstants.CENTER);
            dis.add(n, sd);
            sd.gridx = 1;
            long minutes = (score / 1000)  / 60; //gets the minutes representation of the milliseconds
            long seconds = (score / 1000) % 60; //gets the seconds respresentation of the milliseconds
            JLabel tim = new JLabel("     "+minutes + " mins " + seconds + " secs", SwingConstants.CENTER);
            dis.add(tim, sd);
        }
        
        d.add(logtitle, BorderLayout.NORTH);
        d.add(dis, BorderLayout.CENTER);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
        d.setResizable(false);
        
    }
    /**
     * Creates the Login GUI
     */
    public void logIn() {
        l = new JFrame("Log In");
        JLabel error;
        l.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel logtitle = new JPanel();
        Font titleFont = new Font("Lucida Calligraphy", Font.BOLD, 25);
        JLabel title = new JLabel(" Minesweeper Log In ", SwingConstants.CENTER);
        title.setFont(titleFont);
	logtitle.add(title);
        
        JPanel login = new JPanel();
        login.setLayout(new GridBagLayout());
        GridBagConstraints sd = new GridBagConstraints();
        sd.fill = GridBagConstraints.HORIZONTAL;
        sd.gridx = 0;
        sd.gridy = 1;
        login.add(new JLabel("Username: "), sd);
        sd.gridx = 1;
        sd.gridy = 1;
        login.add(userfield, sd);
        sd.gridx = 0;
        sd.gridy = 2;
        login.add(new JLabel("Password: "), sd);
        sd.gridx = 1;
        sd.gridy = 2;
        login.add(passfield, sd);
        sd.gridx = 0;
        sd.gridy = 3;
        sd.gridwidth = 2;
        error = new JLabel(" ", SwingConstants.CENTER); //reserves space for error message 
        login.add(error, sd);
        
        JPanel buts = new JPanel();
        JButton sign = new JButton("Sign In");
        sign.setFocusPainted(false);
        sign.setBackground(Color.GRAY);
        
        JButton signs = new JButton("Sign Up");
        signs.setFocusPainted(false);
        signs.setBackground(Color.GRAY);
        
        buts.add(sign);
        buts.add(signs);
        
        
        try {
            openFile(); //Loads in the login data from the login text file
            openFile2(); //Loads in the high score data from the scores text file
            for(int i = 0; i < readLines(); i++) {
                System.out.println(unamepass.get(i));
            }
        }
        
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        //Button event that checks to see if the user account exists
        ActionListener res = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            username = userfield.getText();
            password  = passfield.getText();
            error.setText(" ");
            loop:
            //Looks to see if the username and password entered into the fields match any in the text file
            for(int i = 0; i < unamepass.size()-1; i++) {
                if(username.equals(unamepass.get(i)) && password.equals((unamepass.get(i+1)))) {
                    error.setForeground(Color.green);
                    error.setText("Log In Sucessful");
                    l.dispose();
                    createWindow();
                    break;
                }
            }
            error.setForeground(Color.red);
            error.setText("Incorrect Log In");
        };
        
        //Button event that calls the signUp() method for the user to create an account
        ActionListener ress = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            l.dispose();
            signUp();
        };
        
        sign.addActionListener(res);
        signs.addActionListener(ress);
       
        l.add(logtitle, BorderLayout.NORTH);
        l.add(login, BorderLayout.CENTER);
        l.add(buts, BorderLayout.SOUTH);
        l.pack();
        l.setLocationRelativeTo(null);
        l.setVisible(true);
        l.setResizable(false);
        
    }
    
    /**
     * Creates the Sign Up GUI
     */
    public void signUp() {
        s = new JFrame("Sign Up");
        JLabel error;
        s.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel logtitle = new JPanel();
        Font titleFont = new Font("Lucida Calligraphy", Font.BOLD, 25);
        JLabel title = new JLabel(" Minesweeper Sign Up ", SwingConstants.CENTER);
        title.setFont(titleFont);
	logtitle.add(title);
        
        JPanel login = new JPanel();
        login.setLayout(new GridBagLayout());
        GridBagConstraints sd = new GridBagConstraints();
        sd.fill = GridBagConstraints.HORIZONTAL;
        sd.gridx = 0;
        sd.gridy = 1;
        login.add(new JLabel("Username: "), sd);
        sd.gridx = 1;
        sd.gridy = 1;
        login.add(userfield, sd);
        sd.gridx = 0;
        sd.gridy = 2;
        login.add(new JLabel("Password: "), sd);
        sd.gridx = 1;
        sd.gridy = 2;
        login.add(passfield, sd);
        sd.gridx = 0;
        sd.gridy = 3;
        sd.gridwidth = 2;
        error = new JLabel(" ", SwingConstants.CENTER);
        login.add(error, sd);
        
        JPanel buts = new JPanel();
        JButton sign = new JButton("Register");
        sign.setFocusPainted(false);
        sign.setBackground(Color.GRAY);
        
        JButton signs = new JButton("Sign In");
        signs.setFocusPainted(false);
        signs.setBackground(Color.GRAY);
        
        buts.add(sign);
        buts.add(signs);
        
        //Button event
        ActionListener res = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            boolean valid = true;
            String u = userfield.getText().trim(); //Gets the username from field and removes any whitespace before and after the text
            String p  = passfield.getText().trim(); //Gets the password from field and removes any whitespace before and after the text
            error.setText(" ");
            //Checks to see if the username exists since usernames must be unique
            for(int i = 0; i < unamepass.size(); i+=2) {
                if(u.equals(unamepass.get(i))) {
                    error.setForeground(Color.red);
                    error.setText("Username Already Exists");
                    valid = false;
                }
            }
            //Checks to see if there is any text in both fields so you can't input blank data
            if(!(u.length() > 0) || !(p.length() > 0)) {
                error.setForeground(Color.red);
                error.setText("Username/Password Can't Be Blank");
            }
            //Adds data to the text file if it sucessfully passes the above validation
            if(valid && u.length() > 0 && p.length() > 0) {
                append(u, p);
                error.setForeground(Color.black);
                error.setText("Register Sucessfull!!");
            }
        };
        
        //Button event that closes the sign up screen and reopens the login screen
        ActionListener ress = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            s.dispose();
            logIn();
        };
        sign.addActionListener(res);
        signs.addActionListener(ress);
       
        s.add(logtitle, BorderLayout.NORTH);
        s.add(login, BorderLayout.CENTER);
        s.add(buts, BorderLayout.SOUTH);
        s.pack();
        s.setLocationRelativeTo(null);
        s.setVisible(true);
        s.setResizable(false);
        
    }
    
    /**
     * Creates the Settings GUI
     */
    public void setting() {
        settings = new JFrame("Settings");
        JLabel error;
        settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel logtitle = new JPanel();
        Font titleFont = new Font("Lucida Calligraphy", Font.BOLD, 25);
        JLabel title = new JLabel(" User Settings ", SwingConstants.CENTER);
        title.setFont(titleFont);
	logtitle.add(title);
        
        JPanel login = new JPanel();
        login.setLayout(new GridBagLayout());
        GridBagConstraints sd = new GridBagConstraints();
        sd.fill = GridBagConstraints.HORIZONTAL;
        sd.gridx = 0;
        sd.gridy = 1;
        login.add(new JLabel("Change Username: "), sd);
        sd.gridx = 1;
        sd.gridy = 1;
        login.add(userfield, sd);
        sd.gridx = 0;
        sd.gridy = 2;
        login.add(new JLabel("Change Password: "), sd);
        sd.gridx = 1;
        sd.gridy = 2;
        login.add(passfield, sd);
        sd.gridx = 0;
        sd.gridy = 3;
        sd.gridwidth = 2;
        error = new JLabel("Leave username/password field blank to change only one", SwingConstants.CENTER); //reserves space for error message 
        login.add(error, sd);
        
        JPanel buts = new JPanel();
        JButton sign = new JButton("Update");
        sign.setFocusPainted(false);
        sign.setBackground(Color.GRAY);
        
        buts.add(sign);

        //Button event that checks to see if the user account exists
        ActionListener res = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            error.setText("Leave username/password field blank to change only one");
            boolean valid = true;
            String u = userfield.getText().trim(); //Gets the username from field and removes any whitespace before and after the text
            String p  = passfield.getText().trim(); //Gets the password from field and removes any whitespace before and after the text
            //Checks to see if the username exists since usernames must be unique
            for(int i = 0; i < unamepass.size(); i+=2) {
                if(u.equals(unamepass.get(i)) && !u.equals(username)) {
                    error.setForeground(Color.red);
                    error.setText("Username Already Exists");
                    valid = false;
                }
            }
            //Checks to see if there is any text in both fields so you can't input blank data
            if(!(u.length() > 0) && !(p.length() > 0)) {
                error.setForeground(Color.red);
                error.setText("Username/Password Can't Be Blank");
            }
            
            //Adds data to the text file if it sucessfully passes the above validation
            if(valid && u.length() > 0 && p.length() > 0) {
                for(int i = 0; i < unamepass.size(); i+=2) {
                    if(username.equals(unamepass.get(i))) {
                        unamepass.set(i, u);
                        unamepass.set(i+1, p);
                    }
                }
                //updates the username in the highscores with the new username
                for(int i = 0; i < userscore.size(); i++) {
                    if(username.equals(userscore.get(i))) {
                        userscore.set(i, u);
                        writeScore();
                        try {
                            openFile2();
                        } catch (IOException ex) {
                            Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                username = u;
                password = p;
                menu.setText("Welcome " + username + "!"); //updates the current username display with the new one
                error.setForeground(Color.black);
                error.setText("Update Sucessfull!!");
                writeUser();
            }
            
            //Adds data to the text file if it sucessfully passes the above validation, checks if only password field is blank
            if(valid && u.length() > 0 && !(p.length() > 0)) {
                for(int i = 0; i < unamepass.size(); i+=2) {
                    if(username.equals(unamepass.get(i))) {
                        unamepass.set(i, u);
                    }
                }
                
                //updates the username in the highscores with the new username
                for(int i = 0; i < userscore.size(); i++) {
                    if(username.equals(userscore.get(i))) {
                        userscore.set(i, u);
                        writeScore();
                        try {
                            openFile2();
                        } catch (IOException ex) {
                            Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                username = u;
                menu.setText("Welcome " + username + "!"); //updates the current username display with the new one
                error.setForeground(Color.black);
                error.setText("Update Sucessfull!!");
                writeUser();
            }
            
            //Adds data to the text file if it sucessfully passes the above validation, checks if only username field is blank
            if(valid && p.length() > 0 && !(u.length() > 0)) {
                for(int i = 0; i < unamepass.size(); i+=2) {
                    if(username.equals(unamepass.get(i))) {
                        unamepass.set(i+1, p);
                    }
                }
                password = p;
                error.setForeground(Color.black);
                error.setText("Update Sucessfull!!");
                writeUser();
            }
            
        };
        
        sign.addActionListener(res);
       
        settings.add(logtitle, BorderLayout.NORTH);
        settings.add(login, BorderLayout.CENTER);
        settings.add(buts, BorderLayout.SOUTH);
        settings.pack();
        settings.setLocationRelativeTo(null);
        settings.setVisible(true);
        settings.setResizable(false);
        
    }
    
    /**
     * Creates the main GUI frame for the minesweeper game
     */
    public void createWindow() {
	JLabel width, height, mine;
	JPanel buttonPanel, scorePanel, titlePanel;
        int adj = 30;
        time = 000;
        f = new JFrame("Minesweeper");
        
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(t != null) { //closes the timer thread when the game windows closes to stop it running in the background after the game is closes
                    t.cancel();
                }
            }
        });
        
        //Allows for a dynamic frame size, the minesweeper frame will readjust itself automatically depending on the size of the minesweeper grid
        Container con = f.getContentPane();
        int len;
        Dimension dim;
        if (col < 20) {
            dim = new Dimension((25*20)+adj, (row*25)+80);
            len = (25*20)+80;
        }
        else {
            dim = new Dimension((col*25)+adj, (row*25)+80);
            len = (col*20)+80;
        }
        con.setPreferredSize(dim);
        
        menu = new JMenu("Welcome " + username + "!");
        JMenuItem set = new JMenuItem("Settings");
        off = new JMenuItem("Sign Out");
        menu2 = new JMenu("Highscores");
        display = new JMenuItem("Display Scores");
        clear = new JMenuItem("Clear Scores");
        menu3 = new JMenu("Difficulties");
        JMenuItem low = new JMenuItem("Low");
        JMenuItem med = new JMenuItem("Medium");
        JMenuItem high = new JMenuItem("High");
        menu4 = new JMenu("Timer: " +time+ " seconds");
        bar = new JMenuBar();
        menu.add(set);
        menu.add(off);
        menu2.add(display);
        menu2.add(clear);
        menu3.add(low);
        menu3.add(med);
        menu3.add(high);
        bar.add(menu);
        bar.add(menu3);
        bar.add(menu2);
        bar.add(menu4);
        f.setJMenuBar(bar);
        
        //Signs the user out of the game and sends them back to the login screen
        off.addActionListener((ActionEvent ev) -> {
            f.dispose();
            userfield.setText("");
            passfield.setText("");
            logIn();
        });
        
        //Displays the high socres
        display.addActionListener((ActionEvent ev) -> {
            if(this.d != null) { //checks to see if there is alread a fram displaying the scores, if so closes it and open a new one, prevents multiple displays poping up for the same thing
                this.d.dispose();
                displayScore();
            }
            else {
                displayScore();
            }
        });
        
        //Displays the user settings
        set.addActionListener((ActionEvent ev) -> {
            if(this.settings != null) { //checks to see if there is alread a fram displaying the scores, if so closes it and open a new one, prevents multiple displays poping up for the same thing
                this.settings.dispose();
                setting();
            }
            else {
                setting();
            }
        });
        
        //clears the high scores
        clear.addActionListener((ActionEvent ev) -> {
            clearScore();
        });
        
        //starts new game with low difficulty
        low.addActionListener((ActionEvent ev) -> {
            f.dispose();
            newGame(20, 20, 15);
        });
        
        //starts new game with med difficulty
        med.addActionListener((ActionEvent ev) -> {
            f.dispose();
            newGame(20, 20, 30);
        });
        
        //starts new game with high difficulty
        high.addActionListener((ActionEvent ev) -> {
            f.dispose();
            newGame(20, 20, 60);
        });
        
        
        titlePanel = new JPanel();
        Font titleFont = new Font("Lucida Calligraphy", Font.BOLD, 25);
        titlePanel.setLayout(new GridBagLayout());
        GridBagConstraints sr = new GridBagConstraints();
        sr.fill = GridBagConstraints.HORIZONTAL;
        title = new JLabel(" Minesweeper ", SwingConstants.CENTER);
        title.setFont(titleFont);
        sr.gridy = 1;
        sr.weightx = 0;
        sr.weighty = 1.0;
	titlePanel.add(title, sr);
        title.setPreferredSize(new Dimension(len, 40));
        title.setBorder(new LineBorder(Color.GRAY, 1, false));
        titlePanel.setBackground(Color.GRAY);
        f.add(titlePanel, BorderLayout.NORTH);
        
        
        scorePanel = new JPanel();
        GridBagConstraints sp = new GridBagConstraints();
        sp.fill = GridBagConstraints.HORIZONTAL;
        sp.insets = new Insets(0,0,0,10);
        scorePanel.setLayout(new GridBagLayout());
	scorePanel.setBorder(new LineBorder(Color.GRAY, 2, false));
        
	mine = new JLabel("Mines:  ");
	scorePanel.add(mine);
	mineText = new JTextField("" + mines);
	mineText.setEditable(true);
        mineText.setPreferredSize(new Dimension(30, 20));
        mineText.setBorder(new LineBorder(Color.GRAY, 1, false));
	scorePanel.add(mineText, sp);
        
        height = new JLabel(" Rows:  ");
        scorePanel.add(height);
        colText = new JTextField("" + row);
	colText.setEditable(true);
        colText.setPreferredSize(new Dimension(30, 20));
        colText.setBorder(new LineBorder(Color.GRAY, 1, false));
	scorePanel.add(colText, sp);
        
        width = new JLabel(" Cols:  ");
        scorePanel.add(width);
        rowText = new JTextField("" + col);
	rowText.setEditable(true);
        rowText.setPreferredSize(new Dimension(30, 20));
        rowText.setBorder(new LineBorder(Color.GRAY, 1, false));
	scorePanel.add(rowText, sp);
        
        start = new JButton("New Game");
        start.setMargin(new Insets(0, 0, 0, 0));
        start.setFocusPainted(false);
        start.setBackground(Color.LIGHT_GRAY);
        scorePanel.add(start);
        scorePanel.setBackground(Color.GRAY);
        
        JLabel customerror = new JLabel(" Edit for Custom Grid Size  ", SwingConstants.CENTER);
        customerror.setForeground(Color.WHITE);
        sp.gridy = 1;
        sp.gridwidth = 8;
        scorePanel.add(customerror, sp);
        f.add(scorePanel, BorderLayout.SOUTH);
        


	buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gb = new GridBagConstraints();
        buttonPanel.setPreferredSize(new Dimension(col*25+50,row*25+50));
	buttonPanel.setBorder(new LineBorder(new Color(152, 152, 152), 0, true));
        Font myFont = new Font("Serif", Font.BOLD, 12);
        
        //Generates and displays the 2D button array for the minesweeper grid
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                int p = i;
                int m = j;
                grid[i][j] = new JToggleButton("");
                grid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                grid[i][j].setOpaque(true);
                grid[i][j].setBackground(Color.DARK_GRAY);
                grid[i][j].setBorder(new LineBorder(new Color(152, 152, 152), 1, false));
                grid[i][j].setPreferredSize(new Dimension((25), (25)));
                grid[i][j].setMargin(new Insets(0, 0, 0, 0));
                grid[i][j].setFont(myFont);
                grid[i][j].setFocusPainted(false);
                UIManager.put("ToggleButton.select", Color.LIGHT_GRAY);
                SwingUtilities.updateComponentTreeUI(grid[i][j]);
                
                //right click button events
                ActionListener actionListener = (ActionEvent actionEvent) -> {
                    int r = p;
                    int c = m;
                    JToggleButton test = (JToggleButton) actionEvent.getSource();
                    boolean selected = test.getModel().isSelected();
                    if(sTime == 0) { //starts the timer as soon as the user steps on a tile, if it has already started then ignore this
                        sTime = System.currentTimeMillis();
                        t = new Timer();
                        t.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                              time = time+1;
                              menu4.setText("Timer: " +time+" seconds");

                            }
                        }, 1000,1000);
                    }
                    if(!selected) { //If the user steps on a tile it keeps it selected
                        test.setSelected(!test.isSelected());
                    }
                    
                    //If there is a revealed tile that hasn't been selected because it was marked, once it has been unmarked and selected, reveal the tile
                    if (checking > 0) {
                        if(mf.mt[r][c].isRevealed()) {
                            grid[r][c].setSelected(true);

                            if(mf.mt[r][c].getMinedNeighbours() == 0) {
                               display(r,c,0);
                            }

                            else {
                                display(r,c,1);
                            }
                        }
                        checking--;
                    }
                    
                    //ensures that you can't step on a tile that is marked or already revealed
                    if(!mf.mt[r][c].isMarked() && !mf.mt[r][c].isRevealed()) {
                        if(mf.step(r, c)) {
                            for(int x = 0; x < row; x++) {
                                for(int y = 0; y < col; y++) {
                                    if(mf.mt[x][y].isMarked()) {
                                        grid[x][y].setIcon(flag);
                                    }
                                    
                                    else {
                                        if(mf.mt[x][y].isRevealed()) {
                                            grid[x][y].setSelected(true);

                                            if(mf.mt[x][y].getMinedNeighbours() == 0) {
                                               display(x,y,0);
                                            }

                                            else {
                                                display(x,y,1);
                                            }
                                        }
                                    }
                                }
                            }
                            f.repaint();
                        }
                        //If the step method returns false that means the user has stepped on a mine so the lose game senario is executed
                        else {  
                            for(int x = 0; x < row; x++) {
                                for(int y = 0; y < col; y++) {
                                    if(mf.mt[x][y].isMarked() && !mf.mt[x][y].isMined()) {
                                        grid[x][y].setSelected(true);
                                        if(mf.mt[x][y].getMinedNeighbours() == 0) {
                                               display(x,y,0);
                                            }

                                        else {
                                            display(x,y,1);
                                        }
                                    }
                                    
                                    if(!grid[x][y].isSelected()) {
                                        grid[x][y].setEnabled(false);
                                    }
                                    
                                    if(mf.mt[x][y].isMined() && !mf.mt[x][y].isMarked()) {
                                        grid[x][y].setSelected(true);
                                        grid[x][y].setIcon(mine2);
                                        grid[x][y].setDisabledIcon(mine1);
                                    }
                                    
                                    if(mf.mt[x][y].isMined() && mf.mt[x][y].isMarked()) {
                                        grid[x][y].setDisabledIcon(flag);
                                    }
                                }
                            }
                            f.repaint();
                            title.setText("Minesweeper: Game Over!! ");
                            t.cancel();
                            check = true;
                            
                        }
                    
                    }
                   //If a tile is marked then leave it marked                     
                    else if(mf.mt[r][c].isMarked()) {
                        grid[r][c].setIcon(flag);
                        grid[r][c].setSelected(false);       
                    }
                    
                    //Win game senario
                    winner();
                };
                
                //Left click button events
                grid[i][j].addMouseListener(new MouseAdapter() {     
                    @Override
                    public void mousePressed(MouseEvent me) { 
                        int r = p;
                        int c = m;
                        if(sTime == 0) { //starts the timer as soon as the user marks a tile, if it has already started then ignore this
                            sTime = System.currentTimeMillis();
                            t = new Timer();
                            t.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {//Updates the timer by 1 every second on the GUI
                                  time = time+1;
                                  menu4.setText("Timer: " +time+" seconds");

                                }
                            }, 1000,1000);
                        }
                        if (SwingUtilities.isRightMouseButton(me) && !check) {
                            if(!mf.mt[r][c].isRevealed() || !grid[r][c].isSelected()) {
                                mf.mark(r, c);
                                if(mf.mt[r][c].isMarked() && !grid[r][c].isSelected()) {
                                    grid[r][c].setIcon(flag);
                                }

                                else if (!grid[r][c].isSelected() && mf.mt[r][c].isRevealed()) {
                                    grid[r][c].setIcon(null);
                                    checking++; //Checks to see any revealed tiles have been marked
 
                                }

                                else if (grid[r][c].isSelected()) {
                                    if(mf.mt[r][c].getMinedNeighbours() == 0) {
                                        display(r,c,0);
                                     }

                                    else {
                                        display(r,c,1);
                                    }
                                }

                                else {
                                    grid[r][c].setIcon(null);
                                }
                            }
                            
                            //Win game senario
                            winner();
                        }
                    } 
                });
                
                grid[i][j].addActionListener(actionListener);
                
                gb.fill = GridBagConstraints.VERTICAL;
                gb.gridx = j;
                gb.gridy = i;
                
                buttonPanel.add(grid[i][j], gb);
            }
        }
        
        f.add(buttonPanel, BorderLayout.CENTER);
        
        //Button event for the new game button which has some validation in place
        ActionListener res = (ActionEvent actionEvent) -> {
            JButton test = (JButton) actionEvent.getSource();
            boolean number = true; //check to see if user has made a character or out of bounds error
            int r = 10;
            int c = 15;
            int m = 20;
            
            if (colText.getText().trim().matches("[0-9]+")) { //checks to make sure the input are numbers only
                r = Integer.parseInt(colText.getText().trim());
            } 
            else { 
                colText.setBorder(new LineBorder(Color.RED, 1, true)); 
                number = false;
                customerror.setText(" Only 0-9 numbers are accepted ");
            }
            
            if (rowText.getText().trim().matches("[0-9]+")) {
                c = Integer.parseInt(rowText.getText().trim());
            } 
            else { 
                colText.setBorder(new LineBorder(Color.RED, 1, true)); 
                number = false;
                customerror.setText(" Only 0-9 numbers are accepted ");
            }
            
            if (mineText.getText().trim().matches("[0-9]+")) {
                m = Integer.parseInt(mineText.getText().trim());
            } 
            else {
                colText.setBorder(new LineBorder(Color.RED, 1, true)); 
                number = false;
                customerror.setText(" Only 0-9 numbers are accepted ");
            }
            
            if(number) {
                if (m >= (r * c)) { //Checks to make sure that the mines can't exceed grid size
                    mineText.setBorder(new LineBorder(Color.RED, 1, true));
                    customerror.setText(" Mines can't exceed grid size ");
                }

                else if (r > 50 || r < 3) { //Checks to see that row can't be too big or too small
                    colText.setBorder(new LineBorder(Color.RED, 1, true));
                    customerror.setText(" Rows must be between 3 and 50 ");
                }

                else if (c > 50 || c < 3) { //Checks to see that col can't be too big or too small
                    rowText.setBorder(new LineBorder(Color.RED, 1, true));
                    customerror.setText(" Columns must be between 3 and 50 ");
                }

                else { //If all validation is passed start a new game with specified parameters
                    f.dispose();
                    newGame(r, c, m);
                }
            }
            
        };
        start.addActionListener(res);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(false);
    }
    
    /**
     * Checks the value of the mined neighbours to apply the relevant text colour to them to emulate the colour scheme from the real game
     * @param x
     * @param y
     * @param check 
     */
    private void display(int x, int y, int check) {
        if (check == 0) {
            grid[x][y].setText(""); 
        }
        
        else {
            switch(mf.mt[x][y].getMinedNeighbours()) {
                case 1: 
                    grid[x][y].setForeground(Color.BLUE);
                    break;
                case 2: 
                    grid[x][y].setForeground(new Color(0,100,0));
                    break;
                case 3: 
                    grid[x][y].setForeground(Color.RED);
                    break;
                case 4: 
                    grid[x][y].setForeground(new Color(75,0,130));
                    break;
                case 5: 
                    grid[x][y].setForeground(new Color(128,0,0));
                    break;
                case 6: 
                    grid[x][y].setForeground(new Color(64,224,208));
                    break;
                case 7: 
                    grid[x][y].setForeground(Color.BLACK);
                    break;
                case 8: 
                    grid[x][y].setForeground(Color.LIGHT_GRAY);
                    break;
            }
            grid[x][y].setText("" + mf.mt[x][y].getMinedNeighbours());
        }
    }
    
    /**
     * Checks to see if the user has won the game or not if so it will execute the winner scenario
     */
    private void winner() {
        //Win game senario
        if(mf.areAllMinesRevealed()) {
            title.setText("Minesweeper: Winner!! ");
            t.cancel();
            eTime = System.currentTimeMillis(); //records the current time of the system when the game ends
            long finish = eTime - sTime; //subtracts the end time and starting time to get the duration of the game
            for(int x = 0; x < row; x++) {
                for(int y = 0; y < col; y++) {

                    if(!grid[x][y].isSelected() && mf.mt[x][y].isMined()) {
                        grid[x][y].setIcon(flag);
                    }

                    if(!grid[x][y].isSelected()) {
                        grid[x][y].setDisabledIcon(flag);
                        grid[x][y].setEnabled(false); //disabbles all tiles on the grid so the user can't input anymore commands on the grid
                    }

                    if(mf.mt[x][y].isMarked()) {
                        grid[x][y].setDisabledIcon(flag);
                    }
                }
            }
            f.repaint();
            boolean exist = false;
            //Checks to see if the username exists and if it their new time is better than their old time
            for(int k = 0; k < userscore.size(); k++) {
                if(username.equals(userscore.get(k)) && Long.parseLong(highscore.get(k)) > finish) {
                    highscore.set(k, ""+finish);
                    exist = true;
                }

                else if(username.equals(userscore.get(k))) {
                    exist = true;
                }
            }
            //if a user doesn't exist in the highscores add their name and score
            if(!exist) {
                userscore.add(username);
                highscore.add(""+finish);
            }
            writeScore(); //updates the high scores txt file with new data
            check = true;
        }
    }
}
