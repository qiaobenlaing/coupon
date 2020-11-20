//
//  CardFeed_Cell.m
//  BMSQS
//
//  Created by liuqin on 15/10/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "CardFeed_Cell.h"
#import "UIImageView+AFNetworking.h"

@interface CardFeed_Cell ()

@property (nonatomic, strong)UIImageView *headImage;
@property (nonatomic, strong)UILabel *moneyLabel;
@property (nonatomic, strong)UILabel *usedCouponNameLabel;
@property (nonatomic, strong)UILabel *statusLabel;
@property (nonatomic, strong)UILabel *userLabel;
@property (nonatomic, strong)UILabel *TimeLabel;


@end


@implementation CardFeed_Cell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
        bgView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:bgView];
        
        //用户头像
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 50, 50)];
        self.headImage.layer.masksToBounds = YES;
        self.headImage.layer.cornerRadius = 5;
        self.headImage.backgroundColor = [UIColor clearColor];
        [bgView addSubview:self.headImage];
        
        //金额
        self.moneyLabel = [[UILabel alloc]initWithFrame:CGRectMake(70, 10, APP_VIEW_WIDTH-100, 20)];
        self.moneyLabel.font = [UIFont systemFontOfSize:18.f];
        self.moneyLabel.backgroundColor = [UIColor clearColor];
        [bgView addSubview:self.moneyLabel];
        
        //使用的优惠券
        self.usedCouponNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 10, APP_VIEW_WIDTH/2, 20)];
        self.usedCouponNameLabel.font = [UIFont systemFontOfSize:13.f];
        self.usedCouponNameLabel.backgroundColor = [UIColor clearColor];
        [bgView addSubview:self.usedCouponNameLabel];
        
        
        //支付状态
        self.statusLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-110, 10, 100, 20)];
        self.statusLabel.font = [UIFont systemFontOfSize:14.f];
        self.statusLabel.backgroundColor = [UIColor clearColor];
        self.statusLabel.textAlignment = NSTextAlignmentRight;
        [bgView addSubview:self.statusLabel];
        
        //用户昵称
        self.userLabel = [[UILabel alloc]initWithFrame:CGRectMake(70, 30, APP_VIEW_WIDTH-100, 20)];
        self.userLabel.font = [UIFont systemFontOfSize:14.f];
        self.userLabel.backgroundColor = [UIColor clearColor];
        [bgView addSubview:self.userLabel];
        
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 70-0.5, APP_VIEW_WIDTH, 0.5)];
        lineView.backgroundColor = APP_CELL_LINE_COLOR;
        [bgView addSubview:lineView];
        
        
        //时间
        self.TimeLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 70, APP_VIEW_WIDTH-20, 30)];
        self.TimeLabel.textAlignment = NSTextAlignmentRight;
        self.TimeLabel.font = [UIFont systemFontOfSize:13.f];
        self.TimeLabel.backgroundColor = [UIColor clearColor];
        [bgView addSubview:self.TimeLabel];

        
    }
    return self;
}

-(void)setCareFeedCell:(NSDictionary *)dic{
    
    
    
    id userAvatar =[dic objectForKey:@"avatarUrl"];
    
    if (  [userAvatar isKindOfClass:[NSNull class]] ) {
        [self.headImage setImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }else{
        
        NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"avatarUrl"]];
        [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }
    
    id userName = [dic objectForKey:@"nickName"];
    if ([userName isKindOfClass:[NSNull class]]) {
        self.userLabel.text =@"会员";
    }else{
        self.userLabel.text = [NSString stringWithFormat:@"%@(%@)",[dic objectForKey:@"nickName"], [dic objectForKey:@"userMobileNbr"]];
    }
    
    id message = [dic objectForKey:@"realPay"];
    if ([message isKindOfClass:[NSNull class]] ) {
        self.moneyLabel.text =@"";
    }else{
        self.moneyLabel.text = [NSString stringWithFormat:@"%@元",[dic objectForKey:@"realPay"]];
    }
    
    self.usedCouponNameLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"usedCouponName"]];
    
    id createTime = [dic objectForKey:@"orderTime"];
    if ([createTime isKindOfClass:[NSNull class]]) {
        self.TimeLabel.text = @"";
    }else{
        self.TimeLabel.text =[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderTime"]];
    }
    
    id userConsumeStatus = [dic objectForKey:@"userConsumeStatus"];
    if ([userConsumeStatus isKindOfClass:[NSNull class]]) {
        self.statusLabel.text = @"";
        
    }else{
        
//        1-未付款，2-付款中，3-已付款，4-已取消付款，5-付款失败 ，6-退款申请中，7-已退款
        
        int i = [userConsumeStatus intValue];
        NSString *message;
        switch (i ) {
            case 1:
                message = @"未付款";
                break;
            case 2:
                message = @"付款中";
                break;
            case 3:
                message = @"";
                break;
            case 4:
                message = @"已取消付款";
                break;
            case 5:
                message = @"付款失败";
                break;
            case 6:
                message = @"退款申请中";
                break;
            case 7:
                message = @"已退款";
                self.moneyLabel.text = [NSString stringWithFormat:@"-%@元",[dic objectForKey:@"realPay"]];
                break;
                
            default:
                break;
        }
        
        self.statusLabel.text =message;
    }
    
    
}
@end
