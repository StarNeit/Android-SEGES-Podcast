package com.seges.newpodcast.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seges.newpodcast.R;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.utils.TagsUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jamariya on 18-02-2016.
 */
public class DiscoverFragment extends Fragment implements PauseClickListener {

    @Bind(R.id.btn_short_tags)
    TextView btn_short_tags;

    @Bind(R.id.btn_long_tags)
    TextView btn_long_tags;

    @Bind(R.id.episode)
    LinearLayout episode;

    @Bind(R.id.no_episode)
    LinearLayout no_episode;

    private ArrayList<Conference> conferences;
    private ArrayList<Conference> filtered_conferences;

    private int is_long_tag;

    //conference item
    ImageButton checkbox;
    ImageButton checkbox2;
    TextView btn_session;
    TextView title;
    TextView description;
    TextView categories;
    ImageView thumbnail;
    LinearLayout conference_header;
    TextView show_conference_date;
    LinearLayout adapter_root;
    LinearLayout option_layout;
    LinearLayout pod_detail;
    TextView btn_favorite;
    TextView btn_hear_later;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);

    //---Title---//
        ((MainActivity)getActivity()).tv_fragment_title.setText("Find nyt afsnit");
        ((MainActivity)getActivity()).tv_fragment_title.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).img_white_logo.setVisibility(View.VISIBLE);


    //---Initialize---//
        checkbox = (ImageButton)view.findViewById(R.id.checkbox);
        checkbox2 = (ImageButton)view.findViewById(R.id.checkbox2);
        btn_favorite = (TextView)view.findViewById(R.id.btn_favorite);
        btn_hear_later = (TextView)view.findViewById(R.id.btn_hear_later);

        conference_header = (LinearLayout)view.findViewById(R.id.conference_header);
        btn_session = (TextView)view.findViewById(R.id.btn_session);
        title = (TextView)view.findViewById(R.id.title);
        description = (TextView)view.findViewById(R.id.description);
        categories = (TextView)view.findViewById(R.id.categories);
        thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        show_conference_date = (TextView)view.findViewById(R.id.show_conference_date);
        option_layout = (LinearLayout)view.findViewById(R.id.option_layout);
        adapter_root = (LinearLayout)view.findViewById(R.id.adapter_root);
        pod_detail = (LinearLayout)view.findViewById(R.id.pod_detail);




    //---Two Event---//
        view.findViewById(R.id.btn_short_tags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams param1 = ((LinearLayout.LayoutParams)(btn_short_tags.getLayoutParams()));
                param1.setMargins(6,6,6,6);
                btn_short_tags.setLayoutParams(param1);


                LinearLayout.LayoutParams param2 = ((LinearLayout.LayoutParams)(btn_long_tags.getLayoutParams()));
                param2.setMargins(0,0,0,0);
                btn_long_tags.setLayoutParams(param2);

                is_long_tag = 0;

            }
        });
        view.findViewById(R.id.btn_long_tags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams param1 = ((LinearLayout.LayoutParams)(btn_short_tags.getLayoutParams()));
                param1.setMargins(0,0,0,0);
                btn_short_tags.setLayoutParams(param1);


                LinearLayout.LayoutParams param2 = ((LinearLayout.LayoutParams)(btn_long_tags.getLayoutParams()));
                param2.setMargins(6,6,6,6);
                btn_long_tags.setLayoutParams(param2);

                is_long_tag = 1;

            }
        });


    //---Value Initialize---//
        conferences = new ArrayList<Conference>();
        filtered_conferences = new ArrayList<Conference>();
        conferences = (ArrayList<Conference>) ((MainActivity)getActivity()).g_conferenceList;
        is_long_tag = 0;

        return view;
    }

    @OnClick(R.id.sog)
    public void doSearchPods(){

    //---Get Selected Tags---//
        String []category_list = getResources().getStringArray(R.array.category_list);
        ArrayList<String> selected_tags_list = new ArrayList<String>();
        for(int i=0;i< TagsUtil.NO_TAGS;i++){
            if(((CheckBox)getView().findViewById(TagsUtil.check_id[i])).isChecked()){
                selected_tags_list.add(category_list[i]);
            }
        }

        if (conferences == null)
        {
            episode.setVisibility(View.GONE);
            no_episode.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "No records!", Toast.LENGTH_LONG).show();

            return;
        }

        filtered_conferences.clear();

    //---Get Pods List from selected tags---//
        for (int i = 0 ; i < conferences.size(); i ++)
        {
            if (is_long_tag == 1 && !conferences.get(i).getCategories().contains("Lange podcast"))
            {
                continue;
            }
        //---check if this conference has got selected tags---//
            int j;
            for (j = 0 ; j < selected_tags_list.size(); j ++)
            {
                if (conferences.get(i).getCategories().contains(selected_tags_list.get(j)))
                {
                    break;
                }
            }
        //---hasn't got them---//
            if (j != selected_tags_list.size())
            {
                filtered_conferences.add(conferences.get(i));
            }
        }

        int size_conf = filtered_conferences.size();
        if (size_conf == 0)
        {
            episode.setVisibility(View.GONE);
            no_episode.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(), "No records!", Toast.LENGTH_LONG).show();
        }else{
            episode.setVisibility(View.VISIBLE);
            no_episode.setVisibility(View.GONE);
            int rand_index = (new Random()).nextInt(size_conf);
            showSelectedPods(filtered_conferences.get(rand_index));

//            Toast.makeText(getActivity(), filtered_conferences.get(rand_index).getTitle(), Toast.LENGTH_LONG).show();
        }
    }

    private void showSelectedPods(final Conference conf)
    {

        AppUtils.removePlayEpisode();

    //---Expand Selected Item & save episode ID when clicking---//
        adapter_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pod_detail.getVisibility() == View.VISIBLE) {
                    AppUtils.removePlayEpisode();
                    pod_detail.setVisibility(View.GONE);
                    option_layout.setVisibility(View.GONE);
                    checkbox.setVisibility(View.VISIBLE);
                } else {
                    AppUtils.savePlayEpisode(conf.getId());
                    pod_detail.setVisibility(View.VISIBLE);
                    option_layout.setVisibility(View.VISIBLE);
                    checkbox.setVisibility(View.GONE);
                }
            }
        });

    //---Set Necessary Datas---//
        String pods_title = conf.getTitle();
        int start_pos = pods_title.substring(14).indexOf(" ", 0) + 14;
        pods_title = pods_title.substring(0, start_pos) + "\n" + pods_title.substring(start_pos + 1).trim();
        title.setText(pods_title);

        description.setText(conf.getDescription());
        StringBuilder stringBuilder = new StringBuilder();
        for (String category : conf.getCategories()) {
            stringBuilder.append(category + " ");
        }
        categories.setText(stringBuilder.toString());
        Picasso.with(getActivity()).load(conf.getThumbnailUrl()).into(thumbnail);

