package io.matthd.handwrittendigits.mnist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MnistImageFileReader implements Serializable {
    private static final long serialVersionUID = 1L;
    private int rows, cols;
    private int images;
    private File file;

    List<double[][]> imagePixels = new ArrayList<>();

    public MnistImageFileReader() {}

    public MnistImageFileReader(String fileName) {
        this.file = new File(fileName);

        try {
            FileInputStream fin = new FileInputStream(file);
            DataInputStream din = new DataInputStream(fin);

            int magicNumber = din.readInt();

            if (magicNumber != 2051) {
                System.out.println("Image breaks");
                throw new MnistException("The MNIST image file has the incorrect magic number!");
            }
            this.images = din.readInt();
            this.rows = din.readInt();
            this.cols = din.readInt();

            System.out.println("Reading " + images + " input images");


            for (int i = 0; i < images; i++) {
                double[][] pixels = new double[rows][];
                for (int rowCount = 0; rowCount < rows; rowCount++) {
                    pixels[rowCount] = new double[cols];

                    for (int colCount = 0; colCount < cols; colCount++) {
                        pixels[rowCount][colCount] = ((double)(din.readByte() & 0xff))/255.0;
                    }
                }

                this.imagePixels.add(pixels);
            }

            din.close();
            fin.close();

            System.out.println("Finished loading " + images + " images." );

        } catch (IOException | MnistException e) {
            e.printStackTrace();
        }
    }

    public List<double[][]> getImagePixels() {
        return imagePixels;
    }
}
