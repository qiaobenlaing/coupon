//
//  N_footTableViewCell.m
//  BMSQS
//
//  Created by liuqin on 16/3/1.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "N_footTableViewCell.h"
#import "UIImageView+WebCache.h"

@interface N_footTableViewCell ()
@property (nonatomic, strong)UIImageView *shopImage;
@property (nonatomic, strong)UILabel *userLabel;
@property (nonatomic, strong)UILabel *nbrLast4;;
@property (nonatomic, strong)UILabel *expenseLabel;//花费
@property (nonatomic, strong)UILabel *payLabel;//支付
@property (nonatomic, strong)UILabel *discountLabel;//优惠
@property (nonatomic, strong)UILabel *timeLable;//时间
@property (nonatomic, strong)UILabel *orderLabel;//订单号

@end

@implementation N_footTableViewCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 105)];
        bgView.backgroundColor = [UIColor whiteColor];
        [self addSubview:bgView];
        
        self.shopImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 15, 60, 60)];
        self.shopImage.backgroundColor = [UIColor clearColor];
        self.shopImage.layer.masksToBounds = YES;
        self.shopImage.layer.cornerRadius=3;
        [self addSubview:self.shopImage];
        
        self.nbrLast4 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, 0, 120, 25)];
        self.nbrLast4.textColor = APP_TEXTCOLOR;
        self.nbrLast4.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.nbrLast4];
        
        self.userLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.shopImage.frame.origin.x+self.shopImage.frame.size.width+5, 0, APP_VIEW_WIDTH-(self.nbrLast4.frame.size.width+self.shopImage.frame.size.width+25), 20)];
        self.userLabel.textColor = APP_TEXTCOLOR;
        self.userLabel.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.userLabel];
        
        self.expenseLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.shopImage.frame.origin.x+self.shopImage.frame.size.width+5, 20, APP_VIEW_WIDTH-(self.nbrLast4.frame.size.width+self.shopImage.frame.size.width+25),20)];
        self.expenseLabel.text = @"消费**元";
        self.expenseLabel.textColor = APP_TEXTCOLOR;
        self.expenseLabel.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.expenseLabel];
        
        self.payLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, 20, APP_VIEW_WIDTH-(self.nbrLast4.frame.size.width+self.shopImage.frame.size.width+25),20)];
        self.payLabel.text = @"支付**元";
        self.payLabel.textColor = APP_TEXTCOLOR;
        self.payLabel.textAlignment = NSTextAlignmentRight;
        self.payLabel.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.payLabel];
        
        self.discountLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.userLabel.frame.origin.x,40, APP_VIEW_WIDTH-(self.nbrLast4.frame.size.width+self.shopImage.frame.size.width+25),20)];
        self.discountLabel.text = @"优惠**元";
        self.discountLabel.textColor = APP_TEXTCOLOR;
        self.discountLabel.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.discountLabel];
        
        self.timeLable = [[UILabel alloc]initWithFrame:CGRectMake(self.userLabel.frame.origin.x,60, APP_VIEW_WIDTH-(self.shopImage.frame.size.width+25),25)];
        self.timeLable.text = @"消费时间:";
        self.timeLable.textColor = APP_TEXTCOLOR;
        self.timeLable.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.timeLable];
        
        self.orderLabel = [[UILabel alloc]initWithFrame:CGRectMake(50,80, APP_VIEW_WIDTH-30,25)];
        self.orderLabel.text = @"订单号:";
        self.orderLabel.textColor = APP_TEXTCOLOR;
        self.orderLabel.font = [UIFont systemFontOfSize:13.f];
        [self addSubview:self.orderLabel];
        
    }
    return self;
}
-(void)setMyCell:(NSDictionary *)dic{
    [self.shopImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@""]];
    self.userLabel.text = [dic objectForKey:@"nickName"];
    self.nbrLast4.text = [NSString stringWithFormat:@"手机号后四位:%@",[dic objectForKey:@"mobileNbr"]];
    float orderAmount = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderAmount"]] floatValue];
    NSString *orderStr = orderAmount>10000?[NSString stringWithFormat:@"%0.2f万",orderAmount/10000]:[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderAmount"]];
    self.expenseLabel.text = [NSString stringWithFormat:@"消费%@元",orderStr];
    
    orderAmount = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"realPay"]] floatValue];
    orderStr = orderAmount>10000?[NSString stringWithFormat:@"%0.2f万",orderAmount/10000]:[NSString stringWithFormat:@"%@",[dic objectForKey:@"realPay"]];
    self.payLabel.text = [NSString stringWithFormat:@"支付%@元",orderStr];
    
    orderAmount = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"deduction"]] floatValue];
    orderStr = orderAmount>10000?[NSString stringWithFormat:@"%0.2f万",orderAmount/10000]:[NSString stringWithFormat:@"%@",[dic objectForKey:@"deduction"]];
    self.discountLabel.text = [NSString stringWithFormat:@"优惠%@元",orderStr];
    
    self.timeLable.text = [NSString stringWithFormat:@"消费时间:%@",[dic objectForKey:@"payedTime"]];
    self.orderLabel.text = [NSString stringWithFormat:@"订单号:%@",[dic objectForKey:@"orderNbr"]];

}
@end
