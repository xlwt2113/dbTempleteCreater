package com.ssj.jdbcfront.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JTableUtil {

	public static void setTableHeaderHeight(JTable jTable,int height) {
		DefaultTableCellRenderer render = (DefaultTableCellRenderer) jTable.getTableHeader().getDefaultRenderer();
		Dimension d = render.getSize();
		d.height = height;
		render.setPreferredSize(d);
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-15  下午08:05:49
	* 功能描述: 修改表格的指定列的默认显示和编辑控件，目前只支持int和boolean
	* 方法的参数和返回值: 
	* @param jTable
	* @param column
	* @param type
	*/
	@SuppressWarnings("unchecked")
	public static void setTableColumnEditorAndRender(JTable jTable,int column,Class type){
		TableColumn   aColumn   =   jTable.getColumnModel().getColumn(column);   
		aColumn.setCellEditor(jTable.getDefaultEditor(type));   
		aColumn.setCellRenderer(jTable.getDefaultRenderer(type));
	}

	public static Dimension fixTableColumnWidth(JTable table) {
		int MaxWidth = 0;
		int width = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn col = table.getColumn(table.getColumnName(i));
			width = getPreferredWidthForColumn(table, col);
			width = Math.min(350, width);
			if(col.getWidth()<width)
				col.setPreferredWidth(width);
			MaxWidth += col.getWidth();
		}
		return table.getSize();
	}

	public static int getPreferredWidthForColumn(JTable table, TableColumn col) {
		int hw = columnHeaderWidth(table, col), // hw = header width
		cw = widestCellInColumn(table, col); // cw = column width

		return hw > cw ? hw : cw;
	}

	private static int columnHeaderWidth(JTable table, TableColumn col) {
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = new DefaultTableCellRenderer();
			((DefaultTableCellRenderer) renderer).setText(col.getHeaderValue()
					.toString());
		}
		Component comp = renderer.getTableCellRendererComponent(table, col
				.getHeaderValue(), false, false, 0, 0);

		return comp.getPreferredSize().width + 2;
	}

	private static int widestCellInColumn(JTable table, TableColumn col) {
		int c = col.getModelIndex(), width = 0, maxw = 0;

		for (int r = 0; r < table.getRowCount(); ++r) {
			TableCellRenderer renderer = table.getCellRenderer(r, c);

			Component comp = renderer.getTableCellRendererComponent(table,
					table.getValueAt(r, c), false, false, r, c);

			width = comp.getPreferredSize().width + 2;
			maxw = width > maxw ? width : maxw;
		}
		return maxw;
	}
}
