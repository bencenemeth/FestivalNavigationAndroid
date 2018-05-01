package bme.aut.hu.festivalnavigationandroid.model.time;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by ben23 on 2018-03-10.
 */

public class OpeningHours implements Parcelable {
    @SerializedName("open")
    @Expose
    private String open;

    @SerializedName("close")
    @Expose
    private String close;

    private Calendar openCal;
    private Calendar closeCal;

    //private Gson gson;

    public OpeningHours() {
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public Calendar getOpenCal() {
        return openCal;
    }

    public void setOpenCal(Calendar openCal) {
        this.openCal = openCal;
    }

    public Calendar getCloseCal() {
        return closeCal;
    }

    public void setCloseCal(Calendar closeCal) {
        this.closeCal = closeCal;
    }

    public void setCalendars() throws ParseException {
        //gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        //openCal = gson.fromJson(open, new TypeToken<Calendar>() {}.getType());

        openCal = Calendar.getInstance();
        closeCal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String openTemp = open.replace("Z", "+00:00");
        String closeTemp = close.replace("Z", "+00:00");
        try {
            openTemp = openTemp.substring(0, 22) + openTemp.substring(23);
            closeTemp = closeTemp.substring(0, 22) + closeTemp.substring(23);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
            openCal.setTime(sdf.parse(openTemp));
            closeCal.setTime(sdf.parse(closeTemp));
    }

    /** PARCELABLE */

    protected OpeningHours(Parcel in) {
        open = in.readString();
        close = in.readString();
        openCal = (Calendar) in.readValue(Calendar.class.getClassLoader());
        closeCal = (Calendar) in.readValue(Calendar.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(open);
        dest.writeString(close);
        dest.writeValue(openCal);
        dest.writeValue(closeCal);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OpeningHours> CREATOR = new Parcelable.Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel in) {
            return new OpeningHours(in);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };
}
