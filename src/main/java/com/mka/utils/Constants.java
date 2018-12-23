/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public class Constants {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final List<String> ALL_ROLES = Arrays.asList(new String[]{"ADMIN", "READONLY", "MANAGER"});
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_READONLY = "READONLY";

    public static final String DIRECT = "DIRECT", INDIRECT = "INDIRECT", CASH_TRANSACTIONS = "cashTransactions", CUSTOMERS_BUYERS = "customersBuyers";
    public static final String SALE = "SALE", PURCHASE = "PURCHASE", PRODUCE = "PRODUCE", EXPENSE = "EXPENSE", CONSUME = "CONSUME";
    public static final String RECEIVABLE = "RECEIVABLE";
    public static final String PAYABLE = "PAYABLE";
    public static final String PROFITLOSS = "profitLoss";
    public static final String CLOSINGSTOCK = "closingStock";
    public static final String BALANCESHEET = "balanceSheet";

    public static final String HEADOFFICE = "HeadOffice";
    public static final String PERSON_TO_HEADOFFICE = "Person To HeadOffice";
    public static final String CASH_IN_HAND_TO_HEADOFFICE = "Cash In Hand To HeadOffice";
    public static final String FROM_HEADOFFICE_TO_PERSON = "From HeadOffice To Person";
    public static final String FROM_HEADOFFICE_TO_CASH_IN_HAND = "From HeadOffice To Cash In Hand";
    public static String FROM_CASH_IN_HAND_TO_PERSON = "Cash In Hand To Person";
    public static String FROM_PERSON_TO_CASH_IN_HAND = "Person To Cash In Hand";

//    public static String BASE_PATH = "E:\\";
//    public static String IMAGES_BASE_PATH = "resources\\img\\avatars\\";
    public static final String BASE_PATH = "/var/www/asphaltplant";
    public static final String IMAGES_BASE_PATH = "/resources/img/avatars/";
    public static final String DEFAULT_IMG = "/resources/img/avatars/default-image.jpg";

}
