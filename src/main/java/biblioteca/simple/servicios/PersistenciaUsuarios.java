package biblioteca.simple.servicios;

import biblioteca.simple.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class PersistenciaUsuarios {

    private static final String ARCHIVO = "usuarios.josn";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private PersistenciaUsuarios() {

    }

    //Convertimos a json la lista de Usuarios para exportar
    public static void exportar(List<Usuario> usuarios) throws IOException {
        //NO hace falta poner un catch porque si salta el try, se irá al throws
        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            gson.toJson(usuarios, w);
        }
    }

    public static List<Usuario> importar() throws IOException {
        File f = new File(ARCHIVO);

        if(!f.exists()) return new ArrayList<>();

        try(Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8) ){

            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {}.getType();

            List<Usuario> lista = gson.fromJson(r,tipoLista );

            return (lista != null) ? lista : new ArrayList<>();

        }

    }
}
