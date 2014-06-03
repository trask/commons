/**
 * Copyright (C) 2014 Telenor Digital AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comoyo.emjar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Joiner;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class EmJarClassLoaderTest extends EmJarTest
{
    @Test
    public void testClassPathQuoting()
        throws Exception
    {
        final FilenameFilter jarFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            };
        final File bundle = getResourceFile("bundle-s-large.jar");
        final File allJars[] = bundle.getParentFile().listFiles(jarFilter);

        final Properties props = new Properties();
        props.setProperty("java.class.path", Joiner.on(":").join(allJars));
        props.setProperty("path.separator", ":");
        props.setProperty("file.separator", "/");
        props.setProperty("user.dir", "/tmp");
        final EmJarClassLoader loader = new EmJarClassLoader(props);
        final InputStream is = loader.getResourceAsStream("entry-" + WEIRD + ".txt");
        final BufferedReader entry = new BufferedReader(new InputStreamReader(is));
        assertEquals("Contents mismatch for weird entry", WEIRD, entry.readLine());
    }
}