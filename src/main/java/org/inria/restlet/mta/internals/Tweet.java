package org.inria.restlet.mta.internals;

public class Tweet {

    /** Internal id of the tweet.**/
    private int id_;
    
    /** Content of the tweet.*/
    private String content_;
    
    public Tweet(String content) {
        this.content_ = content;
    }
    
    public String getContent() {
        return this.content_;
    }
    
    public void setContent(String content) {
        this.content_ = content;
    }
    
    public int getId()
    {
        return id_;
    }

    public void setId(int id)
    {
        id_ = id;
    }
}
