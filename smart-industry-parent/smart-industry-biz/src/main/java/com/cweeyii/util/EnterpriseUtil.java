package com.cweeyii.util;

import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public class EnterpriseUtil {
    private static final Logger log = LoggerFactory.getLogger(EnterpriseUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public static String matchFirst(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 设置对象field的默认值(跳过id域及非空域)
     * <p/>
     * String = "", Integer=0, Long = 0, Boolean = false
     *
     * @param obj
     */
    public static void setDefault(Object obj) {
        // 先用反射吧，如果有效率问题再考虑优化
        try {
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(obj);
                // 跳过id，自增长主键
                if (!field.getName().equals("id") && object == null) {
                    if (field.getType().equals(String.class)) {
                        field.set(obj, "");
                    }
                    if (field.getType().equals(Long.class)) {
                        field.set(obj, 0L);
                    }
                    if (field.getType().equals(Integer.class)) {
                        field.set(obj, 0);
                    }
                    if (field.getType().equals(Byte.class)) {
                        field.set(obj, (byte) 0);
                    }
                    if (field.getType().equals(Boolean.class)) {
                        field.set(obj, false);
                    }
                    if (field.getType().equals(Date.class)) {
                        field.set(obj, new Date());
                    }
                    if (field.getType().equals(Float.class)) {
                        field.set(obj, 0f);
                    }
                    if (field.getType().equals(BigDecimal.class)) {
                        field.set(obj, BigDecimal.ZERO);
                    }
                    if (field.getType().equals(Double.class)) {
                        field.set(obj, 0.0d);
                    }
                    if (field.getType().equals(BigDecimal.class)) {
                        field.set(obj, new BigDecimal(0.0f));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void setDefault(Object obj, Object defaultObj) {
        // 先用反射吧，如果有效率问题再考虑优化
        try {
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(obj);
                // 跳过id，自增长主键
                if (object == null) {
                    java.lang.reflect.Field field1 = defaultObj.getClass().getDeclaredField(field.getName());
                    if (field1 == null) {
                        continue;
                    }
                    field1.setAccessible(true);
                    field.set(obj, field1.get(defaultObj));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 读取POST数据
     *
     * @param reader
     * @param length
     * @param returnType
     * @return
     */
    public static <T> T read(BufferedReader reader, int length, Class<T> returnType) {
        StringBuilder sb = new StringBuilder(length);
        try {
            char[] cache = new char[1024];
            int readed;
            while ((readed = reader.read(cache)) != -1) {
                sb.append(cache, 0, readed);
            }
            return mapper.readValue(sb.toString(), returnType);
        } catch (Exception e) {
            log.warn("read json param error,[jsonStr=" + sb + "]", e);
        }
        return null;
    }

    public static <T> T readObjectByReader(BufferedReader reader, TypeReference type) {
        try {
            return mapper.readValue(reader, type);
        } catch (IOException e) {
            log.error("parse json error!", e);
        }
        return null;
    }

    public static <T> T readObject(String json, TypeReference type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("parse json error!", e);
        }
        return null;
    }

    public static String read(BufferedReader reader, int length) {
        try {
            StringBuilder sb = new StringBuilder(length);
            char[] cache = new char[10];
            int readed;
            while ((readed = reader.read(cache)) != -1) {
                sb.append(cache, 0, readed);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 转换为字符串
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Object obj) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            return "null";
        }
        try {
            if (obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Integer
                    || obj instanceof Long || obj instanceof Boolean) {
                return obj.toString();
            }
            if (obj instanceof Map) {
                Map map = (Map) obj;
                return toString(map);
            } else if (obj instanceof Collection) {
                Collection cls = (Collection) obj;
                return toString(cls);
            } else if (obj instanceof Object[]) {
                for (Object o : (Object[]) obj) {
                    if (o.getClass().isPrimitive() || o instanceof String || o instanceof Integer
                            || o instanceof Long || o instanceof Boolean) {
                        sb.append(o + ",");
                    } else {
                        sb.append(toString(o) + ",");
                    }
                }
            }
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(obj);
                if (object != null) {
                    sb.append("\"" + field.getName() + "\":");
                    if (object instanceof String) {
                        sb.append("\"" + object + "\",");
                    } else {
                        sb.append(object + ",");
                    }
                }
            }
            return sb.length() == 0 ? "{}" : "{" + sb.substring(0, sb.length() - 1) + "}";
        } catch (Exception e) {
            return obj.toString();
        }
    }

    /**
     * 集合
     *
     * @param objs
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Collection objs) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (objs == null) {
            return "null";
        }
        for (Object obj : objs) {
            if (obj.getClass().isPrimitive()) {
                sb.append(obj + ",");
            } else {
                sb.append(toString(obj) + ",");
            }
        }
        return sb.length() == 0 ? "[]" : "[" + sb.substring(0, sb.length() - 1) + "]";
    }

    /**
     * Map
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Map map) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (map == null) {
            return "null";
        }
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            sb.append("\"" + toString(key) + "\":" + toString(value) + ",");
        }
        return sb.length() == 0 ? "{}" : "{" + sb.substring(0, sb.length() - 1) + "}";
    }

    /**
     * 根据名字取params中第一个满足条件的值
     *
     * @param params
     * @param name
     * @return
     */
    public static String getFirstValueByName(Map<String, List<String>> params, String name) {
        List<String> values = params == null ? null : params.get(name);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    public static Integer toInteger(String s, Integer defaultValue) {
        return (s == null || s.isEmpty()) ? defaultValue : Integer.valueOf(s);
    }

    public static Boolean toBoolean(String s, Boolean defaultValue) {
        return (s == null || s.isEmpty()) ? defaultValue : Boolean.parseBoolean(s);
    }

    /**
     * 将目标list转换为List<Integer>，目标list内部String为以,分割的数字
     *
     * @param list
     * @return
     */
    public static List<Integer> toIntegerList(List<String> list) {
        List<Integer> ids = new ArrayList<Integer>();
        if (list == null) {
            return ids;
        }
        for (String s : list) {
            ids.addAll(toIntegerList(s));
        }
        return ids;
    }

    public static List<Integer> toIntegerList(String idString) {
        List<Integer> ids = new ArrayList<Integer>();
        String[] strs = idString.split(",");
        for (String s : strs) {
            if (!StringUtil.isBlank(s) && StringUtil.isNumeric(s)) {
                ids.add(Integer.valueOf(s));
            }
        }
        return ids;
    }

    /**
     * @param list
     * @return
     */
    public static int[] toPrimitive(List<Integer> list) {
        if (list != null) {
            list.removeAll(Arrays.asList(new Integer[]{null}));
            return ArrayUtils.toPrimitive(list.toArray(new Integer[]{}));
        }
        return null;
    }

    /**
     * @param list
     * @return
     */
    public static long[] toPrimitiveFromLong(List<Long> list) {
        if (list != null) {
            list.removeAll(Arrays.asList(new Integer[]{null}));
            return ArrayUtils.toPrimitive(list.toArray(new Long[]{}));
        }
        return null;
    }

    /**
     * @param cls
     * @return
     */
    public static boolean isEmpty(Collection<?> cls) {
        return cls == null || cls.isEmpty();
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @param obj
     * @return
     */
    public static Object getId(Object obj) {
        try {
            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("getId");
            return method.invoke(obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param obj
     * @param integer
     */
    public static void resetId(Object obj, Integer integer) {
        try {
            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("setId",
                    Integer.class);
            method.invoke(obj, integer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param obj
     * @return
     */
    public static <T> T clone(T obj) {
        try {
            @SuppressWarnings("unchecked")
            T clone = (T) obj.getClass().newInstance();
            BeanUtils.copyProperties(obj, clone);
            return clone;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 是否为0
     *
     * @param value
     * @return
     */
    public static boolean isZero(Integer value) {
        return value == null || value == 0;
    }

    public static boolean isZero(Long value) {
        return value == null || value == 0;
    }

    public static boolean isZero(Double value) {
        return value == null || value == 0;
    }

    public static String toString(Object[] objs, String delimit) {
        if (objs == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : objs) {
            sb.append(object + delimit);
        }
        return sb.toString().isEmpty() ? "" : sb.substring(0, sb.length() - 1);
    }

    /**
     * 将数组转为可移除的List
     *
     * @param array
     * @return
     */
    public static <T> List<T> toRemovableList(T[] array) {
        List<T> list = new ArrayList<T>();
        list.addAll(Arrays.asList(array));
        return list;
    }

    public static String ConvertArrayToString(String[] reason) {
        StringBuilder comment = new StringBuilder();
        if (reason != null) {
            for (String s : reason) {
                if (!s.equals("")) {
                    comment.append(s);
                    comment.append(",");
                }
            }
        }
        if (comment.lastIndexOf(",") > 0) {
            comment.deleteCharAt(comment.lastIndexOf(","));
        }
        return comment.toString();
    }

    /**
     * @param left
     * @param right
     * @return
     */
    public static boolean equals(Set<Integer> left, Set<Integer> right) {
        if (left == null) {
            if (right != null) {
                return false;
            }
        } else if (right == null || left.size() != right.size()) {
            return false;
        } else {
            List<Integer> sort1 = new ArrayList<Integer>(left);
            List<Integer> sort2 = new ArrayList<Integer>(right);
            Collections.sort(sort1);
            Collections.sort(sort2);
            for (int i = 0; i < left.size(); i++) {
                if (!sort1.get(i).equals(sort2.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Double getDistance(double firstLat, double firstLng, double secondLat,
                                     double secondLng) {
        Double s = 0.0;
        Double firstlatRad = rad(firstLat);
        Double firstlngRad = rad(firstLng);
        Double secondlatRad = rad(secondLat);
        Double secondlngRad = rad(secondLng);

        Double latResult = firstlatRad - secondlatRad;
        Double lngResult = firstlngRad - secondlngRad;
        // s = Math.sin(Math.sqrt(Math.pow(Math.sin(latResult / 2), 2) +
        // Math.cos(firstlatRad)
        // * Math.cos(secondlatRad) * Math.pow(lngResult, 2)));
        s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latResult / 2), 2) + Math.cos(firstlatRad)
                * Math.cos(secondlatRad) * Math.pow(Math.sin(lngResult / 2), 2)));
        s = s * 6378137; // 地球半径 6378.137
        return s;

    }

    public static Double getDistance(int firstLat, int firstLng, int secondLat, int secondLng) {
        Double s = 0.0;
        Double firstlatRad = rad(firstLat / 1000000d);
        Double firstlngRad = rad(firstLng / 1000000d);
        Double secondlatRad = rad(secondLat / 1000000d);
        Double secondlngRad = rad(secondLng / 1000000d);

        Double latResult = firstlatRad - secondlatRad;
        Double lngResult = firstlngRad - secondlngRad;
        s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latResult / 2), 2) + Math.cos(firstlatRad)
                * Math.cos(secondlatRad) * Math.pow(Math.sin(lngResult / 2), 2)));
        s = s * 6378137; // 地球半径 6378.137
        return s;

    }

    // 弧度转换
    private static double rad(Double location) {
        return location * Math.PI / 180.0;
    }


    /**
     * @param tmp
     * @return
     */
    public static List<Integer> toIntList(Collection<Long> tmp) {
        List<Integer> ids = new ArrayList<Integer>();
        for (Long id : tmp) {
            ids.add(id.intValue());
        }
        return ids;
    }

    public static String getMD5code(String m) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        // MD5 验证
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(m.getBytes("utf-8"));
        byte[] array = md5.digest();
        return byteArrayToHexString(array);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 提取商家名，分店名
     *
     * @param pointName
     * @return
     */
    public static Pair<String, String> matchName(String pointName) {
        if (StringUtil.isBlank(pointName)) {
            return new Pair<String, String>("", "");
        }
        String regx = "(.*)[\\(\\[（【](.*)[\\)\\]）】](.*)";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(StringUtil.alltrimWithWideSpace(pointName));
        if (matcher.matches()) {
            String poiName = matcher.group(1).trim();
            String branchName = matcher.group(2).trim();
            return new Pair<String, String>(poiName, branchName);
        }
        return new Pair<String, String>(pointName.trim(), "");
    }

    private static Random rand = new Random(System.currentTimeMillis());

    public static <T> T random(List<T> objs) {
        if (objs == null & objs.isEmpty()) {
            return null;
        }
        int index = rand.nextInt(objs.size());
        return (T) objs.get(index);
    }


    private static boolean isInvaliadPhone(String phone) {
        return StringUtil.isBlank(phone) || !StringUtil.trimWithWideSpace(phone).matches("(.*)\\d{5}(.*)");
    }

    public static float calPhoneSim(String phone1, String phone2) {
        if (StringUtil.isBlank(phone2) || StringUtil.isBlank(phone1)) {
            return 0f;
        }
        //modified by wangshuai06 2015-7-20 判断是否为电话。如果不是电话，则直接return 0;
        if (isInvaliadPhone(phone1) || isInvaliadPhone(phone2)) {
            return 0f;
        }
        // 格式转换1.分隔符2.短杠3.区号
        phone1 = phone1.replaceAll(",", "/");
        phone1 = phone1.replaceAll("，", "/");
        phone1 = phone1.replaceAll("；", "/");
        phone1 = phone1.replaceAll(";", "/");
        phone1 = phone1.replaceAll(" ", "/");
        phone1 = phone1.replaceAll("—", "-");

        phone1 = phone1.replaceAll(",", "/");
        phone1 = phone1.replaceAll("，", "/");
        phone1 = phone1.replaceAll("；", "/");
        phone1 = phone1.replaceAll(";", "/");
        phone1 = phone1.replaceAll(" ", "/");
        phone1 = phone1.replaceAll("—", "-");
        //modify by wangshuai06 增加竖线分隔 竖线没有全角半角的区分
        phone1 = phone1.replaceAll("\\|", "/");

        phone2 = phone2.replaceAll(",", "/");
        phone2 = phone2.replaceAll("，", "/");
        phone2 = phone2.replaceAll("；", "/");
        phone2 = phone2.replaceAll(";", "/");
        phone2 = phone2.replaceAll(" ", "/");
        phone2 = phone2.replaceAll("—", "-");
        //modify by wangshuai06 增加竖线分隔 竖线没有全角半角的区分
        phone2 = phone2.replaceAll("\\|", "/");

        String[] phones1 = phone1.split("/");
        String[] phones2 = phone2.split("/");
        Set<String> phoneSet = new HashSet<String>();
        for (String item : phones1) {
            item = item.trim();
            if (item.startsWith("400") || item.startsWith("800")) {
                item = item.replaceAll("-", "");
            }
            if (item.startsWith("0")) {
                item = item.substring(1, item.length());
            }
            String[] items = item.split("-");
            phoneSet.add(item);
            phoneSet.add(item.replaceAll("-", ""));
            for (String p : items) {
                if (p.length() > 5 && p.indexOf("000000") == -1) {
                    phoneSet.add(p.trim());
                }
            }
        }
        for (String item : phones2) {
            item = item.trim();
            if (item.startsWith("400") || item.startsWith("800")) {
                item = item.replaceAll("-", "");
            }
            if (item.startsWith("0")) {
                item = item.substring(1, item.length());
            }
            String[] items = item.trim().split("-");
            for (String p : items) {
                if (p.length() > 5) {
                    if (phoneSet.contains(p.trim())) {
                        return 1f;
                    }
                }
            }
            if (phoneSet.contains(item) || phoneSet.contains(item.replaceAll("-", ""))) {
                return 1f;
            }
        }
        return 0f;
    }

    public static boolean isContainsChinese(String str) {
        if (!StringUtil.isBlank(str)) {
            char[] chars = str.toCharArray();
            for (char strChar : chars) {
                if (isChinese(strChar)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasImbaWords(String keyword) {
        if (keyword.indexOf("体育馆") > 0 || keyword.indexOf("游泳馆") > 0 || keyword.indexOf("博物馆") > 0 || keyword.indexOf("大学") > 0 || keyword.indexOf("大厦") > 0 || keyword.indexOf("中餐厅") > 0 || keyword.indexOf("宴会厅") > 0 || keyword.indexOf("堂吧") > 0 || keyword.indexOf("酒店") > 0 || keyword.indexOf("饭店") > 0 || keyword.indexOf("食堂") > 0 || keyword.indexOf("招待所") > 0 || keyword.indexOf("宾馆") > 0 || keyword.indexOf("咖啡厅") > 0 || keyword.indexOf("西餐厅") > 0) {
            return true;
        }
        return false;
    }

    public static boolean hasSimilarBranch(String base, String target) {
        if (base.endsWith("一店") || base.endsWith("1店")) {
            return target.endsWith("一店") || target.endsWith("1店") ? true : false;
        }
        if (base.endsWith("二店") || base.endsWith("2店")) {
            return target.endsWith("二店") || target.endsWith("2店") ? true : false;
        }
        if (base.endsWith("三店") || base.endsWith("3店")) {
            return target.endsWith("三店") || target.endsWith("3店") ? true : false;
        }
        if (target.endsWith("一店") || target.endsWith("1店")) {
            return base.endsWith("一店") || base.endsWith("1店") ? true : false;
        }
        if (target.endsWith("二店") || target.endsWith("2店")) {
            return base.endsWith("二店") || base.endsWith("2店") ? true : false;
        }
        if (target.endsWith("三店") || target.endsWith("3店")) {
            return base.endsWith("三店") || base.endsWith("3店") ? true : false;
        }
        return true;

    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String toJsonString(Object obj) {
        try {
            return JSONObject.toJSONString(obj);
        } catch (Exception e) {
            log.error("转换Json失败[obj=" + obj + "]", e);
            return null;
        }

    }

    /**
     * 求两个字符串的最大公共子串
     */
    public static String comSubstring(String s1, String s2) {
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()) {
            return "";
        }

        int len1 = s1.length();
        int len2 = s2.length();

        int[][] match = new int[len1][len2];
        int maxLength = 0; // 子字符串的最大长度
        int lastIndex = 0; // 最大子字符串中最后一个字符的索引

        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {

                if (s2.charAt(j) == s1.charAt(i)) {
                    if (i > 0 && j > 0 && match[i - 1][j - 1] != 0) {
                        match[i][j] = match[i - 1][j - 1] + 1;
                    } else {
                        match[i][j] = 1;
                    }

                    if (match[i][j] > maxLength) {
                        maxLength = match[i][j];
                        lastIndex = i;
                    }
                } else {
                    match[i][j] = 0;
                }
            }
        }

//        // 这里打印出构造出的矩阵
//        for (int i = 0; i < len1; i++) {
//            for (int j = 0; j < len2; j++) {
//                System.out.print(match[i][j] + " ");
//            }
//            System.out.println();
//        }

        if (maxLength == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        // 根据最大索引的位置，回朔出最长子字符串
        for (int i = lastIndex - maxLength + 1; i <= lastIndex; i++) {
            sb.append(s1.charAt(i));
        }

        return sb.toString();
    }

    private static final String[] PARKING_INFO_DELETE_WORDS = new String[]{"各店", "各个店面", "门店", "分店", "咨询商家", "实际情况", "致电商家", "详询",
            "不同", "不一样", "停车位情况", "车位问题", "以商家", "有无", "为准", "反馈"};

    private static final String[] PARKING_INFO_REMAIN_WORDS = new String[]{"提供停车位", "提供免费停车位", "停车便利", "收费停车位", "有停车位", "免费", "停车方便", "地下"};

    /**
     * 格式化停车位信息
     */
    public static String filterParkingInfo(String parkingInfo) {
        if (!StringUtil.isBlank(parkingInfo)) {
            for (String deleteWord : PARKING_INFO_DELETE_WORDS) {
                if (parkingInfo.indexOf(deleteWord) > -1) {
                    for (String ramainWord : PARKING_INFO_REMAIN_WORDS) {
                        if (parkingInfo.indexOf(ramainWord) < 0) {
                            log.info("停车位被清空[parkingInfo=" + parkingInfo + "]");
                            return "";
                        }
                    }
                }
            }
        }
        return StringUtil.trimWithWideSpace(parkingInfo);
    }

    /**
     * 格式化营业时间
     */
    public static String formatOpenInfo(String openInfo) {
        if (!StringUtil.isBlank(openInfo)) {
            //中文冒号替换成英文冒号
            openInfo = openInfo.replaceAll("：", ":");
            //消除空格
            openInfo = openInfo.replaceAll(" ", "");
            //分隔符处理
            openInfo = openInfo.replaceAll("([～|~|—|-])+", "-").replaceAll("(:)+", ":");
        }
        return StringUtil.trimWithWideSpace(openInfo);
    }


    /**
     * 将一个列表等分为num个子列表，返回二维列表
     *
     * @param list 数据源
     * @param num  将一个list等分为num个小list
     * @param <T>  泛型
     * @return 分片后的二维list
     */
    public static <T> List<List<T>> shardList(List<T> list, int num) {
        List<List<T>> result = new ArrayList<>();
        if (num <= 0) {
            throw new IllegalArgumentException("num必须大于0");
        }
        if (list != null && !list.isEmpty()) {
            int total = list.size();
            int per = total / num == 0 ? total / num + 1 : total / num;
            int curIndex = 0;
            for (int i = 0; i < num; i++) {
                int end = curIndex + per;
                List<T> subList = new ArrayList<>();
                if (curIndex >= total) {
                    result.add(subList);
                } else if (end > total) {
                    result.add(subList);
                } else {
                    //最后一个元素包含所有
                    if ((i == num - 1 && end < total)) {
                        end = list.size();
                    }
                    subList = new ArrayList<>();
                    int pageSize = end - curIndex;
                    for (int j = 0; j < pageSize; j++) {
                        subList.add(list.get(j + curIndex));
                    }
                    result.add(subList);
                    curIndex = end;
                }
            }
        }
        return result;
    }


    /**
     * 将一个列表分页成为若干个子列表，每个列表的大小为perNum，返回二维列表
     *
     * @param list   数据源
     * @param perNum 每个子列表的最大长度
     * @param <T>    泛型
     * @return 分片后的二维列表
     */
    public static <T> List<List<T>> shardListByPerNum(List<T> list, int perNum) {
        List<List<T>> result = new ArrayList<>();
        if (perNum <= 0) {
            throw new IllegalArgumentException("perNum必须大于0");
        }
        if (list != null && !list.isEmpty()) {
            int total = list.size() / perNum + 1;
            for (int i = 0; i < total; i++) {
                if (i == total - 1) {
                    //list为perNum的整数倍，反正添加size=0的list
                    if (i * perNum == list.size()) {
                        break;
                    }
                    result.add(list.subList(i * perNum, list.size()));
                } else {
                    result.add(list.subList(i * perNum, i * perNum + perNum));
                }
            }
        }
        return result;
    }

    public static boolean needReview(int rate) {
        Random random = new Random(System.currentTimeMillis());
        int num = random.nextInt(100);
        return num < rate ? true : false;
    }


    public static final String NO_WAIT = "No-Wait-Result";


    /**
     * 用于记录应该设置not_wait_result，但是没有设置not_wait_result的情况
     */
    public static void logIsNotWaitResult(HttpServletRequest request) {
        String result = request.getHeader(NO_WAIT);
        if (!isNotWaitResult(request)) {
            log.info("[url=" + request.getRequestURL() + "][" + NO_WAIT + "=" + result + "]");
        }
    }

    public static boolean isNotWaitResult(HttpServletRequest request) {
        String result = request.getHeader(NO_WAIT);
        return "true".equals(StringUtil.alltrimWithWideSpace(result));
    }


    public static <T> void setObjectValueByFields(T obj, Map<String, Object> fieldValueMap) {
        try {
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(obj);
                if (fieldValueMap.containsKey(field.getName()) && fieldValueMap.get(field.getName()) != null) {
                    field.set(obj, fieldValueMap.get(field.getName()));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void mergeObject(Object srcObj, Object dstObj, List<String> fields) {
        if (srcObj == null || dstObj == null || CollectionUtils.isEmpty(fields)) {
            throw new IllegalArgumentException("参数非法");
        }
        try {
            for (String fieldStr : fields) {
                if (srcObj.getClass().getDeclaredField(fieldStr) != null && dstObj.getClass().getDeclaredField(fieldStr) != null) {
                    Field field = srcObj.getClass().getDeclaredField(fieldStr);
                    field.setAccessible(true);
                    field.set(dstObj, field.get(srcObj));
                }
            }
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException("字段不存在！", ex.getCause());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Map<String, Object> getFieldMap(Object obj, List<String> fields) {
        Map<String, Object> fieldMap = new HashMap<>();
        if (obj == null || CollectionUtils.isEmpty(fields)) {
            throw new IllegalArgumentException("参数非法");
        }
        try {
            for (String fieldStr : fields) {
                if (obj.getClass().getDeclaredField(fieldStr) != null) {
                    Field field = obj.getClass().getDeclaredField(fieldStr);
                    field.setAccessible(true);
                    Object fieldObject = field.get(obj);
                    fieldMap.put(field.getName(), fieldObject);
                }
            }
            return fieldMap;
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException("对象：" + obj.getClass().getSimpleName() + "；参数：" + fields, ex.getCause());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return fieldMap;
    }

    public static List<String> getAllFields(Object obj) {
        List<String> fields = new ArrayList<>();
        try {
            if (obj instanceof Class) {
                for (java.lang.reflect.Field field : ((Class) obj).getDeclaredFields()) {
                    if (!field.getName().equals("serialVersionUID")) {
                        fields.add(field.getName());
                    }
                }
            } else {
                for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                    if (!field.getName().equals("serialVersionUID")) {
                        fields.add(field.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return fields;
    }

    /**
     * 返回min到max之间的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static String trimAllPunctuation(String str) {
        return str.trim().replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\（", "").
                replaceAll("\\）", "").replaceAll("\\~", "").replaceAll("\\【", "").replaceAll("\\】", "").replaceAll(" ", "").replaceAll("\\_", "");
    }

    private static final Map<String, BeanCopier> COPIER_MAP = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 根据param更新目标，当且仅当字段存在且非null时更新目标值
     */
    public static void copyProperties(Object src, Object dst) {
        if (src == null || dst == null) {
            return;
        }
        try {
            String key = src.getClass().getName() + "-" + dst.getClass().getName();
            BeanCopier coper = COPIER_MAP.get(key);
            if (coper == null) {
                synchronized (EnterpriseUtil.class) {
                    if (coper == null) {
                        coper = BeanCopier.create(src.getClass(), dst.getClass(), false);
                        COPIER_MAP.put(key, coper);
                    }
                }
            }
            // modify by wangshuai BeanUtil.copyProperties(src, dst);效率比较低
            coper.copy(src, dst, null);
//            log.debug(PoiUtil.toString(dst));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /*add by caowenyi 动态规划求解，两个字符串之间的编辑距离*/
    public static int editDistance(String firstStr, String secondStr) {
        if (firstStr == null || secondStr == null) {
            return -1;
        }
        int distance[][];
        int firstLen = firstStr.length();
        int secondLen = secondStr.length();
        if (firstLen == 0) {
            return secondLen;
        }
        if (secondLen == 0) {
            return firstLen;
        }
        distance = new int[firstLen + 1][secondLen + 1];
        for (int i = 0; i <= firstLen; i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= secondLen; j++) {
            distance[0][j] = j;
        }
        char ch1, ch2;
        int temp;
        for (int i = 1; i <= firstLen; i++) {
            ch1 = firstStr.charAt(i - 1);
            for (int j = 1; j <= secondLen; j++) {
                ch2 = secondStr.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), distance[i - 1][j - 1] + temp);

            }
        }
        return distance[firstLen][secondLen];
    }

    public static double edSim(String firstStr, String secondStr) {
        if (firstStr == null || secondStr == null) {
            return -1;
        }
        int ed = editDistance(firstStr, secondStr);
        if (firstStr.length() == 0 && secondStr.length() == 0) {
            return 1;
        }
        return 1 - (double) ed / Math.max(firstStr.length(), secondStr.length());
    }

    /**
     * 判断字符串是否是乱码
     */
    public static boolean isMessyCode(String strName, double messyWeight) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > messyWeight) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串电话号码是否正确
     */
    public static boolean isGoodPhoneNumber(String phoneNumber) {
        if (StringUtil.isBlank(phoneNumber)) {
            return false;
        }
        String phoneString = phoneNumber.replaceAll("[\\(（].+[\\)）]", "").replaceAll("[\\u4e00-\\u9fa5a-zA-Z]+", "");
        if (StringUtil.isBlank(phoneString)) {
            return false;
        }
        // 格式转换1.分隔符2.短杠3.区号
        phoneString = phoneString.replaceAll(",", "/");
        phoneString = phoneString.replaceAll("；", "/");
        phoneString = phoneString.replaceAll(";", "/");
        phoneString = phoneString.replaceAll(" ", "/");
        phoneString = phoneString.replaceAll("—", "-");
        phoneString = phoneString.replaceAll("\\|", "/");
        String[] phones = phoneString.split("/");
        if (phones == null || phones.length <= 0) {
            return false;
        }
        for (String phone : phones) {
            phone = phone.replaceAll("[^0-9]", "");
            if (!isServePhone(phone) && !isCellPhone(phone) && !isLandPhone(phone)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isServePhone(String phone) {
        phone = phone.replace("-", "");
        if (phone.startsWith("400") || phone.startsWith("800")) {
            return true;
        }
        return false;
    }

    private static boolean isCellPhone(String phone) {
        if (phone.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")) {
            return true;
        }
        return false;
    }

    private static boolean isLandPhone(String phone) {
        phone = phone.replace("-", "");
        Pattern pattern = Pattern.compile("^(0[0-9]{2,3})?([2-9][0-9]{6,7})([0-9]{1,4})?$");
        Matcher m = pattern.matcher(phone);
        return m.matches();
    }

    public static double formatNum(double num) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(num));
    }
}

