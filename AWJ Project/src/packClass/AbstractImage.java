package packClass;

abstract class AbstractImage extends Thread {
	int w, h;
	static int SLEEP_DURATION;

	//bloc static de initializare
	static {
		SLEEP_DURATION = 1000; //pentru a evidentia etapele comunicarii folosesc sleep(SLEEP_DURATION)
	}

	//pentru a seta height si width
	public AbstractImage(int h, int w) {
		this.w = w;
		this.h = h;
	}
}
