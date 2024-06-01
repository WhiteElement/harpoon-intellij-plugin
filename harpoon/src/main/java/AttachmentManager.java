import com.ibm.icu.impl.Pair;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;
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

    public String toString(String projectBasePath) {
       var sb = new StringBuilder();
      
       // "1. "
       for(int i=0 ; i < attachedFiles.length; i++){
           sb.append(i+1);
           sb.append(". ");
           
           // "---"
           if(attachedFiles[i] == null) {
               sb.append("---\n");
               continue;
           }
           
           // Path from absolute -> project relative
           String filePath = attachedFiles[i].getPath();
           if (projectBasePath != null && filePath.startsWith(projectBasePath)) {
               filePath = filePath.substring(projectBasePath.length() + 1);
           }
           
           String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
           filePath = filePath.replace(fileName, "");
               
           // <em>src/main</em>
           sb.append("<em>");
           sb.append(filePath);
           sb.append("</em>");
           
           // <b>module.xml</b>
           sb.append("<b>");
           sb.append(fileName);
           sb.append("</b>");
           
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
