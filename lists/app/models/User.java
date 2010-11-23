package models;

import java.util.*;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.mvc.Scope.Session;

import com.google.gson.JsonObject;
import siena.*;

public class User extends Model {
    
    @Id
    public Long id;
    public String email;


    public static void facebookOAuthCallback(JsonObject data){
        Session.current().put("user", data.get("email").getAsString());

        String uri = "https://graph.facebook.com/me/friends?access_token="+Session.current().get("accessToken");
        JsonObject jsonData = WS.url(uri).get().getJson().getAsJsonObject();
        new Friends(Session.current().get("user"), jsonData.toString() ).insert();

    }
    
}
