<?php
  if (file_exists("reg.php")) {
    include("reg.php");
	if ($validDomain == "false") {
	  die("Unauthorized Domain: Please contact support.");
	}
  } else {
    die("Missing Files: Please contact support.");
  }

  if (file_exists("config.php")) {
    include("config.php");
  } else {
    header('Location: setup/');
  }
  
session_start();
if (isset($_SESSION['code'])) {
   header( 'Location: control.php' ) ;
}
?>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  
    <title>Login</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet" media="screen">
	
	<style type="text/css">
	  body {
	    background-image:url('assets/img/login_bg.png');
	  }
	  .loginform {
        width: 600px;
        height: 190px;
		
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
		margin: 10px auto 0px auto;
		width: 532px;
	  }
	</style>
  </head>
  <body>
    <div class="loginform">
	  <form class="form-horizontal" action='login.php' method='post' accept-charset='UTF-8'>
	    <div class="<?php if(isset($_GET['error'])){ echo 'has-error'; } ?>"><input type="text" class="form-control" id="username" name="username" placeholder="Username"></div>
		<br />
		  <div class="<?php if(isset($_GET['error'])){ echo 'has-error'; } ?>"><input type="password" class="form-control" id="password" name="password" placeholder="Password"></div>
		<br />
		<button type="submit" name='Submit' class="btn btn-success btn-block">Login</button>
	  </form>
	</div>

    <script src="http://code.jquery.com/jquery.js"></script>
	<script src="assets/js/bootstrap.min.js"></script>
  </body>
</html>