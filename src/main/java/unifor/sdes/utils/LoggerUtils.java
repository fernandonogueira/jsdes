package unifor.sdes.utils;

public class LoggerUtils {

    public static void printArray(int[] arr, int len) {
        System.out.print("[");

        for (int i = 0; i < len; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.print("]");
    }

    public static void printMsg(boolean newLine, String msgToPrint) {
        if (newLine) {
            System.out.println(msgToPrint);
        } else {
            System.out.print(msgToPrint);
        }
    }

    public static void printDots() {
        LoggerUtils.printMsg(true, "~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~");
    }

    public static void emptyLine() {
        LoggerUtils.printMsg(true, "");
    }
}
