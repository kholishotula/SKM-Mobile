<?php 
	include('koneksi.php');
	
    $nama = $_GET['nama'];
    $asal = $_GET['asal'];
    $pekerjaan = $_GET['pekerjaan'];
    $instansi = $_GET['instansi'];
    $jk = $_GET['jenis'];
    $pendidikan = $_GET['pendidikan'];

	$sql = "INSERT INTO `responden` (nama_responden, alamat_asal, pekerjaan_jabatan, instansi, jenis_kelamin, pendidikan_terakhir)
            VALUES ('$nama', '$asal', '$pekerjaan', '$instansi', '$jk', '$pendidikan')";
	
	if(mysqli_query($database, $sql)){
        $result = array();
        $getId = mysqli_query($database, "SELECT id_responden FROM `responden` ORDER BY id_responden DESC LIMIT 1");
        while ($row = mysqli_fetch_array($getId, MYSQLI_ASSOC)) {
            array_push($result,array(
                "id_responden" => $row['id_responden']));
        };
        echo json_encode( $result );
    }
    else{
        echo 'Gagal';
    }

	mysqli_close($database);
?>