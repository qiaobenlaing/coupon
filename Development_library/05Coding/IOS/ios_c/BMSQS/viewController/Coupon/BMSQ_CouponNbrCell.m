//
//  BMSQ_CouponNbrCell.m
//  BMSQC
//
//  Created by liuqin on 15/12/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponNbrCell.h"


#import "CouponImage.h"
#import "ActiVityModel.h"


@interface BMSQ_CouponNbrCell ()

@property (nonatomic, strong) UIImageView *shadowImage;
@property (nonatomic, strong) UIView *shadowView;
@property (nonatomic, strong) UILabel *couponNbrLabe;
@property (nonatomic, strong) UIImageView *couponCodeQR;
@property (nonatomic, strong) UIImageView *typeImage;

@property (nonatomic, strong) UIButton *opButton;

@end


@implementation BMSQ_CouponNbrCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 60)];
        bgView.backgroundColor = [UIColor clearColor];
        [self addSubview:bgView];
        
        self.shadowImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 60)];
        [self.shadowImage setImage:[UIImage imageNamed:@"hide_coupon"]];
        [bgView addSubview:self.shadowImage];
        
        self.shadowView = [[UIView alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 120)];
        self.shadowView.backgroundColor = [UIColor whiteColor];
        [bgView addSubview:self.shadowView];
        
        self.couponCodeQR = [[UIImageView alloc]initWithFrame:CGRectMake(20, 45, self.shadowView.frame.size.width-40, 60)];
        [self.shadowView addSubview:self.couponCodeQR];
        
        
        
       self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 40)];
        [bgView addSubview:self.typeImage];
        
        self.opButton = [[UIButton alloc]initWithFrame:CGRectMake(200, 8, 60,20)];
        self.opButton.layer.cornerRadius =3;
        self.opButton.layer.masksToBounds = YES;
        self.opButton.layer.borderWidth = 0.5;
        self.opButton.layer.borderColor = [[UIColor whiteColor]CGColor];
        [self.opButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.opButton.titleLabel.font = [UIFont systemFontOfSize:11];
        [self.typeImage addSubview:self.opButton];
        self.opButton.hidden = YES;
        
        [self.opButton addTarget:self action:@selector(clickCouponStatus:) forControlEvents:UIControlEventTouchUpInside];
        
        self.couponNbrLabe = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, self.typeImage.frame.size.width, 35)];
        self.couponNbrLabe.textColor = [UIColor whiteColor];
        self.couponNbrLabe.font = [UIFont boldSystemFontOfSize:13.f];
        [self.typeImage addSubview:self.couponNbrLabe];
        
        
        
        self.typeImage.userInteractionEnabled = YES;
        
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesutre)];
        [self.typeImage addGestureRecognizer:tapGesture];
        
        UIImageView *tapImgView = [[UIImageView alloc]initWithFrame:CGRectMake(self.typeImage.frame.size.width/2-5, 30, 10, 5)];
        [tapImgView setImage:[UIImage imageNamed:@"iv_jtDown"]];
        [self.typeImage addSubview:tapImgView];
    }
    return self;
}
-(void)creatNbrCell:(NSDictionary *)couponDic isShow:(BOOL)isShow{
    self.couponDic = couponDic;

    
    
    if(self.isAct){
        
        NSString *couponType =[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"status"]];
        NSString *imgName = [CouponImage couponType:[couponType intValue]];
        [self.typeImage setImage:[UIImage imageNamed:imgName]];

        NSString *userCouponNbr = [couponDic objectForKey:@"actCode"];
        self.couponNbrLabe.text = userCouponNbr;
        [self.couponCodeQR setImage:[CouponImage couponCodeQR:userCouponNbr]];
        
         self.opButton.hidden = NO;
         NSString *str = [ActiVityModel getStatusStr:[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"status"]]];
         CGSize size= [str boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                                     options:NSStringDrawingUsesLineFragmentOrigin
                                                  attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.f]}
                                                     context:nil].size;

        self.opButton.frame = CGRectMake(self.typeImage.frame.size.width-(size.width+10)-10, 6, size.width+10, size.height+5);
        [self.opButton setTitle:str forState:UIControlStateNormal];

    }else{
        
        NSString *couponType =[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"couponType"]];
        NSString *imgName = [CouponImage couponType:[couponType intValue]];
        [self.typeImage setImage:[UIImage imageNamed:imgName]];
        

        NSString *userCouponNbr = [couponDic objectForKey:@"userCouponNbr"];
        self.couponNbrLabe.text = userCouponNbr;
        [self.couponCodeQR setImage:[CouponImage couponCodeQR:userCouponNbr]];
      
    if ([couponType intValue]==7 ||[couponType intValue]==8) {
        
        NSString *orderCouponStatus = [CouponImage getOrderCouponStatus:[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"orderCouponStatus"]]];
        
        CGSize size= [orderCouponStatus boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                                     options:NSStringDrawingUsesLineFragmentOrigin
                                                  attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.f]}
                                                     context:nil].size;
        if (size.width>0) {
            self.opButton.hidden = NO;

        }else{
            self.opButton.hidden = YES;
 
        }
        
        self.opButton.frame = CGRectMake(self.typeImage.frame.size.width-(size.width+10)-10, 6, size.width+10, size.height+5);
        [self.opButton setTitle:orderCouponStatus forState:UIControlStateNormal];
        
    }else{
        self.opButton.hidden = YES;

    }
 
    }
  

    if (isShow) {
        self.shadowImage.hidden = YES;
        self.shadowView.hidden = NO;
    }else{
        self.shadowImage.hidden = NO;
        self.shadowView.hidden = YES;

    }
    
    
}

-(void)clickCouponStatus:(UIButton *)button{
    
    if ([self.NbrDelegate respondsToSelector:@selector(applicationCoupon:)]) {
        [self.NbrDelegate applicationCoupon:self.couponDic];
    }
    
    
}

-(void)tapGesutre{
    
    [self.NbrDelegate clickCouponType:self.indexpath];
    
}

@end
