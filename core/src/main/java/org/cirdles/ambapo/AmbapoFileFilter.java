/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.ambapo;

import java.io.File;

/**
 *
 * @author evc1996
 */
public class AmbapoFileFilter extends javax.swing.filechooser.FileFilter {
    
    @Override
    public boolean accept(File f) {
        boolean accept = f.isDirectory();

        if (!accept) {
            String suffix = getSuffix(f);

            if (suffix != null) {
                accept = suffix.equalsIgnoreCase("csv");
            }
        }
        return accept;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Conversion files (*.csv)";
    }

    private String getSuffix(File f) {
        String s = f.getPath(), suffix = null;
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            suffix = s.substring(i + 1).toLowerCase();
        }

        return suffix;
    }

    
}
