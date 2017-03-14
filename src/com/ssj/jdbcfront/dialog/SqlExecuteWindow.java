package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.dao.JdbcType;
import com.ssj.jdbcfront.dao.LOBData;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.swing.DefaultTableEditor;
import com.ssj.jdbcfront.util.JTableUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class SqlExecuteWindow extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private Frame frame;
	private Connection con = null;
	private ResultSet rs = null;
	private Statement st = null;
	private List<String> sqls = new ArrayList<String>();  //  @jve:decl-index=0:
	/**
	 * 是否翻页操作
	 */
	private boolean isPageTurn;
	/**
	 * 数据是否被锁定
	 */
	private boolean isDataLock;
	/**
	 * 编辑数据时更改的数据位置及值
	 */
	private HashMap<String,Object> changedValue = new HashMap<String,Object>();
	/**
	 * 编辑数据时删除的行序号
	 */
	private List<Integer> delRows = new ArrayList<Integer>();  //  @jve:decl-index=0:
	/**
	 * 执行的SQL
	 */
	public String sql;
	/**
	 * 每页行数及当前页
	 */
	private int rows,curPage;
	/**
	 * 查询后状态的信息
	 */
	private String searchMsg;
	/**
	 * 状态栏信息刷新标记
	 */
	private boolean flashStatus;
	/**
	 * 打开时可指定标题，指定后标题不会再更改
	 */
	private String title;
	
	private JSplitPane jSplitPane = null;
	private RSyntaxTextArea txtSql = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JToolBar jToolBar = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JButton btnSearch = null;
	private JButton btnAdd = null;
	private JButton btnDel = null;
	private JButton btnSave = null;
	private JButton btnRollback = null;
	private JButton btnPrev = null;
	private JButton btnNext = null;
	private JButton btnGenCode = null;
	private JScrollPane spUpdate;
	private JLabel lblTime = null;
	private JLabel lblStatus = null;
	private SqlExecuteWindow _this;
	private DefaultTableEditor curEditor;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JToggleButton btnEditLock = null;
	private JScrollPane spQuery = null;
	private JButton jButton = null;
	public SqlExecuteWindow() {
		super("新建SQL窗口",true,true,true,true);
		initialize();
		frame = Frame.getInstance();
		_this = this;
		Dimension ds = Frame.getInstance().getJDesktopPane().getSize();
		setSize((int)(ds.getWidth()*5/7),514);
	}

	public SqlExecuteWindow(String sql) {
		this();
		if(sql==null)
			sql = "";
		txtSql.setText(sql);
		txtSql.setSelectionStart(0);
		txtSql.setSelectionEnd(0);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(new Dimension(707, 677));
		this.setFrameIcon(new ImageIcon(getClass().getResource("/com/ssj/jdbcfront/img/page_text.gif")));
		this.setContentPane(getJPanel2());
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {   
			public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
				try {
					flashStatus = false;
					if(con!=null){
						con.close();
					}
				} catch (SQLException e1) {
					LogUtil.logError(e1);
				}
			}
		});
	}
	
	public void setSqlTitle(String title){
		this.title = title;
		this.setTitle(title);
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  上午08:52:08
	* 功能描述: 执行SQL语句,对SQL语句进行自动分析
	* 方法的参数和返回值: 
	*/
	public void executeSql(){
		executeSql(false);
	}
	public void executeSql(boolean editQuery){
		String sql = getExecuteSql();
		if(sqls.size()>0&&!sql.equals(sqls.get(0))){
			sqls.add(0,sql);
		}
		sql = sql.trim().toUpperCase();
		if(sql.startsWith("SELECT")){
			if(!spQuery.isAncestorOf(txtSql)){
				spQuery.setViewportView(txtSql);
			}
			executeSqlQuery(editQuery);
		}
		else{
			if(!spUpdate.isAncestorOf(txtSql)){
				spUpdate.setViewportView(txtSql);
			}
			executeSqlUpdate();
		}
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  上午08:55:50
	* 功能描述: 执行数据库更新或DDM操作SQL语句
	* 方法的参数和返回值: 
	*/
	private void executeSqlUpdate() {
		((CardLayout)jPanel3.getLayout()).first(jPanel3);
		final String tmpSql = getExecuteSql();
		setTitle("执行窗口:"+tmpSql);
		new Thread(){
			public void run(){
				long start = System.currentTimeMillis();
				try {
					Statement st = getStatement();
					if(rs!=null){
						rs.close();
						rs = null;
						curEditor = null;
					}
					LogUtil.logSql(tmpSql);
					final int cnt = st.executeUpdate(tmpSql);
					final float lastTimeUse = (float)(System.currentTimeMillis()-start)/1000;
					con.commit();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setStatus("执行成功！"+cnt+"行被影响,用时"+lastTimeUse+"秒.");
							Pattern pat = Pattern.compile("CREATE\\s+(?:OR\\s+REPLACE\\s+)?+(\\S+)\\s+[\\s|\\S]*",Pattern.MULTILINE);
							Matcher mat = pat.matcher(tmpSql.toUpperCase());
							if(mat.matches()){
								frame.refreshDbType(mat.group(1));
							}
						}
					});
				} catch (Throwable e) {
					try {
						con.rollback();
					} catch (SQLException e1) {
						LogUtil.logError(e1);
					}
					LogUtil.logError(e);
					setStatus("执行失败！错误信息："+e.getMessage());
				}
			}
		}.start();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  上午08:52:17
	* 功能描述: 执行SQL查询语句
	* 方法的参数和返回值: 
	*/
	private void executeSqlQuery() {
		executeSqlQuery(false);
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  上午08:27:59
	* 功能描述: 执行一个SQL语句的查询，并试图对查询结果进行编辑锁定
	* 方法的参数和返回值: 
	* @param edit
	*/
	private void executeSqlQuery(final boolean edit) {
		new Thread(){
			public void run(){
				try {
					String tmpSql = getExecuteSql();
					if(StringUtil.isBlank(tmpSql)){
						return;
					}
					((CardLayout)jPanel3.getLayout()).last(jPanel3);
					btnSearch.setEnabled(false);
					btnGenCode.setEnabled(false);
					setStatus("正在查询数据",true);
					if(title==null){
						setTitle("查询窗口:"+tmpSql);
					}
					resetButtonStat();
					getBtnEditLock().setEnabled(false);
					final long start = System.currentTimeMillis();
					Statement st = getStatement();
					st.setFetchSize(50);
					if(rs!=null){
						rs.close();
						rs = null;
						curEditor = null;
					}
					LogUtil.logSql(tmpSql);
					rs = st.executeQuery(tmpSql);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								TableColumnModel tcm = getJTable().getColumnModel();
								int ccnt = rs.getMetaData().getColumnCount();
								DefaultTableModel model = (DefaultTableModel) jTable.getModel();
								model.setColumnCount(0);
								JTableUtil.setTableHeaderHeight(jTable,22);
								model.addColumn("");
								for(int i=0;i<ccnt;i++){
									model.addColumn(rs.getMetaData().getColumnName(i+1));
								}
								jTable.repaint();
								tcm.getColumn(0).setPreferredWidth(25);
								double tabHeight = getJSplitPane().getHeight()-getJSplitPane().getDividerLocation()-70;
								rows = (int)(tabHeight/getJTable().getRowHeight())-1;
								rows = (int) (Math.round((1.0d*rows)/10)*10);
								if(rows<10)rows=10;
								curPage = 1;
								float lastTimeUse = (float)(System.currentTimeMillis()-start)/1000;
								isPageTurn = false;
								searchData("用时"+lastTimeUse+"秒.");
								JTableUtil.fixTableColumnWidth(getJTable());
							} catch (SQLException e) {
								LogUtil.logError(e);
								setStatus("错误:"+e.getClass().getName()+"["+e.getMessage()+"]");
							}
						}
					});
					con.commit();
				} catch (Throwable e) {
					LogUtil.logError(e);
					setStatus("错误:"+e.getClass().getName()+"["+e.getMessage()+"]");
				}
				if(edit){
					toggleEdit(true);
				}
				getBtnEditLock().setEnabled(true);
				btnSearch.setEnabled(true);
				btnGenCode.setEnabled(true);
			}
		}.start();
	}
	
	private void resetButtonStat() {
		isDataLock = false;
		getBtnEditLock().setSelected(isDataLock);
		getBtnPrev().setEnabled(false);
		getBtnNext().setEnabled(false);
		getBtnAdd().setEnabled(false);
		getBtnDel().setEnabled(false);
		getBtnSave().setEnabled(false);
		getBtnRollback().setEnabled(false);
	}
	
	private Connection getCon(){
		if(con==null){
			try {
				con = DbUtil.createConnection();
				con.setAutoCommit(false);
			} catch (Exception e) {
				
				LogUtil.logError(e);
			}
		}
		return con;
	}
	
	private Statement getStatement(){
		if(st==null){
			Connection con = getCon();
			try {
				st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,   
                        ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return st;
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-29  下午09:34:56
	* 功能描述: 表格内单元格变化后，将值写入缓存，在COMMIT时进行数据更新
	* 方法的参数和返回值: 
	* @param row
	* @param col
	* @param value
	*/
	public void addChangedValue(int row,int col,Object value){
		this.changedValue.put(""+row+"_"+col, value);
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setDividerLocation(45);
			jSplitPane.setName("jSplitPane");
			jSplitPane.setTopComponent(getSpQuery());
			jSplitPane.setBottomComponent(getJPanel());
		}
		return jSplitPane;
	}

	private JScrollPane getSpUpdate() {
		if (spUpdate == null) {
			spUpdate = new JScrollPane();
			spUpdate.setName("jScrollPane2");
			spUpdate.setViewportView(getTxtSql());
		}
		return spUpdate;
	}
	/**
	 * This method initializes txtSql	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtSql() {
		if (txtSql == null) {
			txtSql = new RSyntaxTextArea();
			txtSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
			txtSql.addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent arg0) {
					if(getJTable().getRowCount()>0)
						setStatus(searchMsg);
				}

				public void mouseEntered(MouseEvent arg0) {
				}

				public void mouseExited(MouseEvent arg0) {
				}

				public void mousePressed(MouseEvent arg0) {
				}

				public void mouseReleased(MouseEvent arg0) {
				}});
			txtSql.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_F8){
						executeSql();
					}
				}
				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
				}});
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtSql.requestFocusInWindow();
			}
		});
		return txtSql;
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
			jPanel.add(getJToolBar(), BorderLayout.NORTH);
			jPanel.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jPanel1.add(getLblTime(), BorderLayout.WEST);
			jPanel1.add(getLblStatus(), BorderLayout.CENTER);
			jPanel1.add(getJButton(), BorderLayout.EAST);
		}
		return jPanel1;
	}

	private JLabel getLblTime() {
		if(lblTime==null){
			lblTime = new JLabel();
			lblTime.setUI(new BasicLabelUI(){
				 public void paint(Graphics g, JComponent c) {
				  // TODO Auto-generated method stub
				  g.setColor(Color.gray);
				  int y = (int) c.getSize().getHeight();
				  int x = (int) c.getSize().getWidth();
				  g.drawLine(x-1,0, x-1, y-1);
				  super.paint(g, c);
				 }
				});
			lblTime.setPreferredSize(new Dimension(45, 18));
			lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTime;
	}

	private JLabel getLblStatus() {
		if(lblStatus==null){
			lblStatus = new JLabel();
			lblStatus.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
			lblStatus.setText("按F8可执行SQL语句");
		}
		return lblStatus;
	}

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			jToolBar = new JToolBar();
			jToolBar.setLayout(gridLayout1);
			jToolBar.setPreferredSize(new Dimension(51, 26));
			jToolBar.add(getBtnSearch());
//			jToolBar.add(getBtnStop());
			jToolBar.add(getBtnPrev());
			jToolBar.add(getBtnNext());
			jToolBar.add(getBtnAdd());
			jToolBar.add(getBtnDel());
			jToolBar.add(getBtnEditLock());
			jToolBar.add(getBtnSave());
			jToolBar.add(getBtnRollback());
			jToolBar.add(getCodeProduce());
		}
		return jToolBar;
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
			jTable = new JTable(){

				private static final long serialVersionUID = 1L;

				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					int jdbcType = Types.VARCHAR;
					try {
						jdbcType = rs.getMetaData().getColumnType(column);
					} catch (SQLException e) {
						LogUtil.logError(e);
					}
					if(column>0){
						try {
							setStatus(rs.getMetaData().getColumnTypeName(column));
						} catch (SQLException e1) {
							LogUtil.logError(e1);
						}
					}
					DefaultTableEditor editor =DefaultTableEditor.getEditor(_this,jdbcType );
					curEditor = editor;
					return editor;
				}

				@Override
				public TableCellRenderer getCellRenderer(int row, int column) {
//					if(this.getValueAt(row, column) instanceof Boolean){
//						return this.getDefaultRenderer(Boolean.class);
//					}
					return new DefaultTableCellRenderer(){
						private static final long serialVersionUID = 1L;

						@Override
						public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
							super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
							int align = SwingConstants.LEFT;
							Object val = table.getValueAt(row, column);
							if(val instanceof Date)
								align=SwingConstants.CENTER;
							else if(val instanceof Integer||val instanceof Long||val instanceof Float||val instanceof Double||val instanceof BigInteger||val instanceof BigDecimal)
								align=SwingConstants.RIGHT;
							if(column==0)align=SwingConstants.CENTER;
							this.setHorizontalAlignment(align);
							return this;
						}

					};
				}
			};
			jTable.setModel(new DefaultTableModel() {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					if(column==0){
						return false;
					}
					int type = 0;
					if (rs != null) {
						try {
							type = rs.getMetaData().getColumnType(column);
						} catch (SQLException e) {
							LogUtil.logError(e);
						}
					}
					return isDataLock
							&& DaoAccess.isKnowJdbcType(type);
				}
			});
			jTable.getModel().addTableModelListener(new TableModelListener(){
				public void tableChanged(TableModelEvent e) {
					if(rs!=null&&isDataLock){
						changeResultTable();
					}
				}
			});
			jTable.setVisible(true);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return jTable;
	}

	public void changeResultTable() {
		getBtnSave().setEnabled(true);
		getBtnRollback().setEnabled(true);
	}

	public void setStatus(final String status){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getLblStatus().setText(status);
				flashStatus = false;
			}
		});
	}

	public void setStatus(String status,boolean flash){
		setStatus(status);
		flashStatus = true;
		SwingUtilities.invokeLater(new Thread(){
			public void run(){
				long t = System.currentTimeMillis();
				NumberFormat nf = new DecimalFormat("00");
				while(true){
					try {
						long time = Math.round(((double)(System.currentTimeMillis()-t))/1000);
						lblTime.setText(nf.format(time/60)+":"+nf.format(time));
						Thread.sleep(500);
						if(!flashStatus){
							return;
						}
						String st = getLblStatus().getText();
						if(st.length()>6&&"......".equals(st.substring(st.length()-6))){
							st = st.substring(0,st.length()-6);
						}
						else{
							st +=".";
						}
						getLblStatus().setText(st);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						LogUtil.logError(e);
					}
				}
			}
		});
	}
	
	/**
	 * This method initializes btnSearch	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton();
			btnSearch.setText("查询");
			btnSearch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(getBtnSave().isEnabled()){
						int type = Frame.confirm("是否将数据保存至数据库？","确认",JOptionPane.YES_NO_CANCEL_OPTION);
						try {
							switch(type){
							case JOptionPane.YES_OPTION:
								commitData();
								break;
							case JOptionPane.NO_OPTION:
								con.rollback();
								executeSqlQuery();
								break;
							case JOptionPane.CANCEL_OPTION:
								return;
							}
						} catch (SQLException e1) {
							Frame.showMsg(e1.getMessage());
						}
					}
					executeSqlQuery();
				}
			});
		}
		return btnSearch;
	}

	/**
	 * This method initializes btnAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton();
			btnAdd.setText("+");
			btnAdd.setEnabled(false);
			btnAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Object[] row = new Object[getJTable().getColumnCount()];
//					row[0] = getJTable().getRowCount()+1;
					((DefaultTableModel) jTable.getModel()).addRow(row);
				}
			});
		}
		return btnAdd;
	}

	/**
	 * This method initializes btnDel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JButton();
			btnDel.setText("－");
			btnDel.setEnabled(false);
			btnDel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(curEditor!=null)
						curEditor.stopCellEditing();
					int[] rows = getJTable().getSelectedRows();
					for(int i =rows.length-1;i>=0;i--){
						delRows.add((Integer)getJTable().getValueAt(rows[i], 0));
						((DefaultTableModel) jTable.getModel()).removeRow(rows[i]);
					}
					
				}
			});
		}
		return btnDel;
	}

	/**
	 * This method initializes btnSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setText("保存");
			btnSave.setEnabled(false);
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					commitData();
					executeSqlQuery();
				}
			});
		}
		return btnSave;
	}

	/**
	 * This method initializes btnRollback	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRollback() {
		if (btnRollback == null) {
			btnRollback = new JButton();
			btnRollback.setText("回滚");
			btnRollback.setEnabled(false);
			btnRollback.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(curEditor!=null){
						curEditor.stopCellEditing();
						curEditor = null;
					}
					try {
						con.rollback();
						executeSqlQuery();
					} catch (SQLException e1) {
						Frame.showMsg(e1.getMessage());
					}
				}
			});
		}
		return btnRollback;
	}

	/**
	 * This method initializes btnShowAll	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnPrev() {
		if (btnPrev == null) {
			btnPrev = new JButton();
			btnPrev.setText("上一页");
			btnPrev.setEnabled(false);
			btnPrev.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(curPage>1){
						curPage--;
						searchData();
					}
				}
			});
		}
		return btnPrev;
	}

	/**
	 * This method initializes btnShowAll	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton();
			btnNext.setText("下一页");
			btnNext.setEnabled(false);
			btnNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					curPage++;
					searchData();
				}
			});
		}
		return btnNext;
	}

	/**
	 * This method initializes btnEditLock	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getBtnEditLock() {
		if (btnEditLock == null) {
			btnEditLock = new JToggleButton();
			btnEditLock.setText("编辑");
			btnEditLock.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleEdit(!isDataLock);
				}
			});
		}
		return btnEditLock;
	}

	/**
	 * This method initializes codeProduce	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCodeProduce() {
		if (btnGenCode == null) {
			btnGenCode = new JButton();
			btnGenCode.setText("生成代码");
			btnGenCode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					frame.genCode(sql);
				}
			});
		}
		return btnGenCode;
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-28  上午12:40:33
	* 功能描述: 切换可编辑状态
	* 方法的参数和返回值: 
	*/
	private void toggleEdit(boolean canEdit){
		try {
			isDataLock = canEdit;
			getBtnEditLock().setSelected(isDataLock);
			if(isDataLock&&rs.getConcurrency()!=ResultSet.CONCUR_UPDATABLE){
				Frame.showMsg("该结果不能编辑!");
				getBtnEditLock().setSelected(false);
				isDataLock = getBtnEditLock().isSelected();
				return;
			}
			if(isDataLock){
				getBtnAdd().setEnabled(true);
				getBtnDel().setEnabled(true);
			}
			else{
				getBtnAdd().setEnabled(false);
				getBtnDel().setEnabled(false);
				getBtnSave().setEnabled(false);
				getBtnRollback().setEnabled(false);
			}
		} catch (SQLException e1) {
			
			LogUtil.logError(e1);
		}
	}

	private void commitData() {
		if(curEditor!=null){
			curEditor.stopCellEditing();
			curEditor = null;
		}
		try {
			rs.close();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LogUtil.logError(e);
		}
		//先处理更新的值
		for(String key:changedValue.keySet()){
			String[] tmp = key.split("_");
			int row = Integer.parseInt(tmp[0]);
			int col = Integer.parseInt(tmp[1]);
			try {
				rs.absolute(row);
				Object obj = changedValue.get(key);
				if(obj instanceof LOBData){
					if(((LOBData) obj).getType()==Types.CLOB){
						Clob cl = rs.getClob(col);
						if(cl==null){
							cl = frame.dao.getEmptyClob();
							rs.updateClob(col,cl);
							rs.updateRow();
							cl = rs.getClob(col);
						}
						if(cl!=null){
							cl.truncate(0);
							cl.setString(1, ((LOBData) obj).getValue());
							rs.updateClob(col, cl);
						}
					}
					else{
						rs.updateObject(col, ((LOBData) obj).getData());
					}
				}
				rs.updateRow();
			} catch (SQLException e1) {
				Frame.showMsg(e1.getMessage());
				LogUtil.logError(e1);
				break;
			}
		}
		JTable t = getJTable();
		//处理被删除的行
		Collections.sort(delRows,new Comparator<Integer>(){
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2-o1;
			}
		});
		for(Integer row:delRows){
			try {
				rs.absolute(row);
				rs.deleteRow();
			} catch (SQLException e1) {
				Frame.showMsg(e1.getMessage());
				LogUtil.logError(e1);
				break;
			}
		}
		for(int i=0;i<t.getRowCount();i++){
			//处理新插入的数据
			if(t.getValueAt(i, 0)==null){
				try {
					rs.moveToInsertRow();
					for(int j=1;j<t.getColumnCount();j++){
						Object obj = t.getValueAt(i,j);
						try {
							int type = rs.getMetaData().getColumnType(j);
							rs.updateObject(j, JdbcType.tansValue(obj,type));
						} catch (Exception e1) {
							LogUtil.logError(e1);
						}
					}
					rs.insertRow();
				} catch (SQLException e1) {
					Frame.showMsg(e1.getMessage());
					LogUtil.logError(e1);
					break;
				}
			}
		}
		try {
			con.commit();
			changedValue.clear();
			delRows.clear();
		} catch (SQLException e1) {
			Frame.showMsg("未保存成功，错误原因：\n"+e1.getMessage());
			try {
				con.rollback();
			} catch (SQLException e2) {
				LogUtil.logError(e2);
			}
			return;
		}
		getBtnSave().setEnabled(false);
		getBtnRollback().setEnabled(false);
	}

	private void searchData() {
		searchData("");
	}

	private void searchData(String msg) {
		int i=0;
		int beginRow=0,endRow=0;
		try {
			DefaultTableModel model = (DefaultTableModel) jTable.getModel();
			model.setRowCount(0);
			if(isPageTurn){
				rs.close();
				rs = st.executeQuery(sql);
			}
			if(curPage<=1){
				rs.beforeFirst();
			}
			else{
				rs.absolute((curPage-1)*rows);
			}
			beginRow = rs.getRow()+1;
			while(i++<rows&&rs!=null&&rs.next()){
				int ccnt = rs.getMetaData().getColumnCount();
				Object[] rowData = new Object[ccnt+1];
				rowData[0] = rs.getRow();
				for(int j=0;j<ccnt;j++){
					int type = rs.getMetaData().getColumnType(j+1);
					if(type==Types.CLOB){
						rowData[j+1] = new LOBData(rs.getClob(j+1),type);
					}
					else if(type==Types.BLOB){
						rowData[j+1] = new LOBData(rs.getBlob(j+1),type);
					}
					else{
						rowData[j+1] = rs.getObject(j+1);
					}
				}
				model.addRow(rowData);
			}
			btnPrev.setEnabled(curPage>1);
			btnNext.setEnabled(!rs.isAfterLast());
			if(rs.isAfterLast()){
				rs.last();
			}
			endRow = rs.getRow();
			searchMsg = "第"+curPage+"页，记录从第"+(beginRow)+"行至第"+endRow+"行."+msg;
			setStatus(searchMsg);
			isPageTurn = true;
		} catch (SQLException e1) {
			LogUtil.logError(e1);
		}
		getBtnEditLock().setEnabled(true);
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
			jPanel2.add(getJPanel3(), BorderLayout.CENTER);
			jPanel2.add(getJPanel1(), BorderLayout.SOUTH);
		}
		return jPanel2;
	}

	private String getExecuteSql() {
		String tmpSql = getTxtSql().getText();
		if(StringUtil.isNotBlank(getTxtSql().getSelectedText())){
			tmpSql = getTxtSql().getSelectedText();
		}
		if(StringUtil.isBlank(tmpSql)){
			return "";
		}
		if(tmpSql.endsWith(";")){
			tmpSql = tmpSql.replaceAll(";$", "");
		}
		sql = tmpSql;
		return tmpSql;
	}
	
	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new CardLayout());
			jPanel3.add(getSpUpdate(), getSpUpdate().getName());
			jPanel3.add(getJSplitPane(), getJSplitPane().getName());
		}
		return jPanel3;
	}

	/**
	 * This method initializes spQuery	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSpQuery() {
		if (spQuery == null) {
			spQuery = new JScrollPane();
		}
		return spQuery;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("详细");
			jButton.setPreferredSize(new Dimension(40, 23));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Frame.showMsg(lblStatus.getText());
				}
			});
		}
		return jButton;
	}

}  //  @jve:decl-index=0:visual-constraint="165,15"
