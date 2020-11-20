//
//  BMSQ_MarketCell.h
//  BMSQS
//
//  Created by djx on 15/7/19.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol BMSQ_MarketCellDelegate <NSObject>

-(void)findJoinPeople:(NSDictionary *)dic;

@end


@interface BMSQ_MarketCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_title;
    UILabel* lb_activityCount;
    UILabel* lb_time;
}

@property (nonatomic ,strong) UIButton *button;
@property (nonatomic, strong)id<BMSQ_MarketCellDelegate>findDelegate;

@property (nonatomic, strong)NSDictionary *actDic;
- (void)setCellValue:(NSDictionary*)dicData;


@end
