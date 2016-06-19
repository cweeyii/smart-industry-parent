//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cweeyii.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public StringUtil() {
    }

    public static final String alltrimWithWideSpace(String str) {
        if(str == null) {
            return "";
        } else {
            char[] chArray = str.toCharArray();
            int first = -1;
            int last = -1;

            int sb;
            char i;
            for(sb = 0; sb < chArray.length; ++sb) {
                i = chArray[sb];
                if(!Character.isWhitespace(i) && !Character.isSpaceChar(i)) {
                    first = sb;
                    break;
                }
            }

            for(sb = chArray.length - 1; sb >= 0; --sb) {
                i = chArray[sb];
                if(!Character.isWhitespace(i) && !Character.isSpaceChar(i)) {
                    last = sb;
                    break;
                }
            }

            if(first != -1 && last != -1 && last >= first) {
                StringBuilder var7 = new StringBuilder();

                for(int var8 = first; var8 <= last; ++var8) {
                    char ch = chArray[var8];
                    if(!Character.isWhitespace(ch) && !Character.isSpaceChar(ch)) {
                        var7.append(ch);
                    }
                }

                return var7.toString();
            } else {
                return "";
            }
        }
    }

    public static final String ltrimWithWideSpace(String str) {
        return rawTrimWithWideSpace(str, true, false);
    }

    public static final String rtrimWithWideSpace(String str) {
        return rawTrimWithWideSpace(str, false, true);
    }

    public static final String trimWithWideSpace(String str) {
        return rawTrimWithWideSpace(str, true, true);
    }

    public static final String rawTrimWithWideSpace(String str, boolean left, boolean right) {
        if(str == null) {
            return "";
        } else {
            char[] chArray = str.toCharArray();
            int first = -1;
            int last = -1;
            int i;
            char ch;
            if(!left) {
                first = 0;
            } else {
                for(i = 0; i < chArray.length; ++i) {
                    ch = chArray[i];
                    if(!Character.isWhitespace(ch) && !Character.isSpaceChar(ch)) {
                        first = i;
                        break;
                    }
                }
            }

            if(!right) {
                last = chArray.length - 1;
            } else {
                for(i = chArray.length - 1; i >= 0; --i) {
                    ch = chArray[i];
                    if(!Character.isWhitespace(ch) && !Character.isSpaceChar(ch)) {
                        last = i;
                        break;
                    }
                }
            }

            return first != -1 && last != -1 && last >= first?String.valueOf(chArray, first, last - first + 1):"";
        }
    }

    public static final String null2Trim(String str) {
        return str == null?"":str.trim();
    }

    public static final InputStream str2InputStream(String str) {
        if(!null2Trim(str).equals("")) {
            ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
            return stream;
        } else {
            return null;
        }
    }

    public static final String[] sort(String[] strArray) {
        if(strArray == null) {
            return null;
        } else {
            String tmp = "";

            for(int i = 0; i < strArray.length; ++i) {
                for(int j = 0; j < strArray.length - i - 1; ++j) {
                    if(strArray[j].compareTo(strArray[j + 1]) < 0) {
                        tmp = strArray[j];
                        strArray[j] = strArray[j + 1];
                        strArray[j + 1] = tmp;
                    }
                }
            }

            return strArray;
        }
    }

    public static final String iso2gbk(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("iso-8859-1"), "GBK");
    }

    public static final String gbk2Iso(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("GBK"), "iso-8859-1");
    }

    public static final String gbk2Utf8(String str) {
        try {
            return new String(str.getBytes("GBK"), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return str;
        }
    }

    public static final String utf82Gbk(String str) {
        try {
            return new String(str.getBytes("UTF-8"), "GBK");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return str;
        }
    }

    public static final boolean regexMatch(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static final boolean regexMatchInsensitive(String str, String regx) {
        Pattern pattern = Pattern.compile(regx, 2);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static final String regexReplaceAll(String str, String regx, String replaceStr) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll(replaceStr);
    }

    public static final String subStringFromStartByByteNum(String symbol, int len) throws UnsupportedEncodingException {
        int counterOfDoubleByte = 0;
        byte[] b = symbol.getBytes("GBK");
        if(b.length <= len) {
            return symbol;
        } else {
            for(int i = 0; i < len; ++i) {
                if(b[i] < 0) {
                    ++counterOfDoubleByte;
                }
            }

            if(counterOfDoubleByte % 2 == 0) {
                return new String(b, 0, len, "GBK");
            } else {
                return new String(b, 0, len - 1, "GBK");
            }
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String subNormString(String str) {
        return str == null?"":str.replaceAll("<[.[^<]]*>", "").replace("<", "");
    }

    public static String subNormString(String str, int beginIndex, int endIndex) {
        if(str == null) {
            return "";
        } else {
            str = str.replaceAll("<[.[^<]]*>", "");
            int length = str.length();
            endIndex = endIndex < 0?0:endIndex;
            endIndex = endIndex > length?length:endIndex;
            beginIndex = beginIndex < 0?0:beginIndex;
            beginIndex = beginIndex > endIndex?endIndex:beginIndex;
            str = str.substring(beginIndex, endIndex);
            return str.replace("<", "");
        }
    }

    public static String replaceNull(String str) {
        return str == null?"":str;
    }

    public static String replaceNull(String str, String defaultStr) {
        return str == null?defaultStr:str;
    }

    public static String substring(String str, int beginIndex, int endIndex) {
        if(str == null) {
            return "";
        } else {
            int length = str.length();
            endIndex = endIndex < 0?0:endIndex;
            endIndex = endIndex > length?length:endIndex;
            beginIndex = beginIndex < 0?0:beginIndex;
            beginIndex = beginIndex > endIndex?endIndex:beginIndex;
            str = str.substring(beginIndex, endIndex);
            return str;
        }
    }

    public static int parseInt(Object o, int defaultint) {
        try {
            return Integer.parseInt(String.valueOf(o));
        } catch (NumberFormatException var3) {
            return defaultint;
        }
    }

    public static boolean isBlank(Object obj) {
        if(obj == null) {
            return true;
        } else {
            String e;
            if(obj instanceof String) {
                e = (String)obj;
                return e == null?true:"".equals(e.trim());
            } else {
                try {
                    e = String.valueOf(obj);
                    return e == null?true:"".equals(e.trim());
                } catch (Exception var2) {
                    return true;
                }
            }
        }
    }

    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    public static boolean isValid(Object obj) {
        if(obj == null) {
            return false;
        } else {
            String e;
            if(obj instanceof String) {
                e = (String)obj;
                return !"".equals(e.trim());
            } else if(obj instanceof Integer) {
                Integer e1 = (Integer)obj;
                return !e1.equals(Integer.valueOf(0));
            } else {
                try {
                    e = String.valueOf(obj);
                    return !"".equals(e.trim());
                } catch (Exception var2) {
                    return false;
                }
            }
        }
    }

    public static boolean isInvalid(Object obj) {
        return !isValid(obj);
    }

    public static String changeNull(String dest, String str) {
        return !isBlank(dest)?dest:str;
    }

    public static String changeNull(Object dest, String str) {
        return !isBlank(dest)?String.valueOf(dest):str;
    }

    public static String escapeString(String in) {
        if(in == null) {
            return null;
        } else {
            StringBuilder out = new StringBuilder();

            for(int i = 0; in != null && i < in.length(); ++i) {
                char c = in.charAt(i);
                if(c == 10) {
                    out.append("<br/>");
                } else if(c == 39) {
                    out.append("&#039;");
                } else if(c == 34) {
                    out.append("&#034;");
                } else if(c == 60) {
                    out.append("&lt;");
                } else if(c == 62) {
                    out.append("&gt;");
                } else {
                    out.append(c);
                }
            }

            return out.toString();
        }
    }

    public static String unescapeString(String in) {
        return in == null?null:in.replace("<br/>", "\n").replace("&nbsp;", " ").replace("&#039;", "\'").replace("&#034;", "\"").replace("&lt;", "<").replace("&gt;", ">");
    }

    public static String list2SqlString(List<?> list) {
        if(list.size() == 0) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            int i = 0;

            for(int n = list.size(); i < n; ++i) {
                Object obj = list.get(i);
                if(obj instanceof Integer) {
                    out.append(obj + ",");
                } else if(obj instanceof String) {
                    out.append("\'" + obj + "\',");
                } else {
                    out.append("\'" + obj.toString() + "\',");
                }
            }

            return out.substring(0, out.length() - 1);
        }
    }

    public static String list2String(List<?> list, String delimit) {
        if(list.size() == 0) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            int i = 0;

            for(int n = list.size(); i < n; ++i) {
                Object obj = list.get(i);
                out.append(obj.toString() + delimit);
            }

            return out.substring(0, out.length() - delimit.length());
        }
    }

    public static String valueOf(Object obj) {
        return obj == null?null:String.valueOf(obj);
    }
}
