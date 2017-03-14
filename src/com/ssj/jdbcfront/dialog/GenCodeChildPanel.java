package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.model.DbColumnType;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.model.DbTableType;
import com.ssj.jdbcfront.model.GenCodeColumn;
import com.ssj.jdbcfront.model.GenCodeModel;
import com.ssj.jdbcfront.swing.ComboTableEditor;
import com.ssj.jdbcfront.template.TransUtil;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.jdbcfront.util.JTableUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;
import javax.swing.JButton;

public class GenCodeChildPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private GenCodeWindow parWin;
	
	private Frame frame;
	private String mytext;
	private String tblName;  //  @jve:decl-index=0:
	/**
	 * 判断是否可见，如果是第一次显示时，自动更新包路径和开发人
	 */
	private boolean show;
	
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null;
	private RSyntaxTextArea txtQuerySql = null;
	private JPanel jPanel1 = null;
	private JScrollPane jScrollPane1 = null;
	private JTable tblColumns = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JTextField txtUrl = null;
	private JTextField txtComment = null;
	private JLabel jLabel2 = null;
	private JTextField txtPackage = null;
	private JLabel jLabel3 = null;
	private JTextField txtDevelpor = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private JPanel jPanel4 = null;

	private JPanel jPanel5 = null;

	private JSplitPane jSplitPane1 = null;

	private JPanel jPanel6 = null;

	private JLabel jLabel4 = null;

	private JComboBox cbSubTable = null;
	
	private JCheckBox jCheckBox1 = null;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel label;
	private JLabel label_1;
	private JTextField txtTemplatePath;
	private JTextField txtTargetPath;
	private JButton btnNewButton;
	private JButton button;

	public void setParWin(GenCodeWindow parWin) {
		this.parWin = parWin;
	}
	/**
	 * This is the default constructor
	 */
	public GenCodeChildPanel(String text) {
		super();
		initialize();
		this.addComponentListener(new ComponentListener(){
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
			}
			public void componentResized(ComponentEvent arg0) {
			}
			public void componentShown(ComponentEvent arg0) {
				if(!show){
					loadData(mytext);
					txtPackage.setText(ConfigUtil.getString("LAST_PACKAGE"));
					txtDevelpor.setText(ConfigUtil.getString("DEFAULT_DEVELPOR"));
				}
				show = true;
			}});
		frame = Frame.getInstance();
		mytext = text;
	}
	
	public GenCodeModel getModel(){
		if(!show){
			loadData(mytext);
			txtPackage.setText(ConfigUtil.getString("LAST_PACKAGE"));
			txtDevelpor.setText(ConfigUtil.getString("DEFAULT_DEVELPOR"));
		}
		if(StringUtil.isBlank(txtUrl.getText())){
			txtUrl.requestFocus();
			throw new RuntimeException("小写字母复数不能为空!");
		}
		if(StringUtil.isBlank(txtPackage.getText())){
			txtPackage.requestFocus();
			throw new RuntimeException("小写字母单数不能为空!");
		}
		if(StringUtil.isBlank(txtDevelpor.getText())){
			txtDevelpor.requestFocus();
			throw new RuntimeException("数据表名不能为空!");
		}
		if(StringUtil.isBlank(txtDevelpor.getText())){
			txtDevelpor.requestFocus();
			throw new RuntimeException("数据表名单数格式不能为空!");
		}
		GenCodeModel model = new GenCodeModel();
		model.setTemplatePath(txtTemplatePath.getText());
		model.setTargetPath(txtTargetPath.getText());
		model.setDbUser(frame.dao.getDbv().getUserName());
		model.setName(tblName);
		if(cbSubTable.getSelectedItem() instanceof DbObject){
			DbObject dbo = (DbObject) cbSubTable.getSelectedItem();
			model.setSubTable(frame.dao.getTableType(dbo.getName()));
		}
		model.setUrl(txtUrl.getText());
		model.setBasePackage(txtPackage.getText());
		model.setComment(txtComment.getText());
		model.setDevelpor(txtDevelpor.getText());
		model.setQuerySql(txtQuerySql.getText());
		List<GenCodeColumn> keys = new ArrayList<GenCodeColumn>();
		List<GenCodeColumn> list = new ArrayList<GenCodeColumn>();
		model.setKeyList(keys);
		model.setColumnList(list);
		boolean multiSel = tblColumns.getSelectedRowCount()>1;
		System.out.println("==============="+tblColumns.getRowCount());
		
		for(int i=0;i<tblColumns.getRowCount();i++){
			if(multiSel&&!tblColumns.isRowSelected(i))
				continue;
			GenCodeColumn col = new GenCodeColumn();
			col.setName(toString(tblColumns.getModel().getValueAt(i, 1)));
			col.setFieldName(toString(tblColumns.getModel().getValueAt(i, 2)));
			col.setType(toString(tblColumns.getModel().getValueAt(i, 3)));
			try {
				col.setTypeLen(Integer.parseInt(toString(tblColumns.getModel().getValueAt(i, 4))));
			} catch (NumberFormatException e) {
			}
			col.setFieldType(toString(tblColumns.getModel().getValueAt(i, 5)));
			col.setComment(toString(tblColumns.getModel().getValueAt(i, 6)));
			col.setIsNull(toString(tblColumns.getModel().getValueAt(i, 7)));
			col.setIsPri(toString(tblColumns.getModel().getValueAt(i, 8)));
			col.setIsHidden(toString(tblColumns.getModel().getValueAt(i, 9)));
			col.setQueryCol(toString(tblColumns.getModel().getValueAt(i, 10)));
			col.setQueryType(toString(tblColumns.getModel().getValueAt(i, 11)));
			col.setDictName(toString(tblColumns.getModel().getValueAt(i, 12)));
			String str = toString(tblColumns.getModel().getValueAt(i, 13));
			if(str!=null&&str.length()>0)
				col.setSeq(Integer.parseInt(str));
			if(col.isIsPri()){
				keys.add(col);
			}
			else{
				list.add(col);
			}
		}
		if(keys.size()==0){
			throw new RuntimeException("请指定至少一个主键列!");
		}
		ConfigUtil.saveTblModel(model);
		return model;
	}
	
	private String toString(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	private String nvl(String s1,String s2){
		return s1==null||s1.length()==0?s2:s1;
	}

	private void loadData(final String arg0) {
		if("true".equals(ConfigUtil.getString("USE_TABLE_NAME"))){
			jCheckBox1.setSelected(true);
		}
		DaoAccess dao = frame.dao;
		String text = arg0.trim().toUpperCase();
		text = text.replaceAll("\\s+FOR\\s+UPDATE", "");
		if(Pattern.matches("SELECT T\\.\\* FROM \\S+ T", text)){
			text = text.replaceAll("SELECT T\\.\\* FROM (\\S+) T", "$1");
		}
		//王涛 改成小写
		tblName = text.toLowerCase();
		List<DbColumnType> cols = null;
		DbTableType dtt = null;
		if(!text.startsWith("SELECT")){
			text = "SELECT * FROM "+(jCheckBox1.isSelected()?dao.getDbv().getUserName()+".":"")+text+" t";
			dtt = dao.getTableType(tblName);
			cols = dtt.getColumns();
		}
		else{
			if(text.endsWith(";")){
				text = text.replaceAll(";+$", "");
			}
			Pattern pat = Pattern.compile("SELECT.+FROM\\s+(\\S+)\\s+.+");
			Matcher mat = pat.matcher(text);
			if(mat.matches()){
				tblName = mat.group(1);
			}
			else{
				tblName = "";
			}
			Connection con;
			cols = new ArrayList<DbColumnType>();
			try {
				con = DbUtil.createConnection();
				Statement st = con.createStatement();
				st.setFetchSize(1);
				//王涛 改为小写
				text = text.toLowerCase();
				ResultSet rs = st.executeQuery(text.toLowerCase());
				ResultSetMetaData rsm = rs.getMetaData();
				for(int i=0;i<rsm.getColumnCount();i++){
					DbColumnType dt = new DbColumnType(rsm.getColumnName(i+1),rsm.getColumnTypeName(i+1));
					cols.add(dt);
				}
			} catch (Exception e) {
				LogUtil.logError(e);
				cols.add(new DbColumnType(e.getMessage()));
			}
			dtt = dao.getTableType(tblName);
		}
		txtTemplatePath.setText(ConfigUtil.getString("TEMPLATE_PATH"));
		txtTargetPath.setText(ConfigUtil.getString("TARGET_PATH"));
		
		//王涛  设置默认值
		String objName = TransUtil.getPropertyName(tblName);
		txtUrl.setText(objName);
		txtPackage.setText(objName.substring(0,objName.length()-1));
		txtDevelpor.setText(tblName.toLowerCase());
		txtComment.setText(tblName.substring(0, tblName.length()-1).toLowerCase());
		
		System.out.println("===cols.size()==="+cols.size());
		System.out.println("===dtt.getColumns().size()==="+dtt.getColumns().size());
		
		for(int i=0;i<cols.size();i++){
			GenCodeColumn dc = new GenCodeColumn(dtt.getColumns().get(i));
			String javaType = TransUtil.trans2JavaType(dc.getBaseType());
			if(javaType==null){
				javaType = "java.lang.String";
			}
			if(Pattern.matches("NUMBER\\(\\d+,\\d+\\)", dc.getType())){
				javaType = "double";
			}
		
			Object[] row = new Object[]{i+1,dc.getName(),TransUtil.getPropertyName(dc.getName()),
					dc.getType(),dc.getTypeLen(),javaType,dc.getComment(),
					dc.isIsNull(),dc.isIsPri(),dc.getIsHidden(),dc.getQueryType(),dc.getDictName()};
			((DefaultTableModel)tblColumns.getModel()).addRow(row);
		}
		txtQuerySql.setText(text);
		List<DbObject> list = frame.dao.getAllTables();
		for(DbObject tbl:list){
			cbSubTable.addItem(tbl);
			if(tbl.getName().length()>tblName.length()&&tbl.getName().toUpperCase().startsWith(tblName.toUpperCase())){
				cbSubTable.setSelectedItem(tbl);
			}
		}
		GenCodeModel model = ConfigUtil.loadTblModel(tblName);
		if(model!=null){
			setGenContent(model);
		}
	}
	
	/**
	 * 根据存储的内容对界面进行填充
	 * @param model
	 */
	private void setGenContent(GenCodeModel model) {
		System.out.println("============"+tblColumns.getRowCount());
		if(StringUtil.isNotBlank(model.getTemplatePath())){
			txtTemplatePath.setText(model.getTemplatePath());
		}
		if(StringUtil.isNotBlank(model.getTargetPath())){
			txtTargetPath.setText(model.getTargetPath());
		}
		txtUrl.setText(model.getUrl());
		txtPackage.setText(model.getBasePackage());
		txtComment.setText(model.getComment());
		txtDevelpor.setText(model.getDevelpor());
		txtQuerySql.setText(model.getQuerySql());
		if(model.getSubTable()!=null){
			cbSubTable.setSelectedItem(model.getSubTable().getName());
		}
		for(int i=0;i<tblColumns.getRowCount();i++){
			String name = (String) tblColumns.getValueAt(i, 1);
			GenCodeColumn col = model.getColumn(name);
			if(col!=null){
				tblColumns.setValueAt(col.getFieldName(), i, 2);
				tblColumns.setValueAt(col.getTypeLen(), i, 4);
				tblColumns.setValueAt(col.getFieldType(), i, 5);
				tblColumns.setValueAt(col.getComment(), i, 6);
				tblColumns.setValueAt(col.isIsNull(), i, 7);
				tblColumns.setValueAt(col.isIsPri(), i, 8);
				tblColumns.setValueAt(col.getIsHidden(), i, 9);
				tblColumns.setValueAt(col.isQueryCol(), i, 10);
				tblColumns.setValueAt(col.getQueryType(), i, 11);
				tblColumns.setValueAt(col.getDictName(), i, 12);
				tblColumns.setValueAt(col.getSeq(), i, 13);
			}
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(564, 394);
		this.setLayout(new BorderLayout());
		this.add(getJPanel(),BorderLayout.CENTER);
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
			jPanel.add(getJSplitPane1(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setViewportView(getTxtQuerySql());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes txtQuerySql	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtQuerySql() {
		if (txtQuerySql == null) {
			txtQuerySql = new RSyntaxTextArea();
			txtQuerySql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
			txtQuerySql.setLineWrap(true);
		}
		return txtQuerySql;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("数据表名称：");  //原开发人
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel2 = new JLabel();
			jLabel2.setText("数据表名称单数格式："); //原注释
			jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			GridLayout gridLayout = new GridLayout();
			gridLayout.setColumns(4);
			gridLayout.setRows(4);
			gridLayout.setHgap(7);
			gridLayout.setVgap(4);
			jLabel1 = new JLabel();
			jLabel1.setText("小写字母单数："); //原包路径
			jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel = new JLabel();
			jLabel.setText("小写字母复数：");  //原URL
			jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jPanel1 = new JPanel();
			jPanel1.setPreferredSize(new Dimension(156, 120));
			jPanel1.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			jPanel1.setLayout(gridLayout);
			jPanel1.add(getPanel());
			jPanel1.add(getPanel_1());
			jPanel1.add(getJPanel2(), null); //小写字母复数
			jPanel1.add(getJPanel3(), null); //小写字母单数
			jPanel1.add(getJPanel5(), null); //表名
			jPanel1.add(getJPanel4(), null); //表名单数
			jPanel1.add(getJPanel6(), null);
			jPanel1.add(getJCheckBox1());
		}
		return jPanel1;
	}

	private JCheckBox getJCheckBox1() {
		if(jCheckBox1==null){
			jCheckBox1 = new JCheckBox();
			jCheckBox1.setText("使用表模式名");
			jCheckBox1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					ConfigUtil.setString("USE_TABLE_NAME", ""+jCheckBox1.isSelected());
				}});
		}
		return jCheckBox1;
	}

	/**
	 * This method initializes txtUrl	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtUrl() {
		if (txtUrl == null) {
			txtUrl = new JTextField();
		}
		return txtUrl;
	}

	/**
	 * This method initializes txtComment	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtComment() {
		if (txtComment == null) {
			txtComment = new JTextField();
		}
		return txtComment;
	}

	/**
	 * This method initializes txtPackage	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPackage() {
		if (txtPackage == null) {
			txtPackage = new JTextField();
			txtPackage.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					ConfigUtil.setString("LAST_PACKAGE", txtPackage.getText());
				}
			});
		}
		return txtPackage;
	}

	/**
	 * This method initializes txtDevelpor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtDevelpor() {
		if (txtDevelpor == null) {
			txtDevelpor = new JTextField();
			txtDevelpor.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					ConfigUtil.setString("DEFAULT_DEVELPOR", txtDevelpor.getText());
				}
			});
		}
		return txtDevelpor;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getTblColumns());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes tblColumns	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblColumns() {
		if (tblColumns == null) {
			tblColumns = new JTable(){
				public void changeSelection(int rowIndex, int columnIndex,boolean toggle, boolean extend){
					super.changeSelection(rowIndex, columnIndex, toggle, extend);
					super.editCellAt(rowIndex, columnIndex);//实现鼠标单击编辑单元格
				}
			};
			DefaultTableModel model = new DefaultTableModel(){
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return column>1&&column!=3&&column!=13;
				}
			};
			model.setDataVector(new Object[0][13],
					new Object[]{"","列名","变量名","列类型","列长度","Java类型","注释","允许空","主键","隐藏","查询","查询类型","字典名称","排序"});
			tblColumns.setModel(model);
			tblColumns.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent arg0) {
					if(Pattern.matches("SELECT\\s+.+\\s+FROM\\s+\\S+\\s+t(\\s+WHERE\\s+.*)*", txtQuerySql.getText())){
						System.out.println("========"+txtQuerySql.getText());
						int[] rows = tblColumns.getSelectedRows();
						if(rows.length>1){
							StringBuffer sql = new StringBuffer();
							for(int i:rows){
								sql.append(",T.").append(tblColumns.getValueAt(i, 1));
							}
							if(sql.length()>1){
								txtQuerySql.setText(txtQuerySql.getText().replaceAll("SELECT\\s+.+\\s+FROM", "SELECT "+sql.substring(1).replace("$", "\\$")+" FROM"));
							}
						}
					}
				}});
			tblColumns.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					JTable tbl = (JTable)e.getSource();
					int row =tbl.rowAtPoint(e.getPoint()); //获得行位置 
                    int  col=tbl.columnAtPoint(e.getPoint()); //获得列位置
					if(col==13){//排序列单击后数字+1
						Object obj = tbl.getModel().getValueAt(row, col);
						int val = 1;
						if(obj!=null){
							val = Integer.parseInt(obj.toString())+1;
						}
						tbl.setValueAt(val, row, col);
						if(e.getButton()!=MouseEvent.BUTTON1){
							tbl.setValueAt(null, row, col);
						}
					}
					super.mouseClicked(e);
				}
			});
			JTableUtil.setTableHeaderHeight(tblColumns,22);
			DefaultTableCellRenderer rd = (DefaultTableCellRenderer) tblColumns.getDefaultRenderer(Integer.class);
			rd.setHorizontalAlignment(SwingConstants.CENTER);//设置序号列居中对齐
			tblColumns.getColumn("").setCellRenderer(rd);
			tblColumns.getColumn("列长度").setCellRenderer(rd);
			tblColumns.getColumn("排序").setCellRenderer(rd);
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 7, Boolean.class);
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 8, Boolean.class);
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 9, Boolean.class);
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 10, Boolean.class);
			tblColumns.getColumn("").setMaxWidth(25);
			tblColumns.getColumn("列名").setPreferredWidth(95);
			tblColumns.getColumn("变量名").setPreferredWidth(75);
			tblColumns.getColumn("列类型").setPreferredWidth(95);
			tblColumns.getColumn("列类型").setMaxWidth(105);
			tblColumns.getColumn("列长度").setMaxWidth(45);
			tblColumns.getColumn("Java类型").setPreferredWidth(125);
			tblColumns.getColumn("Java类型").setMaxWidth(145);
			tblColumns.getColumn("注释").setMinWidth(130);
			tblColumns.getColumn("允许空").setPreferredWidth(50);
			tblColumns.getColumn("主键").setPreferredWidth(33);
			tblColumns.getColumn("隐藏").setPreferredWidth(33);
			tblColumns.getColumn("查询").setPreferredWidth(33);
			tblColumns.getColumn("查询类型").setPreferredWidth(65);
			tblColumns.getColumn("字典名称").setPreferredWidth(65);
			tblColumns.getColumn("排序").setPreferredWidth(33);
			tblColumns.getColumn("Java类型").setCellEditor(getComboEditor("java.lang.String;测试;int;long;float;double;java.util.Date;java.lang.Integer;java.lang.Long;java.lang.Float;java.lang.Double;java.math.BigInteger;java.math.BigDecimal".split(";")));
			tblColumns.getColumn("查询类型").setCellEditor(getComboEditor("文本输入框;数字范围框;日期范围框;普通下拉框;字典下拉框;下拉机构树".split(";")));
		}
		return tblColumns;
	}
	
	private ComboTableEditor getComboEditor(String[] values){
		ComboTableEditor selEditor = new ComboTableEditor();
		for(String str:values)
			selEditor.addItem(str);
		return selEditor;
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
			jPanel2.add(jLabel, BorderLayout.WEST);
			jPanel2.add(getTxtUrl(), BorderLayout.CENTER);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.add(jLabel1, BorderLayout.WEST);
			jPanel3.add(getTxtPackage(), BorderLayout.CENTER);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BorderLayout());
			jPanel4.add(jLabel2, BorderLayout.WEST);
			jPanel4.add(getTxtComment(), BorderLayout.CENTER);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new BorderLayout());
			jPanel5.add(jLabel3, BorderLayout.WEST);
			jPanel5.add(getTxtDevelpor(), BorderLayout.CENTER);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jSplitPane1	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane1() {
		if (jSplitPane1 == null) {
			jSplitPane1 = new JSplitPane();
			jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane1.setDividerLocation(50);
			jSplitPane1.setDividerSize(5);
			jSplitPane1.setBottomComponent(getJScrollPane1());
			jSplitPane1.setTopComponent(getJScrollPane());
		}
		return jSplitPane1;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jLabel4 = new JLabel();
			jLabel4.setText("子表：");
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BorderLayout());
			jPanel6.add(jLabel4, BorderLayout.WEST);
			jPanel6.add(getCbSubTable(), BorderLayout.CENTER);
		}
		return jPanel6;
	}

	/**
	 * This method initializes cbSubTable	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbSubTable() {
		if (cbSubTable == null) {
			cbSubTable = new JComboBox();
			cbSubTable.setEditable(true);
			cbSubTable.addItem("");
		}
		return cbSubTable;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getLabel(), BorderLayout.WEST);
			panel.add(getTxtTemplatePath(), BorderLayout.CENTER);
			panel.add(getBtnNewButton(), BorderLayout.EAST);
		}
		return panel;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new BorderLayout());
			panel_1.add(getLabel_1(), BorderLayout.WEST);
			panel_1.add(getTxtTargetPath(), BorderLayout.CENTER);
			panel_1.add(getButton(), BorderLayout.EAST);
		}
		return panel_1;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel();
			label.setText("\u6A21\u677F\uFF1A");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label;
	}
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel();
			label_1.setText("\u76EE\u6807\u8DEF\u5F84");
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_1;
	}
	private JTextField getTxtTemplatePath() {
		if (txtTemplatePath == null) {
			txtTemplatePath = new JTextField();
			txtTemplatePath.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					if(StringUtil.isNotBlank(txtTemplatePath.getText())){
						ConfigUtil.setString("TEMPLATE_PATH", txtTemplatePath.getText());
					}
				}
			});
		}
		return txtTemplatePath;
	}
	private JTextField getTxtTargetPath() {
		if (txtTargetPath == null) {
			txtTargetPath = new JTextField();
			txtTargetPath.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					if(StringUtil.isNotBlank(txtTargetPath.getText())){
						ConfigUtil.setString("TARGET_PATH", txtTargetPath.getText());
					}
				}
			});
		}
		return txtTargetPath;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("...");
			btnNewButton.setPreferredSize(new Dimension(30, 28));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jc=new JFileChooser(); //文件选择器
					jc.setDialogTitle("请选择模板目录");
					jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					String path = ConfigUtil.getString("TEMPLATE_PATH");
					if(StringUtil.isNotBlank(txtTemplatePath.getText())){
						jc.setCurrentDirectory(new File(txtTemplatePath.getText()));
					}
					else if(StringUtil.isNotBlank(path)){
						jc.setCurrentDirectory(new File(path));
					}
					int result=jc.showOpenDialog(Frame.getInstance());
					if(result==JFileChooser.APPROVE_OPTION){
						txtTemplatePath.setText(jc.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return btnNewButton;
	}
	private JButton getButton() {
		if (button == null) {
			button = new JButton("...");
			button.setPreferredSize(new Dimension(30, 28));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jc=new JFileChooser(); //文件选择器
					jc.setDialogTitle("请选择目标目录");
					jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					String path = ConfigUtil.getString("TARGET_PATH");
					if(StringUtil.isNotBlank(txtTargetPath.getText())){
						jc.setCurrentDirectory(new File(txtTargetPath.getText()));
					}
					else if(StringUtil.isNotBlank(path)){
						jc.setCurrentDirectory(new File(path));
					}
					int result=jc.showOpenDialog(Frame.getInstance());
					if(result==JFileChooser.APPROVE_OPTION){
						txtTargetPath.setText(jc.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}
		return button;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
