package channel;

import java.net.SocketAddress;

public interface IControlChannelListener {
	void onConnected(SocketAddress address);
	void onDisconnected();
	void onSendControl(String command, int percentage);
	void onException(Throwable t);
}
