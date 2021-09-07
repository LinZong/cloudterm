package com.kodedu.cloudterm.helper;

public class ByteHelper {

    public static int toPositiveRange(byte b) {
        return (b & 0xFF);
    }
}
