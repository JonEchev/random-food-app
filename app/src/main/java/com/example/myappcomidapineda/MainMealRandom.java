package com.example.myappcomidapineda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainMealRandom extends AppCompatActivity {

    private Button btnAceptarRandomLayout;
    private Button btnAtrasRandomLayout;
    private Button btnLimpiarLayout;
    private TextView txtComidaSugeridaLayout;

    private String nameFilePath = "comidas.txt";
    private String nameLastSevenMealsFilePath = "ultimascomidas.txt";
    private String nameFilePathDaysConfig = "diasconfig.txt";

    private int lastMeals = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_random_layout);

        lastMeals = getNumLastMealsConfig();

        // Inicio - obtener el numero de dias configurados

        // recuperar todos los ficheros en la app
        String[] filesNDC = fileList();

        System.out.println("..:::Cargando la comida sugerida por el destino:::..");

        if (Utilities.fileExists(filesNDC, nameFilePathDaysConfig)) {

            try {

                // abrir archivo
                InputStreamReader archivoNDC = new InputStreamReader(openFileInput(nameFilePathDaysConfig));

                // leer archivo
                BufferedReader bufferedReaderNDC = new BufferedReader(archivoNDC);

                // verifica que el archivo no este vacio
                String lineaNDC = bufferedReaderNDC.readLine();
                String numDias = "";

                while (lineaNDC != null) {
                    numDias = numDias + lineaNDC + "\n";
                    lineaNDC = bufferedReaderNDC.readLine();
                }

                System.out.println("..:::La cantidad de dias configurados para no repetir sugerencia de comidas son: " + numDias);

                // finalizar la lectura del archivo
                bufferedReaderNDC.close();

                // cerrar el archivo
                archivoNDC.close();

            } catch (IOException ex) {
                System.out.println("****%****Error en la lectura del archivo de configuracion, el error es: " + ex.getMessage());
            }

        }

        // Fin - numero de dias configurados

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

                // Eliminar espacios en blanco
                String modifiedContent = comidasCompletas.replaceAll("\\s+", " ").trim();

                System.out.println("..:::Las comidas que se encuentran almacenadas en la base de datos local, sin espacios son: " + modifiedContent);

                // Obtener el contenido modificado del archivo
                Map<Integer, String> dataMap = Utilities.convertStringToMap(modifiedContent);

                // Obtener el numero maximo de registros del Map
                Optional<Integer> maxKey = dataMap.keySet().stream().max(Integer::compareTo);

                if (maxKey.isPresent()) {

                    System.out.println("..:::El numero maximo de registros del Map de comidas son: " + maxKey.get());

                    Map<Integer, String> dataMapLSD = getFileMealsLastSeven();

                    System.out.println("..:::Los platillos almacenados de los ultimos: " + lastMeals + " dias son: " + maxKey.get());

                    if (dataMapLSD != null) {

                        // Validar diferencia de valores del Map de Comidas, contra el Map de los ultimos 7 dias, para mostrar el platillo sugerido que haga falta
                        String oneMeal = validateLastDate(dataMap, dataMapLSD);

                        if (oneMeal.isEmpty()) {

                            boolean bandera = true;

                            while (bandera) {

                                System.out.println("..:::Inicio lanzamiento dado del destino");
                                System.out.println("..:::La cantidad de platillos para preparar que existen almacenados son: " + maxKey.get());
                                int randomGenerated = Utilities.getRandomNumber(maxKey.get());

                                // Obtener el valor asociado a la clave
                                String value = Utilities.getValueByKey(dataMap, randomGenerated);

                                System.out.println("..:::El número de la suerte elegido es: " + randomGenerated + " el platillo sugerido para preparar es: " + value);
                                System.out.println("..:::El map con las comidas que se han realizado los ultimos dias (maximo " + lastMeals + " dias) son: " + dataMapLSD);

                                // validar si la comida sugerida existe en el fichero de los ultimos dias configurados en la variable lastMeals
                                if (!isValueInMap(value, dataMapLSD)) {

                                    // Agrega valor del MainActivity en el TextView de MainMenu
                                    txtComidaSugeridaLayout = findViewById(R.id.txtComidaSugerida);
                                    txtComidaSugeridaLayout.setText(value);

                                    bandera = false;

                                    System.out.println("..:::Fin lanzamiento dado del destino");

                                    break;

                                }

                            }

                        } else if (oneMeal.equals("0")) {
                            System.out.println(".::Ya se sugirieron todos los platos de comida para preparar.");
                            // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
                            Toast.makeText(this, ".::Ya se sugirieron todos los platos de comida para preparar::.", Toast.LENGTH_SHORT).show();

                            // habilitar boton de limpiar el random de comidas sugeridas en el layout de menu_user_layout
                            Intent intent = new Intent(MainMealRandom.this, MainMenu.class);
                            intent.putExtra("btnReiniciarRandom", true);

                            // Aquí puedes obtener el valor si es necesario
                            String nameUser = getIntent().getStringExtra("txtNombreUsuario");
                            intent.putExtra("txtNombreUsuario", nameUser);

                            intent.putExtra("key", "valor");

                            startActivity(intent);

                            // cerrar el activity
                            finish();
                        } else {

                            System.out.println(".::El unico plato de comida que quedo para sugerir es: " + oneMeal);

                            // Agrega valor del MainActivity en el TextView de MainMenu
                            txtComidaSugeridaLayout = findViewById(R.id.txtComidaSugerida);
                            txtComidaSugeridaLayout.setText(oneMeal);

                        }

                    }

                } else {
                    System.out.println("..:::No existen platillos almacenados para sugerir, por favor diligencie información para la futura comida a preparar.");
                    Toast.makeText(this, ".xx.No existen comidas almacenadas para sugerir.xx.", Toast.LENGTH_SHORT).show();
                    // cerrar el activity actual
                    finish();
                }

                // FIN -> Eliminar espacios en blanco

                // finalizar la lectura del archivo
                bufferedReader.close();

                // cerrar el archivo
                archivo.close();

            } catch (IOException ex) {
                System.out.println("****%****Error en la creacion del archivo, el error es: " + ex.getMessage());
            }

        } else {

            // Ficheros - fin

            System.out.println("..:::No existe comida almacenada para sugerir");
            txtComidaSugeridaLayout = findViewById(R.id.txtComidaSugerida);

            if (txtComidaSugeridaLayout.getText().toString().isEmpty()) {
                // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
                Toast.makeText(this, ".xx.No existen comidas almacenadas para sugerir.xx.", Toast.LENGTH_SHORT).show();
                // cerrar el activity actual
                finish();
            }

        }
        // boton aceptar random
        btnAceptarRandomLayout = findViewById(R.id.btnAceptar);

        btnAceptarRandomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeals(view);
                // cerrar la activity actual
                finish();
            }
        });

        // boton atras
        btnAtrasRandomLayout = findViewById(R.id.btnAtrasRandom);

        btnAtrasRandomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aquí puedes obtener el valor si es necesario
                String nameUser = getIntent().getStringExtra("txtNombreUsuario");

                Intent intent = new Intent(MainMealRandom.this, MainMenu.class);
                intent.putExtra("txtNombreUsuario", nameUser);
                startActivity(intent);

                // cerrar el activity
                finish();
            }
        });

        // boton limpiar
        btnLimpiarLayout = findViewById(R.id.btnLimpiar);

        btnLimpiarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // limpiar en el archivo la data
                clearFileLastSeven(MainMealRandom.this, view);

                // cerrar la activity actual
                finish();
            }
        });

    }

    public int getNumLastMealsConfig() {

        String diasConfig = "";

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

                while (linea != null) {
                    diasConfig = diasConfig + linea + "\n";
                    linea = bufferedReader.readLine();
                }

                // finalizar la lectura del archivo
                bufferedReader.close();

                // cerrar el archivo
                archivo.close();

            } catch (IOException ex) {
                System.out.println("****%****Error en la creacion del archivo de configuracion, el error es: " + ex.getMessage());
            }

        }

        System.out.println("..:::La cantidad de días configurados para almacenar las comidas sugeridas son: " + diasConfig.trim());

        // Ficheros - fin

        if (diasConfig.isEmpty()) {
            diasConfig = "7";
            System.out.println("..:::Se trabajara con la configuracion de dias por defecto, para almacenar las comidas sugeridas, el valor por defecto es: " + diasConfig + " dias.");
        }

        return Integer.parseInt(diasConfig.trim());

    }

    public Map<Integer, String> getFileMealsLastSeven() {

        Map<Integer, String> dataMapLS = new HashMap<>();

        try {

            // Ficheros - inicio

            // recuperar todos los ficheros en la app
            String[] filesMealsLastSeven = fileList();

            if (Utilities.fileExists(filesMealsLastSeven, nameLastSevenMealsFilePath)) {

                try {

                    // abrir archivo
                    InputStreamReader archivoISR = new InputStreamReader(openFileInput(nameLastSevenMealsFilePath));

                    // leer archivo
                    BufferedReader bufferedReaderLS = new BufferedReader(archivoISR);

                    // verifica que el archivo no este vacio
                    String lineaLS = bufferedReaderLS.readLine();
                    String comidasCompletasLS = "";

                    while (lineaLS != null) {
                        comidasCompletasLS = comidasCompletasLS + lineaLS + "\n";
                        lineaLS = bufferedReaderLS.readLine();
                    }

                    // Eliminar espacios en blanco
                    String modifiedContentLS = comidasCompletasLS.replaceAll("\\s+", " ").trim();

                    // Obtener el contenido modificado del archivo
                    dataMapLS = Utilities.convertStringToMap(modifiedContentLS);

                    // finalizar la lectura del archivo
                    bufferedReaderLS.close();

                    // cerrar el archivo
                    archivoISR.close();

                } catch (IOException ex) {
                    System.out.println("****%****Error en la creacion del archivo, el error es: " + ex.getMessage());
                }

            } else {

                // Ficheros - fin

                System.out.println("..:::Primera vez que se ingresa al Activity.");

                // escribe en el archivo la data
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameLastSevenMealsFilePath, Activity.MODE_PRIVATE));

                txtComidaSugeridaLayout = findViewById(R.id.txtComidaSugerida);

                // valida que el campo no este vacio
                if (!txtComidaSugeridaLayout.getText().toString().isEmpty()) {
                    outputStreamWriter.write(txtComidaSugeridaLayout.getText().toString());
                }

                // limpiar los datos del buffer
                outputStreamWriter.flush();

                // cerrar el fichero
                outputStreamWriter.close();

            }

        } catch (IOException ex) {
            System.out.println("****%****Error guardando una de las ultimas " + lastMeals + " comidas en el archivo, el error es: " + ex.getMessage());
        }

        System.out.println("..:::El contenido recuperado del Map con las comidas realizadas los ultimos dias (maximo " + lastMeals + " dias) son: " + dataMapLS);

        return dataMapLS;

    }

    // Metodo para el boton Guardar
    public void saveMeals(View view) {

        try {

            // Ficheros - inicio

            // recuperar todos los ficheros en la app
            String[] filesMealsLastSeven = fileList();

            if (Utilities.fileExists(filesMealsLastSeven, nameLastSevenMealsFilePath)) {

                try {

                    // abrir archivo
                    InputStreamReader archivoISR = new InputStreamReader(openFileInput(nameLastSevenMealsFilePath));

                    // leer archivo
                    BufferedReader bufferedReaderLS = new BufferedReader(archivoISR);

                    // verifica que el archivo no este vacio
                    String lineaLS = bufferedReaderLS.readLine();
                    String comidasCompletasLS = "";

                    while (lineaLS != null) {
                        comidasCompletasLS = comidasCompletasLS + lineaLS + "\n";
                        lineaLS = bufferedReaderLS.readLine();
                    }

                    System.out.println("..:::Comidas completas del archivo guardado con los ultimos " + lastMeals + " platillos sugeridos previamente: " + comidasCompletasLS);

                    if (!comidasCompletasLS.isEmpty()) {
                        // Eliminar espacios en blanco
                        String modifiedContentLS = comidasCompletasLS.replaceAll("\\s+", " ").trim();

                        Map<Integer, String> dataMapLS = Utilities.convertStringToMap(modifiedContentLS);

                        // Obtener el contenido modificado del archivo
                        Map<Integer, String> dataMapLSF = Utilities.removeFirstAddLastMap(dataMapLS, txtComidaSugeridaLayout.getText().toString(), lastMeals);

                        // escribe en el archivo la data
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameLastSevenMealsFilePath, Activity.MODE_PRIVATE));

                        for (Map.Entry<Integer, String> entry : dataMapLSF.entrySet()) {
                            outputStreamWriter.write(entry.getValue() + ",\n");
                        }

                        // limpiar los datos del buffer
                        outputStreamWriter.flush();

                        // cerrar el fichero
                        outputStreamWriter.close();

                    } else {

                        // Ficheros - fin

                        // escribe en el archivo la data
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameLastSevenMealsFilePath, Activity.MODE_PRIVATE));
                        outputStreamWriter.write(txtComidaSugeridaLayout.getText().toString());

                        // limpiar los datos del buffer
                        outputStreamWriter.flush();

                        // cerrar el fichero
                        outputStreamWriter.close();

                    }

                    // finalizar la lectura del archivo
                    bufferedReaderLS.close();

                    // cerrar el archivo
                    archivoISR.close();

                } catch (IOException ex) {
                    System.out.println("****%****Error en la creacion del archivo, el error es: " + ex.getMessage());
                }

            } else {

                // Ficheros - fin

                // escribe en el archivo la data
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(nameLastSevenMealsFilePath, Activity.MODE_PRIVATE));
                outputStreamWriter.write(txtComidaSugeridaLayout.getText().toString());

                // limpiar los datos del buffer
                outputStreamWriter.flush();

                // cerrar el fichero
                outputStreamWriter.close();

            }

        } catch (IOException ex) {
            System.out.println("****%****Error guardando una de las ultimas " + lastMeals + " comidas en el archivo, el error es: " + ex.getMessage());
        }

        // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
        Toast.makeText(this, ".::Que la fuerza os acompañe::.", Toast.LENGTH_SHORT).show();
        finish();

    }

    public static boolean isValueInMap(String value, Map<Integer, String> map) {

        System.out.println("..:::El valor a buscar es: " + value + " - En el Map: " + map);

        boolean exist = false;

        for (String valuer : map.values()) {

            String nuevoValorPrueba = valuer.trim();
            System.out.println("..:::El valor a evaluar es: " + nuevoValorPrueba);

            if (nuevoValorPrueba.equalsIgnoreCase(value.trim())) {
                System.out.println("..:::SI EXISTE: " + nuevoValorPrueba);
                return true;
            }

        }

        return exist;

    }

    // Metodo para el boton limpiar
    public void clearFileLastSeven(Context context, View view) {

        try {

            System.out.println("..:::Se va a limpiar la lista con los random sugeridos: " + nameLastSevenMealsFilePath);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nameLastSevenMealsFilePath, Activity.MODE_PRIVATE));
            outputStreamWriter.write(Arrays.toString(new byte[0]));

            // limpiar los datos del buffer
            outputStreamWriter.flush();

            // cerrar el fichero
            outputStreamWriter.close();

        } catch (IOException ex) {
            System.out.println("****%****Error en la limpieza del archivo, el error es: " + ex.getMessage());
        }

        // validar a que clase pertenece cada Context
        if (context instanceof MainMenu) {
            // El context pertenece a MainMenu
            // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
            Toast.makeText(context, ".::Se resetearon las comidas sugeridas en los ultimos dias::.", Toast.LENGTH_SHORT).show();
        } else if (context instanceof MainMealRandom) {
            // El context pertenece a MainMealRandom
            // mostrar mensaje al usuario de datos guardados correctamente, como una ventana emergente
            Toast.makeText(context, ".::Se limpiaron las comidas sugeridas los ultimos " + lastMeals + " dias::.", Toast.LENGTH_SHORT).show();
        }

        // cerrar el activity
        finish();

    }

    public String validateLastDate(Map<Integer, String> mapMealsBD, Map<Integer, String> mapMealsLastDays) {

        // Para almacenar el valor diferente
        String valorDiferente = null;
        // Contador de diferencias
        int diferenciaContador = 0;

        // Eliminar el valor asociado con la clave 1 del Map mapMealsLastDays, el cual siempre debe ser []
        mapMealsLastDays.remove(1);

        // Iterar sobre las entradas del Map y aplicar trim() a cada valor
        for (Map.Entry<Integer, String> entry : mapMealsLastDays.entrySet()) {
            // Eliminar espacios al inicio y al final
            String trimmedValue = entry.getValue().trim();
            // Actualizar el Map con el valor recortado
            mapMealsLastDays.put(entry.getKey(), trimmedValue);
        }

        System.out.println("..:::Los valores del Map de comidas para sugerir son: " + mapMealsBD);
        System.out.println("..:::Los valores del Map de comidas ya sugeridas son: " + mapMealsLastDays);

        for (Map.Entry<Integer, String> entry : mapMealsBD.entrySet()) {
            if (!mapMealsLastDays.containsValue(entry.getValue().trim()) || mapMealsLastDays.containsValue("[]") && entry.getValue().equals(mapMealsLastDays.get(entry.getKey()))) {
                valorDiferente = entry.getValue();
                diferenciaContador++;
            }
        }

        // Verificar si solo hay una diferencia
        if (diferenciaContador == 1) {
            System.out.println(".::Existe un plato de comida para sugerir, el cual es: " + valorDiferente);
        } else if (diferenciaContador == 0) {
            System.out.println(".::Ya no existen platos de comida para sugerir.");
            valorDiferente = "0";
        } else {
            System.out.println("..:::Hay " + diferenciaContador + " de comida para sugerir, son muchaassss.");
            valorDiferente = "";
        }

        return valorDiferente;

    }

}