package com.seges.newpodcast.ui.fragments;

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
import android.widget.TextView;

import com.seges.newpodcast.ui.activities.MainActivity;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.R;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jamariya on 17-02-2016.
 */
public class MyListFragment extends Fragment {

    public static final String ACTIVE_TAG = "_active_tag";

    /*@Bind(R.id.tabs)
    TabLayout tabLayout;*/

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.category_1)
    TextView category_1;

    @Bind(R.id.category_2)
    TextView category_2;

    private int active_tab = 0;

    public static final String[] tabs = new String[]{"Favorites", "Listen Later"};

    public static MyListFragment getInstance(int active_tab){
        MyListFragment fragment = new MyListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ACTIVE_TAG, active_tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            active_tab = getArguments().getInt(ACTIVE_TAG, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_mylist, container, false);
        ButterKnife.bind(this, view);

        AppUtils.removePlayEpisode();

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //onTextColorChanged(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(active_tab);
        /*tabLayout.setupWithViewPager(viewPager);*/


        ((MainActivity)getActivity()).tv_fragment_title.setText("Mine lister");
        ((MainActivity)getActivity()).tv_fragment_title.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).img_white_logo.setVisibility(View.VISIBLE);
        return view;
    }

/*    public void onTextColorChanged(int id){
        switch (id) {
            case 0:
                category_2.setBackgroundColor(getResources().getColor(R.color.text_blue));
                category_1.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                break;
            case 1:
                category_1.setBackgroundColor(getResources().getColor(R.color.text_blue));
                category_2.setBackgroundColor(getResources().getColor(R.color.category_unselected));
                break;

        }
    }*/
    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private HashMap mPageReferenceMap;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new HashMap<Integer, MyProgramListFragment>();
        }

        public Fragment getItem(int num) {
            Fragment fragment=null;
            switch (num){
                case 0:
                    fragment = MyProgramListFragment.getInstance(true);
                    break;
                case 1:
                    fragment = MyProgramListFragment.getInstance(false);
                    break;
            }

            mPageReferenceMap.put(num, fragment);
            return fragment;
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }


        public MyProgramListFragment getFragment(int key) {
            return (MyProgramListFragment)mPageReferenceMap.get(key);
        }
    }

    @OnClick(R.id.category_1)
    public void SelectFirst(){
        viewPager.setCurrentItem(0);
    }
    @OnClick(R.id.category_2)
    public void SelectSecond(){
        viewPager.setCurrentItem(1);
    }
}
