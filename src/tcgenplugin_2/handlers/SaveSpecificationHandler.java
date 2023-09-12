package tcgenplugin_2.handlers;

import java.awt.EventQueue;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import version_control.SaveWindow;


public class SaveSpecificationHandler extends AbstractHandler{
	public static String CurrentProjectPath;
	public static String CurrentProjectName;
	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		CurrentProjectName = getCurrentFileRealPath().getName();
		CurrentProjectPath = getCurrentFileRealPath().getLocation().toOSString();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaveWindow window = new SaveWindow();
					window.setSpecInfo(CurrentProjectName, CurrentProjectPath);
					window.frmSave.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		
		return null;
	}
	
	public static IFile getCurrentFileRealPath(){
        IWorkbench wi = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wi.getActiveWorkbenchWindow();
        IWorkbenchPage page = win.getActivePage();
        if (page != null) {
            IEditorPart editor = page.getActiveEditor();
            if (editor != null) {
                IEditorInput input = editor.getEditorInput();
                if (input instanceof IFileEditorInput) {
//                    return ((IFileEditorInput)input).getFile().getLocation().toOSString();
                    return ((IFileEditorInput)input).getFile();
                }
            }
        }
        return null;
	}
}
