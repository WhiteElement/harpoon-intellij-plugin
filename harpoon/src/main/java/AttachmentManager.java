import com.intellij.openapi.components.Service;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Optional;

@Service
public final class AttachmentManager {
    private final VirtualFile[] attachedFiles = new VirtualFile[4];
    
   public Optional<VirtualFile> getFile(int idx) {
        try {
            return Optional.ofNullable(attachedFiles[idx]);
        } catch (Exception e) {
            return Optional.empty();
        }
   }
   
    public void setFile(VirtualFile file) {
       
    } 
    
    public void appendFile(VirtualFile file) throws Exception {
      int idx = firstNotEmptyIdx();
      
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

    public String toString() {
       var sb = new StringBuilder();
       
       for(var af : attachedFiles){
           sb.append(af);
            sb.append("\n");
       }
       
       return sb.toString();
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
}
