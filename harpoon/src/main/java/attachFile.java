import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.jetbrains.annotations.NotNull;

public class attachFile extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        var proj = anActionEvent.getProject();
        if (proj == null) 
            return;
        
        var files = FileEditorManager.getInstance(proj).getSelectedFiles();
        
        // No Files selected Message in the future
        if (files.length == 0) 
            return;

        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
        try {
            mgr.appendFile(files[0]);
        } catch (Exception e) {
            System.out.println(e); 
        }

    }
}
