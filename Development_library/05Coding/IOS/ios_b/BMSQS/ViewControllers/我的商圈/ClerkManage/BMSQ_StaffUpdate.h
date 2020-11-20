//
//  BMSQ_StaffUpdate.h
//  BMSQS
//
//  Created by gh on 15/11/3.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_StaffUpdate : UIViewControllerEx <UITextFieldDelegate>

@property(nonatomic)BOOL isUpdate;//YES编辑状态  NO添加员工

@property(nonatomic,strong)NSDictionary *staffDic;
@property(nonatomic,strong)NSString *stShopCode;

@end
