package com.example.myapplication.constant;

public class AppConfig {
    public final static String BASE_URL_PATH = "http://192.168.137.1:8849";
    public final static String COURSE_FIND_ALL_TO_MAP = BASE_URL_PATH.concat("/course/findAllTOMap");
    public final static String COURSE_FIND_ALL = BASE_URL_PATH.concat("/course/findAll");
    public final static String USER_LOGIN = BASE_URL_PATH.concat("/user/login");
    public final static String USER_REGISTER = BASE_URL_PATH.concat("/user/register");
    public final static String TEST_FIND_ALL = BASE_URL_PATH.concat("/test/findAll");
    public final static String TEST_FIND_QUESTION_BY_TEST_ID = BASE_URL_PATH.concat("/test/findQuestionsByTestId");
    public final static String TEST_FIND_CHOICE_BY_QUEST_ID = BASE_URL_PATH.concat("/test/findChoicesById");
    public final static String VIDEO_FIND_BY_ID = BASE_URL_PATH.concat("/course/findVideoById");
    public final static String COMMENT_FIND_BY_ID = BASE_URL_PATH.concat("/course/findCommentsById");
    public final static String USER_SCORE_SUBMIT = BASE_URL_PATH.concat("/user/submitToScore");
    public final static String TEST_FIND_BY_ID = BASE_URL_PATH.concat("/test/findById");
    public final static String USER_INFO_FIND_BY_NICKNAME = BASE_URL_PATH.concat("/user/findUserInfoByNickName");
    public final static String TEST_PAGE_INIT = BASE_URL_PATH.concat("/test/initTestPage");
    public final static String SCORE_LIST_GET = BASE_URL_PATH.concat("/user/getScoreList");
    public final static String COMMENT_UPDATE = BASE_URL_PATH.concat("/course/insertComment");
    public final static String USER_UPDATE = BASE_URL_PATH.concat("/user/update");
    public final static String NICKNAME_JUDGE = BASE_URL_PATH.concat("/user/judgeNickName");
    public final static String GOODS_FIND_ALL = BASE_URL_PATH.concat("/goods/findByType");
    public final static String GOODS_FIND_BY_ID = BASE_URL_PATH.concat("/goods/findById");
    public final static String USER_INFO_FIND_BY_ID = BASE_URL_PATH.concat("/user/findUserInfoById");
    public final static String GOODS_DETAIL_SUBMIT = BASE_URL_PATH.concat("/goods/submitGoodsDetails");
    public final static String ORDER_FIND_BY_USER_ID = BASE_URL_PATH.concat("/goods/findOrdersByUserId");
    public final static String Order_FIND_BY_ID = BASE_URL_PATH.concat("/goods/findOrderById");
    public final static String POINT_RECORD_FIND_BY_USER_ID = BASE_URL_PATH.concat("/user/findPointsByUserId");

}
