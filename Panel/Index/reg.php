<?php
$allowedDomains = array("www.pizzachip.com", "pizzachip.com");

if (in_array($_SERVER['HTTP_HOST'], $allowedDomains)) {
	$validDomain = "true";
} else {
	$validDomain = "false";
}
?>