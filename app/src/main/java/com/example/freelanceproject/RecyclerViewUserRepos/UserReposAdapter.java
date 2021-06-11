package com.example.freelanceproject.RecyclerViewUserRepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.freelanceproject.R;

import java.util.List;

public class UserReposAdapter extends RecyclerView.Adapter<UserReposAdapter.ViewHolder>{

    // 2. объявили константы
    private final LayoutInflater inflater;
    private final List<UserRepos> userReposList;

    // 3. в качестве параметров передаем Context - это активити, из которого вызывается адаптер
    // и в котором будет отображаться наш RecyclerView
    public UserReposAdapter(Context context, List<UserRepos> userReposList) {
        // 4. инициализируем наши константы
        this.userReposList = userReposList;
        this.inflater = LayoutInflater.from(context);
    }

    // 5. методы Адаптера:
    @Override
    // 5.1. делает из list_item.xml обычную View и передает ее в качестве параметра ViewHolder(view)
    public UserReposAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_user_repos, parent, false);
        return new UserReposAdapter.ViewHolder(view);
    }

    @Override
    // 5.2. делает привязку ViewHolder к определенной позиции (как-то так)
    public void onBindViewHolder(UserReposAdapter.ViewHolder holder, int position) {
        UserRepos userRepos = userReposList.get(position); // получает объект GitUser из List'а согласно переданной позиции
        // и берет из него данные для их присвоения элементам Holder'а
        holder.repoView.setText(userRepos.getRepo());
    }

    @Override
    // 5.3. возвращает кол-во позиций
    public int getItemCount() {
        return userReposList.size();
    }

    // 1. создает Holder на основе view'шек из list_item.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView repoView;
        ViewHolder(View view){
            super(view);
            repoView = (TextView)view.findViewById(R.id.tvRepo);
        }
    }
}
