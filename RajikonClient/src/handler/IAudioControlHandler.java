package handler;

import java.io.File;

public interface IAudioControlHandler {
	void updateData();
	void addData();
	void removeData();
	void saveImage(File file);
	void recordStart(File file);
	void recordStop();
	void inputAudio();
}
