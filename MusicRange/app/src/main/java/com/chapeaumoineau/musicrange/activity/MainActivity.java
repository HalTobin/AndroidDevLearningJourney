package com.chapeaumoineau.musicrange.activity;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.MainPagerAdapter;
import com.chapeaumoineau.musicrange.activity.dialog.AlbumOrPlaylistContentDialog;
import com.chapeaumoineau.musicrange.activity.fragments.AlbumFragment;
import com.chapeaumoineau.musicrange.activity.fragments.PlayerFragment;
import com.chapeaumoineau.musicrange.activity.fragments.PlaylistFragment;
import com.chapeaumoineau.musicrange.activity.fragments.SetlistFragment;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.injections.DI;
import com.chapeaumoineau.musicrange.injections.Injection;
import com.chapeaumoineau.musicrange.injections.ViewModelFactory;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.Scan;
import com.chapeaumoineau.musicrange.services.player.MasterPlayer;
import com.chapeaumoineau.musicrange.viewModel.MusicViewModel;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chapeaumoineau.musicrange.utils.playerValue.*;


public class MainActivity extends AppCompatActivity implements AlbumOrPlaylistContentDialog.PickDialogTitleListener {

    @BindView(R.id.main_tab)
    TabLayout myTabLayout;
    @BindView(R.id.main_viewPager)
    ViewPager myViewPager;
    @BindView(R.id.main_toolbar)
    Toolbar myToolbar;

    private MainPagerAdapter myMainPagerAdapter;

    private MusicViewModel musicViewModel;
    private MasterPlayer myPlayer;

    private static final String CLIENT_ID = "SPOTIFY_CLIENT_ID";
    private static final String REDIRECT_URI = "REDIRECT_CALLBACK";
    private SpotifyAppRemote mSpotifyAppRemote;

    private List<Album> dbAlbum = new ArrayList<>();

    ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        myPlayer = DI.getPlayerApi();
        myPlayer.initSubPlayer();

        setSupportActionBar(myToolbar);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { initApp(); }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { token.continuePermissionRequest(); }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.main_menu_scan_local_item:
                try {
                    scanLocal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.main_menu_connect_spotify_item:
                SpotifyAppRemote.connect(this, connectionParams,
                        new Connector.ConnectionListener() {
                            @Override
                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                mSpotifyAppRemote = spotifyAppRemote;
                                System.out.println("SUCCESS Spotify Connected !");

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println("ERROR Spotify Not Connected : " + throwable.getMessage());
                                // Something went wrong when attempting to connect! Handle errors here
                            }
                        });
            case R.id.main_menu_scan_spotify_item:
                scanSpotify();

        }
        return super.onOptionsItemSelected(item);
    }

    //TODO - Get library token
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        // The next 19 lines of the code are what you need to copy & paste! :)
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }*/


    void initApp() {
        configureViewModel();
        myMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        myMainPagerAdapter.addFragment(SetlistFragment.newInstance());
        myMainPagerAdapter.addFragment(PlayerFragment.newInstance());
        myMainPagerAdapter.addFragment(AlbumFragment.newInstance(musicViewModel));
        myMainPagerAdapter.addFragment(PlaylistFragment.newInstance());

        myViewPager.setAdapter(myMainPagerAdapter);
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTabLayout));
        myTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(myViewPager));

        myPlayer.loadPreferences(this);
    }

    void scanLocal() throws InterruptedException {

        Scan myScan = new Scan(this);

        ProgressDialog myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setMessage("Scanning Phone. Please wait...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.create();
        myProgressDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                myScan.sync(LOCAL);
                myProgressDialog.dismiss();
            }
        };
        mThread.start();

        /*ProgressDialog myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setMessage("Scanning Phone. Please wait...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.create();
        myProgressDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String mySelection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                Cursor songCursor = contentResolver.query(songUri, null, mySelection, null, null);

                if(songCursor != null && songCursor.moveToFirst()) {
                    do {
                        //GET METADATA FROM SONG
                        String myTitle = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String myPath = songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        int myDuration = Integer.parseInt(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                        String myArtist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String myAlbum = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                        //SAVE SONG AND ITS METADATA TO LOCAL DATABASE
                        Song mySong = new Song(1, myPath, myTitle, myDuration, true);
                        musicViewModel.createSongWithAll(mySong, new Artist(myArtist), new Album(myAlbum));
                    } while(songCursor.moveToNext());
                }
                songCursor.close();
                while (musicViewModel.getOP_RUNNING() != 0);
                myProgressDialog.dismiss();
            }
        };
        mThread.start();*/
    }

    void scanSpotify() {

        Scan myScan = new Scan(this);

        ProgressDialog myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setMessage("Scanning Phone. Please wait...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.create();
        myProgressDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                myScan.sync(SPOTIFY);
                myProgressDialog.dismiss();
            }
        };
        mThread.start();
    }

    @Override
    public void sendTitle(List<SongWithAll> setList, int position, int mode) throws IOException {
        switch(mode) {
            case InstructionForSetlist.LOADALBUM:
                myPlayer.setCurrentSetlist(setList, position);
                myViewPager.setCurrentItem(1);
                break;
            case InstructionForSetlist.SETNEXT:
                myPlayer.setNextSong(setList.get(position));
                break;
            case InstructionForSetlist.ADDTOQUEU:
                myPlayer.addToQueu(setList.get(position));
                break;
        }
    }

    private void configureViewModel() {
        ViewModelFactory myViewModelFactory = Injection.provideViewModelFactory(this);
        this.musicViewModel = ViewModelProviders.of(this, myViewModelFactory).get(MusicViewModel.class);
        this.musicViewModel.setFilesDir(this.getFilesDir());
    }

}