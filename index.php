<?php
  //php code goes in here
echo "Hello!";
//test

// Helper method to get a string description for an HTTP status code
// From http://www.gen-x-design.com/archives/create-a-rest-api-with-php/
function getStatusCodeMessage($status) {
  // these could be stored in a .ini file and loaded
  // via parse_ini_file()... however, this will suffice
  // for an example
  $codes = Array(
		 100 => 'Continue',
		 101 => 'Switching Protocols',
		 200 => 'OK',
		 201 => 'Created',
		 202 => 'Accepted',
		 203 => 'Non-Authoritative Information',
		 204 => 'No Content',
		 205 => 'Reset Content',
		 206 => 'Partial Content',
		 300 => 'Multiple Choices',
		 301 => 'Moved Permanently',
		 302 => 'Found',
		 303 => 'See Other',
		 304 => 'Not Modified',
		 305 => 'Use Proxy',
		 306 => '(Unused)',
		 307 => 'Temporary Redirect',
		 400 => 'Bad Request',
		 401 => 'Unauthorized',
		 402 => 'Payment Required',
		 403 => 'Forbidden',
		 404 => 'Not Found',
		 405 => 'Method Not Allowed',
		 406 => 'Not Acceptable',
		 407 => 'Proxy Authentication Required',
		 408 => 'Request Timeout',
		 409 => 'Conflict',
		 410 => 'Gone',
		 411 => 'Length Required',
		 412 => 'Precondition Failed',
		 413 => 'Request Entity Too Large',
		 414 => 'Request-URI Too Long',
		 415 => 'Unsupported Media Type',
		 416 => 'Requested Range Not Satisfiable',
		 417 => 'Expectation Failed',
		 500 => 'Internal Server Error',
		 501 => 'Not Implemented',
		 502 => 'Bad Gateway',
		 503 => 'Service Unavailable',
		 504 => 'Gateway Timeout',
		 505 => 'HTTP Version Not Supported'
		 );

  return (isset($codes[$status])) ? $codes[$status] : '';
}

// Helper method to send a HTTP response code/message
function sendResponse($status = 200, $body = '', $content_type = 'text/html') {
  $status_header = 'HTTP/1.1 ' . $status . ' ' . getStatusCodeMessage($status);
  header($status_header);
  header('Content-type: ' . $content_type);
  echo $body;
}

class AnalyzeAPI{
  private $db;

  //Constructor to open db connection
  function __construct(){
    $this->db = new mysqli('localhost', 'root', '456852abcd', 'battleship');
    $this->db->autocommit(TRUE); //might want to change this later on
  }

  //Destructor to close db connection
  function __destruct(){
    $this->db->close();
  }

