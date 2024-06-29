import java.math.BigInteger;
import java.util.Arrays;

public class AsconState {
    public long[] x = new long[5];

    public final boolean DEBUG = false;

    public AsconState() {
        Arrays.fill(x, 0);
    }

    public void PrintState(String text) {
        if (!DEBUG) {
            return;
        }
        if (text != "")
            System.out.print(text + " ");
        for (int i = 0; i < 5; i++) {
            // print x[i] as hex string
            System.out.print("x" + i + "=" + String.format("%016x", x[i]) + " ");
        }
        System.out.println();
    }
}
