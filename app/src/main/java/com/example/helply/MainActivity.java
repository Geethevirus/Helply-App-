package com.example.helply;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainMenuAdapter.UserClickListener {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.menuRv);

        List<String> usecaseName = new ArrayList<>();
        usecaseName.add("Text Recognition");
        usecaseName.add("Voice Recognition");
        usecaseName.add("Language Identification");

        MainMenuAdapter mm = new MainMenuAdapter(this,usecaseName,this::selectedUser);
        recyclerView.setAdapter(mm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void selectedUser(String useCase) {

        Intent i = new Intent(MainActivity.this, ImageScanner.class);
        i.putExtra("useCaseName",useCase);
        startActivity(i);

    }
}