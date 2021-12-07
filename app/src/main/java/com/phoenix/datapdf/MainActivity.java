package com.phoenix.datapdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.phoenix.datapdf.adapters.HomeAdapter;
import com.phoenix.datapdf.dbhelper.FarmerDBHelper;
import com.phoenix.datapdf.modals.DisplayData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_agri);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        FarmerDBHelper dbHelper = new FarmerDBHelper(this);
        List<DisplayData> dataList = dbHelper.getDisplayData();

        HomeAdapter adapter = new HomeAdapter(dataList, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}