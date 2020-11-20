//
//  AllChooseViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "AllChooseViewCell.h"

@implementation AllChooseViewCell

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
        self.isSelect= NO;
        [self addSubview:self.hokImage];
        [self addSubview:self.allChooseLB];
        [self addSubview:self.promptLB];
    }
    return self;
}

- (UIImageView *)hokImage
{
    if (_hokImage == nil) {
        self.hokImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, self.frame.size.height / 4, self.frame.size.height / 2, self.frame.size.height / 2)];
        _hokImage.image = [UIImage imageNamed:@"radio_no.png"];
    }
    return _hokImage;
}

- (UILabel *)allChooseLB
{
    if (_allChooseLB == nil) {
        self.allChooseLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.height / 2 + 20, self.frame.size.height / 4, 30, self.frame.size.height / 2)];
        _allChooseLB.text = @"全选";
        _allChooseLB.font = [UIFont systemFontOfSize:14];
    }
    return _allChooseLB;
}

- (UILabel *)promptLB
{
    if (_promptLB == nil) {
        self.promptLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.height / 2 + 60, self.frame.size.height / 4, 200, self.frame.size.height / 2)];
        _promptLB.text = @"(请勾选已消费的菜品)";
        _promptLB.font = [UIFont systemFontOfSize:16];
        _promptLB.textColor = [UIColor redColor];
    }
    return _promptLB;
}

- (int)didClickCell:(int)money{
    
    self.isSelect = !self.isSelect;
    
    if (self.isSelect) {
        self.hokImage.image = [UIImage imageNamed:@"radio_yes.png"];
        return money;
        
    }else {
        self.hokImage.image = [UIImage imageNamed:@"radio_no.png"];
        return 0;
        
    }
    
    return 0;
    
}


@end
