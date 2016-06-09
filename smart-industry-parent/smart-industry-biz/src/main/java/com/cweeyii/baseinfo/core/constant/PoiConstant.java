package com.cweeyii.baseinfo.core.constant;

import java.util.Arrays;
import java.util.Collection;

public class PoiConstant {
    public static final String OP_PROJECT = "MDCOP";
    public static String MDCOP_SYS_ACCOUNT = "system";
    public static final int MOVIE_TYPE = 52;
    public static final int CLAIM_TIMEOUT = 1800;
    public static final int MAX_ABANDON = 2;
    public static final int QA_RATE = 5;
    public static final int LOW_RATE = 40;
    public static final int MAX_DISTANCE = 90;
    public static final int CONFIDENCE_N = 100;

    public static final String MD5_MEITUAN_CODE = "43e936102090e926";

    public static final String TRIVIAL = "Trivial";
    public static final String MINOR = "Minor";
    public static final String MAJOR = "Major";
    public static final String CRITICAL = "Critical";
    public static final String BLOCKER = "Blocker";

    public static final int TRIVIAL_INT = 1;
    public static final int MINOR_INT = 2;
    public static final int MAJOR_INT = 3;
    public static final int CRITICAL_INT = 4;
    public static final int BLOCKER_INT = 5;

    public static final String RESOLUTION_FIXED = "Fixed";
    public static final String RESOLUTION_WONT_FIX = "Won't Fix";
    public static final String RESOLUTION_DUPLICATE = "Duplicate";
    public static final String RESOLUTION_DONE = "Done";
    public static final String RESOLUTION_TERMINATION = "Task Termination";

    public static final String APP_KEY = "mtpoiop";

    public static final double LANDMARK_DISTANCE = 3000.0;
    public static final double SUBWAY_DISTANCE = 1000.0;
    /**
     * 酒店品类ID
     */
    public static final Collection<Integer> HOTEL_TYPE_IDS = Arrays.asList(166, 165, 213, 215, 164, 216, 209);

    public interface Fields {

        public static String ADDRESS = "address";

        public static String BAREAID = "bareaId";

        public static String LOCATIONID = "locationId";

        public static String LATLNG = "latLng";

        public static String PHONE = "phone";

        public static String POINTNAME = "pointName";

        public static String TYPEID = "typeId";

        public static String CLOSESTATUS = "closeStatus";

        public static String OPENDATE = "openDate";
    }

    public interface  Queue {

        public interface PhoneCheck {

            //新单
            public static Integer NEW_DEAL = 29;
            //即将上单优先级
            Integer DEAL_ACC_PRIORITY=10;
            //报错反馈优先级
            Integer ERR_ACC_PRIORITY=2;
            //营业状态变更优先级
            Integer STATUS_ACC_PRIORITY=3;
            //点评报错反馈优先级
            Integer DIANPING_ACC_PRIORITY=1;

            //门店状态变更
            public static Integer CLOSE_STATUS = 77;

            //校准报错
            public static Integer FEED_BACK_FAULT = 31;

            //电话校准队列
            public static Integer SIMPLE_PHONE_CHECK = 53;

            //moma坐标定位
            public static Integer MOMA_LOCATION = 81;

            //未消费退款疑似倒闭门店
            public static Integer SUSPECT_CLOSED_BY_REFUND = 121;

            //非完全正确报错校准
            public static Integer NOT_ALL_PASS_FEED_BACK = 129;



        }

        public interface Appeal {

            public static Integer OPERATOR_APPEAL = 104;
        }

    }

    public interface DateFormat {

        public static String DATE_STRING = "yyyy-mm-dd";

    }

    public interface AppReport {

        public static Integer ERROR_PHONE= 1;

        public static Integer ERROR_MAP = 1;

        public static String ERROR_PHONE_STR="电话错误";

        public static String ERROR_MAP_STR = "坐标错误";

        public static Integer DUPLICATED = 1;
    }

}
