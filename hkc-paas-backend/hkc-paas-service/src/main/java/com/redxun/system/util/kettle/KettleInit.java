package com.redxun.system.util.kettle;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;

import java.io.File;
import java.io.FileFilter;

public class KettleInit {

    public static void init(String pluginPath) throws KettleException {
        KettleEnvironment.init();

        initPlugins(pluginPath);
    }



    /**
     * "D:\\kettleonline\\plugins\\"
     * @param pluginPath
     * @throws KettleException
     */
    public static void initPlugins(String pluginPath) throws KettleException {

        File file=new File(pluginPath);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() == true;
            }
        });

        for (File dir:files ) {
            StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(dir.getAbsolutePath(), false, true));
        }
    }


}
