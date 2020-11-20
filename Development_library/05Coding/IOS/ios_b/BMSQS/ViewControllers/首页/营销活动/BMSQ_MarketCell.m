//
//  BMSQ_MarketCell.m
//  BMSQS
//
//  Created by djx on 15/7/19.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MarketCell.h"
#import "UIImageView+AFNetworking.h"

@implementation BMSQ_MarketCell

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
    self.backgroundColor = [UIColor clearColor];
    UIView *bgaview = [[UIView alloc]initWithFrame:CGRectMake(8, 10, APP_VIEW_WIDTH-16, 80)];
    bgaview.backgroundColor = [UIColor whiteColor];
    bgaview.layer.cornerRadius = 4;
    bgaview.layer.masksToBounds = YES;
    [self.contentView addSubview:bgaview];
    
    
//    
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(8, 8, 80-16, 80-16)];
    iv_logo.backgroundColor = [UIColor clearColor];
    iv_logo.layer.cornerRadius = 4;
    iv_logo.layer.masksToBounds = YES;
    [bgaview addSubview:iv_logo];
//
    lb_title = [[UILabel alloc]initWithFrame:CGRectMake(iv_logo.frame.origin.x+iv_logo.frame.size.width+10, iv_logo.frame.origin.y, APP_VIEW_WIDTH-200, 30)];
    lb_title.backgroundColor = [UIColor clearColor];
    lb_title.textColor = UICOLOR(80, 80, 80, 1.0);
    [lb_title setFont:[UIFont systemFontOfSize:18]];
    [bgaview addSubview:lb_title];
//
    lb_activityCount = [[UILabel alloc]initWithFrame:CGRectMake(lb_title.frame.origin.x, 30, lb_title.frame.size.width, 30)];
    lb_activityCount.backgroundColor = [UIColor clearColor];
    lb_activityCount.textColor = UICOLOR(80, 80, 80, 1.0);
    [lb_activityCount setFont:[UIFont systemFontOfSize:14]];
    [bgaview addSubview:lb_activityCount];
//
    lb_time = [[UILabel alloc]initWithFrame:CGRectMake(lb_title.frame.origin.x, 50, APP_VIEW_WIDTH, 30)];
    lb_time.backgroundColor = [UIColor clearColor];
    lb_time.textColor = UICOLOR(80, 80, 80, 1.0);
    [lb_time setFont:[UIFont systemFontOfSize:12]];
    [bgaview addSubview:lb_time];
////
////    UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 79, APP_VIEW_WIDTH, 1)];
////    line.backgroundColor = UICOLOR(207, 207, 207, 1.0);
////    [self addSubview:line];
////
    _button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 10, 60, 30)];
    [_button setTitle:@"报名人员" forState:UIControlStateNormal];
    [_button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    [_button.titleLabel setFont:[UIFont systemFontOfSize:12.0]];
    _button.layer.cornerRadius = 3.0;
    _button.layer.borderColor = UICOLOR(182, 0, 12, 1.0).CGColor;
    _button.layer.borderWidth = 1.0;
    [_button addTarget:self action:@selector(clickButton) forControlEvents:UIControlEventTouchUpInside];
    
    [bgaview addSubview:_button];
//
}
//
//
//- (void)layoutSubviews{
//    [super layoutSubviews];
//    
//    iv_logo.frame = CGRectMake(15, 10, 55, 55);
//    lb_title.frame = CGRectMake(75, 10, APP_VIEW_WIDTH - 80, 16);
//    lb_activityCount.frame = CGRectMake(75, CGRectGetMaxY(lb_title.frame)+10, 100, 16);
//    lb_time.frame = CGRectMake(75, CGRectGetMaxY(lb_activityCount.frame)+10, APP_VIEW_WIDTH - 95, 14);
//    _button.frame = CGRectMake(self.frame.size.width - 75, 10, 60, 25);
//}


- (void)setCellValue:(NSDictionary*)dicData
{
    if (dicData == nil || dicData.count <= 0)
    {
        return;
    }
    self.actDic = dicData;
    NSString* strUrl = [dicData objectForKey:@"activityLogo"];
    [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/%@",APP_SERVERCE_IMG_URL,strUrl]] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
    lb_title.text = [NSString stringWithFormat:@"%@",[gloabFunction changeNullToBlank:[dicData objectForKey:@"activityName"]]];
    
    NSString *count =[gloabFunction changeNullToBlank:[dicData objectForKey:@"participators"]];
    if (count.length==0) {
        count = @"0";
    }
    lb_activityCount.text = [NSString stringWithFormat:@"活动人数: %@",count];
    lb_time.text = [NSString stringWithFormat:@"活动时间: %@",[gloabFunction changeNullToBlank:[dicData objectForKey:@"createTime"]]];
}
-(void)clickButton{
    [self.findDelegate findJoinPeople:self.actDic];
    
    
}
@end
