package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MsgBox extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JLabel msg = new JLabel();
	
	private final MsgBox me = this;

	public MsgBox(JFrame f){
		
		super(f,true);
		this.setTitle("´íÎó");
		this.setSize(240,100);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		msg.setBounds(0,0,240,50);
		msg.setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(msg,BorderLayout.CENTER);
		msg.setFont(new Font("ËÎÌå",Font.PLAIN,13));
		JButton ok = new JButton("È·¶¨");
		ok.setBounds(70,0,60,25);
		JPanel j =new JPanel();
		j.add(ok);
		j.add(new JLabel());
		this.getContentPane().add(j,BorderLayout.SOUTH);
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				me.setVisible(false);
			}
		});
		this.setVisible(false);
	}
	
	public void setContent(String title,String msg){
		
		this.setTitle(title);
		this.msg.setText("<html><body>" + msg + "</body><html>");
	}
}
