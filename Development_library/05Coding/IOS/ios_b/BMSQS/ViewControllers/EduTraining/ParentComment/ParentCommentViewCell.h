//
//  ParentCommentViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RemarkImgView.h"
#import "UIButtonEx.h"

@protocol ParentCommentViewCellDelegate <NSObject>

- (void)setParentWithRemarkCode:(NSString *)remarkCode shopRemark:(NSString *)shopRemark;

@end


@interface ParentCommentViewCell : UITableViewCell



- (void)setCellParentCommentDic:(NSDictionary *)commentDic;
@property(nonatomic,assign)id<ParentCommentViewCellDelegate>parentDelegate;
@property(nonatomic, strong)NSString * remarkCode;
@end
