import java.math.BigInteger;
import java.util.Arrays;


public class AsconHash {
    public final int AsconHashRate = 8;
    public final int AsconHashPARounds = 12;
    public final int AsconHashPBRounds = 12;
    public final int AsconHashBytes = 32;
    public final int CryptoBytes = 32;

    public final long AsconHashIV = ((long) (AsconHashRate * 8)) << 48
            | ((long) AsconHashPARounds) << 40
            | ((long) (AsconHashPARounds - AsconHashPBRounds)) << 32
            | (((long) AsconHashBytes) * 8);


    private byte getByte(long x, int i) {
        return (byte) (x >> (56 - 8 * i) & 0xFF);
    }

    private byte[] storeBytes(long x) {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            result[i] = getByte(x, i);
        }
        return result;
    }

    public long setByte(byte b, int i) {
        return ((long) (b & 0xFF)) << (56 - 8 * i);
    }

    public long pad(int i) {
        return setByte((byte) 0x80, i);
    }

    public long loadBytes(byte[] bytes, int n) {
        long x = 0;
        for (int i = 0; i < n; ++i) {
            x = x | (setByte(bytes[i], i));
        }
        return x;
    }

    public static long ROR(long x, int n) {
        return (x >>> n) | (x << (-n & 63));
    }


    public void round(AsconState s, byte C) {
        //System.out.println("========================================");
        //System.out.printf(" round constant: %02X\n", C);
        AsconState t = new AsconState();
        /* addition of round constant */
        s.x[2] = s.x[2] ^ (C & 0xFF);
        //s.PrintState(" round constant");
        /* substitution layer */
        s.x[0] ^= s.x[4];
        s.x[4] ^= s.x[3];
        s.x[2] ^= s.x[1];
        //s.PrintState(" before keccak s-box");
        /* start of keccak s-box */
        t.x[0] = s.x[0] ^ (~s.x[1] & s.x[2]);
        t.x[1] = s.x[1] ^ (~s.x[2] & s.x[3]);
        t.x[2] = s.x[2] ^ (~s.x[3] & s.x[4]);
        t.x[3] = s.x[3] ^ (~s.x[4] & s.x[0]);
        t.x[4] = s.x[4] ^ (~s.x[0] & s.x[1]);
        //t.PrintState(" after keccak s-box");
        /* end of keccak s-box */
        t.x[1] ^= t.x[0];
        t.x[0] ^= t.x[4];
        t.x[3] ^= t.x[2];
        t.x[2] = ~t.x[2];
        //t.PrintState(" substitution layer");
        /* linear diffusion layer */
        s.x[0] = t.x[0] ^ ROR(t.x[0], 19) ^ ROR(t.x[0], 28);
        s.x[1] = t.x[1] ^ ROR(t.x[1], 61) ^ ROR(t.x[1], 39);
        s.x[2] = t.x[2] ^ ROR(t.x[2], 1) ^ ROR(t.x[2], 6);
        s.x[3] = t.x[3] ^ ROR(t.x[3], 10) ^ ROR(t.x[3], 17);
        s.x[4] = t.x[4] ^ ROR(t.x[4], 7) ^ ROR(t.x[4], 41);
        //s.PrintState(" round output");
    }

    public void Permutation12(AsconState s) {
        round(s, (byte) 0xf0);
        round(s, (byte) 0xe1);
        round(s, (byte) 0xd2);
        round(s, (byte) 0xc3);
        round(s, (byte) 0xb4);
        round(s, (byte) 0xa5);
        round(s, (byte) 0x96);
        round(s, (byte) 0x87);
        round(s, (byte) 0x78);
        round(s, (byte) 0x69);
        round(s, (byte) 0x5a);
        round(s, (byte) 0x4b);
    }


    public final byte[] hash(byte[] in) {
        int len = in.length;
        byte[] out = new byte[CryptoBytes];
        AsconState s = new AsconState();
        s.x[0] = AsconHashIV;
        // other variables are zero
        s.PrintState("Initial value");
        Permutation12(s);
        s.PrintState("initialization");

        // absorb full plaintext blocks
        while (len >= AsconHashRate) {
            s.x[0] = s.x[0] ^ (loadBytes(in, 8));
            s.PrintState("absorb plaintext");
            Permutation12(s);
            len -= AsconHashRate;
            // in should be in[AsconHashRate:]
            in = Arrays.copyOfRange(in, AsconHashRate, in.length);
        }
        // absorb final plaintext block
        s.x[0] ^= loadBytes(in, len);
        s.x[0] ^= pad(len);
        s.PrintState("pad plaintext");
        Permutation12(s);
        s.PrintState("before squeeze");

        // squeeze full output blocks
        len = CryptoBytes;
        int start = 0;
        while (len > AsconHashRate) {
            System.arraycopy(storeBytes(s.x[0]), 0, out, start, 8);
            s.PrintState("squeeze output");
            Permutation12(s);
            start += AsconHashRate;
            len -= AsconHashRate;
        }
        // squeeze final output block
        //System.out.println("len: " + len);
        System.arraycopy(storeBytes(s.x[0]), 0, out, start, len);
        s.PrintState("squeeze output");
        return out;
    }
}
