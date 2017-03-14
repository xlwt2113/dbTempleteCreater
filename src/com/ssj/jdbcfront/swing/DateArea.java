package com.ssj.jdbcfront.swing;

/**
 * <p>Title: OpenSwing</p>
 * <p>Description: JDateField 日期输入框</p>
 *                 确保输入正确的日期
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author <a href="mailto:sunkingxie@hotmail.com">SunKing</a>
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sunking.swing.OpenSwingUtil;
import com.sunking.swing.refer.JDateDocument;

public class DateArea
    extends JTextArea
    implements Serializable {
    /**
     * 年月日格式
     */
    public static final SimpleDateFormat dateFormat
        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JDateDocument doc = new JDateDocument(this,dateFormat);
    public DateArea() {
        setPreferredSize(new Dimension(100, 25));
        setDocument(doc);
    }

    /**
     * 取得日期
     * @return Date
     */
    public Date getDate() {
        try {
            return dateFormat.parse(this.getText());
        }
        catch (Exception ex) {
            return new Date();
        }
    }

    /**
     * 设置日期
     * @return Date
     */
    public void setDate(Date date) {
        try {
            dateFormat.format(date);
        }
        catch (Exception ex) {
            dateFormat.format(new Date());
        }
    }

    /**
     * 测试
     * @param args String[]
     */
    public static void main(String[] args) {
        JFrame frame = OpenSwingUtil.createDemoFrame("JDateField Demo");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new DateArea(), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}