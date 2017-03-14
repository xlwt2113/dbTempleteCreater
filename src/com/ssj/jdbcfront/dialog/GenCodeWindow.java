package com.ssj.jdbcfront.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.ssj.jdbcfront.frame.Frame;
import com.ssj.jdbcfront.model.GenCodeModel;
import com.ssj.jdbcfront.template.Engine;
import com.ssj.jdbcfront.util.SystemUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.file.FileCallback;
import com.ssj.util.file.FileTools;

public class GenCodeWindow extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Frame frame;
	private GenCodeWindow _this;

	private JPanel jContentPane = null;
	private JPanel pnlBottom = null;
	private JButton btnGenCode = null;
	private JButton btnClose = null;
	private JTabbedPane jTabbedPane = null;
	private List<GenCodeChildPanel> childPanes = new ArrayList<GenCodeChildPanel>();

	private JLabel jLabel = null;

	private JComboBox charsetCombo = null;

	private JLabel jLabel1 = null;

	/**
	 * This is the xxx default constructor
	 */
	public GenCodeWindow(String text) {
		this(new String[]{text});
	}

	public GenCodeWindow(String[] texts) {
		super("批量生成代码",true,true,true,true);
		_this = this;
		frame = Frame.getInstance();
		initialize();
		for(String text:texts){
			GenCodeChildPanel pnl = new GenCodeChildPanel(text);
			pnl.setParWin(this);
			childPanes.add(pnl);
			jTabbedPane.addTab(text, pnl);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(768, 430);
		this.setFrameIcon(new ImageIcon(getClass().getResource("/com/ssj/jdbcfront/img/icon_favourites.gif")));
		this.setVisible(true);
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
			jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
			jContentPane.add(getPnlBottom(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes pnlBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlBottom() {
		if (pnlBottom == null) {
			jLabel1 = new JLabel();
			jLabel1.setText(" ");
			jLabel1.setPreferredSize(new Dimension(50, 18));
			jLabel = new JLabel();
			jLabel.setText("文件编码");
			pnlBottom = new JPanel();
			pnlBottom.setLayout(new FlowLayout());
			pnlBottom.add(jLabel, null);
			pnlBottom.add(getJComboBox(), null);
			pnlBottom.add(jLabel1, null);
			pnlBottom.add(getBtnGenCode(), null);
//			pnlBottom.add(getBtnPreview(), null);
			pnlBottom.add(getBtnClose(), null);
		}
		return pnlBottom;
	}

	/**
	 * This method initializes btnGenCode	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnGenCode() {
		if (btnGenCode == null) {
			btnGenCode = new JButton();
			btnGenCode.setText("生成代码");
			btnGenCode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Thread.currentThread().setName("GenCodeThread");
					final StringBuffer msg = new StringBuffer();
					String txtTargetPath = null;
					final int[] cnt = new int[]{0};
					String base = null;
					String tbase = null;
					final VelocityContext context = new VelocityContext();
					ArrayList<GenCodeModel> models = new ArrayList<GenCodeModel>();
					final HashSet<String> sumFiles = new HashSet<String>();
					for(int i=0;i<jTabbedPane.getTabCount();i++){
						GenCodeChildPanel pnl = (GenCodeChildPanel)jTabbedPane.getComponent(i);
						GenCodeModel model = null;
						try {
							model = pnl.getModel();
						} catch (Throwable e1) {
							e1.printStackTrace();
							jTabbedPane.setSelectedComponent(pnl);
							StringWriter buff = new StringWriter();
							PrintWriter out = new PrintWriter(buff);
							e1.printStackTrace(out);
							out.flush();
							Frame.showMsg(buff.toString());
							return;
						}
						if(txtTargetPath==null){
							txtTargetPath = model.getTargetPath();
						}
						final VelocityEngine ve = Engine.getVelocityEngine(model.getTemplatePath(),charsetCombo.getSelectedItem().toString());
						context.put("model",model);
						models.add(model);
//						Properties p = System.getProperties();
//						for(Object key:p.keySet()){
//							System.out.println(key+"\t=\t"+p.get(key));
//						}
						final File root = new File(model.getTemplatePath());
						if(tbase==null){
							tbase = model.getTargetPath()+"/"+model.getClassPre()+(jTabbedPane.getTabCount()>1?"~":"")+"/";
						}
						final String targetBase = tbase;
						final Pattern pat = Pattern.compile(".+\\..+");
						base = targetBase;
						FileTools.travelFolder(root, new FileCallback(){
							public void dealFile(File f) {
								if(f.isDirectory())
									return;
								String path = f.getAbsolutePath().substring(root.getAbsolutePath().length()+1).replace("\\", "/");
								if(path.contains("${models}")){
									sumFiles.add(path);
								}
								try {
									String charSet = charsetCombo.getSelectedItem().toString();
									Template t = ve.getTemplate(path,charSet);
									String newPath = Engine.merge(ve, context, path,charSet);
									File to = new File(targetBase+newPath);
									//如果不符合*.*的文件名格式或有宏没被替换或以.bak为文件名结尾，就不生成该文件
									if(!pat.matcher(to.getName()).matches()||to.getName().contains("$")||to.getName().toLowerCase().endsWith(".bak")){
										return;
									}
									if(!to.getParentFile().exists()){
										to.getParentFile().mkdirs();
									}
									StringWriter sw = new StringWriter();
									PrintStream out = new PrintStream(new FileOutputStream(to),false,charSet );
									t.merge(context, sw);
									sw.flush();
									out.print(sw.toString());
									out.flush();
									out.close();
									cnt[0]++;
								} catch (Exception e) {
									msg.append("\n").append(e.getMessage());
									LogUtil.logError(e);
								}
							}},new FileFilter(){
								public boolean accept(File file) {
									return !(file.isDirectory()&&".svn".equals(file.getName()));
								}});
					}
					if(sumFiles.size()>0){
						context.put("models",models);
						for(String path:sumFiles){
							String charSet = charsetCombo.getSelectedItem().toString();
							VelocityEngine ve = Engine.getVelocityEngine(models.get(0).getTemplatePath(),charsetCombo.getSelectedItem().toString());
							Template t = ve.getTemplate(path,charSet);
							String newPath = Engine.merge(ve, context, path.replace("${models}", ""),charSet);
							File to = new File(tbase+newPath);
							if(!to.getParentFile().exists()){
								to.getParentFile().mkdirs();
							}
							StringWriter sw = new StringWriter();
							PrintStream out;
							try {
								out = new PrintStream(new FileOutputStream(to),false,charSet );
								t.merge(context, sw);
								sw.flush();
								out.print(sw.toString());
								out.flush();
								out.close();
							} catch (Exception e1) {
								LogUtil.logError(e1);
							}
							cnt[0]++;
						}
					}
					if(msg.length()>0){
						Frame.showMsg("生成代码期间发生以下错误："+msg);
					}
					if(Frame.confirm("共生成"+cnt[0]+"个文件，是否现在打开文件夹进行查看？", "提示", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
						if(jTabbedPane.getTabCount()>1){
							SystemUtil.openFolder(txtTargetPath);
						}
						else{
							SystemUtil.openFolder(base);
						}
					}
				}
			});
		}
		return btnGenCode;
	}

	/**
	 * This method initializes btnClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setText("关闭");
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					_this.dispose();
				}
			});
		}
		return btnClose;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox() {
		if (charsetCombo == null) {
			charsetCombo = new JComboBox();
			charsetCombo.setPreferredSize(new Dimension(70, 20));
			charsetCombo.addItem("UTF-8");
			charsetCombo.addItem("GBK");
			charsetCombo.addItem("GB2312");
			charsetCombo.addItem("ISO-8859-1");
		}
		return charsetCombo;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
