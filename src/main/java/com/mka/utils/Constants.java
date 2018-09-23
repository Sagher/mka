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

    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final List<String> ALL_ROLES = Arrays.asList(new String[]{"ADMIN", "READONLY", "MANAGER"});

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_READONLY = "READONLY";

    public static String BASE_PATH = "/var/www/html/mka";
    public static String IMAGES_BASE_PATH = "/resources/img/avatars/";
//    public static String BASE_PATH = "E:\\";
//    public static String IMAGES_BASE_PATH = "resources\\img\\avatars\\";
    public static final String DEFAULT_IMG = "/resources/img/avatars/default-image.jpg";

}
