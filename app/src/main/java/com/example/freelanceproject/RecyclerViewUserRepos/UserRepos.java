package com.example.freelanceproject.RecyclerViewUserRepos;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRepos extends RealmObject {
    String login;
    @PrimaryKey
    String repo;

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
}