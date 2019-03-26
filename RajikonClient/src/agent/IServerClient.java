package agent;

import java.net.InetSocketAddress;

/**
 * 
 * @author dexte
 *
 */
public interface IServerClient {
	void start(InetSocketAddress mainAddress, int cameraPort);
	void stop();
}
