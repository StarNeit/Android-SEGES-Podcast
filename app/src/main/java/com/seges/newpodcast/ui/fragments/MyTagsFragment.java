package com.seges.newpodcast.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import com.seges.newpodcast.R;
import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.utils.TagsUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jamariya on 18-02-2016.
 */
public class MyTagsFragment extends Fragment {

    @Bind(R.id.scroll_view)
    ScrollView scroll_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        ButterKnife.bind(this, view);

        List<Integer> my_tags = TagsUtil.getInstance().getSavedConference();

        for(int i=0;i< TagsUtil.NO_TAGS;i++){
            if(my_tags.contains(TagsUtil.check_id[i])){
                ((CheckBox)view.findViewById(TagsUtil.check_id[i])).setChecked(true);
            }
            /*((CheckBox)view.findViewById(TagsUtil.check_id[i])).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        TagsUtil.getInstance().saveConference(buttonView.getId());
                    }else{
                        if(TagsUtil.getInstance().removeConference(buttonView.getId())){
                            Log.v("removed", "tag removed");
                        }else{
                            Log.v("removed", "tag removed fail");
                        }
                    }
                }
            });*/
        }

        ((MainActivity)getActivity()).tv_fragment_title.setText("Mine emneord");
        ((MainActivity)getActivity()).tv_fragment_title.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).img_white_logo.setVisibility(View.VISIBLE);
        return view;
    }
    @OnClick(R.id.more_info)
    public void showInfo(){
        View view = getView().findViewById(R.id.show_info);
        if(view.getVisibility()==View.VISIBLE){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
            scroll_view.scrollTo(0,getView().findViewById(R.id.show_info).getTop());
        }
    }

    @OnClick(R.id.show_info)
    public void show_infoClicked(){
        getView().findViewById(R.id.show_info).setVisibility(View.GONE);
    }

    @OnClick(R.id.save_tags)
    public void saveTags(){

        for(int i=0;i< TagsUtil.NO_TAGS;i++){
            if(((CheckBox)getView().findViewById(TagsUtil.check_id[i])).isChecked()){
                TagsUtil.getInstance().saveConference(TagsUtil.check_id[i]);
                Log.v("selected","YES" + TagsUtil.check_id[i]);
            }else{
                TagsUtil.getInstance().removeConference(TagsUtil.check_id[i]);
                Log.v("selected", "NO" + TagsUtil.check_id[i]);
            }
        }
        Toast.makeText(getActivity(), getString(R.string.tags_saved), Toast.LENGTH_SHORT).show();
    }
}
