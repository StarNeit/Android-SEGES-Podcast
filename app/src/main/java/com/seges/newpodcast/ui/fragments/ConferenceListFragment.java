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

import com.seges.newpodcast.R;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.ui.adapters.ConferenceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConferenceListFragment extends Fragment implements ConferenceAdapter.OnItemClickListener {
    public static final String TAG = ConferenceListFragment.class.getSimpleName();

    private static ConferenceListFragment instance = null;

    public static final String IS_SAVED_CONFERENCE = "saved_conference_list";

    private List<Conference> conferences = new ArrayList<>();

    public static ConferenceListFragment getInstance() {
        if (instance == null) {
            instance = new ConferenceListFragment();
        }
        return instance;
    }

    public void setConferenceList(List<Conference> conferences) {
        this.conferences = conferences;
        if (adapter != null) {
            adapter.setConferenceList(conferences);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.no_records)
    TextView no_records;

    ConferenceAdapter adapter;

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


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ConferenceAdapter(getActivity());
        adapter.setConferenceList(conferences);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // if no conference show no result found view
        if (conferences.size() == 0) {
            no_records.setVisibility(View.VISIBLE);
        } else {
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
    public void OnPauseClicked() {
        ((MainActivity) getActivity()).pausePlayer();
    }

    // on conference check animate and update records

    /*@Override
    public void OnConferenceChecked(final ImageButton imageButton, final Conference conference) {
        AnimatorSet animatorSet = new AnimatorSet();
        *//*ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageButton, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);*//*

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Drawable drawable = new IconicsDrawable(getActivity())
                        .icon(GoogleMaterial.Icon.gmd_check_circle)
                        .color(getResources().getColor(R.color.colorPrimary))
                        .sizeDp(20);
                imageButton.setImageDrawable(drawable);
                //imageButton.setImageResource(R.drawable.ic_btn_selection_checked);
                AppUtils.saveConference(conference);
                Toast.makeText(getActivity(), getString(R.string.save_conference), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                adapter.notifyDataSetChanged();
            }
        });
        //animatorSet.play(rotationAnim);
        animatorSet.play(bounceAnimX).with(bounceAnimY);//.after(rotationAnim);
        animatorSet.start();
    }

    // on conference un-check animate and update records

    @Override
    public void OnConferenceUnChecked(final ImageButton imageButton, final Conference conference) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageButton, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(imageButton, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(imageButton, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageButton.setImageResource(R.drawable.ic_checkbox_blank_circle_outline_grey600_24dp);
                AppUtils.removeConference(conference);
                Toast.makeText(getActivity(), getString(R.string.remove_confenrece), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                adapter.notifyDataSetChanged();
            }
        });
        animatorSet.play(rotationAnim);
        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
    }

    @Override
    public void OnSessionClick(Conference conference) {
        Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
    }*/
}
