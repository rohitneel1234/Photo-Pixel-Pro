package com.rohitneel.photopixelpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;

public class ContactUs extends AppCompatActivity {

    private ImageView linkToGmail;
    EditText editTextSubject,editTextMessage;
    TextView txtMsg, txtEmailID;
    Button send;
    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        linkToGmail = findViewById(R.id.mailSupport);
        editTextSubject=(EditText)findViewById(R.id.etSubject);
        editTextMessage=(EditText)findViewById(R.id.etMessage);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtEmailID= findViewById(R.id.txtEmailID);
        mSession = new SessionManager(getApplicationContext());

      /*  if (currPass.isEmpty()) {
        }*/
        /*Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        Toast.makeText(getApplicationContext(),"Width"+dpWidth, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Height"+dpHeight, Toast.LENGTH_LONG).show();*/


        if(mSession.loadState()){
            editTextSubject.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            editTextMessage.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            txtMsg.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            txtEmailID.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }

        send=(Button)findViewById(R.id.button1);
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String subject = editTextSubject.getText().toString();
                String message = editTextMessage.getText().toString();

                if (subject.isEmpty() && message.isEmpty()) {
                    editTextSubject.setError("Please enter subject!");
                    editTextSubject.requestFocus();
                    editTextMessage.setError("Please enter message!");
                    editTextMessage.requestFocus();
                } else if (subject.isEmpty()) {
                    editTextSubject.setError("Please enter subject!");
                    editTextSubject.requestFocus();
                } else if (message.isEmpty()) {
                    editTextMessage.setError("Please enter message!");
                    editTextMessage.requestFocus();
                } else {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"rohitneel.app.feedback@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    //need this to prompts email client only
                    email.setType("message/rfc822");
                    email.setPackage("com.google.android.gm");//Added G-mail Package to forcefully open Gmail App
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            }

        });


        linkToGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"rohitneel.app.feedback@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSession.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}