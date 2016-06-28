package model;

import org.json.JSONObject;

public class Post {
    public String dataid;
    public String datatype;
    public String timestamp;
    public String value;
    private Post(){}
    
    public static Post fromJSON(JSONObject j){
        Post post = new Post();
        post.dataid = j.optString("dataid");
        post.datatype = j.optString("datatype");
        post.timestamp = j.optString("timestamp");
        post.value = j.optString("value");
        return post;
    }
    
}
