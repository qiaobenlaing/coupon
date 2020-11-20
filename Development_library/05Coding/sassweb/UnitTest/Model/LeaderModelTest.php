<?php
/**
 * Unit Test for LeaderModel in Application/Lib/Model/LeaderModel.class.php.
 * 
 * Run instructor can be found in UnitTests/SampleTest.php.
 */

require_once 'comm.php';
require_once '../Application/Lib/Model/LeaderModel.class.php';


class Model_LeaderModelTest extends PHPUnit_Extensions_Database_TestCase {
	// only instantiate pdo once for test clean-up/fixture load
	static private $pdo = null;
	
	// only instantiate PHPUnit_Extensions_Database_DB_IDatabaseConnection once per test
	private $conn = null;

	/**
	 * public function getLeaderIDs($userId, $type)
	 */
	public function testGetLeaderIDs() {
		
		$obj_leaderMdl = new LeaderModel;	
		
		$userId = 11;
		$this->assertCount(3, $obj_leaderMdl->getLeaderIDs($userId, LeaderModel::LEADER_TYP_ALL), "Getting all leaders failed");
		$this->assertCount(2, $obj_leaderMdl->getLeaderIDs($userId, LeaderModel::LEADER_TYP_SELF), "Getting self leaders failed");

		$this->assertCount(3, $obj_leaderMdl->getLeaderIDs(9, LeaderModel::LEADER_TYP_ALL));
		$this->assertCount(1, $obj_leaderMdl->getLeaderIDs(9, LeaderModel::LEADER_TYP_SYS));
		
		$this->assertEmpty($obj_leaderMdl->getLeaderIDs(7, LeaderModel::LEADER_TYP_SYS));
	}
	
