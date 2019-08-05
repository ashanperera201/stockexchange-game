package com.stockMarket.ucd.stockMarket.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9- ]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    //Google Resource Constants
    public static final String MAIN_DIR_ID = "main_directory_id";
    public static final String PICTURES_DIR_ID = "pictures_directory_id";
    public static final String GENERAL_FILES_DIR_ID = "general_files_directory_id";
    public static final String CONFIDENTIAL_DIR_ID = "confidential_directory_id";
    public static final String CONTACTS_GROUP_ID = "contacts_group_id";

    /* Workflow Import *
     *
     */
    public static final String STAGE_ID = "Stage ID";
    public static final String QUESTION_ID = "Question ID";
    public static final String STAGE_NAME = "Stage Name";
    public static final String OPPORTUNITY_NAME = "Opportunity Name";

    /* Dashboard */

    public static final Integer RECENT_RECORD_NUMBER = 5;
    public static final String OPPORTUNITY_ASSIGNED_TO_ME = "OPPORTUNITY_ASSIGNED_TO_ME";
    public static final String RECENT_OPPORTUNITIES = "RECENT_OPPORTUNITIES";
    public static final String RECENT_WORKFLOWS = "RECENT_WORKFLOWS";
    public static final String STATUS_OF_USERS = "STATUS_OF_USERS";
    public static final String CURRENCY_TABLE = "CURRENCY_TABLE";
    public static final String LICENCE_STATUS = "LICENCE_STATUS";

    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String DEPARTMENT_ADMIN = "DEPARTMENT_ADMIN";
    public static final String WORKFLOW_ADMIN = "WORKFLOW_ADMIN";
    public static final String USER = "USER";
    public static final String NOT_USER = "NOT_USER";
    public static final String WORKFLOW_MEMBER = "WORKFLOW_MEMBER";

    public static  String departmentName = "";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEV_ADMIN = "chamathlak95@gmail.com";

    public static String APPLICATION_BASE_URL;
    public static String APPLICATION_BASE_EMAIL;
    public static String APPLICATION_SENDGRID_KEY;

    public static String ACCRUAL_CARRY_FORWARD_DISABLE = "DISABLE";
    public static String ACCRUAL_CARRY_FORWARD_BALANCE = "BALANCE";
    public static String ACCRUAL_UPDATE_MONTHLY= "MONTH";
    public static String ACCRUAL_UPDATE_YEARLY= "YEAR";
    public static final String AUTO_USER_SEQUENCE = "user";
    public static final String AUTO_SECTOR_SEQUENCE = "sect";
    public static final String AUTO_COMPANY_SEQUENCE = "comp";
    private Constants() {
    }
}
