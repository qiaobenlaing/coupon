//
//  CommentViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CommentViewCell.h"

@implementation CommentViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setCellView];
    }
    return self;
}

- (void)setCellView
{
    self.userName = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 20, 20)];
    self.userName.font = [UIFont systemFontOfSize:14.0];
    self.userName.text = @"蓝天天才";
    self.userName.backgroundColor = [UIColor blackColor];
    
    
    
    
}





@end
