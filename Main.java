
public class Main{

    public static void main(String[] args){
        int[][] sudoku = {
            {0,4,0,6,0,5,9,0,0},
            {6,0,0,0,2,0,0,8,4},
            {7,3,1,0,0,0,0,0,2},
            {5,8,0,4,7,0,2,0,0},
            {0,0,0,2,0,8,0,0,0},
            {0,0,2,0,1,3,0,7,9},
            {9,0,0,0,0,0,1,2,3},
            {8,7,0,0,4,0,0,0,5},
            {0,0,6,3,0,9,0,4,0}
        };
        int[][] sudoku_easy = {
            {2,4,8,6,3,5,9,1,7},
            {6,0,0,0,2,1,0,8,4},
            {7,3,1,9,8,4,5,6,2},
            {5,8,0,4,7,0,2,0,1},
            {3,1,0,2,0,8,0,0,6},
            {4,6,2,0,1,3,0,7,9},
            {9,5,4,8,6,0,1,2,3},
            {8,7,0,1,4,0,0,0,5},
            {1,2,6,3,5,9,7,4,8}
        };
        
        solve(sudoku_easy).print();
        solve(sudoku).print();
    }

    public static Sudoku solve(int[][] board){
        System.out.println("\nSolving board...");
        Genetic genetic = new Genetic(board);
        Sudoku result = genetic.geneticAlgorithm();
        int iter = 1;
        while(!result.isTerminal()){
            genetic = new Genetic(result.getFixedBoard());
            result = genetic.geneticAlgorithm();
            ++iter;
        }
        System.out.println("Number of iterations it took: " + iter);
        return result;
    }
}