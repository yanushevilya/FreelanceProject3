package com.example.freelanceproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.freelanceproject.RecyclerViewGitUser.GitUser;
import com.example.freelanceproject.RecyclerViewGitUser.GitUserAdapter;
import com.example.freelanceproject.RecyclerViewUserRepos.UserRepos;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.freelanceproject.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGitUsers;
    private GitUserAdapter gitUserAdapter;
    private ArrayList<GitUser> listGitUser = new ArrayList<GitUser>();
    private static ArrayList<UserRepos> userReposList = new ArrayList<UserRepos>();
    private String url = "https://api.github.com/users";

    public static ArrayList<UserRepos> getUserReposList() {
        return userReposList;
    }

    // класс для запуска в отдельном потоке задачи подключения по URL и получения JSON
    class GitQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            // 1. делаем запрос по URL и в ответ получаем JSON, который записываем в переменную - response
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
            try {
                JSONArray jsonArray = new JSONArray(response);

                // 2. считываем данные из полученного JSON и сразу записываем их в наш List для адаптера RecyclerView
                for (int i = 0; i < jsonArray.length(); i++ ) {
                    String login = jsonArray.getJSONObject(i).get("login").toString();
                    String id = jsonArray.getJSONObject(i).get("id").toString();
                    String changesCount = jsonArray.getJSONObject(i).get("node_id").toString();
                    // (сделать чтобы вместо записи в List, полученные из JSON данные записывались в БД)
                    // (а в отдельном методе, вызываемом после init(), сделать запись из БД в List)
                    // (таким образом данные будут всегда на устройстве, а по-возможности БД будет обновляться из Интернета)
                    listGitUser.add(new GitUser(login, id, changesCount));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // класс для запуска в отдельном потоке задачи подключения по URL и получения JSON
    // (тап по Холдеру и получение репозиториев пользователя)
    class UserQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            // 1. делаем запрос по URL и в ответ получаем JSON, который записываем в переменную - response
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
            try {
                JSONArray jsonArray = new JSONArray(response);

                Context context = MainActivity.this;
                Class destinationActivity = DetailsActivity.class;
                Intent detailsActivityIntent = new Intent(context, destinationActivity);
                startActivity(detailsActivityIntent);


                // 2. считываем данные из полученного JSON и сразу записываем их в наш List для адаптера RecyclerView
                String repo = "";
                for (int i = 0; i < jsonArray.length(); i++ ) {
                    repo = jsonArray.getJSONObject(i).get("html_url").toString();
                    userReposList.add(new UserRepos(repo));
                    System.out.println(repo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        // ОБРАБОТКА НАЖАТИЯ
        // определяем слушателя нажатия элемента в списке
        GitUserAdapter.OnGitUserClickListener gitUserClickListener = new GitUserAdapter.OnGitUserClickListener() {
            @Override
            public void onGitUserClick(GitUser gitUser, int position) {
                System.out.println(gitUser.getLogin());
                try {
                    userReposList.clear(); // очищаем List для RV репозитория юзера, перед тапом по новому юзеру
                    new UserQueryTask().execute(new URL("https://api.github.com/users/" + gitUser.getLogin() + "/repos"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };


        // помещаем наш List в адаптер для RecyclerView
        gitUserAdapter = new GitUserAdapter(this, listGitUser, gitUserClickListener); //
        recyclerViewGitUsers.setAdapter(gitUserAdapter);
    }

    public void init() {

        try {
            new GitQueryTask().execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}