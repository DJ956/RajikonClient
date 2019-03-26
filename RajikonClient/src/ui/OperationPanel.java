package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;

import channel.IControlChannelListener;
import handler.IControlHandler;

public class OperationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int BUTTON_WIDTH = 80;
	private static final int BUTTON_HEIGHT = 80;
	private JButton forwardButton;
	private JButton leftButton;
	private JButton backButton;
	private JButton rightButton;
	private JButton stopButton;
	private JScrollBar powerScrollBar;
	
	private IControlHandler handler;
	private IControlChannelListener listener;
	
	public OperationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane toolBarPanel = new JSplitPane();
		
		JLabel powerLabel = new JLabel("50%");
		toolBarPanel.setLeftComponent(powerLabel);
		
		powerScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0,0, 100);
		powerScrollBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				powerLabel.setText(e.getValue() + "%");
			}
		});
		toolBarPanel.setRightComponent(powerScrollBar);
		
		add(toolBarPanel, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		forwardButton = new JButton(IControlHandler.FORWARD);
		forwardButton.setActionCommand(IControlHandler.FORWARD);
		forwardButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		forwardButton.setBorder(new LineBorder(Color.CYAN));
		forwardButton.addActionListener(new ActionPerformedIMPL());
		panel.add(forwardButton, BorderLayout.NORTH);
		
		leftButton = new JButton(IControlHandler.LEFT);
		leftButton.setActionCommand(IControlHandler.LEFT);
		leftButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		leftButton.setBorder(new LineBorder(Color.CYAN, 1, true));
		leftButton.addActionListener(new ActionPerformedIMPL());
		panel.add(leftButton, BorderLayout.WEST);
		
		backButton = new JButton(IControlHandler.BACK);
		backButton.setActionCommand(IControlHandler.BACK);
		backButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		backButton.setBorder(new LineBorder(Color.CYAN, 1, true));
		backButton.addActionListener(new ActionPerformedIMPL());
		panel.add(backButton, BorderLayout.SOUTH);
		
		rightButton = new JButton(IControlHandler.RIGHT);
		rightButton.setActionCommand(IControlHandler.RIGHT);
		rightButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		rightButton.setBorder(new LineBorder(Color.CYAN, 1, true));
		rightButton.addActionListener(new ActionPerformedIMPL());
		panel.add(rightButton, BorderLayout.EAST);
		
		stopButton = new JButton(IControlHandler.STOP);
		stopButton.setActionCommand(IControlHandler.STOP);
		stopButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		stopButton.setBorder(new LineBorder(Color.CYAN));
		stopButton.addActionListener(new ActionPerformedIMPL());
		panel.add(stopButton, BorderLayout.CENTER);

		toolBarPanel.setDividerLocation(35);
	}
	
	/**
	 * 操作用のハンドラを渡す
	 * @param handler
	 */
	public void setControlHandler(IControlHandler handler){
		this.handler = handler;
	}
	
	/**
	 * 操作ボタンを押されたことを通知するためのリスナーを渡す
	 * @param listener
	 */
	public void setControlListener(IControlChannelListener listener){
		this.listener = listener;
	}
	
	/**
	 * 操作ボタンを押すと操作ハンドラに命令が飛びリスナーが検知する
	 * @author dexte
	 *
	 */
	private class ActionPerformedIMPL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(handler == null){
				System.out.println("Control Handler is Null");
				return;
			}
			
			int percentage = powerScrollBar.getValue();
			switch(e.getActionCommand()){
				case IControlHandler.FORWARD:{
					handler.forward(percentage);
					onAction(IControlHandler.FORWARD, percentage);
					break;
				}
				case IControlHandler.LEFT:{
					handler.left(percentage);
					onAction(IControlHandler.LEFT, percentage);
					break;
				}
				case IControlHandler.RIGHT:{
					handler.right(percentage);
					onAction(IControlHandler.RIGHT, percentage);
					break;
				}
				case IControlHandler.STOP:{
					handler.stop(IControlHandler.STOP_BOTH);
					onAction(IControlHandler.STOP, IControlHandler.STOP_BOTH);
					break;
				}
				case IControlHandler.BACK:{
					handler.back(percentage);
					onAction(IControlHandler.BACK, percentage);
					break;
				}
				default:{
					break;
				}
			}
		}
		
		private void onAction(String command, int percentage){
			if(listener != null){
				listener.onSendControl(command, percentage);
			}
		}
	}

}
