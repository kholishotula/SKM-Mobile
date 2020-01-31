<?php 
	include('koneksi.php');
	
	$id = intval($_GET['id']);

	$sql = "SELECT * FROM `pertanyaan` WHERE id_pertanyaan IN
			(SELECT id_pertanyaan FROM `terdiri_dari` WHERE id_kuesioner = $id)";
	
	$result = array();
	$r = mysqli_query($database, $sql);

	while ($row = mysqli_fetch_array($r, MYSQLI_ASSOC)) {
		array_push($result,array(
			"id_pertanyaan" => $row['id_pertanyaan'],
			"konten_pertanyaan" => $row['konten_pertanyaan']));
	};
 
	//Menampilkan dalam format JSON
	echo json_encode($result);
	
	mysqli_close($database);
?>