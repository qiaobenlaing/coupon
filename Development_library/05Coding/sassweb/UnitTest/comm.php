<?php
// define('PHP_SAPI', 'cli');
define('APP_NAME', 'Baomi');
define('APP_PATH', '../Application/');
define('APP_DEBUG', true);
define('DEBUG_MODE', true);
define('SQL_DEBUG_LOG', true);
// !!! IMPORTANT: define PHP_UNIT so that ThinkPHP won't dispatch the URL to Index.indexAction()!!!
// A code snippet was add in ThinkPHP/Lib/Core/Think.class.php to utilize the constant.
define('PHP_UNIT', true);

error_reporting(E_ERROR | E_WARNING);
require_once "../ThinkPHP/ThinkPHP.php";
require_once '../ThinkPHP/Common/common.php';
require_once '../App/Common/common.php';
//require_once "PHPUnit/Framework/TestCase.php";
require_once 'PHPUnit/Extensions/Database/TestCase.php';
require_once './DbUtil/ArrayDataSet.php';
