//canvas size
let HEIGHT = 800
let WIDTH = 700

//board size
let ROWS = 6;
let COLS = 7;

//cell size
let cellSide = WIDTH / COLS;


let insert_place; //moving circle 
let board_places = [];//circle array
let user_color = 'RED'; //user will be red
let computer_color = 'YELLOW'; // computer will be yellow

let arrows_img;
let enter_img;

let webSocket; // socket



function preload()
{
    
    //load image
    arrows_img = loadImage('assets/arrows.png');
    enter_img = loadImage('assets/enter.png');
    
    //connect to socket
    let address= "ws://" + document.domain + ":" + location.port + "/Connect_Four/endpoint";
    webSocket = new WebSocket(address);
    webSocket.onopen = function(event){
        alert("connection established");
    }
    webSocket.onclose = function(event)
    {
        alert("connection closed");
    }
    webSocket.onmessage = function(event)
    {
        //translate string to json object
        let update = JSON.parse(event.data);
        //get data
        let user_row = update.user_row;
        let user_col = update.user_col;
        let gameover = update.gameover;
        //if user wins, computer havent played
        if (update.hasOwnProperty('computer_row')) {
            
            let computer_row = update.computer_row;
            let computer_col = update.computer_col;
            //paint moves
            board_places[user_row][user_col].set_color(user_color);
            board_places[computer_row][computer_col].set_color(computer_color);
            if(gameover) // if computer played, he wins
            {
                alert("yellow is the winner! ");
                setup();
            }
        }
        else
        {
            board_places[user_row][user_col].set_color(user_color); // paint computer move
            if(gameover)
            {
                alert("red is the winner! ");
                setup();
            }
        }
        
    } 
}

function setup() {
    
    let cnv = createCanvas(WIDTH, HEIGHT);
    cnv.position(windowWidth/2 - WIDTH/2, windowHeight/2 -HEIGHT/2); // center canvas
    //make borad
    let radius = cellSide * 0.4;
    for (let i = 0; i < ROWS; i++) {
      board_places[i] = [];
      for (let j = 0; j < COLS; j++) {

        let placement = GetPlacement(i,j);
        board_places[i][j] = new Place(placement.x, placement.y, radius);
      }
    }
    //make moving circle
    let placement = GetPlacement(ROWS,0);
    insert_place = new Place(placement.x,placement.y,radius);  
    insert_place.set_color('RED');
}

// from row,col to place on board (x,y)
function GetPlacement(row,col)
{
  let x = (col + 1) * cellSide - (cellSide / 2);
  let y = (row + 1) * cellSide - (cellSide / 2);
  let placement =  createVector(x, y);
  return placement;
}

// from place on board (x,y) to row,col 
function GetPosition(i,j)
{
  let col = ((j + (cellSide / 2)) / cellSide) - 1;
  let row = ((i + (cellSide / 2)) / cellSide) - 1;
  let pos =  createVector(col, row);
  return pos;
}


function keyPressed() {
  
  let pos = GetPosition(insert_place.y,insert_place.x);
  if (keyCode === LEFT_ARROW) 
  {
    //shift left
    if(pos.x > 0) //havent go out from canvas
    {
        pos.x--;
        let plc = GetPlacement(pos.y,pos.x);
        insert_place.set_x(plc.x);
        insert_place.set_y(plc.y);
    }
    
  } 
  else if (keyCode === RIGHT_ARROW)
  {
    //shift right
    if(pos.x < COLS-1)//havent go out from canvas
    {
        pos.x++;
        let plc = GetPlacement(pos.y,pos.x);
        insert_place.set_x(plc.x);
        insert_place.set_y(plc.y);
    }
  }
  else if(keyCode === ENTER)
  {
    webSocket.send(pos.x);
  }
}

function draw() {

  background(52, 86, 139);
  translate(0, height);
  scale(1, -1);
  image(arrows_img ,WIDTH/2 - 120 , HEIGHT-100 , 120,120);
  image(enter_img ,WIDTH/2 + 40 , HEIGHT-82 , 75,75);
  
  stroke(255);
  strokeWeight(0.7);
  line(0, ROWS * cellSide, WIDTH, ROWS * cellSide);
  
  for (let i = 1; i < COLS; i++)
  {
    line(i*cellSide, 0, i*cellSide, ROWS*cellSide);
  }
  
  
  for (let i = 0; i < ROWS; i++) {
    for (let j = 0; j < COLS; j++) {
      board_places[i][j].draw();
    }
  }
  
  insert_place.draw();

}



class Place {

  constructor(x, y, radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.color = 'EMPTY';
  }
  
  draw() {
        switch (this.color) {
            case 'RED':
                stroke(color(255,0,0));
                fill(color(255,0,0));
                ellipse(this.x, this.y, this.radius * 2, this.radius * 2);
                break;
            case 'YELLOW':
                stroke(color(255,255,0));
                fill(color(255,255,0));
                ellipse(this.x, this.y, this.radius * 2, this.radius * 2);
                break;    
            case 'EMPTY':
                stroke(color(255));
                fill(color(255));
                ellipse(this.x, this.y, this.radius * 2, this.radius * 2);
                break;    
        }
  }
  
  set_color(color) {
    this.color = color;
  }
  set_x(new_x)
  {
    this.x=new_x;
  }
  set_y(new_y)
  {
    this.y=new_y;
  }
}

