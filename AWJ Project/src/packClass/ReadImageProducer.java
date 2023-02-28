//PRODUCER
package packClass;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;

public class ReadImageProducer extends AbstractImage implements Runnable {
	private Buffer buffer;

	// pentru a salva imaginea
	BufferedImage image;
	int i, j;
	
 //bloc de initializare
	{
		image = null;
		i = 0;
		j = 0;

	}

	public ReadImageProducer(BufferedImage image, Buffer buffer, int h, int w) {
		super(h, w);
		this.buffer = buffer;
		this.image = image;
	}

	public void run() {
		// Matrice pentru a stoca pixelii
		int[][] pixels = new int[h / 4][w];

		try {
			// Primul sfert de imagine
			LocalDateTime start = LocalDateTime.now();
			for (i = 0; i < h / 4; i++) {
				for (j = 0; j < w; j++) {
					int p = image.getRGB(j, i);
					pixels[i][j] = p;
				}
			}
			buffer.savePixels(pixels);
			LocalDateTime end = LocalDateTime.now();
			System.out.println(
					String.format("Gata segmentul 0 produs; Durata= %s ms", Duration.between(start, end).toMillis()));
			sleep(SLEEP_DURATION);

			start = LocalDateTime.now();
			for (i = 0; i < h / 4; i++) {
				for (j = 0; j < w; j++) {
					// Pentru a lua al doilea sfert de imagine
					int actualI = i + h / 4;
					int p = image.getRGB(j, actualI);
					pixels[i][j] = p;
				}
			}
			buffer.savePixels(pixels);
			end = LocalDateTime.now();
			System.out.println(
					String.format("Gata segmentul 1 produs; Durata= %s ms", Duration.between(start, end).toMillis()));
			sleep(SLEEP_DURATION);

			start = LocalDateTime.now();
			for (i = 0; i < h / 4; i++) {
				for (j = 0; j < w; j++) {
					// Pentru a lua al treilea sfert de imagine
					int actualI = i + 2 * h / 4;
					int p = image.getRGB(j, actualI);
					pixels[i][j] = p;
				}
			}
			buffer.savePixels(pixels);
			end = LocalDateTime.now();
			System.out.println(
					String.format("Gata segmentul 2 produs; Durata= %s ms", Duration.between(start, end).toMillis()));
			sleep(SLEEP_DURATION);

			start = LocalDateTime.now();
			for (i = 0; i < h / 4; i++) {
				for (j = 0; j < w; j++) {
					// Pentru a lua ultimul sfert de imagine
					int actualI = i + 3 * h / 4;
					int p = image.getRGB(j, actualI);
					pixels[i][j] = p;
				}
			}
			buffer.savePixels(pixels);
			end = LocalDateTime.now();
			System.out.println(
					String.format("Gata segmentul 3 produs; Durata= %s ms", Duration.between(start, end).toMillis()));
			sleep(SLEEP_DURATION);

		} catch (InterruptedException e) {
			System.out.println("Error: " + e);
		}
	}

}
