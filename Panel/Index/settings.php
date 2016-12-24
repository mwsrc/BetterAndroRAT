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
  if (empty($_SESSION['code'])) {
    header( 'Location: index.php' ) ;
    die();
  }
  
  include("functions.php");
?>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  
    <title>Settings</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet" media="screen">
	<link href="assets/css/bootstrap-glyphicons.css" rel="stylesheet" media="screen">
	
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="assets/js/jquery.tablesorter.min.js"></script>
	<script src="assets/js/jquery.tablesorter.widgets.min.js"></script>
    <style type="text/css">
	  body {
	    background-image:url('assets/img/login_bg.png');
	  }
	  .main {
        width: 1350px;
        height: 600px;
		
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
	</style>
  </head>
  <body>
    <div class="main">
	  <div class="logo">
	  </div>
	  <div class="row">
	   <form class="form-horizontal" method="post" action="applysettings.php">
	    <div class="col-lg-6">
		  <h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Settings</h1>
		  <hr />
		    <div class="form-group">
              <label for="dbusername" class="col-lg-4 control-label">Database Username</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="dbusername" name="dbusername" value="<?php echo $dbuser; ?>" placeholder="">
              </div>
            </div>
		    <div class="form-group">
              <label for="dbpassword" class="col-lg-4 control-label">Database Password</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="dbpassword" name="dbpassword" value="" placeholder="Leave Blank to No Change">
              </div>
            </div>
		    <div class="form-group">
              <label for="dbhost" class="col-lg-4 control-label">Database Host</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="dbhost" name="dbhost" value="<?php echo $dbhost; ?>" placeholder="">
              </div>
            </div>
		    <div class="form-group">
              <label for="dbname" class="col-lg-4 control-label">Database Name</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="dbname" name="dbname" value="<?php echo $dbname; ?>" placeholder="">
              </div>
            </div>
			<hr />
		    <div class="form-group">
              <label for="devicetablerefr" class="col-lg-5 control-label">Bot List Refresh Time (Sec)</label>
              <div class="col-lg-7">
                <input type="number" class="form-control" id="devicetablerefr" name="devicetablerefr" value="<?php echo $devicestablerefreshspeed / 1000; ?>" placeholder="">
              </div>
            </div>
		    <div class="form-group">
              <label for="filetablerefr" class="col-lg-5 control-label">File List Refresh Time (Sec)</label>
              <div class="col-lg-7">
                <input type="number" class="form-control" id="filetablerefr" name="filetablerefr" value="<?php echo $filestablerefreshspeed / 1000; ?>" placeholder="">
              </div>
            </div>
		    <div class="form-group">
              <label for="historyboxrefr" class="col-lg-5 control-label">History Box Refresh Time (Sec)</label>
              <div class="col-lg-7">
                <input type="number" class="form-control" id="historyboxrefr" name="historyboxrefr" value="<?php echo $messageboxrefreshspeed / 1000; ?>" placeholder="">
              </div>
            </div>
		</div>
		<div class="col-lg-6">
		  <div style="margin: 0px 60px 0px 0px;">
		    <div class="form-group">
              <label for="username" class="col-lg-4 control-label">Username</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="username" name="username" value="<?php echo $username; ?>" placeholder="">
              </div>
            </div>
		    <div class="form-group">
              <label for="password" class="col-lg-4 control-label">Password</label>
              <div class="col-lg-8">
                <input type="text" class="form-control" id="password" name="password" placeholder="Leave Blank for No Change">
              </div>
            </div>
			<hr />
		    <div class="form-group">
              <label for="botoffline" class="col-lg-4 control-label">Bot Offline Time (Min)</label>
              <div class="col-lg-8">
                <input type="number" class="form-control" id="botoffline" name="botoffline" value="<?php echo $offlineminutes; ?>" placeholder="">
              </div>
            </div>
			<hr />
		    <div class="form-group">
              <label for="timezone" class="col-lg-4 control-label">TimeZone</label>
              <div class="col-lg-8">
                <select class="form-control" id="timezone" name="timezone">
				<?php echo "<option>" . $timezonesetting . "</option>" ?>
<option value="Pacific/Midway">(GMT-11:00) Midway Island, Samoa</option>
<option value="America/Adak">(GMT-10:00) Hawaii-Aleutian</option>
<option value="Etc/GMT+10">(GMT-10:00) Hawaii</option>
<option value="Pacific/Marquesas">(GMT-09:30) Marquesas Islands</option>
<option value="Pacific/Gambier">(GMT-09:00) Gambier Islands</option>
<option value="America/Anchorage">(GMT-09:00) Alaska</option>
<option value="America/Ensenada">(GMT-08:00) Tijuana, Baja California</option>
<option value="Etc/GMT+8">(GMT-08:00) Pitcairn Islands</option>
<option value="America/Los_Angeles">(GMT-08:00) Pacific Time (US & Canada)</option>
<option value="America/Denver">(GMT-07:00) Mountain Time (US & Canada)</option>
<option value="America/Chihuahua">(GMT-07:00) Chihuahua, La Paz, Mazatlan</option>
<option value="America/Dawson_Creek">(GMT-07:00) Arizona</option>
<option value="America/Belize">(GMT-06:00) Saskatchewan, Central America</option>
<option value="America/Cancun">(GMT-06:00) Guadalajara, Mexico City, Monterrey</option>
<option value="Chile/EasterIsland">(GMT-06:00) Easter Island</option>
<option value="America/Chicago">(GMT-06:00) Central Time (US & Canada)</option>
<option value="America/New_York">(GMT-05:00) Eastern Time (US & Canada)</option>
<option value="America/Havana">(GMT-05:00) Cuba</option>
<option value="America/Bogota">(GMT-05:00) Bogota, Lima, Quito, Rio Branco</option>
<option value="America/Caracas">(GMT-04:30) Caracas</option>
<option value="America/Santiago">(GMT-04:00) Santiago</option>
<option value="America/La_Paz">(GMT-04:00) La Paz</option>
<option value="Atlantic/Stanley">(GMT-04:00) Faukland Islands</option>
<option value="America/Campo_Grande">(GMT-04:00) Brazil</option>
<option value="America/Goose_Bay">(GMT-04:00) Atlantic Time (Goose Bay)</option>
<option value="America/Glace_Bay">(GMT-04:00) Atlantic Time (Canada)</option>
<option value="America/St_Johns">(GMT-03:30) Newfoundland</option>
<option value="America/Araguaina">(GMT-03:00) UTC-3</option>
<option value="America/Montevideo">(GMT-03:00) Montevideo</option>
<option value="America/Miquelon">(GMT-03:00) Miquelon, St. Pierre</option>
<option value="America/Godthab">(GMT-03:00) Greenland</option>
<option value="America/Argentina/Buenos_Aires">(GMT-03:00) Buenos Aires</option>
<option value="America/Sao_Paulo">(GMT-03:00) Brasilia</option>
<option value="America/Noronha">(GMT-02:00) Mid-Atlantic</option>
<option value="Atlantic/Cape_Verde">(GMT-01:00) Cape Verde Is.</option>
<option value="Atlantic/Azores">(GMT-01:00) Azores</option>
<option value="Europe/Belfast">(GMT) Greenwich Mean Time : Belfast</option>
<option value="Europe/Dublin">(GMT) Greenwich Mean Time : Dublin</option>
<option value="Europe/Lisbon">(GMT) Greenwich Mean Time : Lisbon</option>
<option value="Europe/London">(GMT) Greenwich Mean Time : London</option>
<option value="Africa/Abidjan">(GMT) Monrovia, Reykjavik</option>
<option value="Europe/Amsterdam">(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna</option>
<option value="Europe/Belgrade">(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague</option>
<option value="Europe/Brussels">(GMT+01:00) Brussels, Copenhagen, Madrid, Paris</option>
<option value="Africa/Algiers">(GMT+01:00) West Central Africa</option>
<option value="Africa/Windhoek">(GMT+01:00) Windhoek</option>
<option value="Asia/Beirut">(GMT+02:00) Beirut</option>
<option value="Africa/Cairo">(GMT+02:00) Cairo</option>
<option value="Asia/Gaza">(GMT+02:00) Gaza</option>
<option value="Africa/Blantyre">(GMT+02:00) Harare, Pretoria</option>
<option value="Asia/Jerusalem">(GMT+02:00) Jerusalem</option>
<option value="Europe/Minsk">(GMT+02:00) Minsk</option>
<option value="Asia/Damascus">(GMT+02:00) Syria</option>
<option value="Europe/Moscow">(GMT+03:00) Moscow, St. Petersburg, Volgograd</option>
<option value="Africa/Addis_Ababa">(GMT+03:00) Nairobi</option>
<option value="Asia/Tehran">(GMT+03:30) Tehran</option>
<option value="Asia/Dubai">(GMT+04:00) Abu Dhabi, Muscat</option>
<option value="Asia/Yerevan">(GMT+04:00) Yerevan</option>
<option value="Asia/Kabul">(GMT+04:30) Kabul</option>
<option value="Asia/Yekaterinburg">(GMT+05:00) Ekaterinburg</option>
<option value="Asia/Tashkent">(GMT+05:00) Tashkent</option>
<option value="Asia/Kolkata">(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi</option>
<option value="Asia/Katmandu">(GMT+05:45) Kathmandu</option>
<option value="Asia/Dhaka">(GMT+06:00) Astana, Dhaka</option>
<option value="Asia/Novosibirsk">(GMT+06:00) Novosibirsk</option>
<option value="Asia/Rangoon">(GMT+06:30) Yangon (Rangoon)</option>
<option value="Asia/Bangkok">(GMT+07:00) Bangkok, Hanoi, Jakarta</option>
<option value="Asia/Krasnoyarsk">(GMT+07:00) Krasnoyarsk</option>
<option value="Asia/Hong_Kong">(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi</option>
<option value="Asia/Irkutsk">(GMT+08:00) Irkutsk, Ulaan Bataar</option>
<option value="Australia/Perth">(GMT+08:00) Perth</option>
<option value="Australia/Eucla">(GMT+08:45) Eucla</option>
<option value="Asia/Tokyo">(GMT+09:00) Osaka, Sapporo, Tokyo</option>
<option value="Asia/Seoul">(GMT+09:00) Seoul</option>
<option value="Asia/Yakutsk">(GMT+09:00) Yakutsk</option>
<option value="Australia/Adelaide">(GMT+09:30) Adelaide</option>
<option value="Australia/Darwin">(GMT+09:30) Darwin</option>
<option value="Australia/Brisbane">(GMT+10:00) Brisbane</option>
<option value="Australia/Hobart">(GMT+10:00) Hobart</option>
<option value="Asia/Vladivostok">(GMT+10:00) Vladivostok</option>
<option value="Australia/Lord_Howe">(GMT+10:30) Lord Howe Island</option>
<option value="Etc/GMT-11">(GMT+11:00) Solomon Is., New Caledonia</option>
<option value="Asia/Magadan">(GMT+11:00) Magadan</option>
<option value="Pacific/Norfolk">(GMT+11:30) Norfolk Island</option>
<option value="Asia/Anadyr">(GMT+12:00) Anadyr, Kamchatka</option>
<option value="Pacific/Auckland">(GMT+12:00) Auckland, Wellington</option>
<option value="Etc/GMT-12">(GMT+12:00) Fiji, Kamchatka, Marshall Is.</option>
<option value="Pacific/Chatham">(GMT+12:45) Chatham Islands</option>
<option value="Pacific/Tongatapu">(GMT+13:00) Nuku'alofa</option>
<option value="Pacific/Kiritimati">(GMT+14:00) Kiritimati</option>
				</select>
              </div>
            </div>
			<hr />
		    <div class="form-group">
              <label for="messageboxscroll" class="col-lg-5 control-label">Auto Scroll Message Box</label>
              <div class="col-lg-7">
                <select class="form-control" id="messageboxscroll" name="messageboxscroll">
				<?php if ($autoscrolltextbox == true){
				  echo "<option>Yes</option>";
				  echo "<option>No</option>";
				  } else{
				  echo "<option>No</option>";
				  echo "<option>Yes</option>";
				  }
				?>
				</select>
              </div>
            </div>
		    <div class="form-group">
              <label for="postboxsize" class="col-lg-5 control-label">History Box Font Size</label>
              <div class="col-lg-7">
                <input type="number" class="form-control" id="postboxsize" name="postboxsize" value="<?php echo $postboxtextsize; ?>" placeholder=""><br />
				<button type="button" onClick="window.location='clearmessages.php'" class="btn btn-default btn-small btn-block">Clear Message Box</button>
				<button type="button" onClick="window.location='clearawaiting.php'" class="btn btn-default btn-small btn-block">Clear Awaiting Commands</button>
              </div>
            </div>
			<br />
			<button type="submit" class="btn btn-primary btn-small btn-block">Save Settings</button>
			<button type="button" onClick="window.location='control.php'" class="btn btn-default btn-small btn-block">Exit Without Saving</button>
		  </div>
		</div>
	  </form>
	</div>
  </body>
</html>