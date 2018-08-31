import javafx.util.Pair;
import java.util.List;
import java.util.ArrayList;

void CreateMap(float gridSizeX, float gridSizeY) {
  List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> gridDots = new ArrayList<>();
  float xSpacing = width / gridSizeX, ySpacing = height / gridSizeY;
  for (int y = 0; y < gridSizeY; y++) {
    for (int x = 0; x < gridSizeX; x++) {
      Pair<Integer, Integer> location = new Pair<>(x, y);
      Pair<Integer, Integer> coordinates = new Pair<>(x * xSpacing, y * ySpacing);
    }
  }
}
