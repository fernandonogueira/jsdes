package unifor.sdes;

import unifor.sdes.utils.LoggerUtils;

/**
 * @author Fernando Nogueira
 * @since 11/24/16 11:47 PM
 */
public class KeyGen {

    private int[] key = new int[10];
    private int[] key1 = new int[8];
    private int[] key2 = new int[8];

    private boolean keysAlreadyGenerated = false;

    /**
     * Executes P10 permutation
     * (1, 2, 3, 4, 5, 6, 7, 8, 9, 10) = (3, 5, 2, 7, 4, 10, 1, 9, 8, 6)
     */
    private void p10() {
        int[] temp = new int[10];

        temp[0] = key[2];
        temp[1] = key[4];
        temp[2] = key[1];
        temp[3] = key[6];
        temp[4] = key[3];
        temp[5] = key[9];
        temp[6] = key[0];
        temp[7] = key[8];
        temp[8] = key[7];
        temp[9] = key[5];

        key = temp;
    }

    public int[] getKey1() {
        if (!keysAlreadyGenerated) {
            LoggerUtils.printMsg(true, "Error! " +
                    "Keys were not created! You must first create your S-DES keys.");
            return null;
        }
        return key1;
    }

    public int[] getKey2() {
        if (!keysAlreadyGenerated) {
            LoggerUtils.printMsg(true, "Error! " +
                    "Keys were not created! You must first create your S-DES keys.");
            return null;
        }
        return key2;
    }

    /**
     * Executes LS1
     **/
    private void ls1() {
        int[] temp = new int[10];

        temp[0] = key[1];
        temp[1] = key[2];
        temp[2] = key[3];
        temp[3] = key[4];
        temp[4] = key[0];

        temp[5] = key[6];
        temp[6] = key[7];
        temp[7] = key[8];
        temp[8] = key[9];
        temp[9] = key[5];

        key = temp;

    }

    public void genKeys(String startKey) {
        int[] key = new int[10];

        char c1;
        String ts;

        try {
            for (int i = 0; i < 10; i++) {
                c1 = startKey.charAt(i);
                ts = Character.toString(c1);
                key[i] = Integer.parseInt(ts);

                if (key[i] != 0 && key[i] != 1) {
                    LoggerUtils.printMsg(true, "Error! Invalid key... Stopping...");
                    System.exit(0);
                    return;
                }
            }
        } catch (Exception e) {
            LoggerUtils.printMsg(true, "Error! Invalid key... Stopping...");
            System.exit(0);
            return;

        }
        this.key = key;

        LoggerUtils.printMsg(false, "Key: ");
        LoggerUtils.printArray(this.key, 10);
        LoggerUtils.printMsg(true, "");

        p10();

        LoggerUtils.printMsg(false, "P10: ");
        LoggerUtils.printArray(this.key, 10);
        LoggerUtils.printMsg(true, "");

        ls1();

        LoggerUtils.printMsg(false, "LS1: ");
        LoggerUtils.printArray(this.key, 10);
        LoggerUtils.printMsg(true, "");


        this.key1 = p8();

        LoggerUtils.printMsg(false, "K1 created: ");
        LoggerUtils.printArray(this.key1, 8);
        LoggerUtils.printMsg(true, "");

        ls2();

        LoggerUtils.printMsg(false, "LS2: ");
        LoggerUtils.printArray(this.key, 10);
        LoggerUtils.printMsg(true, "");

        this.key2 = p8();
        LoggerUtils.printMsg(false, "K2 created: ");
        LoggerUtils.printArray(this.key2, 8);
        LoggerUtils.printMsg(true, "");

        keysAlreadyGenerated = true;

    }

    /**
     * Executes P8 permutation
     *
     * @return (6 3 7 4 8 5 10 9)
     **/
    private int[] p8() {
        int[] temp = new int[8];

        temp[0] = key[5];
        temp[1] = key[2];
        temp[2] = key[6];
        temp[3] = key[3];
        temp[4] = key[7];
        temp[5] = key[4];
        temp[6] = key[9];
        temp[7] = key[8];

        return temp;

    }

    private void ls2() {
        int[] temp = new int[10];

        temp[0] = key[2];
        temp[1] = key[3];
        temp[2] = key[4];
        temp[3] = key[0];
        temp[4] = key[1];

        temp[5] = key[7];
        temp[6] = key[8];
        temp[7] = key[9];
        temp[8] = key[5];
        temp[9] = key[6];

        key = temp;

    }


}
