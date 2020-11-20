//
//  BMSQ_shopImageCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/9.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>




@protocol BMSQ_shopImageCellDelegate <NSObject>

-(void)clicktelShop:(NSString *)telNum;

@end



@interface BMSQ_shopImageCell : UITableViewCell



@property (nonatomic, weak)id<BMSQ_shopImageCellDelegate>shopHeadDelegate;

-(void)setShopImageCell:(id)imageS shopInfo:(id)object :(float)h;


@end
