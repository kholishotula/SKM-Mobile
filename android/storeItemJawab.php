<?php 
	include('koneksi.php');
    
    $id_isi_submit = $_GET['id_isi_submit'];
    $id_pertanyaan = $_GET['id_pertanyaan'];
    $nilai = $_GET['nilai'];

	$sql = "INSERT INTO `submission` (id_isi_submit, id_pertanyaan, nilai)
            VALUES ('$id_isi_submit', '$id_pertanyaan', '$nilai')";
	
	if(mysqli_query($database, $sql)){
        echo 'Data Submit';
    }
    else{
        echo 'Gagal';
    }

	mysqli_close($database);
?>