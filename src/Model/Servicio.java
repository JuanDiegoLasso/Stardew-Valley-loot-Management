package Model;

import java.util.ArrayList;
import java.util.List;

public class Servicio {

    public static class NodoCultivo {
        public String nombre;
        public int x, y;
        public NodoCultivo siguiente;
        public boolean completado = false;
        public boolean[] circulos;
        

        public int sourceTree;
        public int sourceNodeIndex;

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

    public List<NodoCultivo> crearGrafoManualidades() {
        List<NodoCultivo> nodos = new ArrayList<>();

        nodos.add(new NodoCultivo("Morado",    370, 60, 4, 0, 0));
        nodos.add(new NodoCultivo("Centro",    370, 220,5, 0, 1));
        nodos.add(new NodoCultivo("Verde",     110, 180,4, 0, 2));
        nodos.add(new NodoCultivo("Azul",      200, 280,3, 0, 3));
        nodos.add(new NodoCultivo("Amarillo",  540, 280,4, 0, 4));
        nodos.add(new NodoCultivo("Naranja",   650, 180,4, 0, 5));
        return nodos;
    }


    public List<NodoCultivo> crearGrafoAlacena() {
        List<NodoCultivo> nodos = new ArrayList<>();

        nodos.add(new NodoCultivo("Alacena1", 370, 60, 4, 1, 0));
        nodos.add(new NodoCultivo("Alacena2", 370, 220,6, 1, 1));
        nodos.add(new NodoCultivo("Alacena3", 110, 180,3, 1, 2));
        nodos.add(new NodoCultivo("Alacena4", 200, 280,4, 1, 3));
        nodos.add(new NodoCultivo("Alacena5", 540, 280,4, 1, 4));
        nodos.add(new NodoCultivo("Alacena6", 650, 180,5, 1, 5));
        return nodos;
    }


    public List<NodoCultivo> crearGrafoPecera() {
        List<NodoCultivo> nodos = new ArrayList<>();

        nodos.add(new NodoCultivo("Pecera1", 370, 60, 4, 2, 0));
        nodos.add(new NodoCultivo("Pecera2", 370, 220,5, 2, 1));
        nodos.add(new NodoCultivo("Pecera3", 110, 180,3, 2, 2));
        nodos.add(new NodoCultivo("Pecera4", 200, 280,4, 2, 3));
        nodos.add(new NodoCultivo("Pecera5", 540, 280,4, 2, 4));
        nodos.add(new NodoCultivo("Pecera6", 650, 180,4, 2, 5));
        return nodos;
    }


    public List<NodoCultivo> crearGrafoCaldera() {
        List<NodoCultivo> nodos = new ArrayList<>();

        nodos.add(new NodoCultivo("Caldera1", 370, 60, 3, 3, 0));
        nodos.add(new NodoCultivo("Caldera2", 110, 180,4, 3, 1));
        nodos.add(new NodoCultivo("Caldera3", 650, 180,2, 3, 2));
        return nodos;
    }


    public List<NodoCultivo> crearGrafoTablon() {
        List<NodoCultivo> nodos = new ArrayList<>();

        nodos.add(new NodoCultivo("Tablon1", 300, 100, 6, 4, 0));
        nodos.add(new NodoCultivo("Tablon2", 300, 200, 6, 4, 1));
        nodos.add(new NodoCultivo("Tablon3", 150, 180, 4, 4, 2));
        nodos.add(new NodoCultivo("Tablon4", 200, 280, 3, 4, 3));
        nodos.add(new NodoCultivo("Tablon5", 300, 320, 4, 4, 4));
        return nodos;
    }


    public List<NodoCultivo> crearGrafoMultiverso() {
        List<NodoCultivo> nodos = new ArrayList<>();

        int offManual = 0;
        int offAlacena = 400;
        int offPecera = 800;
        int offCaldera = 1200;
        int offTablon = 1600;


        nodos.add(new NodoCultivo("Morado",    370, 60+offManual, 4, 0, 0));
        nodos.add(new NodoCultivo("Centro",    370, 220+offManual,5, 0, 1));
        nodos.add(new NodoCultivo("Verde",     110, 180+offManual,4, 0, 2));
        nodos.add(new NodoCultivo("Azul",      200, 280+offManual,3, 0, 3));
        nodos.add(new NodoCultivo("Amarillo",  540, 280+offManual,4, 0, 4));
        nodos.add(new NodoCultivo("Naranja",   650, 180+offManual,4, 0, 5));


        nodos.add(new NodoCultivo("Alacena1", 370, 60+offAlacena,4, 1, 0));
        nodos.add(new NodoCultivo("Alacena2", 370, 220+offAlacena,6, 1, 1));
        nodos.add(new NodoCultivo("Alacena3", 110, 180+offAlacena,3, 1, 2));
        nodos.add(new NodoCultivo("Alacena4", 200, 280+offAlacena,4, 1, 3));
        nodos.add(new NodoCultivo("Alacena5", 540, 280+offAlacena,4, 1, 4));
        nodos.add(new NodoCultivo("Alacena6", 650, 180+offAlacena,5, 1, 5));


        nodos.add(new NodoCultivo("Pecera1", 370, 60+offPecera,4, 2, 0));
        nodos.add(new NodoCultivo("Pecera2", 370, 220+offPecera,5, 2, 1));
        nodos.add(new NodoCultivo("Pecera3", 110, 180+offPecera,3, 2, 2));
        nodos.add(new NodoCultivo("Pecera4", 200, 280+offPecera,4, 2, 3));
        nodos.add(new NodoCultivo("Pecera5", 540, 280+offPecera,4, 2, 4));
        nodos.add(new NodoCultivo("Pecera6", 650, 180+offPecera,4, 2, 5));


        nodos.add(new NodoCultivo("Caldera1", 370, 60+offCaldera,3, 3, 0));
        nodos.add(new NodoCultivo("Caldera2", 110, 180+offCaldera,4, 3, 1));
        nodos.add(new NodoCultivo("Caldera3", 650, 180+offCaldera,2, 3, 2));


        nodos.add(new NodoCultivo("Tablon1", 300, 100+offTablon,6, 4, 0));
        nodos.add(new NodoCultivo("Tablon2", 300, 200+offTablon,6, 4, 1));
        nodos.add(new NodoCultivo("Tablon3", 150, 180+offTablon,4, 4, 2));
        nodos.add(new NodoCultivo("Tablon4", 200, 280+offTablon,3, 4, 3));
        nodos.add(new NodoCultivo("Tablon5", 300, 320+offTablon,4, 4, 4));

        return nodos;
    }

    public void conectarNodos(List<NodoCultivo> nodos) {
        if (nodos.size() < 2) return;
        for (int i = 0; i < nodos.size() - 1; i++) {
            nodos.get(i).conectar(nodos.get(i + 1));
        }
        nodos.get(nodos.size() - 1).conectar(nodos.get(0));
    }
}

