//
//  BMSQ_RedPackageCell.m
//  BMSQS
//
//  Created by djx on 15/7/21.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RedPackageCell.h"
#import "UIImageView+AFNetworking.h"

@implementation BMSQ_RedPackageCell

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
    if (self)
    {
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(15, 10, 55, 55)];
    iv_logo.backgroundColor = [UIColor clearColor];
    [self addSubview:iv_logo];
    
    lb_title = [[UILabel alloc]initWithFrame:CGRectMake(75, 15, APP_VIEW_WIDTH - 80, 25)];
    lb_title.backgroundColor = [UIColor clearColor];
    lb_title.textColor = UICOLOR(80, 80, 80, 1.0);
    [lb_title setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_title];
    
    lb_usedCount = [[UILabel alloc]initWithFrame:CGRectMake(75, 40, 100, 25)];
    lb_usedCount.backgroundColor = [UIColor clearColor];
    lb_usedCount.textColor = UICOLOR(80, 80, 80, 1.0);
    lb_usedCount.textAlignment = NSTextAlignmentRight;
    [lb_usedCount setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_usedCount];
    
    
    _button = [UIButton buttonWithType:UIButtonTypeCustom];
    [_button setTitle:@"领取人员" forState:UIControlStateNormal];
    [_button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    [_button.titleLabel setFont:[UIFont systemFontOfSize:12.0]];
    _button.frame = CGRectZero;
    _button.layer.cornerRadius = 3.0;
    _button.layer.borderColor = UICOLOR(182, 0, 12, 1.0).CGColor;
    _button.layer.borderWidth = 1.0;
    [_button addTarget:self action:@selector(clickButton) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:_button];
    

    
    UILabel *proBackLable = [[UILabel alloc]initWithFrame:CGRectMake(75, 50,APP_VIEW_WIDTH/2-30, 10)];
    proBackLable.layer.masksToBounds = YES;
    proBackLable.layer.cornerRadius = 8;
    proBackLable.layer.borderWidth = 0.5;
    proBackLable.layer.borderColor = [[UIColor grayColor]CGColor];
    [self addSubview:proBackLable];
    
    proBackLable1 = [[UILabel alloc]initWithFrame:CGRectMake(5,2, 0,6)];
    proBackLable1.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    proBackLable1.layer.masksToBounds = YES;
    proBackLable1.layer.cornerRadius = 6;
    proBackLable1.tag = 101;
    [proBackLable addSubview:proBackLable1];
    
    UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 79, APP_VIEW_WIDTH, 1)];
    line.backgroundColor = UICOLOR(207, 207, 207, 1.0);
    [self addSubview:line];
    
}


- (void)layoutSubviews{
    [super layoutSubviews];
    
    lb_usedCount.frame = CGRectMake(self.frame.size.width -100 - 20, 40, 100, 25);
    _button.frame = CGRectMake(self.frame.size.width - 75, 10, 60, 25);
    pregressView.frame = CGRectMake(CGRectGetMinX(lb_title.frame), 50, 120, 15);
}



- (void)setCellValue:(NSDictionary*)dicData
{
    if (dicData == nil || dicData.count <= 0)
    {
        return;
    }
    self.redDic = dicData;
    NSString* strUrl = [dicData objectForKey:@"logoUrl"];
    [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/%@",APP_SERVERCE_IMG_URL,strUrl]]];
    lb_title.text = [NSString stringWithFormat:@"红包批次: %@",[gloabFunction changeNullToBlank:[dicData objectForKey:@"batchNbr"]]];
    lb_usedCount.text = [NSString stringWithFormat:@"%@%%(%@/%@)", dicData[@"getPercent"],dicData[@"getNbr"],dicData[@"totalVolume"]];
    //    pregressView.progress = [dicData[@"getPercent"] floatValue] / 100.0;
    
    float w = [[dicData objectForKey:@"getNbr"]floatValue]/[[dicData objectForKey:@"totalVolume"]floatValue]*(APP_VIEW_WIDTH/2-40);
    
    proBackLable1.frame = CGRectMake(5, 2, w, 6) ;
    
}
-(void)clickButton{
    [self.redMebDelegate gotofindRedMemeber:self.redDic];
}

@end
