package librarysystem;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import com.sun.jdi.event.EventQueue;

import business.LoginException;
import business.SystemController;
import dataaccess.Auth;

public class LoginPanel implements MessageableWindow, LibWindow {
	public static final LibWindow INSTANCE = new LoginPanel();
	LibrarySystem libSystem;

	public void setLibSystem(LibrarySystem sys) {
		libSystem = sys;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private JPanel mainPanel;
	private JPanel upperHalf;
	private JPanel middleHalf;
	private JPanel lowerHalf;
	private JPanel container;

	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel lowerPanel;
	private JPanel lowerlowr;
	private JPanel leftTextPanel;
	private JPanel rightTextPanel;

	private JTextField username;
	private JTextField password;
	private JLabel label;
	private JButton loginButton;
	private JButton logoutButton;
	private SystemController s;

	public LoginPanel() {

		mainPanel = new JPanel();
		defineUpperHalf();
		defineMiddleHalf();
		defineLowerHalf();
		BorderLayout bl = new BorderLayout();
		bl.setVgap(50);
		mainPanel.setLayout(bl);

		mainPanel.add(upperHalf, BorderLayout.NORTH);
		mainPanel.add(middleHalf, BorderLayout.CENTER);
		mainPanel.add(lowerHalf, BorderLayout.SOUTH);

	}

	private void defineLowerHalf() {
		lowerHalf = new JPanel();
		lowerHalf.setLayout(new BorderLayout());
		defineLogoutlebelPanal();
		defineLogoutButtonPanel();
		lowerHalf.add(lowerPanel, BorderLayout.NORTH);
		lowerHalf.add(lowerlowr, BorderLayout.CENTER);
	}

	private void defineLogoutButtonPanel() {
		lowerlowr = new JPanel();
		lowerlowr.setLayout(new FlowLayout());

		logoutButton = new JButton("Logout");
		addActionLlistner(logoutButton);
		lowerlowr.add(logoutButton, FlowLayout.LEFT);

	}

	private void addActionLlistner(JButton loginButton2) {
		loginButton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			 SystemController.currentAuth=null;
			 username.setText("");
			 password.setText("");
			 s.displayInfo("Successfully LogOut!!");
			 logoutList();
			 libSystem.repaint();
				
			}
		});
		
	}

	private void defineLogoutlebelPanal() {
		lowerPanel = new JPanel();

		JLabel loginLabel = new JLabel("Logout");
		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);

		lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lowerPanel.add(loginLabel);

	}

	private void defineUpperHalf() {

		upperHalf = new JPanel();
		upperHalf.setLayout(new BorderLayout());
		defineTopPanel();
		defineMiddlePanel();
		defineLowerPanel();
		upperHalf.add(topPanel, BorderLayout.NORTH);
		upperHalf.add(middlePanel, BorderLayout.CENTER);
		upperHalf.add(lowerPanel, BorderLayout.SOUTH);

	}

	private void defineMiddleHalf() {
		middleHalf = new JPanel();
		middleHalf.setLayout(new BorderLayout());
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		//middleHalf.add(Box.createRigidArea(new Dimension(0,50)));
		middleHalf.add(s, BorderLayout.SOUTH);

	}

	private void defineTopPanel() {
		topPanel = new JPanel();

		JLabel loginLabel = new JLabel("Login");
		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);

		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(loginLabel);

	}

	private void defineMiddlePanel() {
		middlePanel = new JPanel();
		middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		defineLeftTextPanel();
		defineRightTextPanel();
		middlePanel.add(leftTextPanel);
		middlePanel.add(rightTextPanel);
	}

	private void defineLowerPanel() {
		lowerPanel = new JPanel();
		loginButton = new JButton("Login");
		addLoginButtonListener(loginButton);
		lowerPanel.add(loginButton);
	}

	private void defineLeftTextPanel() {

		JPanel topText = new JPanel();
		JPanel bottomText = new JPanel();
		topText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		username = new JTextField(10);
		label = new JLabel("Username");
		label.setFont(Util.makeSmallFont(label.getFont()));
		topText.add(username);
		bottomText.add(label);

		leftTextPanel = new JPanel();
		leftTextPanel.setLayout(new BorderLayout());
		leftTextPanel.add(topText, BorderLayout.NORTH);
		leftTextPanel.add(bottomText, BorderLayout.CENTER);
	}

	private void defineRightTextPanel() {

		JPanel topText = new JPanel();
		JPanel bottomText = new JPanel();
		topText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		password = new JPasswordField(10);
		label = new JLabel("Password");
		label.setFont(Util.makeSmallFont(label.getFont()));
		topText.add(password);
		bottomText.add(label);

		rightTextPanel = new JPanel();
		rightTextPanel.setLayout(new BorderLayout());
		rightTextPanel.add(topText, BorderLayout.NORTH);
		rightTextPanel.add(bottomText, BorderLayout.CENTER);
	}

	private void addLoginButtonListener(JButton butn) {
		butn.addActionListener(evt -> {
			 s = new SystemController();
			try {
				s.login(username.getText(), password.getText());
				updateLeftPanel(SystemController.currentAuth);
				libSystem.repaint();
				
			} catch (LoginException e) {
				// TODO Auto-generated catch block
				System.out.println("Error Loggingin: " + e.getMessage());
//				JOptionPane.showMessageDialog(this,e.getMessage());
//				e.printStackTrace();
			}
		});
	}

	private void updateLeftPanel(Auth auth) {
		if (auth == Auth.ADMIN)
			adminItems();
		else if (auth == Auth.LIBRARIAN)
			libraryItems();
		else if (auth == Auth.BOTH)
			bothItems();

	}

	private void adminItems() {
		ListItem[] adminItems = libSystem.adminItems();
		updateList(adminItems);
	}

	private void libraryItems() {
		ListItem[] librarianItems = libSystem.libListItems();
		updateList(librarianItems);
	}

	private void bothItems() {
		updateList(null);
	}
	private void logoutList() {
		updateLoggedoutList(libSystem.bothItem);
	}

	@SuppressWarnings("unchecked")
	private void updateList(ListItem[] items) {
		JList<ListItem> linkList = libSystem.getLinkList();
		DefaultListModel<ListItem> model = (DefaultListModel) linkList.getModel();
		int size = model.getSize();
		if (items != null) {
			java.util.List<Integer> indices = new ArrayList<>();
			for (ListItem item : items) {
				int index = model.indexOf(item);
				indices.add(index);
				ListItem next = (ListItem) model.get(index);
				next.setHighlight(true);

			}
			for (int i = 0; i < size; ++i) {
				if (!indices.contains(i)) {
					ListItem next = (ListItem) model.get(i);
					next.setHighlight(false);
				}
			}
		} else {
			for (int i = 0; i < size; ++i) {
				ListItem next = (ListItem) model.get(i);
				next.setHighlight(true);
			}

		}
	}
	public void updateLoggedoutList(ListItem[] item) {
		JList<ListItem> linkList = libSystem.getLinkList();
		DefaultListModel<ListItem> model = (DefaultListModel) linkList.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; ++i) {
			ListItem next = (ListItem) model.get(i);
			if (!next.getItemName().equals("Login/Logout")) {
				next.setHighlight(false);
			} else {
				next.setHighlight(true);
			}
		}
	}

//	@Override
//	public void updateData() {
//		// nothing to do
//		
//	}

	private static final long serialVersionUID = 3618976789175941432L;

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void isInitialized(boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub

	}

}
