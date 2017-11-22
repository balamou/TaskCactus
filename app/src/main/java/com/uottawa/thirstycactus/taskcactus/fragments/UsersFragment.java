package com.uottawa.thirstycactus.taskcactus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.uottawa.thirstycactus.taskcactus.AddUser;
import com.uottawa.thirstycactus.taskcactus.EditUser;
import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.ViewSingleton;
import com.uottawa.thirstycactus.taskcactus.UserListview;
import com.uottawa.thirstycactus.taskcactus.domain.DataSingleton;

/**
 * Created by michelbalamou on 11/2/17.
 *
 * This class represents the view with the USER list.
 */

public class UsersFragment extends Fragment
{
    // ATTRIBUTES

    private ListView userList;
    private Button addUser;
    private UserListview usr;

    private DataSingleton dataSingleton = DataSingleton.getInstance();
    private ViewSingleton refreshView = ViewSingleton.getInstance();


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.users_fragment, container, false);

        // ++++
        userList = view.findViewById(R.id.userList);
        usr = new UserListview(getActivity(), dataSingleton.getUsers());
        userList.setAdapter(usr);
        refreshView.setAdapter(usr);

        // THIS METHOD IS EXECUTED EVERY TIME AN ITEM IS CLICKED IN THE LIST VIEW
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Start a new activity with user information and statistics
                Intent intent = new Intent(getActivity(), EditUser.class);

                intent.putExtra("USER_ID", i);

                startActivity(intent);
            }
        });

        addUser = view.findViewById(R.id.addUserBtn);
        addUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAddUser(v);
            }
        });
        // ----

        return view;
    }

    /**
     *  Event that occurs when cliking on Add User
     */
    public void onAddUser(View view)
    {
        Intent intent = new Intent(getActivity(), AddUser.class);
        intent.putExtra("USER_ID", -1);
        startActivity(intent);
    }


}

