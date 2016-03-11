package com.next.myforismatic;

/**
 * Created by maslparu on 11.03.2016.
 */
public class Quote {
    private String mText;
    private String mAuthor;
    private String mName;
    private String mSenderLink;
    private String mQuoteLink;

    public Quote(String text, String author, String name, String senderLink, String quoteLink){
        mText = text;
        mAuthor = author;
        mName = name;
        mSenderLink = senderLink;
        mQuoteLink = quoteLink;
    }

    public Quote(String text){
        mText = text;
    }

    @Override
    public String toString() {
        if ((mAuthor == null) || (mAuthor == "")){
            return mText;
        }else{
            return mText + " (" + mAuthor + ")";
        }
    }
}
