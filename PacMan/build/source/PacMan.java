import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javafx.util.Pair; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PacMan extends PApplet {

class Stick {
  int xPos, yPos;
  int length;
  Stick(int ix, int iy) {
    xPos = ix;
    yPos = iy;
    length = 100;
  }
}

public void setup() {
  
}
public void draw() {
  ellipse(100,200,200,100);
}
public void mouseClicked() {
  exit();
}


public void CreateMap(int gridSizeX, int gridSizeY) {
  Pair[] gridDots;
  for (int y = 0; y < gridSizeY; y++) {
    for (int x = 0; x < gridSizeX; x++) {

    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PacMan" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
