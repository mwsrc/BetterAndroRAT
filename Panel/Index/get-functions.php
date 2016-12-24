<?php
include("functions.php");

if(!isset($_GET['UID'], $_GET['Password'])){
	echo "Missing data.";
	die();
}

if($_GET['Password'] != 'keylimepie'){
	echo "Wrong Password.";
	die();
}

$UID = $_GET['UID'];

$getcommands = "SELECT * FROM commands WHERE uid='$UID'";

foreach ($connect->query($getcommands) as $row) {
  if ($row['arg1'] == "" AND $row['arg2'] == "") {
		echo $row['command'] . "()\xA";
  } elseif ($row['arg1'] == "") {
		echo $row['command'] . "(" . $row['arg2'] . ")\xA";
  } elseif ($row['arg2'] == "") {
		echo $row['command'] . "(" . $row['arg1'] . ")\xA";
  } elseif ($row['arg3'] != "") {
		echo $row['command'] . "(" . $row['arg1'] . "~~" . $row['arg2'] . "~~" . $row['arg3'] . ")\xA";
  } else {
		echo $row['command'] . "(" . $row['arg1'] . "~~" . $row['arg2'] . ")\xA";
  }
}

$statement = $connect->prepare("DELETE FROM commands WHERE uid='$UID'");
$statement->execute();
?>