package org.insbaixcamp.reus.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddItem extends AppCompatActivity {

    EditText etNombre, etDescripcion, etResponsable;

    RadioGroup rg;

    RadioButton low, mid, high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Objects.requireNonNull(getSupportActionBar()).hide();


        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etResponsable = findViewById(R.id.etResponsable);

        rg = findViewById(R.id.radioGroup);

        low = findViewById(R.id.low);
        mid = findViewById(R.id.mid);
        high = findViewById(R.id.high);

        // Obtener el ID del RadioButton seleccionado
        int selectedId = rg.getCheckedRadioButtonId();

        // Obtener el RadioButton seleccionado
        RadioButton selectedRadioButton = findViewById(selectedId);

        // Obtener el texto del RadioButton seleccionado
        String urgency = selectedRadioButton.getText().toString();

        String name = etNombre.getText().toString();

        String description = etDescripcion.getText().toString();

        String responsable = etResponsable.getText().toString();

        Task task = new Task(name, description, urgency, responsable);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        String userId = user.getUid();


        // Obtener las referencias a la base de datos
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = databaseRef.child("tasks");
        DatabaseReference userRef = tasksRef.child(userId);

        // Guardar la tarea en la base de datos
        userRef.push().setValue(task);

    }

}