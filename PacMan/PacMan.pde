class Stick {
  int xPos, yPos;
  int length;
  Stick(int ix, int iy) {
    xPos = ix;
    yPos = iy;
    length = 100;
  }
}

void setup() {
  fullScreen();
}
void draw() {
  ellipse(100,200,200,100);
}
void mouseClicked() {
  exit();
}
