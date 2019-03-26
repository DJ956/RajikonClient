package channel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import handler.ControlHandler;
import handler.IControlHandler;

public class ControlChannel implements Channel {

	private final InetAddress CONTROL_ADDRESS;
	private final int PORT;
	
	private Socket clientSocket = null;
	
	private IControlHandler handler;
	private IControlChannelListener listener;
	
	/**
	 * メイン制御の通信を行う
	 * @param controlAddress 接続するホストのIPアドレス
	 * @param controlPort 接続するホストのポート番号
	 * @param listener 接続や切断、例外発生を検知する
	 */
	public ControlChannel(InetAddress controlAddress, int controlPort, IControlChannelListener listener) {
		this.CONTROL_ADDRESS = controlAddress;
		this.PORT = controlPort;
		this.listener = listener;
	}
	
	@Override
	public void start() {
		try{
			clientSocket = new Socket(CONTROL_ADDRESS, PORT);
			handler = new ControlHandler(clientSocket.getOutputStream());
			listener.onConnected(clientSocket.getRemoteSocketAddress());
		}catch(IOException e){
			listener.onException(e);
		}
	}

	@Override
	public void stop() {
		if(clientSocket != null){
			try{
				clientSocket.close();
				listener.onDisconnected();
			}catch(IOException e){
				listener.onException(e);
			}
		}
	}
	
	public boolean isConnected(){
		return clientSocket.isConnected();
	}
	
	/**
	 * ここで生成したハンドラを使用して操作を実行する
	 * @return メイン操作を行うためのハンドラ
	 */
	public IControlHandler getHandler(){
		return handler;
	}
}
