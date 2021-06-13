package com.example.freelanceproject.RecyclerViewGitUser;

import io.realm.RealmObject;

public class GitUser extends RealmObject {
    String login;
    String changesCount;

    public String getLogin() {
        return login;
    }

    public String  getChangesCount() {
        return changesCount;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setChangesCount(String changesCount) {
        this.changesCount = changesCount;
    }
}
