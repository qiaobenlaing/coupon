//
//  ButScreenViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ButScreenViewCell.h"

@implementation ButScreenViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self addSubview:self.cancelBut];
        [self addSubview:self.determineBut];
        [self addSubview:self.screenLabel];
    }
    return self;
}

- (UIButton *)cancelBut{
    if (_cancelBut == nil) {
        self.cancelBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _cancelBut.frame = CGRectMake(10, 0, self.frame.size.width / 10, self.frame.size.height);
        [_cancelBut setTitle:@"取消" forState:UIControlStateNormal];
        [_cancelBut setTitleColor:UICOLOR(114, 114, 114, 1.0) forState:UIControlStateNormal];
        _cancelBut.titleLabel.font = [UIFont systemFontOfSize:14];
    }
    return _cancelBut;
}

- (UIButton *)determineBut{
    if (_determineBut == nil) {
        self.determineBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _determineBut.frame = CGRectMake(self.frame.size.width * 2 / 5 - 10, 0, self.frame.size.width / 10, self.frame.size.height);
        [_determineBut setTitle:@"确定" forState:UIControlStateNormal];
        [_determineBut setTitleColor:UICOLOR(114, 114, 114, 1.0) forState:UIControlStateNormal];
        _determineBut.titleLabel.font = [UIFont systemFontOfSize:14];
    }
    return _determineBut;
}

- (UILabel *)screenLabel{
    if (_screenLabel == nil) {
        self.screenLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width / 5, 0, self.frame.size.width / 10, self.frame.size.height)];
        _screenLabel.text = @"筛选";
        _screenLabel.textAlignment = NSTextAlignmentCenter;
        _screenLabel.font = [UIFont systemFontOfSize:14];
    }
    return _screenLabel;
}


@end
