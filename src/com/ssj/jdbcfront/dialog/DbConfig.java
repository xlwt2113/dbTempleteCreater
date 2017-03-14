package com.ssj.jdbcfront.dialog;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.jdbcfront.model.DbTypeView;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class DbConfig extends JDialog {

	private static final long serialVersionUID = 1L;
	private DbConfig _this;
	private JSplitPane jSplitPane = null;
	private JList jList = null;
	private DefaultListModel leftListModel=null;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JComboBox dbType = null;
	private JTextField ip = null;
	private JTextField port = null;
	private JTextField sid = null;
	private JTextField driver = null;
	private JLabel jLabel4 = null;
	private JButton jButton = null;
	private JPanel jPanel1 = null;
	private JLabel jLabel5 = null;
	private JTextField tnsName = null;
	private JButton jButton1 = null;
	private JButton jButton11 = null;
	private JButton jButton12 = null;
	private JLabel jLabel31 = null;
	private JLabel jLabel32 = null;
	private JTextField username = null;
	private JTextField password = null;
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        _this = this;
        this.setSize(new Dimension(394, 349));
        this.setContentPane(getJSplitPane());
        this.setModal(true);
        this.setTitle("���ݿ�����");
	}

	/**
	 * ����ָ�Ԫ�أ��������Ϊ����������
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerSize(3);
			jSplitPane.setDividerLocation(80);
			jSplitPane.setLeftComponent(new JScrollPane(getLeftList()));
			jSplitPane.setRightComponent(getMainPanel());
		}
		return jSplitPane;
	}

	/**
	 * ������ߵ��б�
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLeftList() {
		if (jList == null) {
			jList = new JList();
			jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					DataBaseView tns = (DataBaseView) jList.getSelectedValue();
					setTnsView(tns);
					jButton12.setEnabled(jList.getSelectedIndex()>=0);
				}
			});
			leftListModel = new DefaultListModel();
			jList.setModel(leftListModel);
			List<DataBaseView> list = ConfigUtil.getTnsViewList();
			for(int i=0;i<list.size();i++){
				leftListModel.addElement(list.get(i));
			}
		}
		return jList;
	}
	
	public void setSelectedDbv(DataBaseView dbv){
		jList.setSelectedValue(dbv, true);
	}

	/**
	 * �ұ����
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRightPanel() {
		if (jPanel1 == null) {
			jLabel32 = new JLabel();
			jLabel32.setBounds(new Rectangle(16, 225, 78, 18));
			jLabel32.setText("���룺");
			jLabel32.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel31 = new JLabel();
			jLabel31.setBounds(new Rectangle(16, 195, 78, 18));
			jLabel31.setText("�û�����");
			jLabel31.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(16, 14, 78, 18));
			jLabel5.setText("���ݿ������");
			jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(10, 77, 84, 22));
			jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel4.setText("���ݿ�������");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(16, 167, 78, 22));
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel3.setText("���ݿ����ƣ�");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(16, 137, 78, 22));
			jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel2.setText("�˿ڣ�");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(16, 107, 78, 22));
			jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel1.setText("IP��ַ��");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(10, 47, 84, 22));
			jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel.setText("���ݿ����ͣ�");
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(9, 8, 285, 261));
			jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanel1.add(jLabel, null);
			jPanel1.add(jLabel1, null);
			jPanel1.add(jLabel2, null);
			jPanel1.add(jLabel3, null);
			jPanel1.add(getDbType(), null);
			jPanel1.add(getIp(), null);
			jPanel1.add(getPort(), null);
			jPanel1.add(getSid(), null);
			jPanel1.add(getDriver(), null);
			jPanel1.add(jLabel4, null);
			jPanel1.add(getBtnOpenFile(), null);
			jPanel1.add(jLabel5, null);
			jPanel1.add(getTnsName(), null);
			jPanel1.add(jLabel31, null);
			jPanel1.add(jLabel32, null);
			jPanel1.add(getUsername(), null);
			jPanel1.add(getPassword(), null);
		}
		return jPanel1;
	}

	/**
	 * �����ұߵĲ���
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getRightPanel(), null);
			jPanel.add(getBtnSave(), null);
			jPanel.add(getBtnSaveAs(), null);
			jPanel.add(getBtnDelete(), null);
		}
		if(leftListModel.getSize()>0){
			jList.setSelectedIndex(0);
		}
		return jPanel;
	}

	/**
	 * ѡ�����ݿ�����
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getDbType() {
		if (dbType == null) {
			dbType = new JComboBox();
			dbType.setBounds(new Rectangle(106, 47, 169, 22));
			dbType.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					DbTypeView dbv = (DbTypeView) ((JComboBox)e.getSource()).getSelectedItem();
					getTnsName().setText(dbv.getName());
					getDriver().setText(dbv.getDriverPath());
					if(StringUtil.isBlank(getIp().getText())){
						getIp().setText(dbv.getIp());
					}
					getPort().setText(dbv.getPort());
					getSid().setText(dbv.getSid());
					getUsername().setText(dbv.getUsername());
					getPassword().setText(dbv.getPassword());
				}});
			List<DbTypeView> list = ConfigUtil.getDefineDbType();
			for(int i=0;i<list.size();i++){
				dbType.addItem(list.get(i));
			}
		}
		return dbType;
	}

	/**
	 * ���ݿ�IP��ַ
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getIp() {
		if (ip == null) {
			ip = new JTextField();
			ip.setBounds(new Rectangle(106, 107, 169, 22));
		}
		return ip;
	}

	/**
	 * ���ݿ�˿�
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPort() {
		if (port == null) {
			port = new JTextField();
			port.setBounds(new Rectangle(106, 137, 169, 22));
		}
		return port;
	}

	/**
	 * ���ݿ�ʵ����
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSid() {
		if (sid == null) {
			sid = new JTextField();
			sid.setBounds(new Rectangle(106, 167, 169, 22));
		}
		return sid;
	}

	/**
	 * ���ݿ�JDBC����·��
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDriver() {
		if (driver == null) {
			driver = new JTextField();
			driver.setBounds(new Rectangle(106, 77, 131, 22));
		}
		return driver;
	}

	/**
	 * ���ݿ����
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTnsName() {
		if (tnsName == null) {
			tnsName = new JTextField();
			tnsName.setBounds(new Rectangle(106, 15, 169, 22));
		}
		return tnsName;
	}

	/**
	 * �ļ��򿪰�ť������󵯳��ļ�ѡ�������ѡ��JDBC����λ��
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOpenFile() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setLocation(new Point(241, 78));
			jButton.setText("���...");
			jButton.setSize(new Dimension(34, 20));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser jc=new JFileChooser(); //�ļ�ѡ����
					String path = ConfigUtil.getString("lastOpenFilePath");
					if(StringUtil.isNotBlank(driver.getText())){
						jc.setCurrentDirectory(new File(driver.getText()));
					}
					else if(StringUtil.isNotBlank(path)){
						jc.setCurrentDirectory(new File(path));
					}
					int result=jc.showOpenDialog(Frame.getInstance());
					ConfigUtil.setString("lastOpenFilePath", jc.getCurrentDirectory().getAbsolutePath());
					if(result==JFileChooser.APPROVE_OPTION){
						driver.setText(jc.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return jButton;
	}

	/**
	 * ���水ť
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSave() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("����");
			jButton1.setLocation(new Point(31, 280));
			jButton1.setSize(new Dimension(70, 22));
			jButton1.setMnemonic(KeyEvent.VK_O);
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DataBaseView view = genTnsView();
					if(jList.getSelectedIndex()<0){
						leftListModel.add(leftListModel.getSize(), view);
						jList.setSelectedIndex(leftListModel.getSize()-1);
					}
					DataBaseView oldView = (DataBaseView) jList.getSelectedValue();
					if(!oldView.getName().equals(view.getName())){
						ConfigUtil.removeTnsView(oldView);
					}
					ConfigUtil.saveTnsView(view , view.getName());
					leftListModel.set(jList.getSelectedIndex(), view);
					Frame.getInstance().loginDia.initDataBaseList();
					Frame.getInstance().loginDia.getDataBaseView().setSelectedItem(ConfigUtil.getTnsView(view.getName()));
					_this.setVisible(false);
					Frame.getInstance().loginDia.tipLabel.setVisible(true);
					new Thread(){
						public void run(){
							try {
								Thread.sleep(600);
							} catch (InterruptedException e) {
							}
							SwingUtilities.invokeLater(new Runnable(){
								public void run(){
									Frame.getInstance().loginDia.tipLabel.setVisible(false);
								}
							});
						}
					}.start();
				}

			});
		}
		return jButton1;
	}

	/**
	 * ���Ϊ��ť
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSaveAs() {
		if (jButton11 == null) {
			jButton11 = new JButton();
			jButton11.setText("���Ϊ");
			jButton11.setSize(new Dimension(75, 22));
			jButton11.setLocation(new Point(113, 280));
			jButton11.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DataBaseView view = genTnsView();
					int i=0;
					for(i=0;i<leftListModel.getSize();i++){
						if(((DataBaseView)leftListModel.get(i)).getName().equals(view.getName()))
							break;
					}
					String name = null;
					if(i<leftListModel.getSize()){
						name = ((DataBaseView)leftListModel.get(i)).getName();
						if(2==JOptionPane.showConfirmDialog(Frame.getInstance(), "ϵͳ�Ѿ���������Ϊ["+name+"]������Դ���������潫�Ḳ�Ǿɵ����ݣ��Ƿ������", "��ʾ", JOptionPane.OK_CANCEL_OPTION))
							return;
					}
					ConfigUtil.saveTnsView(view , name);
					int pos = i;
					if(i<leftListModel.getSize())
						leftListModel.set(i, view);
					else{
						pos = leftListModel.getSize();
						leftListModel.add(leftListModel.getSize(), view);
					}
					jList.setSelectedIndex(pos);
					Frame.getInstance().loginDia.initDataBaseList();
				}
			});
		}
		return jButton11;
	}
	
	/**
	 * ɾ����ť
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDelete() {
		if (jButton12 == null) {
			jButton12 = new JButton();
			jButton12.setText("ɾ��");
			jButton12.setSize(new Dimension(60, 22));
			jButton12.setEnabled(false);
			jButton12.setLocation(new Point(201, 280));
			jButton12.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int pos = jList.getSelectedIndex();
					if(pos<0)
						return;
					if(JOptionPane.NO_OPTION==JOptionPane.showConfirmDialog(Frame.getInstance(), "�Ƿ�Ҫɾ����ѡ��Ŀ��","ȷ��",JOptionPane.YES_NO_OPTION)){
						return;
					}
					Object[] objs = jList.getSelectedValues();
					for(int i=0;i<objs.length;i++){
						leftListModel.removeElement(objs[i]);
						ConfigUtil.removeTnsView((DataBaseView)objs[i]);
					}
					Frame.getInstance().loginDia.initDataBaseList();
				}
			});
		}
		return jButton12;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2010-11-25  ����10:10:55
	* ��������: ���û���������ת���ɶ���
	* �����Ĳ����ͷ���ֵ: 
	* @return
	*/
	private DataBaseView genTnsView() {
		DataBaseView view =null;
		try {
			view = new DataBaseView((DbTypeView)dbType.getSelectedItem());
			view.setName(tnsName.getText());
			view.setSid(sid.getText());
			view.setIp(ip.getText());
			view.setPort(port.getText());
			view.setDriver(driver.getText());
			view.setUserName(username.getText());
			view.setPassword(password.getText());
		} catch (AppException e) {
			LogUtil.logError(e);
		}
		return view;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2010-11-25  ����10:11:19
	* ��������: �Ӷ���ת������������
	* �����Ĳ����ͷ���ֵ: 
	* @param view
	*/
	private void setTnsView(DataBaseView view) {
		if(view==null)return;
		dbType.setSelectedItem(view.getDbType());
		tnsName.setText(view.getName());
		sid.setText(view.getSid());
		ip.setText(view.getIp());
		port.setText(view.getPort());
		driver.setText(view.getDriver());
		username.setText(view.getUserName());
		password.setText(view.getPassword());
	}
	/**
	 * ���ݿ��û���
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUsername() {
		if (username == null) {
			username = new JTextField();
			username.setBounds(new Rectangle(106, 194, 169, 22));
		}
		return username;
	}

	/**
	 * ���ݿ�����
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPassword() {
		if (password == null) {
			password = new JTextField();
			password.setBounds(new Rectangle(107, 223, 169, 22));
		}
		return password;
	}

	/**
	 * ���췽��
	 */
	public DbConfig() {
		super(Frame.getInstance());
		initialize();
		//
		this.setLocationRelativeTo(null);
	}
	

}  //  @jve:decl-index=0:visual-constraint="10,10"