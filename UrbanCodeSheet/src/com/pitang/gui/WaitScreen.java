package com.pitang.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class WaitScreen extends JDialog {
	
	private static final long serialVersionUID = 1791029314128865476L;

	private final JProgressBar progressBar;
	
	public WaitScreen(final JFrame parent) {
		super(parent);

		final JPanel panel = new JPanel();
		panel.setLayout(null);

		this.progressBar = new JProgressBar();
		this.progressBar.setIndeterminate(false);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString(0 + "%");
		this.progressBar.setBounds(25, 25, 240, 18);
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
		panel.add(this.progressBar);
		
		this.add(panel);
		this.setModal(true);
		this.setTitle("Aguarde...");
		this.setSize(300, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	public void setMaximum(final Integer maximum) {
		this.progressBar.setMaximum(maximum);
	}
	
	public void incrementProgressValue() {
		final int value = this.progressBar.getValue() + 1;
		final double maximum = this.progressBar.getMaximum();
		this.progressBar.setValue(value);
		this.progressBar.setString(Math.round(((value / maximum) * 100)) + "%");
	}
}
