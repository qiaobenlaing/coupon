//
//  ZGProgressHUD.h
//  NiuNiu
//
//  Created by zltianhen on 11-9-2.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ZGProgressHUDDelegate;

@interface ZGProgressHUD : UIView
{
    UILabel *m_textLabel;
	UIActivityIndicatorView *m_actView;

}

@property (nonatomic,assign) id<ZGProgressHUDDelegate>	hudDelegate;

- (id)initWithFrame:(CGRect)frame hasHUD:(BOOL)ishud;
- (void)setText:(NSString *)text;

@end




@protocol ZGProgressHUDDelegate <NSObject>

@required
- (void)cancalProgressHUD:(ZGProgressHUD*)bud;

@end


/////////////////////////////demo////////////////////////


//- (void)showHUBView:(NSString *)text
//{
//	if (_waitDlg == nil) 
//	{
//		_waitDlg = [[ZGProgressHUD alloc] initWithFrame:CGRectMake(0, 0, 160, 120) hasHUD:YES];
//		_waitDlg.hudDelegate = self;
//		_waitDlg.center = CGPointMake(160, 240);
//		[[UIApplication sharedApplication].keyWindow addSubview:_waitDlg];
//	}	
//	[_waitDlg setText:text];
//	
//	if (![[UIApplication sharedApplication] isIgnoringInteractionEvents])
//		[[UIApplication sharedApplication] beginIgnoringInteractionEvents];
//}
//
//- (void)hideHUBView
//{
//	if (_waitDlg) {
//		_waitDlg.hudDelegate = nil;
//		[_waitDlg removeFromSuperview];
//		[_waitDlg release];
//		_waitDlg = nil;
//	}
//	
//	if ([[UIApplication sharedApplication] isIgnoringInteractionEvents])
//		[[UIApplication sharedApplication] endIgnoringInteractionEvents];
//}
//
//- (void)cancalProgressHUD:(ZGProgressHUD *)bud
//{
//	[self hideHUBView];
//}
//


