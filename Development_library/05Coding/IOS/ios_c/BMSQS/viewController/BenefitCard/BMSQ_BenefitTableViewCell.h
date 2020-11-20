//
//  BMSQ_BenefitTableViewCell.h
//  BMSQC
//
//  Created by liuqin on 15/9/12.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BMSQ_BenefitTableViewCellDelegate <NSObject>

-(void)gotoDetail:(NSDictionary *)dic;
-(void)gotoActivity:(NSDictionary *)dic;

@end


@interface BMSQ_BenefitTableViewCell : UITableViewCell



@property (nonatomic, strong)NSDictionary *detailDic;

@property (nonatomic, weak)id<BMSQ_BenefitTableViewCellDelegate>beneDelegate;
-(void)setBenefitCell:(NSDictionary *)dic;


+(CGFloat )cellHeigh:(NSDictionary *)dic;

@end
