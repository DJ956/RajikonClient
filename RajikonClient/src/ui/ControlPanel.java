package ui;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import channel.IControlChannelListener;
import handler.IAudioControlHandler;
import handler.IControlHandler;

import java.awt.Dimension;
import java.awt.BorderLayout;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Dimension SIZE = new Dimension(1500, 500);
	private OperationPanel operationPanel;
	private AudioControlPanel audioControlPanel;

	private IControlHandler iControlHandler;
	private IAudioControlHandler iAudioInputPanelHandler;
	
	private IControlChannelListener controlChannelListener;
	
	public ControlPanel() {
		setPreferredSize(SIZE);
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane jSplitPane = new JSplitPane();
		add(jSplitPane, BorderLayout.CENTER);
		
		audioControlPanel = new AudioControlPanel();
		jSplitPane.setRightComponent(audioControlPanel);
		
		operationPanel = new OperationPanel();
		jSplitPane.setLeftComponent(operationPanel);
		
		jSplitPane.setResizeWeight(0.5);
	}
	
	/**
	 * OperationPanelにメイン操作を行うためのハンドラを渡す
	 * @param iControlHandler
	 */
	public void setControlHandler(IControlHandler iControlHandler){
		this.iControlHandler = iControlHandler;
		operationPanel.setControlHandler(this.iControlHandler);
	}
	
	/**
	 * AudioPanelに音声通信を行うためのハンドラを渡す(未実装)
	 * @param iAudioInputPanelHandler
	 */
	public void setAudioControlHandler(IAudioControlHandler iAudioInputPanelHandler){
		this.iAudioInputPanelHandler = iAudioInputPanelHandler;
		audioControlPanel.setAudioControlHandler(this.iAudioInputPanelHandler);
	}
	
	/**
	 * OperationPanelにメイン操作ボタンが抑えたことを検知するためのリスナーを渡す
	 * @param listener
	 */
	public void setControlChannelListener(IControlChannelListener listener){
		this.controlChannelListener = listener;
		operationPanel.setControlListener(this.controlChannelListener);
	}
	
	/**
	 * 
	 * @return
	 */
	public OperationPanel getOperationPanel(){
		return operationPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	public AudioControlPanel getAudioControlPanel(){
		return audioControlPanel;
	}
}
