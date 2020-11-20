<?php
namespace Client\Controller;
use Think\Controller;
class IndexController extends Controller {
    public function index(){
    	$this->redirect('/Client/Home/download');
    }
}