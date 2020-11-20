//
//  AllChooseViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AllChooseViewCell : UITableViewCell

@property (nonatomic, strong)id object;
@property (nonatomic)BOOL isSelect;
@property (nonatomic, strong) UIImageView * hokImage;
@property (nonatomic, strong) UILabel * allChooseLB;
@property (nonatomic, strong) UILabel * promptLB;

- (int)didClickCell:(int)money;

@end
