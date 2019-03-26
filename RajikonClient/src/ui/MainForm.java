package ui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import agent.ServerClient;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class MainForm {
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 1500;

	private ServerClient serverClient;

	private VideoPanel videoPanel;
	private ControlPanel controlPanel;

	private JFrame frame;
	private JToolBar toolBar;
	private JSplitPane splitPane;
	private JMenu fileMenu;
	private JButton startButton;
	private JTextArea consoleBox;
	private JMenu settingMenu;
	private JMenuItem connectSettingMenuItem;
	private JButton endChannelButton;
	private JButton endSessionButton;

	public MainForm() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Rajikon Control Window");

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		settingMenu = new JMenu("Setting");
		menuBar.add(settingMenu);

		connectSettingMenuItem = new JMenuItem("Connectting...");
		connectSettingMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PropertiesDialog propertiesDialog = new PropertiesDialog();
				propertiesDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				propertiesDialog.setVisible(true);
			}
		});
		settingMenu.add(connectSettingMenuItem);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		startButton = new JButton("Start Server");
		startButton.setActionCommand("Start");
		startButton.addActionListener(new StartButtonClickListenerIMPL());
		startButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		toolBar.add(startButton);
		
		frame.getContentPane().add(toolBar, BorderLayout.SOUTH);
		
		endChannelButton = new JButton("End Channel");
		endChannelButton.setActionCommand("EndChannel");
		endChannelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		toolBar.add(endChannelButton);
		
		endSessionButton = new JButton("End Session");
		endSessionButton.setActionCommand("EndSession");
		endSessionButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		toolBar.add(endSessionButton);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane consoleSplitPane = new JSplitPane();
		consoleSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		videoPanel = new VideoPanel();
		consoleSplitPane.setLeftComponent(videoPanel);
		consoleBox = new JTextArea();
		consoleBox.setBackground(Color.BLACK);
		consoleBox.setForeground(Color.GREEN);
		JScrollPane scrollPane = new JScrollPane(consoleBox);
		consoleSplitPane.setRightComponent(scrollPane);
		
		splitPane.setLeftComponent(consoleSplitPane);

		controlPanel = new ControlPanel();
		splitPane.setRightComponent(controlPanel);

		frame.pack();

		splitPane.setDividerLocation(0.5);
		consoleSplitPane.setDividerLocation(0.7);
	}

	/**
	 * このメソッドを使ってServerClientにControlPanelを渡すことで
	 * ServerClient内にあるリスナーがメイン制御用の通信が確立したあとにControlHandlerを
	 * ControlPanelに渡すことができるようになる
	 * @return
	 */
	public ControlPanel getControlPanel(){
		return controlPanel;
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	/**
	 * VideoPanelに画像を表示させる
	 * @param update
	 */
	public void updateImage(BufferedImage update) {
		videoPanel.updateImage(update);
	}
	
	/**
	 * ConsoleBoxに文字を表示させる
	 * @param message
	 */
	public void printToConsole(String message){
		consoleBox.append(message);
	}

	private class StartButtonClickListenerIMPL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(serverClient == null){
				serverClient = new ServerClient(MainForm.this);
			}
			
			
			//通信開始
			if (e.getActionCommand().equals("Start")) {
				//プロパティから接続用のIPアドレスなどを取得し使用する
				PropertiesDialog propertiesDialog = new PropertiesDialog();
				Map<String, InetSocketAddress> connecInfo = propertiesDialog.getConnectInfo();
				if(connecInfo == null){
					//プロパティにデータがなければ設定させる
					propertiesDialog.setVisible(true);
				}
				
				//プロパティからのデータを使いサーバーを開始する.ControlPanelにリスナーをセットする.ハンドラは接続が確立されたあとに渡す必要があるのでここでは渡さない(ServerClientのリスナーで渡してある)
				serverClient.start(connecInfo.get(PropertiesDialog.CONTROL), connecInfo.get(PropertiesDialog.CAMERA).getPort());
				controlPanel.setControlChannelListener(serverClient.getControlChannelListener());
				
				startButton.setText("Stop");
				startButton.setActionCommand("Stop");
			}else if(e.getActionCommand().equals("Stop")){
				serverClient.stop();
				
				startButton.setText("Start");
				startButton.setActionCommand("Start");
			}else if(e.getActionCommand().equals("EndChannel")){
				if(serverClient.getControlHandler() != null){
					serverClient.getControlHandler().endChannel();
					printToConsole("End Channel");
				}
			}else if(e.getActionCommand().equals("EndSession")){
				serverClient.getControlHandler().endSession();
				printToConsole("End Session");
			}
		}

	}
}
