package com.seges.newpodcast.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.seges.newpodcast.R;
import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.model.OnConferencesLoadedListener;
import com.seges.newpodcast.model.RSSLoader;
import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.utils.TagsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jamariya on 17-02-2016.
 */
public class HomeFragment extends Fragment implements OnConferencesLoadedListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    List<Conference> conferenceList;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.category_1)
    TextView category_1;

    @Bind(R.id.category_2)
    TextView category_2;

    @Bind(R.id.category_3)
    TextView category_3;

    public static final String[] tabs = new String[]{"Personlig Nyheds Podcasts", "Alle Nyheds Podcasts", "Lange Podcasts"};
    private ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onTextColorChanged(position);
                updateView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SelectSecond();

        ((MainActivity)getActivity()).tv_fragment_title.setVisibility(View.GONE);
        ((MainActivity)getActivity()).img_white_logo.setVisibility(View.GONE);

        ((MainActivity)getActivity()).img_white_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).backToRootView();
            }
        });
        return view;
    }

    private void updateView() {
        int index = viewPager.getCurrentItem();
        ProgramListFragment fragment = ((ViewPagerAdapter)viewPager.getAdapter()).getFragment(index);

        if (fragment != null) {
            switch (index) {
                case 0:
                    fragment.updateData(selectPreferredConferences(conferenceList));
                    break;
                case 1:
                    fragment.updateData(conferenceList);
                    break;
                case 2:
                    fragment.updateData(selectLongConferences(conferenceList));
                    break;
            }
        } else {
            Log.e(TAG, "Failed to retrieve Fragment object");
        }

    }

    private List<Conference> selectPreferredConferences(List<Conference> conferences) {
        //TODO Implement

    //---Get MyTags---//
        List<Integer> my_tags = TagsUtil.getInstance().getSavedConference();
        String []category_list = getResources().getStringArray(R.array.category_list);
        for(int i=0;i< TagsUtil.NO_TAGS;i++) {
            if (!my_tags.contains(TagsUtil.check_id[i])) {
                category_list[i] = "";
            }
        }

    //---Filtering---//
        ArrayList<Conference> preferred_list = new ArrayList<Conference>();
        for (int i = 0 ; i < conferences.size(); i ++)
        {
            ArrayList<String> categories = conferences.get(i).getCategories();
            int j;
            for (j = 0; j < category_list.length; j ++)
            {
                if (categories.contains(category_list[j]))
                {
                    break;
                }
            }
            if (j != category_list.length)
            {
                preferred_list.add(conferences.get(i));
            }
        }
        return preferred_list;
    }

    private List<Conference> selectLongConferences(List<Conference> conferences) {
        ArrayList<Conference> result = new ArrayList<Conference>();
        /*for (Conference conference : conferences) {
            if (conference.getCategories().contains("Lange podcast")) {
                result.add(conference);
            }
        }*/

        for (int i = 0 ; i < conferences.size(); i ++)
        {
            ArrayList<String> categories = conferences.get(i).getCategories();
            if (categories.contains("Lange podcast"))
            {
                result.add(conferences.get(i));
            }
        }
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFeed();
    }


    private void loadFeed() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        RSSLoader.getInstance().loadConferences(this);
    }

    public void onTextColorChanged(int id) {
        switch (id) {
            case 0:
                category_1.setBackgroundColor(getResources().getColor(R.color.text_blue));
                category_2.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                category_3.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                break;
            case 1:
                category_2.setBackgroundColor(getResources().getColor(R.color.text_blue));
                category_1.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                category_3.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                break;
            case 2:
                category_3.setBackgroundColor(getResources().getColor(R.color.text_blue));
                category_1.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                category_2.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                break;
        }
    }

    @Override
    public void onConferencesLoaded(List<Conference> conferences) {
        Log.d(TAG, "loaded " + conferences.size() + " items");

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        conferenceList = conferences;
        ((MainActivity)getActivity()).g_conferenceList = conferenceList;
        updateView();
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private HashMap mPageReferenceMap;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new HashMap<Integer, ProgramListFragment>();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }


        @Override
        public Fragment getItem(int position) {
            Fragment myFragment = ProgramListFragment.newInstance(position);
            mPageReferenceMap.put(position, myFragment);
            return myFragment;
        }

        public void destroyItem (ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public ProgramListFragment getFragment(int key) {
            return (ProgramListFragment)mPageReferenceMap.get(key);
        }
    }

    @OnClick(R.id.category_1)
    public void SelectFirst() {
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.category_2)
    public void SelectSecond() {
        viewPager.setCurrentItem(1);
    }

    @OnClick(R.id.category_3)
    public void SelectThird() {
        viewPager.setCurrentItem(2);
    }

}
