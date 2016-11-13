package com.seges.newpodcast.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seges.newpodcast.R;
import com.seges.newpodcast.ui.fragments.HomeFragment;

import java.util.Stack;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseFragmentActivity extends FragmentActivity {

    private Context mContext;

    protected Stack<FragmentStack> fragmentStacks = new Stack<>();

    protected ImageView img_top;

    protected TextView tv_fragment_title;

    protected ImageView img_white_logo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (fragmentStacks.size() > 1) {
            backView();
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            System.exit(1);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure to Exit?").setPositiveButton("YES", dialogClickListener)
                    .setNegativeButton("NO", dialogClickListener).show();
        }
    }

    protected void initTitleViews(){
        img_top = (ImageView) findViewById(R.id.background_id);
        tv_fragment_title = (TextView) findViewById(R.id.tv_fragment_title);
        img_white_logo = (ImageView) findViewById(R.id.img_white_logo);

        mContext = this;
    }

    protected void backToRootView() {
        while (!fragmentStacks.isEmpty()) {
            backView();
        }
    }

    protected void openView(final Fragment fragment, final FragmentStack fragmentStack) {
        if (fragmentStack.getShowBack())
        {
            img_top.setImageResource(R.drawable.homepage_short);
        }else{
            img_top.setImageResource(R.drawable.subpage_short);
        }
        openView(fragment, fragmentStack, false);
    }

    protected void openView(final Fragment fragment, final FragmentStack fragmentStack, final boolean openAsRoot) {
        if (fragmentStack.getShowBack())
        {
            img_top.setImageResource(R.drawable.homepage_short);
        }else{
            img_top.setImageResource(R.drawable.subpage_short);
        }

        if (openAsRoot) {
            backToRootView();
        }

        String id = System.currentTimeMillis() + "";
        fragmentStack.setId(id);
        fragmentStacks.push(fragmentStack);

        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .add(R.id.container, fragment, id)
                .commit();

    }

    protected void backView() {
        FragmentStack fragmentStack = fragmentStacks.pop();

        String id = fragmentStack.getId();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(id);
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .remove(fragment)
                .commit();

        if (!fragmentStacks.isEmpty()) {
            fragmentStack = fragmentStacks.peek();

            if (fragmentStack.getShowBack())
            {
                img_top.setImageResource(R.drawable.homepage_short);
                tv_fragment_title.setVisibility(View.GONE);
                img_white_logo.setVisibility(View.GONE);
            }else{
                img_top.setImageResource(R.drawable.subpage_short);
                tv_fragment_title.setVisibility(View.VISIBLE);
                img_white_logo.setVisibility(View.VISIBLE);
            }

            //show calendar
            Fragment curFragment = getSupportFragmentManager().findFragmentByTag(fragmentStack.getId());
            if (curFragment instanceof HomeFragment) {
                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .show(curFragment)
                        .commit();
            }
        }
    }

}
