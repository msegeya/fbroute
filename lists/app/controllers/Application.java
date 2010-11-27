package controllers;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.Play;
import play.libs.WS;
import play.modules.fbconnect.FBConnectPlugin;
import play.mvc.*;
import play.modules.gae.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

    public static void index() {
        if(session.contains("user")) {
            redirect("http://apps.facebook.com/fbroute/");
        }
        render();
    }

    public static void canvas(String signed_request) throws Base64DecoderException {
//        if(session.contains("user")) {
//            Lists.index();
//        }

        JsonObject jsonObject = new JsonParser().parse(base64decode(signed_request.split("\\.")[1])).getAsJsonObject();
        if(jsonObject.get("oauth_token") == null){
            renderArgs.put("url", Play.plugin(FBConnectPlugin.class).session().getLoginUrl());
//            renderArgs.put("url", "https://graph.facebook.com/oauth/authorize?client_id=131378720252742&display=page&redirect_uri=http://apps.facebook.com/fbroute/&scope=email");
            render();
        }

        Lists.index();        
    }

    public static void logout() {
        session.remove("user");
        redirect("Application.index");
    }


    public static String base64decode(String signed) throws Base64DecoderException {
        signed = signed.replace('-','+');
        signed = signed.replace('_','/');
        return new String(Base64.decode(signed));
    }
}