package com.seges.newpodcast.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.ui.adapters.MyConferenceAdapter;
import com.seges.newpodcast.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyProgramListFragment extends Fragment implements MyConferenceAdapter.OnItemClickListener {

    public static final String IS_SAVED_CONFERENCE = "saved_conference_list";

    private boolean is_saved_conference_list = false;
    private List<Conference> conferences = new ArrayList<>();

    public static MyProgramListFragment getInstance(boolean is_saved_conference){
        MyProgramListFragment fragment = new MyProgramListFragment();
        Bundle bundle =new Bundle();
        bundle.putBoolean(IS_SAVED_CONFERENCE, is_saved_conference);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            is_saved_conference_list = getArguments().getBoolean(IS_SAVED_CONFERENCE);
        }
    }

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.no_records)
    TextView no_records;

    MyConferenceAdapter adapter;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        ButterKnife.bind(this, view);

        // set list data
        setRecyclerView();
        return view;
    }

    /* public List<Conference> createDummyConference(){
        List<Conference> conferences = new ArrayList<>();
        for(int i=0;i<25;i++){
            Conference conference = new Conference();
            conference.setId(i + "");
            conference.setTitle("Conference " + i);
            if(i%9==0){
                conference.setTitle("Conference Large Title For Test Big Data" + i);
            }

            conference.setDescription("Conference Description " + i);
            conference.setZone_id("Section " + i);

            // Set your date formal
            Calendar calendar = Calendar.getInstance();
            if(i<10){
                calendar.add(Calendar.DATE, -1);
            }else if(i<20){
                calendar.add(Calendar.DATE, -2);
            }else{
                calendar.add(Calendar.DATE, -3);
            }

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate = df.format(calendar.getTime());

            conference.setDate(formattedDate);
            conferences.add(conference);
        }
        return conferences;
    } */


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyConferenceAdapter(getActivity());

        // check is display saved conference or all conference

        if(is_saved_conference_list){
            conferences = AppUtils.getSavedConference();
        }else{
            conferences = AppUtils.getSavedHearLaterConference();
        }
        adapter.setConferenceList(conferences);
        adapter.setOnItemClickListener(this);
        adapter.setIs_favorited_list(is_saved_conference_list);
        recyclerView.setAdapter(adapter);

        // if no conference show no result found view
        if(conferences.size()==0){
            no_records.setVisibility(View.VISIBLE);
        }else{
            no_records.setVisibility(View.GONE);
        }

    }

    @Override
    public void OnConferenceChecked(ImageButton imageButton, Conference conference) {

    }

    @Override
    public void OnConferenceUnChecked(ImageButton imageButton, Conference conference) {

    }

    @Override
    public void OnSessionClick(Conference conference) {

    }

    @Override
    public void OnPlayClicked(Conference conference, PauseClickListener listener) {

        ((MainActivity) getActivity()).startPlayer(conference.getAudioUrl(), conference.getTitle(), listener);
    }

    @Override
    public void OnPauseClicked()
    {
        ((MainActivity) getActivity()).pausePlayer();
    }
}
