package packTest;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import packClass.*;

import static java.lang.Thread.sleep;

public class MyMain {
	public static void main(String[] args) throws IOException, InterruptedException {

		// imaginile trebuie puse in directorul in care se afla proiectul , in  src -> packClass
		
		String inputFilePath = null;
		String outputFilePath = null;
		// Mesaj de eroare in cazul in care nu au fost folosite argumentele
		// corespunzatoare
		if (args.length != 2) {
			System.out.println("Introduceti numele imaginii dorite (extensie .bmp)!");

			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String inputFilePath2 = (br.readLine()).toString();
			inputFilePath = System.getProperty("user.dir") + "/src/packClass/" + inputFilePath2;
			
			System.out.println("Introduceti numele imaginii noi create (extensie .bmp)!");

			String outputFilePath2 = (br.readLine()).toString();
			outputFilePath = System.getProperty("user.dir") + "/src/packClass/" + outputFilePath2;


		} else if (args.length == 2) {
			inputFilePath = args[0]; // numele/adresa imaginii pe care doresc sa
										// o procesez
			outputFilePath = args[1]; // numele/adresa imaginii pe care o obtin
		}

		// Creare pipe-uri

		PipedOutputStream pipeOut = new PipedOutputStream();
		PipedInputStream pipeIn = new PipedInputStream(pipeOut);

		DataOutputStream out = new DataOutputStream(pipeOut);
		DataInputStream in = new DataInputStream(pipeIn);

		File input_file = new File(inputFilePath);

		// Citire imagine pentru a afla height si width
		BufferedImage image = ImageIO.read(input_file);

		int width = image.getWidth(); // get width of image
		int height = image.getHeight();// get height of image
		// System.out.println("Imaginea are width : " + width + " height : " +
		// height);

		Buffer b = new Buffer(height, width);
		ReadImageProducer producer = new ReadImageProducer(image, b, height, width);
		ProcessingImageConsumer consumer = new ProcessingImageConsumer(in, out, b, height, width);
		WriterResult writerResult = new WriterResult(in, outputFilePath, height, width);

		producer.start();
		consumer.start();
		writerResult.start();

	}

}