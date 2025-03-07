package com.mycompany.modeloia;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ItemClassifier {

    // Diccionario para almacenar (nombre de archivo -> embedding)
    private Map<String, float[]> databaseEmbeddings = new HashMap<>();

    // Predictor para obtener embeddings usando MobileNetV2 preentrenado
    private Predictor<Image, float[]> predictor;

    public ItemClassifier() throws Exception {
        // Se define el Criteria para cargar MobileNetV2 con DJL y se establece un Translator personalizado
        Criteria<Image, float[]> criteria = Criteria.builder()
                .setTypes(Image.class, float[].class)
                .optArtifactId("mobilenet")
                .optEngine("TensorFlow")
                .optFilter("flavor", "v2")  // intenta con "v2" para MobileNetV2
                .optTranslator(new MobileNetTranslator())
                .build();

        ZooModel<Image, float[]> model = criteria.loadModel();
        predictor = model.newPredictor();
    }

    // Translator personalizado para MobileNetV2: redimensiona, normaliza y prepara la imagen
    public static class MobileNetTranslator implements Translator<Image, float[]> {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            // Redimensionar la imagen a 224x224
            Image resized = input.resize(224, 224, false);
            // Convertir la imagen a NDArray (se asume formato RGB)
            NDArray array = resized.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
            System.out.println("Shape original: " + array.getShape());
            // Convertir a float32 y normalizar: (valor/127.5 - 1) para rango [-1, 1]
            array = array.toType(DataType.FLOAT32, true).div(127.5f).sub(1.0f);
            // Agregar la dimensión de batch: [1, 224, 224, 3]
            
            return new NDList(array);
        }

        @Override
        public float[] processOutput(TranslatorContext ctx, NDList list) {
            // Se espera un NDArray de salida con forma [1, embedding_dim]
            NDArray embedding = list.singletonOrThrow().squeeze();
            return embedding.toFloatArray();
        }

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }
    }

    // Extrae el embedding de una imagen dada su ruta
    public float[] extractFeatures(String imagePath) throws IOException, TranslateException {
        Image image = ImageFactory.getInstance().fromFile(Paths.get(imagePath));
        return predictor.predict(image);
    }

    // Construye la base de datos de embeddings recorriendo la carpeta de sprites
    public void buildDatabase(String folderPath) throws IOException, TranslateException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
            }
        });

        if (files != null) {
            for (File file : files) {
                float[] emb = extractFeatures(file.getAbsolutePath());
                databaseEmbeddings.put(file.getName(), emb);
                System.out.println("Procesado: " + file.getName());
            }
        }
        System.out.println("Base de datos cargada con " + databaseEmbeddings.size() + " ítems.");
    }

    // Calcula la distancia euclidiana entre dos vectores
    public static float euclideanDistance(float[] a, float[] b) {
        float sum = 0;
        for (int i = 0; i < a.length; i++) {
            float diff = a[i] - b[i];
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }

    // Dada la ruta de una imagen de prueba, encuentra el ítem más cercano en la base de datos
    public PredictionResult predictItem(String testImagePath) throws IOException, TranslateException {
        float[] testEmb = extractFeatures(testImagePath);
        String bestLabel = null;
        float bestDistance = Float.MAX_VALUE;
        for (Map.Entry<String, float[]> entry : databaseEmbeddings.entrySet()) {
            float dist = euclideanDistance(testEmb, entry.getValue());
            if (dist < bestDistance) {
                bestDistance = dist;
                bestLabel = entry.getKey();
            }
        }
        return new PredictionResult(bestLabel, bestDistance);
    }

    // Clase para encapsular el resultado de la predicción
    public static class PredictionResult {
        public String label;
        public float distance;

        public PredictionResult(String label, float distance) {
            this.label = label;
            this.distance = distance;
        }
    }

    // Método main de ejemplo
    public static void main(String[] args) {
        try {
            // Crear el clasificador
            ItemClassifier classifier = new ItemClassifier();

            // Ruta a la carpeta de sprites en NetBeans
            String itemsFolder = "C:\\Users\\cjcue\\Documents\\NetBeansProjects\\ModeloIA\\src\\main\\resources\\ITEMS";
            classifier.buildDatabase(itemsFolder);

            // Imagen de prueba: por ejemplo, "sprite_0.png" dentro de la misma carpeta
            String testImagePath = "C:\\Users\\cjcue\\Documents\\NetBeansProjects\\ModeloIA\\src\\main\\resources\\ITEMS\\sprite_0.png";

            PredictionResult result = classifier.predictItem(testImagePath);

            System.out.println("Predicción: " + result.label);
            System.out.printf("Distancia: %.4f%n", result.distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
