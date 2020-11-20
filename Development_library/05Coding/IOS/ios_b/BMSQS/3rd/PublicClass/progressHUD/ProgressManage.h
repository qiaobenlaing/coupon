//
//  ProgressManage.h
//  NiuNiu
//
//  Created by zltianhen on 11-9-2.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

//单例模式
#import <Foundation/Foundation.h>
#import "ZGProgressHUD.h"

@interface ProgressManage : NSObject
{

}

//@property (nonatomic,readonly) ZGProgressHUD* progressHUD;

//显示等待框
+ (void)openProgressText:(NSString*)title;
//显示等待框
+(void)closeProgress;
@end



///////////////////////////////////DEMO///////////////////////////////////////////////

//ProgressManage* _waitDlg;
//
//- (void)showHUBView:(NSString *)text
//{
//	if (_waitDlg)
//	{
//		[_waitDlg openProgressText:text];
//		_waitDlg.progressHUD.hudDelegate = self;
//	}
//	
//}
//
//- (void)hideHUBView
//{
//	if (_waitDlg)
//	{
//		_waitDlg.progressHUD.hudDelegate = nil;
//		[_waitDlg closeProgress];
//	}
//}