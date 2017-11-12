package com.uottawa.thirstycactus.taskcactus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.uottawa.thirstycactus.taskcactus.AddTask;
import com.uottawa.thirstycactus.taskcactus.EditUser;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.UserListview;

/**
 * Created by michelbalamou on 11/2/17.
 *
 * This class represents the view with the USER list.
 */

public class UsersFragment extends Fragment {


    private TextView addTaskText;

    // Temporary data: TO BE CHANGED BY PETER ++++
    private ListView userList;
    private String[] username = {"Michel Balamou", "Nathanael Adams", "Peter Nguyen", "Julie Tourrilhes"};

    private String[] firstname = {"Michel", "Nathanael", "Peter", "Julie"};
    private String[] lastname = {"Balamou", "Adams", "Nguyen", "Tourrilhes"};

    private int[] chores = {5, 4, 2, 4};
    // Temporary data: TO BE CHANGED BY PETER ----


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment, container, false);


        userList = view.findViewById(R.id.userList);
        UserListview usr = new UserListview(getActivity(), username, chores);
        userList.setAdapter(usr);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Start a new activity with user information and statistics
                // as well as the user editor activity
                Intent intent = new Intent(getActivity(), EditUser.class);

                intent.putExtra("F_NAME", firstname[i]);
                intent.putExtra("L_NAME", lastname[i]);

                startActivity(intent);
            }
        });


        addTaskText = view.findViewById(R.id.addTaskText);
        addTaskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTask.class);

                startActivity(intent);
            }
        });

        return view;
    }


}

