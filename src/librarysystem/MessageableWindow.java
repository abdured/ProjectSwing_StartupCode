package librarysystem;


import static librarysystem.LibrarySystem.statusBar;
public interface MessageableWindow {
	public default void displayError(String msg) {
		statusBar.setForeground(Util.ERROR_MESSAGE_COLOR);
		statusBar.setText(msg);
	}
	
	public default void displayInfo(String msg) {
	statusBar.setForeground(Util.INFO_MESSAGE_COLOR);
	statusBar.setText(msg);
	}
	
	public void updateData();
}
