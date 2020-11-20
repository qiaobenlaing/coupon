//
//  BMSQ_AddBankSureController.h
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_AddBankSureController : UIViewControllerEx<UITextFieldDelegate>


@property (nonatomic,assign)int formvc;

@property(nonatomic,strong)NSString* phoneNumber;
@property(nonatomic,strong)NSString* orderNbr; //签订协议号
@end
