package graphics.theme;

import graphics.app.Launcher;
import graphics.app.Utility;

import java.util.Objects;

public enum Theme {
    LIGHT, DARK;
    public static Theme currentTheme = LIGHT;

    @Override public String toString(){
        String path;
        if (this.equals(LIGHT)) path = Utility.LIGHT_MODE_CSS_PATH;
        else path = Utility.DARK_MODE_CSS_PATH;
        return Objects.requireNonNull(Launcher.class.getResource
                (path)).toString();
    }
}
