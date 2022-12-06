package com.hipoker.chainutil.util;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Keccak256 implemented by java
 */
public class HashUtil {

    public static String getKeccak256Result(String hash, String key) {
        String result = keccak256(key + hash);
        BigInteger bigInteger = new BigInteger(result.substring(2, result.length()), 16);
        return bigInteger.toString();
    }



    public static String keccak256(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        String hex = "0x" + Hex.toHexString(bytes);
        return Hash.sha3(hex);
    }
}
