package bme.aut.hu.festivalnavigationandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ben23 on 2018-02-14.
 */

public class MyTime implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyTime() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * Comparing times.
     *
     * @param rhs the comparable time
     * @return -1 if less, 0 is equal, 1 if more
     */
    public int compareTo(MyTime rhs) {
        if (this.hour < rhs.hour)
            return -1;
        else if (this.hour > rhs.hour)
            return 1;
        else {
            if (this.minute < rhs.minute)
                return -1;
            else if (this.minute > rhs.minute)
                return 1;
            else
                return 0;
        }
    }

    // TODO REMOVE OR USE THIS
    public int compareTo(Calendar rhs) {
        if (this.hour < rhs.HOUR_OF_DAY)
            return -1;
        else if (this.hour > rhs.HOUR_OF_DAY)
            return 1;
        else {
            if (this.minute < rhs.MINUTE)
                return -1;
            else if (this.minute > rhs.MINUTE)
                return 1;
            else
                return 0;
        }
    }

    protected MyTime(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MyTime> CREATOR = new Parcelable.Creator<MyTime>() {
        @Override
        public MyTime createFromParcel(Parcel in) {
            return new MyTime(in);
        }

        @Override
        public MyTime[] newArray(int size) {
            return new MyTime[size];
        }
    };
}
