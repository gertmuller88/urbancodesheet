package com.pitang.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import com.pitang.control.Controller;
import com.pitang.control.IncludeController;
import com.pitang.control.RenameController;
import com.pitang.control.ReviewController;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = -4744083079719737609L;
	
	private File file;
	
	public MainScreen() {
		this.file = null;
		
		final JPanel panel = new JPanel();
		panel.setLayout(null);

		final JLabel fileLabel = new JLabel("Arquivo:");
		fileLabel.setBounds(25, 20, 45, 20);
		panel.add(fileLabel);

		final JTextField filePathText = new JTextField();
		filePathText.setBounds(80, 20, 300, 20);
		filePathText.setEnabled(false);
		filePathText.setEditable(false);
		filePathText.setBackground(Color.WHITE);
		panel.add(filePathText);

		final JButton chooseFileButton = new JButton();
		chooseFileButton.setText("Abrir");
		chooseFileButton.setBounds(390, 19, 70, 22);
		chooseFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Planilhas do Excel (*.xls, *.xlsx)", "xls", "xlsx"));
				final int returnValue = fileChooser.showOpenDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					MainScreen.this.setFile(fileChooser.getSelectedFile());
					filePathText.setText(MainScreen.this.getFile().getAbsolutePath());
				}
			}
		});
		panel.add(chooseFileButton);

		final NumberFormatter integerFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
		integerFormatter.setValueClass(Integer.class);
		integerFormatter.setAllowsInvalid(false);
		integerFormatter.setMinimum(1);
		integerFormatter.setMaximum(Integer.MAX_VALUE);

		final JLabel startLabel = new JLabel("Início:");
		startLabel.setBounds(25, 50, 45, 20);
		panel.add(startLabel);

		final JFormattedTextField startText = new JFormattedTextField(integerFormatter);
		startText.setBounds(80, 50, 90, 20);
		startText.setHorizontalAlignment(SwingConstants.RIGHT);
		startText.setText("2");
		panel.add(startText);

		final JLabel endLabel = new JLabel("Fim:");
		endLabel.setBounds(25, 80, 45, 20);
		panel.add(endLabel);

		final JFormattedTextField endText = new JFormattedTextField(integerFormatter);
		endText.setBounds(80, 80, 90, 20);
		endText.setHorizontalAlignment(SwingConstants.RIGHT);
		endText.setText("500");
		panel.add(endText);

		final JLabel taskLabel = new JLabel("Tarefa:");
		taskLabel.setBounds(25, 120, 45, 20);
		panel.add(taskLabel);
		
		final ButtonGroup taskGroup = new ButtonGroup();
		
		final JRadioButton includeComponents = new JRadioButton("Incluir Componentes", true);
		includeComponents.setBounds(80, 120, 250, 20);
		taskGroup.add(includeComponents);
		panel.add(includeComponents);
		
		final JRadioButton reviewComponents = new JRadioButton("Revisar Componentes", true);
		reviewComponents.setBounds(80, 140, 250, 20);
		taskGroup.add(reviewComponents);
		panel.add(reviewComponents);
		
		final JRadioButton renameComponents = new JRadioButton("Renomear Componentes", true);
		renameComponents.setBounds(80, 160, 250, 20);
		taskGroup.add(renameComponents);
		panel.add(renameComponents);
		
		final JButton importButton = new JButton();
		importButton.setText("Importar");
		importButton.setBounds(310, 200, 150, 42);
		importButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				final WaitScreen waitScreen = new WaitScreen(MainScreen.this);

				final Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						Controller controller = null;

						if(renameComponents.isSelected()) {
							controller = RenameController.getInstance();
						} else if(reviewComponents.isSelected()) {
							controller = ReviewController.getInstance();
						} else {
							controller = IncludeController.getInstance();
						}

						try {
							final String result = controller.execute(waitScreen, MainScreen.this.getFile(), Integer.parseInt(startText.getText()), Integer.parseInt(endText.getText()));
							JOptionPane.showMessageDialog(waitScreen, result, "Resultados", JOptionPane.INFORMATION_MESSAGE);
						} catch(final Exception e) {
							e.printStackTrace();
							final StringWriter sw = new StringWriter();
							e.printStackTrace(new PrintWriter(sw));
							JOptionPane.showMessageDialog(waitScreen, sw.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
						}
						
						waitScreen.dispose();
					}
				});
				thread.start();

				waitScreen.setVisible(true);
			}
		});
		panel.add(importButton);
		
		this.add(panel);
		this.setTitle("UrbanCodeSheet");
		this.setSize(500, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.addWindowFocusListener(new WindowAdapter() {
			
			@Override
			public void windowGainedFocus(final WindowEvent e) {
				super.windowGainedFocus(e);
				importButton.requestFocusInWindow();
			}
		});
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

}
