/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.database;

import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 *
 * @author Fabrian
 */
public class ImageFilter  extends FileFilter{
    

    @Override
    public boolean accept(File file) {
        if(file.isDirectory()){
            return true;
        }
        
        String extension = ExtensionUtil.getExtension(file);
        if (extension != null) {
            if (extension.equals(ExtensionUtil.jpeg)||
                extension.equals(ExtensionUtil.jpg) ||
                extension.equals(ExtensionUtil.png)) {
                    return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Image File(*.jpg,*.jpeg,*.png)";
    }
    
}

