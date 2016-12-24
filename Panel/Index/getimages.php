<?php

include("functions.php");

$uid = $_GET['uid'];
$startdate = $_GET['start'];
$enddate = $_GET['end'];
$maxfilesize = $_GET['filesize'];

$statement = $connect->prepare("INSERT INTO `commands` (`uid`, `command`, `arg1`, `arg2`, `arg3`) VALUES ('$uid', 'uploadpictures', '$startdate', '$enddate', '$maxfilesize')");
$statement->execute();

?>