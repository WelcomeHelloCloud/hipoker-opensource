package com.hipoker.chainutil.util;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.util.Optional;

public class ChainUtil {


    /**
     * This url is blockChain url.
     * If you need to switch to eth, change it;
     */
    private static final String URL;

    private static final Web3j web3j;

    static {
        URL = "https://bsc-dataseed1.defibit.io/";
        web3j = Web3j.build(new HttpService(URL));
    }

    /**
     * This is schedule task.
     * Execute per seconds;
     */
    public void updateBlockTask() {
        getCurrentBlockNumber();
    }

    public Long getCurrentBlockNumber() {
        Long temp = null;
        try {
            temp = web3j.ethBlockNumber().sendAsync().get().getBlockNumber().longValue();
            DefaultBlockParameterNumber defaultBlockParameterNumber = new DefaultBlockParameterNumber(temp);
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameterNumber,true).sendAsync().get();
            EthBlock.Block _block = ethBlock.getBlock();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
    }

    public String getTransactionByHash(String hash, int index) {
        String result = null;
        try {
            Optional<Transaction> optional = web3j.ethGetTransactionByHash(hash).sendAsync().get().getTransaction();
            if (optional.isPresent()) {
                result = toStringHex(optional.get().getInput().substring(2, optional.get().getInput().length()));
                String[] array = result.split("->");

                result = array[index].replace(" "+(index+1), "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
