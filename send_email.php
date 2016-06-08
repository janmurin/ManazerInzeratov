<?php

if(isset($_POST['email'])) {

    function died($error) {
        // your error code can go here
         echo "We are very sorry, but there were error(s) found with the form you submitted. ";
         echo "These errors appear below.<br /><br />";
         echo $error."<br /><br />";
         echo "Please go back and fix these errors.<br /><br />";
         die();
     }
 
    // validation expected data exists
     if(!isset($_POST['subject']) ||
         !isset($_POST['sender']) ||
		 !isset($_POST['recipient']) ||
         !isset($_POST['message'])) {
         died('We are sorry, but there appears to be a problem with the form you submitted.');      
     }

     $sender = $_POST['sender']; // required
     $recipient = $_POST['recipient]']; // required
     $subject = $_POST['subject']; // required
     $message = $_POST['message']; // not required
 
    $error_message = "";
     $email_exp = '/^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/'; 
  if(!preg_match($email_exp,$sender)) {
     $error_message .= 'The Email Address you entered does not appear to be valid.<br />';
   }
   if(!preg_match($email_exp,$recipient)) {
     $error_message .= 'The Email Address you entered does not appear to be valid.<br />';
   }
     $string_exp = "/^[A-Za-z .'-]+$/";
   if(!preg_match($string_exp,$subject)) {
     $error_message .= 'The First Name you entered does not appear to be valid.<br />';
   }
   if(strlen($message) < 2) {
     $error_message .= 'The Comments you entered do not appear to be valid.<br />';
   }
   if(strlen($error_message) > 0) {
     died($error_message);
   }
   function clean_string($string) {
       $bad = array("content-type","bcc:","to:","cc:","href");
       return str_replace($bad,"",$string);
     }
 
 // Always set content-type when sending HTML email
$headers = "MIME-Version: 1.0" . "\r\n";
$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";

// More headers
$headers .= 'From: Nove Inzeraty <' . $sender . '>' . "\r\n";
//$headers .= 'Cc: myboss@example.com' . "\r\n";
// create email headers
 $headers .= 'X-Mailer: PHP/' . phpversion();
 @mail($recipient, $subject, $message, $headers); 
}
 ?>

