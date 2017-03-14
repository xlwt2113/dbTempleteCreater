package com.ssj.jdbcfront.dialog;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.SetterOnlyReflection;

public class LoginDia extends JDialog {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Frame frame;
	
	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JLabel jLabel3 = null;

	private JLabel jLabel31 = null;

	private JLabel jLabel32 = null;

	private JButton btnLogin = null;

	private JButton btnExit = null;

	private JTextField username = null;

	private JPasswordField password = null;

	private JComboBox dataBaseView = null;

	private JButton btnConfig = null;

	private DbConfig dbConfig = null;  //  @jve:decl-index=0:visual-constraint="454,12"

	private JCheckBox autoLogin = null;
	
	public LoginDia(Frame f) throws HeadlessException {
		super(f, true);
		frame = f;
		initialize();
		f.loginDia=this;
		if(getAutoLogin().isSelected()){
			login();
		}
	}

	private void initialize() {
        this.setSize(new Dimension(277, 197));
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setTitle("数据库登录");
        this.setBackground(Color.lightGray);
		String lastDb = ConfigUtil.getString("LAST_LOGIN");
		if(StringUtil.isNotBlank(lastDb)){
			for(int i=0;i<this.getDataBaseView().getItemCount();i++){
				if(lastDb.equals(this.getDataBaseView().getItemAt(i).toString())){
					this.getDataBaseView().setSelectedIndex(i);
					DbUtil.dbv = (DataBaseView) this.getDataBaseView().getItemAt(i);
					break;
				}
			}
		}
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getBtnLogin(), null);
			jContentPane.add(getBtnExit(), null);
		}
		return jContentPane;
	}
	
	public JPanel getContentPane(){
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			tipLabel = new JLabel();
			tipLabel.setOpaque(true);
			tipLabel.setBackground(Color.YELLOW);
			tipLabel.setBorder(null);
			tipLabel.setBounds(new Rectangle(182, 99, 67, 20));
			tipLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			tipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			tipLabel.setVisible(false);
			tipLabel.setText("保存成功！");
			jLabel32 = new JLabel();
			jLabel32.setText("数据库：");
			jLabel32.setSize(new Dimension(52, 22));
			jLabel32.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jLabel32.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jLabel32.setLocation(new Point(11, 71));
			jLabel31 = new JLabel();
			jLabel31.setText("密码：");
			jLabel31.setSize(new Dimension(52, 22));
			jLabel31.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel31.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jLabel31.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jLabel31.setLocation(new Point(11, 41));
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(11, 11, 52, 22));
			jLabel3.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			jLabel3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jLabel3.setText("用户名：");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(9, 8, 256, 125));
			jPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanel.add(jLabel3, null);
			jPanel.add(jLabel31, null);
			jPanel.add(jLabel32, null);
			jPanel.add(getUsername(), null);
			jPanel.add(getPassword(), null);
			jPanel.add(getDataBaseView(), null);
			jPanel.add(getBtnConfig(), null);
			jPanel.add(getAutoLogin(), null);
			jPanel.add(tipLabel, null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton();
			btnLogin.setBounds(new Rectangle(42, 140, 66, 23));
			btnLogin.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			btnLogin.setText("登录");
			btnLogin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					login();
				}
			});
		}
		return btnLogin;
	}
	
	private Thread loginThread;

	public JLabel tipLabel = null;

	private void login(){
		if(loginThread!=null){
			try {
				loginThread.join(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loginThread.interrupt();
			loginThread = null;
			btnLogin.setText("登录");
			return;
		}
		(loginThread=new Thread(){
			public void run(){
				setName("登录线程");
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
//						btnLogin.setEnabled(false);
						btnLogin.setText("取消登录");
					}
				});
				final Frame frame = Frame.getInstance();
				final DataBaseView dbv = (DataBaseView) dataBaseView.getSelectedItem();
				frame.curDb = dbv;
				if(dbv==null){
					Frame.showMsg("请选择要登录的数据库！");
					return;
				}
				frame.curDb.setUserName(username.getText());
				frame.curDb.setPassword(new String(password.getPassword()));
				ConfigUtil.setString("LAST_LOGIN", dbv.getName());
				try {
					frame.mainCon = DbUtil.createConnection();
				} catch (final Exception e1) {
					if(this.isInterrupted()){//如果已被强行停止，则直接返回，不再打印错误信息
						return;
					}
					LogUtil.logError(e1);
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							Frame.showMsg("登录失败！\n原因："+e1.getMessage());
//							btnLogin.setEnabled(true);
							btnLogin.setText("登录");
						}
					});
					return;
				}
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						frame.loginDia.setVisible(false);
						frame.initDbObjects();
						frame.setTitle("数据库工具:"+username.getText()+"@"+dbv.getSid()+"["+dbv.getIp()+"]");
