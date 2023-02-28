package packClass;

//Clasa Buffer cu sincronizare
public class Buffer {
	int[][] pixels; // o matrice de pixeli care va contine un sfert(1/4) din
					// imagine
	private boolean available = false;

	public Buffer(int h, int w) {
		pixels = new int[h / 4][w]; // am impartit imaginea pe linii : h/4 ;
									// o matrice pixels va avea dimensiunea [h/4][w]
	}

	public synchronized int[][] getPixels() {
		while (!this.available) {
			try {
				wait(); // Asteapta producatorul sa puna matricea de pixeli
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.available = false;
		notifyAll();
		// Intoarce matricea de pixeli
		return pixels;
	}

	public synchronized void savePixels(int[][] pixels) {
		while (this.available) {
			try {
				// Asteapta consumatorul sa preia matricea de pixeli
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Pune noua matrice de pixeli
		this.pixels = pixels;
		available = true;
		notifyAll();
	}
}