package hk.com.csci4140.culife.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class memberProfileModel {

    public int id;
    public String username;
    public String bio;
    public String image;
    public Boolean following;

    public int getId() {
        return id;
    }


    public void initMemberProfileWithJSON(JSONObject response){
        Integer profile_id = 0;
        String profile_username = new String();
        String profile_bio = new String();
        String profile_image = new String();
        Boolean profile_following = false;
        try {
            profile_id = response.getInt("id");
            profile_username = response.getString("username");
            profile_bio = response.getString("bio");
            profile_image = response.getString("image");
            profile_following = response.getBoolean("following");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        id = profile_id;
        username = profile_username;
        bio = profile_bio;
        image = profile_image;
        following = profile_following;

    }

}
