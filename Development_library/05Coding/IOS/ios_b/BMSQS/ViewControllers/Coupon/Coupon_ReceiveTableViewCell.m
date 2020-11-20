//
//  Coupon_ReceiveTableViewCell.m
//  BMSQS
//
//  Created by liuqin on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "Coupon_ReceiveTableViewCell.h"

#import "UIImageView+AFNetworking.h"

@interface Coupon_ReceiveTableViewCell ()

@property (nonatomic, strong)UIImageView *headImage;

@property (nonatomic, strong)UILabel *timelabel;
@property (nonatomic, strong)UILabel *userNameLabel;
@property (nonatomic, strong)UILabel *couponMessage;


@end


@implementation Coupon_ReceiveTableViewCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.backgroundColor = [UIColor clearColor];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(8, 10, APP_VIEW_WIDTH-16, 90)];
        backView.layer.masksToBounds = YES;
        backView.layer.cornerRadius = 5;
        backView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:backView];
        
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 70, 70)];
        self.headImage.layer.masksToBounds = YES;
        self.headImage.layer.cornerRadius = 5;
        self.headImage.backgroundColor = [UIColor clearColor];
        [backView addSubview:self.headImage];
        
        self.timelabel = [[UILabel alloc]initWithFrame:CGRectMake(self.headImage.frame.size.width, 2, backView.frame.size.width-self.headImage.frame.size.width-10, 10)];
        self.timelabel.textColor = [UIColor grayColor];
        self.timelabel.text = @"2015-09-09 12:12:12";
        self.timelabel.textAlignment = NSTextAlignmentRight;
        self.timelabel.font = [UIFont systemFontOfSize:11.f];
        [backView addSubview:self.timelabel];
        
        self.userNameLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.headImage.frame.size.width+15, 15, backView.frame.size.width-self.headImage.frame.size.width-10, 20)];
        self.userNameLabel.textColor = [UIColor blackColor];
        self.userNameLabel.text = @"用户3223425424534535";
        self.userNameLabel.font = [UIFont systemFontOfSize:15.f];
        [backView addSubview:self.userNameLabel];
        
        self.couponMessage = [[UILabel alloc]initWithFrame:CGRectMake(self.userNameLabel.frame.origin.x, self.userNameLabel.frame.origin.y+self.userNameLabel.frame.size.height, backView.frame.size.width-self.headImage.frame.size.width-20, backView.frame.size.height-(self.userNameLabel.frame.origin.y+self.userNameLabel.frame.size.height)-10)];
        self.couponMessage.text = @"领取了一张优惠券";
        self.couponMessage.font = [UIFont systemFontOfSize:13.f];
        self.couponMessage.backgroundColor = [UIColor clearColor];

        [backView addSubview:self.couponMessage];
        
        
        
        
    }
    return self;
}

//Coupon_receiveCell = 0,
//Coupon_FindCell ,
//,
//,

-(void)setReceiveCell:(NSDictionary *)receiveDic type:(CouponCellType *)type{
    
    if (type == Coupon_receiveCell) {
        
        
        NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[receiveDic objectForKey:@"avatarUrl"]];
        [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        self.timelabel.text = [receiveDic objectForKey:@"applyTime"];
        self.userNameLabel.text = [receiveDic objectForKey:@"nickName"];
        
//        int i = (int)[[receiveDic objectForKey:@"couponType"]integerValue];
        
//        if (i == 1) {
//            self.couponMessage.text = @"领取了N元购";
//        }
//        else if (i ==3 ) {
//            //         self.couponMessage.text=[NSString stringWithFormat:@"领取了满%@元立减%@元的优惠券",[receiveDic objectForKey:@"availablePrice"],[receiveDic objectForKey:@"insteadPrice"]];
//            self.couponMessage.text = @"领取了抵扣券";
//            
//        }else if (i == 4){
//            //        self.couponMessage.text=[NSString stringWithFormat:@"领取了满%@元打%0.1f折的优惠券",[receiveDic objectForKey:@"availablePrice"],(float) [[receiveDic objectForKey:@"discountPercent"]floatValue]];
//            self.couponMessage.text = @"领取了折扣券";
//            
//        }else if(i ==5){
//            self.couponMessage.text = @"领取了实物券";
//            
//        }else if (i==6){
//            self.couponMessage.text = @"领取了体验券";
//            
//        }else if (i==7) {
//            self.couponMessage.text = @"领取了兑换券";
//            
//        }else if (i==8) {
//            self.couponMessage.text = @"领取了代金券";
//            
//        }
        
        
        
    }
    else if (type == Coupon_FindCell){
        
        
        NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[receiveDic objectForKey:@"avatarUrl"]];
        [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
        self.timelabel.text = [receiveDic objectForKey:@"createTime"];
        self.userNameLabel.text = [receiveDic objectForKey:@"nickName"];
        self.couponMessage.text = [receiveDic objectForKey:@"content"];

    
    }
    else if (type == Coupon_CareCell){
        
        
        NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[receiveDic objectForKey:@"avatarUrl"]];
        [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
        self.timelabel.text = [receiveDic objectForKey:@"createTime"];
        self.userNameLabel.text = [receiveDic objectForKey:@"nickName"];
        self.couponMessage.text = [receiveDic objectForKey:@"content"];
      
        
    }
    else if (type == Coupon_shopCell){
        
        
        NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[receiveDic objectForKey:@"avatarUrl"]];
        [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
        self.timelabel.text = [receiveDic objectForKey:@"createTime"];
        self.userNameLabel.text = [receiveDic objectForKey:@"nickName"];
        self.couponMessage.text = [receiveDic objectForKey:@"content"];
      
        
    }
    else if (type == Coupon_chatCell){
        
        id userAvatar =[receiveDic objectForKey:@"userAvatar"];
        
        if (  [userAvatar isKindOfClass:[NSNull class]] ) {
            [self.headImage setImage:[UIImage imageNamed:@"Login_Icon"]];
        }else{
            
            NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[receiveDic objectForKey:@"userAvatar"]];
            [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        }
        
        id userName = [receiveDic objectForKey:@"userName"];
        if ([userName isKindOfClass:[NSNull class]]) {
            self.userNameLabel.text =@"会员";
        }else{
            self.userNameLabel.text = [receiveDic objectForKey:@"userName"];
        }
        
        id message = [receiveDic objectForKey:@"message"];
        if ([message isKindOfClass:[NSNull class]] ) {
            self.userNameLabel.text =@"";
        }else{
            self.couponMessage.text = [receiveDic objectForKey:@"message"];
        }
        
        
        id createTime = [receiveDic objectForKey:@"createTime"];
        if ([createTime isKindOfClass:[NSNull class]]) {
            self.timelabel.text = @"";
        }else{
            self.timelabel.text = [receiveDic objectForKey:@"createTime"];
        }
        
        
    }
    
}
- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
