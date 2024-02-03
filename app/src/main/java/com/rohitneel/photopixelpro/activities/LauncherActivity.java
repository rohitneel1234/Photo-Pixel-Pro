/**
 * Author: Rohit Neel
 */

package com.rohitneel.photopixelpro.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitneel.photopixelpro.R;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();

    private Button btnLogin;
    private int wantToExit=0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.launcher_status_bar_color));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_launcher);
        btnLogin = findViewById(R.id.btnLogin);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(wantToExit==1)
        {
            moveTaskToBack(true);
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
        }
        else {
            Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
        }
        wantToExit++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wantToExit=0;
            }
        }).start();
    }

}

