package org.sdhanbit.mobile.android.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "FeedEntryCategory")
public class FeedEntryCategory {
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField(canBeNull = false, foreign = true)
    private FeedEntry feedEntry;
    @DatabaseField(canBeNull = false, foreign = true)
    private Category category;

    public FeedEntryCategory() {

    }

    public FeedEntryCategory(Category category, FeedEntry feedEntry) {
        this.setCategory(category);
        this.setFeedEntry(feedEntry);
    }

    public Integer getId() {
        return _id;
    }

    public FeedEntry getFeedEntry() {
        return feedEntry;
    }

    public void setFeedEntry(FeedEntry feedEntry) {
        this.feedEntry = feedEntry;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
