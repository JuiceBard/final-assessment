import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Final_Assessment extends PApplet {

/* *
 -----------------------
 ---- Jasper Dorman ----
 ------- 07/2016 -------
 ---- Version 1.4.2-----
 -----------------------
 */

// import sound library


// sound variable called musicFor18
SoundFile musicFor18;

/// Array of strings called first & secondWord
String[] firstWord = {"Trailing", "Spiraling", "Complete", "Capricious", "Didactic", "Recurring", "Nature's", "Iterating", "Endless", "Ceaseless", "Xenomorphic", "Algorithmic", "Coded", "Tornadic", "Harmonic"}; 
String[] secondWord = {"Void", "Chasm", "Aperture", "Rapture", "Harmony", "Dandeline", "Fissure", "Echo", "Galaxy", "Automata", "Motion"};

// establishes strings called title,instructions, up, spacebar, m, enter 
String title;
String instructions = "INSTRUCTIONS";
String up = "UP ARROW: Speed up rate of size change";
String spaceBar = "SPACEBAR: Speed up iteration period";
String m = "M: Mute Audio";
String enter = "ENTER: Generate new pattern";
String pressKey = "PRESS ANY KEY TO CONTINUE";

// boolean (True or False) called turback and dispInstructions
boolean turnback;
boolean dispInstructions = true;

// floats (decimal point values)
float sides;
float maxRadius = 300 * sqrt(2);
float offset, angle, radius, prevAngle, prevRadius, amp;
// bulk creation of variables
float size = 0;
float c = 0.01f;
float cSize = 0.01f;

// interger (whole nunbers) variables
int numTurns, chanceShape, stroke, drawShape;

// array of colors
int[] colour = {
  color(26, 188, 156), 
  color(22, 160, 133), 
  color(46, 204, 113), 
  color(39, 174, 96), 
  color(52, 152, 219), 
  color(41, 128, 185), 
  color(155, 89, 182), 
  color(142, 68, 173), 
  color(52, 73, 94), 
  color(44, 62, 80), 
  color(241, 196, 15), 
  color(243, 156, 18), 
  color(230, 126, 34), 
  color(211, 84, 0), 
  color(231, 76, 60), 
  color(192, 57, 43), 
  color(236, 240, 241), 
  color(189, 195, 199), 
  color(149, 165, 166), 
  color(127, 140, 141)
};

// bulk establishment of color variables
int background, overLay, line, point, textColour;

// this function is called first
public void setup() {
  // set size of window (1200,1200) and render (processing 3D)
  
  // call the randomise function
  randomise();
  // call the loading and playing sound function
  playSound();
}

// called every tick/frame
public void draw() {
  // draw background colour
  background(background);
  // zero point to centre screen (minus 50 of y-axis)
  translate(width / 2, height / 2 - 50);
  if (dispInstructions  == true) {
    displayInst();
    if (mousePressed || keyPressed) {

      dispInstructions = false;
    }
  } else {

    // call the draw spiral function
    drawSpiral();
    // randomising whether or not to draw an overlaying shape
    if (chanceShape == 0) {
      // if chanceShape equals zero then call the shapeLayOver function
      shapeLayOver();
    }
    // call textDisp function
    textDisp();
    // call the the sizeChange frame
    sizeChange();
  }
}

/* function called shapeLayOver that places a shape with 
 super large stroke over the top to create a cut out circle
 layer above the spiral
 */
public void shapeLayOver() {
  // stroke set to overLay colour
  stroke(overLay);
  // shape with no fill
  noFill();
  // set stroke to ridiculously large
  strokeWeight(1100);
  // create ellipse at 0,0 (translated to the centre (minus 50), with the size of 1800 x 1800
  ellipse(0, 0, 1800, 1800);
}
/* function called textDisp that displays the text in the 
 middle bottom of the screen and on top of the shape lay 
 over if drawn
 */
public void textDisp() { 
  // set fill colour to textColour
  fill(textColour);
  // set text size to 30
  textSize(30);
  // set text mode to shape so that it draws text using the glyph outlines of individual characters rather than as textures.
  textMode(SHAPE);
  // align the text centre at the centre and at the bottom of the boundary box
  textAlign(CENTER, BOTTOM);
  // draw the text from the string called title at 0 , 570 from the origin and 5 pixels infront of other elements on the z-axis
  text(title, 0, 570, 5);
}

// function called randomTitle that returns a string  
public String randomTitle() {
  // declare a string called randTitle with a random word from firstWord and secondWord array then append together with a space in the middle
  String randTitle = firstWord[PApplet.parseInt(random(firstWord.length))] + " " + secondWord[PApplet.parseInt(random(secondWord.length))];
  // return the previously calculated phrase now called randTitle
  return randTitle;
}
// only called when a key is pressed
public void keyPressed() {
  // a more efficient if else check
  switch(keyCode) {
  case 32: 
    // for the keyCode value of 32 (SPACEBAR) set c to 0.08
    c = 0.08f;
    // jumps to the next statement in this case without executing anything else
    break;
  case UP: 
    // for the keyCode value of UP set cSize to 0.1
    cSize = 0.1f;
    break;
  case ENTER:
    // for the keyCode value of ENTER scall the randomise function
    randomise();
    break;
  case 'M':
    if (amp == 0) {
      amp = 1;
      musicFor18.amp(amp);
    } else if (amp == 1) {
      amp = 0;
      musicFor18.amp(amp);
    }
  }
}
// much like the keyPressed, only called when a key is released
public void keyReleased() {

  switch(keyCode) {
  case 32: 
    // for the keyCode value of 32 (SPACEBAR) set c to 0.01
    c = 0.01f;
    break;
  case UP: 
    // for the keyCode value of UP set cSize to 0.01
    cSize = 0.01f;
    break;
  }
}


/* function called sizeChange that oscillates between 0 and 100
 a fairly rudimentary/harsh way to do so, but it works so why care :P
 */
public void sizeChange() {
  // only executes if size is less than 100 AND turnback is false
  if (size < 100 && turnback == false) {
    // increases size by size plus cSize
    size = (size + cSize);

    // only executes if size is more than
  } else if (size > 0) {
    // decreases size by size minus cSize
    size = (size - cSize);
    turnback = true;
    // only executes if size is less than zero
  } else if (size < 0) {
    // set turnback to false
    turnback = false;
  }
}

// function called randomise that initialises/randomises a bunch of variables 
public void randomise() {
  // randomises a colour from the colour array, by randomising a number between 0 and the length of the array then converting to a whole number
  background = colour[PApplet.parseInt(random(colour.length))];
  overLay = colour[PApplet.parseInt(random(colour.length))];
  line = colour[PApplet.parseInt(random(colour.length))];
  point = colour[PApplet.parseInt(random(colour.length))];
  numTurns = PApplet.parseInt(random(30, 100));
  sides = random(2, 250);
  chanceShape = PApplet.parseInt(random(3));
  stroke = PApplet.parseInt(random(0, 4));
  drawShape = PApplet.parseInt(random(4));
  // sets the string title to the returned value from the function randomTitle
  title = randomTitle();

  if (chanceShape == 0  && overLay == colour[16] || overLay == colour[17]) {
    // if the colour of the overLayed shape is either of the two light colours in my array (16 & 17) then change the textColour to the dark blue in the array (indexed 9)
    textColour = colour[9];
  } else if (chanceShape != 0 && background == colour[16] || background == colour[17]) {
    // if the shape isn't drawn and the background is either of the two light colours in my array (16 & 17) then change the textColour to the dark blue in the array (indexed 9)
    textColour = colour[9];
  } else {
    // otherwise set the textColour to the white in the array (indexed 16)
    textColour = colour[16];
  }

  // print("----",background, line, point, "----"); <-- here to debug and check the randomised variables
}

// function called drawSpiral responsible for the bulk of the visuals, it draws a spiral using a set number of sides and number of turns
public void drawSpiral() {
  // set sides to (sides plus the constant) modulos 1000 (resets back to 0 upon reaching 1000)/ mainly to prevent overloading and immense amounts of lag
  sides = (sides + c) % 1000;
  // each call reset angle and radius to 0
  angle = 0;
  radius = 0;
  // increase offset by 0.01, responsible for the general speed of the rotation
  offset += 0.01f;
  // for each number less than sides do the following
  for (int i = 0; i < sides; i++) {
    // set stroke colour to the line variable with alpha (opacity) value of 180 (out off 255)
    stroke(line, 180);
    // set stroke width to stroke variable
    strokeWeight(stroke);
    // set float prevAngle to angle
    prevAngle = angle;
    // set prevRadius to radius
    prevRadius = radius;
    // angle equals the current i value multiplied by two by PI by the number of turns all divided by the number of sides // this gives the angle of each side to the zero point
    angle = i * (2 * PI * numTurns) / sides;
    // radius equals the current i value * max radius divided by the sides // sets the radius from the zeropoint to the maxRadius for each side
    radius = i * (maxRadius) / sides;
    // draw a line from the previous radius point to the new radius point // using trig to calculate the positioning from x1,y1 to x2,y2 and the angles from both radii
    line(
      prevRadius * sin(prevAngle + offset), 
      prevRadius * cos(prevAngle + offset), 
      radius * sin(angle + offset), 
      radius * cos(angle + offset));
    // set the fill colour to the color variable point with alpha value 80
    fill(point, 80);
    noStroke();
    // a way of acomplihing randomisation but with an element of control/probability
    if (drawShape == 0) {
      // in the circumstance that drawShape equals zero draw a triangle from one end of a side to the other then to the zero point
      triangle(radius * sin(angle + offset), radius * cos(angle + offset), prevRadius * sin(prevAngle + offset), prevRadius * cos(prevAngle + offset), 0, 0);
    } else if (drawShape == 1) {
      // in the circumstance the drawShape equals 1 draw a rectangle at the vertex of each side with a modulating size
      rectMode(CENTER);
      rect(radius * sin(angle + offset), radius * cos(angle + offset), size - (i/3), size - (i/3));
    } else if (drawShape == 2 || drawShape ==  3) {
      // in the circumstance the drawShape equals 2 OR 3 (|| this means 'or' weird I know), draw a circle at the vertex with modulating size
      ellipseMode(CENTER);
      ellipse(radius * sin(angle + offset), radius * cos(angle + offset), size - (i/3), size - (i/3));
    }
  }
}

// function called play sound responsible for loading and playing/looping the audio
public void playSound() {
  // initiate the SoundFile variable called musicFor18 with a new SoundFile for this sketch from within the data folder with the name steveReichNGGYU.mp3
  musicFor18 = new SoundFile(this, "steveReichNGGYU.mp3");
  // print("There is a surprise at 56 min");
  // once the audio finishes, play again
  musicFor18.loop();
}

// function that displays the beginning instruction screen
public void displayInst() {
  noStroke();
  fill(textColour);
  textSize(20);
  textAlign(CENTER, BOTTOM);
  text(instructions, 0, - 100, 5);
  rectMode(CENTER);
  rect(0, -100, 150, 2);
  textSize(14);
  textAlign(CENTER, BOTTOM);
  text(enter, 0, -50, 5);
  text(spaceBar, 0, -20, 5);
  text(up, 0, 10, 5);
  text(m, 0, 40, 5);
  text(pressKey, 0, 100, 5);
}
  public void settings() {  size(1200, 1200, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Final_Assessment" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
