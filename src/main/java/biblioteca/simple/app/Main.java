package biblioteca.simple.app;

import biblioteca.simple.contratos.Prestable;
import biblioteca.simple.modelo.*;
import biblioteca.simple.servicios.Catalogo;
import biblioteca.simple.servicios.PersistenciaUsuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {


    private static final Catalogo catalogo = new Catalogo();

    private static final List<Usuario> usuarios =new ArrayList<>();


    private static final Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        cargarDatos();
        menu();
    }

    private static void cargarDatos(){
        catalogo.alta(new Libro(1, "El Quijote", "1608", Formato.FISICO, "25225", "Cervantes"));
        catalogo.alta(new Libro(2, "El nombre del viento", "2007", Formato.FISICO, "9788401352836", "Patrick Rothfuss"));
        catalogo.alta(new Pelicula(3, "El Padrino", "1972", Formato.FISICO, "rancis Ford Coppola", 175));
        catalogo.alta(new Pelicula(4, "Parásitos", "2019", Formato.FISICO, "Bong Joon-ho", 132));
        // Añadimos los videojuegos al catálogo
        catalogo.alta(new Videojuego(5, "The Legend of Zelda: Breath of the Wild", "2017", Formato.FISICO,Categoria.AVENTURA, Plataforma.NINTENDO_SWITCH,"PEGI 12"));
        catalogo.alta(new Videojuego(6, "EA Sports FC 25", "2024", Formato.DIGITAL, Categoria.DEPORTES, Plataforma.PLAYSTATION, "PEGI 3"));

        usuarios.add(new Usuario(1, "Juan"));
        usuarios.add(new Usuario(2, "María"));

    }

    private static void menu(){

        int op;

        do {

            System.out.println("\n===Menú de Biblioteca===");
            System.out.println("1. Listar");
            System.out.println("2. Buscar por título");
            System.out.println("3. Buscar por año");
            System.out.println("4. Prestar Producto");
            System.out.println("5. Devolver Producto");
            System.out.println("6. Crear usuario");
            System.out.println("7. Exportar usuarios");
            System.out.println("8. Importar usuarios");
            System.out.println("0. Salir");
            while(!sc.hasNextInt()) sc.next();
            op = sc.nextInt();

            sc.nextLine();

            switch (op){
                case 1 -> listar();
                case 2 -> buscarPorTitulo();
                case 3 -> buscarPorAnio();
                case 4 -> prestar();
                case 5 -> devolver();
                case 6 -> crearUsuario();
                case 7 -> exportarUsuario();
                case 8 -> importarUsarios();
                case 0 -> System.out.println("Sayonara!");
                default -> System.out.println("Opción no válida");
            }

        } while(op != 0);
    }

    private static void listar(){
        List<Producto> lista = catalogo.listar();

        if(lista.isEmpty()){
            System.out.println("Catálogo vacío");
            return;
        }

        System.out.println("==Lista de productos ===");

        for(Producto p : lista) System.out.println("- " + p);


    }

    private static void buscarPorTitulo(){
        System.out.println("Título (escribe parte del título): ");
        String t = sc.nextLine();
        catalogo.buscar(t).forEach(p -> System.out.println("- " + p));
    }

    private static void buscarPorAnio(){
        System.out.println("Año: ");
        int a = sc.nextInt();
        sc.nextLine();
        catalogo.buscar(a).forEach(p -> System.out.println("- " + p));
    }

    private static void listarUsuarios(){
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }
        System.out.println("Lista usuarios");
        usuarios.forEach( u ->
                        System.out.println("- Código : " + u.getId() + "| Nombre: " + u.getNombre() )
        );
    }

    private static Usuario getUsuarioPorCodigo(int id){
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static void prestar(){

        // 1)mostrar productos disponibles

        List<Producto> disponibles = catalogo.listar().stream()
                .filter(p -> p instanceof Prestable pN && !pN.estaPrestado())
                .collect(Collectors.toList());

        if ( disponibles.isEmpty() ) {
            System.out.println("No hay productos para prestar");
            return;
        }

        System.out.println("-- PRODUCTOS DISPONIBLES --");
        disponibles.forEach( p -> System.out.println("- ID: " + p.getId() + " | " + p));

        System.out.println("Escribe el id del producto: ");
        int id = sc.nextInt();
        sc.nextLine();

        Producto pEncontrado = disponibles.stream()
                .filter(p -> {
                    try {
                        return p.getId() == id;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);

                 if (pEncontrado == null){
                     System.out.println("El id no existe");
                     return;
                 }

                 listarUsuarios();
                 System.out.println("Ingresa código de usuario");
                 int cUsuario = sc.nextInt();
                 sc.nextLine();
                 Usuario u1 = getUsuarioPorCodigo(cUsuario);

                 // Si el usuario no existe, ofrecemos crearlo en ese momento
                 if (u1 == null){
                     System.out.println("Usuario no encontrado.");
                     System.out.print("¿Desea crear el usuario ahora? (S/N): ");
                     String respuesta = sc.nextLine().trim().toUpperCase();
                     if (respuesta.equals("S")) {
                         u1 = crearUsuario();
                     } else {
                         System.out.println("Operación de préstamo cancelada.");
                         return;
                     }
                 }
                // Intentamos realizar el préstamo
                 Prestable pPrestable = (Prestable) pEncontrado;
                 pPrestable.prestar(u1);
                 System.out.println("Producto prestado correctamente.");
    }

    public static void devolver(){
        List<Producto> pPrestados = catalogo.listar().stream()
                .filter(p -> p instanceof Prestable pN && pN.estaPrestado())
                .collect(Collectors.toList());

        if ( pPrestados.isEmpty() ) {
            System.out.println("No hay productos para prestar");
            return;
        }

        System.out.println("-- PRODUCTOS PRESTADOS --");
        pPrestados.forEach( p -> System.out.println("- ID: " + p.getId() + " | " + p));

        System.out.println("Escribe el id del producto: ");
        int id = sc.nextInt();
        sc.nextLine();

        Producto pEncontrado = pPrestados.stream()
                .filter(p -> {
                    try {
                        return p.getId() == id;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);

        if (pEncontrado == null){
            System.out.println("El id no existe");
            return;
        }

        Prestable pE = (Prestable) pEncontrado;
        pE.devolver();
        System.out.println("Devuelto correctamente");
    }

    private static Usuario crearUsuario(){
        System.out.println("¿Cuál es el nombre del nuevo usuario?");
        String nombre = sc.nextLine();

        //Generamos nuevo ID automáticamente para evitar problemas
        // Si la lista está vacía, asignamos id=1; si no, sumamos 1 al id del último usuario
        int nuevoId;
        if (usuarios.isEmpty()) {
            nuevoId = 1;
        } else {
            Usuario ultimoUsuario = usuarios.get(usuarios.size() - 1);
            nuevoId = ultimoUsuario.getId() + 1;
        }
        // Creamos el objeto Usuario con el id generado y el nombre introducido
        Usuario nuevoUsuario = new Usuario(nuevoId, nombre);
        // Añadimos el nuevo usuario a la lista global de usuarios
        usuarios.add(nuevoUsuario);

        System.out.println("Usuario '" + nombre + "' con id: " + nuevoId + " creado correctamente.");
        return nuevoUsuario;
    }

    private static void exportarUsuario(){
        //Debemos gestionar las excepciones en esta función también.
        try{
            PersistenciaUsuarios.exportar(usuarios);
            System.out.println("Usuarios exportados correctamente");
        } catch (Exception e){
            System.out.println("Error al exportar usuarios " + e.getMessage());
        }

    }

    private static void importarUsarios(){
        try{
            List<Usuario> cargados = PersistenciaUsuarios.importar();
            usuarios.clear();
            usuarios.addAll(cargados);
            System.out.println("Usuarios cargados con éxito");

        } catch (Exception e) {
            System.out.println("Error al importar: " + e.getMessage());
        }
    }


}
