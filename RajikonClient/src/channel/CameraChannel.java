package channel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.imageio.ImageIO;

public class CameraChannel implements Channel {

	private static final int BUFFER_SIZE = 65536 * 2;
	
	private DatagramSocket cameraSocket;
	private final int PORT;
	
	private ICameraChannelListener listener;
	
	private DatagramPacket packet;
	
	/**
	 * カメラ映像の通信を行う
	 * @param cameraAddress カメラ映像の受信先IPアドレス
	 * @param port カメラ映像の受信先ポート番号
	 * @param listener カメラ映像を受け取ったときに検知するリスナー
	 */
	public CameraChannel(int port, ICameraChannelListener listener) {
		this.listener = listener;
		byte[] buffer = new byte[BUFFER_SIZE];
		this.PORT = port;
		packet = new DatagramPacket(buffer, 0,  buffer.length);
	}
	
	@Override
	public void start() {
		try{
			cameraSocket = new DatagramSocket(PORT);
			listener.onStartServer(PORT);
			while(true){
				if(cameraSocket.isClosed()){
					break;
				}
				cameraSocket.receive(packet);
				listener.receiveImage(ImageIO.read(new ByteArrayInputStream(packet.getData())));
			}
		}catch(IOException e){
			listener.onException(e);
		}finally{
			if(cameraSocket != null){
				cameraSocket.close();
			}
		}
	}

	@Override
	public void stop() {
		if(cameraSocket != null){
			cameraSocket.close();
		}
		listener.onDisconnected();
	}

}
