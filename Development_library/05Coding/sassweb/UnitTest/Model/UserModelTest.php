<?php
/**
 * Unit Test for NewsModel in Application/Lib/Model/NewsModel.class.php.
 * 
 * Run instructor can be found in UnitTests/SampleTest.php.
 */

require_once 'comm.php';
require_once '../Application/Lib/Model/UserModel.class.php';


class Model_NewsModelTest extends PHPUnit_Extensions_Database_TestCase {
	// only instantiate pdo once for test clean-up/fixture load
	static private $pdo = null;
	
	// only instantiate PHPUnit_Extensions_Database_DB_IDatabaseConnection once per test
	private $conn = null;

	
	
	/**
	 * getUserHistory($user , $type = 'read' , $page = 1 , $pagesize = 20)
	 */
	public function testGetUserHistory() {
		$User = D('User');
	
		$arr_news = $User->getUserHistory(5, 'read');
		$this->assertCount(1, $arr_news);
		$arr_news = $User->getUserHistory(8, 'read');
		$this->assertCount(2, $arr_news);
	
		$arr_news = $User->getUserHistory(5, 'liked');
		$this->assertCount(1, $arr_news);
		$arr_news = $User->getUserHistory(10, 'liked');
		$this->assertCount(1, $arr_news);
	
		$arr_news = $User->getUserHistory(5, 'disliked');
		$this->assertEmpty($arr_news);
	
		$arr_news = $User->getUserHistory(5, 'fav');
		$this->assertEmpty($arr_news);
	
		$arr_news = $User->getUserHistory(5, 'submitted');
		$this->assertEmpty($arr_news);
		$arr_news = $User->getUserHistory(1, 'submitted');
		$this->assertCount(1, $arr_news);
	}
	
	/**
	 * isUserPush$userId)
	 */
	public function testIsPushUser() {
		$User = D('User');
		
		$this->assertTrue($User->isPushUser(1));
		$this->assertTrue($User->isPushUser(3));
		$this->assertTrue($User->isPushUser(5));
		$this->assertFalse($User->isPushUser(2));
		$this->assertFalse($User->isPushUser(8));
		$this->assertFalse($User->isPushUser(12));
	} 
	
	/**
	 * Note: the database configuration is in phpunit.xml. Run the test as:
	 * 		user@desktop> phpunit Model/
	 *   or:
	 * 		user@desktop> phpunit --configuration phpunit.xml Model/
	 *
	 * @return PHPUnit_Extensions_Database_DB_IDatabaseConnection
	 *
	 */
	public function getConnection() {
		if ($this->conn === null) {
			if (self::$pdo == null) {
				self::$pdo = new PDO( $GLOBALS['DB_DSN'], $GLOBALS['DB_USER'], $GLOBALS['DB_PASSWD'] );
			}
			$this->conn = $this->createDefaultDBConnection(self::$pdo, $GLOBALS['DB_DBNAME']);
		}
	
		return $this->conn;
	}
	
	/**
	 * @return PHPUnit_Extensions_Database_DataSet_IDataSet
	 */
	public function getDataSet() {
		return $this->createXMLDataSet('data/nz_test_data.xml');
	}
	
	
}