  //Main method to analyze input
  function analyze(){
      
    $fetch_games_played = $this->db->prepare("SELECT * FROM `games` WHERE 1");
    $fetch_games_played->execute();
    $fetch_games_played->store_result();
    echo $fetch_games_played->num_rows;
    $fetch_games_played->close();

    //Check for required parameters and stores into local variables
    //$data = $_SERVER['HTTP_JSON'];
    $string = file_get_contents("string.json");
    $data = json_decode($string, true);
    
    if(isset($data)){
        echo $data."\n";
        echo " yay! ";
    }
    echo "\n\n\n".$data->Ships[1]->hp;
    if(isset($data->cmd)){
      $cmd = $data->cmd;
    }
    
    switch ($cmd){
    case 0: //pressed start, now to initialize grid and update gameid
      
      
      $stmt00 = $this->db->prepare("INSERT INTO `games` (`gameID`, `p1wins`, `p2wins`, `winner`, `limits`)
                                   VALUES(?,?,?,?,?)");
      $stmt00->execute(); //inserts 

	if(count($data->Ships) == 5){
	for($i=0; $i<5; $i++)
	  {
	    $num_hitbox = $data->Ships[$i]->hp;
	    $ship_ID = $data->Ships[$i]->shipID;
	    $team_num = $data->Ships[$i]->team;

	    //updating ships table                                                                        
	    $stmt0 = $this->db->prepare("INSERT INTO `ships` (`shipID`, `hp`, `hits`, `team`) VALUES(?,?,?,?)");
	    $stmt0->bind_param("iiii", $ship_ID, $num_hitbox, 0, $team_num);
            $stmt0->execute();
	   
      	    for($j=0; $j<$num_hitbox; $j++)
		{        
		 $ship_x = $data->Ships[$i]->Hitboxes[$j]->x;
		 $ship_y = $data->Ships[$i]->Hitboxes[$j]->y;
		                                            
		 $stmt = $this->db->prepare("INSERT INTO `hitboxes` (`shipID`, `x`, `y`, `sunk`, `team`)
                                            VALUES(?,?,?)");
		 $stmt->bind_param("iiiii", $ship_ID, $ship_x, $ship_y, 0, $team_num );
		 $stmt->execute();

	     	 }
	       }//end of for loop for 5 ships on a team
	      echo "added ships";
	  }
	break;
	
	
    case 1: //accept fire coordinates from a user, use turns (odds/evens) to know whose turn it is
      $turns_played = NULL;  
      //$fetch_turn = $this->db->prepare("SELECT `turns` FROM `games` ORDER BY turns DESC LIMIT 1 "); 
      $ship_gameID = $data->Ships->gameID;
      $fetch_turn = $this->db->prepare("SELECT `turns` FROM `games`,`ships` WHERE ships.gameID = ? ");
      $fetch_turn->bind_param('i', $ship_gameID);
      $fetch_turn->execute();
      $fetch_turn->bind_result($turns_played);
      $fetch_turn->fetch();
      $fetch_turn->close(); //collected turns_played 
      //$fetch_turn->store_result();
      //$turns_played = $fetch_turn;
      
      if(($turns_played)%2==0){
	$curr_turn = 1;
      }
      else if(($turns_played)%2!=0){
	$curr_turn = 2; //P2's turn
      }
      if(isset($data->x)){
	$x_fired = $data->x;
      }
      if(isset($data->y)){
	$y_fired = $data->y;
      }
      
      //first thing to be done is to see if the player has already chosen the spot
      //$fetch_legal_move $this->db->prepare("");
      //$result = mysql_query("SELECT x, y WHERE");

      //compare input to hitboxes in database
      if($curr_turn == 1){
        $legality = NULL;
        //first check if move is legal (ie not yet chosen)
        $fetch_legal_move = $this->db->prepare("SELECT `hit` FROM `moves` WHERE x=? AND y=?");
        $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
        $fetch_legal_move->execute();
        $fetch_legal_move->bind_result($legality);
        $fetch_legal_move->fetch();
        $fetch_legal_move->close();
        
        if($legality == 3){
            echo "Illegal move to $x_fired, $y_fired. You've fired here before.";
            break;
        }
        
        else if ($legality == 1){
            echo "Illegal move to $x_fired, $y_fired. You've fired here before.";
            break;
        }
         
        else if($legality == 2){ //legal move!
            $update_legality = $this->db->prepare("UPDATE `moves` SET `hit` = 3 WHERE x=? AND y=?");
            $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
            $fetch_legal_move->execute();
            
            //now check if it is a hit
            	$fetch_hits = $this->db->prepare("UPDATE `hitboxes` SET `sunk`= 1
                                          WHERE team =2 AND x = ? AND y= ?");
                $fetch_hits->bind_param('ii', $x_fired, $y_fired);
                $fetch_hits->execute();
                $fetch_hits->fetch();
                if($fetch_hits->affected_rows == 1){
                  echo "Hit!";
                }
                else{
                  echo "Miss!";
                }
        }
        else if($legality == 0){ //legal move!
            $update_legality = $this->db->prepare("UPDATE `moves` SET `hit` = 1 WHERE x=? AND y=?");
            $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
            $fetch_legal_move->execute();
            
            //check if it is a hit
            $fetch_hits = $this->db->prepare("UPDATE `hitboxes` SET `sunk`= 1
                                          WHERE team =2 AND x = ? AND y= ?");
            $fetch_hits->bind_param('ii', $x_fired, $y_fired);
            $fetch_hits->execute();
            $fetch_hits->fetch();
            if($fetch_hits->affected_rows == 1){
              echo "Hit!";
            }
            else{
              echo "Miss!";
            }
        }
 

      }
      else if($curr_turn == 2){
        $legality = NULL;
        //first check if move is legal (ie not yet chosen)
        $fetch_legal_move = $this->db->prepare("SELECT `hit` FROM `moves` WHERE x=? AND y=?");
        $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
        $fetch_legal_move->execute();
        $fetch_legal_move->bind_result($legality);
        $fetch_legal_move->fetch();
        $fetch_legal_move->close();
        
      
      //first thing to be done is to see if the player has already chosen the spot
      //$fetch_legal_move $this->db->prepare("");
      //$result = mysql_query("SELECT x, y WHERE");
        
        if($legality == 3){
            echo "Illegal move to $x_fired, $y_fired. You've fired here before.";
            break;
        }
        
        else if ($legality == 2){
            echo "Illegal move to $x_fired, $y_fired. You've fired here before.";
            break;
        }
         
        else if($legality == 1){ //legal move!
            $update_legality = $this->db->prepare("UPDATE `moves` SET `hit` = 3 WHERE x=? AND y=?");
            $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
            $fetch_legal_move->execute();
            
            //now check if it is a hit
            	$fetch_hits = $this->db->prepare("UPDATE `hitboxes` SET `sunk`= 1
                                          WHERE team =1 AND x = ? AND y= ?");
                $fetch_hits->bind_param('ii', $x_fired, $y_fired);
                $fetch_hits->execute();
                $fetch_hits->fetch();
                if($fetch_hits->affected_rows == 1){
                  echo "Hit!";
                }
                else{
                  echo "Miss!";
                }
        }
        else if($legality == 0){ //legal move!
            $update_legality = $this->db->prepare("UPDATE `moves` SET `hit` = 1 WHERE x=? AND y=?");
            $fetch_legal_move->bind_param('ii', $x_fired, $y_fired);
            $fetch_legal_move->execute();
            
            //check if it is a hit
            $fetch_hits = $this->db->prepare("UPDATE `hitboxes` SET `sunk`= 1
                                          WHERE team =1 AND x = ? AND y= ?");
            $fetch_hits->bind_param('ii', $x_fired, $y_fired);
            $fetch_hits->execute();
            $fetch_hits->fetch();
            if($fetch_hits->affected_rows == 1){
              echo "Hit!";
            }
            else{
              echo "Miss!";
            }
        }

      }
      break;
	
      }//end of switch cmd

    } // end of analyze()
  } // end of class analyzeAPI
$api = new AnalyzeAPI;
$api->analyze();
?>