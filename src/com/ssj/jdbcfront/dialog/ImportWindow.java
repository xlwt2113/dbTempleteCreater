package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.exception.SdbFileFormatException;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.io.SdbFileReader;
import com.ssj.jdbcfront.io.SqlFileReader;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.jdbcfront.util.FixedInputStream;
import com.ssj.util.DateUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class ImportWindow extends JInternalFrame{

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JScrollPane jScrollPane = null;
	private JTextArea txtConsole = null;
	private JLabel jLabel = null;
	private JTextField txtInput = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel2 = null;
	/**
	 * This method initializes 
	 * 
	 */
	public ImportWindow() {
		super("导入对象(数据)",true,true,true,true);
		initialize();
		txtInput.setText(ConfigUtil.getString("IMPORT_FILE_PATH"));
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(565, 439));
        this.setContentPane(getJPanel());
			
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJPanel1(), BorderLayout.NORTH);
			jPanel.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel = new JLabel();
			jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel.setText("导入文件：");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.setPreferredSize(new Dimension(10, 29));
			jPanel1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			jPanel1.add(jLabel, BorderLayout.WEST);
			jPanel1.add(getJButton1(), BorderLayout.EAST);
			jPanel1.add(getJPanel2(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtConsole());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes txtConsole	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtConsole() {
		if (txtConsole == null) {
			txtConsole = new JTextArea();
			txtConsole.setBackground(Color.black);
			txtConsole.setForeground(Color.green);
		}
		return txtConsole;
	}

	/**
	 * This method initializes txtInput	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtInput() {
		if (txtInput == null) {
			txtInput = new JTextField();
		}
		return txtInput;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("...");
			jButton.setPreferredSize(new Dimension(30, 28));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser jc=new JFileChooser(); //文件选择器
					jc.setDialogTitle("请选择文件输出路径");
					jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					String path = ConfigUtil.getString("EXPORT_FILE_PATH");
					if(StringUtil.isNotBlank(txtInput.getText())){
						jc.setCurrentDirectory(new File(txtInput.getText()));
					}
					else if(StringUtil.isNotBlank(path)){
						jc.setCurrentDirectory(new File(path));
					}
					int result=jc.showOpenDialog(Frame.getInstance());
					if(result==JFileChooser.APPROVE_OPTION){
						txtInput.setText(jc.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setPreferredSize(new Dimension(62, 20));
			jButton1.setText("导入");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new Thread(){
						public void run(){
							importFile(txtInput.getText());
						}
					}.start();
				}
			});
		}
		return jButton1;
	}

	protected void importSqlFile(String path) {
		try {
			SqlFileReader in = new SqlFileReader(path);
			String sql;
			Connection con = null;
			try {
				con = DbUtil.createConnection();
				con.setAutoCommit(false);
				java.sql.Statement st = con.createStatement();
				while((sql=in.readSql())!=null){
					println(sql);
					if(sql.startsWith("--")){
					}
					else if(sql.equalsIgnoreCase("commit")){
						try {
							con.commit();
						} catch (Exception e) {
							LogUtil.logError(e);
							println("ERROR:"+e.getMessage());
						}
					}
					else{
						if(DbUtil.isSql(sql)){
							try {
								st.executeUpdate(sql);
							} catch (Exception e) {
								LogUtil.logError(e);
								println("ERROR:"+e.getMessage());
							}
						}
					}
				}
				con.commit();
			} catch (Exception e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					LogUtil.logError(e1);
					println("ERROR:"+e1.getMessage());
				}
				LogUtil.logError(e);
				println("ERROR:"+e.getMessage());
			}
			finally{
				if(con!=null){
					try {
						con.close();
					} catch (SQLException e) {
						LogUtil.logError(e);
					}
				}
			}
		} catch (FileNotFoundException e) {
			LogUtil.logError(e);
			println("找不到文件："+path);
		}
	}

	private void executeSql(Statement st, String sql) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(sql)){
			return;
		}
		try {
			if("commit".equalsIgnoreCase(sql)){
				st.getConnection().commit();
			}
			else{
				st.executeUpdate(sql);
			}
		} catch (SQLException e) {
			LogUtil.logError(e);
		}
	}

	protected void importBinFile(SdbFileReader sr) {
		this.println("开始以二进制格式导入...");
		println("导出版本:"+sr.getVersion());
		println("驱动程序:"+sr.getDriver());
		println("JDBC路径:"+sr.getJdbcUrl());
		println("导出日期:"+DateUtil.format(sr.getDate(),"yyyy-MM-dd HH:mm:ss"));
		println("导出用户:"+sr.getUser());
		println("当前用户:"+Frame.getInstance().dao.getDbv().getUserName());
		String sql;
		try {
			while((sql=sr.getSql())!=null){
				sql = sql.trim();
				sql = sql.replaceAll("--.*[\\r|\\n]+", "");
				println(sql);
				Connection con = null;
				try {
					con = DbUtil.createConnection();
					con.setAutoCommit(false);
					PreparedStatement pst = con.prepareStatement(sql);
					java.sql.Statement st = con.createStatement();
					if(StringUtil.isBlank(sql)||sql.startsWith("--")||sql.startsWith("set")){
						continue;
					}
					else if(sql.startsWith("INSERT INTO")){
						String ext = sql.substring(sql.indexOf("VALUES("));
						int cols = (ext.length()-"VALUES(".length())/2;
						byte[] types = sr.getRowDataType(cols);
						Object[] row;
							int cnt = 0;
							while((row=sr.getRowData(types))!=null){
								try {
									for(int i=0;i<row.length;i++){
										if(types[i]==1){
											pst.setTimestamp(i+1, (Timestamp) row[i]);
										}
										else if(types[i]==2){
											if(row[i]!=null){
												FixedInputStream fin = (FixedInputStream) row[i];
												pst.setBinaryStream(i+1, fin.getIn(), fin.getLen());
											}
											pst.setObject(i+1, null);
										}
										pst.setObject(i+1, row[i]);
									}
									pst.execute();
									cnt++;
								} catch (Exception e) {
									LogUtil.logError(e);
									println("ERROR:"+e.getMessage());
								}
								if(cnt%100==0){
									con.commit();
									print(".");
									if(cnt%10000==0){
										println("");
									}
								}
							}
							println("\n"+cnt+"条记录插入成功!");
							con.commit();
							pst.close();
					}
					else{
						st.executeUpdate(sql);
						con.commit();
					}
				} catch (Exception e) {
					LogUtil.logError(e);
					println("ERROR:"+e.getMessage());
				}
				finally{
					if(con!=null){
						try {
							con.close();
						} catch (SQLException e) {
							LogUtil.logError(e);
						}
					}
				}
			}
		} catch (IOException e) {
			LogUtil.logError(e);
			println("文件格式错误!");
		}
		println("导入成功!");
	}
	
	private void println(Object o){
		if(o==null){
			o = "";
		}
		System.out.println(o);
		StringBuffer txt = new StringBuffer(txtConsole.getText());
		txt.append(o).append("\n");
		txtConsole.setText(txt.toString());
		txtConsole.setSelectionStart(txt.length());
	}

	private void print(Object o){
		if(o==null){
			o = "";
		}
		StringBuffer txt = new StringBuffer(txtConsole.getText());
		txt.append(o);
		txtConsole.setText(txt.toString());
		txtConsole.setSelectionStart(txt.length());
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(getTxtInput(), BorderLayout.CENTER);
			jPanel2.add(getJButton(), BorderLayout.EAST);
		}
		return jPanel2;
	}

	private void importFile(String path) {
		SdbFileReader sr = null;
		try {
			sr = new SdbFileReader(path);
		} catch (SdbFileFormatException e1) {
			
		} catch (IOException e1) {
			LogUtil.logError(e1);
			println("找不到文件："+path);
			return;
		}
		ConfigUtil.setString("IMPORT_FILE_PATH", txtInput.getText());
		txtConsole.setText("");
		if(sr!=null){
			importBinFile(sr);
		}
		else{
			importSqlFile(path);
		}
	}
	
	public static void main(String[] args){
		ImportWindow iw = new ImportWindow();
		iw.importFile("C:/1.dmp");
	}

}
