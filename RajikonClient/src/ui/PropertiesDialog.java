package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PropertiesDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final File PROPERTIES_FILE = new File("config.properties");
	
	public static final String CONTROL = "Control";
	public static final String CAMERA = "Camera";
	public static final String AUDIO = "Audio";
	
	private static final String CONTROL_IP = "CONTROL_IP";
	private static final String CONTROL_PORT = "CONTROL_PORT";
	private static final String CAMEAR_IP = "CAMERA_IP";
	private static final String CAMERA_PORT = "CAMERA_PORT";
	private static final String AUDIO_IP = "AUDIO_IP";
	private static final String AUDIO_PORT = "AUDIO_PORT";
	
	private final JPanel contentPanel = new JPanel();
	private JTextField controlIpTextBox;
	private JTextField cameraPortTextBox;
	private JTextField cameraIpTextBox;
	private JTextField controlPortTextBox;
	private JTextField audioIpTextBox;
	private JTextField audioPortTextBox;
	
	public PropertiesDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(new Dimension(450, 280));
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Properties");
		getContentPane().setLayout(new BorderLayout());
		FlowLayout fl_contentPanel = new FlowLayout();
		fl_contentPanel.setAlignment(FlowLayout.LEFT);
		contentPanel.setLayout(fl_contentPanel);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblMainControl = new JLabel("Main Control:");
			contentPanel.add(lblMainControl);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				JLabel lblIpAddress = new JLabel("IP Address:");
				panel.add(lblIpAddress);
			}
			{
				controlIpTextBox = new JTextField();
				panel.add(controlIpTextBox);
				controlIpTextBox.setColumns(10);
			}
			{
				JLabel label = new JLabel("Port Number:");
				panel.add(label);
			}
			{
				controlPortTextBox = new JTextField();
				panel.add(controlPortTextBox);
				controlPortTextBox.setColumns(10);
			}
		}
		{
			JLabel lblCamera = new JLabel("Camera:      ");
			contentPanel.add(lblCamera);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				JLabel label = new JLabel("IP Address:");
				panel.add(label);
			}
			{
				cameraIpTextBox = new JTextField();
				panel.add(cameraIpTextBox);
				cameraIpTextBox.setColumns(10);
			}
			{
				JLabel label = new JLabel("Port Number:");
				panel.add(label);
			}
			{
				cameraPortTextBox = new JTextField();
				cameraPortTextBox.setColumns(10);
				panel.add(cameraPortTextBox);
			}
		}
		{
			JLabel lblAudio = new JLabel("Audio:        ");
			contentPanel.add(lblAudio);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				JLabel label = new JLabel("IP Address:");
				panel.add(label);
			}
			{
				audioIpTextBox = new JTextField();
				audioIpTextBox.setColumns(10);
				panel.add(audioIpTextBox);
			}
			{
				JLabel label = new JLabel("Port Number:");
				panel.add(label);
			}
			{
				audioPortTextBox = new JTextField();
				audioPortTextBox.setColumns(10);
				panel.add(audioPortTextBox);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Properties properties = new Properties();
						properties.setProperty(CONTROL_IP, controlIpTextBox.getText());
						properties.setProperty(CONTROL_PORT, controlPortTextBox.getText());
						
						properties.setProperty(CAMEAR_IP, cameraIpTextBox.getText());
						properties.setProperty(CAMERA_PORT, cameraPortTextBox.getText());
						
						properties.setProperty(AUDIO_IP, audioIpTextBox.getText());
						properties.setProperty(AUDIO_PORT, audioPortTextBox.getText());
						
						try(FileOutputStream outputStream = new FileOutputStream(PROPERTIES_FILE)){
							properties.store(outputStream, "connect config");
						}catch(IOException ex){
							JOptionPane.showMessageDialog(getRootPane(), ex.getMessage());
						}
						setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		init();
	}
	
	private void init(){
		Properties properties = new Properties();
		if(PROPERTIES_FILE.exists()){
			try(FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE)){
				properties.load(inputStream);
				
				controlIpTextBox.setText(properties.getProperty(CONTROL_IP));
				controlPortTextBox.setText(properties.getProperty(CONTROL_PORT));
				
				cameraIpTextBox.setText(properties.getProperty(CAMEAR_IP));
				cameraPortTextBox.setText(properties.getProperty(CAMERA_PORT));
				
				audioIpTextBox.setText(properties.getProperty(AUDIO_IP));
				audioPortTextBox.setText(properties.getProperty(AUDIO_PORT));
			}catch(IOException e){
				JOptionPane.showMessageDialog(getParent(), e.getMessage());
			}
		}
	}
	
	public Map<String, InetSocketAddress> getConnectInfo(){
		if(PROPERTIES_FILE.exists()){
			Properties properties = new Properties();
			try(FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE)){
				Map<String, InetSocketAddress> connectInfo = new HashMap<>();
				properties.load(inputStream);
				
				InetSocketAddress controlAddress = new InetSocketAddress(properties.getProperty(CONTROL_IP),
						Integer.valueOf(properties.getProperty(CONTROL_PORT)));
				connectInfo.put(CONTROL, controlAddress);
				
				InetSocketAddress cameraAddress = new InetSocketAddress(properties.getProperty(CAMEAR_IP),
						Integer.valueOf(properties.getProperty(CAMERA_PORT)));
				connectInfo.put(CAMERA, cameraAddress);
				
				InetSocketAddress audioAddress = new InetSocketAddress(properties.getProperty(AUDIO_IP),
						Integer.valueOf(properties.getProperty(AUDIO_PORT)));
				connectInfo.put(AUDIO, audioAddress);
				
				return connectInfo;
			}catch(IOException e){
				return null;
			}
		}else{
			return null;
		}
	}

}
