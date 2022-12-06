package com.hipoker.chainutil.model;

import com.hipoker.chainutil.util.AESUtil;


import java.util.*;

/**
 * Poker
 */
public class Poker {

    private static final int SIZE = 90000000;

    private static final Random RANDOM = new Random();

    private static final String PREFIX = "HIPOKER-";

    private List<Integer> pokerList;

    private String aesKey;

    private String encryption;


    public static Poker instance() {
        Poker poker = new Poker();
        poker.initKey();
        poker.initEncryption();
        return poker;
    }

    private void initKey() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < AESUtil.KEY_LENGTH; i++) {
            stringBuilder.append(AESUtil.CHARS[random.nextInt(AESUtil.CHARS.length)]);
        }
        aesKey = stringBuilder.toString();
    }


    private void initEncryption() {
        AESUtil aesUtil = new AESUtil(aesKey);
        encryption = aesUtil.encrypt(PREFIX+(10000000 + RANDOM.nextInt(SIZE)));
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public List<Integer> getPokerList() {
        return pokerList;
    }

    public void setPokerList(List<Integer> pokerList) {
        this.pokerList = pokerList;
    }
}
