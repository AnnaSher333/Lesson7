package Ex3;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class RockPaperScissorsGame {
    public static void main(String[] args) {
        File pluginsDir = new File("/Users/annasheremet/IdeaProjects/Lesson7/src/Ex3");
        List<PlayableRockPaperScissors> plugins = loadPlugins(pluginsDir);

        PlayableRockPaperScissors currentChampion = null;

        for (PlayableRockPaperScissors plugin : plugins) {
            System.out.println("Загружается: " + plugin.getClass().getSimpleName());
            if (currentChampion == null) {
                currentChampion = plugin;
                System.out.println(currentChampion.getClass().getSimpleName() + " стал чемпионом.");
                continue;
            }

            int currentWins = 0;
            int championWins = 0;

            for (int i = 0; i < 3; i++) {
                RockPaperScissorsEnum currentPlay = currentChampion.play();
                RockPaperScissorsEnum newPlay = plugin.play();
                System.out.println(currentChampion.getClass().getSimpleName() + " выбирает: " + currentPlay);
                System.out.println(plugin.getClass().getSimpleName() + " выбирает: " + newPlay);

                if (currentPlay == newPlay) {
                    System.out.println("Ничья!");
                } else if (isCurrentChampion(currentPlay, newPlay)) {
                    System.out.println(currentChampion.getClass().getSimpleName() + " выигрывает!");
                    currentWins++;
                } else {
                    System.out.println(plugin.getClass().getSimpleName() + " выигрывает!");
                    championWins++;
                }
            }

            if (currentWins > championWins) {
                System.out.println(currentChampion.getClass().getSimpleName() + " остается чемпионом.");
            } else {
                System.out.println(plugin.getClass().getSimpleName() + " стал новым чемпионом!");
                currentChampion = plugin;
            }
        }

        System.out.println("Победитель: " + currentChampion.getClass().getSimpleName());
    }

    private static boolean isCurrentChampion(RockPaperScissorsEnum current, RockPaperScissorsEnum opponent) {
        return (current == RockPaperScissorsEnum.ROCK && opponent == RockPaperScissorsEnum.SCISSORS) ||
                (current == RockPaperScissorsEnum.SCISSORS && opponent == RockPaperScissorsEnum.PAPER) ||
                (current == RockPaperScissorsEnum.PAPER && opponent == RockPaperScissorsEnum.ROCK);
    }

    private static List<PlayableRockPaperScissors> loadPlugins(File pluginsDir) {
        List<PlayableRockPaperScissors> plugins = new ArrayList<>();
        File[] pluginFiles = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (pluginFiles != null) {
            for (File file : pluginFiles) {
                try {
                    URL[] urls = {file.toURI().toURL()};
                    URLClassLoader loader = new URLClassLoader(urls);
                    ServiceLoader<PlayableRockPaperScissors> serviceLoader = ServiceLoader.load(PlayableRockPaperScissors.class, loader);
                    for (PlayableRockPaperScissors plugin : serviceLoader) {
                        plugins.add(plugin);
                    }
                    loader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return plugins;
    }
}
