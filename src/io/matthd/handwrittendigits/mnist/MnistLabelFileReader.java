package io.matthd.handwrittendigits.mnist;

import java.io.*;

public class MnistLabelFileReader implements Serializable {
    private static final long serialVersionUID = 1L;

    private int[] labels;

    public MnistLabelFileReader() {}

    public MnistLabelFileReader(String fileName) {

        try {

            FileInputStream fin = new FileInputStream(fileName);
            DataInputStream din = new DataInputStream(fin);

            int magicNum = din.readInt();

            if (magicNum != 2049) {
                throw new MnistException("Magic number is incorrect for a label file!");
            }

            int numLabels = din.readInt();
            this.labels = new int[numLabels];

            for (int i = 0; i < numLabels; i++) {
                labels[i] = din.readByte() & 0xff;
            }

            din.close();
            fin.close();

        } catch (IOException | MnistException e) {
            e.printStackTrace();
        }
    }



    public int[] getLabels() {
        return labels;
    }
}
