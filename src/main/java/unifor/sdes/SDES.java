package unifor.sdes;

import unifor.sdes.utils.LoggerUtils;

/**
 * @author Fernando Nogueira
 * @since 11/24/16 11:47 PM
 */
public class SDES {

    public static void main(String[] args) {

        KeyGen keyGen = new KeyGen();
        EncryptionDecryption encryptionDecryption = new EncryptionDecryption();

        String plainText;
        String key;
        int[] encDecResult = new int[8];

        try {
            plainText = "11001111";
            key = "1001100111";

            LoggerUtils.emptyLine();
            System.out.println("Using 8bit plaintext: [" + plainText + "]");
            System.out.println("Using 10bit key: [" + key + "]");

            LoggerUtils.emptyLine();

            LoggerUtils.printMsg(true, " Starting encryption...");
            LoggerUtils.printMsg(true, " Generating key...");
            LoggerUtils.printDots();
            keyGen.genKeys(key);
            LoggerUtils.printDots();
            encDecResult = encryptionDecryption.encrypt(plainText, keyGen.getKey1(), keyGen.getKey2());

            LoggerUtils.printDots();
            LoggerUtils.printMsg(true, "");
            LoggerUtils.printMsg(true, " Starting decryption...");

            plainText = "10101011";

            LoggerUtils.emptyLine();
            LoggerUtils.printMsg(true, "Text to decrypt: [" + plainText + "]");


            LoggerUtils.printMsg(true, " Generating key...");
            LoggerUtils.printDots();
            LoggerUtils.printMsg(true, " Decrypting...");
            LoggerUtils.printDots();
            keyGen.genKeys(key);
            LoggerUtils.printDots();
            encDecResult = encryptionDecryption.encrypt(plainText, keyGen.getKey2(), keyGen.getKey1());
            LoggerUtils.printDots();

        } catch (Exception e) {
            System.out.println("Error ocurred while trying to execute sdes:");
            e.printStackTrace();
        }

    }

}