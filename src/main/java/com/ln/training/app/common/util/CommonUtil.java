package com.ln.training.app.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class CommonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * Convert date to string with input format
     * @param inputDate
     * @param dateFormat
     * @return
     */
    static String cvDateToString(Date inputDate, String dateFormat){
        String outputDate = StringUtils.EMPTY;
        if (inputDate != null){
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat(dateFormat);
            outputDate = dateFormatOutput.format(inputDate);
        }
        return outputDate;
    }

    /**
     * Convert date to string with input format and locale
     * @param inputDate
     * @param dateFormat
     * @param locale
     * @return
     */
    public static String cvDateToString(Date inputDate, String dateFormat, Locale locale){
        String outputDate = StringUtils.EMPTY;
        if (inputDate != null){
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat(dateFormat,locale);
            outputDate = dateFormatOutput.format(inputDate);
        }
        return outputDate;
    }

    /**
     * Convert string to date
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date cvStringToDate(String dateStr, String dateFormat){
        Date outputDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            outputDate = simpleDateFormat.parse(dateStr);
        }catch (ParseException e){
            LOG.error("Error when converting date: " + e.getMessage());
        }
        return outputDate;
    }

    /**
     * Read Properties
     * @param key
     * @param fileName
     * @return
     */
    public static String readProperties(String key, String fileName){
        InputStream inputStream = CommonUtil.class.getClassLoader().getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
            return prop.getProperty(key);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
