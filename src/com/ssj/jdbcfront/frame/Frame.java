package com.ssj.jdbcfront.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jvnet.substance.SubstanceDefaultTreeCellRenderer;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.dao.DbUtil;
import com.ssj.jdbcfront.dialog.ConsoleWindow;
import com.ssj.jdbcfront.dialog.ExportWindow;
import com.ssj.jdbcfront.dialog.GenCodeWindow;
import com.ssj.jdbcfront.dialog.ImportWindow;
import com.ssj.jdbcfront.dialog.LoginDia;
import com.ssj.jdbcfront.dialog.SqlExecuteWindow;
import com.ssj.jdbcfront.dialog.TableEditWindow;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.swing.Lodding;
import com.ssj.jdbcfront.util.ConfigUtil;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class Frame extends JFrame {

	private static final long serialVersionUID = -2227559327209189646L;

	static Map<String,String> skinMap;
	
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	private JMenuItem menuLogin = null;
	public JTree jTree = null;
	public JDesktopPane jDesktopPane = null;
	public LoginDia loginDia;
	public ConsoleWindow console;
	
	private int desktopWinPos;
	
	public DataBaseView curDb;
	public DaoAccess dao;  //  @jve:decl-index=0:
	public Connection mainCon;

	private static Frame instance;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JMenuItem menuNewSqlWin = null;
	private JMenuItem menuOpenSql = null;
	private JMenu jMenu1 = null;
	private JPopupMenu menuTables = null;
	private JMenuItem menuEditTable = null;
	private JMenuItem menuQuerySql = null;
	private JMenuItem menuEditSql = null;
	private JPopupMenu menuObjectRoot = null;
	private JMenuItem menuNewTable = null;
	private JMenuItem menuRefresh = null;
	private JMenuItem menuGenCode = null;
	private JMenuItem menuDeleteObject = null;
	private JMenu jMenu2 = null;
	private JMenuItem jCheckBoxMenuItem = null;

	private JMenuItem miExport = null;

	private JMenu jMenu3 = null;

	private JMenuItem jMenuItem = null;

	private JMenuItem jMenuItem1 = null;

	private JMenuItem menuDeleteData = null;

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem2() {
		if (miExport == null) {
			miExport = new JMenuItem();
			miExport.setText("导出对象");
			miExport.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					exportDbObj();
				}
			});
		}
		return miExport;
	}

	public ExportWindow openExportWindow(Collection<DbObject> objs) {
		ExportWindow sf = new ExportWindow(objs);
		sf.setVisible(true);
		getJDesktopPane().add(sf);
		sf.setLocation(getChildLocation());
		getJDesktopPane().getDesktopManager().activateFrame(sf);
		return sf;
	}

	/**
	 * This method initializes jMenu3	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu3() {
		if (jMenu3 == null) {
			jMenu3 = new JMenu();
			jMenu3.setText("工具");
			jMenu3.add(getJMenuItem1());
			jMenu3.add(getJMenuItem3());
		}
		return jMenu3;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem3() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("导入文件");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ImportWindow sf = new ImportWindow();
					getJDesktopPane().add(sf);
					sf.setLocation(getChildLocation());
					getJDesktopPane().getDesktopManager().activateFrame(sf);
					sf.setVisible(true);
				}
			});
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("导出对象");
			jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					exportDbObj();
				}
			});
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem22() {
		if (menuDeleteData == null) {
			menuDeleteData = new JMenuItem();
			menuDeleteData.setText("删除数据");
			menuDeleteData.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					new Thread(){
						public void run(){
							for(int i=0;paths!=null&&i<paths.length;i++){
								DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
								Object obj = node.getUserObject();
								if(obj instanceof DbObject){
									DbObject dbo = (DbObject) obj;
									if("TABLE".equals(dbo.getType().toUpperCase())){
										String msg = "是否要删除表"+dbo.getTypeName()+"["+dbo.getName()+"]中的数据吗？";
										if(confirm(msg,"确认删除",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
											DbUtil.executeSqlUpdate("delete from "+dbo.getName());
										}
									}
								}
							}
						}
					}.start();
				}
			});
		}
		return menuDeleteData;
	}

	/**
	 * 开发人：宋帅杰
	 * 开发日期: 2009-4-4  下午06:45:14
	 * 功能描述: 
	 * 方法的参数和返回值: 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length>0){
			ConfigUtil.ROOT_PATH = args[0];
		}
		setStyle();
		System.setProperty("java.awt.im.style","on-the-spot");
		Frame thisClass = Frame.getInstance();
		thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thisClass.setVisible(true);
		thisClass.loginDia.setVisible(true);
	}

	/**
	 * This method initializes menuNewSqlWin	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuNewSqlWin() {
		if (menuNewSqlWin == null) {
			menuNewSqlWin = new JMenuItem();
			menuNewSqlWin.setText("新SQL窗口");
			menuNewSqlWin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openSqlFrame("");
				}
			});
		}
		return menuNewSqlWin;
	}

	/**
	 * This method initializes menuOpenSql	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuOpenSql() {
		if (menuOpenSql == null) {
			menuOpenSql = new JMenuItem();
			menuOpenSql.setText("打开SQL文件");
			menuOpenSql.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return menuOpenSql;
	}

	/**
	 * This method initializes jMenu1	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu1() {
		if (jMenu1 == null) {
			jMenu1 = new JMenu();
			jMenu1.setText("皮肤");
			skinMap = ConfigUtil.getSkinMap();
			ActionListener skinal = new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					try {
						String skin = ((JMenuItem)e.getSource()).getText();
						String skinClass = skinMap.get(skin);
						UIManager.setLookAndFeel(skinClass);
						instance.getRootPane().updateUI();
						SwingUtilities.updateComponentTreeUI(instance);
//						jTree.getRootPane().updateUI();
//						SwingUtilities.updateComponentTreeUI(jTree);
						ConfigUtil.setString("userTheme", skin);
					} catch (UnsupportedLookAndFeelException e1) {
						showMsg("不支持此种皮肤");
						LogUtil.logError(e1);
					} catch (ClassNotFoundException e1) {
						showMsg("找不到此种皮肤对应的类");
						LogUtil.logError(e1);
					} catch (InstantiationException e1) {
						
						LogUtil.logError(e1);
					} catch (IllegalAccessException e1) {
						
						LogUtil.logError(e1);
					}
				}};
			ButtonGroup buttonGroup=new ButtonGroup();
			for(String skin:skinMap.keySet()){
				JRadioButtonMenuItem item = new JRadioButtonMenuItem(skin);
				item.addActionListener(skinal);
				item.setSelected(skin.equals(ConfigUtil.getString("userTheme")));
				jMenu1.add(item);
				buttonGroup.add(item);
			}
		}
		return jMenu1;
	}

	/**
	 * This method initializes menuGenCode	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuGenCode() {
		if (menuGenCode == null) {
			menuGenCode = new JMenuItem();
			menuGenCode.setText("生成代码");
			menuGenCode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					List<String> list = new ArrayList<String>();
					for(int i=0;paths!=null&&i<paths.length;i++){
						Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						if(obj instanceof DbObject){
							DbObject dbo = (DbObject)obj;
							if(dbo.isCanSelect()){
								list.add(dbo.getName());
							}
						}
					}
					genCode(list.toArray(new String[list.size()]));
				}
			});
		}
		return menuGenCode;
	}

	/**
	 * This method initializes menuDeleteObject	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuDeleteObject() {
		if (menuDeleteObject == null) {
			menuDeleteObject = new JMenuItem();
			menuDeleteObject.setText("删除对象");
			menuDeleteObject.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					new Thread(){
						public void run(){
							for(int i=0;paths!=null&&i<paths.length;i++){
								DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
								Object obj = node.getUserObject();
								if(obj instanceof DbObject){
									DbObject dbo = (DbObject) obj;
									String msg = "是否要删除"+dbo.getTypeName()+"["+dbo.getName()+"]吗？";
									if("TABLE".equals(dbo.getType().toUpperCase())){
										msg = "删除后表中的数据将全部丢失，您确定要删除表["+dbo.getName()+"]吗？";
									}
									if(confirm(msg,"确认删除",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
										DbUtil.executeSqlUpdate("drop "+dbo.getType()+" "+dbo.getName());
										((DefaultTreeModel)jTree.getModel()).removeNodeFromParent(node);
									}
									ConfigUtil.removeRecentObject(dbo);
								}
							}
						}
					}.start();
				}
			});
		}
		return menuDeleteObject;
	}

	/**
	 * This method initializes jMenu2	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu2() {
		if (jMenu2 == null) {
			jMenu2 = new JMenu();
			jMenu2.setText("视图");
			jMenu2.add(getJMenuItem());
		}
		return jMenu2;
	}

	/**
	 * This method initializes jCheckBoxMenuItem	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JMenuItem getJMenuItem() {
		if (jCheckBoxMenuItem == null) {
			jCheckBoxMenuItem = new JMenuItem();
			jCheckBoxMenuItem.setText("控制台");
			jCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					console.setVisible(!console.isVisible());
				}
			});
		}
		return jCheckBoxMenuItem;
	}

	private static void setStyle() {
		try {
			skinMap = ConfigUtil.getSkinMap();
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			String theme = ConfigUtil.getString("userTheme");
			if(StringUtil.isBlank(theme)){
				theme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			}
			else{
				theme = skinMap.get(theme);
			}
			UIManager.setLookAndFeel(theme);
			String fname = ConfigUtil.getString("defaultFont");
			if(StringUtil.isBlank(fname)){
				fname = "\u5b8b\u4f53";
			}
			Font font = new Font(fname,Font.PLAIN,12);
			String[] fontCtr = ("Frame,InternalFrame,ToolTip,Table,TableHeader,TextField,ComboBox,TextField,PasswordField,TextArea," +
					"TextPane,EditorPane,FormattedTextField,Button,CheckBox,RadioButton,ToggleButton,ProgressBar,DesktopIcon," +
					"TitledBorder,Label,List,TabbedPane,MenuBar,Menu,MenuItem,PopupMenu,CheckBoxMenuItem,RadioButtonMenuItem," +
					"Spinner,Tree,ToolBar").split(",");
			for(String fc:fontCtr){
				UIManager.put(fc+".font",font);   
			}
			UIManager.put("OptionPane.messageFont",font);   
			UIManager.put("OptionPane.buttonFont",font);
		} catch (Exception e) {
			
			LogUtil.logError(e);
		}
	}
	
	public static Frame getInstance(){
		if(instance==null){
			instance = new Frame();
			Dimension scr=Toolkit.getDefaultToolkit().getScreenSize();
			instance.setSize(scr.width-100, scr.height-80);
			instance.setLocation(50,10);
		}
		return instance;
	}

	/**
	 * This is the default constructor
	 */
	public Frame() {
		super();
		instance = this;
		initialize();
		console = new ConsoleWindow();
		getJDesktopPane().add(console);
		this.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent e) {
			}
			public void windowClosed(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
//				new Thread(){
//					public void run(){
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException e) {
//							
//							LogUtil.logError(e);
//						}
//						DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) getJTree().getModel().getRoot();
//						for(int i=0;i<treeRoot.getChildCount();i++){
//							getJTree().expandPath(new TreePath(treeRoot.getChildAt(i)));
//						}
//					}
//				}.start();
			}});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/ssj/jdbcfront/img/page_package.gif")));
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("数据库工具");
		this.loginDia = getLoginDia();
		
	}
	
	private LoginDia getLoginDia(){
		return new LoginDia(this);
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
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setBounds(new Rectangle(3, 46, 537, 222));
			jSplitPane.setPreferredSize(new Dimension(53, 3));
			jSplitPane.setDividerLocation(203);
			jSplitPane.setDividerSize(6);
			jSplitPane.setRightComponent(getJDesktopPane());
			jSplitPane.setLeftComponent(getJScrollPane());
		}
		return jSplitPane;
	}
	
	private JScrollPane getJScrollPane(){
		return new JScrollPane(getJTree());
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
			jJMenuBar.add(getJMenu3());
			jJMenuBar.add(getJMenu2());
			jJMenuBar.add(getJMenu1());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("文件");
			jMenu.add(getMenuLogin());
			jMenu.add(getMenuNewSqlWin());
			jMenu.add(getMenuOpenSql());
			JMenuItem item = new JMenuItem("退出");
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}});
			jMenu.add(item);
		}
		return jMenu;
	}

	/**
	 * This method initializes menuLogin	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuLogin() {
		if (menuLogin == null) {
			menuLogin = new JMenuItem();
			menuLogin.setText("登录");
			menuLogin.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					Frame.getInstance().loginDia.setVisible(true);
				}
			
			});
		}
		return menuLogin;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	public JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree();
			jTree.setToggleClickCount(1);
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("database");
			top.add(new DefaultMutableTreeNode("尚未登录"));
			DefaultTreeModel m_model = new DefaultTreeModel(top);
			jTree.setModel(m_model);
			jTree.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					JTree tree = (JTree) e.getSource();
					TreePath tp = tree.getPathForLocation(e.getX(),e.getY());
					if(tp==null)
						return;
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
					Object obj = node.getUserObject();
					//按住Ctrl时可进行多选
					if(e.isControlDown()){
						if(!tree.isPathSelected(tp)){
							tree.addSelectionPath(tp);
						}
					}
					//双击
					if(e.getClickCount()>1){
						if(tp.getPath().length>2 && obj instanceof DbObject){
							DaoAccess da = curDb.getDbType().getDao();
							DbObject dbo = (DbObject) obj;
							ConfigUtil.addRecentObject(dbo);
							if("TABLE".equals(dbo.getType())){
								openSqlFrame(da.getEditQuerySql(dbo.getName()),true,true);
							}
							else if("VIEW".equals(dbo.getType())){
								openSqlFrame("select t.* from "+dbo.getName()+" t").executeSql();
							}
							else{
								openSqlFrame(curDb.getDbType().getDao().getObjectSql(dbo.getName())).setTitle("编辑"+dbo.getTypeName()+"："+dbo.getName());
							}
						}
					}
					//super.mouseClicked(e);
				}

				public void mouseReleased(MouseEvent e) {
					JTree tree = (JTree) e.getSource();
					if(e.isPopupTrigger()){
						Object obj = tree.getPathForLocation(e.getX(),e.getY());
						if(obj instanceof TreePath){
							if(!tree.isPathSelected((TreePath) obj))
								tree.setSelectionPath((TreePath) obj);
						}
						Object uo = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject();
						if(uo instanceof DbObject){//如果是数据库对象
							boolean canQuery = ((DbObject)uo).isCanSelect();
							getMenuQuerySql().setVisible(canQuery);
							getMenuEditSql().setVisible(canQuery);
							getMenuTables().show(e.getComponent(),e.getX(),e.getY());
						}
						else{
							String type = uo.toString().toUpperCase();
							getMenuNewTable().setVisible(type.indexOf("TABLE")>=0);
							getMenuObjectRoot().show(e.getComponent(),e.getX(),e.getY());
						}
					}
				}
				
			});
			jTree.addTreeExpansionListener(new TreeExpansionListener(){

				public void treeCollapsed(TreeExpansionEvent event) {
				}

				public void treeExpanded(TreeExpansionEvent event) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
					DefaultMutableTreeNode flag = (DefaultMutableTreeNode) node.getFirstChild();
					Object obj = flag.getUserObject();
					if (!(obj instanceof Lodding))
						return;
					reLoadTreeNode(node);
				}
				
			});
			jTree.setCellRenderer(new SubstanceDefaultTreeCellRenderer(){
				private static final long serialVersionUID = 53857653828592746L;
				private HashMap<String,ImageIcon> map = new HashMap<String,ImageIcon>();
				public Component getTreeCellRendererComponent(JTree tree, 
						Object value, boolean sel, boolean expanded, boolean leaf, 
						int row, boolean hasFocus) {
					SubstanceDefaultTreeCellRenderer com = (SubstanceDefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					Object obj = ((DefaultMutableTreeNode)value).getUserObject();
					if(obj instanceof DbObject){
						DbObject dbo = (DbObject) obj;
						com.setIcon(getImageIcon(dbo.getType()));
					}
					else if(obj instanceof String){
						com.setIcon(getImageIcon("TREE_FLODER"+(expanded?"_OPEN":"")));
					}
					return com; 
				}
				private ImageIcon getImageIcon(String type){
					if(map.get(type)!=null){
						return map.get(type);
					}
					URL url = this.getClass().getClassLoader().getResource("com/ssj/jdbcfront/img/ico/"+type+".gif");
					map.put(type, new ImageIcon(url));
					return map.get(type);
				}
			});
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			jTree.setShowsRootHandles(true);
			jTree.setEditable(false);
			jTree.setRootVisible(false);

		}
		return jTree;
	}

	/**
	 * This method initializes jDesktopPane	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 */
	public JDesktopPane getJDesktopPane() {
		if (jDesktopPane == null) {
			jDesktopPane = new JDesktopPane();
		}
		return jDesktopPane;
	}

	public SqlExecuteWindow openSqlFrame(String sql){
		return openSqlFrame(sql,false,false);
	}

	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-28  下午06:44:20
	* 功能描述: 打开一个SQL查询窗口
	* 方法的参数和返回值: 
	* @param sql
	* @return
	*/
	public SqlExecuteWindow openSqlFrame(String sql,boolean execNow,boolean edit){
		for(JInternalFrame jif:getJDesktopPane().getAllFrames()){
			if(jif instanceof SqlExecuteWindow){
				SqlExecuteWindow sf = (SqlExecuteWindow) jif;
				if(sql.equals(sf.sql)){
					jif.toFront();
					getJDesktopPane().getDesktopManager().activateFrame(sf);
					return (SqlExecuteWindow) jif;
				}
			}
		}
		SqlExecuteWindow sf = new SqlExecuteWindow(sql);
		sf.setVisible(true);
		getJDesktopPane().add(sf);
		sf.setLocation(getChildLocation());
		getJDesktopPane().getDesktopManager().activateFrame(sf);
		if(execNow){
			sf.executeSql(edit);
		}
		return sf;
	}
	
	private Point getChildLocation(){
		Point p = new Point(desktopWinPos/2, desktopWinPos);
		desktopWinPos += 30;
		if(desktopWinPos>getJDesktopPane().getHeight()/2)
			desktopWinPos = 10;
		return p;
	}

	public TableEditWindow editTable(String name){
		for(JInternalFrame jif:getJDesktopPane().getAllFrames()){
			if(jif instanceof TableEditWindow){
				TableEditWindow sf = (TableEditWindow) jif;
				if(sf.getDefTableName().equals(name)){
					jif.toFront();
					jif.requestFocusInWindow();
//					getJDesktopPane().getDesktopManager().activateFrame(jif);
					return (TableEditWindow) jif;
				}
			}
		}
		TableEditWindow sf = new TableEditWindow(name);
		sf.setVisible(true);
		getJDesktopPane().add(sf);
		sf.setLocation(getChildLocation());
		getJDesktopPane().getDesktopManager().activateFrame(sf);
		return sf;
	}

	public GenCodeWindow genCode(String name){
		return genCode(new String[]{name});
	}

	public GenCodeWindow genCode(String[] texts){
		GenCodeWindow sf = new GenCodeWindow(texts);
		sf.setVisible(true);
		getJDesktopPane().add(sf);
		sf.setLocation(getChildLocation());
		getJDesktopPane().getDesktopManager().activateFrame(sf);
		return sf;
	}

	public void initDbObjects(){
		try {
			if(mainCon==null||mainCon.isClosed())
				return;
			dao = curDb.getDbType().getDao();
			dao.setDbv(curDb);
			List<String> type = dao.getObjectTypeList();
			type.add(0, "最近访问");
			final DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) getJTree().getModel().getRoot();
			treeRoot .removeAllChildren();
			for(int i=0;i<type.size();i++){
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(type.get(i));
				node.add(new DefaultMutableTreeNode(Lodding.lodding));
				treeRoot.add(node);
			}
			Runnable runnable = new Runnable() {
				public void run() {
					((DefaultTreeModel)jTree.getModel()).reload(treeRoot);
				}
			};
			SwingUtilities.invokeLater(runnable);
		} catch (SQLException e) {
			LogUtil.logError(e);
		}
	}
	
	public static void showMsg(String msg){
		JOptionPane.showMessageDialog(Frame.getInstance(),msg);
	}

	public static int confirm(String msg,String title,int type){
		return JOptionPane.showConfirmDialog(Frame.getInstance(),msg,title,type);
	}

	/**
	 * This method initializes menuObjectRoot	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	private JPopupMenu getMenuObjectRoot() {
		if (menuObjectRoot == null) {
			menuObjectRoot = new JPopupMenu();
			menuObjectRoot.add(getMenuRefresh());
		}
		menuObjectRoot.add(getMenuNewTable(),0);
		return menuObjectRoot;
	}

	/**
	 * This method initializes menuNewTable	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuNewTable() {
		if (menuNewTable == null) {
			menuNewTable = new JMenuItem();
			menuNewTable.setText("新建表");
			menuNewTable.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					editTable("");
				}
			});
		}
		return menuNewTable;
	}

	/**
	 * This method initializes menuRefresh	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuRefresh() {
		if (menuRefresh == null) {
			menuRefresh = new JMenuItem();
			menuRefresh.setText("刷新");
			menuRefresh.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = getJTree().getSelectionPaths();
					for(int i=0;paths!=null&&i<paths.length;i++){
						Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						if(obj instanceof String){
							reLoadTreeNode((DefaultMutableTreeNode)paths[i].getLastPathComponent());
						}
					}
				}});
		}
		return menuRefresh;
	}

	/**
	 * This method initializes menuTables	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	JPopupMenu getMenuTables() {
		if (menuTables == null) {
			menuTables = new JPopupMenu();
			menuTables.setSize(500,600);
			menuTables.add(getMenuEditTable());
			menuTables.add(getMenuDeleteObject());
			menuTables.add(getJMenuItem22());
			menuTables.add(getMenuQuerySql());
			menuTables.add(getMenuEditSql());
			menuTables.add(getMenuGenCode());
			menuTables.add(getJMenuItem2());
		}
		menuTables.add(getMenuNewTable(),0);
		return menuTables;
	}

	/**
	 * This method initializes menuEditSql	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuEditSql() {
		if (menuEditSql == null) {
			menuEditSql = new JMenuItem();
			menuEditSql.setText("编辑数据");
			menuEditSql.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			menuEditSql.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					for(int i=0;paths!=null&&i<paths.length;i++){
						Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						if(obj instanceof DbObject){
							DaoAccess da = curDb.getDbType().getDao();
							openSqlFrame(da.getEditQuerySql(((DbObject) obj).getName()),true,true);
							ConfigUtil.addRecentObject((DbObject) obj);
						}
					}
				}
			});
		}
		return menuEditSql;
	}

	/**
	 * This method initializes menuQuerySql	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuQuerySql() {
		if (menuQuerySql == null) {
			menuQuerySql = new JMenuItem();
			menuQuerySql.setText("查询数据");
			menuQuerySql.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			menuQuerySql.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					for(int i=0;paths!=null&&i<paths.length;i++){
						Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						if(obj instanceof DbObject){
							DbObject dbo = (DbObject) obj;
							if(dbo.isCanSelect()){
								DaoAccess da = curDb.getDbType().getDao();
								openSqlFrame(da.getSearchQuerySql(((DbObject) obj).getName()),true,false);
								ConfigUtil.addRecentObject(dbo);
							}
						}
					}
				}
			});
		}
		return menuQuerySql;
	}

	/**
	 * This method initializes menuEditTable	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMenuEditTable() {
		if (menuEditTable == null) {
			menuEditTable = new JMenuItem();
			menuEditTable.setText("编辑");
			menuEditTable.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
			menuEditTable.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
					for(int i=0;paths!=null&&i<paths.length;i++){
						Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
						if(obj instanceof DbObject){
							DbObject dbo = (DbObject) obj;
							if("TABLE".equals(dbo.getType().toUpperCase())){
								editTable(dbo.getName());
							}
							else{
								openSqlFrame(curDb.getDbType().getDao().getObjectSql(dbo.getName()));
							}
							ConfigUtil.addRecentObject(dbo);
						}
					}
				}
			});
		}
		return menuEditTable;
	}

	private void reLoadTreeNode(final DefaultMutableTreeNode node) {
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				try {
					if(mainCon==null||mainCon.isClosed())
						return;
					DaoAccess dao = curDb.getDbType().getDao();
					List<DbObject> list = null;
					if("最近访问".equals(node.getUserObject())){
						list = ConfigUtil.getRecentObjects();
					}
					else{
						list = dao.getObjectList(node.getUserObject().toString());
					}
					node.removeAllChildren();
					for(int i=0;i<list.size();i++){
						node.add(new DefaultMutableTreeNode(list.get(i)));
					}
					Runnable runnable = new Runnable() {
						public void run() {
							DefaultTreeModel model = (DefaultTreeModel) getJTree().getModel();
							model.reload(node);
						}
					};
					SwingUtilities.invokeLater(runnable);
				} catch (SQLException e) {
					
					LogUtil.logError(e);
				}
			}

		});
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-12  上午10:49:50
	* 功能描述: 刷新某一类数据库对象
	* 方法的参数和返回值: 
	* @param type
	*/
	public void refreshDbType(String type){
		DefaultTreeModel model = (DefaultTreeModel)jTree.getModel();
		if(model.getRoot()==null)
			return;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		for(int i=0;i<root.getChildCount();i++){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			if(node!=null&&node.getUserObject().toString().toUpperCase().indexOf(type)>=0){
				reLoadTreeNode(node);
			}
		}
	}
	
	/**
	 * 更新最近访问对象列表
	 */
	public void refreshRecentObject(){
		DefaultTreeModel model = (DefaultTreeModel)jTree.getModel();
		if(model.getRoot()==null)
			return;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)model.getChild(model.getRoot(),0);
		if(node!=null){
			reLoadTreeNode(node);
		}
	}

	private void exportDbObj() {
		TreePath[] paths = Frame.getInstance().getJTree().getSelectionPaths();
		List<DbObject> objs = new ArrayList<DbObject>();
		for(int i=0;paths!=null&&i<paths.length;i++){
			Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
			if(obj instanceof DbObject){
				objs.add((DbObject) obj);
			}
		}
		instance.openExportWindow(objs);
	}

}
