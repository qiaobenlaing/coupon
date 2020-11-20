//
//  StateViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "StateViewCell.h"

@implementation StateViewCell

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
        [self addSubview:self.stateLabel];
    }
    return self;
}

- (UILabel *)stateLabel{
    if (_stateLabel == nil) {
        self.stateLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width / 6, 0, self.frame.size.width / 6, self.frame.size.height)];
        _stateLabel.font = [UIFont systemFontOfSize:14];
        _stateLabel.textColor = UICOLOR(88, 88, 88, 1.0);
    }
    return _stateLabel;
}

@end
