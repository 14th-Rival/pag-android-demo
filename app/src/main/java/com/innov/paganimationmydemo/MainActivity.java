package com.innov.paganimationmydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innov.paganimationmydemo.Adapters.AssetsAdapter;
import com.innov.paganimationmydemo.Models.AssetsModel;

import org.libpag.PAGFile;
import org.libpag.PAGView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PAGView mPAGView;
    private PAGFile mPAGFile = null;
    private RelativeLayout mRelativeLayout;

    private RecyclerView mRecyclerViewAssets;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AssetsModel> assetsModelList = new ArrayList<>();
    private AssetsAdapter mAssetsAdapter;

    private TextView tv_Filename;
    private Button
            btn_playAnim,
            btn_stopAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializedActivity();
    }

    private void initializedActivity(){
        // PAG:
        // Kindly refer to this website for documentation
        // https://pag.io/api.html#/apis/android/org/libpag/package-summary.html

        tv_Filename = findViewById(R.id.tv_filename);
        btn_playAnim = findViewById(R.id.btn_play_anim);
        btn_stopAnim = findViewById(R.id.btn_stop_anim);
        mRelativeLayout = findViewById(R.id.background_render);
        mPAGView = new PAGView(MainActivity.this);
        mPAGView.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        mRelativeLayout.addView(mPAGView);
        mRecyclerViewAssets = findViewById(R.id.rv_assets);
        mRecyclerViewAssets.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mAssetsAdapter = new AssetsAdapter(assetsModelList(), this, MainActivity.this);
        mRecyclerViewAssets.setLayoutManager(mLayoutManager);
        mRecyclerViewAssets.setAdapter(mAssetsAdapter);

        btn_playAnim.setOnClickListener(v -> playAnim());
        btn_stopAnim.setOnClickListener(v -> stopAnim());
    }

    private List<AssetsModel> assetsModelList(){
        try {
            String[] str = getAssets().list("");
            if (assetsModelList.isEmpty()) {
                for (String s : str) {
                    if (s.endsWith(".pag")) {
                        AssetsModel assetsModel = new AssetsModel(s);
                        assetsModelList.add(assetsModel);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetsModelList;
    }

    private void stopAnim(){
        mPAGView.stop();
    }

    private void playAnim(){
        mPAGView.play();
    }

    public void setSelectedAsset(String s){
        if (s != null){
            tv_Filename.setText("Filename: "+s);
            mPAGFile = PAGFile.Load(getAssets(), s);
            mPAGView.setComposition(mPAGFile);
            mPAGView.setRepeatCount(0);
            mPAGView.play();
        }
    }
}