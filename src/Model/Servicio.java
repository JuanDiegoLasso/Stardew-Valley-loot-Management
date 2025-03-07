package Model;

/**
 * Clase principal de "Servicio" que crea diferentes "grafos" (listas de nodos)
 * sin usar List ni ArrayList. Incluye:
 *  - MiListaEnlazada (estructura lineal)
 *  - Arbol (estructura de árbol)
 *  - Grafo (estructura de grafo)
 *  - HashEstructura (estructura de hash)
 */
public class Servicio {

    // -------------------------------------------------------------------------
    // 1) NODO DE CULTIVO
    // -------------------------------------------------------------------------
    public static class NodoCultivo {
        public String nombre;
        public int x, y;
        public NodoCultivo siguiente;  // Puntero al siguiente (lista enlazada)
        public boolean completado = false;
        public boolean[] circulos;

        public int sourceTree;       // Indica de qué "árbol/grafo" viene
        public int sourceNodeIndex;  // Indica índice dentro de ese "árbol/grafo"

        public NodoCultivo(String nombre, int x, int y, int numCirculos,
                           int sourceTree, int sourceNodeIndex) {
            this.nombre = nombre;
            this.x = x;
            this.y = y;
            this.circulos = new boolean[numCirculos];
            this.sourceTree = sourceTree;
            this.sourceNodeIndex = sourceNodeIndex;
        }

        public void conectar(NodoCultivo nodo) {
            this.siguiente = nodo;
        }
    }

    // -------------------------------------------------------------------------
    // 2) ESTRUCTURA LINEAL: MiListaEnlazada<T>
    // -------------------------------------------------------------------------
    public static class MiListaEnlazada<T> implements Iterable<T> {

        private Nodo<T> cabeza;
        private Nodo<T> cola;
        private int size;

        public MiListaEnlazada() {
            cabeza = null;
            cola   = null;
            size   = 0;
        }

        // Agrega un nuevo elemento al final de la lista
        public void add(T valor) {
            Nodo<T> nuevo = new Nodo<>(valor);
            if (cabeza == null) {
                cabeza = nuevo;
                cola   = nuevo;
            } else {
                cola.siguiente = nuevo;
                cola = nuevo;
            }
            size++;
        }

