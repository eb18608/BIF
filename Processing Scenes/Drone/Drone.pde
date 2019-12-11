public class droneObject {
  float h, w, d;
  PVector coords;
  PShape hitbox, body, propellerFL, propellerFR, propellerBL, propellerBR;
  
  public droneObject(float wid, float hei, float dep, float x, float y, float z) {
     h = hei;
     w = wid;
     d = dep;
     coords = new PVector(x, y, z);
     hitbox = createShape(BOX, w, h, d);
     body = loadShape("textured_drone_sans_propellers.obj");
     propellerFL = loadShape("textured_propeller.obj");
     propellerFR = loadShape("textured_propeller.obj");
     propellerBL = loadShape("textured_propeller.obj");
     propellerBR = loadShape("textured_propeller.obj");
   }
   
   public float getHeight() {
     return h;
   }
   
   public float getWidth() {
     return w;
   }
   
   public float getDepth() {
     return d;
   }
   
   public PVector getCoords() {
     return coords;
   }
   
   public void move(float x, float y, float z) {
     float totalX = (z * sin(rotation)) + (x * cos(rotation));
     float totalZ = (z * cos(rotation)) + (x * sin(rotation));
     coords.add(totalX, y, totalZ);
     /*pushMatrix();
     hitbox.translate(x, y, z);
     body.translate(x, y, z);
     propellerFL.translate(x, y, z);
     propellerFR.translate(x, y, z);
     propellerBL.translate(x, y, z);
     propellerBR.translate(x, y, z);
     popMatrix();*/
 }
   
   public void draw() {
     shape(hitbox);
     shape(body);
     
     pushMatrix();
     translate(-22, 2, 22);
     shape(propellerFL);
     popMatrix();
     
     pushMatrix();
     translate(22, 2, 22);
     shape(propellerFR);
     popMatrix();
     
     pushMatrix();
     translate(-22, 2, -22);
     shape(propellerBL);
     popMatrix();
     
     pushMatrix();
     translate(22, 2, -22);
     shape(propellerBR);
     popMatrix();
   }
  
}

PImage texture;
droneObject drone;
float rotation;

public void spinPropellers() {
  pushMatrix();
  drone.propellerFL.rotateY(0.1);
  drone.propellerFR.rotateY(-0.1);
  drone.propellerBL.rotateY(0.1);
  drone.propellerBR.rotateY(-0.1);  
  popMatrix();
}

public void setCamera() {
  float eyex = drone.coords.x - (200 * sin(rotation));
  float eyey = drone.coords.y + 100;
  float eyez = drone.coords.z - (200 * cos(rotation));
  camera(eyex, eyey, eyez, drone.coords.x, drone.coords.y, drone.coords.z, 0, -1, 0); 
}

public void building(PImage texture) {
  beginShape(QUADS);
  texture(texture);
  //frontface
  vertex(0, 0, 0, 1, 1);
  vertex(0, 0, 400, 0, 1);
  vertex(0, 600, 400, 0, 0);
  vertex(0, 600, 0, 1, 0);
  //backface
  vertex(400, 0, 400, 1, 1);
  vertex(400, 0, 0, 0, 1);
  vertex(400, 600, 0, 0, 0);
  vertex(400, 600, 400, 1, 0);
  //leftface
  vertex(400, 0, 0, 1, 1);
  vertex(0, 0, 0, 0, 1);
  vertex(0, 600, 0, 0, 0);
  vertex(0, 600, 400, 1, 0);
  //rightface
  vertex(0, 0, 400, 1, 1);
  vertex(400, 0, 400, 0, 1);
  vertex(400, 600, 400, 0, 0);
  vertex(0, 600, 400, 1, 0);
  //topface
  vertex(0, 600, 0);
  vertex(0, 600, 400);
  vertex(400, 600, 400);
  vertex(400, 600, 0);
  endShape();
}

public void setup() {
  size(1600, 900, P3D);
  drone = new droneObject(77, 16, 77, 0, 100, 100);
  texture = loadImage("SkyscraperFront.png");
  textureMode(NORMAL);
}

public void draw() {
  background(100);
  lights();
  spinPropellers();
  setCamera();
  drone.move(0, 0, 0);
  
  pushMatrix();
  translate(200, 0, 50);
  building(texture);
  popMatrix();
  
  pushMatrix();
  rotation = PI/8;
  rotateY(rotation);
  translate(drone.coords.x, drone.coords.y, drone.coords.z);
  drone.draw();
  rotateY(-rotation);
  popMatrix();
  
  drone.hitbox.setVisible(false);
  
  System.out.println("x: " + drone.coords.x + " y: " + drone.coords.y + " z: " + drone.coords.z);
}
