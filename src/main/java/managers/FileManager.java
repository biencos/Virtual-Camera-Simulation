package managers;

import models.Line;
import models.Point;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public ArrayList<Line> readJSONFile(String fileName) {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(fileName);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);

            JSONArray figures = (JSONArray) obj;

            figures.forEach(f -> {
                JSONObject figure = (JSONObject) f;
                JSONArray pointsArray = (JSONArray) figure.get("points");

                for (int i = 0; i < pointsArray.size(); i++) {
                    JSONArray point = (JSONArray) pointsArray.get(i);
                    if (point.size() != 3)
                        throw new IllegalStateException("Error!\n3D point has only 3 coordinates!\n");
                    int x = Integer.parseInt(point.get(0).toString()), y = Integer.parseInt(point.get(1).toString()), z = Integer.parseInt(point.get(2).toString());
                    points.add(new Point(x, y, z));
                }

                JSONArray edgesArray = (JSONArray) figure.get("edges");
                for (int i = 0; i < edgesArray.size(); i++) {
                    JSONArray edge = (JSONArray) edgesArray.get(i);
                    if (edge.size() != 2)
                        throw new IllegalStateException("Error!\nEdge can only have two points!\n");

                    int a = Integer.parseInt(edge.get(0).toString()), b = Integer.parseInt(edge.get(1).toString());
                    if (a >= 0 && a < points.size() && b >= 0 && b < points.size()) {
                        Line line = new Line(points.get(a), points.get(b));
                        lines.add(line);
                    } else
                        throw new IllegalStateException("Error!\nEdge can only have two points!\n");
                }
            });

        } catch (FileNotFoundException e) {
            System.out.println("File with such name doesn't exist!");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("File has incorrect format!");
            System.exit(-1);
        } catch (ParseException e) {
            System.out.println("File has incorrect JSON format!");
            System.exit(-1);
        }
        return lines;
    }
}
