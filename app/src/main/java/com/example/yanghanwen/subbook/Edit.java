package com.example.yanghanwen.subbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.example.yanghanwen.subbook.MainActivity.adapter;
import static com.example.yanghanwen.subbook.MainActivity.subList;


/**
 * Edit activity is used to check the details of a specific subscription
 * It is able to change information if needed
 * and then store details using Gson
 *
 *
 * Edit links with MainActivity
 */
public class Edit extends AppCompatActivity {
    private static final String FILENAME = "file.sav";


    /**
     * First executed when activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Intent intent_get = getIntent();

        final TextView textViewName = (TextView) findViewById(R.id.edit_name);
        final TextView textViewDate = (TextView) findViewById(R.id.edit_date);
        final TextView textViewCharge = (TextView) findViewById(R.id.edit_charge);
        final TextView textViewComment = (TextView) findViewById(R.id.edit_comment);


        Button delete_button = (Button) findViewById(R.id.delete);
        Button update_button = (Button) findViewById(R.id.update);



        //Getting index sent from MainActivity
        //meanwhile set all textviews to information comes from MainActivity
        //https://stackoverflow.com/questions/7074097/how-to-pass-integer-from-one-activity-to-another
        //2018-01-30
        final int index_i = intent_get.getIntExtra("index", 0);
        textViewName.setText(subList.get(index_i).getName());
        textViewDate.setText(subList.get(index_i).getDate());
        textViewCharge.setText(Float.toString(subList.get(index_i).getCharge()));
        textViewComment.setText(subList.get(index_i).getComment());




        delete_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Delete button click action, as the button is clicked
             * the entry is removed from the listview
             * @param view
             */
            @Override
            public void onClick(View view) {
                subList.remove(index_i);
                Intent intent = new Intent(Edit.this, MainActivity.class);
                saveInFile();
                adapter.notifyDataSetChanged();
                finish();
                Toast.makeText(Edit.this, "Subscription deleted", Toast.LENGTH_SHORT).show();
            }
        });




        final Intent intent_edit = new Intent(Edit.this, MainActivity.class);
        update_button.setOnClickListener(new View.OnClickListener() {

            /**
             * update button click action
             * as the information changing, the corresponding info
             * will be changed in MainActivity
             * @param view
             */
            @Override
            public void onClick(View view) {
                String name_edit = textViewName.getText().toString();
                String date_edit = textViewDate.getText().toString();
                String string_charge = textViewCharge.getText().toString();

                //check if the input is legal
                if (!(date_edit.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})"))) {
                    Toast.makeText(Edit.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                }

                else if (!(string_charge.matches("^[0-9.]*$"))) {
                    Toast.makeText(Edit.this, "Only numerical allowed in charge field", Toast.LENGTH_SHORT).show();
                }

                else if (string_charge.equals(".")) {
                    Toast.makeText(Edit.this, "Only numerical allowed in charge field", Toast.LENGTH_SHORT).show();
                }

                else {
                    float charge_edit = Float.parseFloat(textViewCharge.getText().toString());
                    String comment_edit = textViewComment.getText().toString();
                    subList.get(index_i).setName(name_edit);
                    subList.get(index_i).setDate(date_edit);
                    subList.get(index_i).setCharge(charge_edit);
                    subList.get(index_i).setComment(comment_edit);
                    startActivity(intent_edit);
                    saveInFile();
                    finish();
                    Toast.makeText(Edit.this, "Info changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Save file in case any change has been made
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveInFile() {
        try {

            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}

