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
import java.awt.event.KeyEvent;

public class openDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
        
        var model = new DefaultTableModel(4, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };

        JTable table = new JBTable(model);
        table.setTableHeader(null);

        var inputmap = table.getInputMap(JComponent.WHEN_FOCUSED);
        var actionmap = table.getActionMap();

        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "NextRow");
        actionmap.put("NextRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var idx = table.getSelectedRow();
                if (idx < 3) {
                    idx++;
                    table.setRowSelectionInterval(idx, idx);
                }
            }
        });

        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "PrevRow");
        actionmap.put("PrevRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var idx = table.getSelectedRow();
                if (idx > 0) {
                    idx--;
                    table.setRowSelectionInterval(idx, idx);
                }
            }
        });


        var parentFrame = WindowManager.getInstance().getFrame(anActionEvent.getProject());
        JDialog dialog = new JDialog(parentFrame, "Attached Files", true);
        
        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Quit");
        actionmap.put("Quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke('d'), "StartDD");
        actionmap.put("StartDD", new AbstractAction() {
            private int dCount = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                dCount++;
                if (dCount == 2) {
                    dCount = 0;
                    int selectedRow = table.getSelectedRow();
                    mgr.setCachedFile(selectedRow);
                    mgr.getFiles()[selectedRow] = null;
                    table.setValueAt(String.format("%s null",selectedRow + 1), selectedRow, 0);
                }
            }
        }); 
        
        inputmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "PasteFile");
        actionmap.put("PasteFile", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                var cachedFile = mgr.setCachedFileAt(selectedRow);
                table.setValueAt(String.format("%s %s", selectedRow +1, mgr.formatFile(cachedFile)), selectedRow, 0);
            }
        });
        
        var files = mgr.getFiles();
        var projectPath = anActionEvent.getProject();
        if (projectPath == null) {
           return; 
        }
        mgr.setProjectPath(projectPath.getBasePath());
        table.setValueAt("1 " + mgr.formatFile(files[0]), 0, 0);
        table.setValueAt("2 " + mgr.formatFile(files[1]), 1, 0);
        table.setValueAt("3 " + mgr.formatFile(files[2]), 2, 0);
        table.setValueAt("4 " + mgr.formatFile(files[3]), 3, 0);
        table.setRowSelectionInterval(0,0);

        
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parentFrame); // Center relative to the parent frame

        dialog.setVisible(true);
    } 
    
}
