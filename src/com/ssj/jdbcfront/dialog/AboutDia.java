package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutDia extends JDialog implements ActionListener  {
	private static final long serialVersionUID = 1L;
	
	private JTextArea about = new JTextArea();
	
	private JTextArea help = new JTextArea();

	public AboutDia(JFrame f){

		super(f, true);
		this.setSize(340, 240);
		this.setLocationRelativeTo(null);
		Font font = new Font("����", Font.PLAIN, 12);
		JPanel downPanel = new JPanel(new FlowLayout());
		JButton ok = new JButton("ȷ��");
		ok.setFont(font);
		ok.addActionListener(this);
		downPanel.add(ok);
		this.getContentPane().add(downPanel,BorderLayout.SOUTH);
		about.setEditable(false);
		about.setLineWrap(true);
		help.setEditable(false);
		help.setLineWrap(true);
		this.getContentPane().add(new JScrollPane(about),BorderLayout.CENTER);
		this.getContentPane().add(new JScrollPane(help),BorderLayout.CENTER);
		StringBuffer tmp = new StringBuffer();
		tmp.append("���������������˧�ܶ��Կ�����ɣ������ʲô��������������˧����ϵ��\r\n\r\n");
		tmp.append("E-mail��shuaijie506@163.com\r\n");
		tmp.append("    QQ��94121497");
		about.setText(tmp.toString());
		about.setVisible(true);
		tmp.delete(0,tmp.length());
		tmp.append("���������ֻ�ܷ���Oracle9I���ݿ⣬�Դ���Oracle9I�����ݿ������������ٰ�װoracle�ͻ��ˡ�\r\n");
		tmp.append("�����ڵ�½������д���ݿ���Ϣ���ܵ�½�����ݿ⣬������κ�������˵�����ݿ�Ĳ���oracle9i����汾���ԣ����ʵ����������ϵ��\r\n");
		tmp.append("������½������б���չʾ�������ݿ��и��û��µ����б����ͼ�����Ҫ����PO����ѡ��һ���򼸸�PO������Ҽ�ѡ������PO���Ϳ�����PO��\r\n");
		tmp.append("����Ŀǰ�汾ΪV1.0��ֻ�����ɻ�����PO�ļ��������������ݿ���еĵ�һ���ֶΣ������ʵ�ʵ���������������Ǹ��������������е�����\r\n");
		help.setText(tmp.toString());
		help.setVisible(true);
//		about;
	}
	
	public void setAbout(){
		help.setVisible(false);
		about.setVisible(true);
	}
	
	public void setHelp(){
		about.setVisible(false);
		help.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɷ������
		this.setVisible(false);
	}
}
