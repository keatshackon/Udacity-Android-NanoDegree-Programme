/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private  TextView mErrorMsg;

    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = findViewById(R.id.et_search_box);

        mUrlDisplayTextView = findViewById(R.id.tv_url_display);

        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json);

        mErrorMsg = findViewById(R.id.tv_error_message_display);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
    }
    void makeGithubSearchQuery(){
        if(!(mSearchBoxEditText.getText().toString().equals(""))){
            String githubQuery = mSearchBoxEditText.getText().toString();
            URL  githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
            assert githubSearchUrl != null;
            mUrlDisplayTextView.setText(githubSearchUrl.toString());

            Log.i("Main","keats");
           new GithubQueryTask().execute(githubSearchUrl);

        }else {
            Toast.makeText(getApplicationContext(),"Url is required",Toast.LENGTH_LONG).show();
        }

    }
    public void showJsonDataView(){
        mErrorMsg.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    public  void showErrorMessage(){
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMsg.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String githubSearchResult1 = null;
            try{
                githubSearchResult1 = NetworkUtils.getResponseFromHttpUrl(searchURL);

            }catch (Exception e){
                e.printStackTrace();
                showErrorMessage();
            }
            return githubSearchResult1;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && !s.equals("")){
                mSearchResultsTextView.setText(s);
                showJsonDataView();
            }else{
                showErrorMessage();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemSelected = item.getItemId();
        if(menuItemSelected == R.id.action_search){
//            Toast.makeText(getApplicationContext(),"Search is selected",Toast.LENGTH_LONG).show();
            makeGithubSearchQuery();
        }
        return super.onOptionsItemSelected(item);
    }
}
