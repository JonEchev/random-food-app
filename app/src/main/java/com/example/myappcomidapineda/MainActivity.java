package com.example.myappcomidapineda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnIngresarLayout;
    private TextView txtNombreUsuarioLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicio - Generado automaticamente
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //ActionBar actionBar = getSupportActionBar();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Fin - Generado automaticamente

        txtNombreUsuarioLayout = findViewById(R.id.txtNombreUsuario);

        // Inicio - Ingresar al layout del menu principal mediante el boton Ingresar
        btnIngresarLayout = findViewById(R.id.btnIngresar);

        btnIngresarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String valueTxtNombreUsuarioLayout = txtNombreUsuarioLayout.getText().toString();

                // validar que ingresen un nombre de usuario
                if (!valueTxtNombreUsuarioLayout.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    // Pasar el nombre del usuario logueado de un Activity a otro
                    intent.putExtra("txtNombreUsuario", valueTxtNombreUsuarioLayout);
                    startActivity(intent);
                } else {
                    validateNameUser(view);
                }

            }

        });
        // Fin - Ingresar al layout del menu principal mediante el boton Ingresar

    }

    public void validateNameUser(View view) {
        Toast.makeText(this, ".xx.Ingrese un nombre de usuario.xx.", Toast.LENGTH_SHORT).show();
    }

}