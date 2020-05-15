package com.example.searchhere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

public static final String TAG= "ADE";
EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.btn);
        et1=findViewById(R.id.et1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final  String text=et1.getText().toString();
                updatetextView(text);
            }
        });
    }
    private void updatetextView(String text){
     String link="https://api.github.com/search/users?q="+text;
        //NetworkTask networkTask=new NetworkTask();
       // networkTask.execute("https://api.github.com/search/users?q=harshit");
        try {
            makeNetworkcall(link);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void makeNetworkcall(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result =response.body().string();

            Gson gson =new Gson();
            ApiResult apiResult=gson.fromJson(result,ApiResult.class);

                // ArrayList<GithubUser> users=parsejson(result);
               // Log.e(TAG,"started"+ users.size());
                final GitHubAdapter gitHubAdapter=new GitHubAdapter(apiResult.getItems());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView recyclerView = findViewById(R.id.rvusers);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(gitHubAdapter);
                    }
                });

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        }
        );}
     class NetworkTask extends AsyncTask<String,Void,String> implements com.example.searchhere.NetworkTask {
         @Override
         protected String doInBackground(String... strings) {
             String stringurl  =strings[0];
             URL url= null;
             try {
                 url = new URL(stringurl);
             }
             catch (MalformedURLException e) {
                 e.printStackTrace();
             }
             try {
                 HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                 InputStream inputStream=httpURLConnection.getInputStream();
                 Scanner scanner=new Scanner(inputStream);
                 scanner.useDelimiter("\\A");
                 if (scanner.hasNext()){
                     String s=scanner.next();
                     return s;
                 }


             } catch (IOException e) {
                 e.printStackTrace();
             }
             return "failed to reload";
         }
         @Override
         public void onPostExecue(String s){
             super.onPostExecute(s);
            // TextView textView=findViewById(R.id.textview);
           //  textView.setText(s);
             ArrayList<GithubUser> users=parsejson(s);
             Log.e(TAG,"started"+ users.size());
             GitHubAdapter gitHubAdapter=new GitHubAdapter(users);
             RecyclerView recyclerView=findViewById(R.id.rvusers);
             recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
             recyclerView.setAdapter(gitHubAdapter);
         }
     }
     ArrayList<GithubUser>parsejson(String s){
        ArrayList<GithubUser> Githubusers= new ArrayList<>();
         try {
             JSONObject root=new JSONObject(s);
             JSONArray items= root.getJSONArray("items");
             for(int i=0;i<items.length();i++){
                 JSONObject object=  items.getJSONObject(i);
                 String login= object.getString("login");
                 Integer id=object.getInt("id");
                 String avatar=object.getString("avatar_url");
                 Double score=object.getDouble("score");
                 String html=object.getString("html_url");

                 GithubUser githubuser=new GithubUser(login,id,avatar,score,html);
                 Githubusers.add(githubuser);
             }

         } catch (JSONException e) {
             e.printStackTrace();
         }
         return Githubusers;
     }
}
    