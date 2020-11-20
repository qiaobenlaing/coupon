//
//  ClassSyllabusCell.m
//  BMSQS
//
//  Created by gh on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ClassSyllabusCell.h"
#import "OpenClassUtil.h"


@implementation ClassSyllabusCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self) {
        
        [self setViewUp];
        
    }
    return self;
    
}

- (void)setViewUp {
    
    self.timeLabel = [OpenClassUtil openClassSetLabel:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 44) text:@"" font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.contentView];
    self.timeLabel.textAlignment = NSTextAlignmentCenter;
    
    [gloabFunction ggsetLineView:CGRectMake(0, 43, APP_VIEW_WIDTH/2, 1) view:self.contentView];
    
    
}

- (void)setCellValue:(NSDictionary *)timeDic {
    
    self.timeLabel.text = [NSString stringWithFormat:@"%@-%@", [timeDic objectForKey:@"startTime"], [timeDic objectForKey:@"endTime"]];
    
}


@end
