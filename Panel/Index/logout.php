<?php
  session_start();
  if (empty($_SESSION['code'])) {
    header( 'Location: index.php' ) ;
    die();
  }
  
  session_unset();
  session_destroy();
  
  header( 'Location: index.php' ) ;
?>