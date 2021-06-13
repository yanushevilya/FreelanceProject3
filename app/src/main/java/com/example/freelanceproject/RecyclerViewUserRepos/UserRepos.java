package com.example.freelanceproject.RecyclerViewUserRepos;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRepos extends RealmObject {
    @PrimaryKey
    String repo;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
}