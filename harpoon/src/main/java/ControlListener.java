import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.impl.ProjectUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ControlListener implements AppLifecycleListener {

    private final KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() != KeyEvent.KEY_PRESSED)
                return false;

            boolean isControlDown = (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0;

            if (isControlDown && e.getKeyCode() != KeyEvent.VK_CONTROL) {
                var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);
                Optional<VirtualFile> file = Optional.empty();

                switch (e.getExtendedKeyCode()) {
                    case 69: // E -> open Harpoon Menu
                        openHarpoon();
                        break;
                    case 74: // J -> setFile 1
                        mgr.getFile(0);
                        break;
                    case 75: // K -> setFile 2
                        mgr.getFile(1);
                        break;
                    case 76: // L -> setFile 3
                        mgr.getFile(2);
                        break;
                    case 16777430: // Ö -> setFile 4
                        mgr.getFile(3);
                        break;
                    default:
                        break;
                }

                Project currentProject = Arrays.stream(ProjectManager.getInstance().getOpenProjects()).findFirst().orElseThrow();
                file.ifPresent(virtualFile -> FileEditorManager.getInstance(currentProject).openFile(virtualFile, true));
            }

            return true;
        }
    };

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        AppLifecycleListener.super.appFrameCreated(commandLineArgs);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
    }

    public void openHarpoon() {
        var mgr = ApplicationManager.getApplication().getService(AttachmentManager.class);

        var model = new DefaultTableModel(4, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JBTable(model);

        var inputmap = table.getInputMap(JComponent.WHEN_FOCUSED);
        var actionmap = table.getActionMap();

        inputmap.put(KeyStroke.getKeyStroke('j'), "NextRow");
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

        inputmap.put(KeyStroke.getKeyStroke('k'), "PrevRow");
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


        Project currentProject = ProjectUtil.getActiveProject();
        var parentFrame = WindowManager.getInstance().getFrame(currentProject);
        JDialog dialog = new JDialog(parentFrame, "Table Dialog", true);
        dialog.setType(Window.Type.UTILITY); // This hints the window manager not to tile it
        inputmap.put(KeyStroke.getKeyStroke('q'), "Quit");
        actionmap.put("Quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
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
                    mgr.getFiles()[selectedRow] = null;
                    table.setValueAt(String.format("%s null",selectedRow + 1), selectedRow, 0);
                }
            }
        });

        var files = mgr.getFiles();
        mgr.setProjectPath(currentProject.getBasePath());
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