//
//  OrderDetailsViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "OrderDetailsViewCell.h"

@implementation OrderDetailsViewCell

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
        
        
        [self addSubview:self.mealNumberLabel];
        [self addSubview:self.dataLabel];
        [self addSubview:self.stateLabel];
        
        UIView *linView = [[UIView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-1, APP_VIEW_WIDTH, 1)];
        linView.backgroundColor = APP_TEXTCOLOR;
        linView.alpha = 0.5;
        [self addSubview:linView];
        
    }
    return self;
}

- (UILabel *)mealNumberLabel{
    if (_mealNumberLabel == nil) {
        self.mealNumberLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 80, self.frame.size.height / 2)];
        _mealNumberLabel.font = [UIFont systemFontOfSize:14];
    }
    return _mealNumberLabel;
}

- (UILabel *)dataLabel{
    if (_dataLabel == nil) {
        self.dataLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 155, 0, 145, self.frame.size.height / 2 - 5)];
        _dataLabel.font = [UIFont systemFontOfSize:14];
        _dataLabel.textColor = UICOLOR(153, 153, 153, 1.0);
    }
    return _dataLabel;
}

- (UILabel *)stateLabel{
    if (_stateLabel == nil) {
        self.stateLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 100, self.frame.size.height / 2 - 5, 90, self.frame.size.height / 2 + 5)];
        _stateLabel.font = [UIFont systemFontOfSize:14];
        _stateLabel.textAlignment = NSTextAlignmentRight;
        _stateLabel.textColor = UICOLOR(86, 86, 86, 1.0);
    }
    return _stateLabel;
}



@end