//						btnLogin.setEnabled(true);
						btnLogin.setText("登录");
					}
				});
				loginThread = null;
			}
		}).start();
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnExit() {
		if (btnExit == null) {
			btnExit = new JButton();
			btnExit.setBounds(new Rectangle(157, 140, 66, 23));
			btnExit.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			btnExit.setText("退出");
			btnExit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return btnExit;
	}

	/**
	 * This method initializes username	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUsername() {
		if (username == null) {
			username = new JTextField();
			username.setBounds(new Rectangle(75, 11, 166, 22));
			username.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			username.addKeyListener(new java.awt.event.KeyAdapter() {   
				public void keyPressed(java.awt.event.KeyEvent e) {    
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						if(StringUtil.isBlank(password.getText())){
							password.requestFocus();
						}
						else{
							login();
						}
					}
				}
			});
		}
		return username;
	}

	/**
	 * This method initializes password	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
			password.setLocation(new Point(75, 41));
			password.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			password.setSize(new Dimension(166, 22));
			password.addKeyListener(new java.awt.event.KeyAdapter() {   
				public void keyPressed(java.awt.event.KeyEvent e) {  
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						login();
					}
				}
			});
		}
		return password;
	}

	/**
	 * This method initializes dataBaseView	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDataBaseView() {
		if (dataBaseView == null) {
			dataBaseView = new JComboBox();
			dataBaseView.setLocation(new Point(75, 71));
			dataBaseView.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			dataBaseView.setSize(new Dimension(132, 22));
			dataBaseView.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					DataBaseView view = (DataBaseView)dataBaseView.getSelectedItem();
					if(view==null)
						return;
					username.setText(view.getUserName());
					password.setText(view.getPassword());
					DbUtil.dbv = view;
				}
			});
			initDataBaseList();
		}
		return dataBaseView;
	}
	
	private DbConfig getDbConfig(){
		if(dbConfig==null){
			dbConfig = new DbConfig();
		}
		return dbConfig;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnConfig() {
		if (btnConfig == null) {
			btnConfig = new JButton();
			btnConfig.setText("配置");
			btnConfig.setSize(new Dimension(27, 22));
			btnConfig.setLocation(new Point(211, 71));
			btnConfig.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					getDbConfig().setSelectedDbv((DataBaseView) dataBaseView.getSelectedItem());
					getDbConfig().setVisible(true);
				}
			});
		}
		return btnConfig;
	}

	public void initDataBaseList() {
		dataBaseView.removeAllItems();
		List<DataBaseView> list = ConfigUtil.getTnsViewList();
		for(int i=0;i<list.size();i++){
			dataBaseView.addItem(list.get(i));
		}
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAutoLogin() {
		if (autoLogin == null) {
			autoLogin = new JCheckBox();
			autoLogin.setBounds(new Rectangle(73, 98, 83, 24));
			autoLogin.setText("自动登录");
			autoLogin.setSelected(ConfigUtil.getInt("auto_login")>0);
			autoLogin.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JCheckBox cb = (JCheckBox) e.getSource();
					ConfigUtil.setInt("auto_login", cb.isSelected()?1:0);
				}});
		}
		return autoLogin;
	}

}  //  @jve:decl-index=0:visual-constraint="113,6"
