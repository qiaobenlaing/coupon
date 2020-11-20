<?php
/**
 * Unit Test for self-defined functions of NewzanModel in Application/Lib/Mode/NewzanModel.class.php.
 * 
 * Run instructor can be found in UnitTests/SampleTest.php.
 */

require_once 'comm.php';
require_once '../Application/Lib/Model/NewsModel.class.php';


class Model_NewsModelTest extends PHPUnit_Extensions_Database_TestCase {
	// only instantiate pdo once for test clean-up/fixture load
	static private $pdo = null;
	
	// only instantiate PHPUnit_Extensions_Database_DB_IDatabaseConnection once per test
	private $conn = null;

	/**
	 * @see $this->testExecuteParams()
	 * 
	 * public function queryParams()
	 */
	public function testQueryParams() {
		$this->assertTrue(true);
	}
	
	/**
	 * public function escapeStr($str) 
	 */
	public function testEscapeStr() {
		$obj_newzanMdl = D('News'); 
		// new NewzanModel();
		$s = 'normal string';
		$this->assertEquals($s, $obj_newzanMdl->escapeStr($s));
		
		$s = 'abnormal\'s string';
		$this->assertEquals('abnormal\\\'s string', $obj_newzanMdl->escapeStr($s));
	}
	
	/**
	 * public function executeParams()
	 */
	public function testExecuteParams() {
		$url = 'http://php.net/manual/en/language.exceptions.php?a=~!@#$%^&*()_+{}:"|<>?`-=[];\'\\,./END';
		$title = 'PHP: - Exceptions ~!@#$%^&*()_+{}:"|<>?`-=[];\'\\,./END';
		$sql = 'INSERT INTO news (submitter, sub_time, url, title) VALUES (%u, %u, "%s", "%s")';
		
		//$obj_mdl = new NewzanModel();
		$News = D('News');
		
		$res = $News->executeParams($sql, 2, time(), $url, $title);
		$this->assertEquals(1, $res);
		
		$news = $News->find(3);
		$this->assertGreaterThan(0, $news['sub_time']);
		$this->assertEquals(1, $news['read_num']);
		$this->assertEquals(1, $news['vote_num']);
		$this->assertEquals(0, $news['comment_num']);
		$this->assertEquals($url, $news['url']);
		$this->assertNotEquals(0, $news['url_hash']);
		$this->assertEquals($title, $news['title']);
		
		$sql = 'SELECT * FROM news WHERE submitter=%d AND title="%s"';
		$news = $News->queryParams($sql, 2, $title);
		$this->assertCount(1, $news);
		$this->assertEquals($title, $news[0]['title']);
		
		// try illegal paramters: no enough parameters
		$excptionCaught = false;
		$sql = 'INSERT INTO news (submitter, sub_time, url, title) VALUES (%u, %u, "%s", "%s")';
		try {
			$res = $News->executeParams($sql, 2, time(), $url);
		} catch (ThinkException $expected) {
			$excptionCaught = true;
		}
		if(!$excptionCaught)
			$this->fail('ThinkPHPException about the wrong parameters has not been raised.');
		
		// try illegal paramters: no parameter
		$excptionCaught = false;
		try {
			$res = $News->executeParams();
		} catch (ThinkException $expected) {
			$excptionCaught = true;
		}
		if(!$excptionCaught)
			$this->fail('ThinkPHPException about the wrong parameters has not been raised.');
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