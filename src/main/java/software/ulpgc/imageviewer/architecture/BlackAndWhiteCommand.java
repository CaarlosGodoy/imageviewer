package software.ulpgc.imageviewer.architecture;

import software.ulpgc.imageviewer.application.gui.SwingImageDisplay;

public class BlackAndWhiteCommand implements Command {
    private final SwingImageDisplay display;
    private boolean active = false;

    public BlackAndWhiteCommand(SwingImageDisplay display) {
        this.display = display;
    }

    @Override
    public void execute() {
        active = !active;
        display.setBlackAndWhite(active);
    }
}