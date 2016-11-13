package com.seges.newpodcast.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MyConferenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, PauseClickListener{

    private Context context;
    private List<Conference> conferenceList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public boolean is_favorited_list;
    private Conference currentlyPlayedConference;
    private ConferenceViewHolder currentConferenceHolder;


    public void setIs_favorited_list(boolean is_favorited_list) {
        this.is_favorited_list = is_favorited_list;
    }

    public MyConferenceAdapter(Context context){
        this.context=context;
    }

    public void setConferenceList(List<Conference> conferenceList) {
        this.conferenceList = conferenceList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.adapter_conference_row, viewGroup, false);
        ConferenceViewHolder viewHolder = new ConferenceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Conference conference = conferenceList.get(position);
        final ConferenceViewHolder holder = (ConferenceViewHolder) viewHolder;

    //---Adapter Cell Background Changing Every Time---//
        if(position%2==0){
            if(is_favorited_list){
                holder.adapter_root.setBackgroundResource(R.color.adapter_favorite_bg_1);
            }else{
                holder.adapter_root.setBackgroundResource(R.color.adapter_bg_1);
            }
        }else{
            if(is_favorited_list){
                holder.adapter_root.setBackgroundResource(R.color.adapter_favorite_bg_2);
            }else{
                holder.adapter_root.setBackgroundResource(R.color.adapter_bg_2);
            }
        }

    //---Expanding Current Playing Item when refresh---//
        if(AppUtils.isEpisodePlaying(conference.getId())){
            holder.pod_detail.setVisibility(View.VISIBLE);
            //holder.option_layout.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        }else{
            holder.pod_detail.setVisibility(View.GONE);
            holder.option_layout.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
        }

    //---Expand Selected Item & save episode ID when clicking---//
        holder.adapter_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.pod_detail.getVisibility()==View.VISIBLE){
                    AppUtils.removePlayEpisode();
                    holder.pod_detail.setVisibility(View.GONE);
                    holder.option_layout.setVisibility(View.GONE);
                    holder.checkbox.setVisibility(View.VISIBLE);
                }else{
                    AppUtils.savePlayEpisode(conference.getId());
                    holder.pod_detail.setVisibility(View.VISIBLE);
                    //holder.option_layout.setVisibility(View.VISIBLE);
                    holder.checkbox.setVisibility(View.GONE);
                }

            }
        });

    //---Set Necessary Datas---//
        String pods_title = conference.getTitle();
        int start_pos = pods_title.substring(14).indexOf(" ", 0) + 14;
        pods_title = pods_title.substring(0, start_pos) + "\n" + pods_title.substring(start_pos + 1).trim();

        holder.title.setText(pods_title);
        holder.description.setText(conference.getDescription());
        StringBuilder stringBuilder = new StringBuilder();
        for (String category : conference.getCategories()) {
            stringBuilder.append(category + " ");
        }
        holder.categories.setText(stringBuilder.toString());
        Picasso.with(context).load(conference.getThumbnailUrl()).into(holder.thumbnail);

    //---Small Play Button---//
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentlyPlayedConference == conference) {
                    //Pause playing
                    onItemClickListener.OnPauseClicked();
                    displayPlayButtons(holder);
                    currentConferenceHolder = null;
                    currentlyPlayedConference = null;
                } else {
                    //Start playing
                    onItemClickListener.OnPlayClicked(conference, MyConferenceAdapter.this);
                    if (currentConferenceHolder != null) {
                        displayPlayButtons(currentConferenceHolder);
                    }
                    currentlyPlayedConference = conference;
                    currentConferenceHolder = holder;
                    displayPauseButtons(holder);
                }
            }
        });

        //---Big Play Button---//
        holder.checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentlyPlayedConference == conference) {
                    //Pause playing
                    onItemClickListener.OnPauseClicked();
                    displayPlayButtons(holder);
                    currentConferenceHolder = null;
                    currentlyPlayedConference = null;
                } else {
                    //Start playing
                    onItemClickListener.OnPlayClicked(conference, MyConferenceAdapter.this);
                    if (currentConferenceHolder != null) {
                        displayPlayButtons(currentConferenceHolder);
                    }
                    currentlyPlayedConference = conference;
                    currentConferenceHolder = holder;
                    displayPauseButtons(holder);
                }
            }
        });

    }

    private void displayPlayButtons(ConferenceViewHolder holder) {
        if (holder != null) {
            holder.checkbox.setImageResource(R.drawable.ic_play);
            holder.checkbox2.setImageResource(R.drawable.ic_play_large);
        }
    }

    private void displayPauseButtons(ConferenceViewHolder holder) {
        if (holder != null) {
            holder.checkbox.setImageResource(R.drawable.pause1);
            holder.checkbox2.setImageResource(R.drawable.pause_large);
        }
    }



    @Override
    public int getItemCount() {
        return conferenceList.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void pauseClicked() {
        displayPlayButtons(currentConferenceHolder);
        currentlyPlayedConference = null;
        currentConferenceHolder = null;
    }


    static class ConferenceViewHolder extends RecyclerView.ViewHolder {

        //@Bind(R.id.checkbox)
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

        public ConferenceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            checkbox = (ImageButton)itemView.findViewById(R.id.checkbox);
            checkbox2 = (ImageButton)itemView.findViewById(R.id.checkbox2);
            btn_favorite = (TextView)itemView.findViewById(R.id.btn_favorite);
            btn_hear_later = (TextView)itemView.findViewById(R.id.btn_hear_later);

            conference_header = (LinearLayout)itemView.findViewById(R.id.conference_header);
            btn_session = (TextView)itemView.findViewById(R.id.btn_session);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            categories = (TextView)itemView.findViewById(R.id.categories);
            thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            show_conference_date = (TextView)itemView.findViewById(R.id.show_conference_date);
            option_layout = (LinearLayout)itemView.findViewById(R.id.option_layout);
            adapter_root = (LinearLayout)itemView.findViewById(R.id.adapter_root);
            pod_detail = (LinearLayout)itemView.findViewById(R.id.pod_detail);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnConferenceChecked(ImageButton imageButton, Conference conference);
        public void OnConferenceUnChecked(ImageButton imageButton, Conference conference);
        public void OnSessionClick(Conference conference);
        public void OnPlayClicked(Conference conference, PauseClickListener listener);
        public void OnPauseClicked();
    }
}
