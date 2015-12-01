package kenkensolver;
import java.io.*;
import java.util.Arrays;
/**
 *
 * @author Ross
 */
public class KenKenSolver {
    static String[][] commands = readCommands();
    public static void main(String[] args) {
        KenKenEntry[][] kenkenmatrix = getCommands(commands);
        kenkenmatrix = initializeMatrix(kenkenmatrix);
        while(!fullMatrix(kenkenmatrix)){
            kenkenmatrix = solveMatrix(kenkenmatrix);
        }
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
    }
    private static String[][] readCommands(){
        String line;
        String[][] commands = new String[50][];
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
        KenKenEntry[][] kenkenmatrix = creatematrix((int)commands[0][0].charAt(0)-48);
        for(int i=1; i< commands.length; i++){
            try{
                        kenkenmatrix = applycommands(commands[i], kenkenmatrix);
                    
            }
            catch(NullPointerException e){
                i = commands.length;
            }           
        }
        return kenkenmatrix;
    }
    private static KenKenEntry[][] creatematrix(int intsize){
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
                    kenkenmatrix[row][col].possvals = updatePoss(kenkenmatrix[row][col].possvals,possabilities);
                }
                break;
            case "-":
                possabilities = recursivesubtract(cells, target, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = updatePoss(kenkenmatrix[row][col].possvals,possabilities);
                }
                break;
            case "*":
                possabilities = recursivemultiply(cells, target, 1, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = updatePoss(kenkenmatrix[row][col].possvals,possabilities);
                }
                break;
            case "/":
                possabilities = recursivedivide(cells, target, size, sumnums);
                for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = updatePoss(kenkenmatrix[row][col].possvals,possabilities);
                }
                break;
            case "=":
               possabilities = new int[size];
               possabilities[target-1] = 1;
               for(int i = 2; i < commands.length; i++){
                    int col = (int)commands[i].charAt(0)-65;
                    int row = (int)commands[i].charAt(1)-49;
                    kenkenmatrix[row][col].possvals = possabilities;
                    kenkenmatrix[row][col].value = target;
                }
                break;
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
        int val = kenkenmatrix[row][col].value-1;
        for(int i = 0; i<kenkenmatrix.length; i++){
            kenkenmatrix[row][i].possvals[val] = 0;
            kenkenmatrix[i][col].possvals[val] = 0;
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
    
    private static KenKenEntry[][] solveMatrix(KenKenEntry[][] kenkenmatrix){
        KenKenEntry[][] newmatrix = clonekenken(kenkenmatrix);
        KenKenEntry[][] presentmatrix = clonekenken(newmatrix);
        boolean loop = true;
        int size = newmatrix.length;
        while(loop){
            int temp, numposs, lowestcol=0, lowestrow=0, lowval = 0, lowposs = size, loc = 0;
            for(int i = 0; i<size; i++){
                for(int j = 0; j<size; j++){
                    if(newmatrix[i][j].value == 0){
                        numposs = 0;
                        for(int k = 0; k<size; k++){
                            temp = newmatrix[i][j].possvals[k];
                            numposs = numposs + temp;
                            if(temp!=0) loc = k+1;
                        }
                        if(numposs<lowposs){
                            lowposs = numposs;
                            lowval = loc;
                            lowestcol = j;
                            lowestrow = i;
                        }
                    }
                }
            }
            if(lowposs == 0){
                loop = false;
            }
            else{
                newmatrix[lowestrow][lowestcol].value = lowval;
                newmatrix = updateMatrix(newmatrix,lowestrow,lowestcol);
            if(fullMatrix(newmatrix)){
                if(checkCommands(commands, newmatrix)){
                    kenkenmatrix = newmatrix;
                }
                loop = false;
            }
            else{
                newmatrix = solveMatrix(newmatrix);
                if(fullMatrix(newmatrix)){
                    kenkenmatrix = newmatrix;
                    loop = false;
                }
                else{
                    presentmatrix[lowestrow][lowestcol].possvals[lowval-1] = 0;
                    newmatrix = clonekenken(presentmatrix);
                }
            }
            }
        }
        return(kenkenmatrix);
    }
    
    private static boolean fullMatrix(KenKenEntry[][] kenkenmatrix){
        int size = kenkenmatrix.length;
        boolean solved = true;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(kenkenmatrix[i][j].value == 0){
                    solved = false;
                    i = size;
                    j = size;
                }
            }
        }
        return solved;
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
            for(int k = 1; k<sumnums.length;k++){
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
            for(int k = 1; k<sumnums.length-1;k++){
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
    private static int[] updatePoss(int[] poss, int[] newposs){
        int length = poss.length;
        for (int i = 0; i<length; i++){
            if(poss[i] == 1 && newposs[i] == 1) poss[i] = 1;
            else poss[i] = 0;
        }
        return poss;
    }
    private static KenKenEntry[][] clonekenken(KenKenEntry[][] kenkenmatrix){
        KenKenEntry[][] clonedkenken = creatematrix(kenkenmatrix.length);
        for(int i = 0; i<kenkenmatrix.length; i++){
            for (int j = 0; j<kenkenmatrix.length; j++){
                for (int k = 0; k<kenkenmatrix.length; k++){
                    clonedkenken[i][j].possvals[k]=kenkenmatrix[i][j].possvals[k];
                }
                clonedkenken[i][j].value=kenkenmatrix[i][j].value;
            }
        }
        return clonedkenken;
    }
    private static boolean checkcommand(String[] commands, KenKenEntry[][] kenkenmatrix){
        int result;
        int target = Integer.parseInt(commands[0]);
        int[] row = new int[commands.length];
        int[] col = new int[commands.length];
        boolean passes = false;
        for(int i = 2; i < commands.length; i++){
        col[i-2] = (int)commands[i].charAt(0)-65;
        row[i-2] = (int)commands[i].charAt(1)-49;}
        switch(commands[1]){
            case "+":
                result = 0;
                for(int i = 0; i<commands.length-2;i++){
                    result = result + kenkenmatrix[row[i]][col[i]].value;
                }
                if(result==target) passes = true;
                break;
            case "-":
                result = kenkenmatrix[row[0]][col[0]].value;
                for(int i = 1; i<commands.length-2;i++){
                    result = Math.abs(result - kenkenmatrix[row[i]][col[i]].value);
                }
                if(result==target) passes = true;
                break;
            case "*":
                result = 1;
                for(int i = 0; i<commands.length-2;i++){
                    result = result*kenkenmatrix[row[i]][col[i]].value;
                }
                if(result==target) passes = true;
                break;
            case "/":
                double divresult=0.0;
                double[] divarray = new double[commands.length-2];
                double maxval=0.0;
                int maxloc=0;
                for(int i = 0; i<commands.length-2;i++){
                    divarray[i] = kenkenmatrix[row[i]][col[i]].value;
                    if(divarray[i]>maxval){
                        maxval = divarray[i];
                        maxloc = i;
                    }
                }
                for(int i = 0; i<divarray.length; i++){
                    if(i!=maxloc){
                        divresult = maxval/divarray[i];
                    }
                }
                if(divresult==(double)target) passes = true;
                break;
            case "=":
                passes = true;
                for(int i = 0; i<commands.length-2;i++){
                    if(kenkenmatrix[row[i]][col[i]].value!=target) passes = false;
                }
                break;
            default:
                System.out.println("an error has occured in the checkcommand function");
                break;
        }
        return(passes);
    }
        private static boolean checkCommands(String[][] commands, KenKenEntry[][] kenkenmatrix){
        boolean passes = true;
        for(int i=1; i< commands.length; i++){
            try{
                for (int j=0; j<commands[i].length;j++){
                    try{
                        if(!checkcommand(commands[i], kenkenmatrix)) passes = false;
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
        return passes;
    }
}