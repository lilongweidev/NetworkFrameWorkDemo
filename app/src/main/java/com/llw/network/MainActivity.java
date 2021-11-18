package com.llw.network;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.llw.network.api.ApiService;
import com.llw.network.bean.WallPaperResponse;
import com.llw.network.environment.NetworkEnvironmentActivity;
import com.llw.network.observer.BaseObserver;
import com.llw.network.utils.KLog;

import java.util.List;

/**
 * @author llw
 */
public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    //点击6次
    private final int CLICK_NUM = 6;
    //点击时间间隔5秒
    private final int CLICK_INTERVAL_TIME = 5000;
    //上一次的点击时间
    private long lastClickTime = 0;
    //记录点击次数
    private int clickNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixClick();
            }
        });

        //访问网络
        requestNetwork();
    }

    /**
     * 访问网络
     */
    @SuppressLint("CheckResult")
    private void requestNetwork() {
        NetworkApi.createService(ApiService.class)
                .getWallPaper()
                .compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                    @Override
                    public void onSuccess(WallPaperResponse wallPaperResponse) {
                        List<WallPaperResponse.ResBean.VerticalBean> vertical = wallPaperResponse.getRes().getVertical();
                        if (vertical != null && vertical.size() > 0) {
                            String imgUrl = vertical.get(0).getImg();
                            Glide.with(MainActivity.this).load(imgUrl).into(imageView);
                        } else {
                            Toast.makeText(MainActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        KLog.e("MainActivity", e.toString());
                        Toast.makeText(MainActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    /**
     * 连续6次点击
     */
    public void sixClick() {
        //点击的间隔时间不能超过5秒
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= CLICK_INTERVAL_TIME || lastClickTime == 0) {
            lastClickTime = currentClickTime;
            clickNum = clickNum + 1;
        } else {
            //超过5秒的间隔
            //重新计数 从1开始
            clickNum = 1;
            lastClickTime = 0;
            return;
        }
        if (clickNum == CLICK_NUM) {
            //重新计数
            clickNum = 0;
            lastClickTime = 0;
            /*实现点击多次后的事件*/
            Toast.makeText(MainActivity.this, "设置网络环境", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, NetworkEnvironmentActivity.class));
        }
    }
}
