//
//  BMSQ_RedPackageCell.h
//  BMSQS
//
//  Created by djx on 15/7/21.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol BMSQ_RedPackageCellDelegate <NSObject>

-(void)gotofindRedMemeber:(NSDictionary *)dic;
@end

@interface BMSQ_RedPackageCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_title;
    UILabel* lb_usedCount; //使用人数
    UIProgressView *pregressView;
    
    UILabel *proBackLable1;
   
}

@property (nonatomic, strong)NSDictionary *redDic;
@property (nonatomic ,strong) UIButton *button;

@property (nonatomic, strong)id<BMSQ_RedPackageCellDelegate>redMebDelegate;

- (void)setCellValue:(NSDictionary*)dicData;
@end
