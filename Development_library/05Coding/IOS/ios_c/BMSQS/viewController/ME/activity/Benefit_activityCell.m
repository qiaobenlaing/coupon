//
//  Benefit_activityCell.m
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "Benefit_activityCell.h"
#import "UIImageView+WebCache.h"

@interface Benefit_activityCell ()

@property (nonatomic, strong)UIImageView *actImg;
@property (nonatomic, strong)UILabel *actTitleLabel;
@property (nonatomic, strong)UILabel *actTimeLabel;
@property (nonatomic, strong)UILabel *actCountLabel;
@property (nonatomic, strong)UILabel *actPriceLabel;

@end


@implementation Benefit_activityCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.actImg = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 110)];
        self.actImg.backgroundColor = [UIColor clearColor];
//        self.actImg sd_setImageWithURL:<#(NSURL *)#> placeholderImage:<#(UIImage *)#>
        [self.actImg setImage:[UIImage imageNamed:@"iv_detailNodata"]];
        [self addSubview:self.actImg];
        
        self.actTitleLabel = [[UILabel alloc]initWithFrame:CGRectZero];
        self.actTitleLabel.textColor = [UIColor whiteColor];
        self.actTitleLabel.numberOfLines = 0;
        self.actTitleLabel.font = [UIFont systemFontOfSize:14];
        [self.actImg  addSubview:self.actTitleLabel];
        
        self.actTimeLabel = [[UILabel alloc]initWithFrame:CGRectZero];
        self.actTimeLabel.textColor = APP_TEXTCOLOR;
        self.actTimeLabel.font = [UIFont systemFontOfSize:12];
        self.actTimeLabel.layer.borderColor = [APP_CELL_LINE_COLOR CGColor];
        self.actTimeLabel.textAlignment = NSTextAlignmentCenter;
        self.actTimeLabel.layer.borderWidth = 0.5;
        self.actTimeLabel.layer.masksToBounds = YES;
        self.actTimeLabel.layer.cornerRadius = 3;
        [self  addSubview:self.actTimeLabel];
        
        self.actCountLabel = [[UILabel alloc]initWithFrame:CGRectZero];
        self.actCountLabel.textColor = APP_TEXTCOLOR;
        self.actCountLabel.font = [UIFont systemFontOfSize:12];
        self.actCountLabel.layer.borderColor = [APP_CELL_LINE_COLOR CGColor];
        self.actCountLabel.textAlignment = NSTextAlignmentCenter;
        self.actCountLabel.layer.borderWidth = 0.5;
        self.actCountLabel.layer.masksToBounds = YES;
        self.actCountLabel.layer.cornerRadius = 3;
        [self  addSubview:self.actCountLabel];
        
        self.actPriceLabel = [[UILabel alloc]initWithFrame:CGRectZero];
        self.actPriceLabel.textColor = APP_TEXTCOLOR;
        self.actPriceLabel.font = [UIFont systemFontOfSize:12];
        self.actPriceLabel.layer.borderColor = [APP_CELL_LINE_COLOR CGColor];
        self.actPriceLabel.textAlignment = NSTextAlignmentCenter;
        self.actPriceLabel.layer.borderWidth = 0.5;
        self.actPriceLabel.layer.masksToBounds = YES;
        self.actPriceLabel.layer.cornerRadius = 3;
        [self  addSubview:self.actPriceLabel];
        
    }
    return self;
}

-(void)initActivityCell:(NSDictionary *)dic{
    self.activityCell = dic;
    self.actTitleLabel.text = [dic objectForKey:@"txtContent"];

    [self.actImg sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"activityImg"]]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
    NSString *minPrice = [NSString stringWithFormat:@"%@",[dic objectForKey:@"totalPayment"]];
    self.actPriceLabel.text = minPrice.floatValue >0?[NSString stringWithFormat:@"￥%.2f",[minPrice floatValue]]:@"免费";
    
    if([self.actPriceLabel.text isEqualToString:@"免费"]){
//        self.actPriceLabel.hidden = NO;
        self.actCountLabel.hidden = YES;

    }else{
//        self.actPriceLabel.hidden = YES;
        self.actCountLabel.hidden = NO;
    }
    
    NSString *startTime =[dic objectForKey:@"startTime"];
    NSArray *array = [startTime componentsSeparatedByString:@" "];
    startTime = [array objectAtIndex:0];
    array = [startTime componentsSeparatedByString:@"-"];
    NSMutableString *Start = [NSMutableString stringWithFormat:@"%@.%@",[array objectAtIndex:1],[array objectAtIndex:2]];
    
    
    startTime =[dic objectForKey:@"endTime"];
    array = [startTime componentsSeparatedByString:@" "];
    startTime = [array objectAtIndex:0];
    array = [startTime componentsSeparatedByString:@"-"];
    NSMutableString *end = [NSMutableString stringWithFormat:@"%@.%@",[array objectAtIndex:1],[array objectAtIndex:2]];
    self.actTimeLabel.text =[NSString stringWithFormat:@"%@-%@",Start,end];// @"12.15-12.25";
    
    
    
    
    
//    self.actCountLabel.text = @"已报名:100/0999";
    self.actCountLabel.text = [NSString stringWithFormat:@"已报名:%@/%@", dic[@"participators"],dic[@"limitedParticipators"]];
//    self.actCountLabel.hidden = YES;
    CGSize size = [self.actTitleLabel.text boundingRectWithSize:CGSizeMake(self.actImg.frame.size.width-15, self.actImg.frame.size.height/2)
                                                          options:NSStringDrawingUsesLineFragmentOrigin
                                                       attributes:@{NSFontAttributeName:self.actTitleLabel.font}
                                                          context:nil].size;
    self.actTitleLabel.frame = CGRectMake(15, self.actImg.frame.size.height-size.height-5, size.width, size.height);
    
    
    size = [self.actTimeLabel.text boundingRectWithSize:CGSizeMake(self.actImg.frame.size.width-15, self.actImg.frame.size.height/2)
                                                 options:NSStringDrawingUsesLineFragmentOrigin
                                              attributes:@{NSFontAttributeName:self.actTimeLabel.font}
                                                 context:nil].size;
    self.actTimeLabel.frame = CGRectMake(10, self.actImg.frame.size.height+5, size.width+5, size.height+5);
    
    
    size = [self.actCountLabel.text boundingRectWithSize:CGSizeMake(self.actImg.frame.size.width-15, self.actImg.frame.size.height/2)
                                                options:NSStringDrawingUsesLineFragmentOrigin
                                             attributes:@{NSFontAttributeName:self.actCountLabel.font}
                                                context:nil].size;
    self.actCountLabel.frame = CGRectMake(self.actTimeLabel.frame.origin.x+self.actTimeLabel.frame.size.width+5, self.actImg.frame.size.height+5, size.width+5, size.height+5);
    
    size = [self.actPriceLabel.text boundingRectWithSize:CGSizeMake(self.actImg.frame.size.width-15, self.actImg.frame.size.height/2)
                                                 options:NSStringDrawingUsesLineFragmentOrigin
                                              attributes:@{NSFontAttributeName:self.actPriceLabel.font}
                                                 context:nil].size;
    self.actPriceLabel.frame = CGRectMake(APP_VIEW_WIDTH-size.width-20, self.actImg.frame.size.height+5, size.width+5, size.height+5);
    
    
}

@end
