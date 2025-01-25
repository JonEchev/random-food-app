package com.example.myappcomidapineda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainAddMeals extends AppCompatActivity {

    private EditText txtComidasAgregarLayout;
    private Button btnGuardarAgregarLayout;
    private Button btnCancelarAgregarComidasLayout;

    private String nameFilePath = "comidas.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicio - Generado automaticamente
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_meals_layout);

        txtComidasAgregarLayout = (EditText) findViewById(R.id.txtComidasAgregar);

        // Ficheros - inicio

        // recuperar todos los ficheros en la app
        String[] filesMeals = fileList();

        if (Utilities.fileExists(filesMeals, nameFilePath)) {

            try {

                // abrir archivo
                InputStreamReader archivo = new InputStreamReader(openFileInput(nameFilePath));

                // leer archivo
                BufferedReader bufferedReader = new BufferedReader(archivo);

                // verifica que el archivo no este vacio
                String linea = bufferedReader.readLine();
                String comidasCompletas = "";

                while (linea != null) {
                    comidasCompletas = comidasCompletas + linea + "\n";
                    linea = bufferedReader.readLine();
                }

                // finalizar la lectura del archivo
                bufferedReader.close();

                // cerrar el archivo
                archivo.close();

                // mostrar el texto que se tiene almacenado en el archivo, en lo ultimo de la caja de texto
                txtComidasAgregarLayout.setText(comidasCompletas);

            } catch (IOException ex) {
                System.out.println("****%****Error en la creacion del archivo, el error es: " + ex.getMessage());
            }

        }

        // Ficheros - fin

        // boton Guardar
        btnGuardarAgregarLayout = findViewById(R.id.btnGuardarAgregar);

        btnGuardarAgregarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeals(view);
            }
        });



        // boton Cancelar
        btnCancelarAgregarComidasLayout = findViewById(R.id.btnCancelarAgregarComidas);

        btnCancelarAgregarComidasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aquí puedes obtener el valor si es necesario
                String nameUser = getIntent().getStringExtra("txtNombreUsuario");

                Intent intent = new Intent(MainAddMeals.this, MainMenu.class);
                intent.putExtra("txtNombreUsuario", nameUser);
                startActivity(intent);

                // cerrar el activity
                finish();
            }
        });

    }

    // Metodo para el boton Guardar
    public void saveMeals(View view) {

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameFilePath, Activity.MODE_PRIVATE));
            outputStreamWriter.write(txtComidasAgregarLayout.getText().toString());

            // limpiar los datos del buffer
            outputStreamWriter.flush();

            // cerrar el fichero
            outputStreamWriter.close();

        } catch (IOException ex) {
            System.out.println("****%****Error guardando el archivo, el error es: " + ex.getMessage());
        }

        // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
        Toast.makeText(this, ".::Comidas guardadas correctamente::.", Toast.LENGTH_SHORT).show();
        finish();

    }

}