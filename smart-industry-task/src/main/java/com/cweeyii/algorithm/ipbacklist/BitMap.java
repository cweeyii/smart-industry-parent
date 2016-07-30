package com.cweeyii.algorithm.ipbacklist;

/**
 * Created by cweeyii on 14/7/16 ${EMAIL}.
 */
public class BitMap {
    private byte[] bitMap = null;

    public BitMap(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException("非法的bitmap size=[" + size + "]");
        }
        if (size % 8 == 0) {
            bitMap = new byte[(int) (size / 8)];
        } else {
            bitMap = new byte[(int) (size / 8 + 1)];
        }
    }

    public synchronized void setLocation(long number) {
        if (number < 0) {
            throw new IllegalArgumentException("非法的位置参数location=[" + number + "]");
        }
        int index = 0;
        int bit_index = 0;
        if (number % 8 == 0) {
            index = (int) (number / 8 - 1);
            bit_index = 8;
        } else {
            index = (int) (number / 8);
            bit_index = (int) (number % 8);
        }
        switch (bit_index) {
            case 1:
                bitMap[index] = (byte) (bitMap[index] | 0x01);
                break;
            case 2:
                bitMap[index] = (byte) (bitMap[index] | 0x02);
                break;
            case 3:
                bitMap[index] = (byte) (bitMap[index] | 0x04);
                break;
            case 4:
                bitMap[index] = (byte) (bitMap[index] | 0x08);
                break;
            case 5:
                bitMap[index] = (byte) (bitMap[index] | 0x10);
                break;
            case 6:
                bitMap[index] = (byte) (bitMap[index] | 0x20);
                break;
            case 7:
                bitMap[index] = (byte) (bitMap[index] | 0x40);
                break;
            case 8:
                bitMap[index] = (byte) (bitMap[index] | 0x8);
                break;
        }

    }

    public synchronized boolean getLocation(long number) {
        if (number < 0) {
            throw new IllegalArgumentException("非法的位置参数location=[" + number + "]");
        }
        int index = 0;
        int bit_index = 0;
        if (number % 8 == 0) {
            index = (int) (number / 8 - 1);
            bit_index = 8;
        } else {
            index = (int) (number / 8);
            bit_index = (int) (number % 8);
        }
        int value = -1;
        switch (bit_index) {
            case 1:
                value = bitMap[index] & 0x01;
                break;
            case 2:
                value = bitMap[index] & 0x02;
                break;
            case 3:
                value = bitMap[index] & 0x04;
                break;
            case 4:
                value = bitMap[index] & 0x08;
                break;
            case 5:
                value = bitMap[index] & 0x10;
                break;
            case 6:
                value = bitMap[index] & 0x20;
                break;
            case 7:
                value = bitMap[index] & 0x40;
                break;
            case 8:
                value = bitMap[index] & 0x8;
                break;
        }
        return value != 0;
    }
}
