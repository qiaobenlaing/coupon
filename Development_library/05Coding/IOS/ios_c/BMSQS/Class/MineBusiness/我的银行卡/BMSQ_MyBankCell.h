//
//  BMSQ_MyBankCell.h
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_MyBankCell : UITableViewCell
{
    UILabel* lb_name;
    UILabel* lb_cardNum;
}
@property (nonatomic, strong)NSDictionary *dicBank;
- (void)setCellValue:(NSDictionary*)dicBank;
@end
