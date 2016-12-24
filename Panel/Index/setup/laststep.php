<?php
  if (file_exists("../reg.php")) {
    include("../reg.php");
	if ($validDomain == "false") {
	  die("Unauthorized Domain: Please contact support.");
	}
  } else {
    die("Missing Files: Please contact support.");
  }
  
if (!file_exists("../config.php")) {
  header('Location: index.php');
  die();
}
?>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  
    <title>Setup: Last Step</title>

    <link href="../assets/css/bootstrap.css" rel="stylesheet" media="screen">
	<link href="../assets/css/bootstrap-glyphicons.css" rel="stylesheet" media="screen">
	
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="../assets/js/jquery.tablesorter.min.js"></script>
	<script src="../assets/js/jquery.tablesorter.widgets.min.js"></script>
    <style type="text/css">
	  body {
	    background-image:url('../assets/img/login_bg.png');
	  }
	  .main {
        width: 1000px;
        height: 260px;
		
		background-color: #ffffff;
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
        outline: none;
        -webkit-box-shadow: 0 3px 9px rgba(0, 0, 0, 0.5);
        box-shadow: 0 3px 9px rgba(0, 0, 0, 0.5);

        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;

        margin: auto;
		
		padding: 15px 15px 15px 15px;
      }
	  .logo {
		margin: 0 0 0 -140px;
		height: 532px;
		width: 200px;
		z-index:-1;
		position: absolute;
	  }
	  .center {
	    margin: 0 auto 0 auto;
		width: 50%;
		text-align: center;
	  }
	</style>
  <body>
    <div class="main">
	  <h2 class="center">Setup Complete</h2>
	  <div class="well"><p>Thank you for completing first time setup. Everything you just setup can be changed in the Panel Settings. <br /><br /> Please delete the setup folder before continuing. It is not required, but recommended for security purposes. If you are ready to continue to the Control Panel, click the button below: <br /><br /></p><button onclick="location.href='../index.php'" type="button" class="btn btn-success btn-block">Finish Setup</button></div>
	</div>
  </body>
</html>