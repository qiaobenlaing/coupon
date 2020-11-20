//
//  BMSQ_InputPwdView.h
//  BMSQC
//
//  Created by liuqin on 15/12/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


typedef void(^submitBlock)(NSString *str,int i);
typedef void(^cancelBlock)(int i);

@interface BMSQ_InputPwdView : UIView

@property (nonatomic, strong)UILabel *label;
@property (nonatomic, strong)UITextField *pwdLable;
@property (nonatomic, strong)UIButton *subButon;
@property (nonatomic, strong)UIButton *cancelButon;

@property (nonatomic,copy)submitBlock clickSubmit;
@property (nonatomic,copy)cancelBlock clickCancel;

@end
