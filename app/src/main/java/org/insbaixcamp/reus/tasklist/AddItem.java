package org.insbaixcamp.reus.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

public class AddItem extends AppCompatActivity {

    EditText etNombre, etDescripcion, etResponsable;

    RadioGroup rg;

    RadioButton low, mid, high;

    Button add;

    int selectedId;

    RadioButton selectedRadioButton;

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

        add = findViewById(R.id.additem);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etNombre.getText().toString().isEmpty() || etDescripcion.getText().toString().isEmpty() || etResponsable.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(), getString(R.string.errorForm), Toast.LENGTH_SHORT).show();
                }else{



                selectedId = rg.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    selectedId = mid.getId();
                }

                // Obtener el RadioButton seleccionado
                selectedRadioButton = findViewById(selectedId);

                String urgency = selectedRadioButton.getText().toString();

                String name = etNombre.getText().toString();

                String description = etDescripcion.getText().toString();

                String responsable = etResponsable.getText().toString();

                String idTask = UUID.randomUUID().toString();




                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                assert user != null;
                String userId = user.getUid();


                // Obtener las referencias a la base de datos
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference tasksRef = databaseRef.child("tasks");
                DatabaseReference userRef = tasksRef.child(userId);

                String key = tasksRef.push().getKey();

                Task task = new Task(name, description, urgency, responsable, key);

                // Guardar la tarea en la base de datos
                userRef.child(key).setValue(task);

                // Enviar la tarea al MainActivity
                Intent intent = new Intent();
                intent.putExtra("task", task);
                setResult(RESULT_OK, intent);
                finish();

                }

            }
        });



    }

}