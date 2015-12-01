package kenkensolver;

/**
 *
 * @author Ross
 */
public class KenKenEntry {
    int[] possvals;
    int value = 0;
    
    public KenKenEntry(int possvals){
        this.possvals = new int[possvals];
        for(int i = 0; i<possvals; i++){
            this.possvals[i] = 1;
        }
    }
}
