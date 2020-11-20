//
//  BMSQ_ModifyInfoController.h
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_ModifyInfoController : UIViewControllerEx<UITextFieldDelegate>

@property(nonatomic)int insertPage; // 0修改昵称,1修改姓名
@property(nonatomic,strong)NSString* modifyInfo;
@property(nonatomic,strong)NSString* mobileNbr;
@end
