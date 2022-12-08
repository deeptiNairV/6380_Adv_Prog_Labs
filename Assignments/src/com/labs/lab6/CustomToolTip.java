package com.labs.lab6;

import javax.swing.*;
import java.awt.*;

class CustomJToolTip extends JToolTip {

    public CustomJToolTip(JComponent component) {
        super();
        setComponent(component);
        setBackground(Color.black);
        setForeground(Color.cyan);
    }
}
