//
//  BMSQ_ClerkCell.h
//  BMSQS
//
//  Created by gh on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"
@protocol ClerkCellDelegate <NSObject>


- (void)cellBtnClick:(id)object;
- (void)cellDelStaff:(id)object;

@end


@interface BMSQ_ClerkCell : UITableViewCell

@property (nonatomic,strong)id<ClerkCellDelegate>delegate;
- (void)setCellValue:(id)object;

@property(nonatomic,strong)UIButtonEx *btnEdit;

@end
