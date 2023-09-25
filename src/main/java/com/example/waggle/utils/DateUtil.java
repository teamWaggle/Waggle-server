package com.example.waggle.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static void main(String[] args) {

    }

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    /**
     * x초전, x분전, x시간 전m, x일 전, x개월전, x년전
     * @param 날짜
     * @return 분 표시
     *
     */
    public static String simpleStoryTimeFormat(LocalDateTime tempDate) {
        long curTime = System.currentTimeMillis();

        long regTime = Timestamp.valueOf(tempDate).getTime();

        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < SEC){
            msg = diffTime + "초 전";
        } else if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "개월 전";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy");
            String curYear = sdf.format(curTime);
            String passYear = sdf.format(tempDate);
            int diffYear = Integer.parseInt(curYear) - Integer.parseInt(passYear);
            msg = diffYear + "년 전";
        }

        return msg;

    }

    public static String storyTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd일 (E) a h:mm:ss", Locale.KOREAN);
        return dateTime.format(formatter);
    }

}