        // Devuelve el elemento en la posición 'index'
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
            }
            Nodo<T> actual = cabeza;
            for (int i = 0; i < index; i++) {
                actual = actual.siguiente;
            }
            return actual.valor;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return (size == 0);
        }

        // Para poder usar "for-each" si lo deseas
        @Override
        public java.util.Iterator<T> iterator() {
            return new MiIterador(cabeza);
        }

        // Clase interna de nodo
        private static class Nodo<U> {
            U valor;
            Nodo<U> siguiente;
            public Nodo(U valor) {
                this.valor = valor;
                this.siguiente = null;
            }
        }

        // Iterador para recorrer la lista
        private class MiIterador implements java.util.Iterator<T> {
            private Nodo<T> actual;
            public MiIterador(Nodo<T> inicio) {
                actual = inicio;
            }
            @Override
            public boolean hasNext() {
                return (actual != null);
            }
            @Override
            public T next() {
                T val = actual.valor;
                actual = actual.siguiente;
                return val;
            }
        }
    }

    // -------------------------------------------------------------------------
    // 3) ESTRUCTURA ÁRBOL: Arbol<T>
    // -------------------------------------------------------------------------
    public static class Arbol<T> {
        public NodoArbol<T> raiz;

        public Arbol() {
            this.raiz = null;
        }
        // Aquí podrías implementar inserción, búsqueda, etc.
    }

    public static class NodoArbol<T> {
        public T dato;
        public NodoArbol<T> izq;
        public NodoArbol<T> der;

        public NodoArbol(T dato) {
            this.dato = dato;
        }
    }

    // -------------------------------------------------------------------------
    // 4) ESTRUCTURA GRAFO: Grafo<T>
    // -------------------------------------------------------------------------
    public static class Grafo<T> {
        // Usamos una lista enlazada de nodos del grafo
        private MiListaEnlazada<NodoGrafo<T>> nodos;

        public Grafo() {
            nodos = new MiListaEnlazada<>();
        }

        // Agrega un nodo al grafo
        public void addNodo(T valor) {
            NodoGrafo<T> nuevo = new NodoGrafo<>(valor);
            nodos.add(nuevo);
        }

        // Agrega arista entre dos nodos
        public void addArista(T origen, T destino) {
            NodoGrafo<T> nOrg = buscarNodo(origen);
            NodoGrafo<T> nDest = buscarNodo(destino);
            if (nOrg != null && nDest != null) {
                nOrg.adyacentes.add(nDest);
            }
        }

        private NodoGrafo<T> buscarNodo(T valor) {
            for (NodoGrafo<T> n : nodos) {
                if (n.valor.equals(valor)) {
                    return n;
                }
            }
            return null;
        }

        public MiListaEnlazada<NodoGrafo<T>> getNodos() {
            return nodos;
        }
    }

    public static class NodoGrafo<T> {
        public T valor;
        public MiListaEnlazada<NodoGrafo<T>> adyacentes;

        public NodoGrafo(T valor) {
            this.valor = valor;
            this.adyacentes = new MiListaEnlazada<>();
        }
    }

    // -------------------------------------------------------------------------
    // 5) ESTRUCTURA HASH: HashEstructura<K,V>
    // -------------------------------------------------------------------------
    public static class HashEstructura<K, V> {

        private static class HashNode<K, V> {
            K key;
            V value;
            HashNode<K, V> next;

            public HashNode(K key, V value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }

        private HashNode<K, V>[] tabla;

        @SuppressWarnings("unchecked")
        public HashEstructura(int capacidad) {
            tabla = (HashNode<K, V>[]) new HashNode[capacidad];
        }

        public void put(K key, V value) {
            int index = Math.abs(key.hashCode() % tabla.length);
            HashNode<K, V> nodo = tabla[index];
            if (nodo == null) {
                tabla[index] = new HashNode<>(key, value);
            } else {
                // Encadenamiento
                HashNode<K, V> actual = nodo;
                HashNode<K, V> anterior = null;
                while (actual != null && !actual.key.equals(key)) {
                    anterior = actual;
                    actual = actual.next;
                }
                if (actual == null) {
                    anterior.next = new HashNode<>(key, value);
                } else {
                    // Actualiza valor si ya existe la clave
                    actual.value = value;
                }
            }
        }

        public V get(K key) {
            int index = Math.abs(key.hashCode() % tabla.length);
            HashNode<K, V> actual = tabla[index];
            while (actual != null) {
                if (actual.key.equals(key)) {
                    return actual.value;
                }
                actual = actual.next;
            }
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE "Servicio": crean "grafos" (en realidad, listas de nodos).
    // -------------------------------------------------------------------------
    
    public MiListaEnlazada<NodoCultivo> crearGrafoManualidades() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Morado",    370,  60, 4, 0, 0));
        nodos.add(new NodoCultivo("Centro",    370, 220, 5, 0, 1));
        nodos.add(new NodoCultivo("Verde",     110, 180, 4, 0, 2));
        nodos.add(new NodoCultivo("Azul",      200, 280, 3, 0, 3));
        nodos.add(new NodoCultivo("Amarillo",  540, 280, 4, 0, 4));
        nodos.add(new NodoCultivo("Naranja",   650, 180, 4, 0, 5));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoAlacena() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Alacena1", 370,  60, 4, 1, 0));
        nodos.add(new NodoCultivo("Alacena2", 370, 220, 6, 1, 1));
        nodos.add(new NodoCultivo("Alacena3", 110, 180, 3, 1, 2));
        nodos.add(new NodoCultivo("Alacena4", 200, 280, 4, 1, 3));
        nodos.add(new NodoCultivo("Alacena5", 540, 280, 4, 1, 4));
        nodos.add(new NodoCultivo("Alacena6", 650, 180, 5, 1, 5));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoPecera() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Pecera1", 370,  60, 4, 2, 0));
        nodos.add(new NodoCultivo("Pecera2", 370, 220, 5, 2, 1));
        nodos.add(new NodoCultivo("Pecera3", 110, 180, 3, 2, 2));
        nodos.add(new NodoCultivo("Pecera4", 200, 280, 4, 2, 3));
        nodos.add(new NodoCultivo("Pecera5", 540, 280, 4, 2, 4));
        nodos.add(new NodoCultivo("Pecera6", 650, 180, 4, 2, 5));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoCaldera() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Caldera1", 370,  60, 3, 3, 0));
        nodos.add(new NodoCultivo("Caldera2", 110, 180, 4, 3, 1));
        nodos.add(new NodoCultivo("Caldera3", 650, 180, 2, 3, 2));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoTablon() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Tablon1", 300, 100, 6, 4, 0));
        nodos.add(new NodoCultivo("Tablon2", 300, 200, 6, 4, 1));
        nodos.add(new NodoCultivo("Tablon3", 150, 180, 4, 4, 2));
        nodos.add(new NodoCultivo("Tablon4", 200, 280, 3, 4, 3));
        nodos.add(new NodoCultivo("Tablon5", 300, 320, 4, 4, 4));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoHuerto() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Huerto1", 300, 100, 6, 4, 0));
        nodos.add(new NodoCultivo("Huerto2", 300, 200, 6, 4, 1));
        nodos.add(new NodoCultivo("Huerto3", 150, 180, 4, 4, 2));
        nodos.add(new NodoCultivo("Huerto4", 200, 280, 3, 4, 3));
        nodos.add(new NodoCultivo("Huerto5", 300, 320, 4, 4, 4));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoCueva() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();
        nodos.add(new NodoCultivo("Cueva1", 300, 100, 6, 4, 0));
        nodos.add(new NodoCultivo("Cueva2", 300, 200, 6, 4, 1));
        nodos.add(new NodoCultivo("Cueva3", 150, 180, 4, 4, 2));
        nodos.add(new NodoCultivo("Cueva4", 200, 280, 3, 4, 3));
        nodos.add(new NodoCultivo("Cueva5", 300, 320, 4, 4, 4));
        return nodos;
    }

    public MiListaEnlazada<NodoCultivo> crearGrafoMultiverso() {
        MiListaEnlazada<NodoCultivo> nodos = new MiListaEnlazada<>();

        int offManual   = 0;
        int offAlacena  = 400;
        int offPecera   = 800;
        int offCaldera  = 1200;
        int offTablon   = 1600;
        int offHuerto   = 2000;
        int offCueva    = 2400;

        // Manualidades
        nodos.add(new NodoCultivo("Morado",    370,  60+offManual,   4, 0, 0));
        nodos.add(new NodoCultivo("Centro",    370, 220+offManual,   5, 0, 1));
        nodos.add(new NodoCultivo("Verde",     110, 180+offManual,   4, 0, 2));
        nodos.add(new NodoCultivo("Azul",      200, 280+offManual,   3, 0, 3));
        nodos.add(new NodoCultivo("Amarillo",  540, 280+offManual,   4, 0, 4));
        nodos.add(new NodoCultivo("Naranja",   650, 180+offManual,   4, 0, 5));

        // Alacena
        nodos.add(new NodoCultivo("Alacena1", 370,  60+offAlacena,  4, 1, 0));
        nodos.add(new NodoCultivo("Alacena2", 370, 220+offAlacena,  6, 1, 1));
        nodos.add(new NodoCultivo("Alacena3", 110, 180+offAlacena,  3, 1, 2));
        nodos.add(new NodoCultivo("Alacena4", 200, 280+offAlacena,  4, 1, 3));
        nodos.add(new NodoCultivo("Alacena5", 540, 280+offAlacena,  4, 1, 4));
        nodos.add(new NodoCultivo("Alacena6", 650, 180+offAlacena,  5, 1, 5));

        // Pecera
        nodos.add(new NodoCultivo("Pecera1", 370,  60+offPecera, 4, 2, 0));
        nodos.add(new NodoCultivo("Pecera2", 370, 220+offPecera, 5, 2, 1));
        nodos.add(new NodoCultivo("Pecera3", 110, 180+offPecera, 3, 2, 2));
        nodos.add(new NodoCultivo("Pecera4", 200, 280+offPecera, 4, 2, 3));
        nodos.add(new NodoCultivo("Pecera5", 540, 280+offPecera, 4, 2, 4));
        nodos.add(new NodoCultivo("Pecera6", 650, 180+offPecera, 4, 2, 5));

        // Caldera
        nodos.add(new NodoCultivo("Caldera1", 370,  60+offCaldera, 3, 3, 0));
        nodos.add(new NodoCultivo("Caldera2", 110, 180+offCaldera, 4, 3, 1));
        nodos.add(new NodoCultivo("Caldera3", 650, 180+offCaldera, 2, 3, 2));

        // Tablón
        nodos.add(new NodoCultivo("Tablon1",  370,  60+offTablon,  6, 4, 0));
        nodos.add(new NodoCultivo("Tablon2",  370, 220+offTablon,  6, 4, 1));
        nodos.add(new NodoCultivo("Tablon3",  110, 180+offTablon,  4, 4, 2));
        nodos.add(new NodoCultivo("Tablon4",  200, 280+offTablon,  3, 4, 3));
        nodos.add(new NodoCultivo("Tablon5",  540, 280+offTablon,  4, 4, 4));

        // Huerto
        nodos.add(new NodoCultivo("Huerto1",  370,  60+offHuerto,  6, 4, 0));
        nodos.add(new NodoCultivo("Huerto2",  370, 220+offHuerto,  6, 4, 1));
        nodos.add(new NodoCultivo("Huerto3",  110, 180+offHuerto,  4, 4, 2));
        nodos.add(new NodoCultivo("Huerto4",  200, 280+offHuerto,  3, 4, 3));
        nodos.add(new NodoCultivo("Huerto5",  540, 280+offHuerto,  4, 4, 4));

        // Cueva
        nodos.add(new NodoCultivo("Cueva1",   370,  60+offCueva,   6, 4, 0));
        nodos.add(new NodoCultivo("Cueva2",   370, 220+offCueva,   6, 4, 1));
        nodos.add(new NodoCultivo("Cueva3",   110, 180+offCueva,   4, 4, 2));
        nodos.add(new NodoCultivo("Cueva4",   200, 280+offCueva,   3, 4, 3));
        nodos.add(new NodoCultivo("Cueva5",   540, 280+offCueva,   4, 4, 4));

        return nodos;
    }

    /**
     * Conecta todos los nodos de la lista en un ciclo:
     *  - nodos[i].siguiente = nodos[i+1]
     *  - el último conecta con el primero
     */
    public void conectarNodos(MiListaEnlazada<NodoCultivo> nodos) {
        if (nodos.size() < 2) return;
        for (int i = 0; i < nodos.size() - 1; i++) {
            NodoCultivo actual = nodos.get(i);
            NodoCultivo sig    = nodos.get(i + 1);
            actual.conectar(sig);
        }
        // Cierra el ciclo
        nodos.get(nodos.size() - 1).conectar(nodos.get(0));
    }
}