//        Toast.makeText(getActivity(), "" + AppUtils.isEpisodePlaying(conf.getId()), Toast.LENGTH_SHORT).show();
    //---Small Play Button---//
        checkbox.setTag(5003);
        checkbox.setImageResource(R.drawable.ic_play);
        checkbox2.setImageResource(R.drawable.ic_play_large);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((int)checkbox.getTag() == 5001)
                {
                    checkbox.setImageResource(R.drawable.ic_play);
                    checkbox2.setImageResource(R.drawable.ic_play_large);

                    ((MainActivity)getActivity()).pausePlayer();
                    checkbox.setTag(5000);
                    checkbox2.setTag(6000);
                }
                else if ((int)checkbox.getTag() == 5000){
                    checkbox.setImageResource(R.drawable.pause1);
                    checkbox2.setImageResource(R.drawable.pause_large);

                    ((MainActivity)getActivity()).pausePlayer();
                    checkbox.setTag(5001);
                    checkbox2.setTag(6001);
                }else if ((int)checkbox.getTag() == 5003)
                {
                    checkbox.setImageResource(R.drawable.pause1);
                    checkbox2.setImageResource(R.drawable.pause_large);

                    ((MainActivity)getActivity()).startPlayer(conf.getAudioUrl(), conf.getTitle(), DiscoverFragment.this);
                    checkbox.setTag(5001);
                    checkbox2.setTag(6001);
                }
            }
        });

    //---Big Play Button---//
        checkbox2.setTag(6003);
        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int)checkbox2.getTag() == 6001)
                {
                    checkbox.setImageResource(R.drawable.ic_play);
                    checkbox2.setImageResource(R.drawable.ic_play_large);

                    ((MainActivity)getActivity()).pausePlayer();
                    checkbox.setTag(5000);
                    checkbox2.setTag(6000);
                }
                else if ((int)checkbox2.getTag() == 6000){
                    checkbox.setImageResource(R.drawable.pause1);
                    checkbox2.setImageResource(R.drawable.pause_large);

                    ((MainActivity)getActivity()).pausePlayer();
                    checkbox.setTag(5001);
                    checkbox2.setTag(6001);
                }else if ((int)checkbox2.getTag() == 6003)
                {
                    checkbox.setImageResource(R.drawable.pause1);
                    checkbox2.setImageResource(R.drawable.pause_large);

                    ((MainActivity)getActivity()).startPlayer(conf.getAudioUrl(), conf.getTitle(), DiscoverFragment.this);
                    checkbox.setTag(5001);
                    checkbox2.setTag(6001);
                }
            }
        });

    //---Favorite Button---//
        if (AppUtils.isSavedConference(conf))
        {
            btn_favorite.setBackgroundResource(R.color.button_favorite_bg_2);
            btn_favorite.setTag(1001);
        }else{
            btn_favorite.setBackgroundResource(R.color.button_favorite_bg_1);
            btn_favorite.setTag(1000);
        }

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int)btn_favorite.getTag() == 1000)
                {
                    btn_favorite.setBackgroundResource(R.color.button_favorite_bg_2);
                    btn_favorite.setTag(1001);

                    if (!AppUtils.isSavedConference(conf))
                    {
                        AppUtils.saveConference(conf);
                    }
                    Toast.makeText(getActivity(), "Favoritter", Toast.LENGTH_LONG).show();
                }else{
                    btn_favorite.setBackgroundResource(R.color.button_favorite_bg_1);
                    btn_favorite.setTag(1000);

                    AppUtils.removeConference(conf);
                }
            }
        });

    //---Hear Later Button---//
        if (AppUtils.isSavedHearLaterConference(conf))
        {
            btn_hear_later.setTag(2001);
            btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_2);
        }else{
            btn_hear_later.setTag(2000);
            btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_1);
        }

        btn_hear_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int)btn_hear_later.getTag() == 2000)
                {
                    btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_2);
                    btn_hear_later.setTag(2001);

                    if (!AppUtils.isSavedHearLaterConference(conf))
                    {
                        AppUtils.saveHearLaterConference(conf);
                    }
                    Toast.makeText(getActivity(), "Hør senere", Toast.LENGTH_LONG).show();
                }else{
                    btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_1);
                    btn_hear_later.setTag(2000);

                    AppUtils.removeHearLaterConference(conf);
                }
            }
        });
    }

    @Override
    public void pauseClicked() {
        try{
            if (!((MainActivity)getActivity()).mediaPlayer.isPlaying())
            {
                checkbox.setImageResource(R.drawable.ic_play);
                checkbox2.setImageResource(R.drawable.ic_play_large);
                checkbox.setTag(5000);
                checkbox2.setTag(6000);
            }else{
                checkbox.setImageResource(R.drawable.pause1);
                checkbox2.setImageResource(R.drawable.pause_large);
                checkbox.setTag(5001);
                checkbox2.setTag(6001);
            }
        }catch(Exception e){

        }
    }
}
