//
//  NullViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "NullViewCell.h"

@implementation NullViewCell

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
        [self addSubview:self.nullImage];
        [self addSubview:self.conentLB];
        [self addSubview:self.alertBut];
        [self addSubview:self.bellBut];
    }
    return self;
}

- (UIImageView *)nullImage
{
    if (_nullImage == nil) {
        self.nullImage = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 40, APP_VIEW_WIDTH/3, APP_VIEW_WIDTH/3)];
//        _nullImage.backgroundColor = [UIColor blackColor];
    }
    return _nullImage;
}

- (UILabel *)conentLB
{
    if (_conentLB == nil) {
        self.conentLB = [[UILabel alloc] initWithFrame:CGRectMake(0, _nullImage.frame.origin.y + _nullImage.frame.size.height + 40, APP_VIEW_WIDTH, 20)];
//        _conentLB.backgroundColor = [UIColor purpleColor];
        _conentLB.font = [UIFont systemFontOfSize:14.0];
        _conentLB.textAlignment = NSTextAlignmentCenter;
    }
    return _conentLB;
}

- (UIButton *)bellBut
{
    if (_bellBut == nil) {
        self.bellBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _bellBut.frame = CGRectMake(40, _nullImage.frame.origin.y + _nullImage.frame.size.height + 80, 20, 20);
    }
    
    return _bellBut;
}

- (UIButton *)alertBut
{
    if (_alertBut == nil) {
        self.alertBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _alertBut.frame = CGRectMake(60, _nullImage.frame.origin.y + _nullImage.frame.size.height + 80, APP_VIEW_WIDTH - 120, 20);
         _alertBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
        [_alertBut setTitle:@"提醒掌柜更新商品/服务" forState:UIControlStateNormal];
        [_alertBut setTitleColor:UICOLOR(183, 34, 26, 1.0) forState:UIControlStateNormal];
    }
    return _alertBut;
}

@end
