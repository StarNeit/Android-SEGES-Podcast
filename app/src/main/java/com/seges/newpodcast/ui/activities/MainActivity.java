package com.seges.newpodcast.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seges.newpodcast.model.Conference;
import com.seges.newpodcast.model.PauseClickListener;
import com.seges.newpodcast.ui.fragments.AboutFragment;
import com.seges.newpodcast.utils.AppUtils;
import com.seges.newpodcast.R;
import com.seges.newpodcast.ui.fragments.DiscoverFragment;
import com.seges.newpodcast.ui.fragments.HomeFragment;
import com.seges.newpodcast.ui.fragments.MyListFragment;
import com.seges.newpodcast.ui.fragments.MyProfileFragment;
import com.seges.newpodcast.ui.fragments.MyTagsFragment;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    public List<Conference> g_conferenceList;

    public MediaPlayer mediaPlayer;

    private PauseClickListener pauseClickListener;

    @Bind(R.id.background_id)
    ImageView background_id;

    @Bind(R.id.menu_list)
    ListView menu_list;

    @Bind(R.id.player_view)
    LinearLayout player_view;

    @Bind(R.id.player_title)
    TextView player_title;

    @Bind(R.id.img_white_logo)
    public ImageView img_white_logo;

    @Bind(R.id.tv_fragment_title)
    public TextView tv_fragment_title;

    Fragment currentFragment;

    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    private int nSeekbarWidth = 0;

    @Bind(R.id.seekbar_front)
    ImageView seekbar_front;

    @Bind(R.id.seekbar_front2)
    ImageView seekbar_front2;

    @Bind(R.id.player_pause)
    ImageView btn_media_play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        super.initTitleViews();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // load
        setCurrentFragment(new HomeFragment());

        menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setCurrentFragment(new HomeFragment());
                        break;
                    case 1:
                        setCurrentFragment(new MyListFragment());//-----
                        break;
                    case 2:
                        setCurrentFragment(new DiscoverFragment());//-----
                        break;
                    /*case 3:
//                        setCurrentFragment(new MyProfileFragment());//-----
                        break;*/
                    case 3:
                        setCurrentFragment(new MyTagsFragment());//-----
                        break;
                    case 4:
                        setCurrentFragment(new AboutFragment());
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


    //---timer---//
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }

        findViewById(R.id.seekbar_front2).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (nSeekbarWidth == 0)
                {
                    nSeekbarWidth = right - left;
                }
            }
        });

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mediaPlayer.isPlaying())
                        {
                            double total_time = mediaPlayer.getDuration();
                            double current_pos = mediaPlayer.getCurrentPosition();

                            double pos = nSeekbarWidth * (current_pos / total_time);

                            ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams((int)pos, seekbar_front2.getHeight());
                            seekbar_front.setLayoutParams(params);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removePlayEpisode();
    }

    public void hideTopMenu() {
        background_id.setImageResource(R.drawable.subpage_short);

    }

    public void showTopMenu() {
        background_id.setImageResource(R.drawable.homepage_short);

    }

    public void backToRootView(){
        super.backToRootView();
        setCurrentFragment(new HomeFragment());
    }

    // load fragment
    public void setCurrentFragment(Fragment fragment) {
        /*currentFragment = fragment;
        if (fragment instanceof HomeFragment) {
            showTopMenu();
        } else {
            hideTopMenu();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//.addToBackStack(fragment.getClass().getName());
//        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();*/

        if (fragment instanceof HomeFragment) {
            openView(fragment, new FragmentStack("",true));
        } else {
            openView(fragment, new FragmentStack("",false));
        }
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        super.onBackPressed();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle the camera action
            setCurrentFragment(new HomeFragment());
            showTopMenu();
        } else if (id == R.id.nav_list) {
            // Handle the camera action
            setCurrentFragment(new MyListFragment());
            hideTopMenu();
        }
        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.menu_icon)
    public void openMenu() {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    public void showPlayerView(String title) {
        player_title.setText(title);
        player_view.setVisibility(View.VISIBLE);
    }

    public void hidePlayerView() {
        player_view.setVisibility(View.GONE);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @OnClick(R.id.player_pause)
    public void pauseClicked() {
        pausePlayer();
        if (pauseClickListener != null) {
            pauseClickListener.pauseClicked();
        }
    }

    public void pausePlayer() {
        Log.d(TAG, "pausePlayer. isPlaying: " + mediaPlayer.isPlaying());
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_media_play.setImageResource(R.drawable.play_orange);
        }else{
            mediaPlayer.start();
            btn_media_play.setImageResource(R.drawable.pause_orange);
        }
    }

    public void startPlayer(String audioUrl, final String title, PauseClickListener listener) {
        Log.d(TAG, "startPlayer");
        this.pauseClickListener = listener;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();

            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    mediaPlayer.start();
                    showPlayerView(title);

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            hidePlayerView();
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnClick (R.id.back)
    public void jumpBack() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 30*1000);
        }
    }

    @OnClick (R.id.forward)
    public void jumpForward() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 30*1000);
        }
    }
}
