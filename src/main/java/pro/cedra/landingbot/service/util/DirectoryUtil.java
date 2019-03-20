package pro.cedra.landingbot.service.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by TIMUR on 10.12.2017.
 */
public class DirectoryUtil {

    public static File getResource (String resourcePath) {
        URL resource = DirectoryUtil.class.getResource("/" + resourcePath);
        try {
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  Path createAndGet (String pathStr) {
        Path path = Paths.get(pathStr);
        //if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
