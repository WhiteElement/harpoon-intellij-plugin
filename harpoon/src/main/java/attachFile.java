import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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
        
        if (files.length == 0) {
            Notifications.Bus.notifyAndHide(new Notification("MyGroup","No File selected to attach", NotificationType.WARNING), proj);
            return;
        };

        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
        try {
            mgr.appendFile(files[0]);
        } catch (Exception e) {
            System.out.println(e); 
        }

    }
}
