package nl.rug.www.summerschool;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jk on 3/31/17.
 */

public class Content {

    private String mId;
    private String mTitle;
    private String mDescription;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}