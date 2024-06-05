import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.util.Optional;

public class openFile extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
       if (anActionEvent.getProject() == null) 
           return;
       
       var inputEvent = anActionEvent.getInputEvent();
       if (inputEvent instanceof KeyEvent) {
           var keyEvent = (KeyEvent) inputEvent;
           var code = keyEvent.getKeyCode();

           var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);

           Optional<VirtualFile> file = Optional.empty();

           switch (code) {
               case KeyEvent.VK_H: {
                   file = mgr.getFile(0);
                   break;
               }
               case KeyEvent.VK_J: {
                   file = mgr.getFile(1);
                   break;
               }
               case KeyEvent.VK_K: {
                   file = mgr.getFile(2);
                   break;
               }
               case KeyEvent.VK_L: {
                   file = mgr.getFile(3);
                   break;
               }
               
           }

           file.ifPresent(virtualFile -> {
               FileEditorManager.getInstance(anActionEvent.getProject()).openFile(virtualFile, true); 
           });
       }
    }
}
