package unifor.sdes;

import unifor.sdes.utils.BinaryUtils;
import unifor.sdes.utils.LoggerUtils;

/**
 * @author Fernando Nogueira
 * @since 11/24/16 11:47 PM
 */
public class EncryptionDecryption {
    private int[] sdesKey1 = new int[8];
    private int[] sdesKey2 = new int[8];
    private int[] plainText = new int[8];

    public void SaveParameters(String plainText, int[] key1, int[] key2) {
        int[] pt = new int[8];

        char c1;
        String ts;

        try {
            for (int i = 0; i < 8; i++) {
                c1 = plainText.charAt(i);
                ts = Character.toString(c1);
                pt[i] = Integer.parseInt(ts);

                if (pt[i] != 0 && pt[i] != 1) {
                    LoggerUtils.printMsg(true, "Error! Invalid plain text...");
                    System.exit(0);
                    return;
                }
            }
        } catch (Exception e) {
            LoggerUtils.printMsg(true, "Error! Invalid plain text...");
            System.exit(0);
            return;

        }

        this.plainText = pt;

        LoggerUtils.printMsg(false, "Plaintext printArray: ");
        LoggerUtils.printArray(this.plainText, 8);
        LoggerUtils.emptyLine();

        this.sdesKey1 = key1;
        this.sdesKey2 = key2;
    }

    /**
     * Initial permutation [2 6 3 1 4 8 5 7]
     **/
    void ip() {
        int[] temp = new int[8];

        temp[0] = plainText[1];
        temp[1] = plainText[5];
        temp[2] = plainText[2];
        temp[3] = plainText[0];
        temp[4] = plainText[3];
        temp[5] = plainText[7];
        temp[6] = plainText[4];
        temp[7] = plainText[6];

        plainText = temp;

        LoggerUtils.printMsg(false, "IP: ");
        LoggerUtils.printArray(this.plainText, 8);
        LoggerUtils.emptyLine();

    }

    void InverseInitialPermutation() {
        int[] temp = new int[8];

        temp[0] = plainText[3];
        temp[1] = plainText[0];
        temp[2] = plainText[2];
        temp[3] = plainText[4];
        temp[4] = plainText[6];
        temp[5] = plainText[1];
        temp[6] = plainText[7];
        temp[7] = plainText[5];

        plainText = temp;
    }

    /**
     * Right part & plain text->>
     *
     * @param rightPart    right part of plain text
     * @param generatedKey key
     **/
    int[] mapRightPartAndKey(int[] rightPart, int[] generatedKey) {
        int[] temp = new int[8];

        /*
        41232341
         */
        temp[0] = rightPart[3];
        temp[1] = rightPart[0];
        temp[2] = rightPart[1];
        temp[3] = rightPart[2];
        temp[4] = rightPart[1];
        temp[5] = rightPart[2];
        temp[6] = rightPart[3];
        temp[7] = rightPart[0];

        LoggerUtils.printMsg(false, "Perm on right part: ");
        LoggerUtils.emptyLine();
        LoggerUtils.printArray(temp, 8);
        LoggerUtils.emptyLine();

        // Bit by bit XOR with sub-key
        temp[0] = temp[0] ^ generatedKey[0];
        temp[1] = temp[1] ^ generatedKey[1];
        temp[2] = temp[2] ^ generatedKey[2];
        temp[3] = temp[3] ^ generatedKey[3];
        temp[4] = temp[4] ^ generatedKey[4];
        temp[5] = temp[5] ^ generatedKey[5];
        temp[6] = temp[6] ^ generatedKey[6];
        temp[7] = temp[7] ^ generatedKey[7];

        LoggerUtils.printMsg(false, "XOR: ");
        LoggerUtils.printArray(temp, 8);
        LoggerUtils.emptyLine();

        // S matrix
        final int[][] S1 =
                {
                        {0, 1, 2, 3},
                        {2, 0, 1, 3},
                        {3, 0, 1, 0},
                        {2, 1, 0, 3}
                };
        final int[][] S0 =
                {
                        {1, 0, 3, 2},
                        {3, 2, 1, 0},
                        {0, 2, 1, 3},
                        {3, 1, 3, 2}
                };

        int digit11 = temp[0];
        int digit14 = temp[3];
        int digit12 = temp[1];
        int digit13 = temp[2];

        int col1 = BinaryUtils.BinaryToDecimal(digit12, digit13);
        int row1 = BinaryUtils.BinaryToDecimal(digit11, digit14);
        int o1 = S0[row1][col1];

        int[] out1 = BinaryUtils.DecimalToBinaryArray(o1);

        LoggerUtils.printMsg(false, "S0: ");
        LoggerUtils.printArray(out1, 2);
        LoggerUtils.emptyLine();

        int d21 = temp[4];
        int d24 = temp[7];
        int row2 = BinaryUtils.BinaryToDecimal(d21, d24);

        int d22 = temp[5];
        int d23 = temp[6];
        int col2 = BinaryUtils.BinaryToDecimal(d22, d23);

        int o2 = S1[row2][col2];

        int[] out2 = BinaryUtils.DecimalToBinaryArray(o2);

        LoggerUtils.printMsg(false, "S1: ");
        LoggerUtils.printArray(out2, 2);
        LoggerUtils.emptyLine();

        int[] out = new int[4];
        out[0] = out1[0];
        out[1] = out1[1];
        out[2] = out2[0];
        out[3] = out2[1];

        int[] res = new int[4];
        res[0] = out[1];
        res[1] = out[3];
        res[2] = out[2];
        res[3] = out[0];

        LoggerUtils.printMsg(false, "Result of mapRightPartAndKey: ");
        LoggerUtils.printArray(res, 4);
        LoggerUtils.emptyLine();

        return res;
    }

