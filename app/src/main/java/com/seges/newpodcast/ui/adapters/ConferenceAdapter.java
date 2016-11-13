package com.seges.newpodcast.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ConferenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, PauseClickListener{
    public static final String TAG = ConferenceAdapter.class.getSimpleName();

    private Context context;
    private List<Conference> conferenceList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Conference currentlyPlayedConference;
    private ConferenceViewHolder currentConferenceHolder;



    public ConferenceAdapter(Context context){
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
            holder.adapter_root.setBackgroundResource(R.color.adapter_bg_1);
        }else{
            holder.adapter_root.setBackgroundResource(R.color.adapter_bg_2);
        }

    //---Expanding Current Playing Item when refresh---//
        if(AppUtils.isEpisodePlaying(conference.getId())){
            holder.pod_detail.setVisibility(View.VISIBLE);
            holder.option_layout.setVisibility(View.VISIBLE);
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
                if (holder.pod_detail.getVisibility() == View.VISIBLE) {
                    AppUtils.removePlayEpisode();
                    holder.pod_detail.setVisibility(View.GONE);
                    holder.option_layout.setVisibility(View.GONE);
                    holder.checkbox.setVisibility(View.VISIBLE);
                } else {
                    AppUtils.savePlayEpisode(conference.getId());
                    holder.pod_detail.setVisibility(View.VISIBLE);
                    holder.option_layout.setVisibility(View.VISIBLE);
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

        /*
        if(AppUtils.isSavedConference(conference)){
            Drawable drawable = new IconicsDrawable(context)
                    .icon(GoogleMaterial.Icon.gmd_check_circle)
                    .color(context.getResources().getColor(R.color.colorPrimary))
                    .sizeDp(20);
            holder.checkbox.setImageDrawable(drawable);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnConferenceUnChecked((ImageButton) v, conference);
                }
            });
        }else{
            //holder.checkbox.setImageResource(R.drawable.ic_checkbox_blank_circle_outline_grey600_24dp);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                onItemClickListener.OnConferenceChecked((ImageButton) v, conference);

                }
            });
        }*/


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
                    onItemClickListener.OnPlayClicked(conference, ConferenceAdapter.this);
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
                    onItemClickListener.OnPlayClicked(conference, ConferenceAdapter.this);
                    if (currentConferenceHolder != null) {
                        displayPlayButtons(currentConferenceHolder);
                    }
                    currentlyPlayedConference = conference;
                    currentConferenceHolder = holder;
                    displayPauseButtons(holder);
                }
            }
        });

    //---Favorite Button---//
        if (AppUtils.isSavedConference(conference))
        {
            holder.btn_favorite.setBackgroundResource(R.color.button_favorite_bg_2);
            holder.btn_favorite.setTag(1001);
        }else{
            holder.btn_favorite.setBackgroundResource(R.color.button_favorite_bg_1);
            holder.btn_favorite.setTag(1000);
        }

        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int)holder.btn_favorite.getTag() == 1000)
                {
                    holder.btn_favorite.setBackgroundResource(R.color.button_favorite_bg_2);
                    holder.btn_favorite.setTag(1001);

                    if (!AppUtils.isSavedConference(conference))
                    {
                        AppUtils.saveConference(conference);
                    }
                    Toast.makeText(context, "Favoritter", Toast.LENGTH_LONG).show();
                }else{
                    holder.btn_favorite.setBackgroundResource(R.color.button_favorite_bg_1);
                    holder.btn_favorite.setTag(1000);

                    AppUtils.removeConference(conference);
                }
            }
        });

    //---Hear Later Button---//
        if (AppUtils.isSavedHearLaterConference(conference))
        {
            holder.btn_hear_later.setTag(2001);
            holder.btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_2);
        }else{
            holder.btn_hear_later.setTag(2000);
            holder.btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_1);
        }

        holder.btn_hear_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int)holder.btn_hear_later.getTag() == 2000)
                {
                    holder.btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_2);
                    holder.btn_hear_later.setTag(2001);

                    if (!AppUtils.isSavedHearLaterConference(conference))
                    {
                        AppUtils.saveHearLaterConference(conference);
                    }
                    Toast.makeText(context, "Hør senere", Toast.LENGTH_LONG).show();
                }else{
                    holder.btn_hear_later.setBackgroundResource(R.color.button_listen_later_bg_1);
                    holder.btn_hear_later.setTag(2000);

                    AppUtils.removeHearLaterConference(conference);
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
