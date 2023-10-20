<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');
error_reporting(0);
require_once FCPATH . 'application/libraries/Facebook/autoload.php';
require_once FCPATH . 'application/libraries/google-sdk/vendor/autoload.php';
//require_once FCPATH . 'application/libraries/google-sdk/vendor/autoload.php';
// echo FCPATH;
// die('herere');

class Staff extends CI_Controller {
	
 public function __construct(){
 
	parent::__construct();
	
	if($this->session->userdata("memberlogged")===true && $this->session->userdata("memtype")==="Staff"){
			
				#Load Facebook
				$this->config->load("facebook",TRUE);
				$config = $this->config->item('facebook');
				$this->load->library('Facebook', $config);
				
				#Load Instagram
				$this->config->load("instagram",TRUE);
				$configInstagram = $this->config->item('instagram');
				$this->load->library('Instagram',$configInstagram);

				/*Load Twitter*/
				$this->load->library('Twconnect');
				$this->config->load("twitter",TRUE);
				$configTwitter = $this->config->item('twitter');
				
				/*For Twitter feed*/
				$this->ACCESS_TOKEN = $configTwitter['access_token'];
				$this->ACCESS_TOKEN_SECRET = $configTwitter['access_token_secret'];
				$this->CONSUMER_KEY = $configTwitter['consumer_key'];
				$this->CONSUMER_SECRET = $configTwitter['consumer_secret'];
								
				$this->load->helper('form');
				$this->load->library('form_validation');
				$this->load->library('email');
				$this->load->model("db_model","model",TRUE);

				/* YouTube Load */
				require_once(APPPATH . 'libraries/google-api/src/Google/autoload.php');
				$this->config->load("google", TRUE);
				$this->configArray = $this->config->item('google');
				$this->client = new Google_Client();
				$this->client->setClientId($this->configArray['setClientId']);
				$this->client->setClientSecret($this->configArray['setClientSecret']);
				$this->client->setApplicationName($this->configArray['setApplicationName']);
				$this->client->setRedirectUri($this->configArray['setRedirectUri']);
				$this->client->setDeveloperKey($this->configArray['setDeveloperKey']);
				$this->client->setScopes($this->configArray['setScopes']);
				$this->client->setAccessType($this->configArray['setAccessType']);
				$this->client->setApprovalPrompt('force');
				$this->objOAuthService = new Google_Service_Oauth2($this->client);
		}
		else{
			redirect('home');
	}
 }
	
 #The Dashbaord
 public function index(){
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
    	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
			'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
			  					'redirect_uri' => '"'.$config['base_url'].'"index.php/staff/facebook_redirect/'
							));
			
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
			
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
								
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);

		$sql="Select * from events e, users u where u.userid=e.ownerid and u.memtype<>'Staff' and e.status='Active' and 
			(find_in_set('all',event_state) or find_in_set('%".strtolower($this->session->userdata("state"))."%',event_state)) ";
		$data["eventlist"]=$this->model->allSelects($sql);
			
		/*$sql="Select * from events e, users u where u.userid=e.ownerid and u.memtype<>'Staff' and e.status='Active' 
			and (find_in_set('all',event_state) or find_in_set('%".strtolower($this->session->userdata("state"))."%',event_state))";*/
			
		$sql="SELECT * FROM events e 
			  LEFT JOIN company_ref cr ON cr.companyID= e.ownerid 
		      LEFT JOIN users ON users.userid= e.ownerid 
			  WHERE cr.userid=".$this->session->userdata("userid")." and e.status='Active' AND cr.ref_status='Active' 
			  and (find_in_set('all',e.event_state) or find_in_set('".strtolower($this->session->userdata("state"))."',e.event_state)) ORDER BY e.posted_date desc";
		$data["widgeteventlist"]=$this->model->allSelects($sql);
					
		$sql="SELECT * FROM news n 
			  LEFT JOIN company_ref cr ON cr.companyID= n.ownerid 
		      LEFT JOIN users ON users.userid = n.ownerid 
			  WHERE cr.userid=".$this->session->userdata("userid")." and n.status='Active' AND cr.ref_status='Active'
			  AND (find_in_set('all',n.newsstate) or find_in_set('".strtolower($this->session->userdata("state"))."',n.newsstate))";			  
		$data["widgetnewslist"]=$this->model->allSelects($sql);
		 					
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);

		//$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
		$sql="(Select u.user_logo as emplogo,'ATEmp' AS empType 
			   from users u
			   LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   where cr.userid=".$this->session->userdata("userid")." AND cr.ref_status='Active')
			 	 UNION
			  (Select ups.sponsor_logo AS emplogo,'nonATEmp' AS empType
			   from user_prostaff_sponsors ups
			   where ups.prostaff_id=".$this->session->userdata("userid").")";

		$data["emplist"]=$this->model->allSelects($sql);
												
		$data['content']=$this->load->view('staff/staff-dashboard',$data,true);
		$this->load->view('layout-inner',$data);			
 }

#SETUP WIZARD
 public function mediaSetup(){


	
 
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
		$data['insta_basic_url'] = 'https://api.instagram.com/oauth/authorize
		?client_id=775682203383178
		&redirect_uri='.base_url().'index.php/staff/instabasicauth
		&scope=user_profile,user_media
		&response_type=code';
								
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$connIG=$this->model->allSelects($sql);
		$data['connIG'] = $connIG;

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
		
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-media-setup',$data,true);
		$this->load->view('layout-inner',$data);
 }

# Load FB Feeds Dashboard.
 public function loadFB(){

		$fb_append_qry = ''; $fb_append_qry2="";		
		$append_qry =""; $firstCondt =""; $dateCondt=""; $sponsorCondt='';$staffIDCondt="all";

		if(isset($_POST['search_from_date'],$_POST['search_to_date'],$_POST['search_by_pro'])){
			$dateCondt = " and created_time>= '".$_POST["search_from_date"]."' and created_time <='".$_POST['search_to_date']."'";
			$staffIDCondt = $_POST["search_by_pro"];
		}

		if(isset($_POST["search_by_manufacturer"]) && $_POST["search_by_manufacturer"]>0 && $_POST["search_by_manufacturer"]!='all'){
			
			$sql = "select userid from users where memtype='Employer' AND userid=".$_POST["search_by_manufacturer"];
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$fbkeywords	=	getKeywords($_POST["search_by_manufacturer"],'fbkeywords');
			}else {
				
				$sql = "Select ups.fbkeywords from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$fbkeywords = $TempInfo[0]['fbkeywords'];
				}
			}
		} else {
			$fbkeywords	=	getKeywords($this->session->userdata("userid"),'fbkeywords');
		}
if($this->session->userdata("userid")!=41){	
	
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				if($val != '') {
					$keyword_query[]	= " (uf.story like '".trim($val)."%' or uf.story like '%".trim($val)."%') ";
			
				}
				}
			$append_qry	=	implode(' or ',$keyword_query);			
			$fb_append_qry2	.=	" and ( $append_qry )";
		} 		 

		$sql = "SELECT uf.id AS id,uf.photo_url as image_url,uf.likes_count,uf.link,uf.story as story, uf.comment_count,
				uf.share_count, uf.view_count, uf.created_time as created_time ,u.FirstLastname, u.userid 
				FROM user_feed uf
				LEFT JOIN users u ON u.userid = uf.user_id 
				WHERE uf.user_id=".$this->session->userdata("userid")." $fb_append_qry2 $dateCondt ORDER BY created_time desc";			

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt=='all' && $staffIDCondt != $this->session->userdata("userid") ){
		
		$sql = "SELECT * FROM (
					SELECT uf.id AS id,uf.photo_url as image_url,uf.likes_count,uf.link,uf.story as story, uf.comment_count,
					uf.share_count, uf.view_count, uf.created_time as created_time ,u.FirstLastname, u.userid 
					FROM user_feed uf
					LEFT JOIN users u ON u.userid = uf.user_id 
					WHERE uf.user_id=".$this->session->userdata("userid")." $fb_append_qry2 $dateCondt
					
					UNION ALL
					
					SELECT DISTINCT uf.id AS id,uf.photo_url as image_url,uf.likes_count,uf.link,uf.story as story,uf.comment_count,
					uf.share_count,uf.view_count,uf.created_time as created_time ,u.FirstLastname, u.userid
					from user_feed uf
					LEFT JOIN users u ON u.userid = uf.user_id 
					LEFT JOIN company_ref cr on cr.userid=uf.user_id
					WHERE cr.companyID=".$this->session->userdata("userid")." $fb_append_qry2 $dateCondt
				 ) resultSet ORDER BY created_time desc";		
		  }	

				
		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt!='all' && $staffIDCondt != $this->session->userdata("userid")){
		
		$sql = "SELECT DISTINCT uf.id AS id,uf.photo_url as image_url,uf.likes_count,uf.link,uf.story as story,
						uf.comment_count, uf.share_count,uf.view_count,uf.created_time as created_time ,u.FirstLastname, u.userid 
						FROM user_feed uf
						LEFT JOIN users u ON u.userid = uf.user_id 
						LEFT JOIN company_ref cr on cr.userid=uf.user_id
						WHERE uf.user_id =".$staffIDCondt." AND cr.companyID=".$this->session->userdata("userid")." 
						$fb_append_qry2 $dateCondt ORDER BY uf.created_time desc";
		
		  }	
}else{

		$sql = "SELECT uf.id AS id,uf.photo_url as image_url,uf.likes_count,uf.link,uf.story as story, uf.comment_count,
				uf.share_count, uf.view_count, uf.created_time as created_time ,u.FirstLastname, u.userid 
				FROM user_feed uf
				LEFT JOIN users u ON u.userid = uf.user_id 
				WHERE uf.user_id=".$this->session->userdata("userid")." $fb_append_qry2 $dateCondt ORDER BY created_time desc";			

}
				//   echo '<pre>';
				//   print_r($sql);
				//   die('here');
		$widgetFblist	=	$this->model->allSelects($sql);	
		$data["widgetFblist"] = $widgetFblist;	

		if(isset($_POST["format"]) && $_POST["format"]=='report'){
			$this->load->view('staff/load/fbfeeds-reports-grid',$data);
		} else {
			$this->load->view('staff/load/fbfeeds',$data);
		}
 }

 function unique_multidim_array($array, $key){
    $temp_array = array();
    $i = 0;
    $key_array = array();
   
    foreach($array as $val){
        if(!in_array($val[$key],$key_array)){
            $key_array[$i] = $val[$key];
            $temp_array[$i] = $val;
        }
        $i++;
    }
    return $temp_array;
 }

 public function fbTopphoto(){
 
 		$fb_append_qry = ''; $fb_append_qry2="";		
		$append_qry =""; $firstCondt ="";
		
		$fbkeywords	=	getKeywords($this->session->userdata("userid"),'fbkeywords');
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
				$keyword_query2[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%') "; 
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$append_qry2 = implode(' or ', $keyword_query2);
			$fb_append_qry2	.=	" and ($append_qry)";
			$fb_append_qry	.=	" and ($append_qry2)";
		}
						
		$topFBphtsql = "(select DISTINCT(user_photo.id),user_photo.image_url as image_url,user_photo.likes_count,link,text from user_photo 
						where user_id=".$this->session->userdata("userid")." $fb_append_qry 
						AND user_photo.image_url!='')
						UNION
						(select DISTINCT(user_feed.id),user_feed.photo_url as image_url,user_feed.likes_count,user_feed.link,user_feed.story from user_feed 
						where user_feed.feed_type='photo' and user_feed.user_id=".$this->session->userdata("userid")." $fb_append_qry2 
						AND user_feed.photo_url!='' )
						ORDER BY likes_count desc LIMIT 5";
			
		$data['topchartFBpht'] = $this->model->allSelects($topFBphtsql);
		$this->load->view('staff/load/topphoto',$data);
 }

 #Twitter Data Load Dashboard
 public function twtDataLoad(){

		$tw_append_qry = ''; $dateCondt =''; $staffIDCondt = 'all';

		if(isset($_POST["search_from_date"],$_POST['search_to_date'])){
			$dateCondt = " and created_at>= '".$_POST["search_from_date"]."' and created_at <='".$_POST['search_to_date']."'";
		}

		if(isset($_POST["search_by_pro"])){
			$staffIDCondt = $_POST["search_by_pro"];
		}

		if(isset($_POST["search_by_manufacturer"]) && $_POST["search_by_manufacturer"]>0 && $_POST["search_by_manufacturer"]!='all'){
			//$twtkeywords	=	getKeywords($_POST["search_by_manufacturer"],'twtkeywords');

			$sql = "SELECT userid FROM users 
					WHERE memtype='Employer' AND userid=".$_POST["search_by_manufacturer"];
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$twtkeywords	=	getKeywords($_POST["search_by_manufacturer"],'twtkeywords');
			}else {
				
				$sql = "SELECT ups.twitterkeywords FROM user_prostaff_sponsors ups
						WHERE ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				
				if(count($TempInfo)>0){
					$twtkeywords = $TempInfo[0]['twitterkeywords'];
				}
			}
		
		} else {
				$twtkeywords = getKeywords($this->session->userdata("userid"),'twtkeywords');
		}

		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$tw_append_qry	.=	" and ($append_qry)";
		}
		
		$sql ="SELECT ut.*,u.userid FROM user_tweets ut 
			   LEFT JOIN users u ON u.userid = ut.user_id 
			   WHERE ut.user_id=".$this->session->userdata("userid")." $tw_append_qry $dateCondt 
			   ORDER BY created_at desc";				 

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt=='all' && $staffIDCondt != $this->session->userdata("userid")){
		
		$sql = "SELECT * FROM (
						SELECT ut.*,u.userid FROM user_tweets ut 
						LEFT JOIN users u ON u.userid = ut.user_id 
						WHERE user_id=".$this->session->userdata("userid")." $tw_append_qry $dateCondt					
					
						UNION ALL
				
						SELECT ut.*,u.userid FROM user_tweets ut
						LEFT JOIN company_ref cr ON cr.userid = ut.user_id
						LEFT JOIN users u ON u.userid = ut.user_id
						WHERE cr.companyID =".$this->session->userdata("userid")." $tw_append_qry $dateCondt
					
				 ) resultSet ORDER BY created_at desc";		
		  }	
				
		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt!='all' && $staffIDCondt != $this->session->userdata("userid")){
		
			$sql = "SELECT ut.*,u.userid FROM user_tweets ut
					LEFT JOIN company_ref cr ON cr.userid = ut.user_id
					LEFT JOIN users u ON u.userid = ut.user_id
					WHERE ut.user_id = $staffIDCondt AND cr.companyID =".$this->session->userdata("userid")."
					ORDER BY ut.created_at desc";
		}	
				
		$widgetTWlist	=	$this->model->allSelects($sql);
		$data['widgetTWlist'] =	$widgetTWlist;

		if(isset($_POST["format"]) && $_POST["format"]=='report'){
			$this->load->view('staff/load/twtfeeds-reports-grid',$data);
		} else {
			$this->load->view('staff/load/twtfeeds',$data);
		}
 }

# Instagram Data Load
 function instaDataLoad(){

		$inst_append_qry=""; $dateCondt =''; $sponsorCondt=''; $staffIDCondt = 'all';

		if(isset($_POST["search_from_date"],$_POST['search_to_date'])){
			$dateCondt =	" and created_time>= '".strtotime($_POST["search_from_date"])."' and created_time <='".strtotime($_POST['search_to_date'])."'";
		}

		if(isset($_POST["search_by_pro"])){
			$staffIDCondt = $_POST["search_by_pro"];
		}

		if(isset($_POST["search_by_manufacturer"]) && $_POST["search_by_manufacturer"]>0 && $_POST["search_by_manufacturer"]!='all'){
			//$instakeywords = getKeywords($_POST["search_by_manufacturer"],'instakeywords');
			$sql = "select userid from users where memtype='Employer' AND userid=".$_POST["search_by_manufacturer"];
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$instakeywords = getKeywords($_POST["search_by_manufacturer"],'instakeywords');
			}else {
				
				$sql = "Select ups.instakeywords from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$instakeywords = $TempInfo[0]['instakeywords'];
				}
			}

		}else{
			$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
		}
		
if($this->session->userdata("userid")!=41){

		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				if($val != '') {
					$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
				}
				
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$inst_append_qry	.=	" and ($append_qry)";
		}


		$sql=	"SELECT * FROM user_instagram_feed 
				 
				 WHERE user_id=".$this->session->userdata("userid")." $inst_append_qry $dateCondt 
				 ORDER BY created_time desc";

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt=='all' && $staffIDCondt != $this->session->userdata("userid")){
		
		$sql = "SELECT * FROM (
						SELECT DISTINCT uif.*, u.userid FROM user_instagram_feed uif
						LEFT JOIN users u ON u.userid = uif.user_id 
						WHERE uif.user_id=".$this->session->userdata("userid")." $inst_append_qry $dateCondt					
					
						UNION ALL
				
						SELECT DISTINCT uif.*, u.userid FROM user_instagram_feed uif
						LEFT JOIN company_ref cr ON cr.userid = uif.user_id
						LEFT JOIN users u ON u.userid = uif.user_id
						WHERE cr.companyID =".$this->session->userdata("userid")." $inst_append_qry $dateCondt
							
				 ) resultSet ORDER BY created_time desc";		
		  }	

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt!='all' && $staffIDCondt != $this->session->userdata("userid")){
		
			$sql = "SELECT DISTINCT uif.*, u.userid FROM user_instagram_feed uif
					LEFT JOIN company_ref cr ON cr.userid = uif.user_id
					LEFT JOIN users u ON u.userid = uif.user_id
					WHERE uif.user_id = $staffIDCondt AND cr.companyID =".$this->session->userdata("userid")."
					$inst_append_qry $dateCondt ORDER BY uif.created_time desc";
		  }	
}else{

		$sql=	"SELECT * FROM user_instagram_feed 
				 LEFT JOIN users ON users.userid = user_instagram_feed.user_id 
				 WHERE user_id=".$this->session->userdata("userid")." $inst_append_qry $dateCondt 
				 ORDER BY created_time desc";

}				 
		
		$data["widgetINStlist"]	=	$this->model->allSelects($sql);

		// echo '<pre>';
		// print_r($sql);
		// die('heerere');
		
	  
		
		if(isset($_POST["format"]) && $_POST["format"]=='report'){
			$this->load->view('staff/load/instafeeds-reports-grid',$data);
		} else {
			$this->load->view('staff/load/instafeeds',$data);
		}
}

# YouTube DataLoad

 function youTubeDataLoad(){

	   $user_id = $this->input->post('user_id');

	   $fishingType=""; $yt_append_qry=""; $dateCondt ='';	$staffIDCondt = 'all';	

 		//$startWeekDay = date('Y-m-d',strtotime("-30 days"));
		$startWeekDay = '2014-01-01';
		$endDate 	  = date('Y-m-d');

		$search_from_date =	date("Y-m-d",strtotime($startWeekDay));
		$search_to_date	  =	date("Y-m-d",strtotime($endDate));
		
		$dateCondt = " and ytf.created_date>= '".$search_from_date."' and ytf.created_date <='".$search_to_date."'";

		if(isset($_POST["search_from_date"],$_POST['search_to_date'])){
			$dateCondt = " and ytf.created_date>= '".$_POST["search_from_date"]."' and ytf.created_date <='".$_POST['search_to_date']."'";
		}

		if(isset($_POST["search_by_pro"])){
			$staffIDCondt = $_POST["search_by_pro"];
		}

		if(isset($_POST["search_by_manufacturer"]) && $_POST["search_by_manufacturer"]>0 && $_POST["search_by_manufacturer"]!='all'){
			//$youtubekeywords = getKeywords($_POST["search_by_manufacturer"],'youtubekeywords');
			$sql = "select userid from users where memtype='Employer' AND userid=".$_POST["search_by_manufacturer"];
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$youtubekeywords = getKeywords($_POST["search_by_manufacturer"],'youtubekeywords');
			} else {
				
				$sql = "SELECT ups.youtubekeywords FROM user_prostaff_sponsors ups
						WHERE ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$youtubekeywords = $TempInfo[0]['youtubekeywords'];
				}
			}
		}else{
			$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
		}

	    if ($this->session->userdata("filtersettings") != "" && $this->session->userdata("filtersettings") != "both" ) {
				$fishingType = " and u.fishingtype='".$this->session->userdata("filtersettings")."' ";
		}
 
      if ($youtubekeywords) {
            $keywordArr = explode(',', $youtubekeywords);
            $keyword_query = array();
           foreach ($keywordArr as $key => $val) {
            $keyword_query[] = " (ytf.text like '".trim($val)."%' or ytf.text like '%".trim($val)."%' or ytf.title like '".trim($val)."%' or ytf.title like '%".trim($val)."%') ";
           }
            $append_qry = implode(' or ', $keyword_query);
            $yt_append_qry = " AND ($append_qry)";
      }

       $sql = "SELECT DISTINCT ytf.*, u.userid FROM user_youtubefeeds ytf
			   LEFT JOIN users u ON u.userid = ytf.user_id
			   WHERE ytf.user_id=".$this->session->userdata("userid")." $yt_append_qry $dateCondt
			   ORDER BY ytf.created_date desc";

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt=='all' && $staffIDCondt != $this->session->userdata("userid")){
		
		$sql = "SELECT * FROM (
				SELECT DISTINCT ytf.*, u.userid FROM user_youtubefeeds ytf
				LEFT JOIN users u ON u.userid = ytf.user_id
				WHERE ytf.user_id=".$this->session->userdata("userid")." $yt_append_qry $dateCondt					
					
				 UNION ALL
					
				SELECT ytf.* ,u.userid FROM user_youtubefeeds ytf
				LEFT JOIN company_ref cr ON cr.userid = ytf.user_id
				LEFT JOIN users u ON u.userid = ytf.user_id
				WHERE cr.companyID =".$this->session->userdata("userid")." $yt_append_qry $dateCondt
							
				) resultSet ORDER BY created_date desc";		
		  }	

		if($this->session->userdata("acess_type")=='Hybrid' && $staffIDCondt!='all' && $staffIDCondt != $this->session->userdata("userid")){
		
			$sql = "SELECT ytf.* ,u.userid FROM user_youtubefeeds ytf
					LEFT JOIN company_ref cr ON cr.userid = ytf.user_id
					LEFT JOIN users u ON u.userid = ytf.user_id
					WHERE ytf.user_id = $staffIDCondt AND cr.companyID =".$this->session->userdata("userid")."
					$yt_append_qry $dateCondt ORDER BY ytf.created_date desc";
		  }	

		$data["widgetYTlist"] = $this->model->allSelects($sql);

		if(isset($_POST["format"]) && $_POST["format"]=='report'){
			$this->load->view('staff/load/youtubefeeds-reports-grid',$data);
		} else {
			$this->load->view('staff/load/youtubefeeds',$data);
		}
}

# For Facebook
 public function facebook_redirect(){

		$this->config->load("facebook",TRUE);
		$config = $this->config->item('facebook');
		
		$this->load->library('Facebook', $config);
		
		$user = $this->facebook->getUser();
		
		$acessToken = $this->facebook->getAccessToken();
		// long lived token
		//$acessToken = $this->facebook->getAccessTokenFromCode($acessToken, 'https://anglertrack.net/index.php/staff/facebook_redirect/');
	
		if($user==0){
			$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout facebook and Try again.");
			redirect('staff/newsocialpage');
		}
		if($acessToken==""){
			$this->session->set_flashdata('error', "Authorization Failed. Please Logout facebook and Try again.");
			redirect('staff/newsocialpage');				
		}	
		
		$parameter = '?fields=id,first_name,last_name,email,gender';
		$user_info = $this->facebook->api('/me'.$parameter,'GET',array('access_token' =>$acessToken));

		$whereArr = array();
			$whereArr['userid'] = $this->session->userdata("userid");

	// 	echo "<pre>";
	// 	 	print_r($user_info); 
	//  echo "</pre>"; die('herer');
		
		$user_accountInfo = $this->facebook->api('/me/accounts');
		
		#CHECK DB
		$existRow = $this->db->get_where('user_media',array('social_type'=>'facebook','userid'=>$this->session->userdata("userid")))->row_array();
	
		
		if(count($existRow) > 0 ) {

				$updateArr = array(
						'userid'=>$this->session->userdata("userid"),
						'username'=>$user_info['email'],
						'fullname'=>$user_info['first_name'].' '.$user_info['last_name'],
						'email'=>$user_info['email'],
						'social_id'=>$user_info['id'],
						'image' => 'https://graph.facebook.com/'.$user_info['id'].'/picture',
						'created_date'=>date('Y-m-d H:i:s'),
						'download_dated'=>date('Y-m-d H:i:s'),
						'social_type'=>'facebook',
						'access_token'=>$acessToken,
						'status'=>'Active',
						'isScheduled'=> 0
				);
	
				$this->db->where('social_id',$user_info['id']);
				$this->db->update('user_media',$updateArr);


			$fields = "?fields=reactions.summary(true),comments.summary(true),shares,picture,created_time,story,message";
			$userFeed = $this->facebook->api('/me/posts'.$fields, 'GET',array('access_token' =>$acessToken));

			if(count($userFeed['data']) > 0){
				
				foreach($userFeed['data'] as $feed) {
			
				#Feeds Story/Description/Message
				$isset="";
				if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
				{$isset = $feed['story'];}
				if(isset($feed['message']) && array_key_exists('message', $feed))
				{$isset .= $feed['message'];}
				// if(array_key_exists('description', $feed) && isset($feed['description']))
				// {$isset .= $feed['description'];}
				
				#Declare Variables
				 $likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
				 $refLink =""; $feedType=""; $photo_url =""; $objectid=0;

					#Calculation of Interactions				
					if(isset($feed['likes']) && isset($feed['likes']['summary'])){
						//$likes_count = count($feed['likes']['data']);
						$tlikes = $feed['likes']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['reactions']) && isset($feed['reactions']['summary'])){
						$tlikes = $feed['reactions']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['comments']) && isset($feed['comments']['summary'])){
						//$comment_count = count($feed['comments']['data']);
						$tcomment = $feed['comments']['summary'];
						if(isset($tcomment['total_count'])){
							$comment_count = $tcomment['total_count'];
						}
					}

					if(isset($feed['shares']) && isset($feed['shares']['count'])){
						$share_count = $feed['shares']['count'];
					}
					
					#Link of activity
					// if(isset($feed['link'])){
					// 	$refLink = $feed['link'];
					// } else {
					// 	$exp = explode('_',$feed['id']);
					// 	$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
					// }
					// if(isset($feed['attachments']['data'][0]['unshimmed_url'])) {
					// 	$refLink .= $feed['attachments']['data'][0]['unshimmed_url'];
					// }
					if(isset($feed['attachments']['data'][0]['description'])) {
						$isset .= $feed['attachments']['data'][0]['description'];
					}

					if(isset($feed['attachments']['data'][0]['url'])) {
						$refLink = $feed['attachments']['data'][0]['url'];
					} else {
						$exp = explode('_',$feed['id']);
						$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
					}
					
					# Type of Feed
					if(isset($feed['attachments']['data'][0]['type'])) {
						$feedType = $feed['attachments']['data'][0]['type'];
					}
					if(isset($feed['attachments']['data'][0]['type']) && $feed['attachments']['data'][0]['type']=='photo') {
						$photo_url = $feed['attachments']['data'][0]['type'];
					}
					if(isset($feed['attachments']['data'][0]['type']) && $feed['attachments']['data'][0]['type']=='video_inline') {
						$photo_url = $feed['attachments']['data'][0]['url'];
					}


					// if(isset($feed['type'])){
					// 	$feedType = $feed['type'];
					// }
					
					// if(isset($feed['type']) && $feed['type']=='photo'){
					// 	$photo_url = $feed['picture'];
					//  }

					// if(isset($feed['type']) && $feed['type']=='video'){
					// 	$photo_url = $feed['picture'];
					//  }
				
				$existFeed = $this->db->get_where('user_feed',array('id'=>$feed['id']))->row_array();
				
					if(count($existFeed) > 0 ){
					
						$updateArr	=	array();
						$updateArr['story']	=	$isset;
						$updateArr['link'] = $refLink;							
						$updateArr['likes_count'] =	$likes_count;
						$updateArr['comment_count']	= $comment_count;	
						$updateArr['share_count']	= $share_count;	
						$updateArr['view_count']	= $view_count;	
						$updateArr['photo_url']	= $photo_url;
						$updateArr['feed_type'] = $feedType;																
						$this->db->where('id',$feed['id']);
						$this->db->update('user_feed',$updateArr);
												
					} else {
					
						$insertFeed = array(
							'user_id' => $this->session->userdata("userid"),
							'id' => $feed['id'],
							'story' => $isset,
							'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
							'link' => $refLink, 
							'likes_count' => $likes_count,
							'comment_count' => $comment_count,
							'share_count' => $share_count,	
							'photo_url' => $photo_url,
							'feed_type' =>$feedType,							
						);
						$this->db->insert('user_feed',$insertFeed);
					}
				}
		} 


			#CONDITION ARRAY
			

			$flagArr	=	array();
			$flagArr['data_download_flag'] = 0;
			$flagArr['data_download_date'] = date('Y-m-d H:i:s');
			$flagArr['last_modified_data_download_date'] = date('Y-m-d H:i:s');		
			$this->db->where($whereArr);
			$this->db->update('company_ref',$flagArr);

			#Already Exists
			$this->session->set_flashdata('success', 'Facebook Account Authorized.');
			redirect('staff/newsocialpage');
		} 
		else {
			
			if($user_info['id']==""){
				$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout facebook and Try again.");
			  	redirect('staff/newsocialpage');
			}	
				
			if($acessToken==""){
				$this->session->set_flashdata('error', "Authorization Failed. Please Logout facebook and Try again.");
			  	redirect('staff/newsocialpage');				
			}	
				
				$insertArray = array(
					'userid'=>$this->session->userdata("userid"),
					'username'=>$user_info['email'],
					'fullname'=>$user_info['first_name'].' '.$user_info['last_name'],
					'email'=>$user_info['email'],
					'gender'=>$user_info['gender'],
					'social_id'=>$user_info['id'],
					'image' => 'https://graph.facebook.com/'.$user_info['id'].'/picture',
					'created_date'=>date('Y-m-d H:i:s'),
					'social_type'=>'facebook',
					'access_token'=>$acessToken,
					'status'=>'Active'
				);
				$this->db->insert('user_media',$insertArray);
				
				/*echo "<pre>";
					print_r($insertFeed); 
				echo "</pre>";*/

			if($user_accountInfo['data']){
				$sqll = "DELETE FROM user_page where page_user_id=".$this->session->userdata("userid");
				// "DELETE FROM user_media where social_type='facebook' AND userid=".$userid"
				$delete_before_pages = $this->model->allQueries($sqll);
				foreach($user_accountInfo['data'] as $key=>$val){
				
					$insertArray = array(
							'page_user_id'=>$this->session->userdata("userid"),							
							'page_social_id' => $val['id'],
							'page_name' 	 => $val['name'],
							'page_access_token' => $val['access_token']							
						);
						
						
					if($this->session->userdata("userid")==41){
						$insertArray['page_status'] = 'Enable';
					}
					
					$this->db->insert('user_page',$insertArray);		
			}
				
				/*echo "<pre>";
					print_r($insertFeed); 
				echo "</pre>";*/
			// getting instgram account details if busines or not
			$sql="Select * from user_page where page_user_id=".$this->session->userdata("userid");
			$pageData =	$this->model->allSelects($sql);
			$pages=$this->model->allSelects($sql);
				// storing the page feeds as well 
				if(count($pageData)>0){
				
					foreach($pageData as $key=>$val){
	
						$fields = "?fields=likes.summary(true),comments.summary(true),attachments,shares,picture,created_time,story,message";
						$parameters = array('access_token'=>$acessToken);
						$userFeed  = $this->facebook->api('/'.$val['page_social_id'].'/feed'.$fields,'GET',$parameters);
						// echo '<pre>';
						// print_r($userFeed);
						// die('here');
						
	
						if($userFeed['data']) {
						
							foreach($userFeed['data'] as $key1=>$feed){
							
									#Searching story from Feeds
									$isset="";
									if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
									{$isset = $feed['story'];}
									if(isset($feed['message']) && array_key_exists('message', $feed))
									{$isset .= $feed['message'];}
									// if(array_key_exists('description', $feed) && isset($feed['description']))
									// {$isset .= $feed['description'];}
																
									#Declare Variables
									$likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
									$refLink =""; $feedType=""; $photo_url ="";$objectid=0;
									
									#Calculation of Interactions				
									if(isset($feed['likes']) && isset($feed['likes']['summary'])){
										//$likes_count = count($feed['likes']['data']);
										$tlikes = $feed['likes']['summary'];
										if(isset($tlikes['total_count'])){
											$likes_count = $tlikes['total_count'];
										}
									}
				
									if(isset($feed['comments']) && isset($feed['comments']['summary'])){
										//$comment_count = count($feed['comments']['data']);
										$tcomment = $feed['comments']['summary'];
										if(isset($tcomment['total_count'])){
											$comment_count = $tcomment['total_count'];
										}
									}
				
									if(isset($feed['shares']) && isset($feed['shares']['count'])){
										$share_count = $feed['shares']['count'];
									}
									
									#Link of activity
									// if(isset($feed['link'])){
									// 	$refLink = $feed['link'];
									// }
									if(isset($feed['attachments']['data'][0]['description'])) {
										$isset .= $feed['attachments']['data'][0]['description'];
									}
	
									if(isset($feed['attachments']['data'][0]['url'])) {
										$refLink = $feed['attachments']['data'][0]['url'];
									} else {
										$exp = explode('_',$feed['id']);
										$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
									}
		
		
									// if(isset($feed['link'])){
									// 	$refLink = $feed['link'];
									// } else {
									// 	$exp = explode('_',$feed['id']);
									// 	$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
									// }
										
									#Type of Feed
		
									if(isset($feed['attachments']['data'][0]['type'])) {
										$feedType = $feed['attachments']['data'][0]['type'];
									}
									if(isset($feed['attachments']['data'][0]['type']) && $feed['attachments']['data'][0]['type']=='photo') {
										$photo_url = $feed['attachments']['data'][0]['type'];
									}
									if(isset($feed['attachments']['data'][0]['type']) && $feed['attachments']['data'][0]['type']=='video_inline') {
										$photo_url = $feed['attachments']['data'][0]['url'];
									}
									
									// if(isset($feed['attachments']['data'][0]['object_id'])) {
									// 	$object_id = $feed['attachments']['data'][0]['object_id'];
									// }
									#Type of Feed
									// if(isset($feed['type'])){
									// 	$feedType = $feed['type'];
									// }
									
									// if(isset($feed['type']) && $feed['type']=='photo'){
									// 	$photo_url = $feed['picture'];
									// }
	
								$existFeed = $this->db->get_where('user_feed',array('id'=>$feed['id']))->row_array();
								
								if(count($existFeed) > 0 ){
								
										$updateArr	=	array();
										$updateArr['story']	= $isset;
										$updateArr['user_id'] = $this->session->userdata("userid");
										$updateArr['link']	= $refLink;
										$updateArr['feed_type']	= $feedType;
										$updateArr['photo_url']	= $photo_url;
										$updateArr['likes_count'] =	$likes_count;							
										$updateArr['comment_count']	= $comment_count;
										$updateArr['share_count']	= $share_count;	
										$updateArr['view_count']	= $view_count;							
										$updateArr['post_type']	= 'page_feed';
										$this->db->where('id',$feed['id']);
										$this->db->update('user_feed',$updateArr);
									
								} else {						
										$insertFeed = array(
											'user_id' => $this->session->userdata("userid"),
											'id' => $feed['id'],
											'story' => $isset,
											'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
											'link' => $refLink,
											'likes_count' => $likes_count,
											'comment_count' => $comment_count,
											'share_count' => $share_count,	
											'view_count' => $view_count,							
											'feed_type'	=> $feedType,
											'photo_url'	=> $photo_url,
											'post_type' => 'page_feed'					
										);	
									$this->db->insert('user_feed',$insertFeed);
								}
							}
	
							$flagArr = array();
							$flagArr['data_download_flag'] = 0;
							$flagArr['data_download_date'] = date('Y-m-d H:i:s');	
							$flagArr['last_modified_data_download_date'] = date('Y-m-d H:i:s');
							// echo '<pre>';
							// print_r($whereArr);
							// die('heerer');
							$this->db->where($whereArr);
							$this->db->update('company_ref',$flagArr);
	
							$retDataFlag=1;
	
							$returnArr['error']	= false;
							$returnArr['msg'] .= count($userFeed['data']).' of '.count($pageData).' Facebook Pages Feeds Updated Successfully.<br />Dated:'.$fromdate.' To:'.$tilldate.'<br />';
	
						} else { 
							$returnArr['error']	= true;	$returnArr['msg']	.=  count($userFeed['data'])." Pages Post activity.<br />Dated:".$fromdate.' To:'.$tilldate.'<br />'; 
							$flagArr	=	array();					
							$flagArr['last_modified_data_download_date'] = date('Y-m-d H:i:s');
							$this->db->where($whereArr);
							$this->db->update('company_ref',$flagArr);
						}												
					}
				}
					else{ $returnArr['error']	= true;	$returnArr['msg']	.=  count($pageData)." Pages..<br />Dated:".$fromdate.' To:'.$tilldate.'<br />'; 
					}




			
			$pages_array = [];
			$fb = new \Facebook\Facebook([
				'app_id' => '410761562312101',
				'app_secret' => 'af3b1e569a37c23308940a3733c094a5',
				'default_graph_version' => 'v2.10'
    		 ]);
			foreach($pages as $key => $page) {
				try {
					$response = $fb->get('/'.$page['page_social_id'].'/picture?redirect=0',$acessToken);
					// $response_page_posts = $fb->get('/'.$page['page_social_id'].'/feed',$acessToken);
				  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
					// When Graph returns an error
					echo 'Graph returned an error: ' . $e->getMessage();
					exit;
				  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
					// When validation fails or other local issues
					echo 'Facebook SDK returned an error: ' . $e->getMessage();
					exit;
				  }
		   
				  
				  $pics = $response->getGraphUser();
				  $pages_array[$key]['id'] = $page['page_social_id'];
				  $pages_array[$key]['pic'] = $pics['url'];
				  $pages_array[$key]['name'] = $page['page_name'];
		   
			  }
			  $data['pages'] = $pages_array;

				try {
					$getIGinfo = $fb->get('/'.$user_info['id'].'/accounts', $acessToken);
				} catch(\Facebook\Exceptions\FacebookResponseException $e) {
					// When Graph returns an error
					echo 'Graph returned an error: ' . $e->getMessage();
					exit;
				} catch(\Facebook\Exceptions\FacebookSDKException $e) {
					// When validation fails or other local issues
					echo 'Facebook SDK returned an error: ' . $e->getMessage();
					exit;
				}

				$graphedge = $getIGinfo->getGraphEdge();
				$business = 0;
				$connectedIG = '';

				foreach ($graphedge as $graphNode) {
						$singlenode = $graphNode->asArray();	
						try {
							$newcall = $fb->get('/'.$singlenode['id'].'?fields=connected_instagram_account', $acessToken);
						} catch(\Facebook\Exceptions\FacebookResponseException $e) {
							// When Graph returns an error
							echo 'Graph returned an error: ' . $e->getMessage();
							exit;
						} catch(\Facebook\Exceptions\FacebookSDKException $e) {
							// When validation fails or other local issues
							echo 'Facebook SDK returned an error: ' . $e->getMessage();
							exit;
						}
						$results = $newcall->getDecodedBody();
						
						if(isset($results['connected_instagram_account']) and count($results['connected_instagram_account']) != 0) {
							$business++;
							$connectedIG = $results['connected_instagram_account']['id'];
						} 
				}

				  if(isset($connectedIG) and $connectedIG != '') {

					try {
						$IG_business = $fb->get('/'.$connectedIG.'?fields=id,username', $acessToken);
					  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
						// When Graph returns an error
						echo 'Graph returned an error: ' . $e->getMessage();
						exit;
					  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
						// When validation fails or other local issues
						echo 'Facebook SDK returned an error: ' . $e->getMessage();
						exit;
					  }
			   
					  
					  $IGdetails = $IG_business->getDecodedBody();
					  
				  }

				  if($business != 0) {
					  // the facebook has a business accut connected: we will put the connection string in db:

					  $sqll = "DELETE from user_media where userid=".$this->session->userdata("userid")." AND social_type='instagram';";
						$delete_prev = $this->model->allQueries($sqll);
						$insertArray = array(
									'userid'=>$this->session->userdata("userid"),
									'username'=> $IGdetails['username'],
									'fullname'=> $IGdetails['full_name'],
									'social_id'=> $IGdetails['id'],
									'image' => 'https://graph.facebook.com/'.$user_info['id'].'/picture',
									'created_date' =>date('Y-m-d H:i:s'),
									'social_type' =>'instagram',
									'access_token' => $acessToken,
									'status'=>'Active'
								);
						$this->db->insert('user_media', $insertArray);

						try {
							$response = $fb->get('/'.$IGdetails['id'].'/media',$acessToken);
						  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
							// When Graph returns an error
							echo 'Graph returned an error: ' . $e->getMessage();
							exit;
						  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
							// When validation fails or other local issues
							echo 'Facebook SDK returned an error: ' . $e->getMessage();
							exit;
						  }

						  $IG_media_set = $response->getDecodedBody();
						  foreach($IG_media_set['data'] as $single) {
							$feedArray = [];
							// getting feeds like and comments counts:
							$urll = 'https://graph.facebook.com/v10.0/'.$single['id'].'/?fields=caption,media_product_type,permalink,thumbnail_url,id,media_type,media_url,owner,timestamp,comments_count,like_count&access_token='.$acessToken.'';
							$crl = curl_init();
							
							curl_setopt($crl, CURLOPT_URL, $urll);
							curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
							curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
							$response = curl_exec($crl);
							
							if(!response){
								die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
							}
							
							curl_close($crl);
							// data returned as json formatted object so need conversion
							$format = json_decode($response);
							$iglink = $format->permalink;
							$existInstFeed = $this->db->get_where('user_instagram_feed',array('link'=>$iglink))->row_array();
							if(count($existInstFeed) > 0 ){
							
							$updateArr = array();			
										
							$updateArr['id']	= $format->id;
							$updateArr['user_id']	= $this->session->userdata("userid");
							$updateArr['text']	= ($format->caption) ? $format->caption : '';					
							$updateArr['link']  = $format->permalink;					
							$updateArr['likes']	  = $format->like_count?$format->like_count:0;	
							$updateArr['likes_count']	  = $format->like_count?$format->like_count:0;		
							$updateArr['comments_count']	  = $format->comments_count;					
							//$updateArr['type'] 			  =	$instagram->type;	
							$updateArr['thumbnail_image'] =	$format->media_url;					
							$updateArr['standard_image']  =	$format->media_url;
							$updateArr['created_time'] = strtotime($format->timestamp);
							$this->db->where('link',$iglink);
							$this->db->update('user_instagram_feed',$updateArr);
							
							} else {
									$feedArray = array(
									'id' => $format->id,
									'user_id' => $this->session->userdata("userid"),
									'text' => ($format->caption) ? $format->caption : '',
									'link' => $format->permalink,
									'likes' => $format->like_count?$format->like_count:0,		
									'created_time' => strtotime($format->timestamp),
									'comments_count' => $format->comments_count,
									'likes_count' => $format->like_count?$format->like_count:0,
									'thumbnail_image' => $format->media_url,
									'standard_image' => $format->media_url,
							);
							$this->db->insert('user_instagram_feed',$feedArray);

							}
							
						// try {
						// 	$this->db->trans_start(FALSE);
							
						// 	//$this->db->trans_complete();
					
						// 	// documentation at
						// 	// https://www.codeigniter.com/userguide3/database/queries.html#handling-errors
						// 	// says; "the error() method will return an array containing its code and message"
						// 	$db_error = $this->db->error();
						// 	if (!empty($db_error)) {
						// 		throw new Exception('Database error! Error Code [' . $db_error['code'] . '] Error: ' . $db_error['message']);
						// 		return false; // unreachable retrun statement !!!
						// 	}
						// 	return TRUE;
						// } catch (Exception $e) {
						// 	// this will not catch DB related errors. But it will include them, because this is more general. 
						// 	log_message('error: ',$e->getMessage());
						// 	return;
						// }


						
						
						
	  
			
						  }

				  } else {
					  // not a business account of insta so do nothing here.
				  }



	}
	
	$fields = "?fields=reactions.summary(true),comments.summary(true),shares,type,picture,created_time,story,description,message";
	$user_feed	= $this->facebook->api('/me/posts'.$fields, 'GET',array('access_token' =>$acessToken));
									
		#Inserting Feeds
			
		 if(count($user_feed['data'])>0){	
		   
			foreach($user_feed['data'] as $feed){				

					#Story Description Message from Feeds
					$isset="";
					if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
					{$isset = $feed['story'];}
					if(isset($feed['message']) && array_key_exists('message', $feed))
					{$isset .= $feed['message'];}
					if(array_key_exists('description', $feed) && isset($feed['description']))
					{$isset .= $feed['description'];}

					#Declare Variables
					$likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
					$refLink =""; $feedType="";$photo_url ="";$objectid=0;
					
					#Calculation of Interactions				
					if(isset($feed['likes']) && isset($feed['likes']['summary'])){
						//$likes_count = count($feed['likes']['data']);
						$tlikes = $feed['likes']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['reactions']) && isset($feed['reactions']['summary'])){
						//$likes_count = count($feed['likes']['data']);
						$tlikes = $feed['reactions']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['comments']) && isset($feed['comments']['summary'])){
						//$comment_count = count($feed['comments']['data']);
						$tcomment = $feed['comments']['summary'];
						if(isset($tcomment['total_count'])){
							$comment_count = $tcomment['total_count'];
						}
					}

					if(isset($feed['shares']) && isset($feed['shares']['count'])){
						$share_count = $feed['shares']['count'];
					}
					
					#Link of activity
					if(isset($feed['link'])){
						$refLink = $feed['link'];
					} else {
					$exp = explode('_',$feed['id']);
					$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
					}
					
					# Type of Feed
					if(isset($feed['type'])){
						$feedType = $feed['type'];
					}

					if(isset($feed['type']) && $feed['type']=='photo'){
						$photo_url = $feed['picture'];
					}
					
					$insertFeed = array(
							'user_id' =>$this->session->userdata("userid"),
							'id' => $feed['id'],
							'story' => $isset,
							'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
							'link' => $refLink,
							'likes_count' => $likes_count,
							'feed_type'	  => $feed_type,
							'comment_count' => $comment_count,
							'share_count' => $share_count,
							'view_count' => $view_count,		
							'photo_url' => $photo_url,
							'feed_type' => $feedType				
					);				
					$this->db->insert('user_feed',$insertFeed);
				
				/*echo "<pre>";
					print_r($insertFeed); 
				echo "</pre>";*/
			}
		 }			

	if($this->session->userdata("userid")==41){

		$sql = "SELECT * FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
		$pageData = $this->model->allSelects($sql);
		
	if(count($pageData)>0){
		
		foreach($pageData as $key=>$val){

			$fields = "?fields=likes.summary(true),comments.summary(true),shares,picture,attachments,created_time,story,message";
			$params =  array('access_token'=>$val['page_access_token']);
			$userFeed  = $this->facebook->api('/'.$val['page_social_id'].'/posts'.$fields,'GET',$params);

				if($userFeed['data']){
				
					foreach($userFeed['data'] as $key1=>$feed){
					
							#Searching story from Feeds
							$isset="";
							if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
							{$isset = $feed['story'];}
							if(isset($feed['message']) && array_key_exists('message', $feed))
							{$isset .= $feed['message'];}
							if(array_key_exists('description', $feed) && isset($feed['description']))
							{$isset .= $feed['description'];}
														
							#Declare Variables
							$likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
							$refLink =""; $feedType=""; $photo_url ="";$objectid=0;
							
							#Calculation of Interactions				
							if(isset($feed['likes']) && isset($feed['likes']['summary'])){
								//$likes_count = count($feed['likes']['data']);
								$tlikes = $feed['likes']['summary'];
								if(isset($tlikes['total_count'])){
									$likes_count = $tlikes['total_count'];
								}
							}
		
							if(isset($feed['comments']) && isset($feed['comments']['summary'])){
								//$comment_count = count($feed['comments']['data']);
								$tcomment = $feed['comments']['summary'];
								if(isset($tcomment['total_count'])){
									$comment_count = $tcomment['total_count'];
								}
							}
		
							if(isset($feed['shares']) && isset($feed['shares']['count'])){
								$share_count = $feed['shares']['count'];
							}
							
							#Link of activity
							// if(isset($feed['link'])){
							// 	$refLink = $feed['link'];
							// }
							
							#Type of Feed
							if(isset($feed['type'])){
								$feedType = $feed['type'];
							}
							
							if(isset($feed['type']) && $feed['type']=='photo'){
								$photo_url = $feed['picture'];
							}

						$existFeed = $this->db->get_where('user_feed',array('id'=>$feed['id']))->row_array();
						
						if(count($existFeed) > 0 ){
						
								$updateArr	=	array();
								$updateArr['story']	= $isset;
								$updateArr['user_id'] = $this->session->userdata("userid");
								$updateArr['link']	= $refLink;
								$updateArr['feed_type']	= $feedType;
								$updateArr['photo_url']	= $photo_url;
								$updateArr['likes_count'] =	$likes_count;							
								$updateArr['comment_count']	= $comment_count;
								$updateArr['share_count']	= $share_count;	
								$updateArr['view_count']	= $view_count;							
								$updateArr['post_type']	= 'page_feed';
								
								$this->db->where('id',$feed['id']);
								$this->db->update('user_feed',$updateArr);
							
						} else {
											
								$insertFeed = array(
									'user_id' => $this->session->userdata("userid"),
									'id' => $feed['id'],
									'story' => $isset,
									'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
									'link' => $refLink,
									'likes_count' => $likes_count,
									'comment_count' => $comment_count,
									'share_count' => $share_count,	
									'view_count' => $view_count,							
									'feed_type'	=> $feedType,
									'photo_url'	=> $photo_url,
									'post_type' => 'page_feed'					
								);
							$this->db->insert('user_feed',$insertFeed);
							
						}
					}
				}												
			}
		}
} //PAGE DATA ENDS

			#CONDITION ARRAY
			$whereArr = array();
			$whereArr['userid'] = $this->session->userdata("userid");

			$flagArr	=	array();
			$flagArr['data_download_flag'] = 0;
			$flagArr['data_download_date'] = date('Y-m-d H:i:s');
			$flagArr['last_modified_data_download_date'] = date('Y-m-d H:i:s');		
			$this->db->where($whereArr);
			$this->db->update('company_ref',$flagArr);
	
			$this->session->set_flashdata('success', 'Facebook Account is Authorized Successfully.');
			redirect('staff/newsocialpage');
		}
 }

#Facebook Refresh
 public function refreshFacebook(){
 
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);

		$data['navigation']=$this->load->view('staff/staff-navigation','',true);		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-fb-feeds-refresh',$data,true);
		$this->load->view('layout-inner',$data);
 }

#To update selected user's facebook feed
 public function updateFeed($reDirect=0){	 	
 
  try { 
  	
	if(isset($_POST) && $this->input->post('refreshfeeds') == 'download'){
	
	# Check Token is Valid or Not
	$userInfoRow = $this->db->get_where('user_media',array('social_type'=>'facebook','userid'=>$this->session->userdata("userid")))->row_array(); //getting fb id									

	if(count($userInfoRow)>0){
			
			$dataFields = "";

			$appToken ="410761562312101|QGiOIvUAg4pwWCJ-MCkN_vzc_Zg";
			$curl_facebook2 = curl_init();
			$url = "https://graph.facebook.com/debug_token?input_token=".$userInfoRow['access_token']."&access_token=".$appToken; // set url and parameters
			curl_setopt($curl_facebook2, CURLOPT_URL, $url);
			curl_setopt($curl_facebook2, CURLOPT_RETURNTRANSFER, true);
			$curUserinfo = curl_exec($curl_facebook2);
			curl_close($curl_facebook2);			
			$dataJson = json_decode($curUserinfo,true); 

			$tilldate = date('Y-m-d');
			$fromdate = date('Y-m-d', strtotime('-7 day', strtotime($tilldate)));
		
			/*$tilldate = '2018-06-01';
			$fromdate = '2018-05-20';*/
			
			foreach($dataJson as $data=>$valjson){
				if(isset($valjson["error"])){
					$dataFields = $valjson["error"]["message"];
				}
			}
	
		if(isset($dataFields) && $dataFields!=""){	 
		  	$this->session->set_flashdata('error', "Not Authorized. Please contact AnglerTrack For Support. ".$dataFields);
		  	redirect('staff');
		 }
		else {

		$userRow = $this->db->get_where('user_media',array('social_type'=>'facebook','userid'=>$this->session->userdata("userid")))->row_array(); //getting fb id									
		
		$fields = "?fields=fan_count,posts{shares,likes.summary(true),comments.summary(true)}";
		$user_accountInfo = $this->facebook->api('/me/accounts'.$fields, 'GET',array( 'access_token' => $userRow['access_token'] ));		

  		$fields = "?fields=reactions.summary(true),comments.summary(true),shares,type,picture,created_time,story,description,message";
  		$parameters = array('since'=>$fromdate,'until'=>$tilldate ,'access_token' => $userRow['access_token']);
  		$userFeed = $this->facebook->api('/'.$userRow['social_id'].'/posts'.$fields,'GET',$parameters);

		$isset="";

		if($user_accountInfo['data']){	
				
			foreach($user_accountInfo['data'] as $key=>$val){
			
				$sql=	"SELECT * FROM user_page WHERE page_user_id=".$this->session->userdata("userid")." and page_social_id=".$val['id'];
				$pageInfo	=	$this->model->allSelects($sql);
				if(count($pageInfo) == 0){
					$insertArray = array(
							'page_user_id'=>$this->session->userdata("userid"),							
							'page_social_id' => $val['id'],
							'page_name' 	 => $val['name'],
							'page_access_token' => $val['access_token']							
						);
					$this->db->insert('user_page',$insertArray);				
				}
			}
		}		

		/*echo "<pre>";
			print_r($userFeed['data']);
		echo "</pre>"; die;*/

		if(count($userFeed['data']) > 0){
		
			foreach($userFeed['data'] as $feed){
			
				#Feeds Story/Description/Message
				$isset="";
				if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
				{$isset = $feed['story'];}
				if(isset($feed['message']) && array_key_exists('message', $feed))
				{$isset .= $feed['message'];}
				if(array_key_exists('description', $feed) && isset($feed['description']))
				{$isset .= $feed['description'];}
				
				#Declare Variables
				 $likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
				 $refLink =""; $feedType=""; $photo_url =""; $objectid=0;

					#Calculation of Interactions				
					if(isset($feed['likes']) && isset($feed['likes']['summary'])){
						$tlikes = $feed['likes']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['reactions']) && isset($feed['reactions']['summary'])){
						$tlikes = $feed['reactions']['summary'];
						if(isset($tlikes['total_count'])){
							$likes_count = $tlikes['total_count'];
						}
					}

					if(isset($feed['comments']) && isset($feed['comments']['summary'])){
						//$comment_count = count($feed['comments']['data']);
						$tcomment = $feed['comments']['summary'];
						if(isset($tcomment['total_count'])){
							$comment_count = $tcomment['total_count'];
						}
					}

					if(isset($feed['shares']) && isset($feed['shares']['count'])){
						$share_count = $feed['shares']['count'];
					}
					
					#Link of activity
					if(isset($feed['link'])){
						$refLink = $feed['link'];
					} else {
						$exp = explode('_',$feed['id']);
						$refLink = 'https://www.facebook.com/'.$exp[0].'/posts/'.$exp[1];
					}
					
					# Type of Feed
					if(isset($feed['type'])){
						$feedType = $feed['type'];
					}
					
					if(isset($feed['type']) && $feed['type']=='photo'){
						$photo_url = $feed['picture'];
					 }

					if(isset($feed['type']) && $feed['type']=='video'){
						$photo_url = $feed['picture'];
					 }
				
				$existFeed = $this->db->get_where('user_feed',array('id'=>$feed['id']))->row_array();
				
					if(count($existFeed) > 0 ){
					
						$updateArr	=	array();
						$updateArr['story']	= $isset;
						$updateArr['link'] = $refLink;							
						$updateArr['likes_count'] =	$likes_count;
						$updateArr['comment_count']	= $comment_count;	
						$updateArr['share_count']	= $share_count;	
						$updateArr['view_count']	= $view_count;	
						$updateArr['photo_url']	= $photo_url;
						$updateArr['feed_type'] = $feedType;																
						$this->db->where('id',$feed['id']);
						$this->db->update('user_feed',$updateArr);
												
					} else {
					
						$insertFeed = array(
							'user_id' => $this->session->userdata("userid"),
							'id' => $feed['id'],
							'story' => $isset,
							'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
							'link' => $refLink, 
							'likes_count' => $likes_count,
							'comment_count' => $comment_count,
							'share_count' => $share_count,	
							'photo_url' => $photo_url,
							'feed_type' =>$feedType,							
						);
						$this->db->insert('user_feed',$insertFeed);
					}
				}
		} 
		
		$sql = "SELECT * FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
		$pageData = $this->model->allSelects($sql);
		
	if(count($pageData)>0){
		
		foreach($pageData as $key=>$val){

			$fields = "?fields=likes.summary(true),comments.summary(true),shares,attachments,picture,created_time,story,message";
			$params =  array('since'=>$fromdate,'until'=>$tilldate,'access_token'=>$val['page_access_token']);
			$userFeed  = $this->facebook->api('/'.$val['page_social_id'].'/posts'.$fields,'GET',$params);

				if($userFeed['data']){
				
					foreach($userFeed['data'] as $key1=>$feed){
					
							#Searching story from Feeds
							$isset="";
							if(isset($feed['story']) && $feed['story'] != '' && array_key_exists('story', $feed))
							{$isset = $feed['story'];}
							if(isset($feed['message']) && array_key_exists('message', $feed))
							{$isset .= $feed['message'];}
							if(array_key_exists('description', $feed) && isset($feed['description']))
							{$isset .= $feed['description'];}
														
							#Declare Variables
							$likes_count =0; $comment_count = 0; $share_count = 0;$view_count=0;
							$refLink =""; $feedType=""; $photo_url ="";$objectid=0;
							
							#Calculation of Interactions				
							if(isset($feed['likes']) && isset($feed['likes']['summary'])){
								//$likes_count = count($feed['likes']['data']);
								$tlikes = $feed['likes']['summary'];
								if(isset($tlikes['total_count'])){
									$likes_count = $tlikes['total_count'];
								}
							}
		
							if(isset($feed['comments']) && isset($feed['comments']['summary'])){
								//$comment_count = count($feed['comments']['data']);
								$tcomment = $feed['comments']['summary'];
								if(isset($tcomment['total_count'])){
									$comment_count = $tcomment['total_count'];
								}
							}
		
							if(isset($feed['shares']) && isset($feed['shares']['count'])){
								$share_count = $feed['shares']['count'];
							}
							
							#Link of activity
							if(isset($feed['link'])){
								$refLink = $feed['link'];
							}
							
							#Type of Feed
							if(isset($feed['type'])){
								$feedType = $feed['type'];
							}
							
							if(isset($feed['type']) && $feed['type']=='photo'){
								$photo_url = $feed['picture'];
							}

						$existFeed = $this->db->get_where('user_feed',array('id'=>$feed['id']))->row_array();
						
						if(count($existFeed) > 0 ){
						
								$updateArr	=	array();
								$updateArr['story']	= $isset;
								$updateArr['user_id'] = $userRow['userid'];
								$updateArr['link']	= $refLink;
								$updateArr['feed_type']	= $feedType;
								$updateArr['photo_url']	= $photo_url;
								$updateArr['likes_count'] =	$likes_count;							
								$updateArr['comment_count']	= $comment_count;
								$updateArr['share_count']	= $share_count;	
								$updateArr['view_count']	= $view_count;							
								$updateArr['post_type']	= 'page_feed';
								$this->db->where('id',$feed['id']);
								$this->db->update('user_feed',$updateArr);
							
						} else {						
								$insertFeed = array(
									'user_id' => $userRow['userid'],
									'id' => $feed['id'],
									'story' => $isset,
									'created_time' => date("Y-m-d H:i:s",strtotime($feed['created_time'])),
									'link' => $refLink,
									'likes_count' => $likes_count,
									'comment_count' => $comment_count,
									'share_count' => $share_count,	
									'view_count' => $view_count,							
									'feed_type'	=> $feedType,
									'photo_url'	=> $photo_url,
									'post_type' => 'page_feed'					
								);	
							$this->db->insert('user_feed',$insertFeed);
							
						}
					}
				}												
			}
		}
		
		$this->session->set_flashdata('sucess', 'Facebook Feeds Updated Successfully.');
		if($reDirect==0){ redirect('staff/mediaSetup'); }
		if($reDirect==1){ redirect('staff'); }
		
	  	}	
	}
  }
}
 catch (Exception $e) {
 	
 	$this->session->set_flashdata('error', $e->getMessage());
	if($reDirect==0){ redirect('staff/mediaSetup'); }
	if($reDirect==1){ redirect('staff'); }
 }
 
 		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);

 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-fb-feeds-refresh',$data,true);
		$this->load->view('layout-inner',$data);	
}

/*Facebook Section Ends*/
#===============================================================================#
	
 /*Twiter Section Starts*/
 
 public function addTwitterUser() {
		/* twredirect() parameter - callback point in your application */
		/* by default the path from config file will be used */
		//$ok = $this->twconnect->twredirect('staff/callback/1');
		
		$ok = $this->twconnect->twredirect('staff/callback');
		if (!$ok) {
			redirect('staff');
		}
	}
	
 #calling back from twitter after true login
 public function callback(){
		$ok = $this->twconnect->twprocess_callback();
		if ( $ok ) { redirect('staff/success'); }
			else redirect ('staff/failure');
 }
	
 public function success() {
 
		$this->twconnect->twaccount_verify_credentials();
		$user = $this->twconnect->tw_user_info->id;
		$secrets = $this->session->userdata('tw_access_token');
		
		#Check To confirm progress
		if($user==""){
			$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout Twitter and Try again.");
			  	redirect('staff');
			}	
				
		if($secrets['oauth_token']==""){
				$this->session->set_flashdata('error', "Authorization Failed. Please Logout Twitter and Try again.");
			  	redirect('staff');				
			}	
		
		$username = $this->twconnect->tw_user_info->screen_name;
		// check for exists user
		$existRow = $this->db->get_where('user_media',array('social_id'=>$user))->row_array();
		if(count($existRow) > 0 ){
			#already Exists
			$this->session->set_flashdata('sucess', 'Twitter Account Already Authorized.');
			//redirect('staff/clearsession');
			redirect('staff');
		} else {
			$twitter_data = $this->getUserTweets( $this->twconnect->tw_user_info->screen_name);
			$insertArray = array(
					'userid'=>$this->session->userdata("userid"),
					'username'=> $username,
					'fullname'=> isset($this->twconnect->tw_user_info->name) ? $this->twconnect->tw_user_info->name : '',
					'social_id'=> $this->twconnect->tw_user_info->id,
					'image' => isset($this->twconnect->tw_user_info->profile_image_url) ? $this->twconnect->tw_user_info->profile_image_url : '',
					'created_date'=>date('Y-m-d H:i:s'),
					'social_type'=>'twitter',
					'access_token'=>$secrets['oauth_token'],
					'status'=>'Active'
				);
			$this->db->insert('user_media',$insertArray);
						
			$twitter_data = $this->getUserTweets( $this->twconnect->tw_user_info->screen_name);
						
			foreach($twitter_data as $tweet){
				$id_str	=	$tweet->id_str;
				$userInfo	=	$tweet->user;
				$user_id	=	$userInfo->id_str;
				$twitter_url	=	"http://twitter.com/$user_id/status/$id_str";
				$insertTweets = array(
					'user_id' => $this->session->userdata("userid"),
					'id' => $tweet->id_str,
					'text' => $tweet->full_text,
					'created_at' => date( 'Y-m-d H:i:s', strtotime($tweet->created_at) ),
					'link'		 =>	$twitter_url,
					'retweet_count'	 =>	$tweet->retweet_count,
					'favorite_count' =>	$tweet->favorite_count					
				);
				$this->db->insert('user_tweets',$insertTweets);
			}
		}
		
		$this->session->set_flashdata('sucess', 'Twitter Account is Authorized Successfully.');
		redirect('staff/mediaSetup');
	}
	
 /* Authentication un-successful */
 public function failure() {
		echo '<p>Twitter connect failed</p>';
		echo '<p><a href="' . base_url() . 'staff/clearsession">Try again!</a></p>';
	}
	
 /* Clear session */
 public function clearsession() {
 
		$this->session->set_flashdata('sucess', 'Twitter Account is Authorized Successfully.');
		redirect('staff/mediaSetup');
	}
	
 #it will update selected user's tweet feed
 public function updateTweets(){
	
	$id=$this->session->userdata("userid");
	
		$userRow = $this->db->get_where('user_media',array('userid'=>$id,'social_type'=>'twitter'))->row_array(); //getting tw username	
		$twitter_data = $this->getUserTweets($userRow['username']);
		
		/*echo "<pre>";		
			print_r($twitter_data);
		echo "</pre>"; die;*/
			
		if(count($twitter_data) > 0){
			foreach($twitter_data as $tweet){	
				$id_str	=	$tweet->id_str;
				$userInfo	=	$tweet->user;
				$user_id	=	$userInfo->id_str;
				$twitter_url	=	"http://twitter.com/$user_id/status/$id_str";			
				$existTweet = $this->db->get_where('user_tweets',array('id'=>$tweet->id_str))->row_array();
					if(count($existTweet) > 0 ){
						$updateArr	=	array();					
						//$updateArr['text']	=	$tweet->text;
						$updateArr['text']	=	$tweet->full_text;
						$updateArr['link']	=	$twitter_url;
						$updateArr['retweet_count']	=	$tweet->retweet_count;					
						$updateArr['favorite_count']	=	$tweet->favorite_count;					
						$this->db->where('id',$tweet->id_str);
						$this->db->update('user_tweets',$updateArr);						
					} else {												
						$insertTweets = array(
							'user_id' => $id,
							'id' => $tweet->id_str,
							'text' => $tweet->full_text,
							'created_at' => date( 'Y-m-d H:i:s', strtotime($tweet->created_at) ),
							'link'		 =>	$twitter_url,
							'retweet_count'		=>	$tweet->retweet_count,
							'favorite_count'	=>	$tweet->favorite_count
						);						
						$this->db->insert('user_tweets',$insertTweets);
					}
				}
		} else {
			#nothing found
		}
		$this->session->set_flashdata('sucess', 'Twitter Tweets Updated Successfully.');
		redirect('staff/mediaSetup');
	}

 #Common function to get feed of tweet by just passing username/screenname
 public function getUserTweets($screenName){
		#$screenName = 'NikunjEragon';
		$feed_url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=$screenName&tweet_mode=extended";

		if(empty($feed_url)) // url specified ?
		{
			return false;
		}
			
		$this->load->library('twitteroauth/twitteroauth');
		$connection = new TwitterOAuth($this->CONSUMER_KEY, $this->CONSUMER_SECRET, $this->ACCESS_TOKEN, $this->ACCESS_TOKEN_SECRET);
		$content = $connection->get($feed_url);
		return $content;
	}

 /*===========INSTAGRAM===============*/
	
 public function instagram_redirect(){
		
		redirect('staff/addInstaAccounts');
		
		$code = $this->input->get('code');
		
		//receive OAuth token object
		$data = $this->instagram->getOAuthToken($code);
		$username = $data->user->username;
		
		//store user access token
		$this->instagram->setAccessToken($data);
		$user = $this->instagram->getUser();

		#Check To confirm progress
		if($user->data->id==""){
			$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout Instagram and Try again.");
			redirect('staff');
		}	
				
		if($data->access_token==""){
			$this->session->set_flashdata('error', "Authorization Failed. Please Logout Instagram and Try again.");
			redirect('staff');				
		}	
		
		$insertArray = array(
					'userid'=>$this->session->userdata("userid"),
					'username'=> $user->data->username,
					'fullname'=> $user->data->full_name,
					'social_id'=> $user->data->id,
					'image' => $user->data->profile_picture,
					'created_date' =>date('Y-m-d H:i:s'),
					'social_type' =>'instagram',
					'access_token' => $data->access_token,
					'status'=>'Active'
				);
			$this->db->insert('user_media',$insertArray);
						
		$userFeeds = $this->instagram->getUserMedia();
		$regexEmoticons = '/[\x{1F600}-\x{1F64F}]/u\n';
		
		foreach($userFeeds->data as $userFeed){
		
			$feedArray = array(
					'id' => $userFeed->id,
					'user_id' => $this->session->userdata("userid"),
					'text' => ($userFeed->caption->text) ? $userFeed->caption->text : '',
					'link' => $userFeed->link,
					'likes' => $userFeed->likes->count?$userFeed->likes->count:0,					
					'created_time' => $userFeed->created_time,
					'comments_count' => $userFeed->comments->count,
					'likes_count' => $userFeed->likes->count?$userFeed->likes->count:0,
					'thumbnail_image' => $userFeed->images->thumbnail->url,
					'standard_image' => $userFeed->images->standard_resolution->url,
			);
			$this->db->insert('user_instagram_feed',$feedArray);
		}
		
		$this->session->set_flashdata('sucess', 'Instagram Account is Authorized Successfully.');
  		redirect('staff/mediaSetup');
	}

#This will update selected user's instagram feed

 public function _oldupdateInstagramFeed($reDirect=0){
 
		$userid = $this->session->userdata("userid");
		$userRow = $this->db->get_where('user_media',array('userid'=>$userid,'social_type'=>'instagram'))->row_array(); //getting Instagram username		
		
		//$instagram_data = $this->getInstagramFeed($userRow['social_id'],$userRow['access_token']);
		$instagram_info = $this->getInstagramFeed($userRow['social_id'],$userRow['access_token']);
		$regexEmoticons = '/[\x{1F600}-\x{1F64F}]/u';
		
		if($instagram_info->meta->code==400){
			$this->session->set_flashdata('error', 'Authorization Expired. Please contact Anglertrack Support.');
			if($reDirect==0) { redirect('staff/mediaSetup'); }
			if($reDirect==1) { redirect('staff'); }
		}
		
		if($instagram_info->data){	
		
			#Initialise DATA
			$instagram_data = $instagram_info->data;
			
			if(count($instagram_data) > 0){
					
					/*echo "<pre>";
						print_r($instagram_data);
					echo "</pre>"; die;*/
					
					foreach($instagram_data as $instagram){
						
						$existInstFeed = $this->db->get_where('user_instagram_feed',array('id'=>$instagram->id))->row_array();
						
							if(count($existInstFeed) > 0 ){
		
								$updateArr	=	array(); 
		
								$instaText =""; $instaCleanText = "";
								//$instaText      = @$instagram->caption->text?@$instagram->caption->text : '';
								//$instaText      = mb_convert_encoding($instagram->caption->text,"UTF-8");
								//$instaCleanText = preg_replace($regexEmoticons, '', $instaText);
								
								$updateArr['id']	= $instagram->id;
								$updateArr['text']	= @$instagram->caption->text?@$instagram->caption->text : '';
								$updateArr['link']	= $instagram->link;
								$updateArr['likes']	= $instagram->likes->count?$instagram->likes->count:0;					
								$updateArr['comments_count']  =	$instagram->comments->count;					
								$updateArr['likes_count']	  =	$instagram->likes->count?$instagram->likes->count:0;					
								$updateArr['created_time']	  = strtotime($instagram->created_time);
								$updateArr['thumbnail_image'] =	$instagram->images->thumbnail->url;		
								$updateArr['standard_image']  =	$instagram->images->standard_resolution->url;			
								$this->db->where('id',$instagram->id);
								$this->db->update('user_instagram_feed',$updateArr);
								
					/*echo "<pre>";
						print_r($updateArr);
					echo "</pre>"; */
							
							} else {
		
								$insertInstagram = array(
									'id' => $instagram->id,
									'user_id' =>$this->session->userdata("userid"),
									'text' => @$instagram->caption->text?@$instagram->caption->text : '',
									'link' => $instagram->link,
									'likes' => $instagram->likes->count?$instagram->likes->count:0,						
									'created_time' => strtotime($instagram->created_time),
									'comments_count' => $instagram->comments->count,	
									'likes_count' => $instagram->likes->count?$instagram->likes->count:0,
									'thumbnail_image' => $instagram->images->thumbnail->url,	
									'standard_image' => $instagram->images->standard_resolution->url,
									
								);
								$this->db->insert('user_instagram_feed',$insertInstagram);
		
					/*echo "<pre>";
						print_r($insertInstagram);
					echo "</pre>"; */
		
							}
						}
				} 
		}
		else {
			#nothing found
		}
		
		$this->session->set_flashdata('sucess', 'Instagram Feeds Updated Successfully.');
		if($reDirect==0) { redirect('staff/mediaSetup'); }
		if($reDirect==1) { redirect('staff'); }
 }
	
 public function _oldgetInstagramFeed($userId,$userToken){
		
		$feed_url = "https://api.instagram.com/v1/users/$userId/media/recent/?access_token=$userToken";
		#pre($feed_url); die;
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $feed_url);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);     
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 2);
		curl_setopt($ch, CURLOPT_HEADER, 0);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$result = curl_exec($ch);
		curl_close($ch);
		$result = json_decode($result);
		//return $result->data;
		return $result;
	}
	
/* ===Instagram Ends Here=== */

 public function updateprofile(){
	
		$this->form_validation->set_rules('txtname', 'Your Name', 'required');
		$this->form_validation->set_rules('txtemail', 'Email', 'required|valid_email');
    	$this->form_validation->set_error_delimiters('<div style="color:red">', '</div>');

		$user_id = $this->session->userdata("userid");
		
    	$sql = "SELECT u.FirstLastname,u.dob,u.email,u.phone,u.street,u.city,u.state,u.zipcode,upad.boat_MMY,upad.motor_MMY,upad.motor_VIN,upad.electronics_model,
				upad.rods_brand,upad.glove_size,upad.reels_brand,u.fishingtype,upad.isguide,upad.prefer_hardware,upad.water_body_type,upad.shirt_size,upad.other_sposors_1,
				upad.fishingorganization,u.at_profile_pic FROM user_pro_app_details upad
				LEFT JOIN users u ON u.userid = upad.userid
				WHERE u.memtype='Staff' AND u.userid=$user_id";

		$data["profileinfo"] = $this->model->allSelects($sql);	
		
		//$data['profileinfo']= $this->model->getProfileinfo($this->session->userdata("userid"));
		
		if($this->form_validation->run() == FALSE){		
        
					$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        			$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
					
					$data['loginUrl'] = $this->facebook->getLoginUrl(array(
										'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
										'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
					));
					
					$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
					$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
											
					$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
					$data["fblink"]=$this->model->allSelects($sql);
			
					$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
					$data["twlink"]=$this->model->allSelects($sql);
					
					$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
					$data["instalink"]=$this->model->allSelects($sql);
			
					$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
					$data["youtubelink"]=$this->model->allSelects($sql);
											
					$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
					$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
					$data['errorreg']= str_replace("<p>","", str_replace("</p>","",validation_errors()));
									
					$data['content']=$this->load->view('staff/manageprofile',$data,true);
					$this->load->view('layout-inner',$data);
					
				}
        else{
				
 					$dob = $this->input->post('year')."-".$this->input->post('month')."-".$this->input->post('day');
 
							$member=array(
								'FirstLastname'=>$this->input->post('txtname'),
								'dob'=>$dob,
								'email'=>$this->input->post('txtemail'),
								'phone'=>$this->input->post('txtcontactno'),
								'street'=>$this->input->post('txtstreet'),
								'city'=>$this->input->post('txtcity'),
								'state'=>$this->input->post('txtstate'),
								'zipcode'=>$this->input->post('txtzipcode'),
								'fishingtype'=>$this->input->post('fishingtype'),
								'memtype'=>"Staff"
							   );
  
								$memberApp=array(
								'boat_MMY'=>$this->input->post('txtboat_MMY'),
								'motor_MMY'=>$this->input->post('txtmotor_MMY'),
								'motor_VIN'=>$this->input->post('txtmotor_VIN'),
								'electronics_model'=>$this->input->post('txtelectronics_model'),
								'rods_brand'=>$this->input->post('txtrods_brand'),
								'reels_brand'=>$this->input->post('txtreels_brand'),
								'isguide'=>$this->input->post('txtguide'),
								'prefer_hardware'=>$this->input->post('txtpfheadwear'),
								'water_body_type'=>$this->input->post('txtwaterbodytype'),
								'shirt_size'=>$this->input->post('txtshirtsize'),
								'other_sposors_1'=>$this->input->post('txtothersponsor'),
								'fishingorganization' => $this->input->post('txtfishingOrgz'),
								'glove_size' => $this->input->post('glove_size'),
							   );
      
						$profile_pic_path = 'None';
						
						if($_FILES['profile_pic_path']['error'] == 0){
							$fileName = $_FILES['profile_pic_path']['name'];
							$file='profile_pic_path';		
							$upload = uploadFile($file,'*','assets/upload/profilepics',$fileName);
							if($upload['success']){
								$profile_pic_path	=	$upload['path'];
							}									
							$member['at_profile_pic']	=	$profile_pic_path;	
						}			
							   
						$this->model->updateprofile($this->session->userdata("userid"),$member);
						$this->model->allUpdate('userid',$this->session->userdata("userid"),'user_pro_app_details',$memberApp);
						$this->session->set_flashdata('sucess', 'Profile Updated Successfully..!!');
						redirect('staff/updateprofile');

					}	
	}	


 public function facebookgallery(){
 			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);	
		$data['errorreg']= str_replace("<p>","", str_replace("</p>","",validation_errors()));				
		$data['content']=$this->load->view('staff/facebookgallery',$data,true);
		$this->load->view('layout-inner',$data);					
	}		

 public function galleryfb(){

		$user_id	=	$this->input->post('user_id');
		$search_by = ""; $search_value = "";

		if (@$_POST){
		
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
			
        } 

		$fb_append_qry	=	'';		
		$fbkeywords	=	getKeywords($this->session->userdata("userid"),'fbkeywords');
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			$photo_keyword	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
				$photo_keyword[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";				
			}
			$append_qry		=	implode(' or ',$keyword_query);
			$append_qry2	=	implode(' or ',$photo_keyword);
			$fb_append_qry	.=	" and ($append_qry)";
			$fb_append_qry2	.=	" and ($append_qry2)";
		}
		
		if($search_by	==	'bykeyword' && strlen($search_value)>0){
			$feed_qry	.=	" and story like '%$search_value%'";
			$photo_qry	.=	" and text like '%$search_value%'";			
		}
		
		$sql=	"(select DISTINCT id,link AS linkurl,likes_count,comment_count,image_url as pic_url,text as text, created_time 
				from user_photo where user_id=".$this->session->userdata("userid")." $fb_append_qry2 group by user_photo.id)
				Union
				(select DISTINCT id,link AS linkurl,likes_count,comment_count,photo_url as pic_url,story as text,created_time from user_feed 
				where feed_type='photo' and user_id=".$this->session->userdata("userid")." $fb_append_qry group by user_feed.id)	
				order by created_time desc";
							
		$GalleryInfoFB = $this->model->allSelects($sql);
		echo json_encode($GalleryInfoFB);
}

 public function galleryLoadfb(){

		$user_id = $this->input->post('user_id');	
		$search_by = ""; $search_value = "";

		if (@$_POST) {
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
        } 

		$fb_append_qry	=	'';		
		$fbkeywords	=	getKeywords($this->session->userdata("userid"),'fbkeywords');
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			$photo_keyword	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
			}
			$append_qry		=	implode(' or ',$keyword_query);
			$fb_append_qry	.=	" and ($append_qry)";
		}
		
		if($search_by	==	'bykeyword' && strlen($search_value)>0){
			$feed_qry	.=	" and story like '%$search_value%'";
		}
				
		$sql="SELECT id,link AS linkurl,likes_count,comment_count,photo_url as pic_url,story as text,created_time, 'UserFeed' AS sourceTable
			  FROM user_feed where feed_type='photo' and user_id=".$this->session->userdata("userid")." $fb_append_qry GROUP BY id ORDER BY created_time desc";
			  echo $sql;
		$GalleryInfo = $this->model->allSelects($sql);
		
		echo json_encode($GalleryInfo);
		print_r($GalleryInfo);
		die('heree');
 }

 public function getFbPic(){

	$sql="";
	
	if($_POST)
	{
		if($_POST['photo_id']!="" && $_POST['sourcetable']=='UserPhoto'){		
		$sql=	"select DISTINCT id,link AS linkurl,likes_count,comment_count,image_url as pic_url,text as text, created_time
				from user_photo where user_id=".$this->session->userdata("userid")." AND id='".$_POST['photo_id']."'";
		}
		
		if($_POST['photo_id']!="" && $_POST['sourcetable']=='UserFeed')
		{
		
		$sql="select DISTINCT id,link AS linkurl,likes_count,comment_count,photo_url as pic_url,story as text,created_time
				 from user_feed where feed_type='photo' and user_id=".$this->session->userdata("userid")." AND id='".$_POST['photo_id']."'";
		}			
		$data['GalleryInfo'] = $this->model->allSelects($sql);
		$this->load->view('staff/facebookgalleryview',$data);
	}	

}

# Instagram Gallery
 public function instragramgallery(){
 		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
															
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['errorreg']= str_replace("<p>","", str_replace("</p>","",validation_errors()));
						
		$data['content']=$this->load->view('staff/instagramgallery',$data,true);
		$this->load->view('layout-inner',$data);					
	}			

 public function galleryInstagram(){

	     $search_by = ""; $search_value = "";
	
		if (@$_POST) {
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
         } 
		
		$sql=	"SELECT DISTINCT uif.id, uif.link as linkurl,uif.text,uif.likes_count,uif.standard_image 
		         FROM user_instagram_feed uif
				 where uif.user_id=".$this->session->userdata("userid");
		$instakeywords	=	getKeywords($this->session->userdata("userid"),'instakeywords');
			
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (uif.text like '".trim($val)."%' or uif.text like '%".trim($val)."%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}	
				
		if($search_by	==	'bykeyword' && strlen($search_value)>0){
			$sql	.=	" and uif.text like '%$search_value%'";
		}
						
		$sql = $sql. "  GROUP BY uif.id ORDER BY uif.created_time desc";
		//echo $sql;
		$GalleryInfo = $this->model->allSelects($sql);
		//echo json_encode($GalleryInfo);
		 //die('hererer');

	}

public function getInstaPic(){

	$sql="";
	
	if($_POST){
	
		if($_POST['photo_id']!=""){	
			
		$sql=	"SELECT uif.id,uif.link as linkurl,uif.text,uif.likes_count,uif.standard_image AS pic_url, uif.comments_count FROM user_instagram_feed uif
				 WHERE uif.user_id=".$this->session->userdata("userid")." AND uif.id='".$_POST['photo_id']."'";
		}
		
		$data['GalleryInfo'] = $this->model->allSelects($sql);
		$this->load->view('staff/instagramgalleryview',$data);
	}	

}
	
 public function facebookvideo(){
 			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
		
		$fb_append_qry	=	'';		
		$fbkeywords	=	getKeywords($this->session->userdata("userid"),'fbkeywords');
		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			$photo_keyword	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
				$photo_keyword[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";				
			}
			$append_qry		=	implode(' or ',$keyword_query);
			$append_qry2	=	implode(' or ',$photo_keyword);
			$fb_append_qry	.=	" and ($append_qry)";
			$fb_append_qry2	.=	" and ($append_qry2)";
		}
		

		$sql=	"(select DISTINCT id,link ,likes_count,comment_count,video_thumb,text as text, view_count,created_time, 'UserVideo' AS sourceTable 
				from user_video where user_id=".$this->session->userdata("userid")." $fb_append_qry2 group by user_video.id)
				Union
				(select DISTINCT id,link ,likes_count,comment_count,photo_url as video_thumb,story as text, view_count,created_time, 'UserFeed' AS sourceTable
				 from user_feed where feed_type='video' and user_id=".$this->session->userdata("userid")." $fb_append_qry group by user_feed.id)	
				order by created_time desc";

		//$sql=	"select * from user_video where user_id=".$this->session->userdata("userid")." $fb_append_qry order by created_time desc";
		$data["VideoInfo"]=$this->model->allSelects($sql);

		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['errorreg']= str_replace("<p>","", str_replace("</p>","",validation_errors()));
		$data['content']=$this->load->view('staff/facebookvideo',$data,true);
		$this->load->view('layout-inner',$data);					
	}	
	
# View keywords
 public function viewkeywords(){
 
		$sql="Select fbkeywords, twtkeywords, instakeywords from users where userid=".$this->session->userdata("userid");			
		$data["allkeywords"]=$this->model->allSelects($sql);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
    	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);				
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/add-update-keywords',$data,true);
		$this->load->view('layout-inner',$data);

	}

 public function updatekeywords(){
 
        if(isset($_POST["txtfbkey"],$_POST['txttwtkey'],$_POST['txtinstakey'])){
           	
			$qstr="update users set fbkeywords='".$_POST["txtfbkey"]."', twtkeywords='".$_POST["txttwtkey"]."', instakeywords='".$_POST["txtinstakey"]."'
			       where userid=".$this->session->userdata("userid");
			$this->model->allQueries($qstr); 
			/*$updatearr	=	array(
						'fbkeywords'	=>	$_POST["txtfbkey"],
						'twtkeywords'	=>	$_POST["txttwtkey"],
						'instakeywords'	=>	$_POST["txtinstakey"]
				);
			$this->session->set_userdata($updatearr);*/
			$this->session->set_flashdata('sucess', 'Keywords Updated Sucessfully..!!');             
			redirect('staff/viewkeywords');
            
        }        
    }	

# ADD EVENT CALENDAR

 public function addEventCalendar(){

	  if(isset($_POST["eventid"],$_POST["eventdate"])){   
		              
            $eventcal=array(
                  'eventid'=>$_POST["eventid"],
                  'userid'=>$this->session->userdata("userid"),
                  'eventdate'=>$_POST["eventdate"]
                 );
        
		$this->model->allInserts('eventcalendar',$eventcal);
		$this->session->set_flashdata('sucess', 'Successfully Added Event into your Calendar..!!');
		echo "Done";
		redirect('staff');
		}
		else{
		$this->session->set_flashdata('sucess', 'Not Added!!');
		echo "Done";
		redirect('staff');
		}
		            
 }

# ADD Tournament

 public function addTournament($tournamentid=0){
	
			$this->form_validation->set_rules('txttournamenttitle', 'Tournament Title is Required', 'required');
			$this->form_validation->set_rules('txttournamentdate', 'Tournament date is required', 'required');
				
			if ($this->form_validation->run() == TRUE){	
										
					$tournament=array(
							'tournamenttitle'=>$this->input->post('txttournamenttitle'),
							'Anglername'=>$this->input->post('txtResulturl'),
							'tfinishedplace'=>$this->input->post('txtfinishedplace'),
							'treferencename'=>$this->input->post('txtreferencename'),
							'tcontactinfo'=>$this->input->post('txtreferncecontactinfo'),
							'tournamentdate'=>date('Y-m-d',strtotime($this->input->post('txttournamentdate'))),
							'staffid'=>$this->session->userdata("userid"),
							'status'=>"Active"							
 	       );
				
				if($this->input->post('txtmanufacturers')==""){ $tournament['sharedTO'] = "all"; }
				else { $tournament['sharedTO'] = $this->input->post('txtmanufacturers'); }
		
						if($tournamentid>0){
						
							$this->model->allUpdate('tournamentid',$tournamentid,'tournaments',$tournament);
							$this->session->set_flashdata('sucess', 'Tournament Updated Successfully..!!');
							redirect('staff/alltournaments');
							
						}
						else{
        
					$this->model->allInserts('tournaments',$tournament);
					
					# For Notification
					/*$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
					$ulist=$this->model->allSelects($sql);
					$this->emailSend('IMPORTANT New Tournament added by Pro-Staff Member',$ulist);*/
					
					$this->session->set_flashdata('sucess', 'Tournament Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/alltournaments');			
				}

			}
			else
			{
			
						if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($tournamentid>0){
						
							$sql	=	"select * from tournaments where tournamentid=$tournamentid";
							$TempInfo	=	$this->model->allSelects($sql);
							$tournaInfo	=	$TempInfo[0];				
							$data['txttournamenttitle']		= $tournaInfo['tournamenttitle'];
							$data['txttournamentdate']		= $tournaInfo['tournamentdate'];
							$data['txtreferencename']		= $tournaInfo['treferencename'];
							$data['txtreferncecontactinfo'] = $tournaInfo['tcontactinfo'];
							$data['txtfinishedplace']		= $tournaInfo['tfinishedplace'];
							$data['txtResulturl']			= $tournaInfo['Anglername'];
							$data['txtsharedTO']	 		= $tournaInfo['sharedTO'];
						}		
						else{
							$data['txttournamenttitle']		= '';
							$data['txttournamentdate']		= '';
							$data['txtreferencename']		= '';
							$data['txtreferncecontactinfo'] = '';
							$data['txtfinishedplace']		= '';
							$data['txtResulturl']			= '';
							$data['txtsharedTO']	 		= 'all';
							
						}	
					
			}


				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				/*$sql = "Select cr.companyID AS companyID, u.FirstLastname as FirstLastname  
			   			from users u
			   			LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   			where u.memtype='Employer' AND cr.userid=".$this->session->userdata("userid");*/

				$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
						LEFT JOIN company_ref cr ON cr.companyID=u.userid
						where cr.userid=".$this->session->userdata("userid").")
						UNION
						(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid").")";	
						
				$ulist=$this->model->allSelects($sql);
				$data['ulist']=$ulist;

				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				$data['tournamentid']=$tournamentid;
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-tournament',$data,true);
				$this->load->view('layout-inner',$data);
			
 }

 public function addTournamentSch($tournamentid=0){
	
			if($tournamentid==0){
				$this->form_validation->set_rules('txttournamenttitle', 'Tournament Title is Required', 'required');
				$this->form_validation->set_rules('txttournamentdate', 'Tournament date is required', 'required');
			}
				
			if($tournamentid>0){
				$this->form_validation->set_rules('txtfinishedplace', 'Finished Place is Required', 'required');
			}

			if ($this->form_validation->run() == TRUE){	
										
					if($tournamentid==0){
					
						$tournament=array(
								'tournamenttitle'=>$this->input->post('txttournamenttitle'),
								'treferencename'=>$this->input->post('txtreferencename'),
								'tcontactinfo'=>$this->input->post('txtreferncecontactinfo'),
								'tournamentdate'=>date('Y-m-d',strtotime($this->input->post('txttournamentdate'))),
								'staffid'=>$this->session->userdata("userid"),
								'status'=>"Pending"							
						   );
					}
					
					
					if($tournamentid>0){
					
						$tournament=array(
								'Anglername'=>$this->input->post('txtResulturl'),
								'tfinishedplace'=>$this->input->post('txtfinishedplace'),
								'status'=>"Active"							
						   );
						   
						if($this->input->post('txtmanufacturers')==""){ $tournament['sharedTO'] = "all"; }
				
						else { $tournament['sharedTO'] = $this->input->post('txtmanufacturers'); }

					}

					if($tournamentid>0){
						
							$this->model->allUpdate('tournamentid',$tournamentid,'tournaments',$tournament);
							$this->session->set_flashdata('sucess', 'Tournament Updated Successfully..!!');
							redirect('staff/alltournaments');
							
						}
					else{
        
					$this->model->allInserts('tournaments',$tournament);
					
					# For Notification
					/*$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
					$ulist=$this->model->allSelects($sql);
					$this->emailSend('IMPORTANT New Tournament added by Pro-Staff Member',$ulist);*/
					
					$this->session->set_flashdata('sucess', 'Tournament Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/alltournaments');			
				}

			}
			else
			{
			
						if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($tournamentid>0){
						
							$sql	=	"SELECT * FROM tournaments WHERE tournamentid=$tournamentid";
							$TempInfo	=	$this->model->allSelects($sql);
							$tournaInfo	=	$TempInfo[0];				
							$data['txttournamenttitle']					=	$tournaInfo['tournamenttitle'];
							$data['txttournamentdate']					=	$tournaInfo['tournamentdate'];
							$data['txtreferencename']					=	$tournaInfo['treferencename'];
							$data['txtreferncecontactinfo']				=	$tournaInfo['tcontactinfo'];
							$data['txtfinishedplace']					=	$tournaInfo['tfinishedplace'];
							$data['txtResulturl']						=	$tournaInfo['Anglername'];
							$data['txtsharedTO']	 					=	$tournaInfo['sharedTO'];
							$data['txtStatus']	 						=	$tournaInfo['status'];
							
						}		
						else{
							$data['txttournamenttitle']					=	'';
							$data['txttournamentdate']					=	'';
							$data['txtreferencename']					=	'';
							$data['txtreferncecontactinfo']				=	'';
							$data['txtfinishedplace']					=	'';
							$data['txtResulturl']						=	'';
							$data['txtsharedTO']	 					= 	'all';
							$data['txtStatus']	 						=	'';
							
						}	
					
			}

				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
				$ulist=$this->model->allSelects($sql);
				$data['ulist']=$ulist;

				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				$data['tournamentid']=$tournamentid;
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-tournament-schedule',$data,true);
				$this->load->view('layout-inner',$data);			
 }

#Manage Tournaments
 public function allTournaments(){
						
			$sql="Select * from tournaments where staffid=".$this->session->userdata("userid")." ORDER BY tournamentdate desc";
			
			$data["tournamentlist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/managetournaments',$data,true);
			$this->load->view('layout-inner',$data);			
 }

#Manage Tournament Status
 public function tournamentstatus(){
 
		if(isset($_POST["tournamentid"],$_POST["status"])){
        
			if($_POST["status"]=="Delete"){
			$qstr="delete from tournaments where tournamentid=".$_POST["tournamentid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Tournament Deleted Sucessfully..!!');             
			echo "<strong>Tournament Deleted Sucessfully..!!</strong>";

			}
			else{			
			$qstr="update tournaments set status='".$_POST["status"]."' where tournamentid=".$_POST["tournamentid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
			 }        
        } 
 }

#Filter Tournament

 public function filterTournaments(){
 
 	if(isset($_POST["filterBystatus"])){
	  
	  		if($_POST["filterBystatus"]!="Filter By Status")
			{
			
				$sql="Select * from tournaments where staffid=".$this->session->userdata("userid")." and status='".$_POST["filterBystatus"]."'";
			
				$data["tournamentlist"]=$this->model->allSelects($sql);
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
										'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
										'redirect_uri' => site_url() . '/staff/facebook_redirect/'));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/managetournaments',$data,true);
				$this->load->view('layout-inner',$data);
			
			}
		   else
			{
			
				$sql="Select * from tournaments where staffid=".$this->session->userdata("userid");
			
				$data["tournamentlist"]=$this->model->allSelects($sql);
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/managetournaments',$data,true);
				$this->load->view('layout-inner',$data);			
			}
	  	}
 }

#BULK Tournament Status
 public function managebulktournaments(){
 
        if(isset($_POST["id"],$_POST['action'])){
            $id=$_POST["id"];
            $action=$_POST['action'];
           	
			$this->model->updateTournamentstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";           
        }        
  }

#ADD BLOG
 public function addblog($blogid=0){
	
			$this->form_validation->set_rules('txtblogname', 'Blog or Website Name is Required', 'required');
			//$this->form_validation->set_rules('txtbloglink', 'Blog Link is required', 'required');
				
			if($this->form_validation->run() == TRUE){
			
				$blog=array(
              				'blogname'=>$this->input->post('txtblogname'),
							'link'=>$this->input->post('txtbloglink'),
							'coveragetype'=>$this->input->post('txtcoveragetype'),
							'refproducts'=>$this->input->post('txtrefproducts'),
              				'userid'=>$this->session->userdata("userid"),
							'status'=>"Active"							
 	       		);

				if($this->input->post('txtmanufacturers')==""){ $blog['sharedTO'] = "all"; }
				else { $blog['sharedTO'] = $this->input->post('txtmanufacturers'); }

				if($blogid>0){
					$this->model->allUpdate('blogid',$blogid,'user_blog',$blog);
					$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
					redirect('staff/allblogs');
				}
				else{

					$this->model->allInserts('user_blog',$blog);
										
					/*$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
					$ulist=$this->model->allSelects($sql);
					$this->emailSend('IMPORTANT New BLOG added by Pro-Staff Member',$ulist);*/

					$this->session->set_flashdata('sucess', 'BLOG Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/allblogs');				
				}
			}
			else{

					if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($blogid>0){
						
							$sql	=	"select * from user_blog where blogid=$blogid";
							$TempInfo	=	$this->model->allSelects($sql);
							$blogInfo	=	$TempInfo[0];
											
							$data['txtblogname']	 =	$blogInfo['blogname'];
							$data['txtbloglink']	 =	$blogInfo['link'];
							$data['txtcoveragetype'] =	$blogInfo['coveragetype'];
							$data['txtrefproducts']	 =	$blogInfo['refproducts'];
							$data['txtsharedTO']	 =	$blogInfo['sharedTO'];
						
						}		
						else{
						
							$data['txtblogname']		=	'';
							$data['txtbloglink']		=	'';
							$data['txtcoveragetype']	=	'';
							$data['txtrefproducts']		=	'';
							$data['txtsharedTO']	 	= 	'all';
							
						}	
					
			}	
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);				
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
						LEFT JOIN company_ref cr ON cr.companyID=u.userid
						where cr.userid=".$this->session->userdata("userid").")
						UNION
						(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid").")";	
			   
				$ulist=$this->model->allSelects($sql);
				
				$data['ulist']=$ulist;
				$data['blogid']=$blogid;
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-blog',$data,true);
				$this->load->view('layout-inner',$data);
	}

# Manage Blogs
 public function allblogs(){
						
		$sql="Select * from 
			  (Select ub.blogid AS blogid,ub.userid as userid, ub.blogname as blogname, 'NA' as headline,
			  	ub.link, ub.posted_date as posted_date, ub.sharedTO as companyID, 0 AS visitors, 'bb' as WB 
			  from user_blog ub
			  LEFT JOIN users u ON u.userid=ub.userid
			  LEFT JOIN company_ref cr ON cr.userid=u.companyID
			  where ub.userid=".$this->session->userdata("userid")."
				UNION
			  Select uc.id as blogid,uc.user_id as userid, uc.source as blogname, uc.headline as headline, 
			  uc.link, uc.created_date as posted_date, uc.CS_sponsorID as companyID, uc.visitors as visitors, 
			  'CS' as WB
			  from user_customscoops uc
			  where find_in_set('".$this->session->userdata("userid")."',uc.user_id)) a";

			$search_by = "orderbydesc";
        	$search_value = "somevalue";

				if (@$_POST){
					$search_by = $_POST['formelement'];
					$search_value = $_POST['formvalue'];
				 } 

				if($search_by == 'bySponsor' && strlen($search_value)>0 && $search_value <> 'all'){
					$sql	.=	" where find_in_set('".$search_value."',companyID)";	
				}
		
				if($search_by	==	'orderbydesc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date DESC";		
				}
		
				if($search_by	==	'orderbyasc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date ASC";		
				}
			//echo $sql; die;
			$data["bloglist"]=$this->model->allSelects($sql);

			$sql="(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname  
				   from users u
				   LEFT JOIN company_ref cr ON cr.companyID=u.userid
				   where cr.userid=".$this->session->userdata("userid").")
					 UNION
				  (Select DISTINCT sponsorCSid AS companyID, ups.sponsorname as FirstLastname
				   from user_prostaff_sponsors ups
				   where ups.prostaff_id=".$this->session->userdata("userid").")";
	
			$ulist=$this->model->allSelects($sql);
			$data['sponsorList']=$ulist;
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
			$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
				
			$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
			$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
			$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
			$data["fblink"]=$this->model->allSelects($sql);
		
			$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
			$data["twlink"]=$this->model->allSelects($sql);
				
			$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
			$data["instalink"]=$this->model->allSelects($sql);
		
			$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
			$data["youtubelink"]=$this->model->allSelects($sql);
										
			$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manageblogs',$data,true);
			$this->load->view('layout-inner',$data);
			
	}

#Manage Blog Status
 public function blogstatus(){
 
		if(isset($_POST["blogid"],$_POST["status"])){
        
			if($_POST["status"]=="Delete"){
				$qstr="DELETE FROM user_blog WHERE blogid=".$_POST["blogid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
				echo "<strong>Deleted Sucessfully..!!</strong>";
			}
			else if($_POST["status"]=="DeleteCS"){		
				$qstr="DELETE FROM user_customscoops WHERE id=".$_POST["blogid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
				echo "<strong>Deleted Sucessfully..!!</strong>";
			}
			else{			
				$qstr="update user_blog set status='".$_POST["status"]."' where blogid=".$_POST["blogid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
				echo "<strong>Status Updated Sucessfully..!!</strong>";
			}      
    } 
 }
	
#BULK Blog Status
 public function managebulkblogs(){
 
        if(isset($_POST["id"],$_POST['action'])){
		
            $id=$_POST["id"];
            $action=$_POST['action'];	
			$this->model->updateBlogstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
        }        
    }

#Filter Blogs
 public function filterblogs(){
 
 	if(isset($_POST["filterBystatus"])){
	  
	  		if($_POST["filterBystatus"]!="Filter By Status"){
			
				$sql="Select * from user_blog where userid=".$this->session->userdata("userid")." and status='".$_POST["filterBystatus"]."'";
			
				$data["blogslist"]=$this->model->allSelects($sql);
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/manageblogs',$data,true);
				$this->load->view('layout-inner',$data);
		
			}
		   else
			{
			
			$sql="Select * from user_blog where userid=".$this->session->userdata("userid");
			
			$data["blogslist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manageblogs',$data,true);
			$this->load->view('layout-inner',$data);
						
			}
	  	}
 }

# Manage Messaged INBOX
 public function inbox(){
 
			$sql="Select mt.transid,mt.msgid, mm.fromid,uu.firstlastname as 'From',
				  mm.msgsub,mm.msgdescp,mm.posted_date,mt.status,mm.msg_attachment FROM Msg_Trans mt
				  LEFT JOIN Msg_Master mm ON mt.msgid=mm.msgid
				  LEFT JOIN users u ON mt.toid=u.userid
				  LEFT JOIN users uu ON mm.fromid=uu.userid
				  WHERE mt.toid=".$this->session->userdata("userid");
					
			$sql = $sql." AND mt.status!='Delete' ORDER BY mt.status desc";		  
			
			$data["msglist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manage-Inbox',$data,true);
			$this->load->view('layout-inner',$data);
	}

#Sent Items
 public function sent(){
 
	       $sql="SELECT mm.msgsub,mm.msgdescp,mm.posted_date,mt.status,mm.msg_attachment ,
				 GROUP_CONCAT(users.Firstlastname SEPARATOR ',')  as 'To'
				 FROM 
				 Msg_Master mm, Msg_Trans mt
				 LEFT JOIN users ON users.userid=mt.toid 
				 WHERE mm.fromid=".$this->session->userdata("userid")." AND mm.msg_status!='Delete' AND mt.msgid=mm.msgid GROUP BY mm.msgid";

			$data["msglist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manage-Outbox',$data,true);
			$this->load->view('layout-inner',$data);			
 }

#ADD New Message
 public function addNewMessage($toID){
	
			//$sql="Select * from users u, company_ref cr where u.userid=cr.userid and cr.companyID=".$this->session->userdata("userid");	
			
			$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");			
			$data["complist"]=$this->model->allSelects($sql);
			$data["toID"] = $toID;
			
			$this->form_validation->set_rules('txtmsgto', 'Select Members', 'required');			
			$this->form_validation->set_rules('txtmsgsub', 'Subject Title is Required', 'required');
			$this->form_validation->set_rules('txtmsgdescp', 'Message Description required', 'required');
			$this->form_validation->set_rules('msg_attachment', 'Document', 'callback_validate_msg_attachment');	
			
			if ($this->form_validation->run() == FALSE)
			{	
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
										'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
										'redirect_uri' => site_url() . '/staff/facebook_redirect/'));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(GetFormError()));
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-new-message',$data,true);
				$this->load->view('layout-inner',$data);

			}
			else{
					$message=array(
                        'fromid'=>$this->session->userdata("userid"),
						'msgsub'=>$this->input->post('txtmsgsub'),
						'msgdescp'=>$this->input->post('txtmsgdescp'),                       	
 	                   );
				$msg_attachment	=	'';
        		if($_FILES['msg_attachment']['error'] == 0){
					$fileName = $_FILES['msg_attachment']['name'];
					$file='msg_attachment';		
					$upload = uploadFile($file,'*','assets/upload/msg_attachment',$fileName);
					if($upload['success']){
						$msg_attachment	=	$upload['path'];
					}									
				}
				
					$message['msg_attachment']	=	$msg_attachment;					
					$msgID=$this->model->allInsertsRetID('Msg_Master',$message);
					
					if($this->input->post('txtmsgto')==105){
					
						$sql="SELECT GROUP_CONCAT(companyID) as 'list' FROM company_ref where userid=".$this->session->userdata("userid");
						$groupList=$this->model->allSelects($sql);
					
						$toIDS=explode(",",$groupList[0]['list']);
					}
					else
					{	
					$toIDS=explode(",",$this->input->post('txtmsgto'));
					}
					
					for($i=0;$i<count($toIDS);$i++){
					
						$Tmsg=array(
							'msgid'=>$msgID,
							'toid'=>$toIDS[$i],
							'status'=>"Unread"
													
						   );
						$this->model->allInserts('Msg_Trans',$Tmsg);
					}
									
					//$this->model->allInserts('Msg_Master',$message);
					$this->session->set_flashdata('sucess', 'Message Sent Successfully..!!');
					redirect('staff/inbox');	
				}	
}

#Validate image extension	
function validate_msg_attachment(){
 		
		if($_FILES['msg_attachment']['error'] == 0){
			$fileEle	=	$_FILES['msg_attachment'];
			$extension	=	strtolower(end(explode('.',$fileEle['name'])));
			$validExtension	=	GetMsgAttachmentext();
					  				
			if(in_array($extension,$validExtension)){
				return true;
			}
			else{
				$this->form_validation->set_message('validate_msg_attachment', 'Please upload valid attachment file');
				return false;
			}
		}
		else{
			return true;
		}
 }
#Manage Message Status
 public function msgstatus(){
 
		if(isset($_POST["transid"],$_POST["status"])){
        
			$qstr="update Msg_Trans set status='".$_POST["status"]."' where transid=".$_POST["transid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
			         
        } 
	}

 public function msgstatusOutbox() {
        if (isset($_POST["transid"], $_POST["status"])) {

            $qstr = "update Msg_Master set msg_status='" . $_POST["status"] . "' where msgid=" . $_POST["transid"];
            $this->model->allQueries($qstr);
            $this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');
            echo "<strong>Status Updated Sucessfully..!!</strong>";
        }
    }

#BULK Messages Status
 public function managebulkmsgs()
   {
        if(isset($_POST["id"],$_POST['action']))
        {
            $id=$_POST["id"];
            $action=$_POST['action'];
           	
			$this->model->updatemsgstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
            
        }        
    }

# ADD New Message
 public function replyMessage($toid){
	
			//$sql="Select * from users u, company_ref cr where u.userid=cr.userid and cr.companyID=".$this->session->userdata("userid");	
			
			$sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");			
			$data["complist"]=$this->model->allSelects($sql);


			$sql = "Select * from Msg_Master where msgid=$toid";
			$tempInfo = $this->model->allSelects($sql);
			$data["replyInfo"] = $tempInfo[0];

			$this->form_validation->set_rules('txtmsgsub', 'Subject Title is Required', 'required');
			$this->form_validation->set_rules('txtmsgdescp', 'Message Description required', 'required');
			$this->form_validation->set_rules('msg_attachment', 'Document', 'callback_validate_msg_attachment');	
				
			if ($this->form_validation->run() == FALSE)
			{	
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(GetFormError()));
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/reply-message',$data,true);
				$this->load->view('layout-inner',$data);

			}
			else
			{
			
				$message=array(
                   'fromid'=>$this->session->userdata("userid"),
									 'msgsub'=>$this->input->post('txtmsgsub'),
									 'msgdescp'=>$this->input->post('txtmsgdescp')                       							
 	                   );
				   
				$msg_attachment	=	'';
        		if($_FILES['msg_attachment']['error'] == 0){
					$fileName = $_FILES['msg_attachment']['name'];
					$file='msg_attachment';		
					$upload = uploadFile($file,'*','assets/upload/msg_attachment',$fileName);
					if($upload['success']){
						$msg_attachment	=	$upload['path'];
					}									
				}
				$message['msg_attachment']	=	$msg_attachment;					   
        		
				$msgID=$this->model->allInsertsRetID('Msg_Master',$message);
								
				$Tmsg=array(
                        'msgid'=>$msgID,
						'toid'=>$toid,
						'status'=>"Unread"
                       							
 	                   );
					$this->model->allInserts('Msg_Trans',$Tmsg);
								
				//$this->model->allInserts('Msg_Master',$message);
				$this->session->set_flashdata('sucess', 'Message Sent Successfully..!!');
				redirect('staff/inbox');	
		
			}	
	}

#View Message
 public function viewMessage($msgid){
						
		$msgid = $_POST['msgid'];
		
        $qstr = "update Msg_Trans set status='Read' where msgid = $msgid and toid =". $this->session->userdata("userid");
        $this->model->allQueries($qstr);
		
		$sql = "Select * from Msg_Master mm 
				  LEFT JOIN users u ON mm.fromid=u.userid
				  where mm.msgid=" . $msgid;
				  
        $data["msglist"] = $this->model->allSelects($sql);
		
        if (count($data["msglist"]) == 0) {
            redirect("staff/inbox");
        }
		$this->load->view('staff/reply-view',$data);
			
 }

 public function viewMessageSent($msgid) {

		$msgid = $_POST['msgid'];
			
		$sql = "Select * from Msg_Master mm 
				LEFT JOIN users u ON mm.fromid=u.userid
				where mm.msgid=".$msgid;
				  
        $data["msglist"] = $this->model->allSelects($sql);
		
        if (count($data["msglist"]) == 0) {
            redirect("staff/inbox");
        }
		$this->load->view('staff/message-view-sent',$data);

    }

#Manage AllEmployers
 public function allEmployers(){
						  
 		$sql="(Select u.userid AS id, u.FirstLastname as empname ,u.user_logo as emplogo,'0' AS transRef,'ATEmp' AS empType 
			   from users u
			   LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   where cr.userid=".$this->session->userdata("userid")." AND cr.ref_status='Active')
			 	 UNION
			  (Select ups.ps_id AS id, ups.sponsorname as empname, ups.sponsor_logo AS emplogo,trans_ref as transRef,'nonATEmp' AS empType
			   from user_prostaff_sponsors ups
			   where ups.prostaff_id=".$this->session->userdata("userid").")";
			  
		$data["emplist"]=$this->model->allSelects($sql);
			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
   		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
				
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
				
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/manageemployers',$data,true);
		$this->load->view('layout-inner',$data);
			
	}
	
#Manage Sponsor Keywords
 public function sponsorkeywords(){
			
		//$sql="Select * from users where companyID=".$this->session->userdata("userid");
		/*$sql="Select u.userid,u.FirstLastname,u.user_logo,u.fbkeywords,u.twtkeywords,u.instakeywords from users u, 
			  company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");*/	
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
   		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
    	$data['modalKeywordsForm'] = $this->load->view('staff/sponsors-keywords-addedit', $data, true);
		$data['content']=$this->load->view('staff/managesponsorkeywords',$data,true);
		$this->load->view('layout-inner',$data);
			
	}

 public function sponsorsList(){
 
 		$sql="(Select cr.refid AS id, u.FirstLastname as empname ,u.user_logo as emplogo,IFNULL(cr.fbkeywords,u.fbkeywords) AS fbkeywords,
			   IFNULL(cr.twitterkeywords,u.twtkeywords) AS twtkeywords,IFNULL(cr.instakeywords,u.instakeywords) AS instakeywords,
			   IFNULL(cr.youtubekeywords,u.youtubekeywords) AS youtubekeywords,
			   'STEmp' AS empType 
			   from users u
			   LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   where cr.userid=".$this->session->userdata("userid").")
			 	 UNION
			  (Select ups.ps_id AS id, ups.sponsorname as empname, ups.sponsor_logo AS emplogo, ups.fbkeywords AS fbkeywords,
			   ups.twitterkeywords AS twtkeywords, ups.instakeywords AS instakeywords,
			   ups.youtubekeywords AS youtubekeywords,'nonSTEmp' AS empType
			   from user_prostaff_sponsors ups
			   where ups.prostaff_id=".$this->session->userdata("userid").")";
		
		$data["emplist"]=$this->model->allSelects($sql);	
		$this->load->view('staff/sponsors-list', $data);
 
 }

 public function getSponsorKeywords() {

    $common_id = $this->input->post('common_id');
		$emp_type  = $this->input->post('emp_type');
		
		if($emp_type=='STEmp')
		{
        $sql = "select cr.refid AS id, IFNULL(cr.fbkeywords, u.fbkeywords) AS fbkeywords,IFNULL(cr.twitterkeywords,u.twtkeywords) AS twitterkeywords,
				IFNULL(cr.instakeywords,u.instakeywords) AS instakeywords,IFNULL(cr.youtubekeywords,u.youtubekeywords) AS youtubekeywords from company_ref cr
				LEFT JOIN users u ON u.userid=cr.companyID
				where cr.refid=$common_id";	
        }
		
		if($emp_type=='nonSTEmp')
		{
		$sql = "select ps_id AS id, fbkeywords,twitterkeywords,instakeywords,youtubekeywords from user_prostaff_sponsors where ps_id=$common_id";
		}
		
		$TempInfo = $this->model->allSelects($sql);
    $Info = $TempInfo[0];
		$data['info']= $Info;
		$data['emp_type'] = $emp_type;
		
		$this->load->view('staff/sponsors-keywords-form',$data);
		//echo json_encode($Info);
    }

  function addSponsorKeywords() {
	
        $field = array('fbkeywords','twitterkeywords','instakeywords','youtubekeywords');
        $saveData = array();
	
        foreach ($field as $key => $val) {
            $saveData[$val] = $this->input->post($val);
        }

			$emptype = $this->input->post('emptype');
			$id	= $this->input->post('trans_id');
			
			if ($emptype == "STEmp") {
				$this->model->allUpdate('refid', $id, 'company_ref', $saveData);			
				$notification_msg = "Updated Sucessfully.";
			}
			
			if ($emptype == "nonSTEmp") {
				$this->model->allUpdate('ps_id', $id, 'user_prostaff_sponsors', $saveData);			
				$notification_msg = "Updated Sucessfully.";	
			}

		# Keywords Setup
		
		$sql="(Select cr.refid AS id, u.FirstLastname as empname ,u.user_logo as emplogo,IFNULL(cr.fbkeywords,u.fbkeywords) AS fbkeywords,
			   IFNULL(cr.twitterkeywords,u.twtkeywords) AS twtkeywords,IFNULL(cr.instakeywords,u.instakeywords) AS instakeywords,
			   IFNULL(cr.youtubekeywords,u.youtubekeywords) AS youtubekeywords, 'STEmp' AS empType from users u
			   LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   where cr.userid=".$this->session->userdata("userid").")
			 	 UNION
			  (Select ups.ps_id AS id, ups.sponsorname as empname, ups.sponsor_logo AS emplogo, ups.fbkeywords AS fbkeywords,
			   ups.twitterkeywords AS twtkeywords, ups.instakeywords AS instakeywords, ups.youtubekeywords AS youtubekeywords,'nonSTEmp' AS empType
			   from user_prostaff_sponsors ups
			   where ups.prostaff_id=".$this->session->userdata("userid").")";
		
		$emplist = $this->model->allSelects($sql);

		$fbkeywords=""; $twitterkeywords=""; $instakeywords="";
		
			if(count($emplist)>0)
			  {
				 foreach($emplist as $emp)			 
					{
						
						if($fbkeywords!="")	{
							$fbkeywords= $fbkeywords.",".$emp['fbkeywords']; 
						}
						else {
						$fbkeywords= $emp['fbkeywords']; 
						}
						
						if($twitterkeywords!=""){
						$twitterkeywords= $twitterkeywords.",".$emp['twtkeywords']; 
						}
						else {
						$twitterkeywords=$emp['twtkeywords'];
						}
						
						if($instakeywords!=""){
						$instakeywords=$instakeywords.",".$emp['instakeywords'];
						} else{
						$instakeywords=$emp['instakeywords'];
						}
						
						if($youtubekeywords!=""){
						$youtubekeywords=$youtubekeywords.",".$emp['youtubekeywords'];
						} else{
						$youtubekeywords=$emp['youtubekeywords'];
						}

					}
				}

		$keywordArrfb = explode(',', $fbkeywords);
		$keywordArrTwt = explode(',', $twitterkeywords);
		$keywordArrInsta = explode(',', $instakeywords);
		$keywordArrYouTube = explode(',', $youtubekeywords);

		if(count($keywordArrfb)>300 || count($keywordArrTwt)>300 || count($keywordArrInsta)>300 || count($keywordArrYouTube)>300){
			$returnArr['error'] = true;
			$returnArr['msg'] = "Keywords Exceeds Limit of 100.";
		}
		else{

			$qstr="update users set fbkeywords='".$fbkeywords."', twtkeywords='".$twitterkeywords."', instakeywords='".$instakeywords."',
			       youtubekeywords='".$youtubekeywords."' where userid=".$this->session->userdata("userid");
			$this->model->allQueries($qstr); 
			
			/*$updatearr	=	array(
						'fbkeywords'	=>	$fbkeywords,
						'twtkeywords'	=>	$twitterkeywords,
						'instakeywords'	=>	$instakeywords
				);
			$this->session->set_userdata($updatearr);*/

			$returnArr['error'] = false;
			$returnArr['msg'] = "Keywords Setup finished Sucessfully.";
		}

        echo json_encode($returnArr);
  }

  function setKeywords() {
	
 		$sql="(Select cr.refid AS id, u.FirstLastname as empname ,u.user_logo as emplogo,IFNULL(cr.fbkeywords,u.fbkeywords) AS fbkeywords,
			   IFNULL(cr.twitterkeywords,u.twtkeywords) AS twtkeywords,IFNULL(cr.instakeywords,u.instakeywords) AS instakeywords,
			   IFNULL(cr.youtubekeywords,u.youtubekeywords) AS youtubekeywords,'STEmp' AS empType from users u
			   LEFT JOIN company_ref cr ON cr.companyID=u.userid
			   where cr.userid=".$this->session->userdata("userid").")
			 	 UNION
			  (Select ups.ps_id AS id, ups.sponsorname as empname, ups.sponsor_logo AS emplogo, ups.fbkeywords AS fbkeywords,
			   ups.twitterkeywords AS twtkeywords, ups.instakeywords AS instakeywords, ups.youtubekeywords AS youtubekeywords,
			   'nonSTEmp' AS empType
			   from user_prostaff_sponsors ups
			   where ups.prostaff_id=".$this->session->userdata("userid").")";
		
		$emplist = $this->model->allSelects($sql);

		$fbkeywords=""; $twitterkeywords=""; $instakeywords="";
		
			if(count($emplist)>0){
			
				 foreach($emplist as $emp){
						
						if($fbkeywords!=""){
							$fbkeywords= $fbkeywords.",".$emp['fbkeywords']; 
						}
						else{
						$fbkeywords= $emp['fbkeywords']; 
						}
						
						if($twitterkeywords!=""){
						$twitterkeywords= $twitterkeywords.",".$emp['twtkeywords']; 
						}
						else {
						$twitterkeywords=$emp['twtkeywords'];
						}
						
						if($instakeywords!=""){
						$instakeywords=$instakeywords.",".$emp['instakeywords'];
						} else{
						$instakeywords=$emp['instakeywords'];
						}

						if($youtubekeywords!=""){
						$youtubekeywords=$youtubekeywords.",".$emp['youtubekeywords'];
						} else{
						$instakeywords=$emp['youtubekeywords'];
						}

					}
				}

		$keywordArrfb = explode(',', $fbkeywords);
		$keywordArrTwt = explode(',', $twitterkeywords);
		$keywordArrInsta = explode(',', $instakeywords);
		$keywordArryoutube = explode(',', $youtubekeywords);

		if(count($keywordArrfb)>100 || count($keywordArrTwt)>100 || count($keywordArrInsta)>100 || count($keywordArryoutube)>100){
			$returnArr['error'] = true;
			$returnArr['msg'] = "Keywords Exceeds Limit of 100.";
		}
		else{

			$qstr="update users set fbkeywords='".$fbkeywords."', twtkeywords='".$twitterkeywords."', instakeywords='".$instakeywords."',
			       youtubekeywords='".$youtubekeywords." where userid=".$this->session->userdata("userid");
			$this->model->allQueries($qstr); 
			/*$updatearr	=	array(
						'fbkeywords'	=>	$fbkeywords,
						'twtkeywords'	=>	$twitterkeywords,
						'instakeywords'	=>	$instakeywords
				);
			$this->session->set_userdata($updatearr);*/

		$returnArr['error'] = false;
		$returnArr['msg'] = "Keywords Setup finished Sucessfully.";
		}
        echo json_encode($returnArr);
  }
	
#Training Center Info
 public function viewtraining($sponsorid=0){
		
		if($sponsorid==0){
			redirect('staff/allEmployers');
		}
		
		$sql = "Select *, u.FirstLastname, u.user_logo from trainigcenter tc
				LEFT JOIN users u ON u.userid = tc.center_owner
				where tc.center_status='Active' and tc.center_owner=$sponsorid";
		$data["TrainingInfo"]=$this->model->allSelects($sql);
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
   		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
								
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);			
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/view-sponsor-trainings',$data,true);
		$this->load->view('layout-inner',$data);			
 }		

 public function trainigcenter(){

		$sql="SELECT  DISTINCT u.FirstLastname,u.user_logo,u.street,u.city,u.state,u.phone,
			  	u.email,u.user_document,cr.companyID from trainigcenter tc 
			  	LEFT JOIN users u ON u.userid = tc.center_owner
			  	LEFT JOIN company_ref cr ON cr.companyID = u.userid
			  	WHERE cr.userid=".$this->session->userdata("userid");
			  
		$data["emplist"]=$this->model->allSelects($sql);
			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
   	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
									));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/managetraining',$data,true);
		$this->load->view('layout-inner',$data);	
 }		

#download file
 function download($tablename='',$tablecol='',$download_fi='',$filed_id=0){		
		download_file($tablename,$tablecol,$download_fi,$filed_id);
	}
	
#Manage Employer Refernce Status
 public function refstatus(){
 
		if(isset($_POST["refid"],$_POST["status"])){
			$qstr="update company_ref set ref_status='".$_POST["status"]."' where refid=".$_POST["refid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";         
        } 
 }

#BULK Messages Status
 public function managebulkrefstatus(){
 
        if(isset($_POST["id"],$_POST['action'])){
            $id=$_POST["id"];
            $action=$_POST['action'];
           	
			$this->model->updateCompRefstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
            
        }        
    }

# Settings
 public function setting(){
	
		$this->form_validation->set_rules('txtnewpwd', 'New Password is Required', 'required|matches[txtcnfpwd]');
		$this->form_validation->set_rules('txtcnfpwd', 'Confirm Password', 'required');
				
			if ($this->form_validation->run() == FALSE){
				
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/settings',$data,true);
				$this->load->view('layout-inner',$data);

			}
			else{
					
					$qstr="update users set password='".md5($this->input->post('txtcnfpwd'))."' where userid=".$this->session->userdata("userid");
					$this->model->allQueries($qstr); 

					$insertHistory = array(
						'userid' => $this->session->userdata("userid"),
						'email' =>  '',
						'fromPage' => 'setting else staff.php',	
						'password' => $this->input->post('txtcnfpwd'),
						'date_update' => date('Y-m-d H:i:s')						
					);
					$this->db->insert('user_pwd_track',$insertHistory);

					$this->session->set_flashdata('sucess', 'Password Reset Success..!!');
					redirect('staff/setting');			
			}
 }

#For Notifications
 public function emailSend($subject,$userlist){

		foreach($userlist as $ulist){
			
			$this->email->from('support@anglertrack.net', 'AnglerTrack');
		  	$this->email->to($ulist['email']);
			$this->email->subject('Notification: '.$subject);
					
			$emlmsg="Dear ".$ulist['FirstLastname'].
"\n\n ". $subject." \n 

Thank You
Team, AnglerTrack.net";
										
		  	$this->email->message($emlmsg);	  	
		  	$this->email->send();
		  }

	}

   public function emailSendOneByOne($subject,$body, $email, $recptName) {

    //   $config['mailtype'] = 'html';
	// 		$config['protocol'] = 'sendmail';
	// 		$config['mailpath'] = '/usr/sbin/sendmail';
	// 		$config['charset'] = 'utf-8';
	// 		$config['wordwrap'] = TRUE;
	// 		$this->email->initialize($config);
	$this->load->library('email');
	$this->email->initialize(array(
		'protocol' => 'smtp',
		'smtp_host' => 'smtp.sendgrid.net',
		'smtp_user' => 'apikey',
		'smtp_pass' => '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',
		'smtp_port' => 587,
		'crlf' => "\r\n",
		'newline' => "\r\n",
		'mailtype' => 'html',
		'wordwrap' => TRUE
		));
			
            $this->email->from('no-reply@anglertrack.net', 'AnglerTrack');
            $this->email->to($email);
						$this->email->bcc('support@anglertrack.net');
            $this->email->subject($subject);

            $emlmsg = "Dear " . $recptName ."\n\n " . $body . " \n";
            $this->email->message(nl2br($emlmsg));
            $this->email->send();
						$this->email->clear(TRUE);
        
    }

# For ReportByDate NEW Format
 function reportbydate(){
 	
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);

		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);

			$search_from_date	=	"";
			$search_to_date		=	"";
			$search_by_manufacturer="all";
			$search_by_pro			= "all";
			$search_select_staff=	$this->session->userdata("userid");

		if($search_from_date == '' && $search_to_date == ''){
			$search_from_date	=	date("Y-m-d",strtotime("-7 days"))." 00:00:00";
			$search_to_date		=   date("Y-m-d")." 23:59:59";
		}
		
		if($_POST){
				
			$search_from_date	=	date("Y-m-d",strtotime($_POST['search_from_date']));
			$search_to_date		=	date("Y-m-d",strtotime($_POST['search_to_date']));
			$search_by_manufacturer = $_POST['search_by_manufacturer'];
			
			if(isset($_POST['search_by_pro'])){
				$search_by_pro = $_POST['search_by_pro'];
			}
			
		if($search_by_manufacturer!='all'){
					
			$sql = "SELECT u.userid as uid,u.FirstLastname as uname,u.fbkeywords,u.twtkeywords as twtkeywords,
					u.instakeywords, u.youtubekeywords FROM users u 
					WHERE u.memtype='Employer' AND u.userid=".$search_by_manufacturer;
			$TempInfo =	$this->model->allSelects($sql);	
			
			if(count($TempInfo)>0){
				$keywords		 = 	$TempInfo[0];
				$manufactID		 =	$keywords['uid'];
				$fbkeywords		 =	$keywords['fbkeywords'];		
				$twtkeywords	 =	$keywords['twtkeywords'];		
				$instakeywords	 =	$keywords['instakeywords'];	
				$youtubekeywords =	$keywords['youtubekeywords'];		
			}else {
				
				$sql = "SELECT DISTINCT ups.sponsorCSid as uid, ups.sponsorname as uname, ups.prostaff_id,
						ups.fbkeywords,ups.twitterkeywords as twtkeywords,ups.instakeywords, ups.youtubekeywords 
						FROM user_prostaff_sponsors ups
						WHERE ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$keywords		 = 	$TempInfo[0];
					$manufactID		 =	$keywords['uid'];
					$fbkeywords		 =	$keywords['fbkeywords'];		
					$twtkeywords	 =	$keywords['twtkeywords'];		
					$instakeywords	 =	$keywords['instakeywords'];	
					$youtubekeywords =	$keywords['youtubekeywords'];		
				}
			}
		}
		else
		{
			$fbkeywords		 =	getKeywords($this->session->userdata("userid"),'fbkeywords');		
			$twtkeywords	 =	getKeywords($this->session->userdata("userid"),'twtkeywords');		
			$instakeywords	 =	getKeywords($this->session->userdata("userid"),'instakeywords');	
			$youtubekeywords =	getKeywords($this->session->userdata("userid"),'youtubekeywords');	
			$manufactID		 = 0;	
		}
		
		#Fetching out Staff information.
		$sql = "select * from users where memtype='Staff' and userid=".$search_select_staff;
		$tempUserInfo	=	$this->model->allSelects($sql);

		$StaffInfo		=	array();

		$dateAppend = ""; $dateAppend1 = ""; $dateAppend2 = ""; $dateAppend3 = ""; $staffIDCondt=""; $append_qry=""; 
		$fb_append_qry=""; $twt_append_qry=""; $insta_append_qry=""; $yt_append_qry="";
		$fb_feed_tot_indv = $fb_like_tot_indv = $fb_comm_tot_indv =0 ; $fb_feed_tot_hyb  = $fb_like_tot_hyb  = $fb_comm_tot_hyb =0 ;

		$twt_feed_tot_indv = $retweet_tot_indv = $fav_tot_indv =0 ;	$twt_feed_tot_hyb = $retweet_tot_hyb = $fav_tot_hyb =0 ;
		$insta_feed_tot_indv = $insta_like_tot_indv = $insta_comm_tot_indv =0 ; $insta_feed_tot_hyb = $insta_like_tot_hyb = $insta_comm_tot_hyb =0 ;
		$yt_feed_tot_indv = $yt_views_tot_indv = 0 ; $yt_feed_tot_hyb = $yt_views_tot_hyb = 0 ;


				if($search_from_date && $search_to_date){
					$dateAppend1 = " AND created_time>= '$search_from_date' AND created_time <= '$search_to_date' ";
				}

				if($search_from_date && $search_to_date){
					$dateAppend	= " AND created_at>= '".$search_from_date."' AND created_at <= '".$search_to_date."' ";
				}

				if($search_from_date && $search_to_date){
					$dateAppend2 = " AND created_time>= '".strtotime($search_from_date)."' AND created_time <= '".strtotime($search_to_date)."' ";
				}

				if($search_from_date && $search_to_date){
					$dateAppend3	= " AND created_date>= '".$search_from_date."' AND created_date <= '".$search_to_date."' ";
				}
				
				if($search_by_pro!='all'){
					$staffIDCondt = " AND u.userid=$search_by_pro";
				}

				#Facebook Keywords								
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' OR ',$keyword_query);
					$fb_append_qry = " AND ($append_qry)";
				}

				#Twitter Keywords				
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text LIKE '".trim($val)."%' OR text LIKE '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$twt_append_qry = " AND ($append_qry)";
				}

				#Instagram Keywords
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text LIKE '".trim($val)."%' or text LIKE '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$insta_append_qry = " AND ($append_qry)";
				}
					
				#YouTube Keywords
				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text LIKE '".trim($val)."%' or text LIKE '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$yt_append_qry = " AND ($append_qry)";
				}

		if($tempUserInfo){
		
			foreach($tempUserInfo as $key=>$val){
			
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				
				#Tournaments Creation Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);	
				$tempretArr["tournaments_creation_total"]	=	$tempInfo[0]['rec_total'];

				$sql="SELECT * FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);	
				$data['tournaments'] = $tempInfo;
				
				#Tournaments 1st place Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE tfinishedplace=1 and staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);			
				$tempretArr["tournaments_fist_place"]=$tempInfo[0]['rec_total'];	
				
        #For Blog Creation
				$totalBlogCount = 0;
				
        $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_blog WHERE user_blog.status='Active' and userid=$user_id";
        
				   if($search_from_date && $search_to_date) {
                 $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
           }
					 if($search_by_manufacturer!="all"){
								$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
					 }

         $tempInfo = $this->model->allSelects($sql);
				 $totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total'];

         //$sql = "SELECT IFNULL(count(*),0) as rec_total_cs FROM user_customscoop WHERE user_id=$user_id";
         $sql = "SELECT IFNULL(count(*),0) as rec_total_cs FROM user_customscoops WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";

					if($search_from_date && $search_to_date) {
							$sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
					}
					
					if($search_by_manufacturer!="all"){
						//$sql	.=	" and companyID=$search_by_manufacturer";
							$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
					}

        $tempInfo = $this->model->allSelects($sql);
				$totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total_cs'];
				
        $tempretArr["blog_total_count"] = $totalBlogCount;

        	$sql = "SELECT blogname as headline,coveragetype as source,link,'0' as visitors,posted_date as created_date 
					FROM user_blog 
					WHERE status='Active' and userid=$user_id";
           if ($search_from_date && $search_to_date) {
               $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
           }
					 
					 if($search_by_manufacturer!="all"){
							$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
						}

        $tempInfo = $this->model->allSelects($sql);
				$webPostOne = $tempInfo;

        $sql = "SELECT headline,source,link,visitors,created_date FROM user_customscoops 
				WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";
				
				if($search_from_date && $search_to_date) {
            $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
        }
				
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}
        $tempInfo = $this->model->allSelects($sql);
				$webPostCS = $tempInfo;
				$data['webblogs'] =  array_merge($webPostOne,$webPostCS);
				
				# For Impressions Count BLOG
				$sql="select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE find_in_set('".$user_id."', user_id)";
        
				if($search_from_date && $search_to_date) {
           $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
        }
				
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["blog_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Leads Creation
				$count_indv = 0; $count_hybrid = 0;
				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);					
				$count_indv =$tempInfo[0]['rec_total'];

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_lead ul
							LEFT JOIN users u ON u.userid= ul.userid
						  	LEFT JOIN company_ref cr ON cr.userid= ul.userid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',ul.sharedTO) or find_in_set('".$user_id."',ul.sharedTO))";
					if($search_from_date && $search_to_date) {
						$sql .=	" AND ul.posted_date >= '$search_from_date' AND ul.posted_date<='$search_to_date'";
					}
					$tempInfo = $this->model->allSelects($sql);
					$count_hybrid = $tempInfo[0]['rec_total'];
				}

				if($search_by_pro!='all' && $search_by_pro != $this->session->userdata("userid")){				
					$tempretArr["leads_total_count"]= $count_hybrid;
				}else{
					$tempretArr["leads_total_count"]= $count_indv + $count_hybrid;
				}		

				$sql="SELECT * FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql = "SELECT ul.* FROM user_lead ul
							LEFT JOIN users u ON u.userid= ul.userid
						  	LEFT JOIN company_ref cr ON cr.userid= ul.userid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',ul.sharedTO) or find_in_set('".$user_id."',ul.sharedTO))";
					if($search_from_date && $search_to_date) {
						$sql	.=	" AND ul.posted_date >= '$search_from_date' AND ul.posted_date<='$search_to_date'";
					}

				} else{

					$sql .= " 
							UNION
							
							SELECT ul.* FROM user_lead ul
							LEFT JOIN users u ON u.userid= ul.userid
						  	LEFT JOIN company_ref cr ON cr.userid= ul.userid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',ul.sharedTO) or find_in_set('".$user_id."',ul.sharedTO))";
					if($search_from_date && $search_to_date) {
						$sql	.=	" AND ul.posted_date >= '$search_from_date' AND ul.posted_date<='$search_to_date'";
					}
				}

				$tempInfo	=	$this->model->allSelects($sql);	
				$data['leads']	=	$tempInfo;	
				
        #For Print Creation
				$totalPrintCount = 0;
				
          $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_print WHERE status='Active' and userid=$user_id";
        
			if ($search_from_date && $search_to_date) {
              $sql .= " and print_issue_date >= '$search_from_date' and print_issue_date<='$search_to_date'";
           	}
 					
			if($search_by_manufacturer!="all"){
				$sql .=	" and find_in_set('".$manufactID."',sharedTO)";
			}

          $tempInfo = $this->model->allSelects($sql);
					$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total'];

                $sql = "SELECT * FROM user_print WHERE status='Active' and userid=$user_id";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
                }
 				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$data['prints'] = $tempInfo;

				$sql = "SELECT IFNULL(count(*),0) as rec_total_pcs FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."', CS_sponsorID)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total_pcs'];
							
                $tempretArr["print_total_count"] = $totalPrintCount;

				# For Impressions Count PRINT
				$sql = "select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql .=	" and find_in_set('".$manufactID."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["print_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Events Creation
				$count_indv = 0; $count_hybrid = 0;
				 
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and event_start_date >= '$search_from_date' and event_start_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$count_indv = $tempInfo[0]['rec_total'];
				
				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql = "SELECT IFNULL(count(*),0) as rec_total FROM events e
							LEFT JOIN users u ON u.userid= e.ownerid
						  	LEFT JOIN company_ref cr ON cr.userid= e.ownerid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',e.sharedTO) or find_in_set('".$user_id."',e.sharedTO))";
					
						if($search_from_date && $search_to_date) {
							$sql .= " AND e.event_start_date >= '$search_from_date' AND e.event_start_date<='$search_to_date'";
						}
					$tempInfo = $this->model->allSelects($sql);
					$count_hybrid = $tempInfo[0]['rec_total'];
				}

				if($search_by_pro!='all'){				
					$tempretArr["event_total_count"]= $count_hybrid;
				}else{
					$tempretArr["event_total_count"]= $count_indv + $count_hybrid;
				}		

				$sql="SELECT * FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and event_start_date >= '$search_from_date' and event_start_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql = "SELECT e.* FROM events e
							LEFT JOIN users u ON u.userid= e.ownerid
						  	LEFT JOIN company_ref cr ON cr.userid= e.ownerid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',e.sharedTO) or find_in_set('".$user_id."',e.sharedTO))";
					if($search_from_date && $search_to_date) {
						$sql .= " AND e.event_start_date >= '$search_from_date' AND e.event_start_date<='$search_to_date'";
					}

				} else{

					$sql .= " 
							UNION
							
							SELECT e.* FROM events e
							LEFT JOIN users u ON u.userid= e.ownerid
						  	LEFT JOIN company_ref cr ON cr.userid= e.ownerid
							WHERE cr.companyID =$user_id $staffIDCondt 
							AND (find_in_set('all',e.sharedTO) or find_in_set('".$user_id."',e.sharedTO))";
					if($search_from_date && $search_to_date) {
						$sql .= " AND e.event_start_date >= '$search_from_date' AND e.event_start_date<='$search_to_date'";
					}
				}
				
				$tempInfo	=	$this->model->allSelects($sql);		
				$data['events'] = $tempInfo;		

				#Twitter Related Content
				#-------------------------Twitter Feed Count			

				$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(retweet_count),0) as rec_retweet_total,
					  	IFNULL(sum(favorite_count),0) as rec_fav_total 
					  	FROM user_tweets 
					  	WHERE user_id=$user_id $twt_append_qry $dateAppend";		
				$tempInfo	=	$this->model->allSelects($sql);				
				
				$twt_feed_tot_indv = $tempInfo[0]['rec_total']; $retweet_tot_indv = $tempInfo[0]['rec_retweet_total']; $fav_tot_indv = $tempInfo[0]['rec_fav_total'];

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(ut.retweet_count),0) as rec_retweet_total,
						  	IFNULL(sum(ut.favorite_count),0) as rec_fav_total 
						  	FROM user_tweets ut
						  	LEFT JOIN users u ON u.userid= ut.user_id
						  	LEFT JOIN company_ref cr ON cr.userid= ut.user_id
						  	WHERE cr.companyID = $user_id $staffIDCondt $twt_append_qry $dateAppend";		
					$tempInfo = $this->model->allSelects($sql);
					$twt_feed_tot_hyb = $tempInfo[0]['rec_total']; $retweet_tot_hyb = $tempInfo[0]['rec_retweet_total']; $fav_tot_hyb = $tempInfo[0]['rec_fav_total'];
				}

				if($search_by_pro!='all' && $search_by_pro != $this->session->userdata("userid")){
					$tempretArr["twitter_feed_count"]	  = $twt_feed_tot_hyb;
					$tempretArr["twitter_retweet_count"]  = $retweet_tot_hyb;
					$tempretArr["twitter_favorite_count"] = $fav_tot_hyb;
				}
				else{
					$tempretArr["twitter_feed_count"]	  = $twt_feed_tot_indv + $twt_feed_tot_hyb;
					$tempretArr["twitter_retweet_count"]  = $retweet_tot_indv + $retweet_tot_hyb;
					$tempretArr["twitter_favorite_count"] = $fav_tot_indv + $fav_tot_hyb;				
				}
								
				#Facebook Related Content				
				#-------------------------Facebook Feed Count

				$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(likes_count),0) as rec_like_total,
					  	IFNULL(sum(comment_count),0) as rec_comm_total 
					  	FROM user_feed uf 
					  	WHERE user_id=$user_id $fb_append_qry $dateAppend1";		
				$tempInfo	   = $this->model->allSelects($sql);				
				$fb_feed_tot_indv = $tempInfo[0]['rec_total']; $fb_like_tot_indv = $tempInfo[0]['rec_like_total']; $fb_comm_tot_indv = $tempInfo[0]['rec_comm_total'];

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(uf.likes_count),0) as rec_like_total,
						  	IFNULL(sum(uf.comment_count),0) as rec_comm_total 
						  	FROM user_feed uf
						  	LEFT JOIN users u ON u.userid= uf.user_id
						  	LEFT JOIN company_ref cr ON cr.userid= uf.user_id
						  	WHERE cr.companyID = $user_id $staffIDCondt $fb_append_qry $dateAppend1";		
					$tempInfo = $this->model->allSelects($sql);
					$fb_feed_tot_hyb = $tempInfo[0]['rec_total']; $fb_like_tot_hyb = $tempInfo[0]['rec_like_total']; $fb_comm_tot_hyb = $tempInfo[0]['rec_comm_total'];
				}

				if($search_by_pro!='all' && $search_by_pro != $this->session->userdata("userid")){
					$tempretArr["facebook_feed_count"]	  = $fb_feed_tot_hyb;
					$tempretArr["facebook_like_count"]	  = $fb_like_tot_hyb;
					$tempretArr["facebook_comment_count"] = $fb_comm_tot_hyb;
				}
				else{

					$tempretArr["facebook_feed_count"]	  = $fb_feed_tot_indv + $fb_feed_tot_hyb;
					$tempretArr["facebook_like_count"]	  = $fb_like_tot_indv + $fb_like_tot_hyb;
					$tempretArr["facebook_comment_count"] = $fb_comm_tot_indv + $fb_comm_tot_hyb;
				}
												
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(likes_count),0) as rec_likes_total,
					  	IFNULL(sum(comments_count),0) as rec_comments_total 
					  	FROM user_instagram_feed 
					  	WHERE user_id=$user_id $insta_append_qry $dateAppend2";		
				$tempInfo	=	$this->model->allSelects($sql);
				
				$insta_feed_tot_indv = $tempInfo[0]['rec_total']; $insta_like_tot_indv = $tempInfo[0]['rec_likes_total']; 
				$insta_comm_tot_indv = $tempInfo[0]['rec_comments_total'];

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(uif.likes_count),0) as rec_likes_total,
						 	 	IFNULL(sum(uif.comments_count),0) as rec_comments_total 
						  	FROM user_instagram_feed uif
						  	LEFT JOIN users u ON u.userid= uif.user_id
						  	LEFT JOIN company_ref cr ON cr.userid= uif.user_id
						  	WHERE cr.companyID = $user_id $staffIDCondt $insta_append_qry $dateAppend2";		
					$tempInfo = $this->model->allSelects($sql);
					
					$insta_feed_tot_hyb = $tempInfo[0]['rec_total']; $insta_like_tot_hyb = $tempInfo[0]['rec_likes_total']; 
					$insta_comm_tot_hyb = $tempInfo[0]['rec_comments_total'];
				}
				
				if($search_by_pro!='all' && $search_by_pro != $this->session->userdata("userid")){

					$tempretArr["instra_total_feed"]	 = $insta_feed_tot_hyb;
					$tempretArr["instra_total_likes"]	 = $insta_like_tot_hyb;
					$tempretArr["instra_total_comments"] = $insta_comm_tot_hyb;

				} else {

					$tempretArr["instra_total_feed"]	 = $insta_feed_tot_indv + $insta_feed_tot_hyb;
					$tempretArr["instra_total_likes"]	 = $insta_like_tot_indv + $insta_like_tot_hyb;
					$tempretArr["instra_total_comments"] = $insta_comm_tot_indv + $insta_comm_tot_hyb;
				}
				
				#YouTube Related Content
				#-------------------------YouTube Feed Count			

				$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(total_views),0) as rec_views_total
					  	FROM user_youtubefeeds WHERE user_id=$user_id $yt_append_qry $dateAppend3";		

				$tempInfo	=	$this->model->allSelects($sql);
				$yt_feed_tot_indv = $tempInfo[0]['rec_total']; $yt_views_tot_indv = $tempInfo[0]['rec_views_total'];

				if($this->session->userdata("acess_type")=='Hybrid' && $search_by_pro != $this->session->userdata("userid")){
				
					$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(ytf.total_views),0) as rec_views_total
						  	FROM user_youtubefeeds ytf
						  	LEFT JOIN users u ON u.userid = ytf.user_id
						  	LEFT JOIN company_ref cr ON cr.userid= ytf.user_id
						  	WHERE cr.companyID = $user_id $staffIDCondt $yt_append_qry $dateAppend3";		
					$tempInfo = $this->model->allSelects($sql);
					$yt_feed_tot_hyb = $tempInfo[0]['rec_total']; $yt_views_tot_hyb = $tempInfo[0]['rec_views_total'];
				}
				
				if($search_by_pro!='all' && $search_by_pro != $this->session->userdata("userid")){
				
					$tempretArr["youtube_total_feed"]  = $yt_feed_tot_hyb;
					$tempretArr["youtube_total_views"] = $yt_views_tot_hyb;
				}
				else{
				
					$tempretArr["youtube_total_feed"]  = $yt_feed_tot_indv + $yt_feed_tot_hyb;
					$tempretArr["youtube_total_views"] = $yt_views_tot_indv + $yt_views_tot_hyb;
				}
				
				$StaffInfo[] =	$tempretArr;
				
				}
			}		

			/*echo "<pre>";
				print_r($StaffInfo);
			echo "</pre>";
			die;*/
		}
					
			$sql = "(Select cr.companyID as uid,u.FirstLastname as uname ,cr.userid from users u
							LEFT JOIN company_ref cr ON cr.companyID=u.userid
							where cr.userid=".$this->session->userdata("userid").")
								UNION
							(Select DISTINCT ups.sponsorCSid as uid, ups.sponsorname as uname, ups.prostaff_id from user_prostaff_sponsors ups
							 where ups.prostaff_id=".$this->session->userdata("userid").")";								
	
			$data['manufacturers'] = $this->model->allSelects($sql);
			$data['prostaff']	   = "";
			
			if($this->session->userdata("acess_type")=='Hybrid'){
			
				$sql = "SELECT DISTINCT u.userid,u.FirstLastname FROM users u 
								LEFT JOIN company_ref cr ON cr.userid=u.userid
								WHERE u.status='Active' and u.memtype='Staff' and cr.companyID=".$this->session->userdata("userid");
				$data['prostaff'] = $this->model->allSelects($sql);
			}
									
			$data['search_from_date']		=	date('Y-m-d',strtotime($search_from_date));
			$data['search_to_date']			=	date('Y-m-d',strtotime($search_to_date));		
			$data['search_select_staff']	=	$search_select_staff;						
			$data['StaffInfo']				=	$StaffInfo;
			$data['SearchStaffInfo']		=	$SearchStaffInfo;
			$data['search_by_manufacturer']	=	$search_by_manufacturer;
			$data['search_by_pro']			=	$search_by_pro;
			// echo '<pre>';
			// print_r($data);
			// die('here');
	
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/staff-reportbydate-nf',$data,true);
			$this->load->view('layout-inner',$data); 
	
 }

 function byreportstaffPDF()
 {
		if($_POST){
			$search_from_date	=	date("Y-m-d",strtotime($_POST['search_from_date']));
			$search_to_date		=	date("Y-m-d",strtotime($_POST['search_to_date']));
			$search_by_manufacturer = $_POST['search_by_manufacturer'];

		}
		else{
			$search_from_date	=	$this->session->userdata('search_from_date');
			$search_to_date		=	$this->session->userdata('search_to_date');
			$search_by_manufacturer	= $this->session->userdata('search_by_manufacturer');
		}

		$search_select_staff=	$this->session->userdata("userid");
		
		if($search_by_manufacturer!='all')
		{

			$sql = "Select u.userid as uid,u.FirstLastname as uname,u.fbkeywords,u.twtkeywords as twtkeywords,
					u.instakeywords, u.youtubekeywords from users u where u.memtype='Employer' AND u.userid=".$search_by_manufacturer;;
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$keywords		 = 	$TempInfo[0];
				$manufactID		 =	$keywords['uid'];
				$fbkeywords		 =	$keywords['fbkeywords'];		
				$twtkeywords	 =	$keywords['twtkeywords'];		
				$instakeywords	 =	$keywords['instakeywords'];	
				$youtubekeywords =	$keywords['youtubekeywords'];		
			}else {
				
				$sql = "Select DISTINCT ups.sponsorCSid as uid, ups.sponsorname as uname, ups.prostaff_id,
						ups.fbkeywords,ups.twitterkeywords as twtkeywords,ups.instakeywords, ups.youtubekeywords 
						from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$keywords		 = 	$TempInfo[0];
					$manufactID		 =	$keywords['uid'];
					$fbkeywords		 =	$keywords['fbkeywords'];		
					$twtkeywords	 =	$keywords['twtkeywords'];		
					$instakeywords	 =	$keywords['instakeywords'];	
					$youtubekeywords =	$keywords['youtubekeywords'];		
				}
			}		
		}
		else
		{
			$fbkeywords		=	getKeywords($this->session->userdata("userid"),'fbkeywords');		
			$twtkeywords	=	getKeywords($this->session->userdata("userid"),'twtkeywords');		
			$instakeywords	=	getKeywords($this->session->userdata("userid"),'instakeywords');
			$youtubekeywords =	getKeywords($this->session->userdata("userid"),'youtubekeywords');		
			$manufactID		 = 0;
		}
		
		#Fetching out Staff information.
		$sql		=	"select * from users where memtype='Staff'";
		if($search_select_staff){
			$sql	.=	" and userid=$search_select_staff";
		}
		$tempUserInfo	=	$this->model->allSelects($sql);
		$StaffInfo		=	array();

		if($tempUserInfo){
		
			foreach($tempUserInfo as $key=>$val){
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				

				#Tournaments Creation Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);	
				$tempretArr["tournaments_creation_total"]	=	$tempInfo[0]['rec_total'];
				
				#Tournaments 1st place Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE tfinishedplace=1 and staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);			
				$tempretArr["tournaments_fist_place"]=$tempInfo[0]['rec_total'];	
				
               #For Blog Creation
				$totalBlogCount = 0;
				
                $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_blog WHERE user_blog.status='Active' and userid=$user_id";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total'];

				$sql = "SELECT IFNULL(count(*),0) as rec_total_cs FROM user_customscoops WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total_cs'];
				
                $tempretArr["blog_total_count"] = $totalBlogCount;

				# For Impressions Count BLOG
				$sql="select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }

				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["blog_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Leads Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["leads_total_count"]=$tempInfo[0]['rec_total'];
				
               #For Print Creation
				$totalPrintCount = 0;
				
                $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_print WHERE status='Active' and userid=$user_id";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
                }
 				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total'];

				$sql = "SELECT IFNULL(count(*),0) as rec_total_pcs FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total_pcs'];
							
                $tempretArr["print_total_count"] = $totalPrintCount;

				# For Impressions Count PRINT
				 $sql = "select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql .=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["print_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Events Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and event_start_date >= '$search_from_date' and event_start_date <='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["event_total_count"]=$tempInfo[0]['rec_total'];
				
				#Facebook Related Content				
				#-------------------------Facebook Feed Count
				$sql="select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type!='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];				

				#-------------------------Facebook Feed Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Facebook Feed Comment Count
				$sql="select IFNULL(sum(comment_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_comment_count"]=$tempInfo[0]['rec_total'];				
				
				#-------------------------Facebook Photo Count
				$tempretArr["facebook_photo_count"] = 0;				

				#-------------------------Facebook Photo Like Count
				$tempretArr["facebook_photo_like_count"] = 0;	

				#-------------------------Facebook Photo Comment Count
				$tempretArr["facebook_photo_comment_count"]= 0;					

                #-------------------------Facebook Video Count
				$uservideoCount = 0;
				
				#Videos as Timeline Feeds
                $sql = "select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_count"] = $uservideoCount + $userfeedVideoCount;
 				
                #-------------------------Facebook Video Like Count
				$userVideoLikeCount = 0;
				
				#Videos as Timeline Feeds/Likes
                $sql = "select IFNULL(sum(likes_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";
 
                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoLikeCount = $tempInfo[0]['rec_total'];
			
                $tempretArr["facebook_video_like_count"] = $userVideoLikeCount + $userfeedVideoLikeCount;
				
				 #-------------------------Facebook Video Comment Count
				$userVideoCommentCount = 0;

				#Videos as Timeline Feeds/Comments
                $sql = "select IFNULL(sum(comment_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCommentCount = $tempInfo[0]['rec_total'];
				
                $tempretArr["facebook_video_comment_count"] = $userVideoCommentCount + $userfeedVideoCommentCount;
												
				#Twitter Related Content
				#-------------------------Twitter Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql .=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}									  

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					
				
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];	

                #-------------------------Instragram Comments Count			
                $sql = "SELECT IFNULL(sum(comments_count),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";

                if ($instakeywords) {
                    $keywordArr = explode(',', $instakeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (text like '$val%' or text like '%$val%') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '" . strtotime($search_from_date) . "' and created_time <= '" . strtotime($search_to_date) . "' ";
                }
                $tempInfo = $this->model->allSelects($sql);
                $tempretArr["instra_total_comments"] = $tempInfo[0]['rec_total'];

				#YouTube Related Content
				#-------------------------YouTube Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		

				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}
								  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_feed"]=$tempInfo[0]['rec_total'];

				#-------------------------YouTube Views Count			
				$sql="SELECT IFNULL(sum(total_views),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		
						
				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}
								  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_views"]=$tempInfo[0]['rec_total'];	
			
				$StaffInfo[] =	$tempretArr;
			}
		}		
								
		$data['search_from_date']		=	date('Y-m-d',strtotime($search_from_date));
		$data['search_to_date']			=	date('Y-m-d',strtotime($search_to_date));		
		$data['search_select_staff']	=	$search_select_staff;						
		$data['StaffInfo']				=	$StaffInfo;
		$data['SearchStaffInfo']		=	$SearchStaffInfo;
		$data['search_by_manufacturer']	=	$search_by_manufacturer;

	$text = $this->load->view('staff/staff-reportbydatePDF',$data,true);
	$html = '<html><body>'.$text.'</body></html>';

		$this->load->library('pdf');
		$this->pdf->load_html($html);
		$this->pdf->render();
		$pdf = $this->pdf->output();
		$FileName = $this->session->userdata("FirstLastname")."-".date('M-d-Y');
		ob_end_clean();
		$this->pdf->stream("Report-".$FileName.".pdf");
		
 }

 function byreportstaffPDFSM(){
 	
		if($_POST){
			$search_from_date	=	date("Y-m-d",strtotime($_POST['search_from_date']));
			$search_to_date		=	date("Y-m-d",strtotime($_POST['search_to_date']));
			$search_by_manufacturer = $_POST['search_by_manufacturer'];

		}
		else{
			$search_from_date	=	$this->session->userdata('search_from_date');
			$search_to_date		=	$this->session->userdata('search_to_date');
			$search_by_manufacturer	= $this->session->userdata('search_by_manufacturer');
		}

		$search_select_staff=	$this->session->userdata("userid");
		
		if($search_by_manufacturer!='all')
		{

			$sql = "Select u.userid as uid,u.FirstLastname as uname,u.fbkeywords,u.twtkeywords as twtkeywords,
					u.instakeywords, u.youtubekeywords from users u where u.memtype='Employer' AND u.userid=".$search_by_manufacturer;;
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$keywords		 = 	$TempInfo[0];
				$manufactID		 =	$keywords['uid'];
				$fbkeywords		 =	$keywords['fbkeywords'];		
				$twtkeywords	 =	$keywords['twtkeywords'];		
				$instakeywords	 =	$keywords['instakeywords'];	
				$youtubekeywords =	$keywords['youtubekeywords'];		
			}else {
				
				$sql = "Select DISTINCT ups.sponsorCSid as uid, ups.sponsorname as uname, ups.prostaff_id,
						ups.fbkeywords,ups.twitterkeywords as twtkeywords,ups.instakeywords, ups.youtubekeywords 
						from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$keywords		 = 	$TempInfo[0];
					$manufactID		 =	$keywords['uid'];
					$fbkeywords		 =	$keywords['fbkeywords'];		
					$twtkeywords	 =	$keywords['twtkeywords'];		
					$instakeywords	 =	$keywords['instakeywords'];	
					$youtubekeywords =	$keywords['youtubekeywords'];		
				}
			}		
		}
		else
		{
			$fbkeywords		=	getKeywords($this->session->userdata("userid"),'fbkeywords');		
			$twtkeywords	=	getKeywords($this->session->userdata("userid"),'twtkeywords');		
			$instakeywords	=	getKeywords($this->session->userdata("userid"),'instakeywords');
			$youtubekeywords =	getKeywords($this->session->userdata("userid"),'youtubekeywords');		
		}
		
		#Fetching out Staff information.
		$sql =	"select * from users where memtype='Staff'";
		if($search_select_staff){
			$sql .=	" and userid=$search_select_staff";
		}
		$tempUserInfo	=	$this->model->allSelects($sql);
		$StaffInfo		=	array();

		if($tempUserInfo){
			foreach($tempUserInfo as $key=>$val){
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				
			
				#Facebook Related Content				
				#-------------------------Facebook Feed Count
				$sql="select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type !='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];				

				#-------------------------Facebook Feed Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Facebook Feed Comment Count
				$sql="select IFNULL(sum(comment_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '".trim($val)."%' or story like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_comment_count"]=$tempInfo[0]['rec_total'];				
				
				#-------------------------Facebook Photo Count
				$tempretArr["facebook_photo_count"]=0;
							
				#-------------------------Facebook Photo Like Count
				$tempretArr["facebook_photo_like_count"]=0;	

				#-------------------------Facebook Photo Comment Count
				$tempretArr["facebook_photo_comment_count"]=0;					

                #-------------------------Facebook Video Count
				$uservideoCount = 0;
				
				#Videos as Timeline Feeds
                $sql = "select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_count"] = $uservideoCount + $userfeedVideoCount;
 				
                #-------------------------Facebook Video Like Count
				$userVideoLikeCount = 0;
				
				#Videos as Timeline Feeds/Likes
                $sql = "select IFNULL(sum(likes_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoLikeCount = $tempInfo[0]['rec_total'];
			
                $tempretArr["facebook_video_like_count"] = $userVideoLikeCount + $userfeedVideoLikeCount;
				
				#-------------------------Facebook Video Comment Count
				$userVideoCommentCount = 0;

				#Videos as Timeline Feeds/Comments
                $sql = "select IFNULL(sum(comment_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCommentCount = $tempInfo[0]['rec_total'];
				
                $tempretArr["facebook_video_comment_count"] = $userVideoCommentCount + $userfeedVideoCommentCount;
												
				#Twitter Related Content
				#-------------------------Twitter Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql .=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}									  

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					
				
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];	

                #-------------------------Instragram Comments Count			
                $sql = "SELECT IFNULL(sum(comments_count),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";

                if ($instakeywords) {
                    $keywordArr = explode(',', $instakeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (text like '$val%' or text like '%$val%') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '" . strtotime($search_from_date) . "' and created_time <= '" . strtotime($search_to_date) . "' ";
                }
                $tempInfo = $this->model->allSelects($sql);
                $tempretArr["instra_total_comments"] = $tempInfo[0]['rec_total'];

				#YouTube Related Content
				#-------------------------YouTube Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		

				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}
								  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_feed"]=$tempInfo[0]['rec_total'];

				#-------------------------YouTube Views Count			
				$sql="SELECT IFNULL(sum(total_views),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		
						
				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}
								  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_views"]=$tempInfo[0]['rec_total'];	
			
				$StaffInfo[] =	$tempretArr;
			}
		}		
								
		$data['search_from_date']		=	date('Y-m-d',strtotime($search_from_date));
		$data['search_to_date']			=	date('Y-m-d',strtotime($search_to_date));		
		$data['search_select_staff']	=	$search_select_staff;						
		$data['StaffInfo']				=	$StaffInfo;
		$data['SearchStaffInfo']		=	$SearchStaffInfo;
		$data['search_by_manufacturer']	=	$search_by_manufacturer;
		
			/*echo "<pre>";
				print_r($data);
			echo "</pre>";*/

	$text = $this->load->view('staff/staff-reportbydatePDFSM',$data,true);
	$html = '<html><body>'.$text.'</body></html>';

		$this->load->library('pdf');
		$this->pdf->load_html($html);
		$this->pdf->render();
		$pdf = $this->pdf->output();
		$FileName = $this->session->userdata("FirstLastname")."-".date('M-d-Y');
		ob_end_clean();
		$this->pdf->stream("Report-".$FileName.".pdf");
 		
 }

# For Export result by date
  function exportresultbydate(){	
		
		if($_POST){
			$search_from_date	=	$_POST['search_from_date'];
			$search_to_date		=	$_POST['search_to_date'];
			$search_by_manufacturer = $_POST['search_by_manufacturer'];

		}
		else{
			$search_from_date	=	$this->session->userdata('search_from_date');
			$search_to_date		=	$this->session->userdata('search_to_date');
			$search_by_manufacturer	= $this->session->userdata('search_by_manufacturer');

		}

		$search_select_staff = $this->session->userdata("userid");
				
		if($search_by_manufacturer!='all')
		{
			$sql = "Select u.userid as uid,u.FirstLastname as uname,u.fbkeywords,u.twtkeywords as twtkeywords,
					u.instakeywords, u.youtubekeywords from users u where u.memtype='Employer' AND u.userid=".$search_by_manufacturer;
			$TempInfo =	$this->model->allSelects($sql);	
			if(count($TempInfo)>0){
				$keywords		 = 	$TempInfo[0];
				$manufactID		 =	$keywords['uid'];
				$fbkeywords		 =	$keywords['fbkeywords'];		
				$twtkeywords	 =	$keywords['twtkeywords'];		
				$instakeywords	 =	$keywords['instakeywords'];	
				$youtubekeywords =	$keywords['youtubekeywords'];		
			}else {
				
				$sql = "Select DISTINCT ups.sponsorCSid as uid, ups.sponsorname as uname, ups.prostaff_id,
						ups.fbkeywords,ups.twitterkeywords as twtkeywords,ups.instakeywords, ups.youtubekeywords 
						from user_prostaff_sponsors ups
						where ups.prostaff_id=".$this->session->userdata("userid")." AND ups.sponsorCSid=".$_POST["search_by_manufacturer"];
				$TempInfo =	$this->model->allSelects($sql);	
				if(count($TempInfo)>0){
					$keywords		 = 	$TempInfo[0];
					$manufactID		 =	$keywords['uid'];
					$fbkeywords		 =	$keywords['fbkeywords'];		
					$twtkeywords	 =	$keywords['twtkeywords'];		
					$instakeywords	 =	$keywords['instakeywords'];	
					$youtubekeywords =	$keywords['youtubekeywords'];		
				}
			}		
		}
		else
		{
			$fbkeywords		=	getKeywords($this->session->userdata("userid"),'fbkeywords');		
			$twtkeywords	=	getKeywords($this->session->userdata("userid"),'twtkeywords');		
			$instakeywords	=	getKeywords($this->session->userdata("userid"),'instakeywords');
			$youtubekeywords =	getKeywords($this->session->userdata("userid"),'youtubekeywords');	
			$manufactID		 = 0;				
		}

		#Fetching out Staff information.
		$sql = "select * from users where memtype='Staff'";
		if($search_select_staff){
			$sql	.=	" and userid=$search_select_staff";
		}
		$tempUserInfo	=	$this->model->allSelects($sql);
		$StaffInfo		=	array();

		if($tempUserInfo){
			foreach($tempUserInfo as $key=>$val){
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				
				#Tournaments Creation Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);	
				$tempretArr["tournaments_creation_total"]	=	$tempInfo[0]['rec_total'];
				
				#Tournaments 1st place Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE tfinishedplace=1 and staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);			
				$tempretArr["tournaments_fist_place"]=$tempInfo[0]['rec_total'];	
				
               #For Blog Creation
				$totalBlogCount = 0;
				
                $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_blog WHERE user_blog.status='Active' and userid=$user_id";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
                }
 				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total'];

                $sql = "SELECT IFNULL(count(*),0) as rec_total_cs FROM user_customscoops WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";
				if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					//$sql	.=	" and companyID=$search_by_manufacturer";
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalBlogCount = $totalBlogCount + $tempInfo[0]['rec_total_cs'];
				
                $tempretArr["blog_total_count"] = $totalBlogCount;

				# For Impressions Count BLOG
                $sql="select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE sourceType !='Magazine' AND find_in_set('".$user_id."', user_id)";
				if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql .=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["blog_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Leads Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["leads_total_count"]=$tempInfo[0]['rec_total'];
				
               #For Print Creation
				$totalPrintCount = 0;
				
                $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_print WHERE status='Active' and userid=$user_id";
                if ($search_from_date && $search_to_date) {
                    $sql .= " and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$manufactID."',sharedTO)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total'];

                $sql = "SELECT IFNULL(count(*),0) as rec_total_pcs FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
				if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

                $tempInfo = $this->model->allSelects($sql);
				$totalPrintCount = $totalPrintCount + $tempInfo[0]['rec_total_pcs'];
							
                $tempretArr["print_total_count"] = $totalPrintCount;

				# For Impressions Count PRINT
                $sql = "select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops WHERE sourceType='Magazine' and find_in_set('".$user_id."', user_id)";
				if ($search_from_date && $search_to_date) {
                    $sql .= " and created_date >= '$search_from_date' and created_date<='$search_to_date'";
                }
				if($search_by_manufacturer!="all"){
					$sql .=	" and find_in_set('".$search_by_manufacturer."', CS_sponsorID)";
				}

				$tempInfo = $this->model->allSelects($sql);
				$tempretArr["print_impression_count"] = $tempInfo[0]['impression_total'];
				
				#For Events Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and event_start_date >= '$search_from_date' and event_start_date<='$search_to_date'";
				}		
				if($search_by_manufacturer!="all"){
					$sql	.=	" and find_in_set('".$search_by_manufacturer."',sharedTO)";
				}

				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["event_total_count"]=$tempInfo[0]['rec_total'];
				
				#Facebook Related Content				
				#-------------------------Facebook Feed Count
				$sql="select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type !='video' AND feed_type!='photo'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video' AND feed_type!='photo'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Facebook Comment Count
				$sql="select IFNULL(sum(comment_count),0) as rec_total from user_feed  where user_id=$user_id AND feed_type !='video' AND feed_type!='photo'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_comment_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Facebook Photo Count
                $sql = "select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type='photo'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like '$val%' or story like '%$val%') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$tempretArr["facebook_photo_count"] = $tempInfo[0]['rec_total'];	
							
				#-------------------------Facebook Photo Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='photo'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_like_count"]=$tempInfo[0]['rec_total'];	
				#-------------------------Facebook Photo Comment Count
				$sql="select IFNULL(sum(comment_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='photo'";

				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_comment_count"]=$tempInfo[0]['rec_total'];	

                #-------------------------Facebook Video Count
				$uservideoCount = 0;
				
				#Videos as Timeline Feeds
                $sql = "select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_count"] = $uservideoCount + $userfeedVideoCount;
 				
                #-------------------------Facebook Video Like Count
				$userVideoLikeCount = 0;
				
				#Videos as Timeline Feeds/Likes
                $sql = "select IFNULL(sum(likes_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoLikeCount = $tempInfo[0]['rec_total'];
			
                $tempretArr["facebook_video_like_count"] = $userVideoLikeCount + $userfeedVideoLikeCount;
				
				#-------------------------Facebook Video Comment Count
				$userVideoCommentCount = 0;

				#Videos as Timeline Feeds/Comments
                $sql = "select IFNULL(sum(comment_count),0) as rec_total from user_feed where user_id=$user_id AND feed_type='video'";

                if ($fbkeywords) {
                    $keywordArr = explode(',', $fbkeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (story like ' $val %' or story like '% $val %') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
                }
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCommentCount = $tempInfo[0]['rec_total'];
				
                $tempretArr["facebook_video_comment_count"] = $userVideoCommentCount + $userfeedVideoCommentCount;

				#Twitter Related Content
				#-------------------------Twitter Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}									  

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					
				
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];

				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];										

                #-------------------------Instragram Comments Count			
                $sql = "SELECT IFNULL(sum(comments_count),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";

                if ($instakeywords) {
                    $keywordArr = explode(',', $instakeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (text like '$val%' or text like '%$val%') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
                    $sql .= " and created_time>= '" . strtotime($search_from_date) . "' and created_time <= '" . strtotime($search_to_date) . "' ";
                }
                $tempInfo = $this->model->allSelects($sql);
                $tempretArr["instra_total_comments"] = $tempInfo[0]['rec_total'];

				#YouTube Related Content
				#-------------------------YouTube Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		

				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}		  

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_feed"]=$tempInfo[0]['rec_total'];

				#-------------------------YouTube Views Count			
				$sql="SELECT IFNULL(sum(total_views),0) as rec_total FROM user_youtubefeeds where user_id=$user_id ";		
						
				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[] = " (text like '".trim($val)."%' or text like '%".trim($val)."%' or title like '".trim($val)."%' or title like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql .=	" and created_date>= '".$search_from_date."' and created_date <= '".$search_to_date."' ";
				}
								  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["youtube_total_views"]=$tempInfo[0]['rec_total'];	

				$StaffInfo[] =	$tempretArr;
			}
		}		
								
		/*echo "<pre>";
			print_r($StaffInfo);
		echo "</pre>";*/
		
		$fb_query	=	$twitter_qry	=	$instragram_qry	=	$tournament_qry		=	$blog_qry	=	'';					
		
		if($StaffInfo){
			usort($StaffInfo, 'compareName');
		}
		if($SearchStaffInfo){
			usort($SearchStaffInfo, 'compareName');
		}		
		
		if($search_select_staff>0){
			$sql=	"select * from users where userid=$search_select_staff";
			$SearchStaffInfo	=	$this->model->allSelects($sql);			
			$staff_member	=	$SearchStaffInfo[0]['FirstLastname'];
		}
		else 
		$staff_member	=	'All Members';
		$all_activity_total	=	0;
		
		$CsvData	=	array();
		$CsvData[]	=	array('Exported Result');	
		$CsvData[]	=	
						array(
							'From Date',
							$search_from_date,
							'To Date',
							$search_to_date,
							"For $staff_member"
							);
		$CsvData[]	=	array();		
		$DispData	=	array(
								'Staff',
								'Facebook Posts',
								'Facebook Likes',	
								'Facebook Comments',								
								'Facebook Photo',
								'Facebook Photo Likes',
								'Facebook Photo Comments',
								'Facebook Video',
								'Facebook Video Likes',	
								'Facebook Video Comments',																
								'Twitter Feeds',
								'Twitter Retweet',
								'Twitter Favorite',																
								'InstaGram Feeds',
								'InstaGram Likes',
								'InstaGram Comments',
								'YouTube Feeds',
								'YouTube Views',
								'Tournaments Creation',								
								'Blog Creation',
								'Blog Impressions',
								'Events',
								'Leads',
								'Print',	
								'Print Impressions',																																							
								'Total Activity'									
							);
		$CsvData[]	=	$DispData;				
		/*$DispData	=	array(
								'Cumulative Total',
								$facebook_feed_total,
								$facebook_like_total,								
								$facebook_photo_total,
								$facebook_photo_like_total,
								$facebook_video_total,
								$facebook_video_like_total,								
								$twitter_feed_total,
								$twitter_retweet_total,
								$twitter_favorite_total,
								$instragram_feed_total,
								$instragram_like_total,
								$tournament_total,
								$tournament_1stplace_total,
								$blog_total,
								$all_activity_total
							);
		$CsvData[]	=	$DispData;		*/

		if($StaffInfo){
			 $grand_fb_feed	=	$grand_fb_like = $grand_fb_comment  =	$grand_fb_photo_feed	=	$grand_fb_photo_like	=	$grand_fb_photo_comment	=  $grand_fb_video_feed	=	$grand_fb_video_like	=	$grand_fb_video_comment	=	$grand_tw_feed	= $grand_tw_retweet	=	$grand_tw_fav	=	$grand_in_feed	=	$grand_in_like	= $grand_tourn_cr	=	$grand_tourn_1st	=	$grand_blog_crea	=	$grand_activity_total	=	$grand_event_total	=	$grand_print_total	=	$grand_lead_total	= $total_activity = 0;
			
			 foreach($StaffInfo as $key=>$val){
				 $disp_name		=	stripslashes($val['FirstLastname']);
				 $disp_fb_posts	=	$val['facebook_feed_count'];
				 $disp_fb_likes	=	$val['facebook_like_count'];
				 $disp_fb_comment	=	$val['facebook_comment_count'];
				 				 
				 $disp_fb_photo_posts	=	$val['facebook_photo_count'];
				 $disp_fb_photo_likes	=	$val['facebook_photo_like_count'];
				 $disp_fb_photo_comment	=	$val['facebook_photo_comment_count'];
				 
				 $disp_fb_video_posts	=	$val['facebook_video_count'];
				 $disp_fb_video_likes	=	$val['facebook_video_like_count'];								 
				 $disp_fb_video_comment	=	$val['facebook_video_comment_count'];								 
				 
				 $disp_tw_feed	=	$val['twitter_feed_count'];
				 $disp_tw_retweet	=	$val['twitter_retweet_count'];
				 $disp_tw_fav	=	$val['twitter_favorite_count'];
				 $disp_in_feed	=	$val['instra_total_feed'];
				 $disp_in_likes	=	$val['instra_total_likes'];
				 $disp_in_comments = $val['instra_total_comments'];
				 
				 $disp_yt_feed	=	$val['youtube_total_feed'];
				 $disp_yt_views	=	$val['youtube_total_views'];

				 $disp_to_creat	=	$val['tournaments_creation_total'];
				 $disp_to_win	=	$val['tournaments_fist_place'];
				 $disp_blo_creat	=	$val['blog_total_count'];
				 $disp_blog_impression	=	$val['blog_impression_count'];
				 $disp_event_total	=	$val['event_total_count'];
				 $disp_print_total	=	$val['print_total_count'];
				 $disp_print_impression	=	$val['print_impression_count'];
				 $disp_lead_total	=	$val['leads_total_count'];
				 
				 $total_activity	= $disp_fb_posts+$disp_tw_feed+$disp_in_feed+$disp_yt_feed+$disp_to_creat+$disp_blo_creat+ $disp_print_total +$disp_fb_photo_posts+$disp_fb_video_posts+$disp_event_total+$disp_lead_total;
				 
				 $grand_fb_feed	+=	$disp_fb_posts;
				 $grand_fb_like	+=	$disp_fb_likes;
				 $grand_fb_comment	+=	$disp_fb_comment;
				 
				 $grand_fb_photo_feed	+=	$disp_fb_photo_posts;
				 $grand_fb_photo_like	+=	$disp_fb_photo_likes;								 
				 $grand_fb_photo_comment	+=	$disp_fb_photo_comment;								 
				 
				 $grand_fb_video_feed	+=	$disp_fb_video_posts;
				 $grand_fb_video_like	+=	$disp_fb_video_likes;								 				 
				 $grand_fb_video_comment	+=	$disp_fb_video_comment;								 				 
				 
				 $grand_tw_feed	+=	$disp_tw_feed;
				 $grand_tw_retweet +=	$disp_tw_retweet;
				 $grand_tw_fav	+=	$disp_tw_fav;
				 $grand_in_feed	+=	$disp_in_feed;
				 $grand_in_like	+=	$disp_in_likes;
				 $grand_in_comments += $disp_in_comments;
				 
				 $grand_yt_feed	+=	$disp_yt_feed;
				 $grand_yt_views	+=	$disp_yt_views;

				 $grand_tourn_cr	+=	$disp_to_creat;
				 $grand_tourn_1st	+=	$disp_to_win;
				 $grand_blog_crea	+=	$disp_blo_creat;
				 $grand_blog_impression	+=	$disp_blog_impression;
				 $grand_activity_total	+=	$total_activity;
			     $grand_event_total	+=	$disp_event_total;
				 $grand_print_total	+=	$disp_print_total;
				 $grand_print_impression	+=	$disp_print_impression;
				 $grand_lead_total	+=	$disp_lead_total;				 

				 $DispData	=	array(
								$disp_name,
								$disp_fb_posts,
								$disp_fb_likes,								
								$disp_fb_comment,
								$disp_fb_photo_posts,
								$disp_fb_photo_likes,								
								$disp_fb_photo_comment,
								$disp_fb_video_posts,
								$disp_fb_video_likes,								
								$disp_fb_video_comment,																
								$disp_tw_feed,
								$disp_tw_retweet,
								$disp_tw_fav,
								$disp_in_feed,
								$disp_in_likes,
								$disp_in_comments,
								$disp_yt_feed,
								$disp_yt_views,
								$disp_to_creat,
								$disp_blo_creat,
								$disp_blog_impression,
								$disp_event_total,
								$disp_lead_total,
								$disp_print_total,
								$disp_print_impression,																								
								$total_activity
							);
#				$CsvData[]	=	implode(',',$DispData);				 				 				 
				$CsvData[]	=	$DispData;				 				 				 
			 }
			 $DispData	=	array(
								'Total',
								$grand_fb_feed,
								$grand_fb_like,
								$grand_fb_comment,
																
								$grand_fb_photo_feed,
								$grand_fb_photo_like,
								$grand_fb_photo_comment,
								
								$grand_fb_video_feed,
								$grand_fb_video_like,
								$grand_fb_video_comment,
								
								$grand_tw_feed,
								$grand_tw_retweet,
								$grand_tw_fav,
								$grand_in_feed,
								$grand_in_like,
								$grand_in_comments,
								$grand_yt_feed,
								$grand_yt_views,
								$grand_tourn_cr,
								$grand_blog_crea,
								$grand_blog_impression,
								$grand_event_total,
								$grand_lead_total,
								$grand_print_total,	
								$grand_print_impression,																							
								$grand_activity_total
							);
			#$CsvData[]	=	implode(',',$DispData);	
 			$CsvData[]	=	$DispData;
			
		/*echo "<pre>";
			print_r($CsvData);
		echo "</pre>"; die;*/
	
		}

		$file_name	=	date('Ymd',strtotime($search_from_date)).'-'.date('Ymd',strtotime($search_to_date)).'.csv';
		error_reporting(0);
		header( 'Content-Type: text/csv' );
		header( 'Content-Disposition: attachment;filename='.$file_name);
		$fp = fopen('php://output', 'w');		
		foreach ($CsvData as $key=>$val) {
			fputcsv($fp, $val);
		}
		fclose($fp);
		$contLength = ob_get_length();
		header( 'Content-Length: '.$contLength);									

 }	

# ADD prints
 public function addprint($printid=0)
	{	
		$this->form_validation->set_rules('print_publish_name', 'Publication Name', 'required');
		$this->form_validation->set_rules('print_issue_date', 'Issue Date', 'required');	
		//$this->form_validation->set_rules('print_art_name', 'Article Name', 'required');
		//$this->form_validation->set_rules('print_cov_type', 'Coverage type', 'required');
		//$this->form_validation->set_rules('print_img_path', 'Attach image', 'callback_validate_print_attachment');
													
		if ($this->form_validation->run() == TRUE)
		{					
			//$print_cov_type = implode(',',$this->input->post('print_cov_type'));
			
			$InsertArr=array(
					'print_publish_name'=>$this->input->post('print_publish_name'),
					'print_issue_date'=>$this->input->post('print_issue_date'),
					'print_art_name'=>$this->input->post('print_art_name'),
					'print_cov_type'=>$this->input->post('print_cov_type'),
					'print_issue_date'=>date('Y-m-d',strtotime($this->input->post('print_issue_date'))),
					'userid'=>$this->session->userdata("userid"),
					'status'=>"Active"							
				   );

				if($this->input->post('txtmanufacturers')==""){ $InsertArr['sharedTO'] = "all"; }
				else { $InsertArr['sharedTO'] = $this->input->post('txtmanufacturers'); }
				   
				   if($printid>0){
						$this->model->allUpdate('print_id',$printid,'user_print',$InsertArr);
						$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
						redirect('staff/allprints');
				   }
				   else
				   {
						$print_img_path	=	'';
						if($_FILES['print_img_path']['error'] == 0){
							$fileName = $_FILES['print_img_path']['name'];
							$file='print_img_path';		
							$upload = uploadFile($file,'*','assets/upload/print_img_path',$fileName);
							if($upload['success']){
								$print_img_path	=	$upload['path'];
							}									
						}			
						
						$InsertArr['print_img_path']	=	$print_img_path;		   									   
						$this->model->allInserts('user_print',$InsertArr);	
						
						/* $sql="Select * from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	
			$ulist=$this->model->allSelects($sql);
			$this->emailSend('IMPORTANT New Print added by Pro-Staff Member',$ulist);*/	
							
						$this->session->set_flashdata('sucess', 'Print Added Successfully..!! Add Another? Click Add New.');
						redirect('staff/allprints');															

					}				
		}
		
			if(isset($_POST) && count($_POST)>0){
			
				foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
			else if($printid>0){
						
							$sql	=	"select * from user_print where print_id=$printid";
							$TempInfo	=	$this->model->allSelects($sql);
							$printInfo	=	$TempInfo[0];
																		
							$data['print_publish_name']	=	$printInfo['print_publish_name'];
							$data['print_issue_date']	=	$printInfo['print_issue_date'];
							$data['print_art_name']		=	$printInfo['print_art_name'];
							$data['print_cov_type']		=	$printInfo['print_cov_type'];
							$data['print_img_path']		=	$printInfo['print_img_path'];
							$data['txtsharedTO']	 	=	$printInfo['sharedTO'];
							$data['printid']			=	$printid;	
						}		
				else{
					$data['print_publish_name']	=	'';
					$data['print_issue_date']	=	'';
					$data['print_art_name']		=	'';
					$data['print_cov_type']		=	'';
					$data['print_img_path']		=	'';		
					$data['txtsharedTO']	 	= 	'all';
					$data['printid']			=	$printid;	
				}
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
													   
				$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
						 LEFT JOIN company_ref cr ON cr.companyID=u.userid
						 WHERE cr.userid=".$this->session->userdata("userid").")
						 UNION
						(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
						 WHERE ups.prostaff_id=".$this->session->userdata("userid").")";	
	   
				$ulist=$this->model->allSelects($sql);
				$data['ulist']=$ulist;
				
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);		
		$data['errorreg']= trim(strip_tags(validation_errors()));		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/add-print',$data,true);
		$this->load->view('layout-inner',$data);			
	}

#	Validate image extension	
	function validate_print_attachment(){	
		
		if($_FILES['print_img_path']['error'] == 0){
			$fileEle	=	$_FILES['print_img_path'];
			$extension	=	strtolower(end(explode('.',$fileEle['name'])));
			
			//$validExtension	=	GetImageAttachmentext();					  				
			$validExtension		=	GetMsgAttachmentext();
			
			if(in_array($extension,$validExtension)){
				return true;
			}
			else{
				$this->form_validation->set_message('validate_print_attachment', 'Please upload valid Attachment');
				return false;
			}
		}
		else{
			$this->form_validation->set_message('validate_print_attachment', 'Please upload valid Attachment');
				return false;
		}
	}

# Manage Prints
 public function allprints(){						

		$sql = "Select DISTINCT * from (Select up.print_id, up.print_publish_name, up.print_art_name, up.print_cov_type, 
				up.print_img_path, up.print_issue_date, 'PM' as print from user_print up
				LEFT JOIN users u on up.userid=u.userid
				where up.userid=".$this->session->userdata("userid")."
					UNION
				Select uc.id as print_id, uc.source AS print_publish_name, uc.headline as print_art_name,
				uc.sourceType AS print_cov_type, uc.link as print_img_path, uc.created_date as print_issue_date, 'CS' as print
				from user_customscoops uc
				where uc.sourceType='Magazine' AND find_in_set('".$this->session->userdata("userid")."',uc.user_id) 
				) a ORDER BY print_issue_date desc";
				
		if($_POST['filterBystatus']){
			if($_POST['filterBystatus'] != 'Filter By Status'){
				$sql	.=	" and status='".$_POST['filterBystatus']."'";
			}
		}
		
		$search_by = "";
        $search_value = "";

		if (@$_POST) {
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
         } 

		if($search_by	==	'orderbydesc' && strlen($search_value)>0){
			$sql	.=	" ORDER BY print_issue_date DESC";			
		}

		if($search_by	==	'orderbyasc' && strlen($search_value)>0){
			$sql	.=	" ORDER BY print_issue_date ASC";
		}

		
		$data["PrintList"]=$this->model->allSelects($sql);
					
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);					

				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/manageprints',$data,true);
		$this->load->view('layout-inner',$data);			
	}

#Manage Print Status
 public function printstatus()
	{
		if(isset($_POST["printid"],$_POST["status"]))
        {
        
			if($_POST["status"]=="Delete")
			{
				$qstr="delete from user_print where print_id=".$_POST["printid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
				echo "<strong>Deleted Sucessfully..!!</strong>";
			}
			else if($_POST["status"]=="DeleteCS")
			{
				$qstr="delete from user_customscoop where id=".$_POST["printid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
				echo "<strong>Deleted Sucessfully..!!</strong>";
			}
			else
			{

				$qstr="update user_print set status='".$_POST["status"]."' where print_id=".$_POST["printid"];
				$this->model->allQueries($qstr); 
				$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
				echo "<strong>Status Updated Sucessfully..!!</strong>";
			}         
        } 
	}
	
#BULK Print Status
 public function managebulkprint(){
 
        if(isset($_POST["id"],$_POST['action'])){
		
			foreach($_POST["id"] as $key=>$val){
				$id=$val;
				$action=$_POST['action'];
				$qstr="update user_print set status='".$_POST["action"]."' where print_id=".$id;
				$this->model->allQueries($qstr); 
			}
			
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
            
        }        
    }

# ADD leads
 public function addlead($leadid=0){
 	
		$this->form_validation->set_rules('lead_deal_name', 'Dealer Name', 'required');
		//$this->form_validation->set_rules('lead_del_add', 'Dealer Address', 'required');	
		//$this->form_validation->set_rules('lead_del_phone', 'Dealer Phone Number', 'required');
		//$this->form_validation->set_rules('lead_buy_name', 'Contact/Buyer Name', 'required');
		//$this->form_validation->set_rules('lead_buy_phone', 'Attach Contact/Buyer Phone Number', 'required');		
		//$this->form_validation->set_rules('lead_buy_email', 'Contact/Buyer email address', 'required');
		//$this->form_validation->set_rules('lead_rel', 'About dealer', 'required');
															
		if ($this->form_validation->run() == TRUE){
							
				$InsertArr=array(
					'lead_deal_name'=>$this->input->post('lead_deal_name'),
					'lead_del_add'=>$this->input->post('lead_del_add'),
					'lead_del_phone'=>$this->input->post('lead_del_phone'),					
					'lead_buy_name'=>$this->input->post('lead_buy_name'),
					'lead_buy_phone'=>$this->input->post('lead_buy_phone'),
					'lead_buy_email'=>$this->input->post('lead_buy_email'),					
					'lead_rel'=>$this->input->post('lead_rel'),	
					'userid'=>$this->session->userdata("userid"),
					'lead_type'=>'Dealer',
					'status'=>"Active"							
				   );

				if($this->input->post('txtmanufacturers')==""){ $InsertArr['sharedTO'] = "all"; }
				else { $InsertArr['sharedTO'] = $this->input->post('txtmanufacturers'); }

			if($leadid>0){
			
				$this->model->allUpdate('lead_id',$leadid,'user_lead',$InsertArr);
				$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
				redirect('staff/alleads');
			}
			else{ 
			
				$this->model->allInserts('user_lead',$InsertArr);	
			
			#Notifications
			/*if($InsertArr['sharedTO'] == "all")
				{				
				  $sql="Select u.userid,u.email from users u
						LEFT JOIN company_ref cr ON cr.companyID = u.userid
						where u.memtype='Employer' AND cr.userid=".$this->session->userdata("userid");
						
					$ulist=$this->model->allSelects($sql);
					//$this->emailSend('IMPORTANT New Lead added by Pro-Staff Member',$ulist);
						
				}
				
				if($InsertArr['sharedTO'] != "all" && $InsertArr['sharedTO']!=""){
				
					$ulist2 = explode(",",$InsertArr['sharedTO']);
					
					 foreach ($ulist2 as $key => $val) {
					 
						$sql="Select u.userid,u.FirstLastname,u.email from users u where u.memtype='Employer' AND u.userid=".$val;
						$ulist3=$this->model->allSelects($sql);
						
						$body = "<html><body><style>a{color: #2c5d97;}p{line-height: 1.3;margin:10px 0;}body,html{background: #f7f7f7;text-align:center;width:100%}</style><div style='text-align: center;font-family:Helvetica, sans-serif;background: #f7f7f7;padding:20px;margin:0;display:block'><div style='margin: 20px auto 10px auto;border: 1px solid #eee;padding: 20px;width:100%;max-width: 500px;display:block;border-radius: 5px;background: #fff;text-align:center'><img src='http://anglertrack.net/assets/images/angler.png' alt='AnglerTrack' style='padding: 10px;margin: 0 auto 20px 0;'><div style='text-align: left;'><p>Dealer Name: ".$InsertArr['lead_deal_name']."</p><p>Dealer Phone: ".$InsertArr['lead_del_phone']."</p><p>Contact Name: ".$InsertArr['lead_buy_name']."</p><p>Submitted By: ".$this->session->userdata('FirstLastname')."</p></div><p style='text-align: center;font-size: 11px;'>Have questions about <a href='http://anglertrack.net' target='_blank'>AnglerTrack.net</a>? Email our support desk!</p><p style='text-align: center;'><a href='mailto:support@anglertrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;background: #2c5d97;color: #fff;border-radius: 5px;font-size: 11px;margin-bottom: 30px;'>Click To Email Support Desk</a></p></div></body></html>";
						
						//$this->emailSendOneByOne('IMPORTANT New Lead added by Pro-Staff Member',$body, $ulist3[0]['email'], $ulist3[0]['FirstLastname']);
					 }
				}
			*/	
			$this->session->set_flashdata('sucess', 'Lead Added Successfully..!! Add Another? Click Add New.');
			redirect('staff/alleads');															
			
			}	           
		}
		
		if(isset($_POST) && count($_POST)>0){
			
			foreach($_POST as $key=>$val){
						$data[$key]	=	$val;
							}				
						}
			else if($leadid>0){
						
					$sql	=	"select * from user_lead where lead_id=$leadid";
					$TempInfo	=	$this->model->allSelects($sql);
					$leadInfo	=	$TempInfo[0];
																								
			$data['lead_deal_name']	=	$leadInfo['lead_deal_name'];
			$data['lead_del_add']	=	$leadInfo['lead_del_add'];
			$data['lead_del_phone']	=	$leadInfo['lead_del_phone'];
			$data['lead_buy_name']	=	$leadInfo['lead_buy_name'];
			$data['lead_buy_phone']	=	$leadInfo['lead_buy_phone'];		
			$data['lead_buy_email']	=	$leadInfo['lead_buy_email'];		
			$data['lead_rel']		=	$leadInfo['lead_rel'];			
			$data['txtsharedTO']	=	$leadInfo['sharedTO'];
	
			}		
		
		else{
			$data['lead_deal_name']	=	'';
			$data['lead_del_add']	=	'';
			$data['lead_del_phone']	=	'';
			$data['lead_buy_name']	=	'';
			$data['lead_buy_phone']	=	'';			
			$data['lead_buy_email']	=	'';			
			$data['lead_rel']		=	'';		
			$data['txtsharedTO']	= 	'all';	
		}
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
						 LEFT JOIN company_ref cr ON cr.companyID=u.userid
						 WHERE cr.userid=".$this->session->userdata("userid").")
						 UNION
						(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
						 WHERE ups.prostaff_id=".$this->session->userdata("userid").")";	
			   	
				$ulist=$this->model->allSelects($sql);
				$data['ulist']=$ulist;
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);		
		$data['errorreg']= trim(strip_tags(validation_errors()));		

		$data['leadid']=$leadid;

		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/add-lead',$data,true);
		$this->load->view('layout-inner',$data);			
 }

 public function addleadconsumer($leadid=0){
 	
		$this->form_validation->set_rules('lead_consu_firstname', 'First Name', 'required');
		$this->form_validation->set_rules('lead_consu_lastname', 'Last Name', 'required');
															
		if ($this->form_validation->run() == TRUE){					
				$InsertArr=array(
					'lead_consu_firstname'=>$this->input->post('lead_consu_firstname'),
					'lead_consu_lastname'=>$this->input->post('lead_consu_lastname'),
					'lead_deal_name'=>$this->input->post('lead_consu_firstname')." ".$this->input->post('lead_consu_lastname'),
					'lead_del_add'=>$this->input->post('lead_del_add'),
					'lead_consu_city'=>$this->input->post('lead_consu_city'),
					'lead_consu_state'=>$this->input->post('lead_consu_state'),
					'lead_consu_zip'=>$this->input->post('lead_consu_zip'),
					'lead_del_phone'=>$this->input->post('lead_del_phone'),					
					'lead_buy_email'=>$this->input->post('lead_buy_email'),					
					'lead_rel'=>$this->input->post('lead_rel'),	//Notes
					'userid'=>$this->session->userdata("userid"),
					'lead_type'=>'Consumer',
					'status'=>"Active"							
				   );

				if($this->input->post('txtmanufacturers')==""){ $InsertArr['sharedTO'] = "all"; }
				else { $InsertArr['sharedTO'] = $this->input->post('txtmanufacturers'); }

			if($leadid>0)
			{
				$this->model->allUpdate('lead_id',$leadid,'user_lead',$InsertArr);
				$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
				redirect('staff/alleads');

			}
			else
			{ 
			
			$this->model->allInserts('user_lead',$InsertArr);	
			
			#Notifications
			/*if($InsertArr['sharedTO'] == "all")
				{				
				  $sql="Select u.userid,u.email from users u
						LEFT JOIN company_ref cr ON cr.companyID = u.userid
						where u.memtype='Employer' AND cr.userid=".$this->session->userdata("userid");
						
					$ulist=$this->model->allSelects($sql);
					//$this->emailSend('IMPORTANT New Lead added by Pro-Staff Member',$ulist);
						
				}
				
				if($InsertArr['sharedTO'] != "all" && $InsertArr['sharedTO']!=""){
				
					$ulist2 = explode(",",$InsertArr['sharedTO']);
					
					 foreach ($ulist2 as $key => $val) {
					 
						$sql="Select u.userid,u.FirstLastname,u.email from users u where u.memtype='Employer' AND u.userid=".$val;
						$ulist3=$this->model->allSelects($sql);
						
						$body = "<html><body><style>a{color: #2c5d97;}p{line-height: 1.3;margin:10px 0;}body,html{background: #f7f7f7;text-align:center;width:100%}</style><div style='text-align: center;font-family:Helvetica, sans-serif;background: #f7f7f7;padding:20px;margin:0;display:block'><div style='margin: 20px auto 10px auto;border: 1px solid #eee;padding: 20px;width:100%;max-width: 500px;display:block;border-radius: 5px;background: #fff;text-align:center'><img src='http://anglertrack.net/assets/images/angler.png' alt='AnglerTrack' style='padding: 10px;margin: 0 auto 20px 0;'><div style='text-align: left;'><p>Dealer Name: ".$InsertArr['lead_deal_name']."</p><p>Dealer Phone: ".$InsertArr['lead_del_phone']."</p><p>Contact Name: ".$InsertArr['lead_buy_name']."</p><p>Submitted By: ".$this->session->userdata('FirstLastname')."</p></div><p style='text-align: center;font-size: 11px;'>Have questions about <a href='http://anglertrack.net' target='_blank'>AnglerTrack.net</a>? Email our support desk!</p><p style='text-align: center;'><a href='mailto:support@anglertrack.net' target='_blank' style='text-decoration: none;padding: 7px 20px;display: inline-block;background: #2c5d97;color: #fff;border-radius: 5px;font-size: 11px;margin-bottom: 30px;'>Click To Email Support Desk</a></p></div></body></html>";
						
						//$this->emailSendOneByOne('IMPORTANT New Lead added by Pro-Staff Member',$body, $ulist3[0]['email'], $ulist3[0]['FirstLastname']);
					 }
				}
			*/	
			$this->session->set_flashdata('sucess', 'Lead Added Successfully..!! Add Another? Click Add New.');
			redirect('staff/alleads');															
			
			}	           
		}
		
		if(isset($_POST) && count($_POST)>0){
			
			foreach($_POST as $key=>$val){
						$data[$key]	=	$val;
							}				
						}
			else if($leadid>0){
						
					$sql	=	"select * from user_lead where lead_id=$leadid";
					$TempInfo	=	$this->model->allSelects($sql);
					$leadInfo	=	$TempInfo[0];
																								
			$data['lead_consu_firstname'] =	$leadInfo['lead_consu_firstname'];
			$data['lead_consu_lastname']  =	$leadInfo['lead_consu_lastname'];
			$data['lead_del_add']		  =	$leadInfo['lead_del_add'];
			$data['lead_consu_city']	  =	$leadInfo['lead_consu_city'];
			$data['lead_consu_state']	  =	$leadInfo['lead_consu_state'];
			$data['lead_consu_zip']		  =	$leadInfo['lead_consu_zip'];
			$data['lead_del_phone']	=	$leadInfo['lead_del_phone'];
			$data['lead_buy_email']	=	$leadInfo['lead_buy_email'];		
			$data['lead_rel']		=	$leadInfo['lead_rel'];			
			$data['txtsharedTO']	=	$leadInfo['sharedTO'];
	
			}		
		
		else{
			$data['lead_consu_firstname'] =	'';
			$data['lead_consu_lastname']  =	'';
			$data['lead_del_add']		  =	'';
			$data['lead_consu_city']	  =	'';
			$data['lead_consu_state']	  =	'';
			$data['lead_consu_zip']		  =	'';
			$data['lead_del_phone']	=	'';
			$data['lead_buy_email']	=	'';		
			$data['lead_rel']		=	'';		
			$data['txtsharedTO']	= 	'all';	
		}
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
						 LEFT JOIN company_ref cr ON cr.companyID=u.userid
						 WHERE cr.userid=".$this->session->userdata("userid").")
						 UNION
						(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
						 WHERE ups.prostaff_id=".$this->session->userdata("userid").")";	
			   	
				$ulist=$this->model->allSelects($sql);
				$data['ulist']=$ulist;
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);		
		$data['errorreg']= trim(strip_tags(validation_errors()));		

		$data['leadid']=$leadid;

		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/add-lead-consumer',$data,true);
		$this->load->view('layout-inner',$data);			
 }

# Manage Leads
 public function alleads(){
 						
		$sql="Select * from user_lead where userid=".$this->session->userdata("userid");			
		if($_POST['filterBystatus']){
			if($_POST['filterBystatus'] != 'Filter By Status'){
				$sql	.=	" and status='".$_POST['filterBystatus']."'";
			}
		}
		$search_by = "";
        $search_value = "";

				if (@$_POST) {
					$search_by = $_POST['formelement'];
					$search_value = $_POST['formvalue'];
				 } 
		
				if($search_by	==	'orderbydesc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date DESC";
								
				}
		
				if($search_by	==	'orderbyasc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date ASC";
								
				}

		$data["LeadList"]=$this->model->allSelects($sql);
					
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);					

				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);		
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/manageleads',$data,true);
		$this->load->view('layout-inner',$data);			
	}

#Manage Lead Status
 public function leadstatus()
	{
		if(isset($_POST["leadid"],$_POST["status"]))
        {
        
			if($_POST["status"]=="Delete")
			{
			$qstr="delete from user_lead where lead_id=".$_POST["leadid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
			echo "<strong>Deleted Sucessfully..!!</strong>";

			}

			else{
			$qstr="update user_lead set status='".$_POST["status"]."' where lead_id=".$_POST["leadid"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
			 }        
        } 
	}
	
#BULK Lead Status
 public function managebulklead(){
 
        if(isset($_POST["id"],$_POST['action']))
        {
			foreach($_POST["id"] as $key=>$val){
				$id=$val;
				$action=$_POST['action'];
				$qstr="update user_lead set status='".$_POST["action"]."' where lead_id=".$id;
				$this->model->allQueries($qstr); 
			}
//			$this->model->updateBlogstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
        }        
    }

# Manage Events
 public function allEvents(){
 						
			#Events
			$sql = "SELECT * FROM events WHERE ownerid=".$this->session->userdata("userid")." ORDER BY event_start_date DESC ";
			$EventsInfo = $this->model->allSelects($sql);
	
			$data["eventslist"] = $EventsInfo;

			$data['loginUrl'] = $this->facebook->getLoginUrl(array(
              'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
              'redirect_uri' => site_url() . '/staff/facebook_redirect/'));
      $data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
                                    
      $sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
      $data["fblink"]=$this->model->allSelects($sql);
            
      $sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
      $data["twlink"]=$this->model->allSelects($sql);
			      
      $sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
      $data["instalink"]=$this->model->allSelects($sql);	
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
      $data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			$data['navigation']=$this->load->view('staff/staff-navigation','',true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manageevents',$data,true);
			$this->load->view('layout-inner',$data);
	}

# ADD Event
 public function addevent($eventid=0) {

        $this->form_validation->set_rules('txteventtitle', 'Event Title is ', 'required');
        //$this->form_validation->set_rules('txtcontactperson', 'Contact Person Name', 'required');
        $this->form_validation->set_rules('txteventstartdate', 'Event Start Date?', 'required');
				$this->form_validation->set_rules('txteventdescp', 'Event Description?', 'required');
				$this->form_validation->set_rules('attendance_count', ' People in Attendance', 'numeric');
        //$this->form_validation->set_rules('event_attachment', 'Document', 'callback_validate_event_attachment');
		
        if ($this->form_validation->run() == TRUE) {
		
					 $event = array(
									'eventtitle' => $this->input->post('txteventtitle'),
									'description' => $this->input->post('txteventdescp'),
									'venueaddress' => $this->input->post('txteventvenueaddress'),
									'contactperson' => $this->input->post('txtcontactperson'),
									'contactphone' => $this->input->post('txtcontactno'),
									'event_state' => "",
									'event_start_date' => date('Y-m-d', strtotime($this->input->post('txteventstartdate'))),
									'event_start_time' => $this->input->post('txteventstarttime'),
									'event_end_date' => date('Y-m-d', strtotime($this->input->post('txteventenddate'))),
									'sharedTO' => $this->input->post('sharedTO'),
									'ownerid' => $this->session->userdata("userid"),
									'attendance_count' => $this->input->post('attendance_count'),
									'status' => "Active"
							);

				/*if($this->input->post('txtmanufacturers')==""){ $event['sharedTO'] = "all"; }
				else { $event['sharedTO'] = $this->input->post('txtmanufacturers'); }*/

				$event_attachment = ''; $event_attachment1 = ''; $event_attachment2 = ''; $event_attachment3 = '';
				
				if ($_FILES['event_attachment']['error'] == 0) {
					$fileName = $_FILES['event_attachment']['name'];
					$file = 'event_attachment';
					$upload = uploadFile($file, '*', 'assets/upload/event_attachment', $fileName);
					if ($upload['success']) {
						$event_attachment = $upload['path'];
					}
        }
        
				$event['event_attachment'] = $event_attachment;

				if ($_FILES['event_attachment1']['error'] == 0) {
					$fileName = $_FILES['event_attachment1']['name'];
					$file = 'event_attachment1';
					$upload = uploadFile($file, '*', 'assets/upload/event_attachment', $fileName);
					if ($upload['success']) {
						$event_attachment1 = $upload['path'];
					}
        }
        
				$event['event_attachment1'] = $event_attachment1;

				if ($_FILES['event_attachment2']['error'] == 0) {
					$fileName = $_FILES['event_attachment2']['name'];
					$file = 'event_attachment2';
					$upload = uploadFile($file, '*', 'assets/upload/event_attachment', $fileName);
					if ($upload['success']) {
						$event_attachment1 = $upload['path'];
					}
        }
        
				$event['event_attachment2'] = $event_attachment2;

				if ($_FILES['event_attachment3']['error'] == 0) {
					$fileName = $_FILES['event_attachment3']['name'];
					$file = 'event_attachment3';
					$upload = uploadFile($file, '*', 'assets/upload/event_attachment', $fileName);
					if ($upload['success']) {
						$event_attachment3 = $upload['path'];
					}
        }
        
				$event['event_attachment3'] = $event_attachment3;
			
			if($eventid>0){
				$this->model->allUpdate('eventid',$eventid,'events',$event);
				$this->session->set_flashdata('sucess', 'Event Updated Successfully..!!');
				redirect('staff/allevents');
			}
			else{
        $this->model->allInserts('events', $event);

			#For Notification
/*			$sql="Select * from users u, company_ref cr where u.userid=cr.userid and cr.companyID=".$this->session->userdata("userid");
			$ulist=$this->model->allSelects($sql);
			$this->emailSend('New Event Added by Employer',$ulist);*/

        $this->session->set_flashdata('sucess', 'Event Added Successfully..!! Add Another? Click Add New.');
        redirect('staff/allevents');
			}		
    }  
		else {
		 
				if(isset($_POST) && count($_POST)>0){
					foreach($_POST as $key=>$val){
							$data[$key]	=	$val;
							}				
						}
						else if($eventid>0){
						
							$sql	=	"SELECT * FROM events WHERE eventid=$eventid";
							$TempInfo	=	$this->model->allSelects($sql);
							$eventInfo	=	$TempInfo[0];				
							$data['txteventtitle']				=	$eventInfo['eventtitle'];
							$data['txtcontactperson']			=	$eventInfo['contactperson'];
							$data['txteventvenueaddress']	=	$eventInfo['venueaddress'];
							$data['txtcontactno']					=	$eventInfo['contactphone'];
							$data['txteventdescp']				=	$eventInfo['description'];
							$data['txteventstartdate']		=	$eventInfo['event_start_date'];
							$data['txteventenddate']			=	$eventInfo['event_end_date'];
							$data['txteventstarttime']		=	$eventInfo['event_start_time'];
							$data['attendance_count']	 		=	$eventInfo['attendance_count'];
							$data['sharedTO']	 						=	$eventInfo['sharedTO'];
							$data['event_attachment']	 		=	$eventInfo['event_attachment'];
							$data['event_attachment1']	 	=	$eventInfo['event_attachment1'];
							$data['event_attachment2']	 	=	$eventInfo['event_attachment2'];
							$data['event_attachment3']	 	=	$eventInfo['event_attachment3'];
						}		
						else{
							$data['txteventtitle']				=	'';
							$data['txtcontactperson']			=	'';
							$data['txteventvenueaddress']	=	'';
							$data['txtcontactno']					=	'';
							$data['txteventdescp']				=	'';
							$data['txteventstartdate']		=	'';
							$data['txteventenddate']			=	'';
							$data['txteventstarttime']		=	'';
							$data['attendance_count']	 		=	'';
							$data['sharedTO']	 						=	'';
							$data['event_attachment']	 		=	'';
							$data['event_attachment1']	 	=	'';
							$data['event_attachment2']	 	=	'';
							$data['event_attachment3']	 	=	'';

						}	
        }
		
		    $data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
        $data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);

			$sql = "(Select cr.companyID AS companyID, u.FirstLastname as FirstLastname from users u
			 		 		 LEFT JOIN company_ref cr ON cr.companyID=u.userid
					 		 WHERE cr.userid=".$this->session->userdata("userid").")
					 		 UNION
							(Select DISTINCT ups.sponsorCSid as companyID, ups.sponsorname as FirstLastname from user_prostaff_sponsors ups
					 		 WHERE ups.prostaff_id=".$this->session->userdata("userid").")";	

			$ulist=$this->model->allSelects($sql);
			$data['ulist']=$ulist;

      $data['errorreg'] = trim(strip_tags(validation_errors()));
			$data['eventid']=$eventid;

				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
				));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
			
      $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
			$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
      $data['content'] = $this->load->view('staff/add-events', $data, true);
      $this->load->view('layout-inner', $data);
 }
  
#Validate image extension	
	function validate_event_attachment(){	
		if($_FILES['event_attachment']['error'] == 0){
			$fileEle	=	$_FILES['event_attachment'];
			$extension	=	strtolower(end(explode('.',$fileEle['name'])));
			$validExtension	=	GetMsgAttachmentext();
			if(in_array($extension,$validExtension)){
				return true;
			}
			else{
				$this->form_validation->set_message('validate_event_attachment', 'Please upload valid attachment file');
				return false;
			}
		}
		else{
			return true;
		}
	}
    
#Manage Event Status
 public function eventstatus()
	{
        if (isset($_POST["eventid"], $_POST["status"])) {
		
			if($_POST["status"] == 'Remove'){
				$sql="Select * from events where eventid=".$_POST["eventid"];			
				$eventdetails=$this->model->allSelects($sql);
				$eventdetails	=	$eventdetails[0];
				@unlink($eventdetails['']);
				$qstr="delete from  events where eventid=".$_POST["eventid"];
			}
			else{
				$qstr="update events set status='".$_POST["status"]."' where eventid=".$_POST["eventid"];
			}
			$this->model->allQueries($qstr); 
			if($_POST["status"] == 'Remove'){
				$this->session->set_flashdata('sucess', 'Event Removed Sucessfully..!!');             			
			}
			else{
				$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             			
			}
#          $qstr = "update events set status='" . $_POST["status"] . "' where eventid=" . $_POST["eventid"];
#          $this->model->allQueries($qstr);
#          $this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');
            echo "<strong>Status Updated Sucessfully..!!</strong>";
        }
	}

#BULK Event Status
	public function managebulkevents()
    {
        if(isset($_POST["id"],$_POST['action']))
        {
            $id=$_POST["id"];
            $action=$_POST['action'];
           	
			$this->model->updateeventstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
            
        }        
    }

#Filter Events
 public function filterEvents()
 {
 
 	if(isset($_POST["filterBystatus"]))
      {
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
	  
	  		if($_POST["filterBystatus"]!="Filter By Status")
			{
				
				$data["eventslist"]=$this->model->getEvents($this->session->userdata("userid")." and status='".$_POST["filterBystatus"]."'");
				
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				$data['navigation']=$this->load->view('staff/staff-navigation','',true);
			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/manageevents',$data,true);
				$this->load->view('layout-inner',$data);
			}
		   else
			{
			
				$data["eventslist"]=$this->model->getEvents($this->session->userdata("userid"));
				
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				$data['navigation']=$this->load->view('staff/staff-navigation','',true);
			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/manageevents',$data,true);
				$this->load->view('layout-inner',$data);
			}
	  }
 }

# ViewEvents
 public function viewevent(){
	
		$eventid = $_POST['eventid'];
		$sql = "Select * from events where eventid=" . $eventid;
        $data["eventdetails"] = $this->model->allSelects($sql);
        if (count($data["eventdetails"]) == 0) {
            redirect("staff");
        }
		$this->load->view('staff/view-events',$data);
	}

# ViewNews
 public function viewNews(){
	
		$newsid = $_POST['newsid'];
		$sql = "Select * from news where newsid=" . $newsid;
        $data["newsdetails"] = $this->model->allSelects($sql);
        if (count($data["newsdetails"]) == 0) {
            redirect("staff");
        }
		$this->load->view('staff/view-news',$data);

	}

# For Keyword SearchReport
	function searchkeyword(){									
		if($_POST){
			$search_from_date	=	$_POST['search_from_date'];
			$search_to_date		=	$_POST['search_to_date'];
			$search_keyword		=	$_POST['search_keyword'];
			$this->session->set_userdata(array('search_from_date'=>$search_from_date));
			$this->session->set_userdata(array('search_to_date'=>$search_to_date));
			$this->session->set_userdata(array('search_keyword'=>$search_keyword));
		}
		else{
			$search_from_date	=	$this->session->userdata('search_from_date');
			$search_to_date		=	$this->session->userdata('search_to_date');
			$search_keyword		=	$this->session->userdata('search_keyword');
		}
		$search_select_staff=	$this->session->userdata("userid");
		
		if($search_from_date == '' && $search_to_date == ''){
			$search_from_date	=	date("Y-m-d",strtotime("-7 days"))." 00:00:00";
			$search_to_date		=   date("Y-m-d")." 23:59:59";
		}
		else if($search_from_date!='' && $search_to_date==''){
			$temp_date			=	$search_from_date;
			$search_from_date	=	date('Y-m-d',strtotime($temp_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($temp_date))." 23:59:59";			
		}
		else if($search_to_date != '' && $search_from_date==''){
			$temp_date			=	$search_to_date;
			$search_from_date	=	date('Y-m-d',strtotime($temp_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($temp_date))." 23:59:59";			
		}
		else{
			$search_from_date	=	date('Y-m-d',strtotime($search_from_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($search_to_date))." 23:59:59";
		}				
		
		#Fetching out Staff information.
		$sql		=	"select * from users where memtype='Staff'";
		if($search_select_staff){
			$sql	.=	" and userid=$search_select_staff";
		}
		$tempUserInfo	=	$this->model->allSelects($sql);
		$StaffInfo		=	array();
		if($tempUserInfo){
			foreach($tempUserInfo as $key=>$val){
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				
				#Tournaments Creation Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				$tempInfo	=	$this->model->allSelects($sql);	
				$tempretArr["tournaments_creation_total"]	=	$tempInfo[0]['rec_total'];
				
				#Tournaments 1st place Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE tfinishedplace=1 and staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);			
				$tempretArr["tournaments_fist_place"]=$tempInfo[0]['rec_total'];	
				
				#For Blog Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_blog WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["blog_total_count"]=$tempInfo[0]['rec_total'];
				
				#For Leads Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["leads_total_count"]=$tempInfo[0]['rec_total'];
				
				#For Print Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_print WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["print_total_count"]=$tempInfo[0]['rec_total'];
				
				#For Events Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["event_total_count"]=$tempInfo[0]['rec_total'];
				
				#Facebook Related Content				
				#-------------------------Facebook Feed Count
				$sql="select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Feed Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];
				
				#-------------------------Facebook Photo Count
				$sql="select IFNULL(count(*),0) as rec_total from user_photo where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Photo Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_photo  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_like_count"]=$tempInfo[0]['rec_total'];	
				
				#-------------------------Facebook Video Count
				$sql="select IFNULL(count(*),0) as rec_total from user_video where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_video_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Video Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_video  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_video_like_count"]=$tempInfo[0]['rec_total'];												
												
				#Twitter Related Content
				#-------------------------Twitter Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}									  
				$twitter_qry	=	$sql;
				$sql			.=	$append_qry;							
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"]=$tempInfo[0]['rec_total'];
				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];
				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					
				
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				$instakeywords	=	$search_keyword;		
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$instragram_qry	=	$sql;								  
				$sql	=	$sql.$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];
				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		
				$instakeywords	=	$search_keyword;		
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$instragram_qry	=	$sql;								  
				$sql	=	$sql.$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];	
				
				$StaffInfo[]						=	$tempretArr;
			}
		}		
								
		$fb_query	=	$twitter_qry	=	$instragram_qry	=	$tournament_qry		=	$blog_qry	=	'';					
		#Facebook Related general query
		$sql="select IFNULL(count(*),0) as rec_total from user_feed LEFT JOIN company_ref cr on cr.userid=user_feed.user_id
			  LEFT JOIN users ON users.userid = user_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_feed.user_id 
			  where um.social_type='facebook'";
			
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_feed.user_id=$search_select_staff";
		}
		$fb_query	=	$sql;							
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_feed_total']	=	$tempInfo[0]['rec_total'];
		#Facebook Feed Like Total
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed LEFT JOIN company_ref cr on cr.userid=user_feed.user_id
			  LEFT JOIN users ON users.userid = user_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_feed.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		

		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_feed.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_like_total']	=	$tempInfo[0]['rec_total'];
		
		#Facebook Photo General Query
		$sql="select IFNULL(count(*),0) as rec_total from user_photo LEFT JOIN company_ref cr on cr.userid=user_photo.user_id
			  LEFT JOIN users ON users.userid = user_photo.user_id
			  LEFT JOIN user_media um ON um.userid=user_photo.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_photo.user_id=$search_select_staff";
		}							
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_photo_total']	=	$tempInfo[0]['rec_total'];
		#Facebook photo Like Total
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_photo LEFT JOIN company_ref cr on cr.userid=user_photo.user_id
			  LEFT JOIN users ON users.userid = user_photo.user_id
			  LEFT JOIN user_media um ON um.userid=user_photo.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_photo.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_photo_like_total']	=	$tempInfo[0]['rec_total'];
		
		#Facebook Video General Query
		$sql="select IFNULL(count(*),0) as rec_total from user_video LEFT JOIN company_ref cr on cr.userid=user_video.user_id
			  LEFT JOIN users ON users.userid = user_video.user_id
			  LEFT JOIN user_media um ON um.userid=user_video.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_video.user_id=$search_select_staff";
		}							
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_video_total']	=	$tempInfo[0]['rec_total'];
		#Facebook Video Like Total
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_video LEFT JOIN company_ref cr on cr.userid=user_video.user_id
			  LEFT JOIN users ON users.userid = user_video.user_id
			  LEFT JOIN user_media um ON um.userid=user_video.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_video.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$data['facebook_video_like_total']	=	$tempInfo[0]['rec_total'];		
								
		#Twitter Related General Query
		$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$twitter_qry	=	$sql;						  
		$tempInfo	=	$this->model->allSelects($sql);
		$data['twitter_feed_total']	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$tempInfo	=	$this->model->allSelects($sql);
		$data['twitter_retweet_total']	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$tempInfo	=	$this->model->allSelects($sql);
		$data['twitter_favorite_total']	=	$tempInfo[0]['rec_total'];	
		
		#Instragram Related general query
		$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed LEFT JOIN company_ref cr ON cr.userid = user_instagram_feed.user_id
			  LEFT JOIN users ON users.userid = user_instagram_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_instagram_feed.user_id
			  WHERE um.social_type='instagram'";		
		$instakeywords	=	$search_keyword;		
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}				
		if($search_select_staff>0){
			$sql .= " and user_instagram_feed.user_id=$search_select_staff";
		}				
		$instragram_qry	=	$sql;								  
		$tempInfo	=	$this->model->allSelects($sql);
		$data['instragram_feed_total']	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed LEFT JOIN company_ref cr ON cr.userid = user_instagram_feed.user_id
			  LEFT JOIN users ON users.userid = user_instagram_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_instagram_feed.user_id
			  WHERE um.social_type='instagram'";		
		$instakeywords	=	$search_keyword;		
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}				
		if($search_select_staff>0){
			$sql .= " and user_instagram_feed.user_id=$search_select_staff";
		}										  
		$tempInfo	=	$this->model->allSelects($sql);
		$data['instragram_like_total']	=	$tempInfo[0]['rec_total'];	
		
		#Tournament General query
		$tournament_qry="SELECT IFNULL(count(*),0) as rec_total FROM tournaments LEFT JOIN company_ref cr ON cr.userid = tournaments.staffid
				  LEFT JOIN users ON users.userid = tournaments.staffid
				  WHERE tfinishedplace=1 and tournaments.status='Active' ";
		if($search_select_staff>0){
			$tournament_qry .= " and tournaments.staffid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($tournament_qry);
		$data['tournament_1stplace_total']	=	$tempInfo[0]['rec_total'];		

		
		$tournament_qry="SELECT IFNULL(count(*),0) as rec_total FROM tournaments LEFT JOIN company_ref cr ON cr.userid = tournaments.staffid
				  LEFT JOIN users ON users.userid = tournaments.staffid
				  WHERE 1  and tournaments.status='Active' ";		
		if($search_select_staff>0){
			$tournament_qry .= " and tournaments.staffid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($tournament_qry);
		$data['tournament_total']	=	$tempInfo[0]['rec_total'];		
				  
		#bog General query
		$blog_qry="SELECT IFNULL(count(*),0) as rec_total FROM user_blog LEFT JOIN company_ref cr ON cr.userid = user_blog.userid
				  LEFT JOIN users ON users.userid = user_blog.userid
				  WHERE 1 and user_blog.status='Active' ";		  								  
		if($search_select_staff>0){
			$blog_qry .= " and user_blog.userid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($blog_qry);
		$data['blog_total']	=	$tempInfo[0]['rec_total'];
		
		#Event General query
		$event_qry="SELECT IFNULL(count(*),0) as rec_total FROM events LEFT JOIN company_ref cr ON cr.userid = 	ownerid
				  LEFT JOIN users ON users.userid = ownerid
				  WHERE 1 and events.status='Active' ";		  								  
		if($search_select_staff>0){
			$event_qry .= " and ownerid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($event_qry);
		$data['event_total']	=	$tempInfo[0]['rec_total'];
		
		#print General query
		$print_qry="SELECT IFNULL(count(*),0) as rec_total FROM user_print LEFT JOIN company_ref cr ON cr.userid = user_print.userid
				  LEFT JOIN users ON users.userid = user_print.userid
				  WHERE 1 and user_print.status='Active' ";		  								  
		if($search_select_staff>0){
			$print_qry .= " and user_print.userid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($print_qry);
		$data['print_total']	=	$tempInfo[0]['rec_total'];
		
		#Leads General query
		$lead_qry="SELECT IFNULL(count(*),0) as rec_total FROM user_lead LEFT JOIN company_ref cr ON cr.userid = user_lead.userid
				  LEFT JOIN users ON users.userid = user_lead.userid
				  WHERE 1 and user_lead.status='Active' ";		  								  
		if($search_select_staff>0){
			$lead_qry .= " and user_lead.userid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($lead_qry);
		$data['lead_total']	=	$tempInfo[0]['rec_total'];
		
		if($StaffInfo){
			usort($StaffInfo, 'compareName');
		}
		if($SearchStaffInfo){
			usort($SearchStaffInfo, 'compareName');
		}		
				
		#Calculating Data base on the date range
		$FacebookFeedHistory	=	array();
		$TwitterFeedHistory		=	array();
		$InstragramFeedHistory	=	array();
		$DisplayDaysHist		=	array();
		$TournamentHistory		=	array();
		$BlogHistory			=	array();		
		$date_range	=	GetDays($search_from_date, $search_to_date);
	
		foreach($date_range as $key=>$val){		
			$tempInfo	=	array();
			$start_date		=	date('Y-m-d',strtotime($val)).' 00:00:00';
			$end_date		=	date('Y-m-d',strtotime($val)).' 23:59:59';
			$date			=	date('d',strtotime($val));
			$sql=	$fb_query." and created_time>= '$start_date' and created_time <= '$end_date' ";
			$tempInfo	=	$this->model->allSelects($sql);
			$FacebookFeedHistory[]	=	$tempInfo[0]['rec_total'];

			$sql=	$twitter_qry." and created_at>= '$start_date' and created_at <= '$end_date'";			
			$tempInfo	=	$this->model->allSelects($sql);
			$TwitterFeedHistory[]	=	$tempInfo[0]['rec_total'];
			
			$sql=	$instragram_qry." and created_time>= '".strtotime($start_date)."' and created_time <= '".strtotime($end_date)."'";
			$tempInfo	=	$this->model->allSelects($sql);
			$InstragramFeedHistory[]	=	$tempInfo[0]['rec_total'];
			
			$sql=	$tournament_qry." and tournamentdate>= '$start_date' and tournamentdate <= '$end_date'";
			$tempInfo	=	$this->model->allSelects($sql);
			$TournamentHistory[]	=	$tempInfo[0]['rec_total'];			

			$sql=	$blog_qry." and posted_date>= '$start_date' and posted_date <= '$end_date'";
			$tempInfo	=	$this->model->allSelects($sql);
			$BlogHistory[]	=	$tempInfo[0]['rec_total'];			
			$DisplayDaysHist[]	=	strtotime($start_date);
		}					
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation','',true);
		$data['FacebookFeedHistory']	=	$FacebookFeedHistory;
		$data['TwitterFeedHistory']		=	$TwitterFeedHistory;
		$data['InstragramFeedHistory']	=	$InstragramFeedHistory;		
		$data['TournamentHistory']		=	$TournamentHistory;
		$data['BlogHistory']			=	$BlogHistory;		
		$data['DisplayDaysHist']		=	$DisplayDaysHist;					
		$data['search_from_date']		=	date('Y-m-d',strtotime($search_from_date));
		$data['search_to_date']			=	date('Y-m-d',strtotime($search_to_date));		
		$data['search_select_staff']	=	$search_select_staff;						
		$data['search_keyword']			=	$search_keyword;								
		$data['StaffInfo']				=	$StaffInfo;
		$data['SearchStaffInfo']		=	$SearchStaffInfo;
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-reportbykeyword',$data,true);
		$this->load->view('layout-inner',$data); 
	}

# For Export result by date
	function exportresultbykeyword(){	
		
		$search_from_date	=	$this->session->userdata('search_from_date');
		$search_to_date		=	$this->session->userdata('search_to_date');
		$search_keyword		=	$this->session->userdata('search_keyword');
		$search_select_staff=	$this->session->userdata("userid");;
		
		
		if($search_from_date == '' && $search_to_date == ''){
			$search_from_date	=	date("Y-m-d",strtotime("-7 days"))." 00:00:00";
			$search_to_date		=   date("Y-m-d")." 23:59:59";
		}
		else if($search_from_date!='' && $search_to_date==''){
			$temp_date			=	$search_from_date;
			$search_from_date	=	date('Y-m-d',strtotime($temp_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($temp_date))." 23:59:59";			
		}
		else if($search_to_date != '' && $search_from_date==''){
			$temp_date			=	$search_to_date;
			$search_from_date	=	date('Y-m-d',strtotime($temp_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($temp_date))." 23:59:59";			
		}
		else{
			$search_from_date	=	date('Y-m-d',strtotime($search_from_date))." 00:00:00";
			$search_to_date		=	date('Y-m-d',strtotime($search_to_date))." 23:59:59";
		}				
		
		#For Search Staff
		$sql		=	"select * from users where memtype='Staff'";				
		$SearchStaffInfo	=	$this->model->allSelects($sql);
		#Fetching out Staff information.
		$sql		=	"select * from users where memtype='Staff'";
		if($search_select_staff){
			$sql	.=	" and userid=$search_select_staff";
		}
		$tempUserInfo	=	$this->model->allSelects($sql);
		$StaffInfo		=	array();
		if($tempUserInfo){
			foreach($tempUserInfo as $key=>$val){
				$tempretArr	=	array();
				$tempretArr['userid']			=	$val['userid'];
				$tempretArr['FirstLastname']	=	$val['FirstLastname'];
				$user_id						=	$val['userid'];				
				#Tournaments Creation Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}
				$tempInfo	=	$this->model->allSelects($sql);	
				$tempretArr["tournaments_creation_total"]	=	$tempInfo[0]['rec_total'];
				
				#Tournaments 1st place Related Data				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM tournaments WHERE tfinishedplace=1 and staffid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and tournamentdate >= '$search_from_date' and tournamentdate<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);			
				$tempretArr["tournaments_fist_place"]=$tempInfo[0]['rec_total'];	
				
				#For Blog Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_blog WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["blog_total_count"]=$tempInfo[0]['rec_total'];
				
				
				#For Leads Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_lead WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["leads_total_count"]=$tempInfo[0]['rec_total'];
				
				#For Print Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_print WHERE status='Active' and userid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["print_total_count"]=$tempInfo[0]['rec_total'];
				
				#For Events Creation
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM events WHERE status='Active' and ownerid=$user_id";
				if($search_from_date && $search_to_date){
					$sql	.=	" and posted_date >= '$search_from_date' and posted_date<='$search_to_date'";
				}		
				$tempInfo	=	$this->model->allSelects($sql);				
				$tempretArr["event_total_count"]=$tempInfo[0]['rec_total'];
				
				#Facebook Related Content				
				#-------------------------Facebook Feed Count
				$sql="select IFNULL(count(*),0) as rec_total from user_feed where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];
				#-------------------------Facebook Photo Count
				$sql="select IFNULL(count(*),0) as rec_total from user_photo where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Photo Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_photo  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_photo_like_count"]=$tempInfo[0]['rec_total'];	
				
				#-------------------------Facebook Video Count
				$sql="select IFNULL(count(*),0) as rec_total from user_video where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}
				$fb_query	=	$sql;					
				$sql		.=	$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_video_count"]=$tempInfo[0]['rec_total'];				
				#-------------------------Facebook Video Like Count
				$sql="select IFNULL(sum(likes_count),0) as rec_total from user_video  where user_id=$user_id";
				$fbkeywords	=	$search_keyword;		
				if($fbkeywords){
					$keywordArr	=	explode(',',$fbkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}

					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_video_like_count"]=$tempInfo[0]['rec_total'];					
				#Twitter Related Content
				#-------------------------Twitter Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}									  
				$twitter_qry	=	$sql;
				$sql			.=	$append_qry;							
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"]=$tempInfo[0]['rec_total'];
				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];
				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						
				$twtkeywords	=	$search_keyword;		
				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '$search_from_date' and created_at <= '$search_to_date' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					
				
				#Instragram Related Content
				#-------------------------Instragram Feed Count			
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		


				$instakeywords	=	$search_keyword;		
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$instragram_qry	=	$sql;								  
				$sql	=	$sql.$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];
				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		
				$instakeywords	=	$search_keyword;		
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$append_qry	.=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
				}
				$instragram_qry	=	$sql;								  
				$sql	=	$sql.$append_qry;
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];										
				$StaffInfo[]						=	$tempretArr;
			}
		}		
								
		$fb_query	=	$twitter_qry	=	$instragram_qry	=	$tournament_qry		=	$blog_qry	=	'';					
		#Facebook Related general query
		$sql="select IFNULL(count(*),0) as rec_total from user_feed LEFT JOIN company_ref cr on cr.userid=user_feed.user_id
			  LEFT JOIN users ON users.userid = user_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_feed.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_feed.user_id=$search_select_staff";
		}
		$fb_query	=	$sql;							
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_feed_total	=	$tempInfo[0]['rec_total'];
		
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_feed LEFT JOIN company_ref cr on cr.userid=user_feed.user_id
			  LEFT JOIN users ON users.userid = user_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_feed.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (story like '$val%' or story like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_feed.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_like_total	=	$tempInfo[0]['rec_total'];
		
		#Facebook Photo General Query
		$sql="select IFNULL(count(*),0) as rec_total from user_photo LEFT JOIN company_ref cr on cr.userid=user_photo.user_id
			  LEFT JOIN users ON users.userid = user_photo.user_id
			  LEFT JOIN user_media um ON um.userid=user_photo.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_photo.user_id=$search_select_staff";
		}							
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_photo_total	=	$tempInfo[0]['rec_total'];
		#Facebook photo Like Total
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_photo LEFT JOIN company_ref cr on cr.userid=user_photo.user_id
			  LEFT JOIN users ON users.userid = user_photo.user_id
			  LEFT JOIN user_media um ON um.userid=user_photo.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_photo.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_photo_like_total	=	$tempInfo[0]['rec_total'];
		
		#Facebook Video General Query
		$sql="select IFNULL(count(*),0) as rec_total from user_video LEFT JOIN company_ref cr on cr.userid=user_video.user_id
			  LEFT JOIN users ON users.userid = user_video.user_id
			  LEFT JOIN user_media um ON um.userid=user_video.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_video.user_id=$search_select_staff";
		}							
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_video_total	=	$tempInfo[0]['rec_total'];
		#Facebook Video Like Total
		$sql="select IFNULL(sum(likes_count),0) as rec_total from user_video LEFT JOIN company_ref cr on cr.userid=user_video.user_id
			  LEFT JOIN users ON users.userid = user_video.user_id
			  LEFT JOIN user_media um ON um.userid=user_video.user_id 
			  where um.social_type='facebook'";
		$fbkeywords	=	$search_keyword;		
		if($fbkeywords){
			$keywordArr	=	explode(',',$fbkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}
		if($search_select_staff>0){
			$sql .= " and user_video.user_id=$search_select_staff";
		}								
		$tempInfo	=	$this->model->allSelects($sql);
		$facebook_video_like_total	=	$tempInfo[0]['rec_total'];
				
		#Twitter Related General Query
		$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$twitter_qry	=	$sql;						  
		$tempInfo	=	$this->model->allSelects($sql);
		$twitter_feed_total	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$tempInfo	=	$this->model->allSelects($sql);
		$twitter_retweet_total	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets LEFT JOIN company_ref cr ON cr.userid = user_tweets.user_id
			  LEFT JOIN users ON users.userid = user_tweets.user_id
			  LEFT JOIN user_media um ON um.userid=user_tweets.user_id
			  WHERE um.social_type='twitter'";						
		$twtkeywords	=	$search_keyword;		
		if($twtkeywords){
			$keywordArr	=	explode(',',$twtkeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}		
		if($search_select_staff>0){
			$sql .= " and user_tweets.user_id=$search_select_staff";
		}						  
		$tempInfo	=	$this->model->allSelects($sql);
		$twitter_favorite_total	=	$tempInfo[0]['rec_total'];	
		
		#Instragram Related general query
		$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed LEFT JOIN company_ref cr ON cr.userid = user_instagram_feed.user_id
			  LEFT JOIN users ON users.userid = user_instagram_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_instagram_feed.user_id
			  WHERE um.social_type='instagram'";		
		$instakeywords	=	$search_keyword;		
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}				
		if($search_select_staff>0){
			$sql .= " and user_instagram_feed.user_id=$search_select_staff";
		}				
		$instragram_qry	=	$sql;								  
		$tempInfo	=	$this->model->allSelects($sql);
		$instragram_feed_total	=	$tempInfo[0]['rec_total'];
		
		$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed LEFT JOIN company_ref cr ON cr.userid = user_instagram_feed.user_id
			  LEFT JOIN users ON users.userid = user_instagram_feed.user_id
			  LEFT JOIN user_media um ON um.userid=user_instagram_feed.user_id
			  WHERE um.social_type='instagram'";		
		$instakeywords	=	$search_keyword;		
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}				
		if($search_select_staff>0){
			$sql .= " and user_instagram_feed.user_id=$search_select_staff";
		}										  
		$tempInfo	=	$this->model->allSelects($sql);
		$instragram_like_total	=	$tempInfo[0]['rec_total'];	
		
		#Tournament General query
		$tournament_qry="SELECT IFNULL(count(*),0) as rec_total FROM tournaments LEFT JOIN company_ref cr ON cr.userid = tournaments.staffid
				  LEFT JOIN users ON users.userid = tournaments.staffid
				  WHERE tfinishedplace=1  and tournaments.status='Active' ";
		if($search_select_staff>0){
			$tournament_qry .= " and tournaments.staffid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($tournament_qry);
		$tournament_1stplace_total	=	$tempInfo[0]['rec_total'];		

		
		$tournament_qry="SELECT IFNULL(count(*),0) as rec_total FROM tournaments LEFT JOIN company_ref cr ON cr.userid = tournaments.staffid
				  LEFT JOIN users ON users.userid = tournaments.staffid
				  WHERE 1  and tournaments.status='Active' ";		
		if($search_select_staff>0){
			$tournament_qry .= " and tournaments.staffid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($tournament_qry);
		$tournament_total	=	$tempInfo[0]['rec_total'];		
				  
		#bog General query
		$blog_qry="SELECT IFNULL(count(*),0) as rec_total FROM user_blog LEFT JOIN company_ref cr ON cr.userid = user_blog.userid
				  LEFT JOIN users ON users.userid = user_blog.userid
				  WHERE 1 and user_blog.status='Active' ";		  								  
		if($search_select_staff>0){
			$blog_qry .= " and user_blog.userid=$search_select_staff";
		}
		$tempInfo	=	$this->model->allSelects($blog_qry);
		$blog_total	=	$tempInfo[0]['rec_total'];
		
		if($StaffInfo){
			usort($StaffInfo, 'compareName');
		}
		if($SearchStaffInfo){
			usort($SearchStaffInfo, 'compareName');
		}		
		
		if($search_select_staff>0){
			$sql=	"select * from users where userid=$search_select_staff";
			$SearchStaffInfo	=	$this->model->allSelects($sql);			
			$staff_member	=	$SearchStaffInfo[0]['FirstLastname'];
		}
		else{
			$staff_member	=	'All Members';
		$all_activity_total	= 0;
		
		$CsvData	=	array();
		$CsvData[]	=	array('Exported Result');	
		$CsvData[]	=	
						array(
							'From Date',
							$search_from_date,
							'To Date',
							$search_to_date,
							"For $staff_member"
							);
		$CsvData[]	=	array();		
		$DispData	=	array(
								'Staff',
								'Facebook Posts',
								'Facebook Likes',								
								'Facebook Photo',
								'Facebook Photo Likes',
								'Facebook Video',
								'Facebook Video Likes',								
								'Twitter Feeds',
								'Twitter Retweet',
								'Twitter Favorite',																
								'InstraGram Feeds',
								'InstraGram Likes',
								'Tournaments Creation',								
								'Blog Creation',
								'Events',
								'Leads',
								'Print',																																								
								'Total Activity'									
							);
		$CsvData[]	=	$DispData;				
		/*$DispData	=	array(
								'Cumulative Total',
								$facebook_feed_total,
								$facebook_like_total,								
								$facebook_photo_total,
								$facebook_photo_like_total,
								$facebook_video_total,
								$facebook_video_like_total,								
								$twitter_feed_total,
								$twitter_retweet_total,
								$twitter_favorite_total,
								$instragram_feed_total,
								$instragram_like_total,
								$tournament_total,
								$tournament_1stplace_total,
								$blog_total,
								$all_activity_total
							);
		$CsvData[]	=	$DispData;		*/
		if($StaffInfo){
			 $grand_fb_feed	=	$grand_fb_like  =	$grand_fb_photo_feed	=	$grand_fb_photo_like	=  $grand_fb_video_feed	=	$grand_fb_video_like	=	$grand_tw_feed	= $grand_tw_retweet	=	$grand_tw_fav	=	$grand_in_feed	=	$grand_in_like	= $grand_tourn_cr	=	$grand_tourn_1st	=	$grand_blog_crea	=	$grand_activity_total	=	$grand_event_total	=	$grand_print_total	=	$grand_lead_total	=	0;
			 foreach($StaffInfo as $key=>$val){
				 $disp_name		=	stripslashes($val['FirstLastname']);
				 $disp_fb_posts	=	$val['facebook_feed_count'];
				 $disp_fb_likes	=	$val['facebook_like_count'];
				 $disp_fb_photo_posts	=	$val['facebook_photo_count'];
				 $disp_fb_photo_likes	=	$val['facebook_photo_like_count'];
				 $disp_fb_video_posts	=	$val['facebook_video_count'];
				 $disp_fb_video_likes	=	$val['facebook_video_like_count'];								 
				 $disp_tw_feed	=	$val['twitter_feed_count'];
				 $disp_tw_retweet	=	$val['twitter_retweet_count'];
				 $disp_tw_fav	=	$val['twitter_favorite_count'];
				 $disp_in_feed	=	$val['instra_total_feed'];
				 $disp_in_likes	=	$val['instra_total_likes'];
				 $disp_to_creat	=	$val['tournaments_creation_total'];
				 $disp_to_win	=	$val['tournaments_fist_place'];
				 $disp_blo_creat	=	$val['blog_total_count'];
				 $disp_event_total	=	$val['event_total_count'];
				 $disp_print_total	=	$val['print_total_count'];
				 $disp_lead_total	=	$val['leads_total_count'];
				 $total_activity	=	$disp_fb_posts+$disp_tw_feed+$disp_in_feed+$disp_to_creat+$disp_blo_creat+$disp_fb_photo_posts+$disp_fb_video_posts;
				 $grand_fb_feed	+=	$disp_fb_posts;
				 $grand_fb_like	+=	$disp_fb_likes;
				 $grand_fb_photo_feed	+=	$disp_fb_photo_posts;
				 $grand_fb_photo_like	+=	$disp_fb_photo_likes;								 
				 $grand_fb_video_feed	+=	$disp_fb_video_posts;
				 $grand_fb_video_like	+=	$disp_fb_video_likes;								 				 
				 $grand_tw_feed	+=	$disp_tw_feed;
				 $grand_tw_retweet +=	$disp_tw_retweet;
				 $grand_tw_fav	+=	$disp_tw_fav;
				 $grand_in_feed	+=	$disp_in_feed;
				 $grand_in_like	+=	$disp_in_likes;
				 $grand_tourn_cr	+=	$disp_to_creat;
				 $grand_tourn_1st	+=	$disp_to_win;
				 $grand_blog_crea	+=	$disp_blo_creat;
				 $grand_activity_total	+=	$total_activity;
			     $grand_event_total	+=	$disp_event_total;
				 $grand_print_total	+=	$disp_print_total;
				 $grand_lead_total	+=	$disp_lead_total;				 
				 $DispData	=	array(
								$disp_name,
								$disp_fb_posts,
								$disp_fb_likes,
								$disp_fb_photo_posts,
								$disp_fb_photo_likes,
								$disp_fb_video_posts,
								$disp_fb_video_likes,								
								$disp_tw_feed,
								$disp_tw_retweet,
								$disp_tw_fav,
								$disp_in_feed,
								$disp_in_likes,
								$disp_to_creat,
								$disp_blo_creat,
								$disp_event_total,
								$disp_lead_total,
								$disp_print_total,																								
								$total_activity
							);
			# $CsvData[]	=	implode(',',$DispData);				 				 				 
				$CsvData[]	=	$DispData;				 				 				 
			 }
			 $DispData	=	array(
								'Total',
								$grand_fb_feed,
								$grand_fb_like,
								$grand_fb_photo_feed,
								$grand_fb_photo_like,
								$grand_fb_video_feed,
								$grand_fb_video_like,
								$grand_tw_feed,
								$grand_tw_retweet,
								$grand_tw_fav,
								$grand_in_feed,
								$grand_in_like,
								$grand_tourn_cr,
								$grand_blog_crea,
								$grand_event_total,
								$grand_lead_total,
								$grand_print_total,																								
								$grand_activity_total
							);
# 			$CsvData[]	=	implode(',',$DispData);	
 			$CsvData[]	=	$DispData;	
			}
		
		$file_name	=	date('Ymd',strtotime($search_from_date)).'-'.date('Ymd',strtotime($search_to_date)).'.csv';
		error_reporting(0);
		header( 'Content-Type: text/csv' );
		header( 'Content-Disposition: attachment;filename='.$file_name);
		$fp = fopen('php://output', 'w');		
		foreach ($CsvData as $key=>$val) {
			fputcsv($fp, $val);
		}
		fclose($fp);

		$contLength = ob_get_length();
		header( 'Content-Length: '.$contLength);									
	
	}
 }
 
# Manage Pages
 public function managefbpage()
	{
						
			$sql="Select * from user_page where page_user_id=".$this->session->userdata("userid");
			
			$data["pagelist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/managefbpage',$data,true);
			$this->load->view('layout-inner',$data);
			
	}

#Manage Blog Status
 public function fbpagetatus()
	{
		if(isset($_POST["page_id"],$_POST["status"]))
        {
        
			$qstr="update user_page set page_status='".$_POST["status"]."' where page_id=".$_POST["page_id"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
       
        } 
	}

	
#BULK Blog Status
 public function managebulkfbpage()
    {
        if(isset($_POST["id"],$_POST['action']))
        {
            $id=$_POST["id"];
            $action=$_POST['action'];
           	
			foreach($id as $key=>$val){
				$qstr="update user_page set page_status='".$action."' where page_id=".$val;
				$this->model->allQueries($qstr); 
			}
			
			$this->model->updateBlogstatus($id,$action); 
			$this->session->set_flashdata('sucess', 'Status Updated Sucessfully..!!');             
			echo "<strong>Status Updated Sucessfully..!!</strong>";
            
        }        
    }

#Filter Blogs
 public function filterfbpage()
 {
 
 	if(isset($_POST["filterBystatus"]))
      {
	  
	  		if($_POST["filterBystatus"]!="Filter By Status")
			{
			
				$sql="Select * from user_blog where userid=".$this->session->userdata("userid")." and status='".$_POST["filterBystatus"]."'";
			
				$data["blogslist"]=$this->model->allSelects($sql);
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);			
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/manageblogs',$data,true);
				$this->load->view('layout-inner',$data);
		
			}
		   else
			{
			
			$sql="Select * from user_blog where userid=".$this->session->userdata("userid");
			
			$data["blogslist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manageblogs',$data,true);
			$this->load->view('layout-inner',$data);
						
			}
	  	}
 }


# STAFF prints

  public function socialMediaPrompts() {
       
	    //$sql="Select * from user_print up,users u where up.userid=u.userid and u.companyID=".$this->session->userdata("userid")." and up.status='Active'";				
		
		/*$sql="Select cr.userid,u.FirstLastname,u.user_logo,cr.companyID 
			  from users u, company_ref cr where u.userid=cr.companyID and cr.userid=".$this->session->userdata("userid");	*/
		
		$sql = "Select smp.*,cr.userid,u.FirstLastname,u.user_logo,cr.companyID  from social_media_prompt smp
				LEFT JOIN users u ON u.userid=smp.ownerid
				LEFT JOIN company_ref cr on cr.companyID = smp.ownerid
				where cr.userid=" . $this->session->userdata("userid")." 
				and (find_in_set('all',smp.sharedTO) or find_in_set('".$this->session->userdata("userid")."',smp.sharedTO))";
		
		$data["PromptList"] = $this->model->allSelects($sql);
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
       	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-social-media-prompts',$data,true);
		$this->load->view('layout-inner',$data);

    }

  public function socialprompts() {
       		
		$sql = "Select smp.*,cr.userid,u.FirstLastname,u.user_logo,cr.companyID  from social_media_prompt smp
				LEFT JOIN users u ON u.userid=smp.ownerid
				LEFT JOIN company_ref cr on cr.companyID = smp.ownerid
				where cr.userid=" . $this->session->userdata("userid")." 
				and (find_in_set('all',smp.sharedTO) or find_in_set('".$this->session->userdata("userid")."',smp.sharedTO))";
		
		$data["PromptList"] = $this->model->allSelects($sql);
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
       	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
				
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
				
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-social-media-prompt',$data,true);
		$this->load->view('layout-inner',$data);

    }

 public function getSetPost($promptID=0){
 
 		if($promptID==0){
			$this->session->set_flashdata('error', "Invalid Attempt.");
			redirect('staff');
		}
		
		$userID = $this->session->userdata("userid");
		
		$sql	  = "Select um.userid,um.social_id,um.access_token from user_media um 
					 where um.userid=$userID AND um.social_type='facebook'";
		$TempInfo =	$this->model->allSelects($sql);
		
		if(count($TempInfo)>0){
		
			$StaffUserInfo = $TempInfo[0];	
			$user_permissons  = $this->facebook->api( '/'.$StaffUserInfo['social_id'].'/permissions', 'GET', array('access_token' => $StaffUserInfo['access_token']));
			
			if(isset($user_permissons['error'])){	
				
				$dataFields = $user_permissons["error"]["message"];
				$this->session->set_flashdata('error', "Token Expired Acess Denied. Please contact AnglerTrack For Support. ".$dataFields);
		  		redirect('staff');
			}
			else{
				
				if(count($user_permissons['data'])<=7){
				
				  $this->session->set_flashdata('error', "Permissions Acess Denied. Please contact AnglerTrack For Support.");
		  		  redirect('staff');
				  
				} else{
				
					redirect('staff/submitPost/'.$promptID);
				}
			}
		}
 }

 public function permissionsCheck(){
 
		$userID = $this->session->userdata("userid");
		
		$sql	  = "Select um.id,um.userid,um.social_id,um.access_token from user_media um 
					 where um.userid=$userID AND um.social_type='facebook'";
		$TempInfo =	$this->model->allSelects($sql);
		
		if(count($TempInfo)>0){
		
			$StaffUserInfo = $TempInfo[0];	
			
			echo "<pre>";
			echo "ID: ".$StaffUserInfo['id'];
			echo "</pre>";;
			
			$user_permissons  = $this->facebook->api( '/'.$StaffUserInfo['social_id'].'/permissions', 'GET', array('access_token' => $StaffUserInfo['access_token']));
			
			if(isset($user_permissons['error'])){	
				
				$dataFields = $user_permissons["error"]["message"];
				$this->session->set_flashdata('error', "Token Expired Acess Denied. Please contact AnglerTrack For Support. ".$dataFields);
		  		redirect('staff');
			}
			else{
				
				echo "<pre>";
					print_r($user_permissons['data']);
				echo "</pre>"; die;
				
				
				/*if(count($user_permissons['data'])<=7){
				
				  $this->session->set_flashdata('error', "Permissions Acess Denied. Please contact AnglerTrack For Support.");
		  		  redirect('staff');
				  
				} else{
				
					redirect('staff/submitPost/'.$promptID);
				}*/
			}
		}
 }

  public function submitPost($socialPromptId=0) {

 		if($socialPromptId==0){
			$this->session->set_flashdata('error', "Invalid Attempt.");
			redirect('staff');
		}
		
		$userID = $this->session->userdata("userid");
		
		$sql	  = "Select um.userid,um.social_id,um.access_token from user_media um 
					 where um.userid=$userID AND um.social_type='facebook'";
		$TempInfo =	$this->model->allSelects($sql);
		
		if(count($TempInfo)>0){
			$userInfo = $TempInfo[0];
		}

        $this->form_validation->set_rules('txtpostdescp', 'Description/Message', 'required');
		
        if ($this->form_validation->run() == TRUE) {
		
			$postFeed = array(
					'access_token' => $userInfo['access_token'],
					'message' => $this->input->post('txtpostdescp'),
					'picture' => $this->input->post('txtpicture')
				);
			$posted  = $this->facebook->api('/'.$userInfo['social_id'].'/feed','post',$postFeed);
			
			if(strlen($posted["id"])>0){
				$this->session->set_flashdata('sucess', 'Timeline Updated Successfully..!!');
				redirect('staff');
			}
        } 
		else
			{
						if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($socialPromptId>0){
						
							$sql	  =	"Select * from social_media_prompt where smpid=$socialPromptId";
							$TempInfo =	$this->model->allSelects($sql);
							$SocialMediaInfo	  =	$TempInfo[0];				
							$data['txtpostdescp'] =	$SocialMediaInfo['promptdescp']." ".$SocialMediaInfo['keywords'];
							
							if($SocialMediaInfo['smp_attachment']!=""){
							$data['txtpicture'] =	base_url($SocialMediaInfo['smp_attachment']);
							}else{
							$data['txtpicture'] = "";
							}
							
						}		
						else{
							$data['txtpostdescp'] = '';
							$data['txtpicture'] = "";							
						}	
			}
		
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
       	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
				
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
				
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
		$data['content']=$this->load->view('staff/staff-fb-post',$data,true);
		$this->load->view('layout-inner',$data);
  }


#View Prints
 public function viewSocialMediaPrompt() {

        $smpid = $_POST['smpID'];
		
		$sql = "Select * from social_media_prompt where smpid=".$smpid;
        $data["socialMediaPromptdetails"] = $this->model->allSelects($sql);
			
        if (count($data["socialMediaPromptdetails"]) == 0) {
            redirect("staff/socialprompts");
        }
        $this->load->view('staff/view-socialmedia-prompt', $data);

    }

# Individual Pro-Staff Sponsors

# ADD NEW SPONSOR
 public function addSponsorOF($psid=0){
	
			$keywordsArray = array();
			
			$this->form_validation->set_rules('txtsponsorname', 'Sponsor Name is Required', 'required');
			//$this->form_validation->set_rules('txtsponsoremailid', 'Sponsor Email is required', 'required');
				
			if ($this->form_validation->run() == TRUE)
			{	

				$sql = "SELECT u.userid,u.FirstLastname,u.fbkeywords,u.twtkeywords,
						u.instakeywords,u.youtubekeywords,u.user_logo,u.companyID 
					    FROM users u WHERE u.userid=".$this->input->post('txtsponsorname');	  
				$TempInfo = $this->model->allSelects($sql);
				
				//echo "<pre>"; print_r($TempInfo);echo "</pre>"; die;
				
				$postedFBkeywords = $postedTWTkeywords = $postedYTkeywords = $postedINSTAkeywords ="";
				
				if(count($TempInfo)>0){
				
					$EmpInfo = $TempInfo[0];					

					$postedFBkeywords = $EmpInfo['fbkeywords'].$this->input->post('txtfbkeywords');
					$postedTWTkeywords = $EmpInfo['twtkeywords'].$this->input->post('txttwtkeywords');
					$postedINSTAkeywords = $EmpInfo['instakeywords'].$this->input->post('txtinstakeywords');	
					$postedYTkeywords = $EmpInfo['youtubekeywords'].$this->input->post('txtyoutubekeywords');

					$sponsorinfo = array(
						'sponsorname'=>$EmpInfo['FirstLastname'],
						'sponsoremailid'=>"NA",
						'sponsorcontactno'=>"NA",
						'companyID'=>$EmpInfo['userid'],
						'sponsorCSid'=>$EmpInfo['companyID'],
						'fbkeywords'=>$postedFBkeywords,
						'twitterkeywords'=>$postedTWTkeywords,
						'instakeywords'=>$postedINSTAkeywords,
						'youtubekeywords'=>$postedYTkeywords,
						'prostaff_id'=>$this->session->userdata("userid"),
						'sponsor_logo'=>$EmpInfo['user_logo'],
						'status'=>"Active"							
					 );
								 
				}else{

					$postedFBkeywords = $this->input->post('txtfbkeywords');
					$postedTWTkeywords = $this->input->post('txttwtkeywords');
					$postedINSTAkeywords = $this->input->post('txtinstakeywords');	
					$postedYTkeywords = $this->input->post('txtyoutubekeywords');

					$sponsorinfo = array(
						'sponsorname'=>$this->input->post('txtsponsorname'),
						'sponsoremailid'=>"NA",
						'sponsorcontactno'=>"NA",
						'fbkeywords'=>$postedFBkeywords,
						'twitterkeywords'=>$postedTWTkeywords,
						'instakeywords'=>$postedINSTAkeywords,
						'youtubekeywords'=>$postedYTkeywords,
						'prostaff_id'=>$this->session->userdata("userid"),
						'status'=>"Active"							
					 );
				}
				
				$fbkeywords = getKeywords($this->session->userdata("userid"),'fbkeywords');
					
					if($fbkeywords!="")	{
						$fbkeywords = $fbkeywords.",".$postedFBkeywords;
					}
					else{
						$fbkeywords = $postedYTkeywords;
					}
					
					$twtkeywords = getKeywords($this->session->userdata("userid"),'twtkeywords');

					if($twtkeywords!=""){
						$twtkeywords = $twtkeywords.",".$postedTWTkeywords;
					}
					else{
						$twtkeywords = $postedTWTkeywords;
					}

					$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
					
					if($instakeywords!=""){
					
						$instakeywords = $instakeywords.",".$postedINSTAkeywords;
					}
					else{
						$instakeywords = $postedINSTAkeywords;
					}

					$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
					
					if($youtubekeywords!=""){
					
						$youtubekeywords = $youtubekeywords.",".$postedYTkeywords;
					}
					else{
						$youtubekeywords = $postedYTkeywords;
					}

					$keywordsArray['fbkeywords'] = $fbkeywords;
					$keywordsArray['twtkeywords'] = $twtkeywords;
					$keywordsArray['instakeywords'] = $instakeywords;
					$keywordsArray['youtubekeywords'] = $youtubekeywords;

				if($psid>0)
				{

					$this->model->allUpdate('ps_id',$psid,'user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					

					$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
					redirect('staff/mysponsors');

				}
				else{
				
					$this->model->allInserts('user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					
					$this->session->set_userdata($keywordsArray);
					
					$this->session->set_flashdata('sucess', 'Sponsor Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/mysponsors');			
					
				}

			}
			else{
			
					if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($psid>0){
						
							$sql	=	"select * from user_prostaff_sponsors where ps_id=$psid";
							$TempInfo	=	$this->model->allSelects($sql);
							$sponsorInfo	=	$TempInfo[0];
											
							$data['txtsponsorname']	 =	$sponsorInfo['sponsorname'];
							$data['txtcompanyID']	 =	$sponsorInfo['companyID'];
							$data['txtsponsoremailid']	 =	$sponsorInfo['sponsoremailid'];
							$data['txtsponsorcontactno'] =	$sponsorInfo['sponsorcontactno'];
							$data['txtfbkeywords']	 	 =	$sponsorInfo['fbkeywords'];
							$data['txttwtkeywords']	 	 =	$sponsorInfo['twitterkeywords'];
							$data['txtinstakeywords']	 =	$sponsorInfo['instakeywords'];
							$data['txtyoutubekeywords']	 =	$sponsorInfo['youtubekeywords'];
						
						}		
						else{
						
							$data['txtsponsorname']	 	= '';
							$data['txtcompanyID']	 =	'';
							$data['txtsponsoremailid']	= '';
							$data['txtsponsorcontactno']= '';
							$data['txtfbkeywords']	 = '';
							$data['txttwtkeywords']	 = '';
							$data['txtinstakeywords'] =	'';
							$data['txtyoutubekeywords']	 = '';
						}	
			}	
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$sql="Select u.userid,u.FirstLastname FROM users u WHERE u.memtype='Employer'";
				$data["empList"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data['psid']=$psid;
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-sponsors-indvprostaff',$data,true);
				$this->load->view('layout-inner',$data);
	}

 public function addSponsor($psid=0){
	
			$keywordsArray = array();
			
			$this->form_validation->set_rules('txtsponsorname', 'Sponsor Name is Required', 'required');
				
			if ($this->form_validation->run() == TRUE){	

				$sql = "SELECT u.userid,u.FirstLastname,u.fbkeywords,u.twtkeywords,
								u.instakeywords,u.youtubekeywords,u.user_logo,u.companyID 
					    	FROM users u WHERE u.userid=".$this->input->post('txtsponsorname');	  
				$TempInfo = $this->model->allSelects($sql);
								
				$postedFBkeywords = $postedTWTkeywords = $postedYTkeywords = $postedINSTAkeywords ="";
				
				if(count($TempInfo)>0){
				
					$EmpInfo = $TempInfo[0];					

					$postedFBkeywords 	 = $this->input->post('txtfbkeywords');
					$postedTWTkeywords 	 = $this->input->post('txttwtkeywords');
					$postedINSTAkeywords = $this->input->post('txtinstakeywords');	
					$postedYTkeywords 	 = $this->input->post('txtyoutubekeywords');

					$sponsorinfo = array(
						'sponsorname'=>$EmpInfo['FirstLastname'],
						'sponsoremailid'=>"NA",
						'sponsorcontactno'=>"NA",
						'companyID'=>$EmpInfo['userid'],
						'sponsorCSid'=>$EmpInfo['companyID'],
						'fbkeywords'=>$postedFBkeywords,
						'twitterkeywords'=>$postedTWTkeywords,
						'instakeywords'=>$postedINSTAkeywords,
						'youtubekeywords'=>$postedYTkeywords,
						'prostaff_id'=>$this->session->userdata("userid"),
						'sponsor_logo'=>$EmpInfo['user_logo'],
						'status'=>"Active"							
					 );			 
				}
				
				$fbkeywords = getKeywords($this->session->userdata("userid"),'fbkeywords');
					
					if($fbkeywords!="")	{
						$fbkeywords = $fbkeywords.",".$postedFBkeywords;
					}
					else{
						$fbkeywords = $postedYTkeywords;
					}
					
					$twtkeywords = getKeywords($this->session->userdata("userid"),'twtkeywords');

					if($twtkeywords!=""){
						$twtkeywords = $twtkeywords.",".$postedTWTkeywords;
					}
					else{
						$twtkeywords = $postedTWTkeywords;
					}

					$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
					
					if($instakeywords!=""){
					
						$instakeywords = $instakeywords.",".$postedINSTAkeywords;
					}
					else{
						$instakeywords = $postedINSTAkeywords;
					}

					$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
					
					if($youtubekeywords!=""){
					
						$youtubekeywords = $youtubekeywords.",".$postedYTkeywords;
					}
					else{
						$youtubekeywords = $postedYTkeywords;
					}

					$keywordsArray['fbkeywords'] = $fbkeywords;
					$keywordsArray['twtkeywords'] = $twtkeywords;
					$keywordsArray['instakeywords'] = $instakeywords;
					$keywordsArray['youtubekeywords'] = $youtubekeywords;

				if($psid>0){

					$this->model->allUpdate('ps_id',$psid,'user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					

					$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
					redirect('staff/mysponsors');

				}
				else{
				
					$this->model->allInserts('user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					
					$this->session->set_userdata($keywordsArray);
					
					$this->session->set_flashdata('sucess', 'Sponsor Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/mysponsors');			
					
				}

			}
			else{
			
					if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($psid>0){
						
							$sql	=	"select * from user_prostaff_sponsors where ps_id=$psid";
							$TempInfo	 = $this->model->allSelects($sql);
							$sponsorInfo = $TempInfo[0];
											
							$data['txtsponsorname']	 =	$sponsorInfo['sponsorname'];
							$data['txtcompanyID']	 =	$sponsorInfo['companyID'];
							$data['txtsponsoremailid']	 =	$sponsorInfo['sponsoremailid'];
							$data['txtsponsorcontactno'] =	$sponsorInfo['sponsorcontactno'];
							$data['txtfbkeywords']	 	 =	$sponsorInfo['fbkeywords'];
							$data['txttwtkeywords']	 	 =	$sponsorInfo['twitterkeywords'];
							$data['txtinstakeywords']	 =	$sponsorInfo['instakeywords'];
							$data['txtyoutubekeywords']=	$sponsorInfo['youtubekeywords'];
						
						}		
						else{
						
							$data['txtsponsorname']	 	  = '';
							$data['txtcompanyID']	 			=	'';
							$data['txtsponsoremailid']  = '';
							$data['txtsponsorcontactno']= '';
							$data['txtfbkeywords']	 	 = '';
							$data['txttwtkeywords']	 	 = '';
							$data['txtinstakeywords']  =	'';
							$data['txtyoutubekeywords']= '';
						}	
			}	
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        $data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$sql="Select u.userid,u.FirstLastname FROM users u WHERE u.memtype='Employer' ORDER BY u.FirstLastname asc";
				$data["empList"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data['psid']=$psid;
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-sponsors-indvprostaffNF',$data,true);
				$this->load->view('layout-inner',$data);
	}

 public function addSponsorRequest($psid=0){
	
			$keywordsArray = array();
			
			$this->form_validation->set_rules('txtsponsorname', 'Sponsor Name is Required', 'required');
			//$this->form_validation->set_rules('txtsponsoremailid', 'Sponsor Email is required', 'required');
				
			if ($this->form_validation->run() == TRUE)
			{	

					$postedFBkeywords = $this->input->post('txtfbkeywords');
					$postedTWTkeywords = $this->input->post('txttwtkeywords');
					$postedINSTAkeywords = $this->input->post('txtinstakeywords');	
					$postedYTkeywords = $this->input->post('txtyoutubekeywords');

					$sponsorinfo = array(
						'sponsorname'=>$this->input->post('txtsponsorname'),
						'sponsoremailid'=>"NA",
						'sponsorcontactno'=>"NA",
						'companyID'=>0,
						'fbkeywords'=>$postedFBkeywords,
						'twitterkeywords'=>$postedTWTkeywords,
						'instakeywords'=>$postedINSTAkeywords,
						'youtubekeywords'=>$postedYTkeywords,
						'prostaff_id'=>$this->session->userdata("userid"),
						'trans_ref'=>1,
						'status'=>"Active"							
					 );
				
				$fbkeywords = getKeywords($this->session->userdata("userid"),'fbkeywords');
					
					if($fbkeywords!="")	{
						$fbkeywords = $fbkeywords.",".$postedFBkeywords;
					}
					else{
						$fbkeywords = $postedYTkeywords;
					}
					
					$twtkeywords = getKeywords($this->session->userdata("userid"),'twtkeywords');

					if($twtkeywords!=""){
						$twtkeywords = $twtkeywords.",".$postedTWTkeywords;
					}
					else{
						$twtkeywords = $postedTWTkeywords;
					}

					$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
					
					if($instakeywords!=""){
					
						$instakeywords = $instakeywords.",".$postedINSTAkeywords;
					}
					else{
						$instakeywords = $postedINSTAkeywords;
					}

					$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
					
					if($youtubekeywords!=""){
					
						$youtubekeywords = $youtubekeywords.",".$postedYTkeywords;
					}
					else{
						$youtubekeywords = $postedYTkeywords;
					}

					$keywordsArray['fbkeywords'] = $fbkeywords;
					$keywordsArray['twtkeywords'] = $twtkeywords;
					$keywordsArray['instakeywords'] = $instakeywords;
					$keywordsArray['youtubekeywords'] = $youtubekeywords;

				if($psid>0)
				{

					$sponsor_img_path	=	'';
						if($_FILES['sponsor_logo']['error'] == 0){
							$fileName = $_FILES['sponsor_logo']['name'];
							$file='sponsor_logo';		
							$upload = uploadFile($file,'*','assets/upload/emp_logo',$fileName);
							if($upload['success']){
								$sponsor_img_path	=	$upload['path'];
								$sponsorinfo['sponsor_logo']	=	$sponsor_img_path;		   
							}																	
						}			

					$this->model->allUpdate('ps_id',$psid,'user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					

					$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
					redirect('staff/mysponsors');

				}
				else{

					$sponsor_img_path	=	'';
						if($_FILES['sponsor_logo']['error'] == 0){
							$fileName = $_FILES['sponsor_logo']['name'];
							$file='sponsor_logo';		
							$upload = uploadFile($file,'*','assets/upload/emp_logo',$fileName);
							if($upload['success']){
								$sponsor_img_path	=	$upload['path'];
								$sponsorinfo['sponsor_logo']	=	$sponsor_img_path;	
							}									
						}			

					# Assign Custom Scoop ID				
					$customScoopID = 0; $ArraylastCustomID = array(); $adminUserId=1;
					$sql	=	"SELECT companyID from users where userid=1 and memtype='Admin'";
					$TempInfo	 = $this->model->allSelects($sql);
					$customScoopLastID = $TempInfo[0];
					$customScoopID = ($customScoopLastID['companyID'])+1;
					$ArraylastCustomID['companyID'] = $customScoopID;
					
					$sponsorinfo['sponsorCSid'] = $customScoopID;
				
					$this->model->allInserts('user_prostaff_sponsors',$sponsorinfo);
					$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);	
					$this->model->allUpdate('userid',$adminUserId,'users',$ArraylastCustomID);					
					$this->session->set_userdata($keywordsArray);
					
					$this->session->set_flashdata('sucess', 'Sponsor Added Successfully..!! Add Another? Click Add New.');
					redirect('staff/mysponsors');			
					
				}

			}
			else{
			
					if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
						}
						else if($psid>0){
						
							$sql	=	"select * from user_prostaff_sponsors where ps_id=$psid";
							$TempInfo	=	$this->model->allSelects($sql);
							$sponsorInfo	=	$TempInfo[0];
											
							$data['txtsponsorname']	 =	$sponsorInfo['sponsorname'];
							$data['txtcompanyID']	 =	$sponsorInfo['companyID'];
							$data['txtsponsoremailid']	 =	$sponsorInfo['sponsoremailid'];
							$data['txtsponsorcontactno'] =	$sponsorInfo['sponsorcontactno'];
							$data['txtfbkeywords']	 	 =	$sponsorInfo['fbkeywords'];
							$data['txttwtkeywords']	 	 =	$sponsorInfo['twitterkeywords'];
							$data['txtinstakeywords']	 =	$sponsorInfo['instakeywords'];
							$data['txtyoutubekeywords']	 =	$sponsorInfo['youtubekeywords'];
						
						}		
						else{
						
							$data['txtsponsorname']	 	= '';
							$data['txtcompanyID']	 =	0;
							$data['txtsponsoremailid']	= '';
							$data['txtsponsorcontactno']= '';
							$data['txtfbkeywords']	 = '';
							$data['txttwtkeywords']	 = '';
							$data['txtinstakeywords'] =	'';
							$data['txtyoutubekeywords']	 = '';
						}	
			}	
			
				$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
				
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);

				$sql="Select u.userid,u.FirstLastname FROM users u WHERE u.memtype='Employer'";
				$data["empList"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
				
				$data['errorreg']= trim(strip_tags(validation_errors()));
				
				$data['psid']=$psid;
				
				$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
				$data['content']=$this->load->view('staff/add-sponsors-indvprostaff',$data,true);
				$this->load->view('layout-inner',$data);
	}

# Manage Blogs
 public function mysponsors()
	{
						
		$sql="Select * from user_prostaff_sponsors
			  where prostaff_id=".$this->session->userdata("userid");
			
		$search_by = "";
        $search_value = "";

				if (@$_POST) {
					$search_by = $_POST['formelement'];
					$search_value = $_POST['formvalue'];
				 } 
		
				if($search_by	==	'orderbydesc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date DESC";
								
				}
		
				if($search_by	==	'orderbyasc' && strlen($search_value)>0){
					$sql	.=	" ORDER BY posted_date ASC";
								
				}

			$data["sponsorlist"]=$this->model->allSelects($sql);
			
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
        	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			
				$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
										));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
										
				$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
			
			$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);
			$data['content']=$this->load->view('staff/manageindvsponsors',$data,true);
			$this->load->view('layout-inner',$data);	
	}

 public function indvSponsorstatus()
	{
		if(isset($_POST["id"],$_POST["status"]))
        {
        
			if($_POST["status"]=="Delete")
			{
			$qstr="delete from user_prostaff_sponsors where ps_id=".$_POST["id"];
			$this->model->allQueries($qstr); 
			$this->session->set_flashdata('sucess', 'Deleted Sucessfully..!!');             
			echo "<strong>Deleted Sucessfully..!!</strong>";

			}
			         
        } 
	}

public function viewSponsorInfo() {

		$psid = $_POST['psid'];
		$sType = $_POST['sType'];
		
    if($psid>0 && $sType=="nonATEmp"){
				$sql = "SELECT * FROM user_prostaff_sponsors WHERE ps_id=". $psid;
        $sponsordetails = $this->model->allSelects($sql);
				        
        $data["sponsordetails"] = $sponsordetails[0];
        
		if (count($data["sponsordetails"]) == 0) {
            redirect("staff/allEmployers");
        }
			$this->load->view('staff/view-sponsor-info',$data);
		}
		
		if($psid>0 && $sType=="ATEmp"){
		
		$sql = "SELECT u.userid,u.FirstLastname,u.user_logo,u.street,
				u.city,u.state,u.phone,u.email,u.user_document 
				FROM users u 
				WHERE userid=".$psid;
        $sponsordetails = $this->model->allSelects($sql);
				        
        $data["sponsordetails"] = $sponsordetails[0];
        if (count($data["sponsordetails"]) == 0) {
            redirect("staff/allEmployers");
        }

		$sql = "SELECT cr.userid,cr.prostaffName,cr.rank,cr.rank_score 
				FROM company_ref cr 
				WHERE cr.companyID=".$psid." AND cr.rank>0 
				AND cr.rank_score>0 ORDER BY cr.rank asc";
				
		$data["rankedList"] = $this->model->allSelects($sql);
		$this->load->view('staff/view-at-sponsor-info',$data);
		
	}
}

#YouTube 
 public function oAuthYoutube() {
   
        $data = array();
		
        if (isset($_GET['code'])) {
            $this->client->authenticate($_GET['code']);
            $this->session->set_userdata('token', $this->client->getAccessToken());
        }
		else
		{
			$this->session->set_flashdata('error', "Something went Wrong!!! Please try again.");
		  	redirect('staff');
		}

        if ($this->session->userdata('token')) {
            $this->client->setAccessToken($this->session->userdata('token'));
        }

        if ($this->client->getAccessToken()) {
            $Tokenarray = json_decode($this->client->getAccessToken(), true);
            $user = $this->objOAuthService->userinfo->get();
            $condt_array = array('userid'=> $this->session->userdata('userid'),'email' => $user->email, 'social_type' => 'youtube');
			$duplicate_user = $this->db->select('id')->where($condt_array)->get('user_media')->row_array();

			#Check To confirm progress
			if($user->id==""){
				$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout Google YouTube and Try again.");
				redirect('staff');
				}	
					
			if($Tokenarray['refresh_token']==""){
				$this->session->set_flashdata('error', "Authorization Failed. Please Logout Google YouTube and Try again.");
				redirect('staff');				
				}	

            $userData = array(
                'username' => $user->givenName,
                'fullname' => $user->name,
                'email' => $user->email,
                'gender' => $user->gender,
                'userid' => $this->session->userdata("userid"),
								'social_id' => $user->id,
                'image' => $user->picture,
                'created_date' => date('Y-m-d'),
                'social_type' => 'youtube',
                'access_token' => $Tokenarray['refresh_token']
            );
            if (empty($duplicate_user)) {
                $this->db->insert('user_media', $userData);
                $last_opt_id = $this->db->insert_id();
            } else {
                $this->db->where('id', $duplicate_user['id'])->update('user_media', $userData);
                $last_opt_id = $duplicate_user['id'];
            }
            //$this->session->set_userdata('user_id', $last_opt_id);
			$this->getYoutube($last_opt_id);
        } 
    }

   public function getYoutube($rowid) {

		$this->youtube = new Google_Service_YouTube($this->client);
        $data = array();
        $user_data = $this->db->select('access_token,id')->where('id', $rowid)->get('user_media')->row_array();
		$acess_token ="";
		
        if (!empty($user_data)) {

            try {
                $this->client->refreshToken($user_data['access_token']);
                $tokens = $this->client->getAccessToken();
				$this->client->setAccessToken($tokens);
				
            } catch (Exception $ex) {
                //print_r($ex);
            }
            $user_id = $this->session->userdata('userid');
			
			if($acess_token)
			{
				$this->client->setAccessToken($acess_token);
			}

            if ($this->client->getAccessToken()) {
                $channels = $this->youtube->channels->listChannels('snippet,brandingSettings,status,contentDetails', array('mine' => true));
                $channelList = $channels['items'];
                if (!empty($channelList)) {
                    foreach ($channelList as $chkey => $chval) {
                        $duplicate_channel = $this->db->select('id')->where('channel_id', $chval['id'])->where('user_id', $user_id)->get('user_channels')->row_array();
                        $channelData = array(
                            'created_date' => date('Y-m-d', strtotime($chval['snippet']['publishedAt'])),
                            'privacy' => $chval['status']['privacyStatus'],
                            'channel_id' => $chval['id'],
                            'title' => $chval['brandingSettings']['channel']['title'],
                            'banner_image' => $chval['brandingSettings']['image']['bannerImageUrl'],
                            'user_id' =>  $user_id,
														'status'=>'Active'
                        );
                        if (empty($duplicate_channel))
                            $this->db->insert('user_channels', $channelData);
                        else
                            $this->db->where('id', $duplicate_channel['id'])->update('user_channels', $channelData);

                        // Extract the unique playlist ID that identifies the list of videos
                        // uploaded to the channel, and then call the playlistItems.list methodto retrieve that list.
                       
                        $uploadsListId = $chval['contentDetails']['relatedPlaylists']['uploads'];
                        $playlistItemsResponse = $this->youtube->playlistItems->listPlaylistItems('snippet', array(
                            'playlistId' => $uploadsListId,
                            'maxResults' => 50
                        ));
                        foreach ($playlistItemsResponse['items'] as $playlistItem) {
                            $video_ID = $playlistItem['snippet']['resourceId']['videoId'];
                            if ($video_ID)
                                $listResponse = $this->youtube->videos->listVideos("statistics,fileDetails,snippet,status", array('id' => $video_ID));
								
                            if (!empty($listResponse)) {
                                $UserFeed = $listResponse[0];
                                $duplicate_feed = $this->db->select('id')->where('video_id', $video_ID)->where('user_id', $user_id)->get('user_youtubefeeds')->row_array();
                                $feedData = array(
                                    'video_id' => $video_ID,
                                    'channel_id' => $UserFeed['snippet']['channelId'],
                                    'title' => $UserFeed['snippet']['title'],
																		'text' => $UserFeed['snippet']['description'],
                                    'total_views' => $UserFeed['statistics']['viewCount'],
                                    'total_likes' => $UserFeed['statistics']['likeCount'],
                                    'thumb' => $UserFeed['snippet']['thumbnails']['default']['url'],
                                    'video_url' => "https://www.youtube.com/watch?v=" . $video_ID,
																		'created_date'=> $UserFeed['snippet']['publishedAt'],
                                    'privacy'=> $UserFeed['status']['privacyStatus'],
																		'user_id' => $user_id
                                );
                                if (empty($duplicate_feed))
                                    $this->db->insert('user_youtubefeeds', $feedData);
                                else
                                    $this->db->where('id', $duplicate_feed['id'])->update('user_youtubefeeds', $feedData);
                            }
                        }
                    }
                }
				$this->session->set_flashdata('sucess', 'YouTube Feed Successfully.');
				redirect('staff/mediaSetup');
            }
        }
			$this->session->set_flashdata('error', "Something went Wrong!!! Please try again.");
		  	redirect('staff');
    }
	
   public function refreshYoutube() {

		$this->youtube = new Google_Service_YouTube($this->client);
        $data = array();
		$acess_token ="";
        //$user_data = $this->db->select('access_token,id')->where('id', $this->session->userdata('user_id'))->get('user_media')->row_array();

		 $sql=	"Select * from user_media where social_type='youtube' and userid=".$this->session->userdata('userid');
		 $YouTubeInfo=$this->model->allSelects($sql);
		 
		 if(count($YouTubeInfo)>0)
		 {
		 	$user_data = $YouTubeInfo[0];
			//echo "<pre>"; print_r($user_data); echo "</pre>";die;
		 }
		
        if (!empty($user_data)) {
            try {
                
                $this->client->refreshToken($user_data['access_token']);
                $tokens = $this->client->getAccessToken();
				$this->client->setAccessToken($tokens);			
 
            } catch (Exception $ex) {
                //echo "<pre>"; print_r($ex); echo "</pre>";die;
            }
			
            $user_id = $this->session->userdata('userid');

            if ($this->client->getAccessToken()) {

                $channels = $this->youtube->channels->listChannels('snippet,brandingSettings,status,contentDetails', array('mine' => true));
                $channelList = $channels['items'];
                if (!empty($channelList)) {
                    foreach ($channelList as $chkey => $chval) {
                        $duplicate_channel = $this->db->select('id')->where('channel_id', $chval['id'])->where('user_id', $user_id)->get('user_channels')->row_array();
                        $channelData = array(
                            'created_date' => date('Y-m-d', strtotime($chval['snippet']['publishedAt'])),
                            'privacy' => $chval['status']['privacyStatus'],
                            'channel_id' => $chval['id'],
                            'title' => $chval['brandingSettings']['channel']['title'],
                            'banner_image' => $chval['brandingSettings']['image']['bannerImageUrl'],
                            'user_id' =>  $user_id,
							'status'=>'Active'
                        );
                        if (empty($duplicate_channel))
                            $this->db->insert('user_channels', $channelData);
                        else
                            $this->db->where('id', $duplicate_channel['id'])->update('user_channels', $channelData);

                        // Extract the unique playlist ID that identifies the list of videos
                        // uploaded to the channel, and then call the playlistItems.list method
                        // to retrieve that list.
                        $uploadsListId = $chval['contentDetails']['relatedPlaylists']['uploads'];
                        $playlistItemsResponse = $this->youtube->playlistItems->listPlaylistItems('snippet', array(
                            'playlistId' => $uploadsListId,
                            'maxResults' => 50
                        ));
                        foreach ($playlistItemsResponse['items'] as $playlistItem) {
                            $video_ID = $playlistItem['snippet']['resourceId']['videoId'];
                            if ($video_ID)
                                $listResponse = $this->youtube->videos->listVideos("statistics,fileDetails,snippet,status", array('id' => $video_ID));
								
                            if (!empty($listResponse)) {
                                $UserFeed = $listResponse[0];
                                $duplicate_feed = $this->db->select('id')->where('video_id', $video_ID)->where('user_id', $user_id)->get('user_youtubefeeds')->row_array();
                                $feedData = array(
                                    'video_id' => $video_ID,
                                    'channel_id' => $UserFeed['snippet']['channelId'],
                                    'title' => $UserFeed['snippet']['title'],
									'text' => $UserFeed['snippet']['description'],
                                    'total_views' => $UserFeed['statistics']['viewCount'],
                                    'total_likes' => $UserFeed['statistics']['likeCount'],
                                    'thumb' => $UserFeed['snippet']['thumbnails']['default']['url'],
                                    'video_url' => "https://www.youtube.com/watch?v=".$video_ID,
									'created_date'=> $UserFeed['snippet']['publishedAt'],
                                    'privacy'=> $UserFeed['status']['privacyStatus'],
									'user_id' => $user_id
                                );
                                if (empty($duplicate_feed))
                                    $this->db->insert('user_youtubefeeds', $feedData);
                                else
                                    $this->db->where('id', $duplicate_feed['id'])->update('user_youtubefeeds', $feedData);
                            }
                        }
                    }
                }
				$this->session->set_flashdata('sucess', 'YouTube Feed Successfully.');
				redirect('staff/mediaSetup');

            }
        }
			$this->session->set_flashdata('error', "Something went Wrong!!! Please try again.");
		  	redirect('staff');
  }

# YouTube Gallery

 public function youtubegallery()
	{			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
										
		$data["navigation"]=$this->load->view('staff/staff-navigation',$data,true);
		$data["iconmenus"]=$this->load->view('staff/staff-iconmenus','',true);	
		$data['errorreg']= str_replace("<p>","", str_replace("</p>","",validation_errors()));				
		$data['content']=$this->load->view('staff/youtubegallery',$data,true);
		$this->load->view('layout-inner',$data);					
	}		

 public function galleryLoadYouTube()
 {

		$user_id = $this->input->post('user_id');
			
		$search_by = "";
        $search_value = "";

		if (@$_POST) {
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
        } 

		$sql = "select * FROM user_youtubefeeds
				where user_id=".$this->session->userdata("userid");
		
		$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
			
		if($youtubekeywords){
			$keywordArr	=	explode(',',$youtubekeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%' or title like '$val%' or title like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$sql	.=	" and ($append_qry)";
		}	
		
		if($search_by	==	'bykeyword' && strlen($search_value)>0){
			$sql	.=	" and text like '%$search_value%'";
		}
						
		$sql = $sql. " and privacy='public' order by created_date desc";

		$GalleryInfo = $this->model->allSelects($sql);
		echo json_encode($GalleryInfo);
 }

 public function getYouTubePic(){

	$sql="";
	
	if($_POST){
	
		if($_POST['id']!=""){		
		$sql=	"select id,video_url AS linkurl,total_likes AS likes_count,total_views as views_count,thumb as pic_url,text as text, created_date
				from user_youtubefeeds where user_id=".$this->session->userdata("userid")." AND id=".$_POST['id'];
		}
				
		$data['GalleryInfo'] = $this->model->allSelects($sql);
		$this->load->view('staff/youtubegalleryview',$data);
	}
}

 public function getFacebookStats(){
 
 		$startWeekDay = date('Y-m-d',strtotime("-31 days"));
		$endDate 	  = date('Y-m-d');

		$search_from_date = date("Y-m-d",strtotime($startWeekDay));
		$search_to_date	  = date("Y-m-d",strtotime($endDate));

		$tempretArr	=	array();
		$StaffInfo = array();
		$user_id = $this->session->userdata("userid");
		
		#Facebook Related Content				
		#-------------------------Facebook Feed Count
		
				$fbkeywords	= getKeywords($user_id,'fbkeywords');
				$keywordsQuery =""; $dateQuery = "";

if($this->session->userdata("userid")!=41){

				if($fbkeywords){
					$keywordArr	= explode(',',$fbkeywords);
					$keyword_query	= array();
					foreach($keywordArr as $key=>$val){
						if($val != '') {

						
						$keyword_query[] = " (story LIKE '".trim($val)."%' or story LIKE '%".trim($val)."%') ";
					}
				}
					$append_qry	   = implode(' or ',$keyword_query);
					$keywordsQuery = " AND ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$dateQuery = " AND created_time>= '$search_from_date' and created_time <= '$search_to_date' ";
				}				
}
				
				$sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_feed WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type!='video'";
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_feed_count"]=$tempInfo[0]['rec_total'];		
						
				#-------------------------Facebook Feed Like Count
				$sql="SELECT IFNULL(sum(likes_count),0) as rec_total FROM user_feed  WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type!='video'";
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_like_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Facebook Feed Comment Count
				$sql="SELECT IFNULL(sum(comment_count),0) AS rec_total FROM user_feed WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type!='video'";
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["facebook_comment_count"]=$tempInfo[0]['rec_total'];				
				
				#-------------------------Facebook Photo Count
				$tempretArr["facebook_photo_count"]=0;				

				#-------------------------Facebook Photo Like Count
				$tempretArr["facebook_photo_like_count"]=0;	

				#-------------------------Facebook Photo Comment Count
				$tempretArr["facebook_photo_comment_count"]=0;					
				
                #-------------------------Facebook Video Count
				$uservideoCount = 0;
				
				#Videos as Timeline Feeds
                $sql = "SELECT IFNULL(count(*),0) as rec_total FROM user_feed WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type='video'";
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_count"] = $uservideoCount + $userfeedVideoCount;
 
                #-------------------------Facebook Video Like Count
 				$userVideoLikeCount = 0;
				
				#Videos as Timeline Feeds/Likes
                $sql = "SELECT IFNULL(sum(likes_count),0) AS rec_total FROM user_feed WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type='video'";
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoLikeCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_like_count"] = $userVideoLikeCount + $userfeedVideoLikeCount;
				
				#-------------------------Facebook Video Comment Count
				$userVideoCommentCount = 0;

				#Videos as Timeline Feeds/Comments
                $sql = "SELECT IFNULL(sum(comment_count),0) AS rec_total FROM user_feed WHERE user_id=$user_id $keywordsQuery $dateQuery AND feed_type='video'";
                $tempInfo = $this->model->allSelects($sql);
				$userfeedVideoCommentCount = $tempInfo[0]['rec_total'];
                $tempretArr["facebook_video_comment_count"] = $userVideoCommentCount + $userfeedVideoCommentCount;
				
				$StaffInfo[] =	$tempretArr;
				$data['StaffInfo'] = $StaffInfo;

				$this->load->view('staff/load/fbStats',$data);
 }

 public function getTwitterStats(){

 		$startWeekDay = date('Y-m-d',strtotime("-32 days"));
		$endDate 	  = date('Y-m-d');

		$search_from_date	=	date("Y-m-d",strtotime($startWeekDay));
		$search_to_date		=	date("Y-m-d",strtotime($endDate));

		$tempretArr	= array();
		$StaffInfo = array();
		$user_id = $this->session->userdata("userid");


				#Twitter Related Content
				#-------------------------Twitter Feed Count		
				
				$twtkeywords = getKeywords($user_id,'twtkeywords');	
				
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				
				if($search_from_date && $search_to_date){
					$sql .=	" and created_at>= '".$search_from_date."' and created_at <='".$search_to_date."'";
				}	
				
				$tempInfo =	$this->model->allSelects($sql);
				$tempretArr["twitter_feed_count"] = $tempInfo[0]['rec_total'];
												  
				#-------------------------Twitter Re Tweet Count	
				$sql="SELECT IFNULL(sum(retweet_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				//$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '".$search_from_date."' and created_at <= '".$search_to_date."' ";
				}					  
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_retweet_count"]=$tempInfo[0]['rec_total'];

				#-------------------------Twitter Like Count	
				$sql="SELECT IFNULL(sum(favorite_count),0) as rec_total FROM user_tweets where user_id=$user_id ";						

				if($twtkeywords){
					$keywordArr	=	explode(',',$twtkeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	= implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				//$append_qry	=	'';
				if($search_from_date && $search_to_date){
					$sql	.=	" and created_at>= '".$search_from_date."' and created_at <= '".$search_to_date."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["twitter_favorite_count"]=$tempInfo[0]['rec_total'];					

				$StaffInfo[] =	$tempretArr;
				$data['StaffInfo'] = $StaffInfo;

				$this->load->view('staff/load/twtStats',$data);
				
}

 public function getInstagramStats(){

 		$startWeekDay = date('Y-m-d',strtotime("-33 days"));
		//$endDate 	  = date('Y-m-d');
		$endDate = date('Y-m-d',strtotime("+2 days"));
		
		$search_from_date	=	date("Y-m-d",strtotime($startWeekDay));
		$search_to_date		=	date("Y-m-d",strtotime($endDate));

		$tempretArr	=	array();
		$StaffInfo = array();
		$user_id = $this->session->userdata("userid");

		#Instragram Related Content
		#-------------------------Instragram Feed Count		
				
				$instakeywords	 =	getKeywords($user_id,'instakeywords');
					
				$sql="SELECT IFNULL(count(*),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		

				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						if($val != '') {
							$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
						}
						
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}

				if($search_from_date && $search_to_date){
					$sql	.=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
					//$sql .=	" and str_to_date(created_time, '%d/%m/%Y')>= '".date('d/m/Y',strtotime($search_from_date))."' and str_to_date(created_time, '%d/%m/%Y') <= '".date('d/m/Y',strtotime($search_to_date))."' ";
				}

				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_feed"]=$tempInfo[0]['rec_total'];

				#-------------------------Instragram Like Count			
				$sql="SELECT IFNULL(sum(likes),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";		
						
				if($instakeywords){
					$keywordArr	=	explode(',',$instakeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$sql	.=	" and ($append_qry)";
				}
				// echo date('d-m-Y',strtotime($search_from_date));
				// echo '<br>';
				// echo date('d-m-Y',strtotime($search_to_date));
				// die('ehrere');

				if($search_from_date && $search_to_date){
					$sql .=	" and created_time>= '".strtotime($search_from_date)."' and created_time <= '".strtotime($search_to_date)."' ";
					//$sql .=	" and str_to_date(created_time, '%d/%m/%Y')>= '".date('d/m/Y',strtotime($search_from_date))."' and str_to_date(created_time, '%d/%m/%Y') <= '".date('d/m/Y',strtotime($search_to_date))."' ";
				}
				$tempInfo	=	$this->model->allSelects($sql);
				$tempretArr["instra_total_likes"]=$tempInfo[0]['rec_total'];	

                #-------------------------Instragram Comments Count			
                $sql = "SELECT IFNULL(sum(comments_count),0) as rec_total FROM user_instagram_feed where user_id=$user_id ";
                $instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
				//echo $instakeywords;
				if ($instakeywords) {
                    $keywordArr = explode(',', $instakeywords);
                    $keyword_query = array();
                    foreach ($keywordArr as $key => $val) {
                        $keyword_query[] = " (text like '$val%' or text like '%$val%') ";
                    }
                    $append_qry = implode(' or ', $keyword_query);
                    $sql .= " and ($append_qry)";
                }

                if ($search_from_date && $search_to_date) {
					//$sql .=	" and str_to_date(created_time, '%d/%m/%Y')>= str_to_date('".$search_from_date."', 'd/m/Y') and str_to_date(created_time, '%d/%m/%Y') <= str_to_date('".$search_to_date."', 'd/m/Y') ";
                    $sql .= " and created_time>= '" . strtotime($search_from_date) . "' and created_time <= '" . strtotime($search_to_date) . "' ";
				}
				// echo '<pre>';
				// echo $sql;
                $tempInfo = $this->model->allSelects($sql);
                $tempretArr["instra_total_comments"] = $tempInfo[0]['rec_total'];

				$StaffInfo[] =	$tempretArr;
				$data['StaffInfo'] = $StaffInfo;
				// echo '<pre>';
				// print_r($data);
				// die('herere');

				$this->load->view('staff/load/instaStats',$data);
 }

 public function getYouTubeStats(){

 		//$startWeekDay = date('Y-m-d',strtotime("-30 days"));
		$startWeekDay = '2014-01-01';
		$endDate 	  = date('Y-m-d');

		$search_from_date = date("Y-m-d",strtotime($startWeekDay));
		$search_to_date	  = date("Y-m-d",strtotime($endDate));

		$tempretArr	= array();
		$StaffInfo = array();
		$user_id = $this->session->userdata("userid");

		$dateAppend = ""; $append_qry=""; $yt_append_qry="";
		$feed_tot_indv = $views_tot_indv = 0 ; $likes_tot_indv = 0 ;
		$feed_tot_hyb = $views_tot_hyb = 0 ; $likes_tot_hyb = 0 ;

				if($search_from_date && $search_to_date){
					$dateAppend	= " AND created_date>= '".$search_from_date."' AND created_date <= '".$search_to_date."' ";
				}

				#YouTube Related Content
				#-------------------------YouTube Feed Count			
				
				$youtubekeywords =	getKeywords($user_id,'youtubekeywords');	

				if($youtubekeywords){
					$keywordArr	=	explode(',',$youtubekeywords);
					$keyword_query	=	array();
					foreach($keywordArr as $key=>$val){
						$keyword_query[]	=	" (text like '".trim($val)."%' or text like '%".trim($val)."%') ";
					}
					$append_qry	=	implode(' or ',$keyword_query);
					$yt_append_qry = " AND ($append_qry)";
				}

				/*$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(total_views),0) as rec_views_total
					  FROM user_youtubefeeds WHERE user_id=$user_id $yt_append_qry $dateAppend";*/		

				$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(total_views),0) as rec_views_total,
					  IFNULL(sum(total_likes),0) as rec_likes_total
					  FROM user_youtubefeeds WHERE user_id=$user_id $yt_append_qry $dateAppend";		

				$tempInfo	=	$this->model->allSelects($sql);
				$feed_tot_indv = $tempInfo[0]['rec_total']; 
				$views_tot_indv = $tempInfo[0]['rec_views_total'];
				$likes_tot_indv = $tempInfo[0]['rec_likes_total'];

				if($this->session->userdata("acess_type")=='Hybrid'){
				
					/*$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(ytf.total_views),0) as rec_views_total
						  FROM user_youtubefeeds ytf
						  LEFT JOIN users u ON u.userid = ytf.user_id
						  LEFT JOIN company_ref cr ON cr.userid= ytf.user_id
						  WHERE cr.companyID = $user_id $yt_append_qry $dateAppend";*/
					
					$sql="SELECT IFNULL(count(*),0) as rec_total,IFNULL(sum(total_views),0) as rec_views_total,
						  IFNULL(sum(ytf.total_likes),0) as rec_likes_total,
						  FROM user_youtubefeeds ytf
						  LEFT JOIN users u ON u.userid = ytf.user_id
						  LEFT JOIN company_ref cr ON cr.userid= ytf.user_id
						  WHERE cr.companyID = $user_id $yt_append_qry $dateAppend";
						
					$tempInfo = $this->model->allSelects($sql);
					$feed_tot_hyb = $tempInfo[0]['rec_total']; 
					$views_tot_hyb = $tempInfo[0]['rec_views_total'];
					$likes_tot_hyb = $tempInfo[0]['rec_likes_total'];
				}
				
		$tempretArr["youtube_total_feed"]  = $feed_tot_indv + $feed_tot_hyb;
		$tempretArr["youtube_total_views"] = $views_tot_indv + $views_tot_hyb;
		$tempretArr["youtube_total_likes"] = $likes_tot_indv + $likes_tot_hyb;

		$StaffInfo[] 	   = $tempretArr;
		$data['StaffInfo'] = $StaffInfo;

		$this->load->view('staff/load/ytStats',$data);
 }
 
# Wizard Step Functions
function getFbPagesList(){
	
	$sql="Select * from user_page where page_user_id=".$this->session->userdata("userid");
	$data["pagelist"]=$this->model->allSelects($sql);
	$this->load->view('staff/load/fbpagesgrid',$data);
}
 
function getSponsorList(){

 	$sql="(Select u.userid AS id, u.FirstLastname as empname ,u.user_logo as emplogo,'ATEmp' AS empType 
		   from users u
		   LEFT JOIN company_ref cr ON cr.companyID=u.userid
		   where cr.userid=".$this->session->userdata("userid").")
			 	 UNION
		  (Select ups.ps_id AS id, ups.sponsorname as empname, ups.sponsor_logo AS emplogo,'nonATEmp' AS empType
		   from user_prostaff_sponsors ups
		   where ups.prostaff_id=".$this->session->userdata("userid").")";
			  
	$data["emplist"]=$this->model->allSelects($sql);
	$this->load->view('staff/load/sponsorsgrid',$data);

}

function addSponsorInfo(){
		
		$this->form_validation->set_rules('txtsponsorname', 'Sponsor Name', 'required|callback_validate_addSponsor');
					
		if ($this->form_validation->run() == TRUE){
				
				$sql = "SELECT u.userid,u.FirstLastname,u.fbkeywords,u.twtkeywords,
						u.instakeywords,u.youtubekeywords,u.user_logo,u.companyID 
					    FROM users u WHERE u.userid=".$this->input->post('txtsponsorname');	  
				$TempInfo = $this->model->allSelects($sql);
				
				$postedFBkeywords = $postedTWTkeywords = $postedYTkeywords = $postedINSTAkeywords ="";
				
				if(count($TempInfo)>0){
				
					$EmpInfo = $TempInfo[0];					

					$postedFBkeywords = $this->input->post('txtfbkeywords');
					$postedTWTkeywords = $this->input->post('txttwtkeywords');
					$postedINSTAkeywords = $this->input->post('txtinstakeywords');	
					$postedYTkeywords = $this->input->post('txtyoutubekeywords');

					$sponsorinfo = array(
						'sponsorname'=>$EmpInfo['FirstLastname'],
						'sponsoremailid'=>"NA",
						'sponsorcontactno'=>"NA",
						'companyID'=>$EmpInfo['userid'],
						'sponsorCSid'=>$EmpInfo['companyID'],
						'fbkeywords'=>$postedFBkeywords,
						'twitterkeywords'=>$postedTWTkeywords,
						'instakeywords'=>$postedINSTAkeywords,
						'youtubekeywords'=>$postedYTkeywords,
						'prostaff_id'=>$this->session->userdata("userid"),
						'sponsor_logo'=>$EmpInfo['user_logo'],
						'status'=>"Active"							
					 );			 
				}
				
				$fbkeywords = getKeywords($this->session->userdata("userid"),'fbkeywords');
					
					if($fbkeywords!="")	{
						$fbkeywords = $fbkeywords.",".$postedFBkeywords;
					}
					else{
						$fbkeywords = $postedYTkeywords;
					}
					
					$twtkeywords = getKeywords($this->session->userdata("userid"),'twtkeywords');

					if($twtkeywords!=""){
						$twtkeywords = $twtkeywords.",".$postedTWTkeywords;
					}
					else{
						$twtkeywords = $postedTWTkeywords;
					}

					$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
					
					if($instakeywords!=""){
					
						$instakeywords = $instakeywords.",".$postedINSTAkeywords;
					}
					else{
						$instakeywords = $postedINSTAkeywords;
					}

					$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');
					
					if($youtubekeywords!=""){
					
						$youtubekeywords = $youtubekeywords.",".$postedYTkeywords;
					}
					else{
						$youtubekeywords = $postedYTkeywords;
					}

					$keywordsArray['fbkeywords'] = $fbkeywords;
					$keywordsArray['twtkeywords'] = $twtkeywords;
					$keywordsArray['instakeywords'] = $instakeywords;
					$keywordsArray['youtubekeywords'] = $youtubekeywords;

			$this->model->allInserts('user_prostaff_sponsors',$sponsorinfo);
			$this->model->allUpdate('userid',$this->session->userdata("userid"),'users',$keywordsArray);					

			$returnArr['error']	=	false;
			$returnArr['msg']	=	"Sucessfully Added";
		}
		else{
			$returnArr['error']	=	true;
			$returnArr['msg']	=	GetFormError();;
		}		
		echo json_encode($returnArr);
 }

  function validate_addSponsor($str){
		$sql = "SELECT * from user_prostaff_sponsors where companyID=$str and prostaff_id=".$this->session->userdata("userid");
		$TempInfo	=	$this->model->allSelects($sql);
		if(count($TempInfo)>0 && $TempInfo){
			$sposnor_exits_msg	=	"Sponsor Already Added!!!";
			$this->form_validation->set_message('validate_addSponsor', $sposnor_exits_msg);
			return false;
		}
		else
			return true;
	}

 function getMyProfileinfo(){
		
 		/*$sql="SELECT userid,FirstLastname,email,notifemail,loginname,phone,street,city,state,zipcode,
			  fishingorganization,fishingtype,isguide,prefer_hardware,water_body_type,shirt_size,
			  other_sposors_1,at_profile_pic
			  FROM users where userid=".$this->session->userdata("userid");*/

 		$sql="SELECT u.userid,u.FirstLastname,u.email,u.notifemail,u.loginname,u.phone,u.street,u.city,u.state,u.zipcode,
			  	ud.fishingorganization,u.fishingtype,ud.isguide,ud.prefer_hardware,ud.water_body_type,ud.shirt_size,
			  	ud.other_sposors_1,u.at_profile_pic
			  	FROM users u
					LEFT JOIN user_pro_app_details ud ON ud.userid=u.userid
					WHERE u.userid=".$this->session->userdata("userid");

		$data['profileinfo'] = $this->model->allSelects($sql);
		$this->load->view('staff/load/updateprofile',$data);
	}


  function updateMyprofile(){
	
		$this->form_validation->set_rules('txtname', 'Name', 'required');
		$this->form_validation->set_rules('txtemail', 'Email', 'required');

		if ($this->form_validation->run() == TRUE){		            			            								
			 
							$member=array(
								'FirstLastname'=>$this->input->post('txtname'),
								'email'=>$this->input->post('txtemail'),
								'phone'=>$this->input->post('txtcontactno'),
								'street'=>$this->input->post('txtstreet'),
								'city'=>$this->input->post('txtcity'),
								'state'=>$this->input->post('txtstate'),
								'zipcode'=>$this->input->post('txtzipcode'),
								'fishingtype'=>$this->input->post('fishingtype'),
								'memtype'=>"Staff"
							   );
        
						if($this->input->post('profilepicinfo')=='None' || $this->input->post('profilepicinfo')=="" || $this->input->post('profile_pic_path')!=""){
						 
							$profile_pic_path	=	'None'; 
						
							if($_FILES['profile_pic_path']['error'] == 0){
								$fileName = $_FILES['profile_pic_path']['name'];
								$file='profile_pic_path';		
								$upload = uploadFile($file,'*','assets/upload/profilepics',$fileName);
								if($upload['success']){
									$profile_pic_path	=	$upload['path'];
								}									
								$member['at_profile_pic']	=	$profile_pic_path;	
						}	
			}		
							   
			$this->model->updateprofile($this->session->userdata("userid"),$member);

			$memberDet=array(
							'isguide'=>$this->input->post('txtguide'),
							'prefer_hardware'=>$this->input->post('txtpfheadwear'),
							'water_body_type'=>$this->input->post('txtwaterbodytype'),
							'shirt_size'=>$this->input->post('txtshirtsize'),
							'other_sposors_1'=>$this->input->post('txtothersponsor'),
							'fishingorganization' => $this->input->post('txtfishingOrgz')
			);
				
			$this->model->allUpdate('userid',$this->session->userdata("userid"),'user_pro_app_details',$memberDet);

      $notification_msg	=	"Profile Updated Sucessfully.";
			$returnArr['error']	=	false;
			$returnArr['msg']	=	$notification_msg;
		}
		else{
			$returnArr['error']	=	true;
			$returnArr['msg']	=	GetFormError();
		}		
		echo json_encode($returnArr);
 }

 function topInstaPhotos(){
 
	//$user_id = $this->input->post('user_id');

		$instakeywords = getKeywords($this->session->userdata("userid"),'instakeywords');
		$inst_append_qry="";
		
		if($instakeywords){
			$keywordArr	=	explode(',',$instakeywords);
			$keyword_query	=	array();
			foreach($keywordArr as $key=>$val){
				$keyword_query[]	=	" (text like '$val%' or text like '%$val%') ";
			}
			$append_qry	=	implode(' or ',$keyword_query);
			$inst_append_qry	.=	" and ($append_qry)";
		}
		
		$topInstasql = "SELECT * FROM user_instagram_feed
										WHERE user_id=".$this->session->userdata("userid")." $inst_append_qry ORDER BY created_time desc";	
		$data['topchartINStpht'] =	$this->model->allSelects($topInstasql);
		
		$this->load->view('staff/load/InstaTopPhotos',$data);
 }
 
 # Misc Function
 public function getDropValues()
 {

	$itemID = $this->session->userdata("userid");

	/*$sql = "Select u.userid,u.FirstLastname
			from users u
			where (u.userid NOT IN (Select companyID from company_ref where userid=$itemID)) AND
			(u.userid NOT IN (Select companyID from user_prostaff_sponsors where prostaff_id=$itemID))
			AND u.memtype='Employer' ORDER BY u.FirstLastname asc";*/
			
	$sql="Select u.userid,u.FirstLastname FROM users u WHERE u.memtype='Employer' ORDER BY u.FirstLastname asc";
    $TempInfo = $this->model->allSelects($sql);

	$result=array();
		foreach($TempInfo as $t){
			//$result[$t['userid']]=$t['FirstLastname'];
			$result[$t['FirstLastname']]=$t['userid'];
		}								
	//usort($result);
	echo json_encode($result);			
 }

 public function getSponsorInfo(){

	$userID = $this->input->post('user_id');
	$TransMode = $this->input->post('mode');

	if($TransMode=='Add'){
		$sql = "SELECT u.userid,u.FirstLastname,u.fbkeywords,u.twtkeywords,
						u.instakeywords,u.youtubekeywords,u.user_logo,u.companyID,'false' AS error 
						FROM users u WHERE u.userid=$userID";
	}
	if($TransMode=='Edit'){
		$sql = "SELECT companyID,sponsorname as FirstLastname,fbkeywords,twitterkeywords as twtkeywords,
						instakeywords,youtubekeywords,sponsor_logo as user_logo
						FROM user_prostaff_sponsors where ps_id=$userID";
	}
			
    $TempInfo = $this->model->allSelects($sql);
	
	$data['mode'] = $TransMode;
	
	if(count($TempInfo)>0){
		//$returnArr = $TempInfo[0];
		$data['keywords'] = $TempInfo[0];
	}
	//echo json_encode($returnArr);		
	$this->load->view('staff/add-sponsors-tags',$data);
 }


#Manage Prostaff
 public function allStaff(){
 
		if (@$_POST) {
            $search_by = $_POST['formelement'];
            $search_value = $_POST['formvalue'];
         } 

			$sql="Select DISTINCT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings,cr.activation_reminder_date,
						(Select IFNULL(count(social_type),0) from user_media where userid=u.userid and social_type='facebook') AS FB, 
						(Select IFNULL(count(social_type),0) from user_media where userid=u.userid and social_type='twitter') AS twt, 
						(Select IFNULL(count(social_type),0) from user_media where userid=u.userid and social_type='instagram') AS Insta
						FROM users u
						LEFT JOIN company_ref cr ON cr.userid=u.userid
						where u.status='Active' and cr.companyID=".$this->session->userdata("userid");

		if($search_by =='bydisplay' && $search_value=='Active' && strlen($search_value)>0){

		$sql="SELECT DISTINCT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings,cr.activation_reminder_date,
					1 AS FB, 1 AS twt, 1 as Insta
					FROM users u
					LEFT JOIN company_ref cr ON cr.userid=u.userid
					WHERE u.status='Active' and cr.companyID=".$this->session->userdata("userid")." AND u.userid IN (Select userid from user_media um2)";
		
		}
		if($search_by =='bydisplay' && $search_value=='inactive' && strlen($search_value)>0){
		
		$sql="SELECT DISTINCT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings,
					cr.activation_reminder_date,0 AS FB, 0 AS twt, 0 as Insta
					FROM users u
					LEFT JOIN company_ref cr ON cr.userid=u.userid
					WHERE u.status='Active' and cr.companyID=".$this->session->userdata("userid")." AND u.userid NOT IN(Select userid from user_media um2)";
		}
		
		if($search_by	==	'orderby' && strlen($search_value)>0){
			$sql	.=	" ORDER BY u.FirstLastname $search_value";				
		}

		else if($search_by	==	'fishingtype' && strlen($search_value)>0){
			$sql	.=	" and u.fishingtype='".$search_value."'";				
		}

		else if($search_by	==	'fishingorganization' && strlen($search_value)>0){
			$sql	.=	" and u.fishingorganization='".$search_value."'";
		}

		else if($search_by	==	'bystate' && strlen($search_value)>0){
			$sql	.=	" and u.state='".$search_value."'";
		}

		else if($search_by	==	'ratings' && strlen($search_value)>0){
			$sql	.=	" AND cr.staffRatings = $search_value";
		}
		
		else if($search_by	==	'byregdate' && strlen($search_value)>0){
			$sql	.=	" ORDER BY u.registration_date $search_value";				
		}

		else{
			$sql	.=	" ORDER BY u.FirstLastname asc";
		}
		
		$data["stafflist"] = $this->model->allSelects($sql);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
				
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
		$data['content']=$this->load->view('staff/managestaff',$data,true);	
		$this->load->view('layout-inner',$data);

 }


#ADD PRO-STAFF

 public function newstaff($common_id=0){
	
	    $this->form_validation->set_rules('txtname', 'Full Name', 'required');
        $this->form_validation->set_rules('txtemail', 'Email', 'required|valid_email');
		$this->form_validation->set_message('required', '%s');

		if($common_id == 0){
			$this->form_validation->set_rules('txtpassword', 'Password', 'required');
		}

 		$fbkeywords		 = getKeywords($this->session->userdata("userid"),'fbkeywords');
		$twtkeywords 	 = getKeywords($this->session->userdata("userid"),'twtkeywords');	
		$instakeywords	 = getKeywords($this->session->userdata("userid"),'instakeywords');
		$youtubekeywords = getKeywords($this->session->userdata("userid"),'youtubekeywords');

		if ($this->form_validation->run() == TRUE){		
			
            $sql = "select * from users where email='" . $this->input->post('txtemail') . "'";
            $checkuser = $this->model->allSelects($sql);

            if (count($checkuser) <= 0) {

                $pos = strpos($this->input->post('txtemail'), "@", 0);
                $loginname = substr($this->input->post('txtemail'), 0, $pos);
								
				$dob ='';
				
				if( $this->input->post('txtdob')=="" ){	$dob ='1980-01-05';	}
				else { $dob = $this->input->post('txtdob'); }
				
                $user = array(
                    'FirstLastname' => $this->input->post('txtname'),
					'dob' => $dob,
                    'email' => $this->input->post('txtemail'),
					'compname'=>'NA',
                    'phone' => $this->input->post('txtcontactno'),
                    'loginname' => $loginname,
                    'password' => md5($this->input->post('txtpassword')),
                    'street' => $this->input->post('txtstreet'),
                    'city' => $this->input->post('txtcity'),
                    'state' => $this->input->post('txtstate'),
					'fbkeywords'=>$fbkeywords,
					'twtkeywords'=>$twtkeywords,
					'instakeywords'=>$instakeywords,
					'youtubekeywords'=>$youtubekeywords,
                    'zipcode' => $this->input->post('txtzipcode'),
                    'status' => 'Active',
                    'memtype' => 'Staff'
                );

                //$this->model->allInserts('users',$user);
				$lastID = $this->model->allInsertsRetID('users', $user);
				
				$insertHistory = array(
					'userid' => $lastID,
					'email' =>  $this->input->post('txtemail'),
					'fromPage' => 'newstaff else staff.php',	
					'password' => $this->input->post('txtpassword'),
					'date_update' => date('Y-m-d H:i:s')						
				);
				$this->db->insert('user_pwd_track',$insertHistory);

                $compref = array(
                    'userid' => $lastID,
                    'compname' => $this->session->userdata("FirstLastname"),
                    'companyID' => $this->session->userdata("userid"),
					'fbkeywords' => $fbkeywords,
					'twitterkeywords' => $twtkeywords,
					'instakeywords' => $instakeywords,
					'youtubekeywords'=>$youtubekeywords,
                    'ref_status' => 'Active'
                );

                $this->model->allInserts('company_ref', $compref);

		 if(count($infoTemp)>0){

			 $style = "style='padding: 10px;margin: 0 auto 20px 0;'";
			 $empImageLogo = "<img src='https://anglertrack.net/".$this->session->userdata('user_logo')."' alt='".$this->session->userdata('FirstLastname')."' ".$style." />";
					
					$emailBody = file_get_contents(APPPATH.'views/templates/email/notify_emp_new_pro.tpl');
					$emailBody = str_replace('%%EMPNAMELOGO%%',$empImageLogo, $emailBody);
					$emailBody = str_replace('%%FIRSTLASTNAMEPRO%%',$user['FirstLastname'], $emailBody);
					$emailBody = str_replace('%%BODYTEXT%%', $infoTemp['bodytext'], $emailBody);
					$emailBody = str_replace('%%EMAIL%%', $user['email'], $emailBody);
					$emailBody = str_replace('%%PASSWORD%%',$this->input->post('txtpassword'), $emailBody);
					$emailBody = str_replace('%%SIGNATURE%%',$infoTemp['signature'], $emailBody);
								
					$subject = $infoTemp['subject'];
					$attachment = "";$attachment1 = "";$attachment2 = "";
										
					if($infoTemp['attachment']!=""){
						$attachment = $_SERVER["DOCUMENT_ROOT"]."/".$infoTemp['attachment'];
					}
		
					if($infoTemp['attachment1']!=""){
						$attachment1 = $_SERVER["DOCUMENT_ROOT"]."/".$infoTemp['attachment1'];
					}
		
					if($infoTemp['attachment2']!=""){
						$attachment2 = $_SERVER["DOCUMENT_ROOT"]."/".$infoTemp['attachment2'];
					}
															
					$this->emailSendOneByOneAttch($subject,$emailBody, $user['email'],$user['FirstLastname'],$attachment,$attachment1,$attachment2);

			}
				
				$this->session->set_flashdata('sucess', 'Staff Added Successfully..!! Add Another? Click Add New.');
				redirect('staff/allstaff');
			
            } else {

                $compref = array(
                    'userid' => $checkuser[0]['userid'],
                    'compname' => $this->session->userdata("FirstLastname"),
                    'companyID' => $this->session->userdata("userid"),
					'fbkeywords' => $fbkeywords,
					'twitterkeywords' => $twtkeywords,
					'instakeywords' => $instakeywords,
					'youtubekeywords'=>$youtubekeywords,
                    'ref_status' => 'Active'
                );

                $this->model->allInserts('company_ref', $compref);

                $this->session->set_flashdata('sucess', 'Staff Added Successfully..!! Add Another? Click Add New.');
                redirect('staff/allstaff');
            }
		}
		else{
		
			if(isset($_POST) && count($_POST)>0){
				foreach($_POST as $key=>$val){
					$data[$key]	=	$val;
				}				
			}
			else if($common_id>0){
			
				$sql	  =	"SELECT * FROM users WHERE userid=$common_id AND memtype='Staff'";
				$TempInfo =	$this->model->allSelects($sql);
				
					if(count($TempInfo)<=0){
						redirect('staff/allstaff');
					}
				$ContInfo =	$TempInfo[0];	

				$data['txtname']	  =	$ContInfo['FirstLastname'];
				$data['txtdob']		  =	$ContInfo['dob'];	
				$data['txtemail']	  =	$ContInfo['email'];		
				$data['txtcontactno'] =	$ContInfo['phone'];						
				$data['txtstreet']	  =	$ContInfo['street'];						
				$data['txtcity']	  =	$ContInfo['city'];						
				$data['txtstate']	  =	$ContInfo['state'];						
				$data['txtzipcode']	  =	$ContInfo['zipcode'];						

			}		
			else{
				$data['txtname']	  =	'';
				$data['txtdob']		  =	'';	
				$data['txtemail']	  =	'';		
				$data['txtcontactno'] =	'';						
				$data['txtstreet']	  =	'';						
				$data['txtcity']	  =	'';						
				$data['txtstate']	  =	'';						
				$data['txtzipcode']	  =	'';						
			}	
		}					
		
		$data['form_error']	= validation_errors();
		$data['common_id']	= $common_id;

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
				
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
		$data['content']=$this->load->view('staff/add-newstaff',$data,true);	
		$this->load->view('layout-inner',$data);
 }

# STAFF Ratings

 public function ratings(){
	
	 if(@$_POST){
	 
		 if($_POST['staffid']!="" && $_POST['score']!=""){
			$qstr = "update company_ref set staffRatings=" . $_POST["score"] . " where userid=".$_POST["staffid"]." and companyID=".$this->session->userdata("userid");
			$this->model->allQueries($qstr);
			$this->session->set_flashdata('sucess', 'Ratings Updated Sucessfully..!!');
		}
	}	
  }

 public function viewstaff($staff_id){

		if($staff_id == "" || $staff_id==0)
		{ redirect('employer/allstaff');}
		
		$search_staff = $staff_id;
		
		$widgetTlist	=	array();		
		$widgetLelist	=	array();		
		$widgetWelist	=	array();		
		
		if($search_staff >0 ){			
			
			#webentry
			$sql = "Select * from user_blog ub
							LEFT JOIN users u on ub.userid=u.userid
							LEFT JOIN company_ref cr on ub.userid=cr.userid
							where u.userid=$search_staff and cr.companyID=".$this->session->userdata("userid");
			$widgetWelist = $this->model->allSelects($sql);	

			$sql="select IFNULL(sum(visitors),0) as impression_total FROM user_customscoops 
				  	WHERE find_in_set('".$search_staff."',user_id) and find_in_set('".$this->session->userdata("userid")."', CS_sponsorID)";	  
			$widgetCSlist = $this->model->allSelects($sql);
			
			#Tournament						
	        $sql = "Select * from tournaments t LEFT JOIN users u on t.staffid=u.userid
									LEFT JOIN company_ref cr on t.staffid=cr.userid
									where u.userid=$search_staff and cr.companyID=" . $this->session->userdata("userid");					
			$widgetTlist = $this->model->allSelects($sql);	
			
			#Leads									
	        $sql = "Select * from user_lead up
									LEFT JOIN users u on up.userid=u.userid
									LEFT JOIN company_ref cr on up.userid=cr.userid
									where u.userid=$search_staff and  cr.companyID=" . $this->session->userdata("userid");
			$widgetLelist = $this->model->allSelects($sql);				
		}       

        $sql = "SELECT u.userid, u.FirstLastname,u.at_profile_pic,u.city,u.state,u.phone,cr.staffRatings,cr.refid,cr.companyID,
								IFNULL((Select count(userid) from user_media where userid=u.userid),0) as mediaAuthorized 
								FROM users u
								LEFT JOIN company_ref cr ON cr.userid=u.userid
								where cr.companyID=".$this->session->userdata("userid")." AND u.userid=".$search_staff;
        $staffdetails = $this->model->allSelects($sql);
				        
				$data['SearchStaffInfo'] = $staffdetails[0];				
				$data['widgetTlist'] = $widgetTlist;
        $data['widgetLelist'] = $widgetLelist;
        $data['widgetWelist'] = $widgetWelist;
				$data['widgetCSlist'] = $widgetCSlist;
							
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
				
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
		$data['content']=$this->load->view('staff/view-staff',$data,true);	
		$this->load->view('layout-inner',$data);
  
 }

# Pro-Staff Contact Info.

public function viewContactInfo() {

		$userid = $_POST['userid'];
        $sql = "SELECT userid,FirstLastname,email,phone,street,city,state,zipcode FROM users WHERE userid=". $userid;
        $staffdetails = $this->model->allSelects($sql);
				        
        $data["proStaffDetail"] = $staffdetails[0];
        if (count($data["proStaffDetail"]) == 0) {
            redirect("employer/allstaff");
        }
		$this->load->view('staff/view-staff-contactinfo',$data);

}

 public function staffstatus(){
	
	if(isset($_POST["userid"],$_POST['action'])){

	   $staff_id 	= $_POST["userid"];
       $action 	= $_POST['action'];
			
			if($action == 'Delete'){
				
				$qstr = "DELETE FROM company_ref WHERE userid=".$staff_id." AND companyID=".$this->session->userdata("userid");			
				$this->model->allQueries($qstr);	
				$this->session->set_flashdata('sucess', 'ProStaff Deleted Sucessfully..!!');					
			}
	
	}else{
	                        
    	 echo "<strong>Invalid Attempt.!!</strong>";
 	}
 }

# FOR Notifications

#===================

 public function notifications() {

		$sql="SELECT * FROM Notif_templates WHERE ownerid=".$this->session->userdata("userid");
        $data["notifLists"] = $this->model->allSelects($sql);

		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
							'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts',
							'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
								));
		
		$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
		$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
								
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$data["fblink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
		$data["twlink"]=$this->model->allSelects($sql);
		
		$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
		$data["instalink"]=$this->model->allSelects($sql);

		$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
		$data["youtubelink"]=$this->model->allSelects($sql);
				
 		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
		$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
		$data['content']=$this->load->view('staff/manage-notifications',$data,true);	
		$this->load->view('layout-inner',$data);
		
	}

 public function creatTemplates(){

 	$sql = "SELECT * FROM Notif_templates WHERE ownerid=".$this->session->userdata("userid");
	$TempExists	=	$this->model->allSelects($sql);

	if(count($TempExists)>0) { 
		$this->session->set_flashdata('sucess', 'Template Blocks Already Exists..!!');
		redirect('staff/notifications');
	}

	  for($i=0;$i<7; $i++){

				 $type="";

				 if($i==0) { $type="News";} if($i==1) { $type="Events";}
				 if($i==2) { $type="Message";} if($i==3) { $type="Training";}
				 if($i==4) { $type="Welcome";} if($i==5) { $type="Thank You";}
				 //if($i==6) { $type="Accepted";} if($i==7) { $type="Rejected";}
				 if($i==6) { $type="SocialPrompt";} 

				 $template = array(
					'ownerid' => $this->session->userdata("userid"),
					'subject' => "Notification",
					'bodytext' => "Default",
					'signature' => "Default",
					'type' => $type,
					'status' => "Block"
				);	

          $this->model->allInserts('Notif_templates', $template);

 			}

			$this->session->set_flashdata('sucess', 'Template Block created Successfully..!!');
			redirect('staff/notifications');
 }


 public function addTemplate() {

	$tempRef = $_POST['tempRef'];

	 $template = array(
			'ownerid' => $this->session->userdata("userid"),
			'subject' => "Notification",
			'bodytext' => "Default",
			'signature' => "Default",
			'type' => $tempRef,
			'status' => "Block"
			);	

		$this->model->allInserts('Notif_templates', $template);
		$this->session->set_flashdata('sucess', 'Template Block created Successfully..!!');
		//redirect('employer/notifications');

 }

 public function updateTemplates($tempid=0) {

        $this->form_validation->set_rules('txtsubject', 'Subject Line', 'required');
        $this->form_validation->set_rules('txtbodytext', 'Email Body Text', 'required');
        $this->form_validation->set_rules('txtsignature', 'Signature', 'required');

		 $update_attachment =""; $update_attachment1=""; $update_attachment2="";
		 
        if ($this->form_validation->run() == TRUE) {

            if ($_FILES['attachment']['error'] == 0) {
                @unlink($update_user_document);
                $fileName = $_FILES['attachment']['name'];
                $file = 'attachment';
                $upload = $this->uploadFile($file, '*', 'assets/upload/emp_document', $fileName);
                $update_attachment = $upload['path'];
            }

            if ($_FILES['attachment1']['error'] == 0) {
                @unlink($update_user_document);
                $fileName = $_FILES['attachment1']['name'];
                $file = 'attachment1';
                $upload = $this->uploadFile($file, '*', 'assets/upload/emp_document', $fileName);
                $update_attachment1 = $upload['path'];
            }

            if ($_FILES['attachment2']['error'] == 0) {
                @unlink($update_user_document);
                $fileName = $_FILES['attachment2']['name'];
                $file = 'attachment2';
                $upload = $this->uploadFile($file, '*', 'assets/upload/emp_document', $fileName);
                $update_attachment2 = $upload['path'];
            }
		
			$templateInfo = array(
					'subject' => $this->input->post('txtsubject'),
					'bodytext' => $this->input->post('txtbodytext'),
					'signature' => $this->input->post('txtsignature'),
					'type' => $this->input->post('txttype'),
					'attachment' => $update_attachment,
					'attachment1' => $update_attachment1,
					'attachment2' => $update_attachment2,
					'ownerid' => $this->session->userdata("userid"),
					'status' => $this->input->post('enadisable')
				);

			if($tempid>0){
				$this->model->allUpdate('id',$tempid,'Notif_templates',$templateInfo);
				$this->session->set_flashdata('sucess', 'Template Updated Successfully..!!');
				redirect('staff/notifications');
			}

			else{		
				redirect('staff/notifications');
			}		
        } 
		else {

					if(isset($_POST) && count($_POST)>0){
							foreach($_POST as $key=>$val){
								$data[$key]	=	$val;
							}				
					 }
					else if($tempid>0){

							$sql	=	"select * from Notif_templates where id=$tempid";
							$TempInfo	=	$this->model->allSelects($sql);
							$templateInfo	=	$TempInfo[0];				
							$data['txtsubject']		= $templateInfo['subject'];
							$data['txtbodytext']	= $templateInfo['bodytext'];
							$data['txtsignature']	= $templateInfo['signature'];
							$data['txttype']		= $templateInfo['type'];
							$data['attachfile']		= $templateInfo['attachment'];
							$data['attachfile1']	= $templateInfo['attachment1'];
							$data['attachfile2']	= $templateInfo['attachment2'];
							$data['enadisable']		= $templateInfo['status'];

						}		

						else{

							$data['txtsubject']	 =	"";
							$data['txtbodytext'] =	"";
							$data['txtsignature']=	"";
							$data['txttype']	 =	"";
							$data['attachfile']	 =	"";
							$data['attachfile1'] =	"";
							$data['attachfile2'] =	"";
							$data['enadisable']	 =	"Block";

			}	
        }

			$data['loginUrl'] = $this->facebook->getLoginUrl(array(
								'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts',
								'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
									));
			
			$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
			$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
									
			$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
			$data["fblink"]=$this->model->allSelects($sql);
	
			$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
			$data["twlink"]=$this->model->allSelects($sql);
			
			$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
			$data["instalink"]=$this->model->allSelects($sql);
	
			$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
			$data["youtubelink"]=$this->model->allSelects($sql);

      		$data['errorreg'] = trim(strip_tags(validation_errors()));
			$data['tempid']=$tempid;
					
			$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
			$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
			$data['navigation']=$this->load->view('staff/staff-navigation',$data,true);
			$data['content']=$this->load->view('staff/edit-email-notif',$data,true);	
			$this->load->view('layout-inner',$data);

    }


  public function viewtemplate() {

		$tempid = $_POST['tempid'];
		
		$sql = "Select * from Notif_templates where id=" . $tempid;
        $data["tempdetail"] = $this->model->allSelects($sql);

        if (count($data["tempdetail"]) == 0) {
            redirect("employer/notifications");
        }

		$this->load->view('staff/view-template',$data);
	}

 function testTemplate(){

		$tempID	=	$this->input->post('tempID');
		$additionalText = "";

		$returnArr['error']	=	false;
		$returnArr['msg']	=	"";

		$sql= "Select email,notifemail from users where userid=".$this->session->userdata("userid");
		$tempUserinfo = $this->model->allSelects($sql);

		$infoUser = $tempUserinfo[0];

		if($tempID>0){

			$sql = "Select * from Notif_templates where id=" . $tempID;
			$tempInfo = $this->model->allSelects($sql);
			$detailsInfo = $tempInfo[0];

		  	// $config['mailtype'] = 'html';
			// $config['protocol'] = 'sendmail';
			// $config['mailpath'] = '/usr/sbin/sendmail';
			// $config['charset'] = 'utf-8';
			// $config['wordwrap'] = TRUE;
			$this->load->library('email');
			// $this->email->initialize($config);
			$this->email->initialize(array(
				'protocol' => 'smtp',
				'smtp_host' => 'smtp.sendgrid.net',
				'smtp_user' => 'apikey',
				'smtp_pass' => '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',
				'smtp_port' => 587,
				'crlf' => "\r\n",
				'newline' => "\r\n",
				'mailtype' => 'html',
				'wordwrap' => TRUE
				));

			if($detailsInfo['subject']!='Notification' && $detailsInfo['body']!='Default' && $detailsInfo['signature']!='Default'){
				
			#Email Section

				$toemail ="";

				if($infoUser['notifemail']!=""){
					$toemail = $infoUser['notifemail'];
				}
				else{
					$toemail = $this->session->userdata("email");
				}

				$this->email->from('no-reply@anglertrack.net', 'AnglerTrack');
		  	$this->email->to($toemail);
				//$this->email->to('njks5@hotmail.com');
				$this->email->subject($detailsInfo['subject']);		

				if($detailsInfo['type']=='Welcome'){

				/*$additionalText ="\n\n"."Login Information - \n Email: ".'< Pro Staff Email>'." \n Password:".'< Opted Password Info.>'."\n\n Welcome To AnglerTrack\n https://vimeo.com/123164779 \n Watch this video that will show you how to set up your account! \n https://vimeo.com/136522593";*/

				$content1 = "\n\nWebsite: <a href='https://anglertrack.net/'>www.anglertrack.net</a>\n
				Account Setup Instructions: After you log in, click the 'SETUP' button on the left side of the screen. Please READ ALL THE INSTRUCTIONS and complete your account setup using the setup wizard.\n";
				$content2 = "\n Login Information - \n Email: ".$this->input->post('txtemail')." \n Password:".$this->input->post('txtpassword');
				$content3 = "\nIf you have any questions, please email us at support@anglertrack.net";
				$additionalText = $content1.$content2.$content3."\n\n";
				}

				if($detailsInfo['type']=='Accepted'){
					//$additionalText ="\n\n"."Login Information - \n Email: ".'< Pro Staff Email>'." \n Password:".'< Opted Password Info.>'."\n";
					$additionalText = $content1.$content2.$content3."\n\n";
				}

			$emlmsg = 'Dear "Pro-Staff Name here,"'." \n\n".$detailsInfo['bodytext'].$additionalText." \n\n".$detailsInfo['signature'];

			$this->email->message(nl2br($emlmsg));	  	
		  	$this->email->send();

			$returnArr['error']	=	false;
			$returnArr['msg']	=	"Message Sent Sucessfully!!!";

		}	

		else{

			$returnArr['error']	=	true;
			$returnArr['msg']	=	"Kindly Avoid Default Text!!!";
		}
	}

		echo json_encode($returnArr);
}

#EMAIL SECTION

   public function emailSendOneByOneAttch($subject,$body, $email, $recptName,$attach="",$attach1="",$attach2="") {

    //   $config['mailtype'] = 'html';
	// 		$config['protocol'] = 'sendmail';
	// 		$config['mailpath'] = '/usr/sbin/sendmail';
	// 		$config['charset'] = 'utf-8';
	// 		$config['wordwrap'] = TRUE;
	// 		$this->email->initialize($config);
	$this->load->library('email');
	$this->email->initialize(array(
		'protocol' => 'smtp',
		'smtp_host' => 'smtp.sendgrid.net',
		'smtp_user' => 'apikey',
		'smtp_pass' => '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',
		'smtp_port' => 587,
		'crlf' => "\r\n",
		'newline' => "\r\n",
		'mailtype' => 'html',
		'wordwrap' => TRUE
		));
			
            $this->email->from('no-reply@anglertrack.net', 'AnglerTrack');
            $this->email->to($email);
						$this->email->bcc('support@anglertrack.net');
            $this->email->subject($subject);

       			$emlmsg = $body;
            $this->email->message(nl2br($emlmsg));
			
			if($attach!=""){
				$this->email->attach($attach);
			}

			if($attach1!=""){
				$this->email->attach($attach1);
			}

			if($attach2!=""){
				$this->email->attach($attach2);
			}

      $this->email->send();
			$this->email->clear(TRUE);
        
    }

# Manage Store
 public function stores(){
 						
	#Stores
	$sql = "SELECT * FROM user_store 
			WHERE store_userID=".$this->session->userdata("userid")." ORDER BY store_date DESC ";
	$StoreInfo = $this->model->allSelects($sql);
	
	$data["StoreList"] = $StoreInfo;

	$data['loginUrl'] = $this->facebook->getLoginUrl(array(
              			'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
              			'redirect_uri' => site_url().'/staff/facebook_redirect/'));
    $data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
                                    
    $sql="SELECT * FROM user_media WHERE social_type='facebook' AND userid=".$this->session->userdata("userid");
    $data["fblink"]=$this->model->allSelects($sql);
            
    $sql="SELECT * FROM user_media WHERE social_type='twitter' AND userid=".$this->session->userdata("userid");
    $data["twlink"]=$this->model->allSelects($sql);
			      
    $sql="SELECT * FROM user_media WHERE social_type='instagram' AND userid=".$this->session->userdata("userid");
    $data["instalink"]=$this->model->allSelects($sql);	
			
		$data['head']=$this->load->view('templates/head-unicorn-theme','',true);
      	$data['header']=$this->load->view('templates/header-unicorn-theme','',true);
		$data['navigation']=$this->load->view('staff/staff-navigation','',true);			
		$data['content']=$this->load->view('staff/managestores',$data,true);
		$this->load->view('layout-inner',$data);
 }

# ADD STORE
 public function addstore_old($storeid=0) {

        $this->form_validation->set_rules('store_name', 'Store Name is ', 'required');
        $this->form_validation->set_rules('store_date', 'Store Date?', 'required');
		
        if ($this->form_validation->run() == TRUE) {
		
					 $store = array(
									'store_name' => $this->input->post('store_name'),
									'store_date' => $this->input->post('store_date'),
									'store_notes' => $this->input->post('store_notes'),
									'store_followup' => $this->input->post('store_followup'),
									'store_userID' => $this->session->userdata("userid"),
									'store_status' => "Active"
							);
			
			if($storeid>0){
				$this->model->allUpdate('store_id',$storeid,'user_store',$store);
				$this->session->set_flashdata('sucess', 'Store Updated Successfully..!!');
				redirect('staff/stores');
			}
			else{
        $this->model->allInserts('user_store', $store);
        $this->session->set_flashdata('sucess', 'Store Added Successfully..!! Add Another? Click Add New.');
        redirect('staff/stores');
			}		
    }  
		else {
		 
				if(isset($_POST) && count($_POST)>0){
					foreach($_POST as $key=>$val){
							$data[$key]	=	$val;
							}				
						}
						else if($storeid>0){
						
							$sql	=	"SELECT * FROM user_store WHERE store_id=$storeid";
							$TempInfo	=	$this->model->allSelects($sql);
							$DataInfo	=	$TempInfo[0];	
										
							$data['store_name']			=	$DataInfo['store_name'];
							$data['store_date']			=	$DataInfo['store_date'];	
							$data['store_notes']		=	$DataInfo['store_notes'];
							$data['store_followup']	=	$DataInfo['store_followup'];
						
						}		
						else{
						
							$data['store_name']			=	"";
							$data['store_date']			=	"";	
							$data['store_notes']		=	"";
							$data['store_followup']	=	"";
						}	
        }

		
		$data['errorreg'] = trim(strip_tags(validation_errors()));
		$data['storeid']	=	$storeid;
		
		$data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
        $data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);
		$data['loginUrl'] = $this->facebook->getLoginUrl(array(
									'scope'=>'email,public_profile,manage_pages,user_videos,user_photos,user_posts,publish_actions,publish_pages',
									'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
							));
				
				$data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
				$data['googlePlusauthUrl'] = $this->client->createAuthUrl();
										
				$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
				$data["fblink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='twitter' and userid=".$this->session->userdata("userid");
				$data["twlink"]=$this->model->allSelects($sql);
				
				$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
				$data["instalink"]=$this->model->allSelects($sql);
		
				$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
				$data["youtubelink"]=$this->model->allSelects($sql);
			
      $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
			$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
      $data['content'] = $this->load->view('staff/add-new-store', $data, true);
      $this->load->view('layout-inner', $data);
 }

#ADD STORE
 public function addstore($storeid=0) {

    $this->form_validation->set_rules('store_name', 'Store Name is required', 'required');
	$this->form_validation->set_rules('store_companyID', 'Select Store Sponsor is required', 'required');
    $this->form_validation->set_rules('store_date', 'Store Date is required', 'required');
	$this->form_validation->set_message('required', '%s');
		
        if($this->form_validation->run() == TRUE) {

			$storePhotos = $this->input->post('store_photos_existing');
			
            if ($_FILES['store_photos']['error'] == 0) {
                @unlink($existingPhotos);
                $fileName = $_FILES['store_photos']['name'];
                $file = 'store_photos';
                $upload = uploadFile($file, '*', 'assets/upload/store_photos', $fileName);
                $storePhotos = $upload['path'];
            }
		
			$store = array('store_name' => $this->input->post('store_name'),
							'store_companyID' => $this->input->post('store_companyID'),
						    'store_date' => $this->input->post('store_date'),
							'store_notes' => $this->input->post('store_notes'),
							'store_time_CheckIN' => $this->input->post('store_timeCheckIN'),
							'store_chkIn_manager_Duty' => $this->input->post('store_chkIn_managerDuty'),
							'store_chkIn_dept_manager' => $this->input->post('store_chkIn_deptmanager'),
							'store_inventory_level' => $this->input->post('store_inventory_level'),
							'store_comp_win_shelf_space' => $this->input->post('store_comp_win_shelf_space'),
							'store_shelves_condition' => $this->input->post('store_shelves_condition'),
							'store_end_caps_aisle_look' => $this->input->post('store_end_caps_aisle_look'),
							'store_feedback' => $this->input->post('store_feedback'),
							'store_new_product_trend' => $this->input->post('store_new_product_trend'),
							'store_brand_observed_discuss' => $this->input->post('store_brand_observed_discuss'),
							'store_event_name_people' => $this->input->post('store_event_name_people'),
							'store_photos' => $storePhotos,							
							'store_userID' => $this->session->userdata("userid"),
							'store_status' => "Active"
						);
			
			if($storeid>0){
				$this->model->allUpdate('store_id',$storeid,'user_store',$store);
				$this->session->set_flashdata('sucess', 'Store Updated Successfully..!!');
				redirect('staff/stores');
			}
			else{
				$this->model->allInserts('user_store', $store);
				$this->session->set_flashdata('sucess', 'Store Added Successfully..!! Add Another? Click Add New.');
				redirect('staff/stores');
			}		
    }  
		else {
		 
				if(isset($_POST) && count($_POST)>0){
					foreach($_POST as $key=>$val){
							$data[$key]	=	$val;
							}				
						}
						else if($storeid>0){
						
							$sql	  =	"SELECT * FROM user_store WHERE store_id=$storeid";
							$TempInfo =	$this->model->allSelects($sql);
							$DataInfo =	$TempInfo[0];	
										
							$data['store_name']	 = $DataInfo['store_name'];
							$data['store_companyID'] = $DataInfo['store_companyID'];
							$data['store_date']	 = $DataInfo['store_date'];	
							$data['store_notes'] = $DataInfo['store_notes'];
							$data['store_time_CheckIN']		 = $DataInfo['store_timeCheckIN'];
							$data['store_chkIn_managerDuty'] = $DataInfo['store_chkIn_managerDuty'];
							$data['store_chkIn_deptmanager'] = $DataInfo['store_chkIn_deptmanager'];
							$data['store_inventory_level']	 = $DataInfo['store_inventory_level'];
							$data['store_comp_win_shelf_space']	= $DataInfo['store_comp_win_shelf_space'];
							$data['store_shelves_condition']	= $DataInfo['store_shelves_condition'];
							$data['store_end_caps_aisle_look']	= $DataInfo['store_end_caps_aisle_look'];
							$data['store_feedback']	= $DataInfo['store_feedback'];
							$data['store_new_product_trend']	 = $DataInfo['store_new_product_trend'];
							$data['store_brand_observed_discuss']= $DataInfo['store_brand_observed_discuss'];
							$data['store_event_name_people']= $DataInfo['store_event_name_people'];
							$data['store_photos_existing']= $DataInfo['store_photos'];
						
						}		
						else{
						
							$data['store_name']		= '';
							$data['store_companyID'] = '';
							$data['store_date']		= '';	
							$data['store_notes']	= '';
							$data['store_time_CheckIN']	= '';
							$data['store_chkIn_managerDuty'] = '';
							$data['store_chkIn_deptmanager'] = '';
							$data['store_inventory_level']	 = '';
							$data['store_comp_win_shelf_space']	= '';
							$data['store_shelves_condition']	= '';
							$data['store_end_caps_aisle_look']	= '';
							$data['store_feedback']	= '';
							$data['store_new_product_trend']	 = '';
							$data['store_brand_observed_discuss']= '';
							$data['store_event_name_people']= '';
							$data['store_photos_existing']= '';
						}	
        }

	$data['errorreg'] = trim(strip_tags(validation_errors()));
	$data['storeid']  =	$storeid;

	#MANUFACTURER INFO
	$sql = "SELECT u.userid, u.FirstLastname FROM users u
			LEFT JOIN company_ref cr ON cr.companyID=u.userid
			WHERE cr.userid=".$this->session->userdata("userid")." AND cr.ref_status='Active'";
			$TempInfo = $this->model->allSelects($sql);
	$data['sponsorInfo'] = $TempInfo;	

	$data['loginUrl'] = $this->facebook->getLoginUrl(array(
              				'scope'=>'email,public_profile,manage_pages,user_posts,publish_actions,publish_pages',
              				'redirect_uri' => site_url().'/staff/facebook_redirect/'));
    $data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
                                    
    $sql="SELECT * FROM user_media WHERE social_type='facebook' AND userid=".$this->session->userdata("userid");
    $data["fblink"]=$this->model->allSelects($sql);
            
    $sql="SELECT * FROM user_media WHERE social_type='twitter' AND userid=".$this->session->userdata("userid");
    $data["twlink"]=$this->model->allSelects($sql);
			      
    $sql="SELECT * FROM user_media WHERE social_type='instagram' AND userid=".$this->session->userdata("userid");
    $data["instalink"]=$this->model->allSelects($sql);	

	$sql="SELECT * FROM user_media WHERE social_type='youtube' AND userid=".$this->session->userdata("userid");
	$data["youtubelink"]=$this->model->allSelects($sql);

	$data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
	$data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);			
    $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
	$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
    $data['content'] = $this->load->view('staff/add-new-store-nf', $data, true);
    $this->load->view('layout-inner', $data);
 }

# ViewStore
 public function viewstore(){
	
		$storeID = $_POST['storeID'];
		$sql = "Select * from user_store where store_id=".$storeID;
    
		$data["storedetails"] = $this->model->allSelects($sql);
      if (count($data["storedetails"]) == 0) {
          redirect("staff");
      }
		$this->load->view('staff/view-store',$data);
}

#Manage Store Status
 public function storestatus(){
      
	if(isset($_POST["storeID"], $_POST["status"])){
		
		if($_POST["status"] == 'Remove'){
			$qstr="DELETE from user_store WHERE store_id=".$_POST["storeID"];
		}
		$this->model->allQueries($qstr); 
				
		if($_POST["status"] == 'Remove'){
			$this->session->set_flashdata('sucess', 'Store Removed Sucessfully..!!');             			
		}
	 }
 }

#MANAGE INSTAGRAM ACCOUNTS
 public function addInstaAccounts() {

	// $sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
	// $data = $this->model->allSelects($sql);
	// $access_token_fb = $data[0]['access_token'];
	// $fb_user_id = $data[0]['social_id'];
	// $this->config->load('facebook'); 
	// $fb = new \Facebook\Facebook([
	// 	'app_id' => '410761562312101',
	// 	'app_secret' => 'af3b1e569a37c23308940a3733c094a5',
	// 	'default_graph_version' => 'v2.10'
	//   ]);
	
	//   try {
	// 	$response = $fb->get('/me?fields=id,name,picture',$access_token_fb);
	//   } catch(\Facebook\Exceptions\FacebookResponseException $e) {
	// 	// When Graph returns an error
	// 	echo 'Graph returned an error: ' . $e->getMessage();
	// 	exit;
	//   } catch(\Facebook\Exceptions\FacebookSDKException $e) {
	// 	// When validation fails or other local issues
	// 	echo 'Facebook SDK returned an error: ' . $e->getMessage();
	// 	exit;
	//   }
	//   $me = $response->getGraphUser();
	//   $data['logged_in_user_name'] = $me->getName();
	//   $data['logged_in_user_id'] = $me->getId();
	//   $data['logged_in_user_profilepic'] = $me->getPicture()->getUrl();
	$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
	$connIG = $this->model->allSelects($sql);
	$data['connIG'] = $connIG;	
	$feeds = [];
	// getting account type info if authenticated
	// getting saved feeds if authenticated
	if(isset($connIG) and count($connIG) != 0) {

	$url = 'https://graph.instagram.com/'.$connIG[0]['social_id'].'?fields=id,username,account_type&access_token='.$connIG[0]['access_token'].'';
	$crl = curl_init();
	
	curl_setopt($crl, CURLOPT_URL, $url);
	curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
	curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
	$response = curl_exec($crl);
	
	if(!response){
		die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
	}
	curl_close($crl);
	$response = json_decode($response);
	$data['accountType'] = $response->account_type;


	}
	//$data['IG_Feeds'] = $feeds;
	
	$data['insta_basic_url'] = 'https://api.instagram.com/oauth/authorize
	?client_id=775682203383178
	&redirect_uri='.base_url().'index.php/staff/instabasicauth
	&scope=user_profile,user_media&response_type=code';




	// $user = $this->facebook->getUser();
	// // echo $this->session->userdata('name');
	// // // print_r($user);
	// //  die('ehere');
	
	// //$acessToken = $this->facebook->getAccessToken();
	// $sql = "Select * from user_media where userid=".$this->session->userdata("userid")." and social_type='facebook' and status='Active'";
	// $data = $this->model->allSelects($sql);
	// // echo '<pre>';
	// // print_r($data);
	// $acessToken = $data[0]["access_token"];
	// // echo $acessToken;
	// // die('her');
	// //$acessToken = $this->facebook->getAccessTokenFromCode($acessToken, 'https://anglertrack.net/index.php/staff/facebook_redirect/');
	
	// $res = $this->facebook->api('/me/accounts','GET',array('access_token'=> $acessToken));
	// // echo $acessToken;
	// // echo '<pre>';
	// // print_r($res);
	// // die('herere');
	// $business = 0;
	// foreach($res['data'] as $single) {
	// 	// this call is to get the facebook page Instgarm business information
	// 	$new_call = $this->facebook->api($single['id'],'GET', array('fields'=>'instagram_business_account', 'access_token'=> $acessToken));
	// 	if(isset($new_call['instagram_business_account']) and count($new_call['instagram_business_account']) != 0) {
	// 		// this means that the connected instagram account is business Account has one of the facebook page connected to instaram business account
	// 		$business++;
	// 	} else {
	// 		// do nothing
	// 	}
	// }

	// if($business > 0 and $business != 0) {
	// 	// if the account is business we do not need to relogin and re-authenicate.
	// 	$data['insta_logged_in_acc_type'] = 'The logged in instagram account is Business or Creator account';
		
	// } else {
	// 	// however if the account is personal we need to authenticate the instagram account again.
	// 	$data['insta_logged_in_acc_type'] = 'The connected instagram account with Facebook is Personal';
	// }
	
	// echo '<pre>';
	// print_r($acessToken);
	// print_r($res);
	// print_r($ress);
	// die('here');


    // $userID = $this->session->userdata("userid");
	// $tblID = 0;

	// $sql = "SELECT * FROM user_instagram_usernames WHERE userid=$userID";
	// $tempInfo = $this->model->allSelects($sql);
	// if(count($tempInfo)>0){
	// 	//$data['tblID'] = $tempInfo[0]['tblID'];
	// 	$tblID = $tempInfo[0]['tblID'];
	// }	
	
	// $this->form_validation->set_rules('user_name1', 'User Name 1 is required', 'required');
	// //$this->form_validation->set_rules('user_name2', 'User Name 2 is required', 'required');
	// //$this->form_validation->set_rules('user_name3', 'User Name 3 is required', 'required');
	// $this->form_validation->set_message('required', '%s');
		
    // if($this->form_validation->run() == TRUE){

	// 		$instaUsernames = array('userid' => $userID,
	// 					    		'username1' => $this->input->post('user_name1'),
	// 								'username2' => $this->input->post('user_name2'),
	// 								'username3' => $this->input->post('user_name3'),
	// 								'status' => "Active"
	// 								);
	// 		$tblID = $this->input->post('tbl_ID');
			
	// 		if($tblID>0){
	// 			$this->model->allUpdate('tblID',$tblID,'user_instagram_usernames',$instaUsernames);
	// 			$this->session->set_flashdata('sucess', 'Updated Successfully..!!');
	// 			redirect('staff');
	// 		}
	// 		else {
	// 			$this->model->allInserts('user_instagram_usernames', $instaUsernames);
	// 			$this->session->set_flashdata('sucess', 'Added Successfully..!!');
	// 			redirect('staff');
	// 		}		
    // }  
	// 	else {
		 
	// 			if(isset($_POST) && count($_POST)>0){
	// 				foreach($_POST as $key=>$val){
	// 						$data[$key]	=	$val;
	// 						}				
	// 					}
	// 					else if($tblID>0){
						
	// 						$sql	  =	"SELECT * FROM user_instagram_usernames WHERE tblID=$tblID AND userid=$userID";
	// 						$TempInfo =	$this->model->allSelects($sql);
	// 						$DataInfo =	$TempInfo[0];	
										
	// 						$data['user_name1']	 = $DataInfo['username1'];
	// 						$data['user_name2']	 = $DataInfo['username2'];	
	// 						$data['user_name3'] = $DataInfo['username3'];
						
	// 					}		
	// 					else{
						
	// 						$data['user_name1']	= '';
	// 						$data['user_name2']	= '';	
	// 						$data['user_name3'] = '';
	// 					}	
    //     }

	// $data['errorreg'] = trim(strip_tags(validation_errors()));
	// $data['tblID'] =$tblID;

	// $data['loginUrl'] = $this->facebook->getLoginUrl(array(
    //           				'scope'=>'instagram_basic,pages_show_list',
    //           				'redirect_uri' => site_url().'/staff/facebook_redirect/'));
	// $data['InstagramloginUrl'] = $this->instagram->getLoginUrl();
	// echo $data['InstagramloginUrl'];
	// die('heree');
	// $data['insta_url'] = 'https://api.instagram.com/oauth/authorize
	// ?client_id=966276640438286
	// &redirect_uri=https://anglertrack.urtestsite.com/index.php/staff/instagram_redirect/
	// &scope=user_profile,user_media
	// &response_type=code';
	// $data['insta_url'] = 'https://api.instagram.com/oauth/authorize
	// ?client_id=966276640438286
	// &redirect_uri=https://anglertrack.urtestsite.com/index.php/staff/instagram_redirect/
	// &scope=user_profile,user_media
	// &response_type=code';
                                    
    // $sql="SELECT * FROM user_media WHERE social_type='facebook' AND userid=".$this->session->userdata("userid");
    // $data["fblink"]=$this->model->allSelects($sql);
            
    // $sql="SELECT * FROM user_media WHERE social_type='twitter' AND userid=".$this->session->userdata("userid");
    // $data["twlink"]=$this->model->allSelects($sql);
			      
    // $sql="SELECT * FROM user_media WHERE social_type='instagram' AND userid=".$this->session->userdata("userid");
    // $data["instalink"]=$this->model->allSelects($sql);	

	// $sql="SELECT * FROM user_media WHERE social_type='youtube' AND userid=".$this->session->userdata("userid");
	// $data["youtubelink"]=$this->model->allSelects($sql);

	$data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
	$data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);			
    $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
	$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
	$data['content'] = $this->load->view('staff/add-instagram-accounts', $data, true);

    $this->load->view('layout-inner', $data);
 }

#Website Logout
#==============
 public function logout(){
   
	 	//@session_destroy();	
        //$this->session->unset_userdata('token');
        $this->session->unset_userdata('user_id');
        $this->session->unset_userdata('memberlogged');
        $this->session->sess_destroy();
		redirect('home') ;            
   }

   public function instabasicauth() {
	   //this is the redirect uri which will be hit once we will authorize the personal account. This url will be hit with code parameter in the GET request and we need to use that code to get a long loived access token from IG api.
	  
		 $code = $_GET['code'];
		$code = $this->input->get('code');
		
		//receive OAuth token object
		$data = $this->instagram->getOAuthToken($code);
		// echo '<pre>';
		// print_r($data);
		$IG_userid = $data->user_id;

		$url = 'https://graph.instagram.com/access_token?grant_type=ig_exchange_token&client_secret=fbcc7c2de4107da631df5e0a6a2d0d3a&access_token='.$data->access_token.'';
		
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $url);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$res = curl_exec($crl);
		
		
		if(!$res){
			die('Error: "' . curl_error($crl) . '" - Code: ' . curl_errno($crl));
		}
		curl_close($crl);
		$res = json_decode($res);
		// echo '<pre>';
		// var_dump($res);
		// die('hereee');
		// 
		$this->instagram->setAccessToken($res);
		// die('hererer12');echo $this->instagram->getAccessToken();
		









		$url = 'https://graph.instagram.com/'.$IG_userid.'?fields=id,username,account_type&access_token='.$this->instagram->getAccessToken().'';
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $url);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($crl);
		
		if(!response){
			die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
		}
		curl_close($crl);
		$response = json_decode($response);

		// echo '<pre>';
		// print_r($response);
		// die('hert');
	

		#Check To confirm progress
		if($response->id==""){
			$this->session->set_flashdata('error', "Not Authorized. Unexpected Error! Please Logout Instagram and Try again.");
			redirect('staff/newsocialpage');
		}	
				
		if($data->access_token==""){
			$this->session->set_flashdata('error', "Authorization Failed. Please Logout Instagram and Try again.");
			redirect('staff/newsocialpage');				
		}	
		$sqll = "DELETE from user_media where userid=".$this->session->userdata("userid")." AND social_type='instagram';";
		$delete_prev = $this->model->allQueries($sqll);
		$insertArray = array(
					'userid'=>$this->session->userdata("userid"),
					'username'=> $response->username,
					'fullname'=> $response->full_name,
					'social_id'=> $IG_userid,
					'image' => '--',
					'created_date' => date('Y-m-d H:i:s'),
					'social_type' =>'instagram',
					'access_token' => $this->instagram->getAccessToken(),
					'status'=>'Active'
				);
			$this->db->insert('user_media', $insertArray);

		// getting instgram feeds:
		// 
		// echo '<pre>';
		// print_r($response);
		// die('here');
		$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
		$fb_det = $this->model->allSelects($sql);
		$fb = new \Facebook\Facebook([
			'app_id' => '410761562312101',
			'app_secret' => 'af3b1e569a37c23308940a3733c094a5',
			'default_graph_version' => 'v2.10'
		]);

		if($response->account_type == 'MEDIA_CREATOR' or $response->account_type == 'BUSINESS') {
			
			// in this case you need to use graph api to get feeds and their likes as well
			if($fb_det[0]['access_token'] != '') {
				try {
					$response = $fb->get('/'.$IG_userid.'/media',$fb_det[0]['access_token']);
				  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
					// When Graph returns an error
					echo 'Graph returned an error: ' . $e->getMessage();
					exit;
				  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
					// When validation fails or other local issues
					echo 'Facebook SDK returned an error: ' . $e->getMessage();
					exit;
				  }
				//   echo '<pre>';
				//   print_r($response->getDecodedBody());
				  $IG_media_set = $response->getDecodedBody();
				  foreach($IG_media_set['data'] as $single) {
					$feedArray = [];
					// getting feeds like and comments counts:
					$url = 'https://graph.facebook.com/v10.0/'.$single['id'].'/?fields=caption,media_product_type,permalink,thumbnail_url,id,media_type,media_url,owner,timestamp,comments_count,like_count&access_token='.$fb_det[0]['access_token'].'';
					$crl = curl_init();
					
					curl_setopt($crl, CURLOPT_URL, $url);
					curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
					curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
					$response = curl_exec($crl);
					
					if(!response){
						die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
					}
					
					curl_close($crl);
					// data returned as json formatted object so need conversion
					$format = json_decode($response);
					$feedArray = array(
						'id' => $format->id,
						'user_id' => $this->session->userdata("userid"),
						'text' => ($format->caption) ? $format->caption : '',
						'link' => $format->permalink,
						'likes' => $format->like_count?$format->like_count:0,		
						'created_time' => strtotime($format->timestamp),
						'comments_count' => $format->comments_count,
						'likes_count' => $format->like_count?$format->like_count:0,
						'thumbnail_image' => $format->media_url,
						'standard_image' => $format->media_url,
				);
				$this->db->insert('user_instagram_feed',$feedArray);
				
	
				  }

			} else {

				// you will only be able to get normal media from IG:
		$url = 'https://graph.instagram.com/'.$IG_userid.'/media?access_token='.$this->instagram->getAccessToken().'';
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $url);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($crl);
		
		if(!response){
			die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
		}
		
		curl_close($crl);
		// data returned as json formatted object so need conversion
		$formatted = json_decode($response);

		foreach($formatted->data as $single) {
			// getting info about media from IG
			$feedArray = [];
			$url = 'https://graph.instagram.com/'.$single->id.'?fields=caption,id,media_type,media_url,permalink,thumbnail_url,username,timestamp&access_token='.$this->instagram->getAccessToken().'';
			$crl = curl_init();
			
			curl_setopt($crl, CURLOPT_URL, $url);
			curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
			curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
			$response = curl_exec($crl);

			$formatted_feed = json_decode($response);
			// echo '<pre>';
			// print_r($formatted_feed);
			$feedArray = array(
							'id' => $formatted_feed->id,
							'user_id' => $this->session->userdata("userid"),
							'text' => ($formatted_feed->caption) ? $formatted_feed->caption : '',
							'link' => $formatted_feed->permalink,
							'likes' => $formatted_feed->likes?$formatted_feed->likes:0,
							'comments_count' => 0,
							'likes_count' => $formatted_feed->likes?$formatted_feed->likes:0,
							'thumbnail_image' => $formatted_feed->media_url,
							'standard_image' => $formatted_feed->media_url,
							'created_time' => strtotime($formatted_feed->timestamp)
					);
			$this->db->insert('user_instagram_feed',$feedArray);

					}
				}


			} else {
			// you will only be able to get normal media from IG:
		$url = 'https://graph.instagram.com/'.$IG_userid.'/media?access_token='.$this->instagram->getAccessToken().'';
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $url);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($crl);
		
		if(!response){
			die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
		}
		
		curl_close($crl);
		// data returned as json formatted object so need conversion
		$formatted = json_decode($response);

		foreach($formatted->data as $single) {
			// getting info about media from IG
			$feedArray = [];
			$url = 'https://graph.instagram.com/'.$single->id.'?fields=caption,id,media_type,media_url,permalink,thumbnail_url,username,timestamp&access_token='.$this->instagram->getAccessToken().'';
			$crl = curl_init();
			
			curl_setopt($crl, CURLOPT_URL, $url);
			curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
			curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
			$response = curl_exec($crl);

			$formatted_feed = json_decode($response);
			// echo '<pre>';
			// print_r($formatted_feed);
			$feedArray = array(
							'id' => $formatted_feed->id,
							'user_id' => $this->session->userdata("userid"),
							'text' => ($formatted_feed->caption) ? $formatted_feed->caption : '',
							'link' => $formatted_feed->permalink,
							'likes' => $formatted_feed->likes?$formatted_feed->likes:0,
							'comments_count' => 0,
							'likes_count' => $formatted_feed->likes?$formatted_feed->likes:0,
							'thumbnail_image' => $formatted_feed->media_url,
							'standard_image' => $formatted_feed->media_url,
							'created_time' => strtotime($formatted_feed->timestamp)
					);
			$this->db->insert('user_instagram_feed',$feedArray);

			}
		}
		
		
		$this->session->set_flashdata('success', 'Instagram Account is Authorized Successfully.');
  		redirect('staff/newsocialpage');
		

		
	 

	   
   }


   public function newsocialpage() {
	   // this login url will be passed when the facebbok is not authenticated.
	   

	// getting facebook content from here

	$sql="Select * from user_media where social_type='facebook' and userid=".$this->session->userdata("userid");
	$data = $this->model->allSelects($sql);
	// echo '<pre>';
	// print_r($data);
	// die('heree');
	if(isset($data) and $data[0]['access_token'] != '' and $data[0]['isScheduled'] != 1) {
		$access_token_fb = $data[0]['access_token'];
		$fb_user_id = $data[0]['social_id'];
		$this->config->load('facebook'); 
		$fb = new \Facebook\Facebook([
			'app_id' => '410761562312101',
			'app_secret' => 'af3b1e569a37c23308940a3733c094a5',
			'default_graph_version' => 'v2.10'
		]);
		// $fb = new \Facebook\Facebook([
		// 	'app_id' => '467247541182892',
		// 	'app_secret' => 'e9061252d85599f3952ad14237def308',
		// 	'default_graph_version' => 'v2.10'
		// ]);

		// debug token
		
	
		

	  try {
		$response = $fb->get('/me?fields=id,name,picture',$access_token_fb);
	  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
		// When Graph returns an error
		echo 'Graph returned an error: ' . $e->getMessage();
		// echo $e->getCode();
		// die('herer');
		
		// if($e->getCode() == 190) {
		//$this->relinkfb($this->session->userdata("userid")); 
		$sql = "SELECT access_token FROM user_media where social_type='instagram' and userid=".$this->session->userdata("userid")."";
		$run_1 = $this->model->allSelects($sql);
		$sql_2 = "SELECT access_token FROM user_media where social_type='facebook' and userid=".$this->session->userdata("userid")."";
		$run_2 = $this->model->allSelects($sql_2);
		if($run_1[0]['access_token'] == $run_2[0]['access_token']) {
			// this means this is a business instagram account connected:
			$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$this->session->userdata("userid");
			$delete_acc = $this->model->allQueries($sql);
			$sqll="DELETE FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
			$delete_acc_page = $this->model->allQueries($sqll);
			$sql="DELETE FROM user_media where social_type='instagram' AND userid=".$this->session->userdata("userid");
			$delete_acc_IG = $this->model->allQueries($sql);
			$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Also the instagram business account connected is also detached alongwith. Now relink your account from the same window.');   

		} else {
			$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$this->session->userdata("userid");
			$delete_acc = $this->model->allQueries($sql);
			$sqll="DELETE FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
			$delete_acc_page = $this->model->allQueries($sqll);
			$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Now relink your account from the same window.');   
		}

		//redirect('staff/newsocialpage', 'refresh');
		//redirect($_SERVER['REQUEST_URI'], 'refresh'); 
	
   		echo '<script type="text/javascript">location.reload();</script>';

	
		
	  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
		// When validation fails or other local issues
		echo 'Facebook SDK returned an error: ' . $e->getMessage();
		exit;
	  }

	  $me = $response->getGraphUser();
	  $data['logged_in_user_name'] = $me->getName();
	  $data['logged_in_user_id'] = $me->getId();
	  $data['logged_in_user_profilepic'] = $me->getPicture()->getUrl();

	  $sql="Select * from user_page where page_user_id=".$this->session->userdata("userid");
	  $pages=$this->model->allSelects($sql);
	  $pages_array = [];
	  foreach($pages as $key => $page) {
		try {
			$response = $fb->get('/'.$page['page_social_id'].'/picture?redirect=0',$access_token_fb);	
		  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
			// When Graph returns an error
			echo 'Graph returned an error: ' . $e->getMessage();
			$sql = "SELECT access_token FROM user_media where social_type='instagram' and userid=".$this->session->userdata("userid")."";
			$run_1 = $this->model->allSelects($sql);
			$sql_2 = "SELECT access_token FROM user_media where social_type='facebook' and userid=".$this->session->userdata("userid")."";
			$run_2 = $this->model->allSelects($sql_2);
			if($run_1[0]['access_token'] == $run_2[0]['access_token']) {
				// this means this is a business instagram account connected:
				$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$this->session->userdata("userid");
				$delete_acc = $this->model->allQueries($sql);
				$sqll="DELETE FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
				$delete_acc_page = $this->model->allQueries($sqll);
				$sql="DELETE FROM user_media where social_type='instagram' AND userid=".$this->session->userdata("userid");
				$delete_acc_IG = $this->model->allQueries($sql);
				$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Also the instagram business account connected is also detached alongwith. Now relink your account from the same window.');   

			} else {
				$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$this->session->userdata("userid");
				$delete_acc = $this->model->allQueries($sql);
				$sqll="DELETE FROM user_page WHERE page_user_id=".$this->session->userdata("userid");
				$delete_acc_page = $this->model->allQueries($sqll);
				$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Now relink your account from the same window.');   
			}
			echo '<script type="text/javascript">location.reload();</script>';
			//exit;
		  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
			// When validation fails or other local issues
			echo 'Facebook SDK returned an error: ' . $e->getMessage();
			exit;
		  }

		  
		  $pics = $response->getGraphUser();
		  $pages_array[$key]['id'] = $page['page_social_id'];
		  $pages_array[$key]['pic'] = $pics['url'];
		  $pages_array[$key]['name'] = $page['page_name'];

	  }
	  $data['pages'] = $pages_array;

	  try {
		$getIGinfo = $fb->get('/'.$fb_user_id.'/accounts', $access_token_fb);
	  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
		// When Graph returns an error
		echo 'Graph returned an error: ' . $e->getMessage();
		exit;
	  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
		// When validation fails or other local issues
		echo 'Facebook SDK returned an error: ' . $e->getMessage();
		exit;
	  }

	  $graphedge = $getIGinfo->getGraphEdge();
	  $business = 0;
	  $connectedIG = '';
	  
	  foreach ($graphedge as $graphNode) {
		$singlenode = $graphNode->asArray();	
		try {
			$newcall = $fb->get('/'.$singlenode['id'].'?fields=connected_instagram_account', $access_token_fb);
		  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
			// When Graph returns an error
			echo 'Graph returned an error: ' . $e->getMessage();
			exit;
		  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
			// When validation fails or other local issues
			echo 'Facebook SDK returned an error: ' . $e->getMessage();
			exit;
		  }
		  $results = $newcall->getDecodedBody();
		//   echo '<pre>';
		//   print_r($results);
		  if(isset($results['connected_instagram_account']) and count($results['connected_instagram_account']) != 0) {
			$business++;
			$connectedIG = $results['connected_instagram_account']['id'];
		  } 
	  }

	//    print_r($connectedIG);
	//    die('herer');

	  if(isset($connectedIG) and $connectedIG != '') {

		try {
			$IG_business = $fb->get('/'.$connectedIG.'?fields=id,username', $access_token_fb);
		  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
			// When Graph returns an error
			echo 'Graph returned an error: ' . $e->getMessage();
			exit;
		  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
			// When validation fails or other local issues
			echo 'Facebook SDK returned an error: ' . $e->getMessage();
			exit;
		  }

		  
		  $IGdetails = $IG_business->getDecodedBody();
		  
	  }
	  $data['fb_IG_business_acc'] = $business;
	  $data['fb_IG_business_det'] = $IGdetails;

	  if($business != 0) {
		// the facebook has a business accut connected: we will put the connection string in db:

		// $sqll = "DELETE from user_media where userid=".$this->session->userdata("userid")." AND social_type='instagram';";
		// $delete_prev = $this->model->allQueries($sqll);
		$sqll = "SELECT * FROM user_media where userid=".$this->session->userdata("userid")." AND social_type='instagram'";
		$exist = $this->model->allSelects($sqll);
		// echo '<pre>';
		// print_r($exist);
		// print_r($sqll);
		// die('herer');
		if(isset($exist[0]['access_token']) and $exist[0]['access_token'] != '') {
			// do nothing in this case
		} else {

			$insertArray = array(
				'userid'=>$this->session->userdata("userid"),
				'username'=> $IGdetails['username'],
				'fullname'=> $IGdetails['full_name'],
				'social_id'=> $IGdetails['id'],
				'image' => 'https://graph.facebook.com/'.$user_info['id'].'/picture',
				'created_date' =>date('Y-m-d H:i:s'),
				'social_type' =>'instagram',
				'access_token' => $access_token_fb,
				'status'=>'Active'
			);
	$this->db->insert('user_media', $insertArray);

	try {
		$response = $fb->get('/'.$IGdetails['id'].'/media',$access_token_fb);
	  } catch(\Facebook\Exceptions\FacebookResponseException $e) {
		// When Graph returns an error
		echo 'Graph returned an error: ' . $e->getMessage();
		exit;
	  } catch(\Facebook\Exceptions\FacebookSDKException $e) {
		// When validation fails or other local issues
		echo 'Facebook SDK returned an error: ' . $e->getMessage();
		exit;
	  }

	  $IG_media_set = $response->getDecodedBody();
	  foreach($IG_media_set['data'] as $single) {
		$feedArray = [];
		// getting feeds like and comments counts:
		$urll = 'https://graph.facebook.com/v10.0/'.$single['id'].'/?fields=caption,media_product_type,permalink,thumbnail_url,id,media_type,media_url,owner,timestamp,comments_count,like_count&access_token='.$access_token_fb.'';
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $urll);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($crl);
		
		if(!$response){
			die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
		}
		
		curl_close($crl);
		// data returned as json formatted object so need conversion
		$format = json_decode($response);
		$feedArray = array(
			'id' => $format->id,
			'user_id' => $this->session->userdata("userid"),
			'text' => ($format->caption) ? $format->caption : '',
			'link' => $format->permalink,
			'likes' => $format->like_count?$format->like_count:0,		
			'created_time' => strtotime($format->timestamp),
			'comments_count' => $format->comments_count,
			'likes_count' => $format->like_count?$format->like_count:0,
			'thumbnail_image' => $format->media_url,
			'standard_image' => $format->media_url,
	);
	$this->db->insert('user_instagram_feed',$feedArray);


	  }

		}



		  

	} else {
		// not a business account of insta so do nothing here.
	}
	  
	}
	

	

	$sql="Select * from user_media where social_type='instagram' and userid=".$this->session->userdata("userid");
	$IG_connected_acc_auth = $this->model->allSelects($sql);
	$data['IG_connected_acc_auth'] = $IG_connected_acc_auth;
	// get account type of IG
	if(isset($IG_connected_acc_auth[0]) and $IG_connected_acc_auth[0]['social_type'] == 'instagram') {
		// echo 'comes';
		$url = 'https://graph.instagram.com/'.$IG_connected_acc_auth[0]['social_id'].'?fields=id,username,account_type&access_token='.$IG_connected_acc_auth[0]['access_token'].'';
		$crl = curl_init();
		
		curl_setopt($crl, CURLOPT_URL, $url);
		curl_setopt($crl, CURLOPT_FRESH_CONNECT, true);
		curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
		$response = curl_exec($crl);
		
		if(!response){
			die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
		}
		curl_close($crl);
		$response = json_decode($response);	
		// echo '<pre>';
		// print_r($response);
		// die('sfdsf');
		$data['IG_acc_type'] = $response->account_type;
	}



	// YOUTUBE PART BEGIN HERE.
	// here we will do google authentication for youtube.
	$sql="Select * from user_media where social_type='youtube' and userid=".$this->session->userdata("userid");
	$YT_acc = $this->model->allSelects($sql);
	$client = new Google_Client();
	if(isset($YT_acc[0]['access_token'])) {
		// this means authentication is already done.

	} else {
		// // we need to do Youtube AUTH.
		// $client->setApplicationName('Youtube Data Access');
		// $client->setScopes([
		// 	'https://www.googleapis.com/auth/youtube.readonly',
		// ]);
		// $client->setAuthConfig(FCPATH . 'application/libraries/google-sdk/client_secret_628144901679-nga5ivenl7msklrits54a5qdrpu0p0jd.apps.googleusercontent.com.json');
		// $client->setAccessType('offline');
		// $redirect_uri = 'http://' . $_SERVER['HTTP_HOST'] . '/index.php/staff/googleredirect';
		// $client->setRedirectUri($redirect_uri);
		$authUrl = $this->client->createAuthUrl();
		$data['youtube_auth_url'] = $authUrl;

	}

	
	

	


	// YOUTUBE PART ENDS HERE.

	$data['insta_basic_url'] = 'https://api.instagram.com/oauth/authorize
	?client_id=775682203383178&redirect_uri='.base_url().'index.php/staff/instabasicauth
	&scope=user_profile,user_media
	&response_type=code';
	$data['fb_loginUrl'] = $this->facebook->getLoginUrl(array(
		'scope'=>'email,public_profile,user_posts,manage_pages,pages_show_list,instagram_basic,pages_read_engagement,pages_manage_posts,pages_manage_metadata,pages_manage_engagement,pages_manage_ads,pages_read_user_content,email,instagram_manage_insights',
		'redirect_uri' => base_url().'index.php/staff/facebook_redirect/'
	));
	$data['youtube_auth_url'] = $authUrl;
	$data['YT_token'] = $YTtoken;
	$data['Ig_relink_url'] = base_url(). 'index.php/staff/relinkinsta/'.$this->session->userdata("userid");
	$data['FB_relink_url'] = base_url(). 'index.php/staff/relinkfb/'.$this->session->userdata("userid");
	$data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
	$data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);			
    $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
	$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
	$data['content'] = $this->load->view('staff/new-social-page', $data, true);
	// echo '<pre>';
	// print_r($data);
	// die('hererer');
    $this->load->view('layout-inner', $data);

   }

   public function relinkinsta($userid) {
	  	// deleting social account
	  	$sql="DELETE FROM user_media where social_type='instagram' AND userid=".$userid;
		$delete_acc = $this->model->allQueries($sql);
		// $sqll="DELETE FROM user_instagram_feed where userid=".$userid;
		// $delete_feeds = $this->model->allQueries($sqll);
		$this->session->set_flashdata('success', 'Your connected Instagram account is detached succsessfully. Now relink your account from the same window');             
		
		redirect('staff/newsocialpage');

   }

   public function relinkfb($userid) {
	//    echo 'fired';
	//    die();
   // deleting social account
   // check if the IG account is business or not:
	$sql = "SELECT access_token FROM user_media where social_type='instagram' and userid=".$userid."";
	$run_1 = $this->model->allSelects($sql);
	$sql_2 = "SELECT access_token FROM user_media where social_type='facebook' and userid=".$userid."";
	$run_2 = $this->model->allSelects($sql_2);
	if($run_1[0]['access_token'] == $run_2[0]['access_token']) {
		// this means this is a business instagram account connected:
		$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$userid;
		$delete_acc = $this->model->allQueries($sql);
		$sqll="DELETE FROM user_page WHERE page_user_id=$userid";
		$delete_acc_page = $this->model->allQueries($sqll);
		$sql="DELETE FROM user_media where social_type='instagram' AND userid=".$userid;
		$delete_acc_IG = $this->model->allQueries($sql);
		$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Also the instagram business account connected is also detached alongwith. Now relink your account from the same window.');   

	} else {
		$sql="DELETE FROM user_media where social_type='facebook' AND userid=".$userid;
		$delete_acc = $this->model->allQueries($sql);
		$sqll="DELETE FROM user_page WHERE page_user_id=$userid";
		$delete_acc_page = $this->model->allQueries($sqll);
		$this->session->set_flashdata('success', 'Your connected Facebook account is detached succsessfully. Now relink your account from the same window.');   
	}
	
	          
	
	// header('Location: https://anglertrack.net/index.php/staff/newsocialpage');
	// exit();
	redirect('staff/newsocialpage');
		 


}

   public function testt() {
	redirect('staff/newsocialpage', 'refresh');
	$data['head'] = $this->load->view('templates/head-unicorn-theme', '', true);
	$data['header'] = $this->load->view('templates/header-unicorn-theme', '', true);			
    $data['navigation'] = $this->load->view('staff/staff-navigation', '', true);
	$data["iconmenus"] = $this->load->view('staff/staff-iconmenus', '', true);
	$data['content'] = $this->load->view('staff/testt', $data, true);

    $this->load->view('layout-inner', $data);
   } 

   public function googleredirect() {
		// here we will do google authentication for youtube.
		$client = new Google_Client();
		$client->setApplicationName('Youtube Data Access');
		$client->setScopes([
			'https://www.googleapis.com/auth/youtube.readonly',
		]);
		$client->setAuthConfig(FCPATH . 'application/libraries/google-sdk/client_secret_628144901679-nga5ivenl7msklrits54a5qdrpu0p0jd.apps.googleusercontent.com.json');
		$client->setAccessType('offline');
		$redirect_uri = 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
		$client->setRedirectUri($redirect_uri);

		if (isset($_GET['code'])) {
			$token = $client->fetchAccessTokenWithAuthCode($_GET['code']);
			$client->setAccessToken($token['access_token']);

			$sqll = "DELETE from user_media where userid=".$this->session->userdata("userid")." AND social_type='youtube';";
			$delete_prev = $this->model->allQueries($sqll);
			$insertArray = array(
						'userid'=>$this->session->userdata("userid"),
						'username'=> 'one',
						'fullname'=> 'test',
						'social_id'=> 'sd534543',
						'image' => '--',
						'created_date' => date('Y-m-d H:i:s'),
						'social_type' =>'youtube',
						'access_token' => $token['access_token'],
						'status'=>'Active'
					);
			$this->db->insert('user_media', $insertArray);

			
		}
		redirect('staff/newsocialpage');

   }
   
   public function testemail() {

	$this->load->library('email');

	$this->email->initialize(array(
		'protocol' => 'smtp',
		'smtp_host' => 'smtp.sendgrid.net',
		'smtp_user' => 'apikey',
		'smtp_pass' => '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',
		'smtp_port' => 587,
		'crlf' => "\r\n",
		'newline' => "\r\n"
		));

		$this->email->from('no-reply@anglertrack.net', 'Anglertrack');
		$this->email->to('gundeep.impinge@gmail.com');
		$this->email->subject('Email Test');
		$this->email->message('Testing the email class.');
		$this->email->send();

		echo $this->email->print_debugger();						
		die('hereerr');


	

   }

}

/* End of file STAFF */