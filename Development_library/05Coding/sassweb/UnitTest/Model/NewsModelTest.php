<?php
/**
 * Unit Test for NewsModel in Application/Lib/Model/NewsModel.class.php.
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
	 * id_t get_first_from(id_t uid, id_t nid, Statement *st);
	 * getFirstFrom($userId, $newsId)
	 */
	public function testGetFirstFrom() {
		$obj_newsMdl = D('News');
	
		$this->assertEquals(4, $obj_newsMdl->getFirstFrom(8, 1));
		$this->assertEquals(4, $obj_newsMdl->getFirstFrom(9, 1));
		$this->assertEquals(8, $obj_newsMdl->getFirstFrom(11, 1));
		$this->assertEquals(6, $obj_newsMdl->getFirstFrom(10, 2));
		$this->assertEquals(0, $obj_newsMdl->getFirstFrom(12, 1));
	}
	
	/**
	 * findTrail($newsId, $userId, $firstFrom=0, $userType=0)
	 */
	public function testFind_trail() {
		$obj_newsMdl = D('News');
	
		$this->assertEquals('1,4,8', $obj_newsMdl->findTrail(1, 11, 8, 1)); // normal PUSH way
		$this->assertEquals('1,4,8', $obj_newsMdl->findTrail(1, 11, 8, 2)); // do not provide user type
		$this->assertEquals('1,4,8', $obj_newsMdl->findTrail(1, 11, 8, 0)); // find trail in PULL way
	
		// do not provide submitter
		$this->assertEquals('1,4,8', $obj_newsMdl->findTrail(1, 11, 0, 0));
		$this->assertEquals('1,4,8', $obj_newsMdl->findTrail(1, 11, 0, 2));
	
		$this->assertEquals('1,4', $obj_newsMdl->findTrail(1, 9, 4, 1));
		$this->assertEquals('1,4', $obj_newsMdl->findTrail(1, 9, 4, 2)); // do not provide user type
	
		$this->assertEquals("1,2", $obj_newsMdl->findTrail(1, 5, 2, 1));
	
		$this->assertEquals("1,4", $obj_newsMdl->findTrail(1, 8, 4, 0));
		$this->assertEquals("1,4", $obj_newsMdl->findTrail(1, 8, 4, 2));
		$this->assertEquals("1,4", $obj_newsMdl->findTrail(1, 8, 0, 2));
	
		//	cout << $obj_newsMdl->findTrail(2, 11, 10, st) << endl;
		$this->assertEquals("6,10", $obj_newsMdl->findTrail(2, 11, 10, 1));
		$this->assertEquals("6", $obj_newsMdl->findTrail(2, 10, 6, 0));
	
		// check submitter
		$this->assertEquals("", $obj_newsMdl->findTrail(1, 1, 1, 1));
		$this->assertEquals("", $obj_newsMdl->findTrail(1, 4, 4, 0));
	
		// check not found by wrong parameter
		$this->assertEquals("0", $obj_newsMdl->findTrail(3, 11, 8, 1)); // wrong news id / user id
		$this->assertEquals("1,4,8", $obj_newsMdl->findTrail(1, 11, 9, 1)); // wrong first_from
		$this->assertEquals("1,4,9", $obj_newsMdl->findTrail(1, 11, 9, 0)); // wrong first_from & PUSH
		$this->assertEquals("1,2,5,10", $obj_newsMdl->findTrail(1, 11, 10, 0)); // wrong first_from & PUSH
		$this->assertEquals("1,12", $obj_newsMdl->findTrail(1, 11, 12, 0)); // wrong first_from & PUSH
		$this->assertEquals("6,8", $obj_newsMdl->findTrail(2, 11, 8, 0)); // wrong first_from & PUSH
	
		// check news passer didn't get news from his leaders but from popular news
		$obj_actMdl = M('actions');
		$obj_actMdl->execute("INSERT INTO actions (uid, nid, read_time, vote_score, vote_time) VALUES (12, 1, 1336424787, 2, 1336424787)");
		$this->assertEquals("1,12", $obj_newsMdl->findTrail(1, 11, 12, 0)); // find trail with PULL method
	}
	
	/**
	 * public function readNews($newsId, $userId, $isPushUser = false)
	 */
	public function testReadNews() {
		$obj_newsMdl = new NewsModel();
		
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$this->assertEquals(0, $obj_newsMdl->readNews(1, 11, true));
		
		//require_once '../ThinkPHP/Common/common.php';
		$Action = M('Actions');
		$act = $Action->where(array('uid'=>11, 'nid'=>1))->find();
		// getField('id, read_time, vote_score, vote_time, is_author');
		//print_r($act);
		$this->assertGreaterThan(0, $act['read_time']);
		$this->assertEquals(0, $act['vote_score']);
		$this->assertEquals(0, $act['vote_time']);
		$this->assertEquals(0, $act['is_author']);
		
		$News = M('News');
		$newsReadNum = $News->where(array('nid'=>1))->getField('read_num');
		$this->assertEquals(9, $newsReadNum);
		
		$FeedsPush = M('feeds_push');
		$idRead = $FeedsPush->where(array('uid'=>11, 'nid'=>1))->getField('is_read');
		$this->assertEquals(1, $idRead);
		
		$Leader = M('leaders');
		$leaders = $Leader->field('news_score')->where('uid=11')->order('leaderid')->select();
		$this->assertEquals(1, $leaders[0]['news_score']); // leader 8
		$this->assertEquals(1, $leaders[1]['news_score']); // leader 9
		$this->assertEquals(0, $leaders[2]['news_score']); // leader 10
		
		
		// read a news that is already read
		$this->assertEquals(-2, $obj_newsMdl->readNews(1, 8, false));
	}
	
	/**
	 * public function voteNews($newsId, $userId, $voteAct, $isPushUser = false, $isAuthor = 0)
	 */
	public function testVoteNews() {
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$News = D('News');
		$res = $News->voteNews(1, 11, NewsModel::NEWS_ACT_LIKE, true, 0);
		$this->assertEquals(0, $res);
		
		$Action = M('Actions');
		$act = $Action->where(array('uid'=>11, 'nid'=>1))->find();
		$this->assertGreaterThan(0, $act['read_time']);
		$this->assertEquals(2, $act['vote_score']);
		$this->assertGreaterThan(0, $act['vote_time']);
		$this->assertEquals(0, $act['is_author']);
		
		$News = M('News');
		$newsItem = $News->where(array('nid'=>1))->getField('nid, read_num, vote_num');
		$this->assertEquals(9, $newsItem[1]['read_num']);
		$this->assertEquals(7, $newsItem[1]['vote_num']);
		
		$FeedsPush = M('feeds_push');
		$idRead = $FeedsPush->where(array('uid'=>11, 'nid'=>1))->getField('is_read');
		$this->assertEquals(1, $idRead);
		
		$PushQueue = M('push_queue');
		$queue = $PushQueue->where(array('uid'=>11))->select();
		$this->assertCount(1, $queue);
		$this->assertEquals(1, $queue[0]['nid']);
		$this->assertEquals(0, $queue[0]['target_uid']);
		$this->assertGreaterThan(0, $queue[0]['share_time']);
		$this->assertEquals(1, $queue[0]['act']);
		$this->assertEquals(0, $queue[0]['status']);
		
		sleep(1);
		
		// duplicated actions
		$News = D('News');
		$this->assertEquals(-3, $News->voteNews(1, 11, NewsModel::NEWS_ACT_LIKE, true, 0));
		
		// cancel the 'like'
		$News = D('News');
		$this->assertEquals(0, $News->voteNews(1, 11, NewsModel::NEWS_ACT_NONE, true, 0));
		//$Action = M('Actions');
		$act = $Action->where(array('uid'=>11, 'nid'=>1))->find();
		$this->assertGreaterThan(0, $act['read_time']);
		$this->assertEquals(0, $act['vote_score']);
		$this->assertGreaterThan(0, $act['vote_time']);
		//$News = M('News');
		$newsItem = $News->where(array('nid'=>1))->getField('nid, read_num, vote_num');
		$this->assertEquals(9, $newsItem[1]['read_num']);
		$this->assertEquals(6, $newsItem[1]['vote_num']);
		//$FeedsPush = M('feeds_push');
		$idRead = $FeedsPush->where(array('uid'=>11, 'nid'=>1))->getField('is_read');
		$this->assertEquals(1, $idRead);
		//$PushQueue = M('push_queue');
		$queue = $PushQueue->where(array('uid'=>11))->select();
		$this->assertCount(1, $queue);
		$this->assertEquals(1, $queue[0]['nid']);
		$this->assertEquals(0, $queue[0]['target_uid']);
		$this->assertGreaterThan(0, $queue[0]['share_time']);
		$this->assertEquals(0, $queue[0]['act']);
		$this->assertEquals(-1, $queue[0]['status']);
		
		// like it again
		
		sleep(1);
		
		// dislike it
		$News = D('News');
		$this->assertEquals(0, $News->voteNews(1, 11, NewsModel::NEWS_ACT_DISLIKE, true, 0));
		//$Action = M('Actions');
		$act = $Action->where(array('uid'=>11, 'nid'=>1))->find();
		$this->assertGreaterThan(0, $act['read_time']);
		$this->assertEquals(-2, $act['vote_score']);
		$this->assertGreaterThan(0, $act['vote_time']);
		//$News = M('News');
		$newsItem = $News->where(array('nid'=>1))->getField('nid, read_num, vote_num');
		$this->assertEquals(9, $newsItem[1]['read_num']);
		$this->assertEquals(5, $newsItem[1]['vote_num']);
		//$FeedsPush = M('feeds_push');
		$idRead = $FeedsPush->where(array('uid'=>11, 'nid'=>1))->getField('is_read');
		$this->assertEquals(1, $idRead);
		//$PushQueue = M('push_queue');
		$queue = $PushQueue->where(array('uid'=>11))->select();
		$this->assertCount(1, $queue);
		$this->assertEquals(1, $queue[0]['nid']);
		$this->assertEquals(0, $queue[0]['target_uid']);
		$this->assertGreaterThan(0, $queue[0]['share_time']);
		$this->assertEquals(-1, $queue[0]['act']);
		$this->assertEquals(0, $queue[0]['status']);
		
		/* // out of 30s
		sleep(29);
		$News = D('News');
		$this->assertEquals(-2, $News->voteNews(1, 11, NewsModel::NEWS_ACT_DISLIKE, true, 0));
		$this->assertEquals(-2, $News->voteNews(1, 11, NewsModel::NEWS_ACT_LIKE, true, 0));
		$this->assertEquals(-2, $News->voteNews(1, 11, NewsModel::NEWS_ACT_NONE, true, 0)); */
		
		// a pull user likes a news
		
	}
	
	/**
	 * public function addNews($userId, $data)
	 */
	public function testAddNews() {
		$News = D('News');
		
// 		$this->markTestIncomplete('This test has not been implemented yet.');
		$data['url'] = 'http://php.net/manual/en/language.exceptions.php?a=~!@#$%^&*()_+{}:"|<>?`-=[];\'\\,./END'; //
		$data['title'] = 'PHP: - Exceptions ~!@#$%^&*()_+{}:"|<>?`-=[];\'\\,./END';
		$newId = $News->addNews(11, $data);
		$this->assertGreaterThan(0, $newId);
		
		$news = $News->find($newId);
		$this->assertGreaterThan(0, $news['sub_time']);
		$this->assertEquals(1, $news['read_num']);
		$this->assertEquals(1, $news['vote_num']);
		$this->assertEquals(0, $news['comment_num']);
		$this->assertEquals($data['url'], $news['url']);
		$this->assertNotEquals(0, $news['url_hash']);
		$this->assertEquals($data['title'], $news['title']);
		
		$Action = M('Actions');
		$act = $Action->where(array('uid'=>11, 'nid'=>$newId))->find();
		if($act === false) {
			dump($this->getDbError());
			$this->assertTrue(false);
		}
		$this->assertGreaterThan(0, $act['read_time']);
		$this->assertEquals(2, $act['vote_score']);
		$this->assertGreaterThan(0, $act['vote_time']);
		$this->assertEquals(1, $act['is_author']);

		$FeedsPush = M('feeds_push');
		$idRead = $FeedsPush->where(array('uid'=>11, 'nid'=>1))->getField('is_read');
		$this->assertEmpty($idRead);
		
		$PushQueue = M('push_queue');
		$queue = $PushQueue->where(array('uid'=>11))->select();
		$this->assertCount(1, $queue);
		$this->assertEquals($newId, $queue[0]['nid']);
		$this->assertEquals(0, $queue[0]['target_uid']);
		$this->assertGreaterThan(0, $queue[0]['share_time']);
		$this->assertEquals(1, $queue[0]['act']);
		$this->assertEquals(0, $queue[0]['status']);
		
		// isnert an exsiting news
		$this->assertEquals(0, $News->addNews(11, $data));
	}
	
	/**
	 * public function listPopularNews($page, $pageSize, $timespan = 43200, $userId=0)
	 */
	public function testListPopularNews() {
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$News = D('News');
		
		// get news list anonymously
		$arr_news = $News->listPopularNews(1, 5, 2592000);
		$this->assertCount(2, $arr_news);
		
		$this->assertEquals(1, $arr_news[0]['nid']);
		$this->assertEquals(1, $arr_news[0]['submitter']);
		//$this->assertGreaterThan(0, $arr_news[0]['sub_time']);
		$this->assertEquals(8, $arr_news[0]['read_num']);
		$this->assertEquals(6, $arr_news[0]['vote_num']);
		$this->assertEquals('http://www.1.com', $arr_news[0]['url']);
		//$this->assertNotEquals(0, $arr_news[0]['url_hash']);
		$this->assertEquals('news 1', $arr_news[0]['title']);
		$this->assertEquals(0, $arr_news[0]['read_time']);
		$this->assertEquals(0, $arr_news[0]['vote_score']);
		$this->assertEquals(0, $arr_news[0]['vote_time']);
		
		$this->assertEquals(2, $arr_news[1]['nid']);
		$this->assertEquals('http://www.2.com', $arr_news[1]['url']);
		
		
		// get news list with a user id (=5)
		$arr_news = $News->listPopularNews(1, 5, 2592000, 5);
		$this->assertCount(2, $arr_news);
		
		$this->assertEquals(1, $arr_news[0]['nid']);
		$this->assertEquals(1, $arr_news[0]['submitter']);
		//$this->assertGreaterThan(0, $arr_news[0]['sub_time']);
		$this->assertEquals(8, $arr_news[0]['read_num']);
		$this->assertEquals(6, $arr_news[0]['vote_num']);
		$this->assertEquals('http://www.1.com', $arr_news[0]['url']);
		//$this->assertNotEquals(0, $arr_news[0]['url_hash']);
		$this->assertEquals('news 1', $arr_news[0]['title']);
		$this->assertGreaterThan(0, $arr_news[0]['read_time']);
		$this->assertEquals(2, $arr_news[0]['vote_score']);
		$this->assertGreaterThan(0, $arr_news[0]['vote_time']);
		
		$this->assertEquals(2, $arr_news[1]['nid']);
		$this->assertEquals('http://www.2.com', $arr_news[1]['url']);
		
		
		// get page 2 anonymously
		$arr_news = $News->listPopularNews(2, 1, 2592000);
		$this->assertCount(1, $arr_news);
		
		$this->assertEquals(2, $arr_news[0]['nid']);
		$this->assertEquals(6, $arr_news[0]['submitter']);
		$this->assertEquals('http://www.2.com', $arr_news[0]['url']);
		$this->assertEquals('news 2', $arr_news[0]['title']);
		$this->assertEquals(0, $arr_news[0]['read_time']);
		$this->assertEquals(0, $arr_news[0]['vote_score']);
		
		// get page 1  with a user id (=10)
		$arr_news = $News->listPopularNews(1, 1, 2592000, 10);
		$this->assertCount(1, $arr_news);
		
		$this->assertEquals(1, $arr_news[0]['nid']);
		$this->assertEquals(1, $arr_news[0]['submitter']);
		$this->assertEquals('http://www.1.com', $arr_news[0]['url']);
		$this->assertEquals('news 1', $arr_news[0]['title']);
		$this->assertGreaterThan(1, $arr_news[0]['read_time']);
		$this->assertEquals(0, $arr_news[0]['vote_score']);
		$this->assertEquals(0, $arr_news[0]['vote_time']);
		
		// get page 3
		$arr_news = $News->listPopularNews(3, 1, 2592000, 10);
		$this->assertEmpty($arr_news);
		
		// short time (5s)
		$arr_news = $News->listPopularNews(3, 1, 5, 10);
		$this->assertEmpty($arr_news);
	}
	
	/**
	 * public function getNewsIdsByPage($arr_newsId, $page, $pageSize)
	 */
	public function testGetNewsIdsByPage() {
		$arr_id = array(3,2,1,6,5,4,9,8,7);
		
		$News = D('News');
		
		$this->assertEquals('6,5,4', $News->getNewsIdsByPage($arr_id, 2, 3));
		$this->assertEquals('3,2,1', $News->getNewsIdsByPage($arr_id, 1, 3));
		$this->assertEquals('9,8,7', $News->getNewsIdsByPage($arr_id, 3, 3));
		
		$this->assertEquals('', $News->getNewsIdsByPage($arr_id, 4, 3));
		
		$this->assertEquals('3', $News->getNewsIdsByPage($arr_id, 1, 1));
		
		$this->assertEquals('7', $News->getNewsIdsByPage($arr_id, 9, 1));
		
		$this->assertEquals('7', $News->getNewsIdsByPage($arr_id, 5, 2));
		
		$this->assertEquals('', $News->getNewsIdsByPage($arr_id, 11, 1));
		
		$arr_idStr = array(0=>'1', 1=>'2');
		$this->assertEquals('1,2', $News->getNewsIdsByPage($arr_idStr, 1, 20));
	}
	
	
	/**
	 * Here only test the parameters. The results are tested in the sub-functions.
	 * 
	 * public function listPersonalNews($userId, $leaders, $isPush, $orderBy, $currPage = 1, $pageSize = 20, $lastFeedTime=0)
	 */
	public function testListPersonalNews() {
		//$this->markTestIncomplete('This test has not been implemented yet.');
		$exceptionCaught = false;
		try {
			$News = D('News');
			$arr_news = $News->listPersonalNews(11, array(8, 9, 10), true, 2, 20, NewsModel::NEWS_ORDER_BY_SHARE_TIME, 0);
		} catch (ThinkException $expected) {
			$exceptionCaught = true;
			return;
		}
		if(!$exceptionCaught)
			$this->fail('Parameter error not cought.');
		return;
// 		$News = D('News');
// 		$arr_news = $News->listPersonalNews(11, array(8, 9, 10), true, 2, 20, NewsModel::NEWS_ORDER_BY_SHARE_TIME, 0);
		
		//$arr_news = $News->listPersonalNews(-1, NULL, true, -1, -20, NewsModel::NEWS_ORDER_BY_SHARE_TIME, 0);
	}
	
	
	/**
	 * Actually testing by listPersonalNews().
	 * 
	 * private function listPushOrderByTime($userId, $leaderIDs, $page, $pageSize, $lastFeedTime = 0)
	 */
	public function testListPushOrderByTime() {
		$News = D('News');
		$arr_news = $News->listPersonalNews(11, array(8, 9, 10), true, 1, 20, NewsModel::NEWS_ORDER_BY_SHARE_TIME);
		
		$this->assertCount(2, $arr_news);
		
		$this->assertEquals(2, $arr_news[0]['nid']);
		
		$this->assertEquals(1, $arr_news[1]['nid']);
		
		// ... 
	}
	
	/**
	 * Actually testing by listPersonalNews().
	 *
	 * private function listPushOrderByWeight($user, $leaderIDs, $page, $pageSize)
	 */
	public function testListPushOrderByWeight() {
		$News = D('News');
		$arr_news = $News->listPersonalNews(11, array(8, 9, 10), true, 1, 20, NewsModel::NEWS_ORDER_BY_WEIGHT);
	
		$this->assertCount(2, $arr_news);
	
		$this->assertEquals(1, $arr_news[0]['nid']);
	
		$this->assertEquals(2, $arr_news[1]['nid']);
	
		// ...
	}
	
	/**
	 * Actually testing by listPersonalNews().
	 *
	 * private function listPullOrderByTime($user, $leaderIDs, $page, $pageSize, $lastFeedTime = 0)
	 */
	public function testListPullOrderByTime() {
		$News = D('News');
	
		// voteNews($newsId, $userId, $voteAct, $isPushUser = false, $isAuthor = 0)
		$News->voteNews(1, 11, NewsModel::NEWS_ACT_LIKE, true, 0);
		//sleep(1);
		$News->voteNews(2, 11, NewsModel::NEWS_ACT_LIKE, true, 0);
	
	
		$arr_news = $News->listPersonalNews(12, array(11), false, 1, 20, NewsModel::NEWS_ORDER_BY_SHARE_TIME);
	
		$this->assertCount(2, $arr_news);
	
		$this->assertEquals(2, $arr_news[0]['nid']);
	
		$this->assertEquals(1, $arr_news[1]['nid']);
	
		// ...
	}
	
	/**
	 * Actually testing by listPersonalNews().
	 *
	 * private function listPullOrderByWeight($user, $leaderIDs, $page, $pageSize)
	 */
	public function testListPullOrderByWeight() {
		$News = D('News');
	
		$News->voteNews(1, 11, NewsModel::NEWS_ACT_LIKE, true, 0);
		//sleep(1);
		$News->voteNews(2, 11, NewsModel::NEWS_ACT_LIKE, true, 0);
	
	
		$arr_news = $News->listPersonalNews(12, array(11), false, 1, 20, NewsModel::NEWS_ORDER_BY_WEIGHT);
	
		$this->assertCount(2, $arr_news);
	
		$this->assertEquals(1, $arr_news[0]['nid']);
	
		$this->assertEquals(2, $arr_news[1]['nid']);
	
		// ...
	}
	
	/**
	 * favNews($newsId, $userId)
	 */
	public function testFavNews() {
		$News = D('News');
		$this->assertTrue($News->favNews(1, 11));
		$this->assertFalse($News->favNews(1, 11)); // duplicated fav
		
	}
	
	/**
	 * removeFav($newsId, $userId)
	 * 
	 * @depends testFavNews
	 */
	public function testRemoveFav() {
		$News = D('News');
		
		$News->favNews(1, 11);
		
		$this->assertTrue($News->removeFav(1, 11));
		$this->assertFalse($News->removeFav(1, 11)); // duplicated remove
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