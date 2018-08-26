import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.List; 
import java.util.ArrayList; 
import java.util.concurrent.ThreadLocalRandom; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PacMan extends PApplet {





List<Pill> pills;

public static int opposite(int dir) {
  switch (dir) {
  case 0:
    return 2;
  case 1:
    return 3;
  case 2:
    return 0;
  case 3:
    return 1;
  }
  return -1;
}

class Entity {
  int x, y;
  int radius;
  Entity(int ix, int iy, int iradius) {
    x = ix;
    y = iy;
    radius = iradius;
  }
  public boolean isColliding(int objX, int objY, int objRadius) {
    if (sqrt(pow(x - objX, 2) + pow(y - objY, 2)) <= radius + objRadius) return true;
    return false;
  }
}

class MovingEntity extends Entity {
  int dir;
  int textureCount;
  PImage[] textures;
  MovingEntity(int ix, int iy, int iradius, int textureNum) {
    super(ix, iy, iradius);
    textures = new PImage[textureNum];
    textureCount = 0;
  }
}

class Graph {
  List<Point> PointList;
  Graph() {
     PointList = new ArrayList<Point>();
  }
  public void drawGraph() {
    boolean[] marked = new boolean[PointList.size()];
    for (int i = 0; i < PointList.size(); i++) {
      Point p = PointList.get(i);
      for (int num : p.connections) {
        if (!marked[num]) {
          line(p.x, p.y, PointList.get(num).x, PointList.get(num).y);
        }
      }
      marked[i] = true;
    }
  }
}

class Point {
  int x, y;
  int[] connections;
  Point(int ix, int iy, int[] iconnections) {
    x = ix;
    y = iy;
    connections = iconnections;
  }
}

class Pill extends Entity {
  Pill(int ix, int iy) {
    super(ix, iy, 5);
  }
  public void drawEntity() {
    fill(255, 255, 255);
    ellipse(x, y, radius * 2, radius * 2);
  }
};

class Player extends MovingEntity {
  int dir;
  int lastDir;
  Player(int ix, int iy) {
    super(ix, iy, 10, 5);
    dir = -1;
    lastDir = -1;
  }
  public void SetDirection(int idir, List<Point> PointList) {
    int imagX = x, imagY = y;
    switch (idir) {
    case 0:
      imagY--;
      break;
    case 1:
      imagX--;
      break;
    case 2:
      imagY++;
      break;
    case 3:
      imagX++;
      break;
    }
    lastDir = idir;
    /*if (isOnLine) {
      dir = idir;
      return;
    }*/
  }
  public void move(List<Path> paths) {
    boolean isOnLine = false;
    for (Path path : paths) {
      switch (dir) {
      case 0:
        if (path.checkIfOnLine(x, y - 1)) isOnLine = true;
        break;
      case 1:
        if (path.checkIfOnLine(x - 1, y)) isOnLine = true;
        break;
      case 2:
        if (path.checkIfOnLine(x, y + 1)) isOnLine = true;
        break;
      case 3:
        if (path.checkIfOnLine(x + 1, y)) isOnLine = true;
        break;
      }
    }
    if (isOnLine) {
      switch (dir) {
      case 0:
        y--;
        break;
      case 1:
        x--;
        break;
      case 2:
        y++;
        break;
      case 3:
        x++;
        break;
      }
      textureCount++;
      if (textureCount == 20) textureCount = 0;
      if (x <= -25) x = 624;
      if (x >= 625) x = -24;
    } else {
      dir = -1;
      textureCount = 0;
    }
  }

  public void Die() {
    delay(1500);
  }

  public void DrawPlayer() {
    if (textureCount/10 == 0) {
      image(textures[0], x - radius, y - radius, radius * 2, radius * 2);
    } else {
      image(textures[dir + 1], x - radius, y - radius, radius * 2, radius * 2);
    }
  }
}

/*class Ghost extends MovingEntity {
  int dir;
  int[] wishedDir;
  PImage eyeTexture;
  Ghost(int ix, int iy) {
    super(ix, iy, 20, 2);
    dir = -1;
    wishedDir = new int[4];
  }
  void FindWishedDir(Player pacman) {
    for (int i = 0; i < 4; i++) {
      wishedDir[i] = -1;
    }
    int dx = pacman.x - x;
    int dy = pacman.y - y;
    if (dx < 0) {
        if (dy < 0) {
          if (abs(dy) < abs(dx)) {
            wishedDir[0] = 1;
            wishedDir[1] = 0;
          }
          else {
            wishedDir[0] = 0;
            wishedDir[1] = 1;
          }
        } else if (dy > 0) {
          if (abs(dy) < abs(dx)) {
            wishedDir[0] = 1;
            wishedDir[1] = 2;
          }
          else {
            wishedDir[0] = 2;
            wishedDir[1] = 1;
          }
        } else {
          wishedDir[0] = 1;
        }
      } else if (dx > 0) {
        if (dy < 0) {
          if (abs(dy) < abs(dx)) {
            wishedDir[0] = 3;
            wishedDir[1] = 0;
          }
          else {
            wishedDir[0] = 0;
            wishedDir[1] = 3;
          }
        } else if (dy > 0) {
          if (abs(dy) < abs(dx)) {
            wishedDir[0] = 3;
            wishedDir[1] = 2;
          }
          else {
            wishedDir[0] = 2;
            wishedDir[1] = 3;
          }
        } else  {
          wishedDir[0] = 3;
        }
      } else {
        if (dy < 0) wishedDir[0] = 0;
        else if (dy > 0) wishedDir[0] = 2;
      }

      List<Integer> dirList = new ArrayList<Integer>();
      dirList.add(0);
      dirList.add(1);
      dirList.add(2);
      dirList.add(3);
      boolean[] DeletedDirs = {false, false, false, false};
      for (int i = 0; i < 4; i++) {
        if (wishedDir[i] != -1) {
          DeletedDirs[i] = true;
        }
      }
      for (int i = 3; i >= 0; i--) {
        if (DeletedDirs[i]) dirList.remove(i);
      }
      for (int i = 4 - dirList.size(); i < 4; i++) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, dirList.size());
        wishedDir[i] = randomNum;
        dirList.remove(randomNum);
      }
  }
  void SetDirection(Player pacman, List<Path> paths) {
    FindWishedDir(pacman);
    for (Path path : paths) {
      boolean isOnLine = false;
      for (int wishedD : wishedDir) {
        if (wishedD == opposite(dir)) continue;
        switch (wishedD) {
        case 0:
          if (path.checkIfOnLine(x, y - 1)) isOnLine = true;
          break;
        case 1:
          if (path.checkIfOnLine(x - 1, y)) isOnLine = true;
          break;
        case 2:
          if (path.checkIfOnLine(x, y + 1)) isOnLine = true;
          break;
        case 3:
          if (path.checkIfOnLine(x + 1, y)) isOnLine = true;
          break;
        }
        if (isOnLine) {
          dir = wishedD;
          return;
        }
      }
    }
  }
  void move(List<Path> paths) {
    boolean isOnLine = false;
    for (Path path : paths) {
      switch (dir) {
      case 0:
        if (path.checkIfOnLine(x, y - 1)) isOnLine = true;
        break;
      case 1:
        if (path.checkIfOnLine(x - 1, y)) isOnLine = true;
        break;
      case 2:
        if (path.checkIfOnLine(x, y + 1)) isOnLine = true;
        break;
      case 3:
        if (path.checkIfOnLine(x + 1, y)) isOnLine = true;
        break;
      }
      if (x <= -25) x = 624;
      if (x >= 625) x = -24;
    }
    if (isOnLine) {
      switch (dir) {
      case 0:
        y--;
        break;
      case 1:
        x--;
        break;
      case 2:
        y++;
        break;
      case 3:
        x++;
        break;
      }
    } else dir = -1;
  }

  void DrawGhost() {
    image(textures[0], x - radius, y - radius, radius * 2, radius * 2);
    switch (dir) {
    case -1:
      image(eyeTexture, x - radius + 12*(radius/21), y - radius + 12*(radius/21), 18*(radius/21), 2*(radius/21));
      break;
    case 0:
      image(eyeTexture, x - radius + 12*(radius/21), y - radius + 12*(radius/21), 18*(radius/21), 2*(radius/21));
      break;
    case 1:
      image(eyeTexture, x - radius + 12*(radius/21), y - radius + 12*(radius/21), 18*(radius/21), 2*(radius/21));
      break;
    case 2:
      image(eyeTexture, x - radius + 12*(radius/21), y - radius + 12*(radius/21), 18*(radius/21), 2*(radius/21));
      break;
    case 3:
      image(eyeTexture, x - radius + 12*(radius/21), y - radius + 12*(radius/21), 18*(radius/21), 2*(radius/21));
      break;
    }
  }
}*/

public void DeleteUslessPills() {
  int[] xs = new int[pills.size()], ys = new int[pills.size()];
  for (int i = 0; i < pills.size(); i++) {
    Pill pill = pills.get(i);
    xs[i] = pill.x;
    ys[i] = pill.y;

  }
  for (int i = 0; i < pills.size(); i++) {

  }
}

int score = 0;
Player pacman = new Player(250, 50);
static Graph GameMap;
//Ghost Ghosts[] = new Ghost[4];

public void setup() {
  
  background(30, 30, 30);

  GameMap = new Graph();
  pills = new ArrayList<Pill>();

  int[][] ConnectionsList =
  {//0
   {1, 4},
   //1
   {0, 2, 6},
   //2
   {1, 3, 7},
   //3
   {2, 9},
   //4
   {0, 5},
   //5
   {4, 6, 10},
   //6
   {1, 5, 14},
   //7
   {2, 8, 15},
   //8
   {7, 9, 11},
   //9
   {8, 3},
   //10
   {5, 13},
   //11
   {8, 16},
   //12
   {13, 18},
   //13
   {10, 12, 14},
   //14
   {6, 13, 19},
   //15
   {7, 16, 20},
   //16
   {11, 15, 17},
   //17
   {16, 21},
   //18
   {12, 19},
   //19
   {14, 18, 20},
   //20
   {15, 19, 21},
   //21
   {20, 17}};

  //Row 1
  GameMap.PointList.add(new Point(50, 50, ConnectionsList[0]));
  GameMap.PointList.add(new Point(250, 50, ConnectionsList[1]));
  GameMap.PointList.add(new Point(350, 50, ConnectionsList[2]));
  GameMap.PointList.add(new Point(550, 50, ConnectionsList[3]));

  //Row 2
  GameMap.PointList.add(new Point(50, 150, ConnectionsList[4]));
  GameMap.PointList.add(new Point(150, 150, ConnectionsList[5]));
  GameMap.PointList.add(new Point(250, 150, ConnectionsList[6]));
  GameMap.PointList.add(new Point(350, 150, ConnectionsList[7]));
  GameMap.PointList.add(new Point(450, 150, ConnectionsList[8]));
  GameMap.PointList.add(new Point(550, 150, ConnectionsList[9]));
  //Go Round path
  GameMap.PointList.add(new Point(150, 200, ConnectionsList[10]));
  GameMap.PointList.add(new Point(450, 200, ConnectionsList[11]));

  //Row 3
  GameMap.PointList.add(new Point(50, 250, ConnectionsList[12]));
  GameMap.PointList.add(new Point(150, 250, ConnectionsList[13]));
  GameMap.PointList.add(new Point(250, 250, ConnectionsList[14]));
  GameMap.PointList.add(new Point(350, 250, ConnectionsList[15]));
  GameMap.PointList.add(new Point(450, 250, ConnectionsList[16]));
  GameMap.PointList.add(new Point(550, 250, ConnectionsList[17]));

  //Row 4
  GameMap.PointList.add(new Point(50, 350, ConnectionsList[18]));
  GameMap.PointList.add(new Point(250, 350, ConnectionsList[19]));
  GameMap.PointList.add(new Point(350, 350, ConnectionsList[20]));
  GameMap.PointList.add(new Point(550, 350, ConnectionsList[21]));

  //Ghosts[0] = new Ghost(50, 50);
  //Ghosts[1] = new Ghost(550, 50);
  //Ghosts[2] = new Ghost(50, 350);
  //Ghosts[3] = new Ghost(550, 350);

  String[] Colors = {"GhostRed", "GhostTurquoise", "GhostPink", "GhostYellow"};
  //String[] Directions = {"", "_up", "_left", "_down", "_right"};

  /*for (int n = 0; n < 4; n++) {
    //for (int i = 0; i < 5; i++) {
      Ghosts[n].textures[0] = loadImage(Colors[n] + /\*Directions[i] +*\/ ".png");
      Ghosts[n].eyeTexture = loadImage("Ghost_eyes.png");
    //}
  }*/

  /*for (Ghost ghost : Ghosts) {
    ghost.DrawGhost();
  }*/

  for (Pill pill : pills) {
    pill.drawEntity();
  }

  pacman.textures[0] = loadImage("PacMan.png");
  pacman.textures[1] = loadImage("PacMan_up.png");
  pacman.textures[2] = loadImage("PacMan_left.png");
  pacman.textures[3] = loadImage("PacMan_down.png");
  pacman.textures[4] = loadImage("PacMan_right.png");

  pacman.DrawPlayer();

  textSize(14);
}

boolean PrintingENDGAMEText = false;

public void draw() {
  if (keyPressed == true) {
    if (key == 'w'||key == 'W') pacman.SetDirection(0, PathList);
    else if (key == 'a'||key == 'A') pacman.SetDirection(1, PathList);
    else if (key == 's'||key == 'S') pacman.SetDirection(2, PathList);
    else if (key == 'd'||key == 'D') pacman.SetDirection(3, PathList);
  } else pacman.SetDirection(pacman.lastDir, PathList);
  pacman.move(PathList);

  /*for (int i = 0; i < pills.size(); i++) {
    Pill pill = pills.get(i);
    if (pill.isColliding(pacman.x, pacman.y, 10)) {
      pills.remove(i);
      score += 100;
    }
  }*/

  background(30, 30, 30);

  fill(255, 255, 255);
  text ("Score: " + score, 10, 10, 100, 70);

  GameMap.drawGraph();

  for (Pill pill : pills) pill.drawEntity();
  /*for (Ghost ghost : Ghosts) {
    ghost.SetDirection(pacman, PathList);
    ghost.move(PathList);
    ghost.DrawGhost();
  }*/
  //pacman.DrawPlayer();
  /*if (pills.size() == 0 && !PrintingENDGAMEText) {
    text ("YOU WIN!", 200, 200, 100, 70);
    PrintingENDGAMEText = true;
  } else if (pills.size() == 0 && PrintingENDGAMEText) {
    delay(1500);
    exit();
  }*/
  /*for (Ghost ghost : Ghosts) {
    if (ghost.isColliding(pacman.x, pacman.y, 10)) {
      pacman.Die();
      //pacman = null;
      exit();
    }
  }*/
}
  public void settings() {  size(600, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PacMan" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
