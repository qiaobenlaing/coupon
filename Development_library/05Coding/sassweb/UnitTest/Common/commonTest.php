<?php
/**
 * Unit Test for common functions in Application/Common/common.php.
 * 
 * Run instructor can be found in UnitTests/SampleTest.php.
 */

require_once 'comm.php';
require_once '../Application/Lib/Model/NewsModel.class.php';


class Common_CommonTest extends PHPUnit_Extensions_Database_TestCase {
	// only instantiate pdo once for test clean-up/fixture load
	static private $pdo = null;
	
	// only instantiate PHPUnit_Extensions_Database_DB_IDatabaseConnection once per test
	private $conn = null;

	/**
	 * function concat_arr_col($arr_2d, $col, $quota='')
	 */
	public function testConcat_arr_col() {
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$A = array(
				array('a'=>1, 'b'=>2),
				array('a'=>3, 'b'=>4),
				array('a'=>5, 'b'=>6),
		);
		
		$this->assertEquals('1,3,5', concat_arr_col($A, 'a'));
		$this->assertEquals('"1","3","5"', concat_arr_col($A, 'a', '"'));
		$this->assertEquals('2,4,6', concat_arr_col($A, 'b'));
		$this->assertEquals('"2","4","6"', concat_arr_col($A, 'b', '"'));
		
		$A = array();
		$this->assertEmpty(concat_arr_col($A, 'a'));
		
		$A = NULL;
		$this->assertEmpty(concat_arr_col($A, 'a'));
	}
	
	/**
	 * function store_to_cache($id, $value, $timespan)
	 */
	public function testStore_to_cache() {
		// test array
		$A = array(
				array('a'=>1, 'b'=>2),
				array('a'=>3, 'b'=>4),
				array('a'=>5, 'b'=>6),
		);
		store_to_cache('UNIT_TEST_A', $A, 1);
		$this->assertEquals($A, get_from_cache('UNIT_TEST_A'));
		
		sleep(1);
		//$this->assertEmpty(get_from_cache('UNIT_TEST_A'));
		$this->assertTrue(NULL === get_from_cache('UNIT_TEST_A'));
		
		// test integer
		$a = -23423223;
		store_to_cache('UNIT_TEST_A', $a, 2);
		$this->assertEquals($a, get_from_cache('UNIT_TEST_A'));
		
		// test string with wild chars
		$s = '34asdf~`!@#$%^&*()-_=+{[}];"\'\\,<.>/?先选出未读过的上游推荐的新闻及其相关上游信息2389439SDD';
		store_to_cache('UNIT_TEST_A', $s, 2);
		$this->assertEquals($s, get_from_cache('UNIT_TEST_A'));
		
		// test object
		$o = new stdClass();
		$o->A = $A;
		$o->a = $a;
		$o->s = $s;
		store_to_cache('UNIT_TEST_A', $o, 2);
		$this->assertEquals($o, get_from_cache('UNIT_TEST_A'));
		
	}
	
	/**
	 * function get_from_cache($id)
	 * 
	 * @see self::testStore_to_cache()
	 */
	public function testGet_from_cache() {
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$this->assertTrue(true);
	}
	
	/**
	 * function get_user_icon($path)
	 */
	public function testGet_user_icon() {
		$defaultPath = "/Img/weiboicon.jpg";
		
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$path = '/img/newstar.png';
		$this->assertEquals($path, get_user_icon($path));
		
		$path = NULL;
		$this->assertEquals($defaultPath, get_user_icon($path));
		
		$path = "";
		$this->assertEquals($defaultPath, get_user_icon($path));
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