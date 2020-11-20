//
//  BMSQ_homeCircleSquareTableViewCell.h
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_homeCircleSquareTableViewCellDelegate <NSObject>

-(void)clickCircle:(int)tag;

@end

@interface BMSQ_homeCircleSquareTableViewCell : UITableViewCell

-(void)homeCircleSquare:(id)reponse height:(float)height;
@property (nonatomic, strong)UIScrollView *sc;


@property (nonatomic, weak)id<BMSQ_homeCircleSquareTableViewCellDelegate>circleDelegate;

@end
