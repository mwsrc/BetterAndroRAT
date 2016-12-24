<?php
include("functions.php");

$botid = $_GET['UID'];

  $statement = $connect->prepare("SELECT blocked FROM bots WHERE uid='$botid'");
  $statement->execute();
  
  $result = $statement->fetch(PDO::FETCH_ASSOC);
  
  $curblocked = $result[blocked];


if(isset($_GET['UID'], $_GET['Data']) AND $curblocked != 'yes'){
    $gooddata = str_replace("~period", ".", $_GET['Data']);
	addMessage($_GET['UID'], $gooddata);
} else {
	die();
}
?>