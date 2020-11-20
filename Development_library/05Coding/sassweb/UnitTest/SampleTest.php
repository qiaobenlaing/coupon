<?php

/**
 * This is an sample PHP Unit Test calss for LeaderModel.
 * 
 * In order to run this test. These packages are needed:
 * PHPUnit: 
 * 		user> sudo pear config-set auto_discover 1
 *		user> sudo pear install pear.phpunit.de/PHPUnit
 * DbUnit:
 * 		user> sudo pear install phpunit/DbUnit
 * php5-sqlite:
 * 		user> sudo apt-get install php5-sqlite
 * 
 * To integrate phpunit with Eclipse, refer to http://katsande.com/running-phpunit-tests-from-eclipse-helio#comment-3104 
 * 
 * To run the tests (a single file):
 * 		user> phpunit SampleTest.php
 * OR (all the files under a directory):
 *		user> phpUnit Model/	
 *
 */

/* error_reporting(E_ERROR | E_WARNING);

define('APP_NAME', '');
define('APP_PATH', '../Application/');
define('APP_DEBUG', true);
// !!! IMPORTANT: define PHP_UNIT so that ThinkPHP won't dispatch the URL to Index.indexAction()!!!
// A code snippet was add in ThinkPHP/Lib/Core/Think.class.php to utilize the constant.
define('PHP_UNIT', true);

require_once "../ThinkPHP/ThinkPHP.php";
 */
require_once 'comm.php';
require_once '../App/Lib/Model/LeaderModel.class.php';

class SampleTest extends PHPUnit_Extensions_Database_TestCase {
	// only instantiate pdo once for test clean-up/fixture load
	static private $pdo = null;
	
	// only instantiate PHPUnit_Extensions_Database_DB_IDatabaseConnection once per test
	private $conn = null;
	
	/**
	 * Note: the database configuration is in phpunit.xml. Run the test as:
	 * 		user@desktop> phpunit --configuration phpunit.xml MyTests/
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
		return $this->createFlatXMLDataSet(dirname(__FILE__) . '/sample-data.xml'); // flat xml
		// return $this->createXMLDataSet('myXmlFixture.xml'); // normal xml
	}
	
	public function testGetLeaders() {
		
		$obj_leaderMdl = new LeaderModel;	
		
		$userId = 2;
		$type = LeaderModel::LEADER_TYP_SYS;
		$arr_leader = $obj_leaderMdl->getLeaders($userId, $type);
				
		$this->assertCount(10, $arr_leader);
	}
	
}