	/**
	 * getLeaders($uid, $type, $fields = array(), $num = 0)
	 */
	public function testGetLeaders() {
	
		$obj_leaderMdl = new LeaderModel;
	
		$userId = 11;
	
		// prepare test data
		$obj_userInfoMdl = M('user_info');
		$arr_u9Info = array('uid'=>9, 'username'=>'user9', 'avatar'=>'/imgs/user9.jpg');
		$obj_userInfoMdl->create();
		$obj_userInfoMdl->add($arr_u9Info);
	
		// 1st test
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_ALL);
		$this->assertCount(3, $arr_leader, "Getting all leaders failed");
		$this->assertEquals($arr_u9Info,
				array('uid'=>$arr_leader[1]['leaderid'], 'username'=>$arr_leader[1]['username'], 'avatar'=>$arr_leader[1]['avatar'])); // leader 9
		$this->assertEquals(array(1, 0, 0), array($arr_leader[0]['news_num'], $arr_leader[0]['news_score'], $arr_leader[0]['is_system'])); // leader 10
	
	
		$this->assertCount(2, $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_SELF), "Getting self leaders failed");
	
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_SYS);
		$this->assertCount(1, $arr_leader, "Getting system leaders failed");
		$this->assertEquals($arr_u9Info,
				array('uid'=>$arr_leader[0]['leaderid'], 'username'=>$arr_leader[0]['username'], 'avatar'=>$arr_leader[0]['avatar'])); // leader 9
	
		$this->assertCount(3, $obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_ALL));
		$this->assertCount(1, $obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_SYS));
	
		$this->assertEmpty($obj_leaderMdl->getLeaders(7, LeaderModel::LEADER_TYP_SYS));
	
	
		// test fields
		$fields = array('create_time', 'leaderid', 'news_num', 'is_system', 'username');
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_ALL, $fields);
		$this->assertCount(3, $arr_leader);
		$this->assertEquals('user9', $arr_leader[1]['username']);
	
		// test fields
		$fields = array('leaderid', 'username');
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_ALL, $fields);
		$this->assertCount(3, $arr_leader);
	
		$fields = array('leaderid');
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_ALL, $fields);
		$this->assertCount(3, $arr_leader);
	
		// test limit
		$fields = array('leaderid');
		$limit = 1;
		$arr_leader = $obj_leaderMdl->getLeaders($userId, LeaderModel::LEADER_TYP_ALL, $fields, $limit);
		$this->assertCount(1, $arr_leader);
	}
	
	/**
	 * public function addLeader($userId, $isPushUser, $leaderId, $type)
	 */
	public function testAddLeader() {
		$obj_leaderMdl = new LeaderModel;
		
		// add user 5 to user 7 as a system leader
		$this->assertTrue($obj_leaderMdl->addLeader(7, true, 5, LeaderModel::LEADER_TYP_SYS));
		$this->assertCount(2, $obj_leaderMdl->getLeaders(7, LeaderModel::LEADER_TYP_ALL));
		$this->assertCount(1, $obj_leaderMdl->getLeaders(7, LeaderModel::LEADER_TYP_SYS));
		/* // assert table `leaders`
		$queryTable = $this->getConnection()->createQueryTable(
				'leaders', 'SELECT uid, leaderid, news_num, news_score, is_system FROM leaders WHERE uid=7'
		);
		$expectedTable = new DbUnit_ArrayDataSet(array(
            'leaders' => array(
                array('uid' => 7, 'leaderid' => 3, 'news_num' => 0, 'news_score' => 0, 'is_system'=> 0),
                array('uid' => 7, 'leaderid' => 5, 'news_num' => 0, 'news_score' => 0, 'is_system'=> 1),
            ),
        ));
		$this->assertTablesEqual($expectedTable, $queryTable);
		// assert table `user_log`
		$queryTable = $this->getConnection()->createQueryTable(
			'user_log', 'SELECT uid, action, note_int, note_str FROM user_log WHERE uid=7');
		$expectedTable = new DbUnit_ArrayDataSet(array(
			'user_log' => array(
					array('uid' => 7, 'action' => UserLogAct::LEADER_ADD, 'note_int' => 5, 'note_str' => ""),
			),
		));
		$this->assertTablesEqual($expectedTable, $queryTable);	
		// assert table `push_queue`
		$queryTable = $this->getConnection()->createQueryTable(
			'push_queue', 'SELECT uid, nid, target_uid, act, status FROM push_queue WHERE uid=7');
		$expectedTable = new DbUnit_ArrayDataSet(array(
			'push_queue' => array(
					array('uid' => 5, 'nid' => 0, 'target_uid' => 7, 'act' => 1, 'status'=>0),
			),
		));
		$this->assertTablesEqual($expectedTable, $queryTable); */
		// assert table `user_log`
		$this->assertEquals(1, $this->getConnection()->getRowCount('user_log', 'uid=7'));
		// assert table `push_queue`
		$this->assertEquals(1, $this->getConnection()->getRowCount('push_queue', 'target_uid=7'));
		
		
		// change a system leader to user defined leader
		$this->assertTrue($obj_leaderMdl->addLeader(9, true, 5, LeaderModel::LEADER_TYP_SELF));
		/* $queryTable = $this->getConnection()->createQueryTable(
			'leaders', 'SELECT uid, leaderid, news_num, news_score, is_system FROM leaders WHERE uid=9'
		);
		$expectedTable = new DbUnit_ArrayDataSet(array(
			'leaders' => array(
					array('uid' => 9, 'leaderid' => 4, 'news_num' => 1, 'news_score' => 2, 'is_system'=> 0),
					array('uid' => 9, 'leaderid' => 5, 'news_num' => 1, 'news_score' => 2, 'is_system'=> 0),
					array('uid' => 9, 'leaderid' => 6, 'news_num' => 1, 'news_score' => 0, 'is_system'=> 0),
			),
		));
		$this->assertTablesEqual($expectedTable, $queryTable); */
		$this->assertCount(3, $obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_ALL));
		$this->assertEmpty($obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_SYS));
		// assert table `user_log`
		$this->assertEquals(1, $this->getConnection()->getRowCount('user_log', 'uid=9'));
		// assert table `push_queue`
		$this->assertEquals(0, $this->getConnection()->getRowCount('push_queue', 'target_uid=9'));
		
		// insert an existing leadership, 4->1
		$this->assertTrue(!$obj_leaderMdl->addLeader(4, false, 1, 1));
		$this->assertCount(3, $obj_leaderMdl->getLeaders(4, LeaderModel::LEADER_TYP_ALL));
		$this->assertCount(2, $obj_leaderMdl->getLeaders(4, LeaderModel::LEADER_TYP_SYS));
		
	}
	
	/**
	 * removeLeader($userId, $isPushUser, $leaderId) 
	 */
	public function testRemoveLeader() {
		$obj_leaderMdl = new LeaderModel;
		
		// remove a system leader
		$this->assertTrue($obj_leaderMdl->removeLeader(9, true, 5));
		$this->assertCount(2, $obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_ALL));
		$this->assertEmpty($obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_SYS));
		
		$this->assertEquals(1, $this->getConnection()->getRowCount('user_log', 'uid=9'));
		
		$this->assertEquals(1, $this->getConnection()->getRowCount('push_queue', 'target_uid=9'));
		
		$this->assertEquals(1, $this->getConnection()->getRowCount('leaders_deleted', 'uid=9'));
		
		
		// remove a user defined leader
		$this->assertTrue($obj_leaderMdl->removeLeader(9, true, 4));
		$this->assertCount(1, $obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_ALL));
		$this->assertEmpty($obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_SYS));
		
		$this->assertEquals(2, $this->getConnection()->getRowCount('user_log', 'uid=9'));
		
		$this->assertEquals(2, $this->getConnection()->getRowCount('push_queue', 'target_uid=9'));
		
		$this->assertEquals(2, $this->getConnection()->getRowCount('leaders_deleted', 'uid=9'));
		
		// remove the last leader
		$this->assertTrue($obj_leaderMdl->removeLeader(9, true, 6));
		$this->assertEmpty($obj_leaderMdl->getLeaders(9, LeaderModel::LEADER_TYP_ALL));
		
		// remove a non-existent leader
		$this->assertTrue(!$obj_leaderMdl->removeLeader(9, true, 4));
		
		// remove a leader from a PULL user
		$this->assertTrue($obj_leaderMdl->removeLeader(2, false, 1));
		
		$this->assertEquals(1, $this->getConnection()->getRowCount('user_log', 'uid=2'));
		
		$this->assertEquals(0, $this->getConnection()->getRowCount('push_queue', 'target_uid=2'));
		
		$this->assertEquals(1, $this->getConnection()->getRowCount('leaders_deleted', 'uid=2'));
		
	}
	
	/**
	 * TODO this test is not finished yet!
	 * 
	 * public function initLeaders($userId, $arr_taste)
	 */
	public function testInitLeaders() {
		// Stop here and mark this test as incomplete.
		//$this->markTestIncomplete('This test has not been implemented yet.');
		
		$obj_leaderMdl = new LeaderModel;
		
		$this->assertTrue($obj_leaderMdl->initLeaders(3, array(1, 2, 3)));
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