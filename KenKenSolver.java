package kenkensolver;
import java.io.*;
import java.util.Arrays;
/**
 *
 * @author Ross
 */
public class KenKenSolver {
    public static void main(String[] args) {
        int runcount = 0;
        String[][] commands;
        commands = readCommands();
        KenKenEntry[][] kenkenmatrix = getCommands(commands);
        kenkenmatrix = initializeMatrix(kenkenmatrix);
        int length = kenkenmatrix.length;
        int[][] outputmatrix = new int[length][length];
        for(int i = 0; i<length; i++){
            for(int j = 0; j<length; j++){
                outputmatrix[i][j] = kenkenmatrix[i][j].value;
            }
        }
        for(int i = 0; i<length;i++){
            System.out.println(Arrays.toString(outputmatrix[i]));
        }
        while(solvedMatrix(kenkenmatrix)||(runcount>10)){
            checkMatrix(kenkenmatrix);
            runcount++;
        }
        //int length = kenkenmatrix.length;
        //int[][] outputmatrix = new int[length][length];
        for(int i = 0; i<length; i++){
            for(int j = 0; j<length; j++){
                outputmatrix[i][j] = kenkenmatrix[i][j].value;
            }
        }
        for(int i = 0; i<length;i++){
            System.out.println(Arrays.toString(outputmatrix[i]));
        }
        System.out.println(runcount);
    }
    private static String[][] readCommands(){
        String line;
        String[][] commands = new String[30][];
        int count;
    try
    {
   	FileReader filereader = new FileReader("inputs.txt");
        BufferedReader bufferedreader = new BufferedReader(filereader);
        try{
            line = bufferedreader.readLine();
            count = 0;
            while(line != null){
                commands[count]=line.split(" ");
                line = bufferedreader.readLine();
                count++;
            }
        bufferedreader.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    catch (FileNotFoundException ex)  
    {
        ex.printStackTrace();
    }
    return commands;
    }
    private static KenKenEntry[][] getCommands(String[][] commands){
        KenKenEntry[][] kenkenmatrix = creatematrix(commands[0][0]);
        for(int i=1; i< commands.length; i++){
            try{
                for (int j=0; j<commands[i].length;j++){
                    try{
                        kenkenmatrix = applycommands(commands[i], kenkenmatrix);
                    }
                    catch(NullPointerException e){
                        j = commands.length;
                    }
                }
            }
            catch(NullPointerException e){
                i = commands.length;
            }           
        }
        return kenkenmatrix;
    }
    private static KenKenEntry[][] creatematrix(String size){
        int intsize;
        intsize = (int)size.charAt(0)-48;
        KenKenEntry[][] kenkenmatrix = new KenKenEntry[intsize][intsize];
        for (int i = 0; i<intsize; i++){
            for (int j = 0; j<intsize; j++){
                kenkenmatrix[i][j] = new KenKenEntry(intsize);
            }
        }
        return kenkenmatrix;
    }
    
    private static KenKenEntry[][] applycommands(String[] commands, KenKenEntry[][] kenkenmatrix){
        int[] possabilities;
        int size = kenkenmatrix.length;
        int cells = commands.length-2;
        int target = Integer.parseInt(commands[0]);
        int[] sumnums = new int[cells-1];
        switch(commands[1]){
            case "+":
                possabilities = recursiveadd(cells, target, 0, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                }
                break;
            case "-":
                possabilities = recursivesubtract(cells, target, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                }
                break;
            case "*":
                possabilities = recursivemultiply(cells, target, 1, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                }
                break;
            case "/":
                possabilities = recursivedivide(cells, target, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                }
                break;
            case "=":
               possabilities = new int[cells];
               possabilities[target-1] = 1;
               for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                    kenkenmatrix[row][col].value = target;
                    kenkenmatrix[row][col].isKnown = true;
                }
            default:
                System.out.println("an error has occured in applycommands function");
                break;
        }
        return kenkenmatrix;
    }
    private static KenKenEntry[][] initializeMatrix(KenKenEntry[][] kenkenmatrix){
        int size = kenkenmatrix.length;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(kenkenmatrix[i][j].value != 0){
                    kenkenmatrix = updateMatrix(kenkenmatrix, i, j);
                }
            }
        }
        return kenkenmatrix;
    }
    private static KenKenEntry[][] updateMatrix(KenKenEntry[][] kenkenmatrix, int row, int col){
        int size = kenkenmatrix.length;
        int val = kenkenmatrix[row][col].value;
        for(int i = 0; i<size; i++){
            if(i != col) kenkenmatrix[row][i].possvals[val] = 0;
            if(i != row) kenkenmatrix[i][col].possvals[val] = 0;
        }
        return kenkenmatrix;
    }
    private static KenKenEntry[][] checkMatrix(KenKenEntry[][] kenkenmatrix){
        int size = kenkenmatrix.length;
        int temp;
        int numposs;
        int loc = 0;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(kenkenmatrix[i][j].value == 0){
                    numposs = 0;
                    for(int k = 0; k<size; k++){
                        temp = kenkenmatrix[i][j].possvals[k];
                        numposs = numposs + temp;
                        if(temp!=0) loc = k+1;
                    }
                    if(numposs == 1){
                        kenkenmatrix[i][j].value = loc;
                    }
                    kenkenmatrix = updateMatrix(kenkenmatrix,i,j);
                }
            }
        }
        return kenkenmatrix;
    }
    private static boolean solvedMatrix(KenKenEntry[][] kenkenmatrix){
        int size = kenkenmatrix.length;
        boolean unsolved = false;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(kenkenmatrix[i][j].value == 0){
                    unsolved = true;
                }
            }
        }
        return unsolved;
    }
    private static int[] recursiveadd(int depth, int target, int sum, int size, int[]sumnums){
        if(depth>1){
            int[] allposs = new int[size];
            for(int i = 1; i<=size; i++){
                sumnums[depth-2] = i;
                int[] possabilities = recursiveadd((depth-1), target, (sum+i), size, sumnums);
                for(int j = 0; j< (possabilities.length); j++){
                    if(possabilities[j]==1){
                        allposs[j] = 1;
                    }
                }
            }
            return(allposs);
        }
        else{
            int[] possabilities = new int[size];
            int temp;
            for(int i = 1; i<=size; i++){
                temp = sum+i;
                if(temp==target){
                    possabilities[i-1] = 1;
                    for(int j = 0; j<sumnums.length; j++){
                        possabilities[(sumnums[j]-1)] = 1;
                    }
                }
            }
            return(possabilities);
        }
    }
    private static int[] recursivemultiply(int depth, int target, int sum, int size, int[]sumnums){
        if(depth>1){
            int[] allposs = new int[size];
            for(int i = 1; i<=size; i++){
                sumnums[depth-2] = i;
                int[] possabilities = recursivemultiply((depth-1), target, (sum*i), size, sumnums);
                for(int j = 0; j< (possabilities.length); j++){
                    if(possabilities[j]==1){
                        allposs[j] = 1;
                    }
                }
            }
            return(allposs);
        }
        else{
            int[] possabilities = new int[size];
            int temp;
            for(int i = 1; i<=size; i++){
                temp = sum*i;
                if(temp==target){
                    possabilities[i-1] = 1;
                    for(int j = 0; j<sumnums.length; j++){
                        possabilities[(sumnums[j]-1)] = 1;
                    }
                }
            }
            return(possabilities);
        }
    }
    private static int[] recursivesubtract(int depth, int target, int size, int[]sumnums){
        if(depth>1){
            int[] allposs = new int[size];
            for(int i = 1; i<=size; i++){
                sumnums[depth-2] = i;
                int[] possabilities = recursivesubtract((depth-1), target, size, sumnums);
                for(int j = 0; j< (possabilities.length); j++){
                    if(possabilities[j]==1){
                        allposs[j] = 1;
                    }
                }
            }
            return(allposs);
        }
        else{
            int[] possabilities = new int[size];
            int temp;
            int sum=sumnums[0];
            for(int k = 0; k<sumnums.length;k++){
                sum = sum-sumnums[k];
            }
            for(int i = 1; i<=size; i++){
                temp = sum-i;
                if(temp==target){
                    possabilities[i-1] = 1;
                    for(int j = 0; j<sumnums.length; j++){
                        possabilities[(sumnums[j]-1)] = 1;
                    }
                }
            }
            return(possabilities);
        }
    }
    private static int[] recursivedivide(int depth, int target, int size, int[]sumnums){
        if(depth>1){
            int[] allposs = new int[size];
            for(int i = 1; i<=size; i++){
                sumnums[depth-2] = i;
                int[] possabilities = recursivedivide((depth-1), target, size, sumnums);
                for(int j = 0; j< (possabilities.length); j++){
                    if(possabilities[j]==1){
                        allposs[j] = 1;
                    }
                }
            }
            return(allposs);
        }
        else{
            int[] possabilities = new int[size];
            double temp;
            double sum = (double)sumnums[0];
            for(int k = 1; k<sumnums.length;k++){
                sum = sum/(double)sumnums[k];
            }
            for(int i = 1; i<=size; i++){
                temp = sum/i;
                if(temp==(double)target){
                    possabilities[i-1] = 1;
                    for(int j = 0; j<sumnums.length; j++){
                        possabilities[(sumnums[j]-1)] = 1;
                    }
                }
            }
            return(possabilities);
        }
    }
}