package com.ssj.jdbcfront.demo;
/*
 * 01/13/2009
 *
 * DemoRootPane.java - Root pane for the demo applet and standalone application.
 * Copyright (C) 2009 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.text.TextAction;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.autocomplete.VariableCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.ToolTipSupplier;

import com.ssj.util.LogUtil;


/**
 * The root pane for the demo application.  This is all code shared between
 * the applet and standalone versions of the demo.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DemoRootPane extends JRootPane {

	private static final long serialVersionUID = 1L;
	private RSyntaxTextArea textArea;
	private AutoCompletion ac;
	private JCheckBoxMenuItem cellRenderingItem;
	private JCheckBoxMenuItem showDescWindowItem;
	private JCheckBoxMenuItem paramAssistanceItem;
	private JEditorPane ep;


	/**
	 * Constructor.
	 */
	public DemoRootPane() {
		this(null);
	}


	/**
	 * Constructor.
	 *
	 * @param provider The completion provider for the editor to use.
	 */
	public DemoRootPane(CompletionProvider provider) {

		JPanel contentPane = new JPanel(new BorderLayout());

		ep = new JEditorPane("text/html", null);
		updateEditorPane(); // Nimbus doesn't always clean up after itself.
		contentPane.add(ep, BorderLayout.NORTH);

		textArea = new RSyntaxTextArea(25, 40);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);

		if (provider==null) {
			provider = createCompletionProvider();
		}

		// Install auto-completion onto our text area.
		ac = new AutoCompletion(provider);
		ac.setListCellRenderer(new CCellRenderer());
		ac.setShowDescWindow(true);
		ac.setParameterAssistanceEnabled(true);
		ac.install(textArea);
		contentPane.add(new RTextScrollPane(textArea, true));

		textArea.setToolTipSupplier((ToolTipSupplier)provider);
		ToolTipManager.sharedInstance().registerComponent(textArea);

		setJMenuBar(createMenuBar());

		// Get ready to go.
		setContentPane(contentPane);

		// Put the focus into the text area, not the "label" JEditorPane.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.requestFocusInWindow();
			}
		});

	}


	/**
	 * Returns the provider to use when editing code.
	 *
	 * @return The provider.
	 * @see #createCommentCompletionProvider()
	 * @see #createStringCompletionProvider()
	 */
	private CompletionProvider createCodeCompletionProvider() {

		// Add completions for the C standard library.
		DefaultCompletionProvider cp = new DefaultCompletionProvider();

		// First try loading resource (running from demo jar), then try
		// accessing file (debugging in Eclipse).
		ClassLoader cl = getClass().getClassLoader();
		InputStream in = cl.getResourceAsStream("c.xml");
		try {
			if (in!=null) {
				cp.loadFromXML(in);
				in.close();
			}
			else {
				cp.loadFromXML(new File("c.xml"));
			}
		} catch (IOException ioe) {
			LogUtil.logError(ioe);
		}

		// Add some handy shorthand completions.
		cp.addCompletion(new ShorthandCompletion(cp, "main",
							"int main(int argc, char **argv)"));

		return cp;

	}


	/**
	 * Returns the provider to use when in a comment.
	 *
	 * @return The provider.
	 * @see #createCodeCompletionProvider()
	 * @see #createStringCompletionProvider()
	 */
	private CompletionProvider createCommentCompletionProvider() {
		DefaultCompletionProvider cp = new DefaultCompletionProvider();
		cp.addCompletion(new BasicCompletion(cp, "TODO:", "A to-do reminder"));
		cp.addCompletion(new BasicCompletion(cp, "FIXME:", "A bug that needs to be fixed"));
		return cp;
	}


	/**
	 * Creates the completion provider for a C editor.  This provider can be
	 * shared among multiple editors.
	 *
	 * @return The provider.
	 */
	private CompletionProvider createCompletionProvider() {

		// Create the provider used when typing code.
		CompletionProvider codeCP = createCodeCompletionProvider();

		// The provider used when typing a string.
		CompletionProvider stringCP = createStringCompletionProvider();

		// The provider used when typing a comment.
		CompletionProvider commentCP = createCommentCompletionProvider();

		// Create the "parent" completion provider.
		LanguageAwareCompletionProvider provider = new
								LanguageAwareCompletionProvider(codeCP);
		provider.setStringCompletionProvider(stringCP);
		provider.setCommentCompletionProvider(commentCP);

		return provider;

	}


	/**
	 * Returns the menu bar for the demo application.
	 *
	 * @return The menu bar.
	 */
	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("File");
		Action newAction = new TextAction("New") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				AutoCompleteDemoApp app2 = new AutoCompleteDemoApp(
												ac.getCompletionProvider());
				app2.setVisible(true);
			}
		};
		JMenuItem item = new JMenuItem(newAction);
		menu.add(item);
		mb.add(menu);

		menu = new JMenu("View");
		Action renderAction = new FancyCellRenderingAction();
		cellRenderingItem = new JCheckBoxMenuItem(renderAction);
		cellRenderingItem.setSelected(true);
		menu.add(cellRenderingItem);
		Action descWindowAction = new ShowDescWindowAction();
		showDescWindowItem = new JCheckBoxMenuItem(descWindowAction);
		showDescWindowItem.setSelected(true);
		menu.add(showDescWindowItem);
		Action paramAssistanceAction = new ParameterAssistanceAction();
		paramAssistanceItem = new JCheckBoxMenuItem(paramAssistanceAction);
		paramAssistanceItem.setSelected(true);
		menu.add(paramAssistanceItem);
		mb.add(menu);

		ButtonGroup bg = new ButtonGroup();
		menu = new JMenu("LookAndFeel");
		Action lafAction = new LafAction("System", UIManager.getSystemLookAndFeelClassName());
		JRadioButtonMenuItem rbmi = new JRadioButtonMenuItem(lafAction);
		rbmi.setSelected(true);
		menu.add(rbmi);
		bg.add(rbmi);
		lafAction = new LafAction("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		rbmi = new JRadioButtonMenuItem(lafAction);
		menu.add(rbmi);
		bg.add(rbmi);
		lafAction = new LafAction("Ocean", "javax.swing.plaf.metal.MetalLookAndFeel");
		rbmi = new JRadioButtonMenuItem(lafAction);
		menu.add(rbmi);
		bg.add(rbmi);
		lafAction = new LafAction("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		rbmi = new JRadioButtonMenuItem(lafAction);
		menu.add(rbmi);
		bg.add(rbmi);
		mb.add(menu);

		return mb;

	}


	/**
	 * Returns the completion provider to use when the caret is in a string.
	 *
	 * @return The provider.
	 * @see #createCodeCompletionProvider()
	 * @see #createCommentCompletionProvider()
	 */
	private CompletionProvider createStringCompletionProvider() {
		DefaultCompletionProvider cp = new DefaultCompletionProvider();
		cp.addCompletion(new BasicCompletion(cp, "%c", "char", "Prints a character"));
		cp.addCompletion(new BasicCompletion(cp, "%i", "signed int", "Prints a signed integer"));
		cp.addCompletion(new BasicCompletion(cp, "%f", "float", "Prints a float"));
		cp.addCompletion(new BasicCompletion(cp, "%s", "string", "Prints a string"));
		cp.addCompletion(new BasicCompletion(cp, "%u", "unsigned int", "Prints an unsigned integer"));
		cp.addCompletion(new BasicCompletion(cp, "\\n", "Newline", "Prints a newline"));
		return cp;
	}


	/**
	 * Focuses the text area.
	 */
	public void focusEditor() {
		textArea.requestFocusInWindow();
	}


	/**
	 * Updates the font used in the HTML, as well as the background color, of
	 * the "label" editor pane.  The font would always have to be done (since
	 * HTMLEditorKit doesn't use the editor pane's font by default), but the
	 * background only has to be modified because Nimbus doesn't clean up the
	 * colors it installs after itself.
	 */
	private void updateEditorPane() {
		Font f = UIManager.getFont("Label.font");
		String fontTag = "<body style=\"font-family: " + f.getFamily() +
					"; font-size: " + f.getSize() + "pt; \">";
		String text = "<html>" + fontTag + "" +
			"The text area below provides simple code completion for the C " +
			"programming language as you type. Simply type <b>Ctrl+Space</b> " +
			"at any time to see a list of completion choices (function names, "+
			"for example). If there is only one possible completion, it will " +
			"be automatically inserted.<p>" +
			"Also, completions are context-sensitive.  If you type Ctrl+Space" +
			"in a comment or in the middle of a string, you will get " +
			"different completion choices than if you are in code.";
		ep.setText(text);
		ep.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));
		ep.setEditable(false);
		ep.setBackground(UIManager.getColor("Panel.background"));
	}


	/**
	 * Toggles whether the completion window uses "fancy" rendering.
	 */
	private class FancyCellRenderingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public FancyCellRenderingAction() {
			putValue(NAME, "Fancy Cell Rendering");
		}

		public void actionPerformed(ActionEvent e) {
			boolean fancy = cellRenderingItem.isSelected();
			ac.setListCellRenderer(fancy ? new CCellRenderer() : null);
		}

	}


	/**
	 * Sets a new look and feel.
	 */
	private class LafAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private String laf;

		public LafAction(String name, String laf) {
			putValue(NAME, name);
			this.laf = laf;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				UIManager.setLookAndFeel(laf);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(DemoRootPane.this,
					"Error setting LookAndFeel", "Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			Component parent = SwingUtilities.getRoot(DemoRootPane.this);
			SwingUtilities.updateComponentTreeUI(parent);
			updateEditorPane();
		}

	}


	/**
	 * Toggles whether parameter assistance is enabled.
	 */
	private class ParameterAssistanceAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ParameterAssistanceAction() {
			putValue(NAME, "Function Parameter Assistance");
		}

		public void actionPerformed(ActionEvent e) {
			boolean enabled = paramAssistanceItem.isSelected();
			ac.setParameterAssistanceEnabled(enabled);
		}

	}


	/**
	 * Toggles whether the description window is visible.
	 */
	private class ShowDescWindowAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ShowDescWindowAction() {
			putValue(NAME, "Show Description Window");
		}

		public void actionPerformed(ActionEvent e) {
			boolean show = showDescWindowItem.isSelected();
			ac.setShowDescWindow(show);
		}

	}


}
class CCellRenderer extends CompletionCellRenderer {

