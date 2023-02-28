package packClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class WriterResult extends AbstractImage implements PipeWriter {
    DataInputStream in;
    String outputFilePath;
    BufferedImage image;

    public WriterResult(DataInputStream in, String outputFilePath, int h, int w) {
        super(h, w);
        this.in = in;
        this.outputFilePath = outputFilePath;
        image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            LocalDateTime start = LocalDateTime.now();
            writeImageSegment(i);
            LocalDateTime end = LocalDateTime.now();
            System.out.println(String.format("Am scris segmentul= %s; Durata= %s ms", i, Duration.between(start, end).toMillis()));
            try {
                sleep(SLEEP_DURATION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Inchidere pipeIn
            in.close();

            // Output file path
            File output_file = new File(outputFilePath);

            // scrierea imaginii la adresa pe care am dat-o
            ImageIO.write(image, "bmp", output_file);
            
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        System.out.println("Scriere finalizata!");
    }

    @Override
    public void writeImageSegment(int segmentNumber) {
        try {
            for (int i = 0; i < h / 4; i++) {
                for (int j = 0; j < w; j++) {
                    int p = in.readInt();
                    image.setRGB(j, i + segmentNumber * h / 4, p);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
