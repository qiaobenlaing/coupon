//
//  BMSQ_ModifySignController.h
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_ModifySignController : UIViewControllerEx<UIAlertViewDelegate,UITextViewDelegate>
@property (strong, nonatomic) IBOutlet UITextView *textView;
@property(strong,nonatomic)IBOutlet UILabel*  uilabel;
@property(nonatomic,strong)NSString* modifyInfo;
@property(nonatomic,strong)NSString* mobileNbr;

@end
