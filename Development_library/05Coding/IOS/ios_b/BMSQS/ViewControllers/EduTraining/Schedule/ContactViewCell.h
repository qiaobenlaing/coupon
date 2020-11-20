//
//  ContactViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ContactViewCellDelegate <NSObject>

- (void)setCellViewCellDelegate:(NSString *)phoneStr;


@end

@interface ContactViewCell : UITableViewCell

- (void)setCellWithConentDic:(NSDictionary *)dic;
@property (nonatomic, assign)id<ContactViewCellDelegate>contactDelegate;

@end
