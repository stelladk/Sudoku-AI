import java.security.SecureRandom;
import java.util.*;

public class Sudoku implements Comparable<Sudoku>{

    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String BOLD = "\033[0;1m";

    private final int Dimention = 9;
    private final int valueOfBoard = 45;
    private final int MAX_FITNESS = 50;
    private Cell[][] board;
    private int[][] conflicts;
    private int fitness;

    //Empty Board Constructor
    public Sudoku(){
        this.board = new Cell[Dimention][Dimention];
        this.conflicts = new int[Dimention][Dimention];
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                this.conflicts[i][j] = 0;
            }
        }
        initialise();
    }

    //Fixed Board Constructor
    public Sudoku(int[][] sud){
        this.board = new Cell[Dimention][Dimention];
        this.conflicts = new int[Dimention][Dimention];
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                this.conflicts[i][j] = 0;
                this.board[i][j] = new Cell();
                if(sud[i][j] != 0){
                    this.board[i][j].add(sud[i][j]);
                    this.board[i][j].makeFixed();
                }
            }
        }
        initialise();
    }

    //Copy Constructor
    public Sudoku(Sudoku b){
        this.board = new Cell[Dimention][Dimention];
        this.conflicts = new int[Dimention][Dimention];
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                this.board[i][j] = new Cell(b.board[i][j]);
                this.conflicts[i][j] = 0;
            }
        }
    }

    //Crossover Constructor
    public Sudoku(Sudoku a, Sudoku b){
        SecureRandom random = new SecureRandom();
        int p;
        this.board = new Cell[Dimention][Dimention];
        this.conflicts = new int[Dimention][Dimention];
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                this.conflicts[i][j] = 0;
                this.board[i][j] = new Cell(a.board[i][j], b.board[i][j]);

            }
        }
    }

    //Initialise board with all possible values for each cell
    public void initialise(){
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                if(!this.board[i][j].isFixed()){
                    for(int v = 1; v <= Dimention; ++v){
                        if(checkCol(j, v) && checkRow(i, v) && checkSquare(i, j, v)){
                            this.board[i][j].add(v);
                        }
                    }
                    this.board[i][j].setRandom();
                }
            }
        }
        calculateFitness();
    }

    public void calculateFitness(){
        findConflicts();
        int sum = MAX_FITNESS;
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                sum -= conflicts[i][j];
            }
        }
        this.fitness = sum;
    }

    //TODO: replace with functions
    private void findConflicts(){
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                //if the number is fixed it has zero conflicts
                if(this.board[i][j].isFixed()){
                    this.conflicts[i][j] = 0;
                    continue;
                }
                //check row for conflicts
                for(int col = 0; col < Dimention; ++col){
                    if(col == j) continue;
                    if(this.board[i][j].dominant() == this.board[i][col].dominant() && this.board[i][col].isFixed()){
                        this.conflicts[i][j]++;
                    }
                }
                //check column for conflicts
                for(int row = 0; row < Dimention; ++row){
                    if(row == i) continue;
                    if(this.board[i][j].dominant() == this.board[row][j].dominant() && this.board[row][j].isFixed()){
                        this.conflicts[i][j]++;
                    }
                }
                //check square for conflicts
                int root = (int)(Math.sqrt(Dimention));
                int row = i / root;
                int col = j / root;
                for(int k = row*root; k < (row+1)*root; ++k){
                    for(int l = col*root; l < (col+1)*root; ++l){
                        if(k == i || l == j) continue;
                        if(this.board[i][j].dominant() == this.board[k][l].dominant() && this.board[k][l].isFixed()){
                            this.conflicts[i][j]++;
                        }
                    }
                }
            }
        }
    }

    //TODO: delete or fix
    public ArrayList<Integer> getChildren(int x, int y){
        ArrayList<Integer> children = new ArrayList<Integer>();
        int child;
        if(!board[x][y].isFixed()){
            for(int num = 1; num <= Dimention; ++num){
                if(checkRow(x, num) && checkCol(y, num) && checkSquare(x, y, num)){
                    children.add(num);
                }
            }
        }
        return children;
    }

    private boolean checkRow(int x, int number){ 
        boolean flag = true;
        int sum = number;
        for(int i = 0; i < Dimention; ++i){
            if(board[x][i].isFixed()){ 
                sum+= board[x][i].dominant();
                if(board[x][i].dominant() == number) {
                    flag = false;
                    break;
                }
            }
        }
        //if(sum > valueOfBoard) return false;
        return flag;
    }

    private boolean checkCol(int y, int number){
        boolean flag = true;
        int sum = number;
        for(int i = 0; i < Dimention; ++i){
            if(board[i][y].isFixed()){ 
                sum += board[i][y].dominant();
                if(board[i][y].dominant() == number) {
                    flag = false;
                    break;
                }
            }
        }
        //if(sum > valueOfBoard) return false;
        return flag;
    }

    private boolean checkSquare(int x, int y, int number){
        boolean flag = true; //x = 1 y = 6
        int root = (int)(Math.sqrt(Dimention)); //3
        int row = x / root; //1/3=0
        int col = y / root; //6/3=2
        int sum = number;
        for(int k = row*root; k < (row+1)*root; ++k){ //0*3=0 -> 1*3=3
            for(int l = col*root; l < (col+1)*root; ++l){ //2*3=6 -> 3*3=9
                if(board[k][l].isFixed()){ 
                    sum += board[k][l].dominant();
                    if(board[k][l].dominant() == number) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        //if(sum > valueOfBoard) return false;
        return flag;
    }

    public Sudoku mutate(){
        Sudoku child = new Sudoku(this);
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                if(!this.board[i][j].isFixed()){
                    child.board[i][j].setRandom();
                }

            }
        }
        return child;
    }

    public void pickRandom(Sudoku s, int x, int y){
        s.board[x][y].setRandom();
        if(s.board[x][y].isFixed()){
            int num = s.board[x][y].dominant();
            for(int i = 0; i < Dimention; ++i){
                s.board[x][i].remove(num);
                s.board[i][y].remove(num);
            }
        }
    }

    public int[][] getBoard(){
        int[][] cells = new int[Dimention][Dimention];
        for(int i = 0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                cells[i][j] = this.board[i][j].dominant();
            }
        }
        return cells;
    }

    public int[][] getFixedBoard(){
        int[][] cells = new int[Dimention][Dimention];
        for(int i =0; i < Dimention; ++i){
            for(int j = 0; j < Dimention; ++j){
                if(this.board[i][j].isFixed()){
                    cells[i][j] = this.board[i][j].dominant();
                    continue;
                }
                cells[i][j] = 0;
            }
        }
        return cells;
    }

    public int getDimention(){
        return Dimention;
    }

    public int getFitness(){
        return this.fitness;
    }

    public boolean isTerminal(){
        return (this.fitness == MAX_FITNESS);
    }

    private int findValueOfBoard(){
        int sum = 0;
        for(int i = 1; i <= Dimention; ++i){
            sum += i;
        }
        return sum;
    }

    @Override
    public int compareTo(Sudoku other){
        return Integer.compare(other.fitness, this.fitness);
    }

    public void printANSI(){
        String color = "";
        System.out.print("\n ------- ------- -------");
        for(int i = 0; i < Dimention; ++i){
            System.out.println();
            for(int j = 0; j < Dimention; ++j){
                if(j%3==0) System.out.print("| ");
                if(!board[i][j].isFixed()){
                    color = ANSI_BLUE;
                }
                System.out.print(color + this.board[i][j].dominant() + " " + ANSI_RESET);
                color = "";
            }
            System.out.print("|");
            if((i+1)%3==0) System.out.print("\n ------- ------- -------");
        }
        System.out.print(ANSI_RESET);
        System.out.println("\nWith fitness " + this.fitness + "/" + MAX_FITNESS);
        System.out.println("\nWith errors " + (MAX_FITNESS - this.fitness));
    }

    public void print(){
        System.out.print("\n ------- ------- -------");
        for(int i = 0; i < Dimention; ++i){
            System.out.println();
            for(int j = 0; j < Dimention; ++j){
                if(j%3==0) System.out.print("| ");
                System.out.print(this.board[i][j].dominant() + " ");
            }
            System.out.print("|");
            if((i+1)%3==0) System.out.print("\n ------- ------- -------");
        }
        System.out.println("\nWith fitness " + this.fitness + "/" + MAX_FITNESS);
        System.out.println("\nWith errors " + (MAX_FITNESS - this.fitness));
    }

    public void printFixed(){
        System.out.print("\n ------- ------- -------");
        for(int i = 0; i < Dimention; ++i){
            System.out.println();
            for(int j = 0; j < Dimention; ++j){
                if(j%3==0) System.out.print("| ");
                
                if(this.board[i][j].isFixed()) System.out.print(1+" ");
                else System.out.print(0+" ");
            }
            System.out.print("|");
            if((i+1)%3==0) System.out.print("\n ------- ------- -------");
        }
        
        System.out.println("\nWith fitness " + this.fitness + "/" + MAX_FITNESS);
        System.out.println("\nWith errors " + (MAX_FITNESS - this.fitness));
    }

    public void printStar(){
        String pretext = " ";
        System.out.print("\n ------------- ------------- -------------");
        for(int i = 0; i < Dimention; ++i){
            System.out.println("\n");
            for(int j = 0; j < Dimention; ++j){
                if(j%3==0) System.out.print("| ");
                if(!board[i][j].isFixed()){
                    pretext = "*";
                }
                System.out.print(pretext + this.board[i][j].dominant() + pretext + " ");
                pretext = " ";
            }
            System.out.print("|");
            if((i+1)%3==0) System.out.print("\n ------------- ------------- -------------");
        }
        
        System.out.println("\nWith fitness " + this.fitness + "/" + MAX_FITNESS);
        System.out.println("\nWith errors " + (MAX_FITNESS - this.fitness));
    }
}