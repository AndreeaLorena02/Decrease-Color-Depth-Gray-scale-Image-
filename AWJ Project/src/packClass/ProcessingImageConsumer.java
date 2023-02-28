//CONSUMER

package packClass;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

public class ProcessingImageConsumer extends AbstractImage implements Runnable {
    private Buffer buffer;
    private DataOutputStream out;

    private DataInputStream in;

    public ProcessingImageConsumer(DataInputStream in, DataOutputStream out, Buffer buffer, int h, int w) {
        super(h, w);
        this.buffer = buffer;
        this.out = out;
        this.in = in;
    }

    public void run() {
        System.out.println("Processing : Image size : height = " + h + " ; width = " + w);

        int[][] pixels = new int[h][w]; //matrice care va contine toti pixelii imaginii si care va fi folosit pentru a procesa apoi pixelii si a crea noua imagine
        for (int segment = 0; segment < 4; segment++) {  //parcurgem cele 4 segmente si in functie de nr segmentului vom modifica i pentru a pune pixelii pe pozitia corecta in matricea pixels
            LocalDateTime start = LocalDateTime.now();
            int[][] bufferedPixels = buffer.getPixels();
            for (int i = 0; i < h / 4; i++) {
                for (int j = 0; j < w; j++) {
                    int actualI = i + segment * h / 4;
                    pixels[actualI][j] = bufferedPixels[i][j];
                }
            }
            LocalDateTime end = LocalDateTime.now();
            System.out.println(String.format("Consumat segmentul %s; Durata= %s ms", segment, Duration.between(start, end).toMillis()));
        }

        System.out.println("Start procesare");
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int p = pixels[i][j];
                // System.out.println("rgb "+ rgb);

                int a = (p >> 24) & 0xFF;
                int r = (p >> 16) & 0xFF;
                int g = (p >> 8) & 0xFF;
                int b = (p) & 0xFF;

                r = r & 0xB9;// 0xB9=185 IN DECIMAL ; modific intensitatea culorilor
                g = g & 0xB9;
                b = b & 0xB9;
                // set new RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;

                pixels[i][j] = p;  //pun in matricea de pixeli noile valori ale pixelilor
            }
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println(String.format("Procesare finalizata; Durata= %s ms", Duration.between(start, end).toMillis()));

        for (int segmentNumber = 0; segmentNumber < 4; segmentNumber++) {// Iteram prin fiecare bucata din imagine
            start = LocalDateTime.now();

            for (int i = 0; i < h / 4; i++) {
                for (int j = 0; j < w; j++) {
                    int actualI = i + segmentNumber * h / 4;

                    try {
                        out.writeInt(pixels[actualI][j]); //transmit prin pipe fiecare segment de imagine(pixel cu pixel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            end = LocalDateTime.now();
            System.out.println(String.format("Segment trimis catre WriteResult %s; Durata= %s ms", segmentNumber, Duration.between(start, end).toMillis()));

            try {
                sleep(SLEEP_DURATION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Inchidere pipeOut
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
