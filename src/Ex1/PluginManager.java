package Ex1;

import com.sun.source.util.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PluginManager {
    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) {
        try {
            // Создаем полный путь к плагину
            String path = pluginRootDirectory + File.separator + pluginName;
            File pluginDirectory = new File(path);
            if (!pluginDirectory.exists() || !pluginDirectory.isDirectory()) {
                throw new IllegalArgumentException("Дириктория плагина не существует: " + path);
            }

            // Создание URLClassLoader для загрузки плагинов
            URL[] urls = {pluginDirectory.toURI().toURL()}; //путь к каталогу плагина
            try (URLClassLoader classLoader = new URLClassLoader(urls)) {
                // Загружаем класс плагина
                Class<?> pluginClass = classLoader.loadClass(pluginClassName);
                // Проверяем, реализует ли класс интерфейс Plugin
                if (!Plugin.class.isAssignableFrom(pluginClass)) {
                    throw new IllegalArgumentException("Класс " + pluginClassName + " не реализует интерфейс Plugin!");
                }

                // Создаем экземпляр плагина
                return (Plugin) pluginClass.getDeclaredConstructor().newInstance();
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Ошибка загрузки плагина: " + pluginName, e);
        }
    }
}

