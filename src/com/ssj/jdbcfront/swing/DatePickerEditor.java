package com.ssj.jdbcfront.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;

import com.ssj.jdbcfront.dao.LOBData;
import com.ssj.jdbcfront.dialog.SqlExecuteWindow;
import com.ssj.jdbcfront.frame.Frame;

public class DatePickerEditor extends DefaultTableEditor{
	
	private JButton jButton;

	public DatePickerEditor(SqlExecuteWindow sf,int jdbcType) {
		super(sf,jdbcType);
	}

	@Override
	protected JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("...");
			jButton.setPreferredSize(new Dimension(17, 20));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
				}
			});
		}
		return null;
	}

	@Override
	protected JTextArea getJTextField() {
		if(jTextField==null){
			jTextField = new DateArea();
		}
		return jTextField;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
