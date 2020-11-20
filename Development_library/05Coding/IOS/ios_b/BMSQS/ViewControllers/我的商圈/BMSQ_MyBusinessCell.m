//
//  BMSQ_MyBusinessCell.m
//  BMSQS
//
//  Created by gh on 16/3/28.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_MyBusinessCell.h"

@interface BMSQ_MyBusinessCell ()

@property (nonatomic, strong)UIImageView *leftImage;
@property (nonatomic, strong)UILabel *rightLabel;


@end


@implementation BMSQ_MyBusinessCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    
    return self;
    
}

- (void)setViewUp {
    
    self.leftImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 5, 30, 30 )];
    [self.contentView addSubview:self.leftImage];
    
    self.rightLabel = [[UILabel alloc] initWithFrame:CGRectMake(50, 0, APP_VIEW_WIDTH, 40)];
    self.rightLabel.font = [UIFont systemFontOfSize:13.f];
    [self.contentView addSubview: self.rightLabel];
    
    UIImageView *rightImageView = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-25, 0, 10, 40)];
    rightImageView.contentMode = UIViewContentModeScaleAspectFit;
    rightImageView.image = [UIImage imageNamed:@"garyright"];
    [self.contentView addSubview:rightImageView];
    
    [gloabFunction ggsetLineView:CGRectMake(0, 39.5, APP_VIEW_WIDTH, 0.5) view:self.contentView];
    
    
    
}

- (void)setCellValue:(NSString *)imageStr label:(NSString *)labelStr {
    [self.leftImage setImage:[UIImage imageNamed:imageStr]];
    self.rightLabel.text = labelStr;
    
}



@end
