package com.seges.newpodcast.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seges.newpodcast.model.Conference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public static final String saved_conference = "saved_conference";
    public static final String hear_later_conference = "later_conference";
    public static final String play_active_episode = "play_active_episode";

//---Save Favorite Conferences---//
    public static boolean saveConference(Conference conference){
        if(!isSavedConference(conference)){
            Gson gson = new Gson();
            List<Conference> products = getSavedConference();
            products.add(conference);
            Prefs.putString(saved_conference, new Gson().toJson(products));
            return true;
        }else{
            return false;
        }
    }
    public static boolean isSavedConference(Conference product){
        List<Conference> products = getSavedConference();
        for(int i=0;i<products.size();i++){
            if (products.get(i).getTitle().equals(product.getTitle()))
            {
                return true;
            }
        }
        return false;
    }
    public static List<Conference> getSavedConference() {
        String conference_ = Prefs.getString(saved_conference, new Gson().toJson(new ArrayList<Conference>()));
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Conference>>() {}.getType();
        return (List<Conference>) gson.fromJson(conference_, listType);
    }
    public static boolean removeConference(Conference product){
        List<Conference> products = getSavedConference();
        for(int i=0;i<products.size();i++){
            if(products.get(i).getTitle().equals(product.getTitle()))
            {
                products.remove(i);
                updateConferenceList(products);
                return true;
            }
        }
        return false;
    }
    public static void clearConferenceList(){
        Prefs.putString(saved_conference, new Gson().toJson(new ArrayList<Conference>()));
    }
    public static final void updateConferenceList(List<Conference> products){
        Prefs.putString(saved_conference, new Gson().toJson(products));
    }

//---Save Hear Later Conferences---//
    public static boolean saveHearLaterConference(Conference conference){
        if(!isSavedHearLaterConference(conference)){
            Gson gson = new Gson();
            List<Conference> products = getSavedHearLaterConference();
            products.add(conference);
            Prefs.putString(hear_later_conference, new Gson().toJson(products));
            return true;
        }else{
            return false;
        }
    }
    public static boolean isSavedHearLaterConference(Conference product){
        List<Conference> products = getSavedHearLaterConference();
        for(int i=0;i<products.size();i++){
            if (products.get(i).getTitle().equals(product.getTitle()))
            {
                return true;
            }
        }
        return false;
    }
    public static List<Conference> getSavedHearLaterConference() {
        String conference_ = Prefs.getString(hear_later_conference, new Gson().toJson(new ArrayList<Conference>()));
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Conference>>() {}.getType();
        return (List<Conference>) gson.fromJson(conference_, listType);
    }
    public static boolean removeHearLaterConference(Conference product){
        List<Conference> products = getSavedHearLaterConference();
        for(int i=0;i<products.size();i++){
            if (products.get(i).getTitle().equals(product.getTitle()))
            {
                products.remove(i);
                updateHearLaterConferenceList(products);
                return true;
            }
        }
        return false;
    }
    public static void clearHearLaterConferenceList(){
        Prefs.putString(hear_later_conference, new Gson().toJson(new ArrayList<Conference>()));
    }
    public static final void updateHearLaterConferenceList(List<Conference> products){
        Prefs.putString(hear_later_conference, new Gson().toJson(products));
    }




    public static boolean isEpisodePlaying(String player_id){
        if(Prefs.getString(play_active_episode,"").equals(player_id)){
            return true;
        }else{
            return false;
        }
    }
    public static void savePlayEpisode(String player_id){
        Prefs.putString(play_active_episode, player_id);
    }
    public static void removePlayEpisode(){
        Prefs.putString(play_active_episode, "");
    }
}
