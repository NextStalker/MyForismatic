package com.next.myforismatic.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maslparu on 11.03.2016.
 */
public class Quote {
    @SerializedName("quoteText")
    public String text;
    @SerializedName("quoteAuthor")
    public String author;
    @SerializedName("senderName")
    public String name;
    @SerializedName("senderLink")
    public String senderLink;
    @SerializedName("quoteLink")
    public String quoteLink;

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return TextUtils.isEmpty(author) ? "Аноним" : author;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", senderLink='" + senderLink + '\'' +
                ", quoteLink='" + quoteLink + '\'' +
                '}';
    }
}
