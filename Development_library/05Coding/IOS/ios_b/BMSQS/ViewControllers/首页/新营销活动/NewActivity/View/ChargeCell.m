//
//  ChargeCell.m
//  BMSQS
//
//  Created by gh on 16/1/25.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ChargeCell.h"

#define ChargeCellViewHeight 80


@implementation ChargeCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, ChargeCellViewHeight)];
        view.backgroundColor = UICOLOR(255, 255, 255, 1);
        [self.contentView addSubview:view];

        
        self.idLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, ChargeCellViewHeight/2)];
        self.idLabel.tag = 1001;
        self.idLabel.text = @"规格";
        [self.contentView addSubview:self.idLabel];
        
        self.desText  = [[UITextField alloc] initWithFrame:CGRectMake(10, ChargeCellViewHeight/2, APP_VIEW_WIDTH/2-20, 30)];
        self.desText.tag = 2001;
        self.desText.backgroundColor = UICOLOR(235, 235, 235, 1);
        self.desText.placeholder = @"输入规格：如成人票";
        self.desText.font = [UIFont systemFontOfSize:14.f];
        [self.contentView addSubview:self.desText];
        
        self.priceText = [[UITextField alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3 * 2, ChargeCellViewHeight/2, APP_VIEW_WIDTH/3-10, 30)];
        self.priceText.tag = 2002;
        self.priceText.placeholder = @"输入金额";
        self.priceText.keyboardType = UIKeyboardTypeDecimalPad;
        self.priceText.backgroundColor = UICOLOR(235, 235, 235, 1);
        self.priceText.font = [UIFont systemFontOfSize:14.f];
        [self.contentView addSubview:self.priceText];
        
    }
    return self;
    
}






@end
