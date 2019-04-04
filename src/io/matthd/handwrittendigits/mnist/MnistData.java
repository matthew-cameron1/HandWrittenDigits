package io.matthd.handwrittendigits.mnist;

import java.io.*;

public class MnistData {

    private MnistImageFileReader imageReader;
    private MnistLabelFileReader labelReader;

    public MnistData(String imageFile, String labelFile) {
        this.imageReader = new MnistImageFileReader(imageFile);
        this.labelReader = new MnistLabelFileReader(labelFile);
    }

    public MnistData(String file) {
        System.out.println("Attempting to load file MNIST data from file!");
        try {
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(fin);
            this.imageReader = (MnistImageFileReader) oin.readObject();
            this.labelReader = (MnistLabelFileReader) oin.readObject();
            oin.close();
            fin.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void serialize() {
        try {
            FileOutputStream f = new FileOutputStream("HandWrittenDigits/resources/LoadedMnist.txt", true);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(this.imageReader);
            o.writeObject(this.labelReader);
            o.close();
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MnistImageFileReader getImageReader() {
        return imageReader;
    }

    public MnistLabelFileReader getLabelReader() {
        return labelReader;
    }
}
