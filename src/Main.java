import java.util.Scanner;

public class Main {
    public static byte[] hexStringToByteArray(String s) {
        s = s.startsWith("0x") ? s.substring(2) : s;
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static final String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        // convert 0x prefixed hex string to byte array
        byte[] bytes = hexStringToByteArray(line);
        // create AsconHash object
        AsconHash asconHash = new AsconHash();
        byte[] h = asconHash.hash(bytes);
        // print hash
        //System.out.println("");
        System.out.println("0x" + byteArrayToHexString(h));

    }
}