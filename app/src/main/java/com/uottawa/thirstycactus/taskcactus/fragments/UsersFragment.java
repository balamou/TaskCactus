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

import com.uottawa.thirstycactus.taskcactus.EditUser;
import com.uottawa.thirstycactus.taskcactus.R;
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
    private DataSingleton dataSingleton = DataSingleton.getInstance();


    // =============================================================================================

    // METHODS

    // =============================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.users_fragment, container, false);

        userList = view.findViewById(R.id.userList);
        UserListview usr = new UserListview(getActivity(), dataSingleton.getUsers());
        userList.setAdapter(usr);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Start a new activity with user information and statistics
                // as well as the user editor activity
                Intent intent = new Intent(getActivity(), EditUser.class);

                intent.putExtra("F_NAME", dataSingleton.getUsers().get(i).getFirstName());
                intent.putExtra("L_NAME", dataSingleton.getUsers().get(i).getLastName());

                startActivity(intent);
            }
        });

        return view;
    }

}