    /**
     * fK(leftPart, R, generatedKey) = (leftPart (XOR) mapRightPartAndKey(R, generatedKey), R) .. returns 8-bit output
     *
     * @return 8bit result
     **/
    int[] fk(int[] leftPart, int[] rightPart, int[] generatedKey) {
        int[] tmp;
        int[] result = new int[8];

        tmp = mapRightPartAndKey(rightPart, generatedKey);

        /*
        (XOR (LEFT PART + tmp) CONCAT R)
         */
        result[0] = leftPart[0] ^ tmp[0];
        result[1] = leftPart[1] ^ tmp[1];
        result[2] = leftPart[2] ^ tmp[2];
        result[3] = leftPart[3] ^ tmp[3];

        result[4] = rightPart[0];
        result[5] = rightPart[1];
        result[6] = rightPart[2];
        result[7] = rightPart[3];

        return result;
    }

    /**
     * switch function (SW) interchanges the left and right 4 bits
     **/
    int[] sw(int[] input) {

        int[] temp = new int[8];

        temp[0] = input[4];
        temp[1] = input[5];
        temp[2] = input[6];
        temp[3] = input[7];

        temp[4] = input[0];
        temp[5] = input[1];
        temp[6] = input[2];
        temp[7] = input[3];

        return temp;
    }

    public int[] encrypt(String plaintext, int[] key1, int[] key2) {

        SaveParameters(plaintext, key1, key2);

        LoggerUtils.printDots();
        ip(); // init perm
        LoggerUtils.printDots();
        //split left and right parts
        int[] leftPart = new int[4];
        int[] rightPart = new int[4];
        leftPart[0] = plainText[0];
        leftPart[1] = plainText[1];
        leftPart[2] = plainText[2];
        leftPart[3] = plainText[3];

        rightPart[0] = plainText[4];
        rightPart[1] = plainText[5];
        rightPart[2] = plainText[6];
        rightPart[3] = plainText[7];

        LoggerUtils.printMsg(false, "first run leftPart: ");
        LoggerUtils.printArray(leftPart, 4);
        LoggerUtils.emptyLine();

        LoggerUtils.printMsg(false, "first run rightPart: ");
        LoggerUtils.printArray(rightPart, 4);
        LoggerUtils.emptyLine();

        //applying key sdesKey1
        int[] r1 = fk(leftPart, rightPart, sdesKey1);

        LoggerUtils.printMsg(false, "key1 applied: ");
        LoggerUtils.printArray(r1, 8);
        LoggerUtils.emptyLine();
        LoggerUtils.printDots();

        //sw
        int[] temp = sw(r1);

        LoggerUtils.printMsg(false, "SW: ");
        LoggerUtils.printArray(temp, 8);
        LoggerUtils.emptyLine();
        LoggerUtils.printDots();

        leftPart[0] = temp[0];
        leftPart[1] = temp[1];
        leftPart[2] = temp[2];
        leftPart[3] = temp[3];

        rightPart[0] = temp[4];
        rightPart[1] = temp[5];
        rightPart[2] = temp[6];
        rightPart[3] = temp[7];

        LoggerUtils.printMsg(false, "2nd run leftPart: ");
        LoggerUtils.printArray(leftPart, 4);
        LoggerUtils.emptyLine();

        LoggerUtils.printMsg(false, "2nd run rightPart: ");
        LoggerUtils.printArray(rightPart, 4);
        LoggerUtils.emptyLine();

        //Applying key sdesKey2
        int[] r2 = fk(leftPart, rightPart, sdesKey2);

        plainText = r2;

        LoggerUtils.printMsg(false, "key2 applied: ");
        LoggerUtils.printArray(this.plainText, 8);
        LoggerUtils.emptyLine();
        LoggerUtils.printDots();

        InverseInitialPermutation();

        LoggerUtils.printMsg(false, "Inverse IP [FINISHED]: ");
        LoggerUtils.printArray(this.plainText, 8);
        LoggerUtils.emptyLine();

        return plainText;
    }

}