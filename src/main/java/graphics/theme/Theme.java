package graphics.theme;

import TextController.TextController;
import graphics.app.Launcher;
import graphics.app.Utility;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

public enum Theme {
    LIGHT, DARK;
    public static Theme currentTheme = loadThemeLocally();

    @Override public String toString(){
        String path;
        if (this.equals(LIGHT)) path = Utility.LIGHT_MODE_CSS_PATH;
        else path = Utility.DARK_MODE_CSS_PATH;
        return Objects.requireNonNull(Launcher.class.getResource
                (path)).toString();
    }

    private static Theme loadThemeLocally(){
        File file;
        try {file = new File(Objects.requireNonNull(Launcher.class.getResource(Utility.THEME_SAVER)).toURI());
        } catch (URISyntaxException e) {
            TextController.println("Failed to load theme.");
            e.printStackTrace();
            throw new ThemeException();
        }
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            TextController.println("Could not locate theme file!");
            e.printStackTrace();
            throw new ThemeException();
        }
        String read;
        try {read = bufferedReader.readLine();
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            TextController.println("Could not read.");
            e.printStackTrace();
            throw new ThemeException();
        }
        if (read.equals(Utility.THEME_SAVED_LIGHT)) return LIGHT;
        else if (read.equals(Utility.THEME_SAVED_DARK)) return DARK;
        throw new ThemeException();
    }

    public void saveTheme(){
        File file;
        try {file = new File(Objects.requireNonNull(Launcher.class.getResource(Utility.THEME_SAVER)).toURI());
        } catch (URISyntaxException e) {
            TextController.println("Failed to load theme.");
            e.printStackTrace();
            throw new ThemeException();
        }
        FileWriter fileWriter;
        try {fileWriter = new FileWriter(file);
        } catch (IOException e) {
            TextController.println("Could not locate theme file!");
            e.printStackTrace();
            throw new ThemeException();
        }
        try {
            if (this == Theme.LIGHT) fileWriter.write(Utility.THEME_SAVED_LIGHT);
            else fileWriter.write(Utility.THEME_SAVED_DARK);
            fileWriter.close();
        } catch (IOException e) {
            TextController.println("Could not write!");
            e.printStackTrace();
            throw new ThemeException();
        }
    }
}

class ThemeException extends RuntimeException{

}
