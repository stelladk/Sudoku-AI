import java.util.*;
import java.security.SecureRandom;

public class Cell{
    private ArrayList<Integer> list;
    private int dominant;
    private boolean fixed;

    private static final int Dimention = 9;

    public Cell(){ //empty list
        this.list = new ArrayList<Integer>();
        this.dominant = 0;
        this.fixed = false;
    }

    public Cell(Cell other){ //copy constructor
        this.list = new ArrayList<Integer>();
        for(int v : other.list){
            this.list.add(v);
        }
        setRandom();
    }

    public Cell(Cell a, Cell b){ //merge constructor
        this.list = new ArrayList<Integer>();
        if(a.isFixed()){
            this.dominant = a.dominant;
            this.list.add(this.dominant);
            this.fixed = true;
        }else if(b.isFixed()){
            this.dominant = b.dominant;
            this.list.add(this.dominant);
            this.fixed = true;
        }else{
            for(int avalue: a.list){
                this.list.add(avalue);
            }
            for(int bvalue : b.list){
                if(!this.exists(bvalue)){
                    this.list.add(bvalue);
                }
            }
            setRandom();
        }
    }

    public Cell(int value){ //list with value
        this(value, false);
    }

    public Cell(int value, boolean fixed){ // list with value
        this.list = new ArrayList<Integer>();
        this.list.add(value);
        this.dominant = value;
        this.fixed = fixed;
    }

    public void remove(int num){
        for(int v = 0; v < list.size(); ++v){
            if(list.get(v) == num){
                this.list.remove(v);
                break;
            }
        }
        setRandom();
    }

    public void add(int num){
        if(num > Dimention || num < 1) return;
        if(!exists(num)) list.add(num);
        if(list.size() == 1) dominant = list.get(0);
    }

    public void fill(){
        for(int i = 1; i <= Dimention; ++i){
            this.list.add(i);
        }
        this.dominant = pickRandom();
    }

    public void makeFixed(){
        this.fixed = true;
    }

    public boolean exists(int num){
        for(int v : list){
            if(v == num) return true;
        }
        return false;
    }

    public boolean hasDominant(){
        if(dominant == 0) return false;
        return true;
    }

    public int get(int index){
        if(list.size() > index) return list.get(index);
        return 0;
    }

    public int dominant(){
        return dominant;
    }

    public int size(){
        return list.size();
    }

    public boolean isFixed(){
        return this.fixed;
    }

    public void setRandom(){
        if(this.size() == 0) dominant = 0;
        else if(this.size() == 1) {
            dominant = list.get(0);
            fixed = true;
        }
        else dominant = pickRandom();
    }

    private int pickRandom(){
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public void print(){
        System.out.print("Dominant: "+ dominant);
        System.out.print(", [");
        for(int i : list){
            System.out.print(" "+i);
        }
        System.out.println("] fixed:"+fixed);
    }
}