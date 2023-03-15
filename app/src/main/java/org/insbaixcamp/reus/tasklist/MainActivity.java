package org.insbaixcamp.reus.tasklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private RecyclerView recyclerView;
    private Button button;
    private TaskListAdapter adapter;

    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.rvTasks);
        button = findViewById(R.id.bAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        adapter = new TaskListAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpRecyclerViewListener();

        // Obtener el objeto SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("mi_app", MODE_PRIVATE);

        // Obtener el valor del indicador de sesión
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

    }

    private void setUpRecyclerViewListener() {
        adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Context context = getApplicationContext();
                Toast.makeText(context,"Item Removed", Toast.LENGTH_LONG).show();

                items.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(int position) {
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.etTask);
        String itemText = input.getText().toString();

        if (!(itemText.equals(""))) {
            items.add(itemText);
            adapter.notifyDataSetChanged();
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }
    }
}