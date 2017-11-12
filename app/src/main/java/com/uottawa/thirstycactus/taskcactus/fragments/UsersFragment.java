package com.uottawa.thirstycactus.taskcactus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uottawa.thirstycactus.taskcactus.R;
import com.uottawa.thirstycactus.taskcactus.UserListview;

/**
 * Created by michelbalamou on 11/2/17.
 *
 * This class represents the view with the USER list.
 */

public class UsersFragment extends Fragment {

    private ListView userList;
    private String[] username = {"Michel Balamou", "Nate Adams", "Peter Nguyen"};
    private int[] chores = {5, 4, 2};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment, container, false);


        userList = view.findViewById(R.id.userList);
        UserListview usr = new UserListview(getActivity(), username, chores);
        userList.setAdapter(usr);


        return view;
    }


}

