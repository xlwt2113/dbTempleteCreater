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
		Font font = new Font("楷体", Font.PLAIN, 12);
		JPanel downPanel = new JPanel(new FlowLayout());
		JButton ok = new JButton("确定");
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
		tmp.append("　　此软件是由宋帅杰独自开发完成，如果有什么建议和疑问请和宋帅杰联系。\r\n\r\n");
		tmp.append("E-mail：shuaijie506@163.com\r\n");
		tmp.append("    QQ：94121497");
		about.setText(tmp.toString());
		about.setVisible(true);
		tmp.delete(0,tmp.length());
		tmp.append("　　此软件只能访问Oracle9I数据库，自带了Oracle9I的数据库驱动，无需再安装oracle客户端。\r\n");
		tmp.append("　　在登陆窗口填写数据库信息就能登陆到数据库，如果有任何问题则说明数据库的不是oracle9i，或版本不对，请核实，或与我联系。\r\n");
		tmp.append("　　登陆后左边列表中展示的是数据库中该用户下的所有表和视图，如果要生成PO，请选择一个或几个PO，点击右键选择生成PO，就可生成PO。\r\n");
		tmp.append("　　目前版本为V1.0，只能生成基本的PO文件，主键采用数据库表中的第一个字段，如果和实际的主键不符或表中是复合主键的请自行调整。\r\n");
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
		// TODO 自动生成方法存根
		this.setVisible(false);
	}
}
