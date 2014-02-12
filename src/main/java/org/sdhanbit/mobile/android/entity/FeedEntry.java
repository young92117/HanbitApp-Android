package org.sdhanbit.mobile.android.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "FeedEntry")
public class FeedEntry {

    @DatabaseField(id = true)
    private Integer _id;
    @DatabaseField
    private String author;
    @DatabaseField
    private String title;
    @DatabaseField
    private String link;
    @DatabaseField
    private String content;
    @DatabaseField
    private String description;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date publishedDate;
    @DatabaseField
    private String category;
    @DatabaseField
    private Boolean isViewed;

    public FeedEntry() {
        // ORMLite needs a no-arg constructor
    }

    public FeedEntry(String author, String title, String link, String content, String description, Date publishedDate, String category) {
        this.author = author;
        this.title = title;
        this.link = link;
        this.content = content;
        this.description = description;
        this.publishedDate = publishedDate;
        this.category = category;
        this.setIsViewed(false);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(Boolean isViewed) {
        this.isViewed = isViewed;
    }
}
