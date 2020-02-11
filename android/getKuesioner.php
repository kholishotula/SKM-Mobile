<?php 
	include('koneksi.php');
	
	$id = intval($_GET['id']);

	$sql = "SELECT pertanyaan.*, terdiri_dari.id_kuesioner
			FROM `pertanyaan` JOIN `terdiri_dari` ON pertanyaan.id_pertanyaan = terdiri_dari.id_pertanyaan,
				(SELECT id_kuesioner FROM `kuesioner` WHERE id_layanan = $id AND aktif = 1) id_kues
			WHERE pertanyaan.id_pertanyaan IN
				(SELECT id_pertanyaan FROM `terdiri_dari` WHERE id_kuesioner = id_kues.id_kuesioner) AND
				terdiri_dari.id_kuesioner = id_kues.id_kuesioner";
	
	$result = array();
	$r = mysqli_query($database, $sql);

	while ($row = mysqli_fetch_array($r, MYSQLI_ASSOC)) {
		array_push($result,array(
			"id_kuesioner" => $row['id_kuesioner'],
			"id_pertanyaan" => $row['id_pertanyaan'],
			"konten_pertanyaan" => $row['konten_pertanyaan']));
	};
 
	//Menampilkan dalam format JSON
	echo json_encode($result);
	
	mysqli_close($database);
?>