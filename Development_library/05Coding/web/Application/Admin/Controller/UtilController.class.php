<?php
namespace Admin\Controller;
use Admin\Controller\AdminBaseController;
use Com\Umeditor\Uploader;
use Org\FirePHPCore\FP;
use Common\Model\DropdownModel;

/**
 * @author 刘卫平
 */

class UtilController extends AdminBaseController {
	
	/**
	 * 上传图片。给UMEditor和AJAX上传图片使用
	 *
	 */
	public function imageUp() {
		C('SHOW_PAGE_TRACE', false);
		$base64 = isset($_POST['base64']) ? true: false;
		//上传配置
		$config = array(
				'savePath' => 'Public/Uploads/',   //存储文件夹
				'maxSize' => 5000,                   //允许的文件最大尺寸，单位KB
				'allowFiles' => array('.gif', '.png', '.jpg', '.jpeg', '.bmp')  //允许的文件格式
		);
		//上传文件目录
		$Path = 'Public/Uploads/';
		//背景保存在临时目录中
		$config[ 'savePath' ] = $Path;
		$up = new Uploader('upfile', $config, $base64);
		$type = $_REQUEST['type'];
		$callback = $_GET['callback'];
		$info = $up->getFileInfo();
		//返回数据
		if($callback) {
			echo '<script>'.$callback.'('.json_encode($info).')</script>';
		} else {
			echo json_encode($info);
		}
	}
	
	/**
	 * 下拉菜单数据
	 * @param array $_GET 至少需要给定$_GET['id']：下拉框的主键. <br/>
	 * 			特殊下拉框需要给定$_GET['type']: 一般为Normal，调用DropdownModel.getNormal($id)方法，否则调用getXX($id)方法。
	 * @return 如果存在，返回array(name=>val)，否则不存在返回array()
	 */
	public function dropdown() {
		// 下拉框的主键
		$id = I('id', null);
		// 类型，用来决定调用哪个方法，一般为Normal，调用DropdownModel.getNormal($id)方法，否则调用getXx($id)方法。
		$type = I('type', 'Normal');
		
		if (!$id) {
			$this->ajaxError('ID不能为空');
		}
		
		$dropdownMdl = new DropdownModel();
		$method = 'get' . ucfirst($type);
		if (method_exists($dropdownMdl, $method)) { // 例：getDistrict 
			$ret = call_user_func_array(array($dropdownMdl, $method), array($id));
// 			FP::dbg($ret, '$ret');
			if (!$ret) 
				$ret = array();
			$this->ajaxSucc('', $ret);
		} else {
			$this->ajaxSucc('', array());
		}
		
	}
}
