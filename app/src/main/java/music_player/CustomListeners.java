package music_player;

import android.view.View;

import java.util.concurrent.Callable;

public class CustomListeners {

    public static abstract class CustomOnClickListener implements Callable<Void> {
        public int position;
        public View view;
    }

    public static abstract class CustomOnLongClickListener implements Callable<Void> {
        public int position;
        public View view;
    }

}
