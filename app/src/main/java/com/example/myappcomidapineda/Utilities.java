package com.example.myappcomidapineda;

import java.util.*;

public class Utilities {

    public static boolean fileExists(String[] files, String namePath) {

        // buscar el archivo
        for (int i = 0; i < files.length; i++)
            if (namePath.equals(files[i]))
                return true;
        return false;

    }

    public static Map<Integer, String> convertStringToMap(String meals) {

        // Eliminar la coma final y dividir la cadena en elementos individuales
        String[] items = meals.split(",");

        if (items.length >= 1 && !items[0].isEmpty()) {

            // Crear un Map para almacenar los elementos
            Map<Integer, String> map = new HashMap<>();

            // Iterar sobre los elementos y agregarlos al Map
            for (int i = 0; i < items.length; i++) {
                map.put(i + 1, items[i]);
            }

            System.out.println("..:::Los platillos que existen en la base de datos son:");
            // Imprimir el Map para verificar el resultado
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }

            return map;

        } else {
            return new HashMap<>();
        }

    }

    public static int getRandomNumber(int upperLimit) {

        if (upperLimit == 1) {
            return 1;
        } else {
            Random random = new Random();
            return random.nextInt(upperLimit - 1) + 1;  // Genera un número aleatorio entre 1 (inclusive) y upperLimit (exclusivo)
        }

    }

    public static String getValueByKey(Map<Integer, String> map, int key) {
        return map.get(key); // Obtiene el valor asociado a la clave
    }

    public static Map<Integer, String> removeFirstAddLastMap(Map<Integer, String> mapOld, String newValueMeal, int tamMaxime) {

        // Obtener el tamaño del Map original
        Optional<Integer> maxKey = mapOld.keySet().stream().max(Integer::compareTo);
        int valueMaxMap = maxKey.get();

        System.out.println("..:::Tamaño del Map original: " + valueMaxMap);

        // Crear un LinkedHashMap con 7 elementos
        LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>(mapOld);
        System.out.println("..:::01. Map original: " + linkedHashMap);

        System.out.println("..:::El valor maximo del Map es: " + valueMaxMap);
        System.out.println("..:::Los dias que se almacenara de comida sugerida, son los ultimos: " + tamMaxime + " dias");

        if (valueMaxMap >= tamMaxime) {

            for (int i = 1; i < valueMaxMap; i++) {
                linkedHashMap.put(1 + i, linkedHashMap.get(1 + i));
            }

            // Imprimir el mapa original
            System.out.println("..:::02. Map modificado: " + linkedHashMap);

            // Eliminar el primer elemento (más antiguo)
            if (!linkedHashMap.isEmpty()) {
                // Obtenemos la primera clave del mapa
                int firstKey = linkedHashMap.keySet().iterator().next();
                // Eliminamos la entrada correspondiente
                linkedHashMap.remove(firstKey);
            }

            System.out.println("..:::03. Map eliminado: " + linkedHashMap);

            // Crear un nuevo LinkedHashMap con las claves reajustadas
            LinkedHashMap<Integer, String> newlinkedHashMap = new LinkedHashMap<>();
            int newKey = 1;

            // Reinsertar entradas con claves reajustadas
            for (String value : linkedHashMap.values()) {
                newlinkedHashMap.put(newKey++, value);
            }

            // Agregar un nuevo elemento al final
            newlinkedHashMap.put(valueMaxMap, newValueMeal.trim());

            // Imprimir el nuevo mapa
            System.out.println("..:::04. Nuevo Map con las claves reajustadas: " + newlinkedHashMap);

            // Imprimir el mapa modificado
            System.out.println("..:::05. Map final: " + newlinkedHashMap);

            Map<Integer, String> mapFinal = newlinkedHashMap;

            return mapFinal;

        } else {

            linkedHashMap.put(valueMaxMap + 1, newValueMeal);

            System.out.println("..:::Se agrego correctamente el valor al Map: " + linkedHashMap);

            Map<Integer, String> mapFinal = linkedHashMap;

            return mapFinal;

        }

    }

}