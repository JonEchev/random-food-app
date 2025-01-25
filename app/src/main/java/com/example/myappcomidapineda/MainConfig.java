package com.example.myappcomidapineda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class MainConfig extends AppCompatActivity {

    private EditText txtDiasConfigLayout;
    private Button btnGuardarConfigLayout;
    private Button btnAtrasConfigLayout;

    private String nameFilePathDaysConfig = "diasconfig.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicio - Generado automaticamente
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config_layout);

        txtDiasConfigLayout = (EditText) findViewById(R.id.txtDiasConfig);

        // Validar que sean solo numeros y que solo se puedan configurar 31 dias maximo y solo 2 digitos numericos
        txtDiasConfigLayout.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validar la entrada
                String input = s.toString();
                if (!input.isEmpty()) {
                    try {
                        int day = Integer.parseInt(input);
                        if (day < 1 || day > 31) {
                            // Si el número no está en el rango permitido, muestra un mensaje de error
                            Toast.makeText(MainConfig.this, ".::Por favor ingrese un numero entre 1 y 31 dias::.", Toast.LENGTH_SHORT).show();
                            // Limpiar el texto
                            txtDiasConfigLayout.setText("");
                        }
                    } catch (NumberFormatException e) {
                        // Si el texto no es un número, muestra un mensaje de error
                        Toast.makeText(MainConfig.this, "***Solo se pueden ingresar numeros***", Toast.LENGTH_SHORT).show();
                        // Limpiar el texto
                        txtDiasConfigLayout.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // Fin Validar

        // Ficheros - inicio

        // recuperar todos los ficheros en la app
        String[] filesMeals = fileList();

        if (Utilities.fileExists(filesMeals, nameFilePathDaysConfig)) {

            try {

                // abrir archivo
                InputStreamReader archivo = new InputStreamReader(openFileInput(nameFilePathDaysConfig));

                // leer archivo
                BufferedReader bufferedReader = new BufferedReader(archivo);

                // verifica que el archivo no este vacio
                String linea = bufferedReader.readLine();
                String diasConfig = "";

                while (linea != null) {
                    diasConfig = diasConfig + linea + "\n";
                    linea = bufferedReader.readLine();
                }

                // mostrar el texto que se tiene almacenado en el archivo, en lo ultimo de la caja de texto
                txtDiasConfigLayout.setText(diasConfig);

                // finalizar la lectura del archivo
                bufferedReader.close();

                // cerrar el archivo
                archivo.close();

            } catch (IOException ex) {
                System.out.println("****%****Error en la creacion del archivo de configuracion, el error es: " + ex.getMessage());
            }

        }

        // Ficheros - fin

        // boton Guardar
        btnGuardarConfigLayout = findViewById(R.id.btnGuardarConfig);

        btnGuardarConfigLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveConfig(view);
            }
        });

        // boton Atras
        btnAtrasConfigLayout = findViewById(R.id.btnAtrasConfig);

        btnAtrasConfigLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aquí puedes obtener el valor si es necesario
                String nameUser = getIntent().getStringExtra("txtNombreUsuario");

                Intent intent = new Intent(MainConfig.this, MainMenu.class);
                intent.putExtra("txtNombreUsuario", nameUser);
                startActivity(intent);

                // cerrar el activity
                finish();
            }
        });

    }

    // Metodo para el boton Guardar
    public void saveConfig(View view) {

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameFilePathDaysConfig, Activity.MODE_PRIVATE));
            outputStreamWriter.write(txtDiasConfigLayout.getText().toString());

            // limpiar los datos del buffer
            outputStreamWriter.flush();

            // cerrar el fichero
            outputStreamWriter.close();

        } catch (IOException ex) {
            System.out.println("****%****Error guardando el archivo de configuracion, el error es: " + ex.getMessage());
        }

        // mostrar mensaje al usuario de configuracion guardad correctamente, como una ventana emergente
        Toast.makeText(this, ".::Dias configurados correctamente::.", Toast.LENGTH_SHORT).show();
        finish();

    }

}