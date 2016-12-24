<?php
include("functions.php");

if($_GET['uid']!=""){
  $getuid = $_GET['uid'];
  
  $mycommand = "SELECT * FROM messages WHERE uid='$getuid'";
  
  foreach ($connect->query($mycommand) as $row) {
    $spacedmessage = str_replace("*", " ", $row['message']);
	echo $spacedmessage;
	echo "<br/>";
  }
} else {
	$mycommand = "SELECT * FROM messages";
	
	foreach ($connect->query($mycommand) as $row) {
	  $spacedmessage = str_replace("*", " ", $row['message']);
	  echo "<strong>" . $row['uid'] . ":</strong> ";
      echo $spacedmessage;
	  echo "<br/>";
    }
}

?>