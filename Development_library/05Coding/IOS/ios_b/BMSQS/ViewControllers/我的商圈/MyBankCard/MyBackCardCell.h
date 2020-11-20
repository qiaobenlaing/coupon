//
//  MyBackCardCell.h
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyBackCardCell : UITableViewCell
{
    UILabel* lb_name;
    UILabel* lb_cardNum;
}
@property (nonatomic, strong)NSDictionary *dicBank;
- (void)setCellValue:(NSDictionary*)dicBank;



@end
