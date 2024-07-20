package com.openclassrooms.entrevoisins.ui.neighbour_data;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.RemoveFavoriteEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NeighbourDataActivity extends AppCompatActivity {

    int myYellowColor;
    int myGreyColor;

    Neighbour neighbour;
    int neighbourIndex;

    private NeighbourApiService mApiService;

    @BindView(R.id.activity_neighbour_data_txt_name)
    TextView txtBigName;

    @BindView(R.id.activity_neighbour_data_card1_txt_name)
    TextView txtName;

    @BindView(R.id.activity_neighbour_data_card1_txt_adress)
    TextView txtAdress;

    @BindView(R.id.activity_neighbour_data_card1_txt_phone)
    TextView txtPhone;

    @BindView(R.id.activity_neighbour_data_card1_txt_web)
    TextView txtWeb;

    @BindView(R.id.activity_neighbour_data_img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.activity_neighbour_data_img_cancel)
    ImageView imgCancel;

    @BindView(R.id.activity_neighbour_data_card2_txt_aboutMe)
    TextView txtAboutMe;

    @BindView(R.id.activity_neighbour_data_bt_favorite)
    FloatingActionButton btFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_data);
        ButterKnife.bind(this);

        mApiService = DI.getNeighbourApiService();

        myYellowColor = getResources().getColor(R.color.colorYellow);
        myGreyColor = getResources().getColor(R.color.colorLightGrey);

        neighbour = (Neighbour) getIntent().getSerializableExtra("NeighbourData");
        List<Neighbour> tempList = mApiService.getNeighbours();
        neighbourIndex = tempList.indexOf(neighbour);
        neighbour = tempList.get(neighbourIndex);

        fillContent(neighbour);
    }

    @OnClick(R.id.activity_neighbour_data_bt_favorite)
    public void clickFavorite() {
        mApiService.invertFavoriteState(neighbour);
        neighbour = mApiService.getNeighbours().get(neighbourIndex);

        favoriteToColor(neighbour.getFavorite());
    }

    @OnClick(R.id.activity_neighbour_data_img_cancel)
    public void clickCancel() {
        finish();
    }

    public void fillContent(Neighbour neighbourData) {
        txtBigName.setText(neighbourData.getName());
        txtName.setText(neighbourData.getName());
        txtAdress.setText(neighbourData.getAddress());
        txtPhone.setText(neighbourData.getPhoneNumber());
        txtWeb.setText("www.facebook.fr/" + neighbourData.getName());
        txtAboutMe.setText(neighbourData.getAboutMe());

        Glide.with(imgAvatar.getContext())
                .load(neighbourData.getAvatarUrl())
                .into(imgAvatar);

        favoriteToColor(neighbour.getFavorite());
    }

    private void favoriteToColor(boolean stateFavorite) {
        if(stateFavorite == true) btFavorite.setImageTintList(ColorStateList.valueOf(myYellowColor));
        else btFavorite.setImageTintList(ColorStateList.valueOf(myGreyColor));
    }
}