package com.zabrzeski.kamil.concurrentsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kamil on 2015-06-14.
 */
public class ProfileActivity extends Activity {
    int id;
    int groupid;
    Button btnViewProducts;
    Button btnNewProduct;
    private String personName;
    private TextView nameTx;
    private Button singOutbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameTx = (TextView) findViewById(R.id.name);
        singOutbt = (Button) findViewById(R.id.singOut);
        singOutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSingOut();
            }
        });

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
//        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);

        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                if (groupid == 1) {
                    Intent i = new Intent(getApplicationContext(), AllTasksActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), AllTasksActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personName = extras.getString("name");
            id = extras.getInt("id");
            groupid = extras.getInt("group_id");
        }
        nameTx.setText("Hi," + personName);
    }

    private void attemptSingOut() {
        finish();
        Intent goback = new Intent(this, LoginActivity.class);
        startActivity(goback);
    }
}
