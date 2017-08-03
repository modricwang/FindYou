<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello PHP!</title>
</head>
<body>
<?php
function _session_open($save_path=null,$session_name=null) {
    global $handle;
    $host=null;
    $username=null;
    $password=null;
    $database=null;
    $handle = mysql_connect($host, $username, $password) or die("GG");
    mysql_select_db($database);
}
function _session_close() {
    global $handle;
    mysql_close($handle);
    return true;
}
function _session_read($key) {
    global $handle;
    $time=time();
    $sql= "select session_data from tb_session where session_key = '$key' and session_time > $time";
    //$sql= "select session_data from tb_session where  session_time > $time";
    $result=mysql_query($sql, $handle);
    $row=mysql_fetch_array($result);
    if ($row) {
        return $row['session_data'];
    } else {
        return false;
    }
}
function _session_write($key, $data) {
    global $handle;
    $time=1*60;
    $lapse_time =time()+$time;
    $sql="select session_data from tb_session where session_key = '$key' and session_time > $lapse_time";
    $result = mysql_query($sql, $handle);
    if (mysql_num_rows($result)==0) {
        $sql="insert into tb_session values('$key','$data','$lapse_time')";
        $result = mysql_query($sql,$handle);
    } else {
        $sql="delete from tb_session where session_key = '$key'";
        mysql_query($sql, $handle);
        $sql="insert into tb_session values('$key','$data','$lapse_time')";
        $result = mysql_query($sql,$handle);
    }
    return $result;
}
function _session_destroy($key) {
    global $handle;
    $sql="delete from tb_session where session_key = '$key'";
    $result=mysql_query($sql, $handle);
    return ($result);

}
function _session_gc($expiry_time) {
    echo "wwwwwwww<br>";
    global $handle;
    $lapse_time =time();
    $sql = "delete from tb_session where session_time < $lapse_time";
    $result=mysql_query($sql,$handle);
    return ($result);
}
function run_time() {
    list($msec,$sec)=explode(" ",microtime());
    return ((float)$msec+(float)$sec);
}
?>

<?php
$start_time=run_time();
//session_set_save_handler("_session_open","_session_close","_session_read","_session_write","_session_destroy","_session_gc");
//session_start();
//$_SESSION['IP']=$_SERVER['REMOTE_ADDR'];
echo "SERVER_NAME:\t".$_SERVER['SERVER_NAME']."<br>";
echo "SERVER_ADDR:\t".$_SERVER['SERVER_ADDR']."<br>";
echo "YOUR_ADDR:\t".$_SERVER['REMOTE_ADDR']."<br>";
$timezone="Asia/Hong_Kong";
date_default_timezone_set($timezone);
echo "date:\t".date("Y-m-d",mktime())."<br>";
echo "time:\t".date("H:i:s",mktime())."<br>";
echo $_GET[arg0]."<br>";
switch ($_GET[lmbs]) {
    case "test":
        echo "GET!"."<br>";
        break;
    default:
        echo "Missed!"."<br>";
        break;
}
if ($_POST[arg1]!=null)
echo $_POST[arg1]." has arrived!<br>";
$end_time=run_time();
echo "运行时间：\t".sprintf("%.7f",$end_time-$start_time)."s<br>";
echo "运行时间：\t".($end_time-$start_time)."s<br>";
if ($_SESSION!=null) echo "OK!<br>";
print_r($_SESSION);
?>
<a href="login/index.php">hhh</a>
<a href="json.php">json</a>
<a href="../index.php">233</a>
<a href="../session.php">666</a>
<a href="../show.php">show</a>
<a href="../clear.php">clear</a>
</body>
</html>
