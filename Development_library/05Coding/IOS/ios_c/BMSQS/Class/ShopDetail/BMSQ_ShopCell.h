//
//  BMSQ_ShopCell.h
//  BMSQC
//
//  Created by djx on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_ShopCellDelegate <NSObject>

-(void)gotoDetail:(NSDictionary *)dic;

@end

@interface BMSQ_ShopCell : UITableViewCell

@property (nonatomic, strong)NSDictionary *DetailDic;

@property (nonatomic, strong)id<BMSQ_ShopCellDelegate>shopCellDelegate;


- (void)setCellValue:(NSDictionary*)dicData;
+(float)sethight:(NSDictionary *)dic;
@end
