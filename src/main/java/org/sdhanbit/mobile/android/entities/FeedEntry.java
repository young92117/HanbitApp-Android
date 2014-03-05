package org.sdhanbit.mobile.android.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "FeedEntry")
public class FeedEntry {

    @DatabaseField(generatedId = true)
    private Integer _id;
    @DatabaseField
    private String date;
    @DatabaseField
    private String title;
    @DatabaseField
    private String content;
    @DatabaseField
    private String type;
    @DatabaseField
    private Boolean isViewed;

    public FeedEntry() {
        // ORMLite needs a no-arg constructor
    }

    public FeedEntry(String date, String title, String content, String type) {
    	this.date = date;
        this.title = title;
        this.content = content;
        this.type = type;
        this.setIsViewed(false);
    }

    public String getDate() { // "yyyyMMddHHmmss" format
        return date;
    }
    
    public void setDate(String date) { // "yyyyMMddHHmmss" format
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(Boolean isViewed) {
        this.isViewed = isViewed;
    }
}
