package controllers;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.Play;
import play.modules.fbconnect.FBConnectPlugin;
import play.mvc.*;
import play.modules.gae.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

    public static void index() {
        if(session.contains("user")) {
            Lists.index();
        }
        render();
    }

    public static void canvas(String signed_request) throws Base64DecoderException {
        if(session.contains("user")) {
            Lists.index();
        }

        JsonObject jsonObject = new JsonParser().parse(base64decode(signed_request.split("\\.")[1])).getAsJsonObject();
        if(jsonObject.get("oauth_token") == null){
            redirect(Play.plugin(FBConnectPlugin.class).session().getLoginUrl());
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

    public static void main(String[] args) throws Base64DecoderException {
        System.out.println("CCtIfAaL6VW6AX1TAN4Ap3wF7M10acJ5zxuT2h1yoLU.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTI5MDQ3MDAzMH0".split(".")[1]);


    }

}