package com.testing.cmcp.audiosample_log_res;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView test;
    public SessionManagement session;
    Button logout;


    private CountDownTimer timer = null;
    //private MediaCursorAdapter mediaAdapter = null;
    private ListView poplist = null;
    private String currentFile = "";
    private ImageButton stopbutton = null;
    private ImageButton playbutton = null;

    private FloatingActionButton yk = null;
    private MediaPlayer player = null;
    private MediaPlayer pb = null;
    private boolean isStarted = false;
    private TextView selelctedFile = null;

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private RadioButton t10, t15, t30, t60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManagement(getApplicationContext());



        logout = (Button) findViewById(R.id.bLogout);
        Intent intent = getIntent();
        if (intent.getStringExtra("name") != null){
            session.createLoginSession(intent.getStringExtra("name"), intent.getStringExtra("ut"));
        }







        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Intent getbacklogin = new Intent(MainActivity.this, Login.class);
                getbacklogin.putExtra("isLoggedin",session.isLoggedIn());
                MainActivity.this.finish();
                MainActivity.this.startActivity(getbacklogin);
            }
        });


        // OnCreat main section
        //player.setOnErrorListener(onError);




        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(0);
        //yk = (FloatingActionButton) findViewById(R.id.fab);
        player = MediaPlayer.create(this, R.raw.bach);
        timer = new CountClass(player.getDuration());
        pb = MediaPlayer.create(this, R.raw.dark_saga);

        stopbutton = (ImageButton) findViewById(R.id.stop);
        playbutton = (ImageButton) findViewById(R.id.play);

        t10 = (RadioButton) findViewById(R.id.t10);
        t10.setOnClickListener(onClickRadio);
        t15 = (RadioButton) findViewById(R.id.t15);
        t15.setOnClickListener(onClickRadio);
        t30 = (RadioButton) findViewById(R.id.t30);
        t30.setOnClickListener(onClickRadio);
        t60 = (RadioButton) findViewById(R.id.t60);
        t60.setOnClickListener(onClickRadio);

        playbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (player.isPlaying()) {
                    player.pause();
                    pb.pause();
                    timer.cancel();
                    playbutton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    if (isStarted) {
                        player.start();
                        pb.start();
                        timer.start();
                        playbutton.setImageResource(android.R.drawable.ic_media_pause);


                    } else {
                        startPlay();
                        playbutton.setImageResource(android.R.drawable.ic_media_pause);
                        timer.start();
                    }
                }

            }
        });

        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlay();
                playbutton.setImageResource(android.R.drawable.ic_media_play);

            }
        });



    }

    private View.OnClickListener onClickRadio = new View.OnClickListener(){


        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.t10:
                    timer = new CountClass(600000);
                    break;

                case R.id.t15:
                    timer = new CountClass(900000);
                    break;

                case R.id.t30:
                    timer = new CountClass(1800000);
                    break;

                case R.id.t60:
                    timer = new CountClass(3600000);
                    break;

            }
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();


        player.stop();
        player.reset();
        player.release();

        player = null;
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0:
                    rootView.setBackgroundResource(R.drawable.face2);
                    break;
                case 1:
                    rootView.setBackgroundResource(R.drawable.face1);
                    break;
                case 2:
                    rootView.setBackgroundResource(R.drawable.face2);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position );
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }

    private void startPlay() {
        //Log.i("Selected: ", file);
        player.stop();

        player = MediaPlayer.create(this, R.raw.bach);
        try {
            //player.setDataSource(file);
            //player.prepare();
            Log.i("PBBB duration: ", String.valueOf(player.getDuration()));
            pb.stop();

            switch (mViewPager.getCurrentItem()){
                case 0:
                    pb = MediaPlayer.create(this, R.raw.relax);
                    Log.i("Pasé por:", String.valueOf(R.raw.relax));
                    break;

                case 1:
                    pb = MediaPlayer.create(this, R.raw.cartoon);
                    Log.i("Pasé por:", String.valueOf(R.raw.cartoon));

                    break;

                case 2:
                    pb = MediaPlayer.create(this, R.raw.dark_saga);
                    break;

            }






            player.start();
            pb.start();
            pb.setLooping(true);
            player.setLooping(true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        /*
        poplist.setVisibility(View.GONE);
        stopbutton.setVisibility(View.GONE);
        yk.setVisibility(View.VISIBLE); */
        isStarted = true;
    }

    private void stopPlay() {
        player.stop();
        //player.reset();
        pb.stop();



        isStarted = false;
    }



    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {




        }
    };

    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return false;
        }
    };

    /*
    private class MediaCursorAdapter extends SimpleCursorAdapter {

        public MediaCursorAdapter(Context context, int layout, Cursor c) {
            super(context, layout, c,
                    new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION},
                    new int[]{R.id.displayname, R.id.title, R.id.duration}
            );
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView title = (TextView) view.findViewById(R.id.title);
            //TextView name = (TextView) view.findViewById(R.id.displayname);
            //TextView duration = (TextView) view.findViewById(R.id.duration);

            //name.setText(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));

            title.setText(cursor.getString(
                    cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));

            long durationInMs = Long.parseLong(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));

            double durationInMin = ((double) durationInMs / 1000.0) / 60.0;

            durationInMin = new BigDecimal(Double.toString(durationInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue();

            //duration.setText("" + durationInMin);

            view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            //View v = inflater.inflate(R.layout.listitem, parent, false);

            //bindView(v, context, cursor);

            //return v;
            return null;
        }
    }
    */
    private class CountClass extends CountDownTimer {
        public CountClass(long lel) {
            super(lel, 1000);
        }

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            stopPlay();
            playbutton.setImageResource(android.R.drawable.ic_media_play);

        }
    }
}
