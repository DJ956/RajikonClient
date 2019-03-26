package agent;

import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import channel.CameraChannel;
import channel.ControlChannel;
import channel.ICameraChannelListener;
import channel.IControlChannelListener;
import handler.IAudioControlHandler;
import handler.IControlHandler;
import ui.MainForm;

public class ServerClient implements IServerClient {

	private ExecutorService worker = Executors.newFixedThreadPool(2);
	
	private volatile ControlChannel controlChannel = null;
	private volatile CameraChannel cameraChannel = null;
	
	private IControlChannelListener controlChannelListener;
	private ICameraChannelListener cameraChannelListener;
	
	private MainForm mainForm;
	
	/**
	 * 
	 * @param mainForm リスナーの動作のために使う
	 */
	public ServerClient(MainForm mainForm) {
		this.mainForm = mainForm;
		controlChannelListener = new ControlChannelListenerIMPL();
		cameraChannelListener = new CameraChannelListenerIMPL();
	}
	
	@Override
	public void start(InetSocketAddress mainAddress, int cameraPort) {
		controlChannel = new ControlChannel(mainAddress.getAddress(), mainAddress.getPort(), controlChannelListener);
		worker.execute(new Runnable() {
			@Override
			public void run() {
				controlChannel.start();
			}
		});
		
		cameraChannel = new CameraChannel(cameraPort, cameraChannelListener);
		worker.execute(new Runnable() {
			@Override
			public void run() {
				cameraChannel.start();
			}
		});
		
	}

	@Override
	public void stop() {
		if(controlChannel != null){
			controlChannel.stop();
		}
		
		if(cameraChannel != null){
			cameraChannel.stop();
		}
	}
	
	public boolean isConnected(){
		return controlChannel.isConnected();
	}
	
	public IControlHandler getControlHandler(){
		return controlChannel.getHandler();
	}
	
	/**
	 * MainFormにあるControlPanelに渡すため(未実装)
	 * @return
	 */
	public IAudioControlHandler getAudioControlHandler(){
		return null;
	}
	
	/**
	 * MainFormにあるControlPanel内のOperationPanelに渡すため
	 * @return
	 */
	public IControlChannelListener getControlChannelListener(){
		return controlChannelListener;
	}
	
	/**
	 * 
	 * @return
	 */
	public ICameraChannelListener getCameraChannelListener(){
		return cameraChannelListener;
	}
	
	/**
	 * MainFormに画像を表示させる
	 * @author dexte
	 *
	 */
	private class CameraChannelListenerIMPL implements ICameraChannelListener {
		@Override
		public void receiveImage(BufferedImage image) {
			mainForm.updateImage(image);
		}
		
		@Override
		public void onStartServer(int port) {
			mainForm.printToConsole(String.format("Camera Receiver Start:%d\n", port));
		}

		@Override
		public void onDisconnected() {
			mainForm.printToConsole(String.format("Camera Receiver Disconnected\n"));
		}
		
		@Override
		public void onException(Throwable t) {
			mainForm.printToConsole(String.format("Error on Camera Receiver:%s\n", t.getMessage()));
		}
	}
	
	/**
	 * ControlChannelで起きたイベントの内容をMainFormのコンソールに表示させる
	 * @author dexte
	 *
	 */
	private class ControlChannelListenerIMPL implements IControlChannelListener {
		
		@Override
		public void onConnected(SocketAddress address) {
			mainForm.printToConsole(String.format("Control Channel Connected:%s\n",address.toString()));
			mainForm.getControlPanel().setControlHandler(getControlHandler());
		}

		@Override
		public void onDisconnected() {
			mainForm.printToConsole(String.format("Control Channel Disconnected\n"));
		}

		@Override
		public void onSendControl(String command, int percentage) {
			mainForm.printToConsole(String.format("Send Control Command %s:%d\n", command,percentage));
		}

		@Override
		public void onException(Throwable t) {
			mainForm.printToConsole(String.format("Error on Control Channel:%s\n", t.getMessage()));
		}
		
	}

}
