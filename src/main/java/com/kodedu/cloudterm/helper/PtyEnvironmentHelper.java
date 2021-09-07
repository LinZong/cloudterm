package com.kodedu.cloudterm.helper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PtyEnvironmentHelper {

    public static void prepare() throws IOException {
        String userHome = System.getProperty("user.home");
        Path dataDir = Paths.get(userHome).resolve(".terminalfx");
        prepare(dataDir);
    }

    public static void prepare(Path location) throws IOException {
        IOHelper.copyLibPty(location);
    }
}
