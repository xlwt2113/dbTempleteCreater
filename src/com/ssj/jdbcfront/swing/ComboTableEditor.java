package com.ssj.jdbcfront.swing;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import com.ssj.util.StringUtil;

public class ComboTableEditor extends JComboBox implements TableCellEditor {

	private static final long serialVersionUID = 6127034294333708423L;

	  protected transient Vector<CellEditorListener> listeners = new Vector<CellEditorListener>();

	  protected transient int originalValue;

	  protected transient boolean editing;

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		int i=0;
		for(i=0;i<this.getItemCount();i++){
			if(StringUtil.isEqual(value,this.getItemAt(i))){
				break;
			}
		}
		if(i>=this.getItemCount()){
			this.insertItemAt(value,0);
		}
		this.setSelectedIndex(0);
		if(value!=null)
			try {
				this.setSelectedItem(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		this.setEditable(true);
		this.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println(e.getItem());
			}
		});
		return this;
	}

	public void addCellEditorListener(CellEditorListener l) {
		listeners.addElement(l);
	}

	public void cancelCellEditing() {
	    ChangeEvent ce = new ChangeEvent(this);
	    for (int i = listeners.size() - 1; i >= 0; i--) {
	      ((CellEditorListener) listeners.elementAt(i)).editingCanceled(ce);
	    }
	    editing = false;
	}

	public Object getCellEditorValue() {
		Object val = this.getEditor().getItem();
		return val==null?"":val;
	}

	@Override
	public Object getSelectedItem() {
		Object obj = super.getSelectedItem();
		return obj==null?"":obj;
	}

	@Override
	public void setSelectedItem(Object anObject) {
		try {
			super.setSelectedItem(anObject);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void configureEditor(ComboBoxEditor anEditor, Object anItem) {
		try {
			super.configureEditor(anEditor, anItem);
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
	    ChangeEvent ce = new ChangeEvent(this);
	    for (int i = listeners.size() - 1; i >= 0; i--) {
	      ((CellEditorListener) listeners.elementAt(i)).editingStopped(ce);
	    }
		return true;
	}

}
