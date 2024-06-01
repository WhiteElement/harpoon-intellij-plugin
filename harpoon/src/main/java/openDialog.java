import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class openDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
       Messages.showMessageDialog( String.format("ATTACHED FILES:\n%s", mgr.toString(anActionEvent.getProject().getBasePath())), "Harpoon", Messages.getInformationIcon()) ;
       
    }
}
