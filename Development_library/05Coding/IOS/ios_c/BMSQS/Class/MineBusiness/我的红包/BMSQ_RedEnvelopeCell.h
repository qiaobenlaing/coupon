//
//  BMSQ_RedEnvelopeCell.h
//  BMSQC
//
//  Created by djx on 15/8/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_RedEnvelopeCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_name;
    UILabel* lb_price;
    UILabel* lb_status;
}

- (void)setCellValue:(NSDictionary*)dicData;
@end
