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


    private Map<String, float[]> databaseEmbeddings = new HashMap<>();


    private Predictor<Image, float[]> predictor;

    public ItemClassifier() throws Exception {

        Criteria<Image, float[]> criteria = Criteria.builder()
                .setTypes(Image.class, float[].class)
                .optArtifactId("mobilenet")
                .optEngine("TensorFlow")
                .optFilter("flavor", "v2")  
                .optTranslator(new MobileNetTranslator())
                .build();

        ZooModel<Image, float[]> model = criteria.loadModel();
        predictor = model.newPredictor();
    }


    public static class MobileNetTranslator implements Translator<Image, float[]> {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
       
            Image resized = input.resize(224, 224, false);

            NDArray array = resized.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
            System.out.println("Shape original: " + array.getShape());

            array = array.toType(DataType.FLOAT32, true).div(127.5f).sub(1.0f);

            
            return new NDList(array);
        }

        @Override
        public float[] processOutput(TranslatorContext ctx, NDList list) {
       
            NDArray embedding = list.singletonOrThrow().squeeze();
            return embedding.toFloatArray();
        }

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }
    }


    public float[] extractFeatures(String imagePath) throws IOException, TranslateException {
        Image image = ImageFactory.getInstance().fromFile(Paths.get(imagePath));
        return predictor.predict(image);
    }

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


    public static float euclideanDistance(float[] a, float[] b) {
        float sum = 0;
        for (int i = 0; i < a.length; i++) {
            float diff = a[i] - b[i];
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }

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


    public static class PredictionResult {
        public String label;
        public float distance;

        public PredictionResult(String label, float distance) {
            this.label = label;
            this.distance = distance;
        }
    }


    public static void main(String[] args) {
        try {

            ItemClassifier classifier = new ItemClassifier();

            String itemsFolder = "C:\\Users\\cjcue\\Documents\\NetBeansProjects\\ModeloIA\\src\\main\\resources\\ITEMS";
            classifier.buildDatabase(itemsFolder);

            String testImagePath = "C:\\Users\\cjcue\\Documents\\NetBeansProjects\\ModeloIA\\src\\main\\resources\\ITEMS\\sprite_0.png";

            PredictionResult result = classifier.predictItem(testImagePath);

            System.out.println("Predicción: " + result.label);
            System.out.printf("Distancia: %.4f%n", result.distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
