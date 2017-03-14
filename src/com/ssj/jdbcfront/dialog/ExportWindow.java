package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.io.SdbFileWriter;
import com.ssj.jdbcfront.model.DataSet;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.jdbcfront.util.JTableUtil;
import com.ssj.jdbcfront.util.SystemUtil;
import com.ssj.util.DateUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class ExportWindow extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private Frame frame;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel lblRowSel = null;
	private JCheckBox chkDelObject = null;
	private JCheckBox chkCreateObj = null;
	private JCheckBox chkDisableTigger = null;
	private JCheckBox chkDelData = null;
	private JCheckBox chkInsertData = null;
	private JLabel jLabel1 = null;
	private JTextField txtCommitPerRow = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField txtWhere = null;
	private JLabel jLabel4 = null;
	private JTextField txtOutput = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JLabel jLabel5 = null;

	private Collection<DbObject> objs;
	private JPanel jPanel2 = null;
	private JLabel lblStatus = null;

	private JCheckBox chkBinary = null;
	/**
	 * This is the xxx default constructor
	 */
	public ExportWindow(Collection<DbObject> objs) {
		super("导出对象(数据)",true,true,true,true);
		frame = Frame.getInstance();
		this.objs = objs;
		initialize();

		chkDelObject.setSelected("true".equals(ConfigUtil.getString("EXPORT_DEL_OBJ"))||ConfigUtil.getString("EXPORT_DEL_OBJ")==null);
		chkCreateObj.setSelected("true".equals(ConfigUtil.getString("EXPORT_CREATE_OBJ"))||ConfigUtil.getString("EXPORT_CREATE_OBJ")==null);
		chkDisableTigger.setSelected("true".equals(ConfigUtil.getString("EXPORT_DISABLE_TIGGER"))||ConfigUtil.getString("EXPORT_DISABLE_TIGGER")==null);
		chkDelData.setSelected("true".equals(ConfigUtil.getString("EXPORT_DEL_DATA"))||ConfigUtil.getString("EXPORT_DEL_DATA")==null);
		chkInsertData.setSelected("true".equals(ConfigUtil.getString("EXPORT_INSERT_DATA"))||ConfigUtil.getString("EXPORT_INSERT_DATA")==null);
		chkBinary.setSelected("true".equals(ConfigUtil.getString("EXPORT_BINARY")));
		txtCommitPerRow.setText(ConfigUtil.getString("EXPORT_COMMIT_PER_ROWS")==null?"100":ConfigUtil.getString("EXPORT_COMMIT_PER_ROWS"));
		txtWhere.setText(ConfigUtil.getString("EXPORT_WHERE_CASE"));
		txtOutput.setText(ConfigUtil.getString("EXPORT_FILE_PATH"));
		SwingUtilities.invokeLater(new Thread(){
			public void run(){
				loadDbObjects();
				selectRows();
			}
		});
	}

	private void exportDbObject(List<DbObject> list) {
		try {
			File f = new File(txtOutput.getText());
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			PrintWriter out = new PrintWriter(new FileWriter(f));
			out.println("--export by jdbcfront,at "+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
			out.println(frame.dao.getExportPre());
			setStatus("正在导出对象");
			for(DbObject obj:list){//生成对象删除和创建语句
				if(chkDelObject.isSelected()){//删除语句
					out.println("drop "+obj.getType()+" "+obj.getName()+";");
				}
				if(chkCreateObj.isSelected()){//创建语句
					String sql = frame.dao.getObjectSql(obj);
					out.print(sql);
					if("TABLE".equals(obj.getType())||"VIEW".equals(obj.getType())){
						if(!sql.trim().endsWith(";")){
							out.println(";");
						}
					}
					else{
						out.println();
						out.println("/");
					}
					out.println();
				}
				out.flush();
			}
			HashSet<String> set = new HashSet<String>();
//			setStatus("正在导出数据...");
			try {
				for(DbObject obj:list){//生成表的删除和插入语句
					if(!obj.getType().toUpperCase().equals("TABLE")){
						continue;
					}
					if(chkDisableTigger.isSelected()){//禁用触发器
						out.println(frame.dao.getTableDisableTigger(obj.getName())+";");
					}
					if(chkDelData.isSelected()){//删除数据
						out.println("delete from "+obj.getName()+";");
					}
					if(chkInsertData.isSelected()){
						setStatus("正在导出表["+obj.getName()+"]的数据...");
						DataSet ds = null;
						int page = 1;
						String sql = "select * from "+obj.getName();
						if(txtWhere.getText().trim().length()>0){
							sql += " where "+txtWhere.getText();
						}
						do{
							ds = frame.dao.getData(sql, 10000, page++);
							List<String> head = ds.getHeader();
							List<String> headType = ds.getHeaderType();
							for(Integer row:ds.getData().keySet()){
								Object[] r = ds.getData().get(row);
								StringBuffer pre = new StringBuffer("insert into ").append(obj.getName()).append("(");
								StringBuffer ext = new StringBuffer(") \nvalues(");
								for(int i=0;i<head.size();i++){
									Object o = r[i];
									String val = frame.dao.getExportSqlValue(o);
									if(val!=null){
										pre.append(head.get(i));
										ext.append(val);
										if(i<head.size()-1){
											pre.append(",");
											ext.append(",");
										}
									}
									else{
										set.add(obj.getName()+"."+head.get(i)+"["+headType.get(i)+"]");
									}
								}
								out.print(pre.toString());
								out.print(ext.toString());
								out.println(");");
								if(row%Integer.parseInt(txtCommitPerRow.getText())==0){
									out.println("commit;");
								}
							}
							out.flush();
						}while(!ds.getData().keySet().isEmpty());
					}
					if(chkDisableTigger.isSelected()){//启用触发器
						out.println(frame.dao.getTableEnableTigger(obj.getName())+";");
					}
				}
			} catch (Exception e1) {
				LogUtil.logError(e1);
			}
			out.println(frame.dao.getExportExt());
			out.flush();
			out.close();
			setStatus("导出完成");
			if(set.size()>0){
				StringBuffer buff = new StringBuffer();
				for(String t:set){
					buff.append(t).append("\n");
				}
				Frame.showMsg("有以下未知数据类型未被导出！\n"+buff.toString());
			}
			if(Frame.confirm("数据库对象已导出，是否现在打开文件进行查看?", "是否打开", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
				SystemUtil.openFile(f.getAbsolutePath());
			}
		} catch (IOException e1) {
			LogUtil.logError(e1);
		}
	}
	
	private void exportDbObjectToBin(List<DbObject> list) {
		try {
			File f = new File(txtOutput.getText());
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			SdbFileWriter out = new SdbFileWriter(f);
			out.writeHead("0.1", frame.dao.getDbv().getDbType().getDriverClass(), frame.dao.getDbv().getJdbcUrl(), frame.dao.getDbv().getUserName());
			String sql = frame.dao.getExportPre();
			out.writeSqls(sql,";");
			setStatus("正在导出对象");
			for(DbObject obj:list){//生成对象删除和创建语句
				if(chkDelObject.isSelected()){//删除语句
					out.writeSql("drop "+obj.getType()+" "+obj.getName());
				}
				if(chkCreateObj.isSelected()){//创建语句
					sql = frame.dao.getObjectSql(obj);
					if("TABLE".equals(obj.getType())){
						out.writeSqls(sql,";");
						if(chkDisableTigger.isSelected()){//禁用触发器
							out.writeSql(frame.dao.getTableDisableTigger(obj.getName()));
						}
						if(chkDelData.isSelected()){//删除数据
							out.writeSql("delete from "+obj.getName());
						}
						DataSet ds = null;
						int page = 1;
						sql = "select * from "+obj.getName();
						if(txtWhere.getText().trim().length()>0){
							sql += " where "+txtWhere.getText();
						}
						byte[] htype=null;
						do{
							try {
								ds = frame.dao.getData(sql, 10000, page++);
							} catch (AppException e1) {
								Frame.showMsg(e1.getMessage());
							}
							List<String> head = ds.getHeader();
							List<String> headType = ds.getHeaderType();
							if(htype==null){//第一页时写入列类型
								htype = new byte[head.size()];
								StringBuffer pre = new StringBuffer("INSERT INTO ").append(obj.getName()).append("(");
								StringBuffer ext = new StringBuffer(") VALUES(");
								for(int i=0;i<head.size();i++){
									htype[i] = SdbFileWriter.getColType(headType.get(i));
									pre.append(head.get(i));
									ext.append("?");
									if(i<head.size()-1){
										pre.append(",");
										ext.append(",");
									}
								}
								out.writeSql(pre.toString()+ext.toString()+")");
								out.writeRowDataType(htype);
							}
							for(Integer row:ds.getData().keySet()){
								Object[] r = ds.getData().get(row);
								for(int i=0;i<htype.length;i++){
									Object o = r[i];
									if(o!=null){
										if(o instanceof Blob){
											Blob bl = (Blob) o;
											try {
												r[i] = bl.getBinaryStream();
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										else if(o instanceof BigDecimal||o instanceof Integer||o instanceof Long||o instanceof Float||o instanceof Double||o instanceof Short){
											r[i] = o.toString();
										}
									}
								}
								out.writeRowData(r);
							}
						}while(!ds.getData().keySet().isEmpty());
						out.writeTableDataEnd();
						if(chkDisableTigger.isSelected()){//启用触发器
							out.writeSql(frame.dao.getTableEnableTigger(obj.getName()));
						}
					}
					else{
						out.writeSql(sql);
					}
				}
				if("TABLE".equals(obj.getType())){
					if(chkDisableTigger.isSelected()){//禁用触发器
						out.writeSql(frame.dao.getTableDisableTigger(obj.getName()));
					}
					if(chkDelData.isSelected()){//删除数据
						out.writeSql("delete from "+obj.getName());
					}
					if(chkInsertData.isSelected()){
						DataSet ds = null;
						int page = 1;
						sql = "select * from "+obj.getName();
						if(txtWhere.getText().trim().length()>0){
							sql += " where "+txtWhere.getText();
						}
						byte[] htype=null;
						do{
							try {
								ds = frame.dao.getData(sql, 10000, page++);
							} catch (AppException e1) {
								Frame.showMsg(e1.getMessage());
							}
							List<String> head = ds.getHeader();
							List<String> headType = ds.getHeaderType();
							if(htype==null){//第一页时写入列类型
								htype = new byte[head.size()];
								StringBuffer pre = new StringBuffer("INSERT INTO ").append(obj.getName()).append("(");
								StringBuffer ext = new StringBuffer(") VALUES(");
								for(int i=0;i<head.size();i++){
									htype[i] = SdbFileWriter.getColType(headType.get(i));
									pre.append(head.get(i));
									ext.append("?");
									if(i<head.size()-1){
										pre.append(",");
										ext.append(",");
									}
								}
								out.writeSql(pre.toString()+ext.toString()+")");
								out.writeRowDataType(htype);
							}
							for(Integer row:ds.getData().keySet()){
								Object[] r = ds.getData().get(row);
								for(int i=0;i<htype.length;i++){
									Object o = r[i];
									if(o!=null){
										if(o instanceof Blob){
											Blob bl = (Blob) o;
											try {
												r[i] = bl.getBinaryStream();
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										else if(o instanceof BigDecimal||o instanceof Integer||o instanceof Long||o instanceof Float||o instanceof Double||o instanceof Short){
											r[i] = o.toString();
										}
									}
								}
								out.writeRowData(r);
							}
						}while(!ds.getData().keySet().isEmpty());
						out.writeTableDataEnd();
					}
					if(chkDisableTigger.isSelected()){//启用触发器
						out.writeSql(frame.dao.getTableEnableTigger(obj.getName()));
					}
				}
			}
			out.writeSql(frame.dao.getExportExt());
			out.writeSql(null);
			out.close();
			setStatus("导出完成");
			if(Frame.confirm("数据库对象已导出，是否现在查看?", "是否查看", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
				SystemUtil.openFolder(f.getParentFile().getAbsolutePath());
			}
		} catch (IOException e1) {
			LogUtil.logError(e1);
		}
	}

	private void selectRows() {
		for(int i=0;i<jTable.getRowCount();i++){
			if(objs.contains(jTable.getValueAt(i, 1))){
				jTable.addRowSelectionInterval(i, i);
				Rectangle rect = jTable.getCellRect(i,0,true);
//				rect.y = Math.max(10,rect.y-jScrollPane.getHeight()/100);
				jTable.scrollRectToVisible(rect);
			}
		}
	}

	private void loadDbObjects() {
		setStatus("正在加载数据库对象列表");
		List<String> list = frame.dao.getObjectTypeList();
		DefaultTableModel model = (DefaultTableModel) jTable.getModel();
		int row = 1;
		for(String type:list){
			List<DbObject> dbos = frame.dao.getObjectList(type);
			if(dbos!=null){
				for(DbObject obj:dbos){
					model.addRow(new Object[]{row++,obj,obj.getName(),obj.getComment(),type});
				}
			}
		}
		setStatus("数据库对象列表加载完毕，共"+(row-1)+"个对象.");
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(539, 429);
		this.setContentPane(getJContentPane());
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
			jContentPane.add(getJPanel(), BorderLayout.SOUTH);
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
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(new DefaultTableModel(new Object[0][0],new Object[]{"","对象","名称","注释","类型"}){
				private static final long serialVersionUID = 7143505854308433964L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				
			});
			jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e){
					lblRowSel.setText("共选中"+jTable.getSelectedRowCount()+"个对象");
				}
			});
			JTableUtil.setTableHeaderHeight(jTable,22);
			DefaultTableCellRenderer rd = (DefaultTableCellRenderer) jTable.getDefaultRenderer(Integer.class);
			rd.setHorizontalAlignment(SwingConstants.CENTER);//设置序号列居中对齐
			jTable.getColumn("").setCellRenderer(rd);
			jTable.getColumn("").setMaxWidth(30);
			jTable.getColumn("").setPreferredWidth(25);
			jTable.getColumn("对象").setMaxWidth(0);
			jTable.getColumn("对象").setMinWidth(0);
			jTable.getColumn("对象").setPreferredWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0); 
			jTable.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
			jTable.getColumn("类型").setMaxWidth(85);
			jTable.getColumn("类型").setPreferredWidth(85);
		}
		return jTable;
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
			jPanel.add(getJPanel1(), BorderLayout.CENTER);
			jPanel.add(getJPanel2(), BorderLayout.SOUTH);
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
			jLabel5 = new JLabel();
			jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel5.setSize(new Dimension(19, 19));
			jLabel5.setLocation(new Point(248, 48));
			jLabel5.setText("每");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(28, 95, 80, 19));
			jLabel4.setText("文件输出路径");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(37, 70, 69, 18));
			jLabel3.setText("Where子句");
			jLabel2 = new JLabel();
			jLabel2.setText("记录");
			jLabel2.setLocation(new Point(327, 48));
			jLabel2.setSize(new Dimension(38, 18));
			jLabel1 = new JLabel();
			jLabel1.setText("提交频率");
			jLabel1.setLocation(new Point(175, 48));
			jLabel1.setSize(new Dimension(60, 18));
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setPreferredSize(new Dimension(38, 120));
			jPanel1.add(getChkDelObject(), null);
			jPanel1.add(getChkCreateObj(), null);
			jPanel1.add(getChkDisableTigger(), null);
			jPanel1.add(getChkDelData(), null);
			jPanel1.add(getChkInsertData(), null);
			jPanel1.add(jLabel1, null);
			jPanel1.add(getTxtCommitPerRow(), null);
			jPanel1.add(jLabel2, null);
			jPanel1.add(jLabel3, null);
			jPanel1.add(getTxtWhere(), null);
			jPanel1.add(jLabel4, null);
			jPanel1.add(getTxtOutput(), null);
			jPanel1.add(getJButton(), null);
			jPanel1.add(getJButton1(), null);
			jPanel1.add(jLabel5, null);
			jPanel1.add(getChkBinary(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes chkDelObject	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkDelObject() {
		if (chkDelObject == null) {
			chkDelObject = new JCheckBox();
			chkDelObject.setText("删除对象");
			chkDelObject.setSize(new Dimension(100, 21));
			chkDelObject.setLocation(new Point(15, 5));
		}
		return chkDelObject;
	}

	/**
	 * This method initializes chkCreateObj	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkCreateObj() {
		if (chkCreateObj == null) {
			chkCreateObj = new JCheckBox();
			chkCreateObj.setText("创建对象");
			chkCreateObj.setLocation(new Point(15, 25));
			chkCreateObj.setSize(new Dimension(93, 21));
			chkCreateObj.setSelected(true);
		}
		return chkCreateObj;
	}

	/**
	 * This method initializes chkDisableTigger	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkDisableTigger() {
		if (chkDisableTigger == null) {
			chkDisableTigger = new JCheckBox();
			chkDisableTigger.setText("禁用触发器(表)");
			chkDisableTigger.setLocation(new Point(15, 45));
			chkDisableTigger.setSize(new Dimension(123, 21));
			chkDisableTigger.setSelected(true);
		}
		return chkDisableTigger;
	}

	/**
	 * This method initializes chkDelData	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkDelData() {
		if (chkDelData == null) {
			chkDelData = new JCheckBox();
			chkDelData.setText("删除数据(表)");
			chkDelData.setLocation(new Point(170, 5));
			chkDelData.setSize(new Dimension(113, 23));
			chkDelData.setSelected(true);
		}
		return chkDelData;
	}

	/**
	 * This method initializes chkInsertData	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkInsertData() {
		if (chkInsertData == null) {
			chkInsertData = new JCheckBox();
			chkInsertData.setText("生成数据(表)");
			chkInsertData.setLocation(new Point(170, 25));
			chkInsertData.setSize(new Dimension(115, 23));
			chkInsertData.setPreferredSize(new Dimension(98, 24));
			chkInsertData.setSelected(true);
		}
		return chkInsertData;
	}

	/**
	 * This method initializes txtCommitPerRow	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtCommitPerRow() {
		if (txtCommitPerRow == null) {
			txtCommitPerRow = new JTextField();
			txtCommitPerRow.setSize(new Dimension(56, 19));
			txtCommitPerRow.setText("100");
			txtCommitPerRow.setLocation(new Point(268, 48));
		}
		return txtCommitPerRow;
	}

	/**
	 * This method initializes txtWhere	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtWhere() {
		if (txtWhere == null) {
			txtWhere = new JTextField();
			txtWhere.setSize(new Dimension(410, 19));
			txtWhere.setLocation(new Point(110, 70));
		}
		return txtWhere;
	}

	/**
	 * This method initializes txtOutput	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtOutput() {
		if (txtOutput == null) {
			txtOutput = new JTextField();
			txtOutput.setBounds(new Rectangle(110, 94, 311, 19));
		}
		return txtOutput;
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
			jButton.setLocation(new Point(421, 94));
			jButton.setPreferredSize(new Dimension(43, 26));
			jButton.setSize(new Dimension(24, 18));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser jc=new JFileChooser(); //文件选择器
					jc.setDialogTitle("请选择文件输出路径");
					jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					String path = ConfigUtil.getString("EXPORT_FILE_PATH");
					if(StringUtil.isNotBlank(txtOutput.getText())){
						jc.setCurrentDirectory(new File(txtOutput.getText()));
					}
					else if(StringUtil.isNotBlank(path)){
						jc.setCurrentDirectory(new File(path));
					}
					int result=jc.showOpenDialog(Frame.getInstance());
					if(result==JFileChooser.APPROVE_OPTION){
						txtOutput.setText(jc.getSelectedFile().getAbsolutePath());
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
			jButton1.setBounds(new Rectangle(456, 93, 62, 20));
			jButton1.setText("导出");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(StringUtil.isBlank(txtOutput.getText())){
						Frame.showMsg("请选择文件输出路径");
						return;
					}
					if(jTable.getSelectedRowCount()==0){
						Frame.showMsg("请选择要导出的对象");
						return;
					}
					ConfigUtil.setString("EXPORT_DEL_OBJ", ""+chkDelObject.isSelected());
					ConfigUtil.setString("EXPORT_CREATE_OBJ", ""+chkCreateObj.isSelected());
					ConfigUtil.setString("EXPORT_DISABLE_TIGGER", ""+chkDisableTigger.isSelected());
					ConfigUtil.setString("EXPORT_DEL_DATA", ""+chkDelData.isSelected());
					ConfigUtil.setString("EXPORT_INSERT_DATA", ""+chkInsertData.isSelected());
					ConfigUtil.setString("EXPORT_BINARY", ""+chkBinary.isSelected());
					ConfigUtil.setString("EXPORT_COMMIT_PER_ROWS", txtCommitPerRow.getText());
					ConfigUtil.setString("EXPORT_WHERE_CASE", txtWhere.getText());
					ConfigUtil.setString("EXPORT_FILE_PATH", txtOutput.getText());
					setStatus("正在分析数据...");
					int[] rows = jTable.getSelectedRows();
					final List<DbObject> list = new ArrayList<DbObject>();
					for(int i=0;i<rows.length;i++){
						list.add((DbObject) jTable.getValueAt(rows[i], 1));
					}
					setStatus("开始导出数据");
					new Thread(){
						public void run(){
							if(chkBinary.isSelected()){
								exportDbObjectToBin(list);
							}
							else{
								exportDbObject(list);
							}
						}
					}.start();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			lblRowSel = new JLabel(){
	            public void paint(Graphics g) {
	                super.paint(g);
	                int w = getWidth();
	                int h = getHeight();
	                g.setColor(Color.white);
	                g.drawLine(w - 3, 0, w - 3, h - 1);
	                g.setColor(new Color(128, 128, 128));
	                g.drawLine(w - 1, 0, w - 1, h - 1);
	            }

	            public Insets getInsets() {
	                return new Insets(0, 0, 0, 5);
	            }
	         };
			lblRowSel.setText(" ");
			lblRowSel.setPreferredSize(new Dimension(100, 18));
			lblStatus = new JLabel();
			lblStatus.setText(" ");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
			jPanel2.add(lblRowSel, BorderLayout.WEST);
			jPanel2.add(lblStatus, BorderLayout.CENTER);
		}
		return jPanel2;
	}
	
	private void setStatus(String msg){
		lblStatus.setText(" "+msg);
	}

	/**
	 * This method initializes chkBinary	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChkBinary() {
		if (chkBinary == null) {
			chkBinary = new JCheckBox();
			chkBinary.setText("二进制文件");
			chkBinary.setLocation(new Point(325, 5));
			chkBinary.setSize(new Dimension(111, 21));
		}
		return chkBinary;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
