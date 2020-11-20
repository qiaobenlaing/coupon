//
//  BMSQ_TrainViewController.h
//  BMSQC
//
//  Created by liuqin on 16/3/28.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_TrainViewController : UIViewControllerEx

@end







@protocol TrainCellDelegate <NSObject>

-(void)payTrain:(NSDictionary *)trainDic;

@end


@interface TrainCell : UITableViewCell

@property (nonatomic, strong)NSDictionary *trainDic;
@property (nonatomic, weak)id<TrainCellDelegate>trainDelegate;


-(void)setTrainCell:(NSDictionary *)trainDic;

@end