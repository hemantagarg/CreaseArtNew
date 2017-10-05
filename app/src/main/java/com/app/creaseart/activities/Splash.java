package com.app.creaseart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.creaseart.R;
import com.app.creaseart.utils.AppConstant;
import com.app.creaseart.utils.AppUtils;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // Log.e("gcm", AppUtils.getGcmRegistrationKey(getApplicationContext()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUtils.getUserId(getApplicationContext()).equalsIgnoreCase("")) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    if (AppUtils.getUserRole(getApplicationContext()).equalsIgnoreCase(AppConstant.PICKUPBOYTYPE)) {
                        startActivity(new Intent(getApplicationContext(), PickupBoyDashboard.class));
                        finish();
                    } else if (AppUtils.getUserRole(getApplicationContext()).equalsIgnoreCase(AppConstant.USERTYPE)) {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                    }else {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                    }
                }
            }
        }, 2000);

    }
}
