package com.example.yanghanwen.subbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 *
 *  * Copyright © 2018 Hanwen Yang, CMPUT301, University of Alberta - All Rights Reserved.
 *  * You may use, distribute or modify this code under terms and conditions of Code of Student Behavior at
 *  *  University of Alberta.
 *  * You can find a copy of the license in this project, otherwise please contact at
 *  *   hyang4@ualberta.ca
 *
 *
 */

/**
 * MAIN ACTIVITY which is used for creating, initializing,
 * saving, loading files. Sublist shows all the subscriptions
 * infos that been added by user.
 *
 *
 * layout: activity_main.xml
 *
 *
 * @author Hanwen Yang
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "file.sav";

    private ListView listView;
    private Button deleteButton, quitButton;
    public static ArrayList<Subscription> subList = new ArrayList<Subscription>();
    public static ArrayAdapter<Subscription> adapter;
    private TextView total_charge;



    /**
     * The activity that been called firstly once
     * the app starts going
     * @param savedInstanceState
     * @throws NullPointerException
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        deleteButton = (Button) findViewById(R.id.delete);
        quitButton = (Button) findViewById(R.id.quit);

        //function called to load subscriptions from Json file
        loadFromFile();


        //setting up an adapter for subList
        //http://www.viralandroid.com/2016/02/floating-action-button-fab-with-android-listview.html
        //2018-01-28
        adapter = new ArrayAdapter<Subscription>(this, android.R.layout.simple_list_item_1, subList);
        listView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
             * add button click action
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, add_new.class);
                startActivity(intent);
            }
        });



        //adding obtained information to ListView
        setResult(RESULT_OK);
        Intent intent = getIntent();

        if (intent.hasExtra("get_name")) {
            String returnName = intent.getStringExtra("get_name").toString();
            String returnDate = intent.getStringExtra("get_date").toString();
            //https://stackoverflow.com/questions/24492037/send-float-from-activity-to-activity
            //2018-01-29
            Bundle bundle = getIntent().getExtras();
            float returnCharge = bundle.getFloat("get_charge");

            String returnComment = intent.getStringExtra("get_comment").toString();
            Subscription subs = new Subscription(returnName, returnDate, returnCharge, returnComment);

            subList.add(subs);
            adapter.notifyDataSetChanged();
            saveInFile();
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * listview entries click action
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Edit.class);
                //https://stackoverflow.com/questions/3405369/getting-the-index-of-clicked-item-in-a-listview
                //2018-01-30
                //subList.get(i),get index of the item that been clicked
                //and send to Edit activity
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });

        //add all charges up
        float sum = 0.0f;
        total_charge = (TextView) findViewById(R.id.sum);
        for(int i = 0; i < subList.size(); i++) {
            sum += subList.get(i).getCharge();
        }
        adapter.notifyDataSetChanged();
        saveInFile();
        total_charge.setText(Float.toString(sum));



        quitButton.setOnClickListener(new View.OnClickListener() {

            /**
             * quit button click action
             * @param view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {

            /**
             * delete button click action
             * @param view
             */
            @Override
            public void onClick(View view) {
                total_charge.setText("0.0");
                adapter.clear();
                saveInFile();
                Toast.makeText(MainActivity.this, "Subscriptions cleared", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }



    /**
     * Resume to the last executed activity
     * this is for adding all monthly charge together
     */
    @Override
    protected void onResume() {
        super.onResume();
        float sum = 0.0f;
        for(int i = 0; i < subList.size(); i++) {
            sum += subList.get(i).getCharge();
        }
        total_charge.setText(Float.toString(sum));
    }



    /**
     * load any information stored in Gson when we open up
     * the app
     * @throws FileNotFoundException
     * @throws NumberFormatException
     */
    protected void loadFromFile() throws NumberFormatException{
        try {
           FileInputStream fis = openFileInput(FILENAME);
           BufferedReader in = new BufferedReader(new InputStreamReader(fis));

           //https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            //2018-01-29
           Gson gson = new Gson();
           Type listType = new TypeToken<List<Subscription>>(){}.getType();
           subList = gson.fromJson(in, listType);

           if(subList == null) {
               subList = new ArrayList<Subscription>();
           }
        } catch(FileNotFoundException e) {
            subList = new ArrayList<Subscription>();
        } catch (NumberFormatException e1) {

        }
    }


    /**
     * Saving action, saves information in listView so that
     * it doesn't lose when we close the app
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


    /**
     * Final step for the activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}