package com.example.myappcomidapineda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    private TextView txtNombreUsuarioLogueadoLayout;
    private Button btnIngresarComidasLayout;
    private Button btnRuletaComidasLayout;
    private Button btnConfigLayout;
    private Button btnSalirLayout;
    private Button btnResetLayotu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_user_layout);

        // Recibe valor del MainActivity
        Bundle dataMainActivity = this.getIntent().getExtras();
        String nombreUsuarioBundle = "Usuario";

        // Verifica que el Bundle no sea null
        if (dataMainActivity != null) {
            nombreUsuarioBundle = dataMainActivity.getString("txtNombreUsuario");
        } else {
            // Manejo de casos donde el Bundle es null (opcional)
            System.out.println("****%****Error, el bundle recibido para obtener el nombre del usuario logueado es Null.");
        }

        // Agrega valor del MainActivity en el TextView de MainMenu
        txtNombreUsuarioLogueadoLayout = findViewById(R.id.txtUsuarioLogueado);
        txtNombreUsuarioLogueadoLayout.setText(nombreUsuarioBundle);

        // boton ingresar comidas
        btnIngresarComidasLayout = findViewById(R.id.btnIngresar);

        btnIngresarComidasLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String valueTxtNombreUsuarioLogueadoLayout = txtNombreUsuarioLogueadoLayout.getText().toString();

                Intent intent = new Intent(MainMenu.this, MainAddMeals.class);
                // Pasar el nombre del usuario logueado de un Activity a otro
                intent.putExtra("txtNombreUsuario", valueTxtNombreUsuarioLogueadoLayout);
                startActivity(intent);

            }
        });

        // boton de ruletas de comida
        btnRuletaComidasLayout = findViewById(R.id.btnRuletaComida);

        btnRuletaComidasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String valueTxtNombreUsuarioLogueadoLayout = txtNombreUsuarioLogueadoLayout.getText().toString();

                Intent intent = new Intent(MainMenu.this, MainMealRandom.class);
                // Pasar el nombre del usuario logueado de un Activity a otro
                intent.putExtra("txtNombreUsuario", valueTxtNombreUsuarioLogueadoLayout);
                startActivity(intent);

            }
        });

        // boton ingresar config
        btnConfigLayout = findViewById(R.id.btnConfigMenu);

        btnConfigLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String valueTxtNombreUsuarioLogueadoLayout = txtNombreUsuarioLogueadoLayout.getText().toString();

                Intent intent = new Intent(MainMenu.this, MainConfig.class);
                // Pasar el nombre del usuario logueado de un Activity a otro
                intent.putExtra("txtNombreUsuario", valueTxtNombreUsuarioLogueadoLayout);
                startActivity(intent);

            }
        });

        // boton de salir de la app
        btnSalirLayout = findViewById(R.id.btnSalir);

        btnSalirLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la aplicacion
                finishAffinity();
            }
        });

        // boton reset random
        btnResetLayotu = findViewById(R.id.btnReiniciarRandom);

        boolean enableButton = getIntent().getBooleanExtra("btnReiniciarRandom", false);

        if (enableButton) {
            // Habilitar el boton
            btnResetLayotu.setEnabled(true);

            MainMealRandom mainMealRandom = new MainMealRandom();

            btnResetLayotu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // invoca desde esta clase, hasta la clase MainMealRandom el metodo de limpiar pasandole el context
                    mainMealRandom.clearFileLastSeven(MainMenu.this, v);
                }
            });

        } else{
            // Inhabilitar el boton
            btnResetLayotu.setEnabled(false);
        }

    }

}