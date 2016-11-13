package com.seges.newpodcast.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.R;
import com.seges.newpodcast.utils.TagsUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jamariya on 18-02-2016.
 */
public class MyProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        ButterKnife.bind(this, view);

        List<Integer> my_tags = TagsUtil.getInstance().getSavedConference();
        for(int i=0;i< TagsUtil.NO_TAGS;i++){
            if(my_tags.contains(TagsUtil.check_id[i])){
                ((CheckBox)view.findViewById(TagsUtil.check_id[i])).setChecked(true);
            }
            ((CheckBox)view.findViewById(TagsUtil.check_id[i])).setEnabled(false);
        }

        return view;
    }

    @OnClick(R.id.my_tags)
    public void myTags(){
        ((MainActivity)getActivity()).setCurrentFragment(new MyTagsFragment());
    }
    @OnClick(R.id.favorite)
    public void favorite(){
        ((MainActivity)getActivity()).setCurrentFragment(MyListFragment.getInstance(1));
    }
    @OnClick(R.id.listen_later)
    public void listerlater(){
        ((MainActivity)getActivity()).setCurrentFragment(new MyListFragment());
    }
}
