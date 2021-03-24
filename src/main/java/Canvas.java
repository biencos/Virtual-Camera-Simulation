import models.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Canvas extends JComponent {
    private ArrayList<Line> lines;
    private Color backgroundColor = new Color(0, 3, 127);
    private Color linesColor = new Color(38, 255, 0, 255);
    private int lineWidth = 3;

    public Canvas() {
        super();
        this.lines = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(this.backgroundColor);

        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics.setColor(this.linesColor);

        graphics.setStroke(new BasicStroke(lineWidth));
        //graphics.draw(new Line2D.Float(30, 20, 80, 90));
        for (Line line : this.lines)
            graphics.draw(new Line2D.Float((int) line.a.x + this.getWidth() / 2, (int) line.a.y + this.getHeight() / 2,
                    (int) line.b.x + this.getWidth() / 2, (int) line.b.y + this.getHeight() / 2));
//            graphics.drawLine((int) line.a.x + this.getWidth() / 2, (int) line.a.y + this.getHeight() / 2,
//                    (int) line.b.x + this.getWidth() / 2, (int) line.b.y + this.getHeight() / 2);

    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
