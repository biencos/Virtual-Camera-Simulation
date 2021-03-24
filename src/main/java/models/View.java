package models;

import java.util.ArrayList;

public class View {
    private ArrayList<Line> lines;

    public View() {
        this.lines = new ArrayList<>();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
