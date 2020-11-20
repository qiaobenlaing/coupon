//
//  BMSQ_shopImageCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/9.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_shopImageCell.h"


@interface BMSQ_shopImageCell ()

@property (nonatomic, strong)UIScrollView *imageScrollView; //背景图
@property (nonatomic, strong)UILabel *shopNameLabel;         //店家名称
@property (nonatomic, strong)UIImageView *cardImage;        //卡图标
@property (nonatomic, strong)UILabel *discountLabel;         //折扣
@property (nonatomic, strong)UIButton *phoneBtn;       //电话

@property (nonatomic, strong)NSString *phoneStr;       //电话

@end

@implementation BMSQ_shopImageCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.imageScrollView = [[UIScrollView alloc]init];
        [self addSubview:self.imageScrollView];
        
        self.shopNameLabel = [[UILabel alloc]init];
        self.shopNameLabel.textColor = APP_TEXTCOLOR;
        self.shopNameLabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.shopNameLabel];
        
        self.cardImage = [[UIImageView alloc]init];
        [self.cardImage setImage:[UIImage imageNamed:@"ICBCcardicon"] ];
        [self addSubview:self.cardImage];
        
        self.discountLabel = [[UILabel alloc]init];
        self.discountLabel.textColor = APP_NAVCOLOR;
        self.discountLabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.discountLabel];
        
        self.phoneBtn = [[UIButton alloc]init];
        [self addSubview:self.phoneBtn];
        [self.phoneBtn setImage:[UIImage imageNamed:@"phoneIcon"] forState:UIControlStateNormal];
        [self.phoneBtn addTarget:self action:@selector(clickPhone) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return self;
}

-(void)setShopImageCell:(id)imageS shopInfo:(id)object :(float)h{
    
    for (UIView *v in self.imageScrollView.subviews) {
        if([v isKindOfClass:[UIImageView class]]){
            [v removeFromSuperview];

        }
    }
    
    self.phoneStr = [object objectForKey:@"tel"];
    
    self.imageScrollView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, h-45);
    self.imageScrollView.backgroundColor = [UIColor clearColor];
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, self.imageScrollView.frame.size.height)];
    [imageView setImage:[UIImage imageNamed:@"iv_noData"]];
    [self.imageScrollView addSubview:imageView];
    
    NSString *str =[object objectForKey:@"shopName"];
    CGSize size = [str boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:self.shopNameLabel.font}
                                                        context:nil].size;
    
    self.shopNameLabel.frame = CGRectMake(10,self.imageScrollView.frame.size.height , size.width, 45);
    self.shopNameLabel.text = str;
    
    self.cardImage.frame = CGRectMake(size.width+15, self.imageScrollView.frame.size.height+15, 15, 15);
    
    float onlinePaymentDiscount = [[NSString stringWithFormat:@"%@",[object objectForKey:@"onlinePaymentDiscount"]]floatValue];
    self.discountLabel.text =[NSString stringWithFormat:@"%0.2f",onlinePaymentDiscount];
    self.discountLabel.frame = CGRectMake(self.cardImage.frame.origin.x+45,self.imageScrollView.frame.size.height , 45, 45);
    if (onlinePaymentDiscount==0 || onlinePaymentDiscount ==10) {
        self.discountLabel.hidden = YES;
        self.cardImage.hidden = YES;
    }else{
        self.discountLabel.hidden = NO;
        self.cardImage.hidden = NO;
    }
    
    self.phoneBtn.frame = CGRectMake(APP_VIEW_WIDTH-45, self.imageScrollView.frame.size.height, 45, 45);
 
    
    
}
-(void)clickPhone{
    
    if ([self.shopHeadDelegate respondsToSelector:@selector(clicktelShop:)]) {
        [self.shopHeadDelegate clicktelShop:self.phoneStr];
    }
    
}
@end
