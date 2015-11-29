package kenkensolver;

/**
 *
 * @author Ross
 */
public class KenKenEntry {
    int[] possvals;
    int value = 0;
    boolean isKnown = false;
    
    public KenKenEntry(int possvals){
        this.possvals = new int[possvals];
        for(int i = 0; i<possvals; i++){
            this.possvals[i] = 1;
        }
    }
}
