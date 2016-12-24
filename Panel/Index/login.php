<?php
  if (file_exists("config.php")) {
    include("config.php");
  } else {
    die();
  }
if(empty($_POST['username'])) {
  header( 'Location: index.php?error=user' ) ;
  die();
}
if(empty($_POST['password'])) {
  header( 'Location: index.php?error=pass' ) ;
  die();
}

$inputpass = hash('whirlpool', $_POST['password']);

if ($_POST['username'] == $username AND $inputpass == $password) {
  $supercode = hash('whirlpool', $_POST['username'] . $inputpass);

  session_start();
  $_SESSION['code'] = $supercode;
  header( 'Location: control.php' ) ;
} else {
  header( 'Location: index.php?error=both' ) ;
}

?>