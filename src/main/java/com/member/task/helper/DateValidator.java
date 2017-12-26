package com.member.task.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator {

    public static boolean isThisDateValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateToValidate);

        } catch (ParseException e) {
            e.getMessage();
            return false;
        }
        return true;
    }
}
