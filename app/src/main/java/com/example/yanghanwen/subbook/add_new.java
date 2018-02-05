package com.example.yanghanwen.subbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Add_new activity is used to add new subscription
 * to the listview
 *
 * add_new links with MainActivity
 */
public class add_new extends AppCompatActivity {

    EditText edit_name;
    EditText edit_date;
    EditText edit_charge;
    EditText edit_comment;
    Button confirm;


    /**
     * First called when activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        edit_name = (EditText) findViewById(R.id.name);
        edit_date = (EditText) findViewById(R.id.date);
        edit_charge = (EditText) findViewById(R.id.charge);
        edit_comment = (EditText) findViewById(R.id.comment);
        confirm = (Button) findViewById(R.id.confirm);



        confirm.setOnClickListener(new View.OnClickListener() {

            /**
             * confirm button click action
             * @param view
             */
            @Override
            public void onClick(View view) {
                String name = edit_name.getText().toString();
                String date = edit_date.getText().toString();
                String charge_string = edit_charge.getText().toString();

                //See if the input format legal
                if (!(date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})"))) {
                    Toast.makeText(add_new.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                }

                else if(!(charge_string.matches("^[0-9.]*$"))) {
                    Toast.makeText(add_new.this, "Only numerical allowed in charge field", Toast.LENGTH_SHORT).show();
                }

                else if(charge_string.equals(".")) {
                    Toast.makeText(add_new.this, "Only numerical allowed in charge field", Toast.LENGTH_SHORT).show();
                }

                else if(name.isEmpty()) {
                    Toast.makeText(add_new.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else if(date.isEmpty()) {
                    Toast.makeText(add_new.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else if(charge_string.isEmpty()) {
                    Toast.makeText(add_new.this, "Charge cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    //if format is legal, send information to main activity and
                    //append at the bottom of the list using intent
                    float charge = Float.parseFloat(edit_charge.getText().toString());
                    String comment = edit_comment.getText().toString();
                    Intent intent = new Intent(add_new.this, MainActivity.class);
                    intent.putExtra("get_name", name);
                    intent.putExtra("get_date", date);
                    intent.putExtra("get_charge", charge);
                    intent.putExtra("get_comment", comment);
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(add_new.this, "Subscription added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

