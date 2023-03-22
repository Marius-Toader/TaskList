package org.insbaixcamp.reus.tasklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADD_TASK = 1;
    private ArrayList<Task> items;
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private TaskListAdapter adapter;
    private FirebaseAuth mAuth;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rvTasks);
        button = findViewById(R.id.bAdd);


        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });


        items = new ArrayList<>();
        adapter = new TaskListAdapter(this, items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Obtener la tarea que se va a eliminar
                Task taskToDelete = items.get(position);

                // Eliminar la tarea de Firebase
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                String userId = user.getUid();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference tasksRef = databaseRef.child("tasks");
                DatabaseReference userRef = tasksRef.child(userId);
                DatabaseReference taskRef = userRef.child(taskToDelete.getId());
                taskRef.removeValue();

                // Eliminar la tarea de la lista de tareas
                items.remove(position);

                // Notificar al adaptador que un elemento ha sido eliminado
                adapter.notifyItemRemoved(position);

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Si el usuario está arrastrando un elemento a la izquierda
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                    View itemView = viewHolder.itemView;
                    Paint paint = new Paint();
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete); // Reemplaza R.drawable.ic_delete con el ID de tu icono
                    float iconWidth = icon.getWidth();
                    float iconHeight = icon.getHeight();


                    // Dibujar fondo
                    paint.setColor(Color.RED);
                    RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    c.drawRect(background, paint);

                    // Dibujar el icono en el fondo del elemento arrastrado
                    c.drawBitmap(icon, itemView.getRight() - iconWidth - 50, itemView.getTop() + (itemView.getHeight() - iconHeight) / 2, paint);

                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mostrarLista();
    }

    private void mostrarLista() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        String userId = user.getUid();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = databaseRef.child("tasks");
        DatabaseReference userRef = tasksRef.child(userId);

        // Agregar listener para obtener las tareas del usuario
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Eliminar las tareas actuales del adaptador
                adapter.clearTasks();

                // Iterar sobre los hijos de la referencia del usuario
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    // Obtener la tarea y agregarla a una lista o hacer algo más con ella
                    Task task = taskSnapshot.getValue(Task.class);
                    // Hacer algo con la tarea obtenida

                    // Agregar la tarea al RecyclerView
                    adapter.addTask(task);

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores
            }
        });
    }

    // Sobrescribir el método onActivityResult para recibir la tarea agregada desde AgregarTareaActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprobar que la solicitud es para agregar una tarea y el resultado es OK
        if (requestCode == REQUEST_ADD_TASK && resultCode == RESULT_OK) {

            // Obtener la tarea desde el intent
            Task task = (Task) data.getSerializableExtra("task");

            // Agregar la tarea al RecyclerView
            adapter.addTask(task);

            adapter.notifyDataSetChanged();
        }
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