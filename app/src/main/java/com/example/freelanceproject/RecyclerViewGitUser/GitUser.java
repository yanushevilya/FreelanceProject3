package com.example.freelanceproject.RecyclerViewGitUser;

public class GitUser {
    String login;
    String id;
    String changesCount;

    public GitUser(String login, String id, String changesCount) {
        this.login = login;
        this.id = id;
        this.changesCount = changesCount;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String  getChangesCount() {
        return changesCount;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChangesCount(String changesCount) {
        this.changesCount = changesCount;
    }
}
