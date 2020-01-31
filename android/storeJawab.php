<?php 
	include('koneksi.php');
    
    $id_resp = $_GET['id_responden'];
    $id_kues = $_GET['id_kuesioner'];
    $skor = $_GET['skor'];
    $kritik = $_GET['kritik'];
    $tgl = date('Y-m-d', time());

	$sql = "INSERT INTO `isi_submit` (id_responden, id_kuesioner, skor_akhir, kritik_saran, tgl_submit)
            VALUES ('$id_resp', '$id_kues', '$skor', '$kritik', '$tgl')";
	
	if(mysqli_query($database, $sql)){
        echo 'Data Submit';
    }
    else{
        echo 'Gagal';
    }

	mysqli_close($database);
?>