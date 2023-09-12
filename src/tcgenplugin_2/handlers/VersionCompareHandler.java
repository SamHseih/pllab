package tcgenplugin_2.handlers;

import java.awt.EventQueue;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import version_control.VersionCompareWindow;

public class VersionCompareHandler extends AbstractHandler{

	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VersionCompareWindow window = new VersionCompareWindow();
					//CurrentProjectName = getCurrentFileRealPath().getProject().getName();
					//window.setSpecInfo(CurrentProjectName, "");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		
		return null;
	}
}
