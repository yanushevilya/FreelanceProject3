package com.example.freelanceproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.freelanceproject.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGitUsers;
    private GitUserAdapter gitUserAdapter;
    private ArrayList<GitUser> listGitUser = new ArrayList<GitUser>();
    private String url = "https://api.github.com/users";

    class GitQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;

            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        // здесь надо распарсить полученный в response JSON и поместить данные из него в наш массив listGitUser
        @Override
        protected void onPostExecute(String response) {
//            System.out.println("============" + response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        recyclerViewGitUsers = findViewById(R.id.rv_gitUsers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGitUsers.setLayoutManager(layoutManager);

        recyclerViewGitUsers.setHasFixedSize(true);

        gitUserAdapter = new GitUserAdapter(this, listGitUser); //
        recyclerViewGitUsers.setAdapter(gitUserAdapter);
    }

    public void init() {

        try {
            new GitQueryTask().execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        listGitUser.add(new GitUser("Andy", "1", "4"));
        listGitUser.add(new GitUser("Bob", "2", "3"));
        listGitUser.add(new GitUser("Den", "3", "0"));
        listGitUser.add(new GitUser("Ashley", "4", "1"));
        listGitUser.add(new GitUser("Freddy", "5", "1"));
        listGitUser.add(new GitUser("Garry", "6", "2"));
        listGitUser.add(new GitUser("John", "7", "2"));
        listGitUser.add(new GitUser("Sully", "8", "2"));
        listGitUser.add(new GitUser("Rob", "9", "2"));
        listGitUser.add(new GitUser("Pauel", "10", "2"));
        listGitUser.add(new GitUser("Penny", "11", "2"));
        listGitUser.add(new GitUser("Ronald", "12", "2"));
        listGitUser.add(new GitUser("George", "13", "2"));
        listGitUser.add(new GitUser("Stefany", "14", "2"));
        listGitUser.add(new GitUser("Steev", "15", "2"));
        listGitUser.add(new GitUser("Brenda", "16", "2"));
    }
}