package sourcecoded.core.core.launch.hooks.deps;

import javax.swing.*;
import java.awt.*;

public class DownloadGUI extends JPanel {

    JProgressBar progress;
    JFrame parent;
    JLabel text;
    boolean started = false;

    public DownloadGUI(int depCount, JFrame parent) {
        this.setLayout(null);
        progress = new JProgressBar(0, depCount);
        progress.setValue(0);
        progress.setStringPainted(true);

        progress.setBounds(5, 30, 450, 50);

        text = new JLabel("SourceCodedCore has some setup to do before Minecraft can start loading.");
        text.setBounds(5, 5, 450, 20);

        add(progress);
        add(text);

        this.parent = parent;
        this.setPreferredSize(new Dimension(450, 80));
    }

    public static DownloadGUI createAndShowGUI(int depCount) {
        JFrame frame = new JFrame("Downloading Dependencies");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        DownloadGUI pane = new DownloadGUI(depCount, frame);
        pane.setOpaque(true);
        frame.setContentPane(pane);

        frame.pack();
        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        return pane;
    }

    public void transition(String name) {
        if (started)
            progress.setValue(progress.getValue() + 1);
        else
            started = true;

        progress.setString("Downloading: " + name);
    }

    public void finish() {
        progress.setValue(progress.getMaximum());
        parent.dispose();
    }

}