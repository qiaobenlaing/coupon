//
//  UserCenterCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol UserCenterCellDelegate <NSObject>

-(void)clickUserCenterCell:(int)tag;

@end


@interface UserCenterCell : UITableViewCell

@property (nonatomic, weak)id<UserCenterCellDelegate>userCenterDelegate;

@end


@interface UserCenterView : UIView



-(id)initWithFrame:(CGRect)frame image:(UIImage *)image titile:(NSString *)title;


@end