	private static final long serialVersionUID = 1L;
	private Icon variableIcon;
	private Icon functionIcon;
	private Icon emptyIcon;


	/**
	 * Constructor.
	 */
	public CCellRenderer() {
		variableIcon = getIcon("img/var.png");
		functionIcon = getIcon("img/function.png");
		emptyIcon = new EmptyIcon(16);
	}


	/**
	 * Returns an icon.
	 *
	 * @param resource The icon to retrieve.  This should either be a file,
	 *        or a resource loadable by the current ClassLoader.
	 * @return The icon.
	 */
	private Icon getIcon(String resource) {
		ClassLoader cl = getClass().getClassLoader();
		URL url = cl.getResource(resource);
		if (url==null) {
			File file = new File(resource);
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException mue) {
				LogUtil.logError(mue); // Never happens
			}
		}
		return url!=null ? new ImageIcon(url) : null;
	}


	/**
	 * {@inheritDoc}
	 */
	protected void prepareForOtherCompletion(JList list,
			Completion c, int index, boolean selected, boolean hasFocus) {
		super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
		setIcon(emptyIcon);
	}


	/**
	 * {@inheritDoc}
	 */
	protected void prepareForVariableCompletion(JList list,
			VariableCompletion vc, int index, boolean selected,
			boolean hasFocus) {
		super.prepareForVariableCompletion(list, vc, index, selected,
										hasFocus);
		setIcon(variableIcon);
	}


	/**
	 * {@inheritDoc}
	 */
	protected void prepareForFunctionCompletion(JList list,
			FunctionCompletion fc, int index, boolean selected,
			boolean hasFocus) {
		super.prepareForFunctionCompletion(list, fc, index, selected,
										hasFocus);
		setIcon(functionIcon);
	}


	/**
	 * An standard icon that doesn't paint anything.  This can be used to take
	 * up an icon's space when no icon is specified.
	 *
	 * @author Robert Futrell
	 * @version 1.0
	 */
	private static class EmptyIcon implements Icon, Serializable {

		private static final long serialVersionUID = 1L;
		private int size;

		public EmptyIcon(int size) {
			this.size = size;
		}

		public int getIconHeight() {
			return size;
		}

		public int getIconWidth() {
			return size;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
		}
		
	}


}