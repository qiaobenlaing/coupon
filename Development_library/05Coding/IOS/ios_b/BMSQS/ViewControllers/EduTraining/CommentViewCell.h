//
//  CommentViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentViewCell : UITableViewCell

@property (nonatomic, strong) UILabel * userName;//用户名称
@property (nonatomic, strong) UILabel * gradeLB1;//等级1
@property (nonatomic, strong) UILabel * gradeLB2;//等级2
@property (nonatomic, strong) UILabel * gradeLB3;//等级3
@property (nonatomic, strong) UILabel * gradeLB4;//等级4
@property (nonatomic, strong) UILabel * gradeLB5;//等级5
@property (nonatomic, strong) UILabel * commentTime;//评论时间
@property (nonatomic, strong) UILabel * commentContent;//评论内容

@end
