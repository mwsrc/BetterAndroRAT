<?php
$uid = $_GET['uid'];

$dirname = "dlfiles/" . $uid . "/";

$images = glob($dirname."*.*");

echo '<button type="button" class="btn btn-default btn-small" onclick="deletePics(\'' . $uid . '\');">Delete All</button><br />';
foreach($images as $image) {
echo '<a href="'.$image.'" target="_blank"><img class="getimgimg" style="width: 250px; margin: 5px 5px 5px 5px;" src="'.$image.'" /></a>';
}
?>