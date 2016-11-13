package com.seges.newpodcast.model;

import java.util.List;

/**
 * Created by alexeyreznik on 08/03/16.
 */
public interface OnConferencesLoadedListener {

    public void onConferencesLoaded(List<Conference> conferences);
}
