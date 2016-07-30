package com.cweeyii.algorithm.ipbacklist;


import com.cweeyii.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by cweeyii on 13/7/16 ${EMAIL}.
 */
public class IpListBitMap implements IpList {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpListBitMap.class);
    /*
    * 思路:先将IP地址,如:192.168.0.1 转化为整数, 之后用Bitmap存储这个数字
    * 将四个数字分别存储
     */
    private BitMap bitMap = null;

    private long pow(long base, int exp) {
        Double value = Math.pow(base, exp);
        return value.longValue();
    }

    public long ip2Long(String ip) {
        if (StringUtil.isNotBlank(ip)) {
            long ipNum = 0;
            String ips[] = ip.split("\\.");
            if (ips.length != 4) {
                throw new IllegalArgumentException("非法的Ip地址=[" + ip + "]");
            }
            for (int i = 0; i < ips.length; i++) {
                long k = Long.parseLong(ips[i]);
                ipNum = ipNum + k * pow(256, 3 - i);

            }
            return ipNum;
        }
        return -1;
    }

    public IpListBitMap(String fileName) {
        try {
            long size= pow(2,32);
            bitMap=new BitMap(size);
            readIpListFromFile(fileName);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void readIpListFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String ip;
        while ((ip = br.readLine()) != null) {
            putIp(ip);
        }
        br.close();
    }

    private void putIp(String ip) {
        Long iIp = ip2Long(ip);
        if (iIp >= 0) {
            bitMap.setLocation(iIp);
        }
    }

    private boolean exsitIp(String ip) {
        if (StringUtil.isNotBlank(ip)) {
            String ipStrs[] = ip.split("\\.");
            if (ipStrs.length != 4) {
                throw new IllegalArgumentException("非法的Ip地址=[" + ip + "]");
            }
            Long iIp = ip2Long(ip);
            return bitMap.getLocation(iIp);
        }
        return false;
    }

    @Override
    public boolean isInList(String ip) {
        return exsitIp(ip);
    }

    public static void main(String[] args) {
        String fileName = "/var/sankuai/data/ip.data";
        IpListBitMap bitMapIpList = new IpListBitMap(fileName);
        LOGGER.info("192.168.1.1="+bitMapIpList.isInList("192.168.1.1"));
        LOGGER.info("255.255.255.255="+bitMapIpList.isInList("255.255.255.255"));
        LOGGER.info("192.168.1.2="+bitMapIpList.isInList("192.168.1.2"));
        LOGGER.info("255.255.255.254="+bitMapIpList.isInList("255.255.255.254"));
    }
}
