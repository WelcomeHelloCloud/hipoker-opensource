package com.hipoker.chainutil;

import com.hipoker.chainutil.model.Poker;
import com.hipoker.chainutil.util.AESUtil;
import com.hipoker.chainutil.util.ChainUtil;
import com.hipoker.chainutil.util.HashUtil;

import java.util.*;

/**
 * Main implemented
 * We need 51 random numbers, because every 6 bits are a unit, and the decimal length of the hash result is 77 or 78.
 * So we generate a new length 6 hash array from hash and one sixth of it.
 * Then each hash is converted to decimal again, and every six bits are used as random numbers in order.
 * Finally, use the knuth algorithm, poker is ready!
 */
public class Main {


    private static final int HASH_SIZE = 6;

    private static final int HASH_LENGTH = 12;

    private static final int POKER_DECOR_SIZE = 4;

    private static final int POKER_NUMBER_SIZE = 13;

    private static final int POKER_SIZE = POKER_DECOR_SIZE * POKER_NUMBER_SIZE;


    public static void main(String[] args) {
        String aesKey = "kf1FuagUZXo4rKAW";
        String encryption = "hqiJ8RfV03nwicf8IIaj2PDTSRazXhwg5d/7Nju+yY0=";
        String hash = "0x896092a3dfe8d0a2b6794661fb1992f2582b15ba9f13c5212a3ecf12d187d68e";

        String aesEncryptionHash = "0xfebbdd2709de45bceaa5064004b77e3135e4dd82b625d36bfdcbf3eebb227741";
        int aesEncryptionIndex = 4;

        ChainUtil c = new ChainUtil();
        String chainEncryption = c.getTransactionByHash(aesEncryptionHash, aesEncryptionIndex);
        System.out.println("The chain encryption is "+chainEncryption);
        System.out.println("Their comparison results is " + chainEncryption.equals(encryption));

        Poker poker = new Poker();
        poker.setAesKey(aesKey);
        poker.setEncryption(encryption);
        executePoker(poker, hash);

        StringBuilder sb = new StringBuilder();
        poker.getPokerList().forEach(pokerValue -> {
            sb.append(pokerValue).append(",");
        });
        System.out.println("The poker Array is ["+sb.toString()+"]");


    }




    private static void executePoker(Poker poker, String hash) {
        String plaintext = new AESUtil(poker.getAesKey()).decrypt(poker.getEncryption());
        System.out.println("The plaintext is "+plaintext);
        String hashResult = HashUtil.getKeccak256Result(hash, plaintext.split("-")[1]);
        List<String> hashResultList = new ArrayList<>();
        for (int i = 0; i < HASH_SIZE; i++) {
            hashResultList.add(HashUtil.getKeccak256Result(hash, hashResult.substring(i * HASH_LENGTH, i * HASH_LENGTH + HASH_LENGTH)));
        }
        poker.setPokerList(new ArrayList<>(52));
        int[] pokerArray = initPokerArray();

        for (int i = pokerArray.length - 1; i > 0; i--) {
            int index = pokerArray.length - 1 - i;
            int batch = index % HASH_SIZE;
            int round = index / HASH_SIZE;
            int randomIndex = Integer.valueOf(hashResultList.get(batch).substring(round * HASH_SIZE, round * HASH_SIZE + HASH_SIZE)) % (i + 1);
            int temp = pokerArray[randomIndex];
            pokerArray[randomIndex] = pokerArray[i];
            pokerArray[i] = temp;
        }

        for (int pokerValue : pokerArray) {
            poker.getPokerList().add(pokerValue);
        }
    }

    private static int[] initPokerArray() {
        int[] pokerArray = new int[POKER_SIZE];
        for (int i = 0; i < POKER_DECOR_SIZE; i++) {
            for (int j = 0; j < POKER_NUMBER_SIZE; j++) {
                pokerArray[i * POKER_NUMBER_SIZE  + j] = ((i + 1) * 100) + j + 2;
            }
        }
        return pokerArray;
    }
}
