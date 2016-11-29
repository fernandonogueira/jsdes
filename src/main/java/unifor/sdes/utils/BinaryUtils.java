package unifor.sdes.utils;

/**
 * @author Fernando Nogueira
 * @since 11/24/16 11:47 PM
 */
public class BinaryUtils {

    /**
     * Receives a decimal number and returns an printArray representing that number in binary
     *
     * @param decimalNumber decimal number (e.g. 3)
     * @return The binary printArray ([1,1] in the example)
     */
    public static int[] DecimalToBinaryArray(int decimalNumber) {
        if (decimalNumber == 0) {
            int[] zero = new int[2];
            zero[0] = 0;
            zero[1] = 0;
            return zero;
        }

        int[] tmp = new int[10];

        int count = 0;
        for (int i = 0; decimalNumber != 0; i++) {
            tmp[i] = decimalNumber % 2;
            decimalNumber = decimalNumber / 2;
            count++;
        }

        int[] temp2 = new int[count];

        for (int i = count - 1, j = 0; i >= 0 && j < count; i--, j++) {
            temp2[j] = tmp[i];
        }

        if (count < 2) {
            tmp = new int[2];
            tmp[0] = 0;
            tmp[1] = temp2[0];
            return tmp;
        }

        return temp2;
    }

    /**
     * Binary to Dec
     *
     * @param bits Bits printArray. e.g. [1,1]
     * @return Decimal result (3 in the example)
     */
    public static int BinaryToDecimal(int... bits) {
        int tmp = 0;
        int tmp2 = 1;
        for (int i = bits.length - 1; i >= 0; i--) {
            tmp = tmp + (bits[i] * tmp2);
            tmp2 = tmp2 * 2;
        }

        return tmp;
    }
}

