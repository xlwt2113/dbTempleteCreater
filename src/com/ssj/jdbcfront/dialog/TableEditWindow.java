package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.model.DbColumnType;
import com.ssj.jdbcfront.model.DbTableType;
import com.ssj.jdbcfront.swing.ComboTableEditor;
import com.ssj.jdbcfront.util.JTableUtil;
import com.ssj.util.StringUtil;

public class TableEditWindow extends JInternalFrame {

	private static final long serialVersionUID = -3068809689683315531L;
	private String tableName;
	public DbTableType dtt;  //  @jve:decl-index=0:
	protected List<DbColumnType> colDelList = new ArrayList<DbColumnType>();
	
	TableEditWindow _this;

	private Frame frame;
	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel pnlBottom = null;
	private JPanel pnlTop = null;
	private JScrollPane spMain = null;
	private JTable tblColumns = null;
	private JLabel jLabel = null;
	private JTextField txtName = null;
	private JLabel jLabel1 = null;
	private JTextField txtComment = null;
	private JPanel pnlOpt = null;
	private JButton btnAdd = null;
	private JButton btnDel = null;
	private JButton btnApply = null;
	private JButton btnRefresh = null;
	private JButton btnClose = null;
	private JButton btnQuery = null;
	private JToggleButton btnViewSql = null;

	protected TableCellEditor curEditor;

	private JButton jButton = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JScrollPane jScrollPane = null;

	private RSyntaxTextArea jTextArea = null;

	/**
	 * This is the xxx default constructor
	 */
	public TableEditWindow(String name) {
		super(StringUtil.isBlank(name)?"�½���":"�༭��ṹ��"+name,true,true,true,true);
		frame = Frame.getInstance();
		_this = this;
		initialize();
		if(name==null){
			name = "";
		}
		tableName = name;
		this.txtName.setText(name);
		this.requestFocus();
		if("".equals(name)){
			txtName.requestFocus(true);
		}
		loadTableColumns();
	}

	/**
	* �����ˣ���˧��
	* ��������: 2010-11-29  ����10:45:55
	* ��������: ���ر�ṹ
	* �����Ĳ����ͷ���ֵ: 
	*/
	private void loadTableColumns() {
		dtt = frame.dao.getTableType(tableName);
		txtComment.setText(dtt.getComment());
		tblColumns.getModel();
		int i=1;
		if(tblColumns.getCellEditor()!=null)
			tblColumns.getCellEditor().stopCellEditing();
		DefaultTableModel model = (DefaultTableModel) tblColumns.getModel();
		model.setRowCount(0);
		for(DbColumnType dct:dtt.getColumns()){
			Object[] rowData = new Object[8];
			rowData[0] = i++;
			rowData[1] = dct;
			rowData[2] = dct.getName();
			rowData[3] = dct.getType();
			rowData[4] = dct.isIsNull();
			rowData[5] = dct.isIsPri();
			rowData[6] = dct.getDefaul();
			rowData[7] = dct.getComment();
			model.addRow(rowData);
		}
	}
	
