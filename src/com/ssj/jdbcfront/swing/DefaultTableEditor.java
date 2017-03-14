package com.ssj.jdbcfront.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.JdbcType;
import com.ssj.jdbcfront.dao.LOBData;
import com.ssj.jdbcfront.dialog.SqlExecuteWindow;
import com.ssj.jdbcfront.frame.Frame;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

/**
* 创建人：宋帅杰
* 创建日期：2010-12-1
* 创建时间：下午07:47:57
* 功能描述：
* ==============================================
* 修改历史
* 修改人		修改时间		修改原因
*
* ==============================================
*/
public class DefaultTableEditor extends JPanel  implements TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -491938167262506716L;
	protected JTextArea jTextField = null;
	private JButton jButton = null;
	private int row,col;
	private JTable jTable;
	private SqlExecuteWindow sf;
	private int jdbcType;
	private boolean change;
	private boolean isNewRow;
	private Object userObject;
	protected transient Vector<CellEditorListener> listeners;

	  protected transient int originalValue;

	  protected transient boolean editing;

	  protected static transient HashMap<Integer,DefaultTableEditor> editors = new HashMap<Integer,DefaultTableEditor>();  //  @jve:decl-index=0:
	private JDialog jDialog = null;
	private JPanel jContentPane = null;
	private JTextArea jTextArea = null;
	private JScrollPane jScrollPane = null;
	/**
	 * This method initializes 
	 * 
	 */
	public DefaultTableEditor(SqlExecuteWindow sf,int jdbcType) {
		super();
		this.sf = sf;
		this.jdbcType = jdbcType;
		initialize();
	}
	protected DefaultTableEditor(){
		super();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(174, 20));
        this.add(getJTextField(), BorderLayout.CENTER);
        JButton jb = getJButton();
        if(jb!=null){
        	this.add(getJButton(), BorderLayout.EAST);
        }
	    listeners = new Vector<CellEditorListener>();
		if(jdbcType==java.sql.Types.CLOB||
				jdbcType==java.sql.Types.BLOB){
			jTextField.setEditable(false);
			jTextField.setText(DaoAccess.getJdbcTypeName(jdbcType));
		}
	}

	public static DefaultTableEditor getEditor(SqlExecuteWindow sf,int jdbcType) {
//		if(jdbcType==Types.DATE||jdbcType==Types.TIME||jdbcType==Types.TIMESTAMP){
//			return new DatePickerEditor(sf,jdbcType);
//		}
		return new DefaultTableEditor(sf,jdbcType);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if(value==null&&(jdbcType==Types.CLOB||jdbcType==Types.BLOB)){
			value = new LOBData(value,jdbcType);
		}
		userObject = value;
		getJTextField().setText(value==null?"":value.toString());
		table.setRowSelectionInterval(row, row);
	    table.setColumnSelectionInterval(column, column);
	    getJTextField().requestFocus();
	    jTable = table;
	    this.row = row;
	    this.col = column;
	    editing = true;
	    change = false;
	    isNewRow = table.getValueAt(row, 0)==null||StringUtil.isBlank(table.getValueAt(row, 0).toString());
		return this;
	}

	public void addCellEditorListener(CellEditorListener l) {
		   listeners.addElement(l);
	}

	public void cancelCellEditing() {
	    fireEditingCanceled();
	    editing = false;
	}

	public Object getCellEditorValue() {
		if(!(userObject instanceof LOBData)){
			String obj = jTextField.getText();
			userObject = JdbcType.tansValue(obj, jdbcType);
		}
		return userObject;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		
		return true;
	}

	public boolean stopCellEditing() {
		try {
		    fireEditingStopped();
		    if(!change){//没有更改内容，
		    	return true;
		    }
			if(row>=jTable.getRowCount()||jTable.getValueAt(row, 0)==null)//如果是新插入的行，则不进行更新
				return true;
//			if(!jTextField.isEditable()){//CLOB和BLOB暂不处理
//				return true;
//			}
			Integer rowIndex = (Integer)jTable.getValueAt(row, 0);
			jTextField.setText(userObject==null?"":userObject.toString());
			jTable.setValueAt(userObject==null?"":userObject, row, col);
			sf.addChangedValue(rowIndex, col, userObject);
		} catch (Exception e) {
			LogUtil.logError(e);
			sf.setStatus("数据格式不对，错误原因：\n"+e.getMessage());
			return false;
		}
		
	    editing = false;
	    change = false;
		return true;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextArea getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextArea();
			jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					change = true;
					sf.changeResultTable();
				}
			});
		}
		return jTextField;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("...");
			jButton.setPreferredSize(new Dimension(17, 20));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(userObject instanceof LOBData){
						if(isNewRow){
							Frame.showMsg("暂不提供在新行中插入CLOB值");
							return;
						}
						LOBData ld = (LOBData) userObject;
						if(ld.getType()==Types.CLOB){
							Clob cl = (Clob) ld.getData();
							if(cl!=null){
								try {
									getJTextArea().setText(cl.getSubString(1, (int) cl.length()));
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									LogUtil.logError(e1);
								}
							}
						}
						else if(ld.getType()==Types.CLOB){
							getJTextArea().setText("<暂不提供对BLOB的读写功能>");
						}
					}
					else{
						getJTextArea().setText(getJTextField().getText());
						userObject = getJTextField().getText();
					}
					Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
					getJDialog().setBounds(ds.width*3/7,ds.height/7,ds.width*3/7,ds.height*4/7);
					getJDialog().setVisible(true);
				}
			});
		}
		return jButton;
	}

	  protected void fireEditingCanceled() {
	    ChangeEvent ce = new ChangeEvent(this);
	    for (int i = listeners.size() - 1; i >= 0; i--) {
	      ((CellEditorListener) listeners.elementAt(i)).editingCanceled(ce);
	    }
	  }

	  protected void fireEditingStopped() {
	    ChangeEvent ce = new ChangeEvent(this);
	    for (int i = listeners.size() - 1; i >= 0; i--) {
	      ((CellEditorListener) listeners.elementAt(i)).editingStopped(ce);
	    }
	   }

	public void setCol(int col) {
		this.col = col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setSf(SqlExecuteWindow sf) {
		this.sf = sf;
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getJDialog() {
		if (jDialog == null) {
			jDialog = new JDialog(Frame.getInstance(),true);
			jDialog.setSize(new Dimension(156, 129));
			jDialog.setContentPane(getJContentPane());
			jDialog.setTitle("大数据编辑器");
			jDialog.setVisible(false);
			jDialog.addWindowListener(new WindowListener(){

				public void windowActivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void windowClosed(WindowEvent arg0) {
					// TODO Auto-generated method stub
				}

				public void windowClosing(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void windowDeactivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					if(!getJTextArea().getText().equals(getJTextField().getText())){
						if(userObject instanceof LOBData){
							LOBData ld = (LOBData) userObject;
							if(ld.getType()==Types.CLOB){
								Clob cl = (Clob) ld.getData();
								ld.setValue(getJTextArea().getText());
								try {
//									cl.truncate(1);
//									Writer out = cl.setCharacterStream(1);
//									out.write(getJTextArea().getText());
//									cl.setString(1,getJTextArea().getText());
//									out.flush();
//									out.close();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									LogUtil.logError(e1);
//								} catch (IOException e1) {
//									// TODO Auto-generated catch block
//									LogUtil.logError(e1);
								}
							}
						}
						else{
							getJTextField().setText(getJTextArea().getText());
						}
						change = true;
						stopCellEditing();
					}
				}

				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}});
		}
		return jDialog;
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
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
		}
		return jTextArea;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
