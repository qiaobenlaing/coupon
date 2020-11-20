//
//  ProgressManage.m
//  NiuNiu
//
//  Created by zltianhen on 11-9-2.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ProgressManage.h"




static UIView* _alphaView = nil;
static ZGProgressHUD* _progressHUD = nil;
@implementation ProgressManage
//@synthesize progressHUD = _progressHUD;

//显示等待框
+ (void)openProgressText:(NSString*)title
{
	if (_alphaView == nil)
	{
        //		_alphaView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, iCard_VIEW_WIDTH, 480)];
        //		_alphaView.alpha = 0.5;
        //		_alphaView.backgroundColor = [UIColor blackColor];
        //		_alphaView.center = CGPointMake(160, 240);
        //		[[UIApplication sharedApplication].keyWindow addSubview:_alphaView];
	}
	if (_progressHUD == nil)
	{
		_progressHUD = [[ZGProgressHUD alloc] initWithFrame:CGRectMake(0, 0, 100,100) hasHUD:YES];
		_progressHUD.hudDelegate = nil;
		
		_progressHUD.center = CGPointMake(160, 230);
		[[UIApplication sharedApplication].keyWindow addSubview:_progressHUD];
	}
	[_progressHUD setText:title];
}






//显示等待框
+ (void)closeProgress
{
	if (_alphaView)
	{
		[_alphaView removeFromSuperview];
	}
	
	if (_progressHUD)
	{
		[_progressHUD removeFromSuperview];
	}
    
}




@end
