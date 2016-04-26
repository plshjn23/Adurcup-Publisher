package Fragements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by om on 4/25/2016.
 */
public class ParseJSON  {
    public static String[] approves;
    public static String[] added_credits;
    public static String[] types;
    public static String[] image_srcs;
    public static String[] time_stamps;

    public static final String JSON_ARRAY = "images";
    public static final String KEY_APPROVE = "approve";
    public static final String KEY_ADDED_CREDIT = "added_credit";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE_SRC = "image_src";
    public static final String KEY_TIME_STAMP = "time_stamp";

    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            approves = new String[users.length()];
            added_credits = new String[users.length()];
            types = new String[users.length()];
            image_srcs = new String[users.length()];
            time_stamps = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                approves[i] = jo.getString(KEY_APPROVE);
                added_credits[i] = jo.getString(KEY_ADDED_CREDIT);
                types[i] = jo.getString(KEY_TYPE);
                image_srcs[i] = jo.getString(KEY_IMAGE_SRC);
                time_stamps[i] = jo.getString(KEY_TIME_STAMP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
