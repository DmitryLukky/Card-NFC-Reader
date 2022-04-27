package com.pro100svitlo.creditCardNfcReader.utils;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by pro100svitlo on 15.05.16.
 */

public final class AtrUtils {

    /**
     * MultiMap containing ATR
     */
    private static final HashMap<String, String> MAP = new HashMap<String, String>();

    static {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            is = AtrUtils.class.getResourceAsStream("/smartcard_list.txt");
            isr = new InputStreamReader(is, CharEncoding.UTF_8);
            br = new BufferedReader(isr);

            int lineNumber = 0;
            String line;
            String currentATR = null;
            while ((line = br.readLine()) != null) {
                ++lineNumber;
                if (line.startsWith("#") || line.trim().length() == 0) { // comment ^#/ empty line ^$/
                    continue;
                } else if (line.startsWith("\t") && currentATR != null) {
                    MAP.put(currentATR, line.replace("\t", "").trim());
                } else if (line.startsWith("3")) { // ATR hex
                    currentATR = StringUtils.deleteWhitespace(line.toUpperCase());
                } else {
                    throw new RuntimeException("Encountered unexpected line in atr list: currentATR=" + currentATR + " Line(" + lineNumber + ") = " + line);
//                    LOGGER.debug("Encountered unexpected line in atr list: currentATR=" + currentATR + " Line(" + lineNumber
//                            + ") = " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(isr);
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Private constructor
     */
    private AtrUtils() {
    }

}
