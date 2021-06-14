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

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.User;

import static com.example.freelanceproject.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private RecyclerView recyclerViewGitUsers;
    private GitUserAdapter gitUserAdapter;
    private ArrayList<GitUser> listGitUser = new ArrayList<GitUser>();
    private static ArrayList<UserRepos> userReposList = new ArrayList<UserRepos>();
    private String url = "https://api.github.com/users";

    public static ArrayList<UserRepos> getUserReposList() {
        return userReposList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance(); // получаем экземпляр БД
        init();
    }

    public void init() {
        // ??? - ВЫЗОВ МЕТОДА, КОТОРЫЙ ЧИТАЕТ ПУШ С FCM ОБ ИЗМЕНЕНИЯХ В РЕПОЗИТОРИЯХ
        // IF - ЕСЛИ ЕСТЬ ИЗМЕНЕНИЯ, ТО ВЫЗЫВАЕМ GitQueryTask()-->saveToRealm() (ЧТОБЫ ПОЛУЧИТЬ НОВЫЙ json и распарсить его в REALM)
        // ЕСЛИ НЕТ ИЗМЕНЕНИЙ, ТО ПРОСТО ЗАПУСКАЕМ getListGitUsers() ДЛЯ ИНИЦИАЛИЗАЦИИ RecyclerView
//        if (listGitUser.size()>0) {                         //del - добавить сюда МЕТОД вместо IF
//            getListGitUsers();                              //del
//        } else {                                            //del
            try {
                new GitQueryTask().execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
//        }                                                   //del
    }




    // ===================================
    // поток для получения JSON про ЮЗЕРОВ
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

        // парсинг полученного в response JSON и поместить данные из него в наш Realm
        @Override
        protected void onPostExecute(String response) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                // 2. считываем данные из полученного JSON и сразу записываем их в наш List для адаптера RecyclerView
                for (int i = 0; i < jsonArray.length(); i++ ) {
                    String login = jsonArray.getJSONObject(i).get("login").toString();
                    String changesCount = jsonArray.getJSONObject(i).get("node_id").toString();
                    saveGitUserToRealm(login, changesCount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGitUserToRealm(String login, String changesCount) {
        RealmResults<GitUser> gitUsers = realm.where(GitUser.class).findAll(); // получаем всю БД по модели Contact
        try {
            realm.beginTransaction();
            GitUser dataGitUser = new GitUser();
            dataGitUser.setLogin(login);
            dataGitUser.setChangesCount(changesCount);
//            realm.copyToRealm(dataGitUser);       // просто добавляет данные в БД
            realm.copyToRealmOrUpdate(dataGitUser); // обновляет БД по @PrimaryKey или добавляет, если по @PrimaryKey записи не найдено

            realm.commitTransaction();
        } catch (Exception e) {
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw new RuntimeException(e);
        }
        getListGitUsers();
    }

    public void getListGitUsers() {
        RealmResults<GitUser> realmResults = realm.where(GitUser.class).sort("login").findAll();
        listGitUser.clear();
        listGitUser.addAll(realmResults);

        // заполняет List для RV MainActivity и инициализирует RecyclerView
        if(realmResults.size() > 0) {
//            contactAdapter.setArray(contactArrayList);
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

            gitUserAdapter = new GitUserAdapter(this, listGitUser, gitUserClickListener); //
            recyclerViewGitUsers.setAdapter(gitUserAdapter);

        }
    }





    // ========================================
    // поток для получения JSON про РЕПОЗИТОРИИ
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


                // 2. считываем данные из полученного JSON и сразу записываем их в Realm
                String repo = "";
                String login = "";
                for (int i = 0; i < jsonArray.length(); i++ ) {
                    repo = jsonArray.getJSONObject(i).get("html_url").toString();
                    login = jsonArray.getJSONObject(i).getJSONObject("owner").get("login").toString();
                    saveUserReposToRealm(repo, login);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUserReposToRealm(String repo, String login) {
        RealmResults<UserRepos> userRepos = realm.where(UserRepos.class).findAll(); // получаем всю БД по модели Contact
        try {
            realm.beginTransaction();
            UserRepos dataUserRepos = new UserRepos();
            dataUserRepos.setRepo(repo);
            dataUserRepos.setLogin(login);
            realm.copyToRealmOrUpdate(dataUserRepos); // обновляет БД по @PrimaryKey или добавляет, если по @PrimaryKey записи не найдено
            realm.commitTransaction();
        } catch (Exception e) {
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw new RuntimeException(e);
        }
        getListUserRepos(login);
    }

    // заполняет List для RV DetailsActivity
    public void getListUserRepos(String login) {
        RealmResults<UserRepos> realmResults = realm.where(UserRepos.class).sort("repo").findAll();
        userReposList.clear();
        for (int i=0; i<realmResults.size(); i++) {
            if (realmResults.get(i).getLogin().equals(login)) {
                userReposList.add(realmResults.get(i));
            }
        }
    }
}