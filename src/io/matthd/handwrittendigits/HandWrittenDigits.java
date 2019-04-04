package io.matthd.handwrittendigits;

import io.matthd.handwrittendigits.mnist.MnistData;
import io.matthd.handwrittendigits.mnist.MnistImageFileReader;
import io.matthd.handwrittendigits.mnist.MnistLabelFileReader;
import io.matthd.neurallibrary.network.Network;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class HandWrittenDigits {

    private Network network;
    private MnistData data;

    public static void main(String[] args) {

        HandWrittenDigits main = new HandWrittenDigits();
        main.loadData();
        main.guessImage(true, 100000);
    }

    public void loadData() {
         data = new MnistData("HandWrittenDigits/resources/LoadedMnist.txt");
        //data = new MnistData("HandWrittenDigits/resources/train-images.idx3-ubyte", "HandWrittenDigits/resources/train-labels.idx1-ubyte");
        //data.serialize();

        int[] networkSizes = new int[] {784, 28, 10};
        network = new Network(networkSizes);

        MnistImageFileReader imageReader = data.getImageReader();
        MnistLabelFileReader labelReader = data.getLabelReader();


        List<double[][]> imageInput = imageReader.getImagePixels();
        int[] labels = labelReader.getLabels();

        System.out.println("List: " + imageInput);

        List<double[]> batchInputs = new ArrayList<>();
        List<double[]> batchOutputs = new ArrayList<>();

        System.out.println("Creating batch of 5000 images");
        for (int i = 0; i < 500; i++) {

            double[] inputs = new double[28 * 28];
            double[] outputs = new double[10];
            double[][] pixels = imageInput.get(i);
            int count = 0;

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[x].length; y++) {
                    inputs[count] = pixels[x][y];
                    count++;
                }
            }

            for (int out = 0; out <= 9; out++) {
                if (labels[i] == out) {

                    outputs[out] = 1;
                }
                else {
                    outputs[out] = 0;
                }
            }

            batchInputs.add(inputs);
            batchOutputs.add(outputs);
            //TODO loop through pixels and create an input array of 784 size. Add x,y to list then add that to the training batch.
        }
        trainBatch(batchInputs, batchOutputs, 0, 100, 10000);

    }

    public void trainBatch(List<double[]> inputs, List<double[]> outputs, int start, int numOfBatches, int epochs) {
        System.out.println("Training " + numOfBatches + " batches. Each " + epochs + " times..");

        for (int batch = start; batch < numOfBatches && batch < inputs.size(); batch++) {
            double[] ins = inputs.get(batch);
            double[] outs = outputs.get(batch);

            for (int epoch = 0; epoch < epochs; epoch++) {
                network.train(ins, outs, 0.6);
            }
        }
    }

    public double guessImage(boolean trainIfWrong, int epochs) {
        double[] pixels = new double[784];
        double[] expected = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};

        try {
            BufferedImage image = ImageIO.read(new File("HandWrittenDigits/resources/test_guess.png"));

            int width = image.getWidth();
            int height = image.getHeight();
            int c = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = image.getRGB(x, y);

                    pixels[c] = (double) rgb;
                    c++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] guess = network.guess(pixels);

        int label = getHighestValue(guess);
        System.out.println("Network outs: " + Arrays.toString(network.guess(pixels)));
        System.out.println("Network guess: " + label);

        if (label != 9 && trainIfWrong) {
            for (int epoch = 0; epoch < epochs; epoch++) {
                network.train(pixels, expected, 0.5);
            }
            guessImage(true, 100000);
        }

        return label;
    }

    public int getHighestValue(double[] arr) {
        double max = Double.MIN_VALUE;
        int index = 0;
        int count = 0;
        for (double d : arr) {
            if (d > max) {
                max = d;
                index = count;
            }
            count++;
        }
        return index;
    }
}
