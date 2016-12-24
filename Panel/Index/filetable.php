<?php
  if (file_exists("config.php")) {
    include("config.php");
  } else {
    die();
  }
  
  include("functions.php");
  
  $getslaves = 'SELECT * FROM files';
?>
<!DOCTYPE html>
<html>
  <head>
  	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="assets/js/jquery.tablesorter.min.js"></script>
	<script src="assets/js/jquery.tablesorter.widgets.min.js"></script>
	<script>
	    $("#files").tablesorter({ 
          widgets: ['saveSort'] 
        }); 
		
		function deletefile(filename){
		  var agree=confirm("Are you sure you want to delete?");
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
		  xmlhttp.open("GET","deletefile.php?file="+filename,true);
          xmlhttp.send();
		}
	</script>
  </head>
  <body>
  <table class="table table-bordered table-hover" id="files">
    <thead>
		    <tr>
			  <th>UID</th>
			  <th>File</th>
			  <th>Options</th>
			</tr>
	</thead>
	<tbody>
  <?php 
  foreach ($connect->query($getslaves) as $row) {
    $file = preg_replace("#\{(.*)\}#", "", $row['file']);
	echo '<tr><td>' . $row['uid'] . '</td>';
	echo '<td>' . $file . '</td>';
	echo '<td><div class="btn-group"><button type="button" class="btn btn-default btn-small dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button><ul class="dropdown-menu" style="left: -425%;"><li><a href="dlfiles/' . $row['file'] . '"><span class="glyphicon glyphicon-download-alt"></span> Download</a></li><li><a href="#" onclick="deletefile(\'' . $row['file'] . '\');"><span class="glyphicon glyphicon-trash"></span> Delete</a></li></ul></div></td></tr>';
  }?>
    </tbody>
	</table>
  </body>
</html>