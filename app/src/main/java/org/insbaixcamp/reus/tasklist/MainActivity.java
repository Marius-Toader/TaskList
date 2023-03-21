package org.insbaixcamp.reus.tasklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private TaskListAdapter adapter;
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

                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);

            }
        });

        items = new ArrayList<>();
        adapter = new TaskListAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpRecyclerViewListener();


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Remove item from the list
                items.remove(position);

                // Notify the adapter that an item has been removed
                adapter.notifyItemRemoved(position);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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

    /*private void addItem(View view) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);

        builder1.setMessage("Escribe tu mensaje");
        builder1.setCancelable(true);

        builder1.setView(edittext);



        builder1.setPositiveButton(
                "Añadir",
                (dialog, id) -> {

                    String itemText = edittext.getText().toString();
                    if (!(itemText.equals(""))) {
                        items.add(itemText);
                        adapter.notifyDataSetChanged();
                        //edittext.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
                    }
                });
        builder1.setNegativeButton(
                "Cancelar",
                (dialog, id) -> dialog.cancel());


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }*/
}