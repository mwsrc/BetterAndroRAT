<?php
include("functions.php");

  $mycommand = "SELECT * FROM commands";
  
  foreach ($connect->query($mycommand) as $row) {
    echo "<strong>" . $row['uid'] . ": </strong>" . $row['command'] . "(" . $row['arg1'] . ", " . $row['arg2'] . ")";
	echo "<br/>";
  }

?>