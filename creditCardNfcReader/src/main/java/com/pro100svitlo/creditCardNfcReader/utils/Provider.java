package com.pro100svitlo.creditCardNfcReader.utils;

import android.nfc.tech.IsoDep;

import com.pro100svitlo.creditCardNfcReader.enums.SwEnum;
import com.pro100svitlo.creditCardNfcReader.exception.CommunicationException;
import com.pro100svitlo.creditCardNfcReader.parser.IProvider;


import java.io.IOException;

public class Provider implements IProvider {

    private IsoDep mTagCom;

    public void setmTagCom(final IsoDep mTagCom) {
        this.mTagCom = mTagCom;
    }

    @Override
    public byte[] transceive(byte[] pCommand) throws CommunicationException {
        byte[] response = null;
        try {
            // send command to emv card
            response = mTagCom.transceive(pCommand);
        } catch (IOException e) {
            throw new CommunicationException(e.getMessage());
        }

        try {
            //LOGGER.debug("resp: " + TlvUtil.prettyPrintAPDUResponse(response));
            SwEnum val = SwEnum.getSW(response);
            if (val != null) {
                throw new RuntimeException("resp: " + val.getDetail());
                //LOGGER.debug("resp: " + val.getDetail());
            }
        } catch (Exception e) {
        }

        return response;
    }
}
