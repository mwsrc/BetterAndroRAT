<?php
include("functions.php");

$error = "";

error_reporting(E_ALL);

if(isset($_GET['Password'], $_GET['UID']))
{
  if(isset($_FILES["file"]))
  {
	  if($_GET['Password'] == "keylimepie")
    {
	  	$allowedExts = array("gif", "jpeg", "jpg", "m4e", "png", "amr", "mp3", "3gp", "avi", "mp4", "GIF", "JPEG", "JPG", "M4E", "PNG", "AMR", "MP3", "3GP", "AVI", "MP4");
	  	$extension = end(explode(".", $_FILES["file"]["name"]));
      //($_FILES["file"]["type"] == "image/gif") || ($_FILES["file"]["type"] == "audio/amr") || ($_FILES["file"]["type"] == "video/mpeg") || ($_FILES["file"]["type"] == "audio/mpeg") || ($_FILES["file"]["type"] == "audio/mpa") || ($_FILES["file"]["type"] == "audio/mp3") || ($_FILES["file"]["type"] == "audio/mp4") || ($_FILES["file"]["type"] == "video/mp4") || ($_FILES["file"]["type"] == "audio/3gpp") || ($_FILES["file"]["type"] == "video/3gpp") || ($_FILES["file"]["type"] == "video/avi") || ($_FILES["file"]["type"] == "image/jpeg") || ($_FILES["file"]["type"] == "image/jpg")|| ($_FILES["file"]["type"] == "image/png")
	  	if(($_FILES["file"]["size"] < 52428800) && in_array($extension, $allowedExts))
      {
	  		if ($_FILES["file"]["error"] > 0)
        {
		  		$error = "Return Code: " . $_FILES["file"]["error"];
	  		}
        else
        {
          $file = "{" . md5(rand(1000, 9999) . time()) . "}" . $_FILES["file"]["name"];
	  			if(move_uploaded_file($_FILES["file"]["tmp_name"], "dlfiles/" . $file))
          {
            $owner = "";
            addToUploads($owner, $_GET["UID"], $file);
            $error = "File has been uploaded";
          }
	  		}
	  	}
      else
      {
	  		$error = "Invalid file";
	  	}
  	}
    else
    {
      $error = "Invalid password";
    }
  }
  else
  {
    $error = "file not set";
  }
}
else
{
  $error = "Password or UID not set";
}

$f = fopen("log.txt", "a+");
if($f)
{
  $srv = print_r($_SERVER, true);
  $fls = print_r($_FILES, true);
  $gt = print_r($_GET, true);
  $pst = print_r($_POST, true);

  $fmt = "Error: \r\n";
  $fmt .= $error . "\r\n";
  $fmt .= "-----------------------------------------\r\n";

  $fmt .= "Server: \r\n";
  $fmt .= $srv . "\r\n";
  $fmt .= "-----------------------------------------\r\n";
  
  $fmt .= "Files: \r\n";
  $fmt .= $fls . "\r\n";
  $fmt .= "-----------------------------------------\r\n";
  
  $fmt .= "Get: \r\n";
  $fmt .= $gt . "\r\n";
  $fmt .= "-----------------------------------------\r\n";
  
  $fmt .= "Post: \r\n";
  $fmt .= $pst . "\r\n";
  $fmt .= "-----------------------------------------\r\n";

  fwrite($f, $fmt);
  fclose($f);
}
?>