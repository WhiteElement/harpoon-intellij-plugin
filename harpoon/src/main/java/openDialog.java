import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class openDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
       //Messages.showMessageDialog( String.format("ATTACHED FILES:\n%s", mgr.toString(anActionEvent.getProject().getBasePath())), "Harpoon",
        //        Messages.getInformationIcon()) ;
        


        
        var model = new DefaultTableModel(4, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        
        // TODO insert files into table
        table.setValueAt("test1", 0, 0);
        table.setValueAt("test2", 1, 0);
        table.setValueAt("test3", 2, 0);
        table.setValueAt("test4", 3, 0);

        var parentFrame = WindowManager.getInstance().getFrame(anActionEvent.getProject());
        // Create the dialog containing the table
        JDialog dialog = new JDialog(parentFrame, "Table Dialog", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parentFrame); // Center relative to the parent frame

        // Display the dialog
        dialog.setVisible(true);
    } 
    
}
