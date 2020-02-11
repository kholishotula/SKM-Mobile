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
        $result = array();
        $getId = mysqli_query($database, "SELECT id_isi_submit FROM `isi_submit` ORDER BY id_isi_submit DESC LIMIT 1");
        while ($row = mysqli_fetch_array($getId, MYSQLI_ASSOC)) {
            array_push($result,array(
                "id_isi_submit" => $row['id_isi_submit']));
        };
        echo json_encode( $result );
    }
    else{
        echo 'Gagal';
    }

	mysqli_close($database);
?>