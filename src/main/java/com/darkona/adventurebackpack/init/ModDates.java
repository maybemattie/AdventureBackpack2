package com.darkona.adventurebackpack.init;

import java.util.Calendar;

public class ModDates {

    private static String holiday;

    private static final int year = Calendar.getInstance().get(Calendar.YEAR);
    private static final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private static final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public static void init() {
        holiday = setHoliday();
    }

    public static String getHoliday() {
        // TODO add check for current day, change skin if new day is holiday
        return holiday;
    }

    private static String setHoliday() {
        // here and below commented lines: textures are missing.

        String result = "Standard";
        if (month == 1) {
            if (day == 1) result = "NewYear";
            if (day == 28) result = "Shuttle"; // Challenger
        }
        if (month == 2) {
            if (day == 1) result = "Shuttle"; // Columbia
            if (day == 14) result = "Valentines";
        }
        if (month == 3) {
            if (day == 17) result = "Patrick";
        }
        if (month == 4) {
            if (day == 1) result = "Fools";
        }
        if (month == 7) {
            if (day == 4) result = "USA";
            if (day == 24) result = "Bolivar";
        }
        if (month == 12) {
            if (day >= 22 && day <= 26) result = "Christmas";
            if (day == 31) result = "NewYear";
        }
        // LogHelper.info("Today is: " + day + "/" + month + "/" + year + ". Which means today is: " + result);
        return result;
    }

    private static int[] calculateEaster(int year) {
        int a = year % 19, b = year / 100, c = year % 100, d = b / 4, e = b % 4, g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30, j = c / 4, k = c % 4, m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7, n = (h - m + r + 90) / 25, p = (h - m + r + n + 19) % 32;

        return new int[] { n, p };
    }
}
