package com.seges.newpodcast.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seges.newpodcast.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamariya on 20-02-2016.
 */
public class TagsUtil {

    public static final String saved_tags = "saved_tags";

    public static int check_id[] = {R.id.tag_1, R.id.tag_2, R.id.tag_3, R.id.tag_4, R.id.tag_5, R.id.tag_6, R.id.tag_7, R.id.tag_8
            , R.id.tag_9, R.id.tag_10, R.id.tag_11, R.id.tag_12, R.id.tag_13, R.id.tag_14, R.id.tag_15, R.id.tag_16, R.id.tag_17, R.id.tag_18
            , R.id.tag_19, R.id.tag_20, R.id.tag_21, R.id.tag_22, R.id.tag_23, R.id.tag_24, R.id.tag_25, R.id.tag_26, R.id.tag_27, R.id.tag_28, R.id.tag_29};

    public static final int NO_TAGS = 29;

    private static TagsUtil INSTANCE = null;

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TagsUtil();
        }
    }

    public static TagsUtil getInstance() {
        if (INSTANCE == null)
            createInstance();
        return INSTANCE;
    }


    public boolean isSavedConference(Integer product){
        List<Integer> products = getSavedConference();
        for(int i=0;i<products.size();i++){
            if(products.get(i).equals(product)){
                return true;
            }
        }
        return false;
    }

    public boolean saveConference(Integer conference){
        if(!isSavedConference(conference)){
            Gson gson = new Gson();
            List<Integer> products = getSavedConference();
            products.add(conference);
            Prefs.putString(saved_tags, new Gson().toJson(products));
            return true;
        }else{
            return false;
        }
    }
    public ArrayList<Integer> getSavedConference() {
        String conference_ = Prefs.getString(saved_tags, new Gson().toJson(new ArrayList<Integer>()));
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return (ArrayList<Integer>) gson.fromJson(conference_, listType);
    }
    public boolean removeConference(Integer product){
        List<Integer> products = getSavedConference();
        Log.v("saved", new Gson().toJson(products));
        Log.v("remove", new Gson().toJson(product));
        for(int i=0;i<products.size();i++){
            ///Log.v("compare", new Gson().toJson(product));
            if(products.get(i).equals(product)){
                products.remove(i);
                updateConferenceList(products);
                return true;
            }
        }
        return false;
    }
    public void updateConferenceList(List<Integer> conferences){
        Prefs.putString(saved_tags, new Gson().toJson(conferences));
    }
}
