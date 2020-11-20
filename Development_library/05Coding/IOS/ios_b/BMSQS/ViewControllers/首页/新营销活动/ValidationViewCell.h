//
//  ValidationViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ValidationViewCell : UITableViewCell

@property (nonatomic, strong) UILabel * verificationCodeLB;
@property (nonatomic, strong) UILabel * phoneNumberLB;
@property (nonatomic, strong) UILabel * stateLB;

- (void)setCellValidationWithDic:(NSDictionary *)newDic;


@end
