//
//  OrderDetailTopCell.m
//  BMSQS
//
//  Created by gh on 15/12/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "OrderDetailTopCell.h"

@implementation OrderDetailTopCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setViewUp];
        
    }
    
    return self;
    
}

- (void)setViewUp {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH/3, 44)];
    label.tag = 2001;
    label.font = [UIFont systemFontOfSize:13];
    label.text = @"";
    [self addSubview:label];
    
    UILabel *centerLaebl = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
    centerLaebl.tag = 2002;
    centerLaebl.font = [UIFont systemFontOfSize:13];
    centerLaebl.textAlignment = NSTextAlignmentCenter;
    centerLaebl.backgroundColor = [UIColor clearColor];
    centerLaebl.text = @"";
    [self addSubview:centerLaebl];
    
    UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
    rightLabel.tag = 2003;
    rightLabel.font = [UIFont systemFontOfSize:13];
    rightLabel.textAlignment = NSTextAlignmentRight;
    rightLabel.backgroundColor = [UIColor clearColor];
    rightLabel.text = @"";
    [self addSubview:rightLabel];
}

- (void)setCellValue:(id)object {
    UILabel *label = [self viewWithTag:2001];
    UILabel *centerlabel = [self viewWithTag:2002];
    UILabel *rightLabel = [self viewWithTag:2003];
    
    
    label.text = [NSString stringWithFormat:@"%@",[object objectForKey:@"productName"]];
    centerlabel.text = [NSString stringWithFormat:@"x%@",[object objectForKey:@"productNbr"]];
    rightLabel.text = [NSString stringWithFormat:@"%@元",[object objectForKey:@"productPrice"]];
    
    
    
    
}




@end
