package nl.rug.www.summerschool;

/**
 * Created by jk on 4/1/17.
 */

public class TimeTable extends Content {
    private String mDate;
    private String mLocation;
    private String mStartDate;
    private String mEndDate;

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}