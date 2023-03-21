package org.insbaixcamp.reus.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Objects;

public class AddItem extends AppCompatActivity {

    EditText etNombre, etDescripcion;

    RadioGroup rg;

    RadioButton low, mid, high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Objects.requireNonNull(getSupportActionBar()).hide();


        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);

        rg = findViewById(R.id.radioGroup);

        low = findViewById(R.id.low);
        mid = findViewById(R.id.mid);
        high = findViewById(R.id.high);

    }
}