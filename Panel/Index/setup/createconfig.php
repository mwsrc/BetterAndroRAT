<?php
  if (file_exists("../reg.php")) {
    include("../reg.php");
	if ($validDomain == "false") {
	  die("Unauthorized Domain: Please contact support.");
	}
  } else {
    die("Missing Files: Please contact support.");
  }
  
if (file_exists("../config.php")) {
  header('Location: ../index.php');
  die();
}

$inputdbhost = $_POST['dbhost'];
$inputdbname = $_POST['dbname'];
$inputdbuser = $_POST['dbusername'];
$inputdbpass = $_POST['dbpassword'];

$inputusername = $_POST['username'];
$inputpassword = $_POST['password'];

if ($inputpassword == ""){
  die("Password is blank, please click the back button and try again.");
}

$inputpostboxtextsize = $_POST['postboxsize'];

$inputdevicestablerefreshspeed = $_POST['devicetablerefr'] * 1000;
$inputfilestablerefreshspeed = $_POST['filetablerefr'] * 1000;
$inputmessageboxrefreshspeed = $_POST['historyboxrefr'] * 1000;

$inputofflineminutes = $_POST['botoffline'];

$inputtimezonesetting = $_POST['timezone'];

$inputautoscrolltextbox = $_POST['messageboxscroll'];

file_put_contents("../config.php", "");

$f = fopen("../config.php", "w+");
if($f){
  $write = "<?php\n";
  $write .= '$dbhost=\'' . $inputdbhost . "';\n";
  $write .= '$dbname=\'' . $inputdbname . "';\n";
  $write .= '$dbuser=\'' . $inputdbuser . "';\n";
  $write .= '$dbpass=\'' . $inputdbpass . "';\n";
  $write .= '$username=\'' . $inputusername . "';\n";
  $write .= '$password=\'' . hash('whirlpool', $inputpassword) . "';\n";
  $write .= '$postboxtextsize=' . $inputpostboxtextsize . ";\n";
  $write .= '$devicestablerefreshspeed=' . $inputdevicestablerefreshspeed . ";\n";
  $write .= '$filestablerefreshspeed=' . $inputfilestablerefreshspeed . ";\n";
  $write .= '$messageboxrefreshspeed=' . $inputmessageboxrefreshspeed . ";\n";
  $write .= '$offlineminutes=' . $inputofflineminutes . ";\n";
  $write .= '$timezonesetting=\'' . $inputtimezonesetting . "';\n";
  $write .= '$autoscrolltextbox=' . ($inputautoscrolltextbox == 'Yes' ? 'true' : 'false') . ";\n";
  $write .= "?>";
  
  fwrite($f, $write);
  fclose($f);
}

header('Location: laststep.php');
?>