	public String getDefTableName(){
		return tableName;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(663, 433);
		this.setFrameIcon(new ImageIcon(getClass().getResource("/com/ssj/jdbcfront/img/calendar_view_month.gif")));
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
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getPnlTop(), BorderLayout.NORTH);
			mainPanel.add(getJPanel2(), BorderLayout.CENTER);
			mainPanel.add(getPnlBottom(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	/**
	 * This method initializes pnlBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBottom() {
		if (pnlBottom == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			pnlBottom = new JPanel();
//			pnlBottom.setLayout(gridLayout);
			pnlBottom.setLayout(new BorderLayout());
			pnlBottom.add(getJPanel1(), BorderLayout.CENTER);
			pnlBottom.add(getJPanel(), BorderLayout.EAST);
		}
		return pnlBottom;
	}

	/**
	 * This method initializes pnlTop	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlTop() {
		if (pnlTop == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("ע�ͣ�");
			jLabel = new JLabel();
			jLabel.setText("������");
			pnlTop = new JPanel();
			pnlTop.setLayout(new BoxLayout(getPnlTop(), BoxLayout.X_AXIS));
			pnlTop.add(getPnlOpt(), null);
			pnlTop.add(jLabel, null);
			pnlTop.add(getTxtName(), null);
			pnlTop.add(jLabel1, null);
			pnlTop.add(getTxtComment(), null);
		}
		return pnlTop;
	}

	/**
	 * This method initializes spMain	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSpMain() {
		if (spMain == null) {
			spMain = new JScrollPane();
			spMain.setName("spMain");
			spMain.setViewportView(getTblColumns());
		}
		return spMain;
	}

	/**
	 * This method initializes tblColumns	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTblColumns() {
		if (tblColumns == null) {
			tblColumns = new JTable(new DefaultTableModel(new Object[0][0],
					new Object[]{"","�ж���","����","����","�����","����","Ĭ��","ע��"}) {
				private static final long serialVersionUID = 0L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column>0;
				}
			});
			JTableUtil.setTableHeaderHeight(tblColumns,22);
			DefaultTableCellRenderer rd = (DefaultTableCellRenderer) tblColumns.getDefaultRenderer(Integer.class);
			rd.setHorizontalAlignment(SwingConstants.CENTER);//��������о��ж���
			tblColumns.getColumn("").setCellRenderer(rd);
			tblColumns.getColumn("").setMaxWidth(25);
			tblColumns.getColumn("����").setPreferredWidth(110);
			tblColumns.getColumn("����").setMaxWidth(150);
			tblColumns.getColumn("�ж���").setMinWidth(0);
			tblColumns.getColumn("�ж���").setMaxWidth(0);
			tblColumns.getColumn("�ж���").setPreferredWidth(0);
			tblColumns.getColumn("����").setPreferredWidth(105);
			tblColumns.getColumn("����").setMinWidth(125);
			tblColumns.getColumn("����").setMaxWidth(125);
			tblColumns.getColumn("�����").setMaxWidth(55);
			tblColumns.getColumn("�����").setMinWidth(45);
			tblColumns.getColumn("����").setMaxWidth(33);
			tblColumns.getColumn("Ĭ��").setPreferredWidth(80);
			tblColumns.getColumn("Ĭ��").setMaxWidth(105);
			tblColumns.getColumn("ע��").setMinWidth(120);
			tblColumns.getColumnModel().getColumn(0).setPreferredWidth(25);//��������������
			tblColumns.getColumnModel().getColumn(3).setPreferredWidth(45);//��������������
			tblColumns.getColumnModel().getColumn(4).setPreferredWidth(33);//��������������
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 4, Boolean.class);
			JTableUtil.setTableColumnEditorAndRender(tblColumns, 5, Boolean.class);
			ComboTableEditor typeEditor = new ComboTableEditor();
			for(String type:frame.curDb.getDbType().getDataType()){
				typeEditor.addItem(type);
			}
			tblColumns.getColumn("����").setCellEditor(typeEditor);
		}
		return tblColumns;
	}

	/**
	 * This method initializes txtName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
		}
		return txtName;
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
	 * This method initializes pnlOpt	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlOpt() {
		if (pnlOpt == null) {
			pnlOpt = new JPanel();
			pnlOpt.setLayout(new BoxLayout(getPnlOpt(), BoxLayout.X_AXIS));
			pnlOpt.add(getBtnAdd(), null);
			pnlOpt.add(getBtnDel(), null);
		}
		return pnlOpt;
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
			btnAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Object[] row = new Object[]{"","","","",true,false,"",""};
					if(tblColumns.getSelectedRowCount()==1){
						((DefaultTableModel)tblColumns.getModel()).insertRow(tblColumns.getSelectedRow(),row);
					}
					else{
						((DefaultTableModel)tblColumns.getModel()).addRow(row);
					}
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
			btnDel.setText("-");
			btnDel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(tblColumns.getSelectedRowCount()>0){
						int row = tblColumns.getSelectedRow();
						int[] rows = tblColumns.getSelectedRows();
						DefaultTableModel model = ((DefaultTableModel)tblColumns.getModel());
						for(int i=rows.length-1;i>=0;i--){
							if(model.getValueAt(rows[i], 1) instanceof DbColumnType){
								colDelList.add((DbColumnType) model.getValueAt(rows[i], 1));
							}
							model.removeRow(rows[i]);
						}
						if(row>=0&&row<tblColumns.getRowCount()){
							tblColumns.getSelectionModel().setSelectionInterval(row,row);
						}
					}
				}
			});
		}
		return btnDel;
	}

	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton();
			btnApply.setText("Ӧ��");
			btnApply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					TableCellEditor ce = tblColumns.getCellEditor();
					if(ce!=null){
						ce.stopCellEditing();
					}
					if(StringUtil.isBlank(txtName.getText())){
						Frame.showMsg("���������!");
						return;
					}
					if(tblColumns.getRowCount()==0){
						Frame.showMsg("������Ӧ�þ���һ���ֶ�!");
						return;
					}
					String newName = txtName.getText();
					if(frame.dao.isObjectExist(dtt.getName())){//���Ѵ���
						if(!newName.equals(dtt.getName())){//����
							DbUtil.executeSqlUpdate("alter table "+dtt.getName()+" rename to "+newName);
						}
						if(!txtComment.getText().equals(dtt.getComment())){//�޸�ע��
							frame.dao.commentTable(dtt.getName(), txtComment.getText());
						}
						StringBuffer keys = new StringBuffer();
						for(int i=0;i<tblColumns.getRowCount();i++){
							Object obj = tblColumns.getModel().getValueAt(i, 1);
							DbColumnType dct = getDbColumnType(i);
							if(StringUtil.isBlank(dct.getName())){
								Frame.showMsg("��"+(i+1)+"�е��ֶ�������Ϊ��!");
								return;
							}
							if(StringUtil.isBlank(dct.getType())){
								Frame.showMsg("��"+(i+1)+"�е��ֶ����Ͳ���Ϊ��!");
								return;
							}
							if(obj instanceof String){//������
								frame.dao.addTableColumn(dtt.getName(), dct);
							}
							else if(obj instanceof DbColumnType){//�޸���
								DbColumnType oldCol = (DbColumnType) obj;
								frame.dao.updateTableColumn(dtt.getName(), oldCol, dct);
							}
							if(dct.isIsPri()){
								keys.append(",").append(dct.getName());
							}
						}
						for(DbColumnType col:colDelList){//ɾ����
							frame.dao.delTableColumn(dtt.getName(), col);
						}
						String newPkCols = keys.length()>0?keys.substring(1):"";
						if(StringUtil.isNotEqual(dtt.getPkCols(), newPkCols)){//���������и���
							frame.dao.updatePrimaryKey(dtt.getName(), dtt.getPkName(), dtt.getPkCols(), newPkCols);
						}
						colDelList.clear();
					}
					else{//�����ڣ��´���֮
						dtt.setName(newName);
						dtt.setComment(txtComment.getText());
						dtt.setType("TABLE");
						dtt.setPkName(newName+"_pk");
						List<DbColumnType> list = new ArrayList<DbColumnType>();
						for(int i=0;i<tblColumns.getRowCount();i++){
							DbColumnType dct = getDbColumnType(i);
							if(StringUtil.isBlank(dct.getName())){
								Frame.showMsg("��"+(i+1)+"�е��ֶ�������Ϊ��!");
								return;
							}
							if(StringUtil.isBlank(dct.getType())){
								Frame.showMsg("��"+(i+1)+"�е��ֶ����Ͳ���Ϊ��!");
								return;
							}
							list.add(dct);
						}
						if(list.size()==0){
							Frame.showMsg("������Ӧ�þ���һ���ֶ�!");
							return;
						}
						dtt.setColumns(list);
						String sql = frame.dao.getTableCreateSql(dtt);
						for(String tmp:sql.split(";")){
							if(StringUtil.isNotBlank(tmp)){
								DbUtil.executeSqlUpdate(tmp);
							}
						}
					}
					frame.refreshDbType("TABLE");
					tableName = txtName.getText();
					loadTableColumns();
					txtName.setText(dtt.getName());
					txtComment.setText(dtt.getComment());
				}
			});
		}
		return btnApply;
	}
	
	public DbColumnType getDbColumnType(int row){
		DbColumnType dct = new DbColumnType();
		dct.setName(StringUtil.toString(tblColumns.getModel().getValueAt(row, 2)));
		dct.setType(StringUtil.toString(tblColumns.getModel().getValueAt(row, 3)));
		dct.setIsNull(StringUtil.toString(tblColumns.getModel().getValueAt(row, 4)));
		dct.setIsPri(StringUtil.toString(tblColumns.getModel().getValueAt(row, 5)));
		dct.setDefaul(StringUtil.toString(tblColumns.getModel().getValueAt(row, 6)));
		dct.setComment(StringUtil.toString(tblColumns.getModel().getValueAt(row, 7)));
		return dct;
	}

	/**
	 * This method initializes btnRefresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRefresh() {
		if (btnRefresh == null) {
			btnRefresh = new JButton();
			btnRefresh.setText("ˢ��");
			btnRefresh.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					loadTableColumns();
				}});
		}
		return btnRefresh;
	}

	/**
	 * This method initializes btnClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setText("�ر�");
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					_this.dispose();
				}
			});
		}
		return btnClose;
	}

	/**
	 * This method initializes btnQuery	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnQuery() {
		if (btnQuery == null) {
			btnQuery = new JButton();
			btnQuery.setText("��ѯ");
			btnQuery.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DaoAccess da = frame.curDb.getDbType().getDao();
					frame.openSqlFrame(da.getEditQuerySql(dtt.getName()),true,true);
				}
			});
		}
		return btnQuery;
	}

	/**
	 * This method initializes btnViewSql	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JToggleButton getBtnViewSql() {
		if (btnViewSql == null) {
			btnViewSql = new JToggleButton();
			btnViewSql.setText("SQL���");
			btnViewSql.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					((CardLayout)jPanel2.getLayout()).next(jPanel2);
					if(btnViewSql.isSelected())
						jTextArea.setText(frame.dao.getTableCreateSql(dtt));
				}
			});
		}
		return btnViewSql;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("���ɴ���");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					frame.genCode(dtt.getName());
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.add(getBtnViewSql(), null);
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
			jPanel1 = new JPanel();
			FlowLayout fl = new FlowLayout();
			fl.setAlignment(FlowLayout.LEFT);
			jPanel1.setLayout(fl);
			jPanel1.add(getBtnApply(), null);
			jPanel1.add(getBtnRefresh(), null);
			jPanel1.add(getBtnClose(), null);
			jPanel1.add(getBtnQuery(), null);
			jPanel1.add(getJButton(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new CardLayout());
			jPanel2.add(getSpMain(), getSpMain().getName());
			jPanel2.add(getJScrollPane(), getJScrollPane().getName());
		}
		return jPanel2;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setName("jScrollPane");
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
			jTextArea = new RSyntaxTextArea();
			jTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
			jTextArea.setEditable(false);
		}
		return jTextArea;
	}

}  //  @jve:decl-index=0:visual-constraint="262,11"
