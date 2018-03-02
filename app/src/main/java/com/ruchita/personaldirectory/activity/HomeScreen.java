package com.ruchita.personaldirectory.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ruchita.personaldirectory.R;
import com.ruchita.personaldirectory.adapter.DirectoryAdapter;
import com.ruchita.personaldirectory.models.User;

import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<User> usersList = User.listAll(User.class);

        if (usersList != null) {
            DirectoryAdapter directoryAdapter = new DirectoryAdapter(this, usersList);
            recyclerView.setAdapter(directoryAdapter);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFloating:
                Intent i = new Intent(HomeScreen.this, AddDirectoryScreen.class);
                startActivityForResult(i, 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            List<User> usersList = User.listAll(User.class);

            if (usersList != null) {
                DirectoryAdapter directoryAdapter = new DirectoryAdapter(this, usersList);
                recyclerView.setAdapter(directoryAdapter);
            }
        }
    }
}
