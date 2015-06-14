package com.zabrzeski.kamil.concurrentsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.internal.na;

/**
 * Created by Kamil on 2015-06-14.
 */
public class ProfileActivity extends Activity {
    private String personID;
    private TextView nameTx;
    private Button singOutbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameTx= (TextView) findViewById(R.id.name);
        singOutbt = (Button) findViewById(R.id.singOut);
        singOutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSingOut();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            personID = extras.getString("name");
        }
        nameTx.setText("Hi,"+personID);
    }

    private void attemptSingOut() {
        finish();
        Intent goback = new Intent(this,LoginActivity.class);
        startActivity(goback);
    }
}
