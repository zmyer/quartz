package org.quartz.osgi;

import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;

import java.util.List;

/**
 * Adds {@link BundleClassLoaderHelper} to a {@link CascadingClassLoadHelper}.
 *
 * Created by jhouse on 4/16/17.
 *
 * @see org.quartz.simpl.CascadingClassLoadHelper
 * @see BundleClassLoaderHelper
 */
public class OsgiCascadingClassLoadHelper extends CascadingClassLoadHelper {

    @Override
    protected void addAditionalLoadHelpers(List<ClassLoadHelper> loadHelpers) {
        loadHelpers.add(new BundleClassLoaderHelper());
    }

}
