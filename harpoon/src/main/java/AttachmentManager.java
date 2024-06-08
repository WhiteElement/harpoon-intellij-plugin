import com.intellij.openapi.components.Service;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;
import java.util.Optional;

@Service
public final class AttachmentManager {
    private final VirtualFile[] attachedFiles = new VirtualFile[4];
    private VirtualFile cachedFile = null;
    private String basePath;

    public Optional<VirtualFile> getFile(int idx) {
        try {
            return Optional.ofNullable(attachedFiles[idx]);
        } catch (Exception e) {
            return Optional.empty();
        }
   }
   
    public VirtualFile setCachedFileAt(int idx) {
       this.attachedFiles[idx] = this.cachedFile;
       return this.cachedFile;
    } 
    
    public void appendFile(VirtualFile file) throws Exception {
      int idx = firstNotEmptyIdx();
      
      if(Arrays.asList(attachedFiles).contains(file))
          return;
      
      if(idx != -1) {
          attachedFiles[idx] = file;
      } else {
          throw new Exception("No Empty Slot");
      }
    }
   
   
   public int firstNotEmptyIdx() {
       int idx = 0;
       
       while (idx < attachedFiles.length) {
           if (attachedFiles[idx] == null) 
               return idx;
           
           idx ++;
       }
       
       return -1;
   }

    
   private boolean spotIsEmpty (int idx) {
       try {
           var file = attachedFiles[idx];
           if (file != null) return false;
       } catch (Exception e) {
           return true;
       }
       return true;
   }

    public VirtualFile[] getFiles() {
       return attachedFiles;
    }

    public String formatFile(VirtualFile file) {
        
        if (file == null)
            return "null";
        
        String filePath = file.getPath();
        if (this.basePath != null && filePath.startsWith(basePath)) {
            filePath = filePath.substring(basePath.length() + 1);
        }

        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        filePath = filePath.replace(fileName, "");
        
        return filePath + fileName;
    }

    public void setProjectPath(String basePath) {
       this.basePath = basePath;
    }

    public void setCachedFile(int selectedRow) {
       this.cachedFile = attachedFiles[selectedRow];
    }
    
    public Optional<VirtualFile> getCachedFile() {
        return Optional.ofNullable(this.cachedFile);
    }
}
