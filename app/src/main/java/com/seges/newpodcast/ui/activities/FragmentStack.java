package com.seges.newpodcast.ui.activities;

/**
 * Created by coneits on 7/21/16.
 */
public class FragmentStack {
    private boolean showBack;

    private boolean showTitle;

    private String id;

    private String title;

    public FragmentStack(String title, boolean showBack) {

        this.showBack = showBack;

        if (title.isEmpty()){
            this.showTitle = false;
        }else {
            this.showTitle = true;
        }

        this.title = title;
    }

    public boolean getShowBack()
    {
        return this.showBack;
    }

    public void setShowBack(boolean showBack){
        this.showBack = showBack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean getShowTitle()
    {
        return this.showTitle;
    }

    public void setShowTitle(boolean showTitle)
    {
        this.showTitle = showTitle;
    }
}
