package org.sdhanbit.mobile.android.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Category")
public class Category {
    @DatabaseField(generatedId = true)
    private Integer _id;
    @DatabaseField
    private String name;

    public Category() {
        // ORMLite needs a no-arg constructor
    }

    public Category(String name) {
        this.setName(name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return _id;
    }
    public void setId(Integer id) {
        _id = id;
    }
}
