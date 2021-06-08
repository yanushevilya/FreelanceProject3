package com.example.freelanceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GitUserAdapter  extends RecyclerView.Adapter<GitUserAdapter.ViewHolder>{

    // 2. объявили константы
    private final LayoutInflater inflater;
    private final List<GitUser> gitUsers;


    GitUserAdapter(Context context, List<GitUser> gitUsers) {  // 3. в качестве параметров передаем Context - это активити, из которого вызывается адаптер и в котором будет отображаться наш RecyclerView
        // 4. инициализируем наши константы
        this.gitUsers = gitUsers;
        this.inflater = LayoutInflater.from(context);
    }

    // 5. методы Адаптера:
    @Override
    // 5.1. делает из list_item.xml обычную View и передает ее в качестве параметра ViewHolder(view)
    public GitUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // 5.2. делает привязку ViewHolder к определенной позиции (как-то так)
    public void onBindViewHolder(GitUserAdapter.ViewHolder holder, int position) {
        GitUser gitUser = gitUsers.get(position); // получает объект GitUser из List'а согласно переданной позиции
        // и берет из него данные для их присвоения элементам Holder'а
        holder.idView.setText(gitUser.getId());
        holder.loginView.setText(gitUser.getLogin());
        holder.changesCountView.setText(gitUser.getChangesCount());
    }

    @Override
    // 5.3. возвращает кол-во позиций
    public int getItemCount() {
        return gitUsers.size();
    }

    // 1. создает Holder на основе view'шек из list_item.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView idView, loginView, changesCountView;
        ViewHolder(View view){
            super(view);
            idView = (TextView)view.findViewById(R.id.tvIdUser);
            loginView = (TextView) view.findViewById(R.id.tvLoginUser);
            changesCountView = (TextView) view.findViewById(R.id.tvChangesCount);
        }
    }
}