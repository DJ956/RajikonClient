package channel;

import java.awt.image.BufferedImage;

public interface ICameraChannelListener {
	void receiveImage(BufferedImage image);
	void onStartServer(int port);
	void onDisconnected();
	void onException(Throwable t);
}
