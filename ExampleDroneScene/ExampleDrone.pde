PShape drone;
PShape propeller;

public void setup() {
  size(600, 500, P3D);

  drone = loadShape("drone_sans_propellers_big.obj");
  propeller = loadShape("propellerbig.obj");
}

public void draw() {
  background(100);
  lights();

  translate(width/3, height/3, 0);          
  rotateZ(-PI );

  rotateY(map(mouseX, mouseY, width, 2.5, -2.5));

  pushMatrix();
  translate(1500,-400,0);
  shape(drone);
  popMatrix();
  
  pushMatrix();
  translate(970,-300,-520);
  shape(propeller);
  popMatrix();
  
  pushMatrix();
  translate(970,-300,520);
  shape(propeller);
  popMatrix();
  
  pushMatrix();
  translate(2470,-300,-520);
  shape(propeller);
  popMatrix();
  
  pushMatrix();
  translate(2470,-300,520);
  shape(propeller);
  popMatrix();
  
  propeller.rotateY(0.05);
 }
