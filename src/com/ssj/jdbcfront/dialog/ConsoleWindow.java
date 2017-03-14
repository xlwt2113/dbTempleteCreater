package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;

	/**
	 * This is the xxx default constructor
	 */
	public ConsoleWindow() {
		super("¿ØÖÆÌ¨",true,true,true,true);
		initialize();
	}

	private PrintStream systemout = System.out;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 450);
		this.setContentPane(getJContentPane());
		System.setOut(new PrintStream(new OutputStream(){
			public void write(int arg0) throws IOException {
				systemout.write(arg0);
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				super.write(b, off, len);
				print(new String(b,off,len));
			}

		}));
	}

	@Override
	public void doDefaultCloseAction() {
		this.setVisible(false);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBackground(Color.black);
			jTextArea.setForeground(Color.green);
		}
		return jTextArea;
	}

	public void print(Object o){
		jTextArea.setText(jTextArea.getText()+o);
	}

	public void println(Object o){
		jTextArea.setText(jTextArea.getText()+o+"\n");
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
