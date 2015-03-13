package com.ferrishibernatesupport.saveferris.httpsearchapi;

/**
 * Created by uiradias on 06/09/14.
 */
public class DateFormatDiscover {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_WITH_TIME = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static String discoverDateFormatForString(String dateString) {
        if (dateString.length() > DATE_FORMAT.length() && dateString.contains("T")) {
            return DATE_FORMAT_WITH_TIME;
        } else {
            return DATE_FORMAT;
        }
    }

}
