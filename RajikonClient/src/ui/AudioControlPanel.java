package ui;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import handler.IAudioControlHandler;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Insets;

public class AudioControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton inputImageSaveButton;

	private IAudioControlHandler handler;
	
	public AudioControlPanel() {
		setLayout(new BorderLayout(0, 0));
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(20, "row", "column");
		dataset.addValue(43, "row1", "column1");
		
		JFreeChart chart = ChartFactory.createLineChart("Audio graphics", "row", "column", dataset);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		
		add(chartPanel, BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(5, 5, 5, 5));
		add(toolBar, BorderLayout.SOUTH);
		
		inputImageSaveButton = new JButton("SaveImage");
		inputImageSaveButton.setMargin(new Insets(2, 14, 2, 20));
		inputImageSaveButton.setBorder(new LineBorder(Color.GREEN));
		toolBar.add(inputImageSaveButton);
		
		JButton btnRec = new JButton("REC");
		btnRec.setMargin(new Insets(2, 14, 2, 20));
		btnRec.setBorder(new LineBorder(Color.RED));
		toolBar.add(btnRec);
	}
	
	public void setAudioControlHandler(IAudioControlHandler handler){
		this.handler = handler;
	}
}
