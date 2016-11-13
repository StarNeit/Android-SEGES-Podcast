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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgramListFragment extends Fragment implements ConferenceAdapter.OnItemClickListener {
    public static final String TAG = ProgramListFragment.class.getSimpleName();
    int mNum;

    static ProgramListFragment newInstance(int num) {
        ProgramListFragment f = new ProgramListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    public int getIndex() {
        return mNum;
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
        setRecyclerView(null);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setRecyclerView(List<Conference> conferences) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ConferenceAdapter(getActivity());

        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        if (conferences != null && conferences.size() != 0) {
            no_records.setVisibility(View.GONE);
        } else {
            no_records.setVisibility(View.VISIBLE);
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

    public void updateData(List<Conference> conferences) {
        if (conferences != null && conferences.size() != 0) {
            adapter.setConferenceList(conferences);
            no_records.setVisibility(View.GONE);
        } else {
            no_records.setVisibility(View.VISIBLE);
        }
    }
}
