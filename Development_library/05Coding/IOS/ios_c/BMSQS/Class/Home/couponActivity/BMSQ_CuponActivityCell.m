//
//  BMSQ_CuponCell.m
//  BMSQC
//
//  Created by djx on 15/7/27.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CuponActivityCell.h"
#import "UIImageView+WebCache.h"

@interface BMSQ_CuponActivityCell()

@property (nonatomic, strong)NSArray *status;


@end


@implementation BMSQ_CuponActivityCell

@synthesize delegate;

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
    if (self) {
        
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    
    
    self.status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券"];
    
    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(15, 5, APP_VIEW_WIDTH-30-80, 77)];
    iv_topBack.image = [UIImage imageNamed:@"coupon_left"];
    iv_topBack.userInteractionEnabled = YES;
    [self addSubview:iv_topBack];
    
    iv_shopLogo = [[UIImageView alloc]initWithFrame:CGRectMake(8, 13, 50, 50)];
    iv_shopLogo.layer.borderWidth = 0.5;
    iv_shopLogo.layer.cornerRadius = 5;
    iv_shopLogo.layer.borderColor = [[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1]CGColor];
    iv_shopLogo.layer.masksToBounds = YES;
    iv_shopLogo.layer.backgroundColor = [[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1]CGColor];
    [iv_topBack addSubview:iv_shopLogo];
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(65, 5, iv_topBack.frame.size.width - 70, 25)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    [lb_shopName setFont:[UIFont fontWithName:APP_FONT_NAME size:14]];
    [iv_topBack addSubview:lb_shopName];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(65, 30, iv_topBack.frame.size.width - 70, 20)];
    lb_useDes.backgroundColor = [UIColor clearColor];
    lb_useDes.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_useDes.text = @"";
    lb_useDes.font = [UIFont systemFontOfSize:10];
    [iv_topBack addSubview:lb_useDes];
    
    
    lb_endTime = [[UILabel alloc]initWithFrame:CGRectMake(65, 50, iv_topBack.frame.size.width - 70, 20)];
    lb_endTime.font = [UIFont systemFontOfSize:10];
    lb_endTime.text = @"有效期:";
    lb_endTime.textColor = UICOLOR(135, 135, 135, 1.0);
    [iv_topBack addSubview:lb_endTime];
    
    iv_bottomBack = [[UIImageView alloc]initWithFrame:CGRectMake(15, 77, APP_VIEW_WIDTH - 35, 70)];
    
    lb_couponTime = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 118, 20)];
    lb_couponTime.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_couponTime.text = @"00:00:00 - 00:00:00";
    lb_couponTime.font = [UIFont systemFontOfSize:12];
    [iv_bottomBack  addSubview:lb_couponTime];
    
    lb_state = [[UILabel alloc]initWithFrame:CGRectMake(10, 25, APP_VIEW_WIDTH - 118, 20)];
    lb_state.font = [UIFont systemFontOfSize:12];
    lb_state.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_state.text = @"使用说明:";
    [iv_bottomBack addSubview:lb_state];
    
    lb_stateLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 45,iv_topBack.frame.size.width-50 , 20)];
    lb_stateLabel.font = [UIFont systemFontOfSize:12];
    lb_stateLabel.backgroundColor = [UIColor clearColor];
    lb_stateLabel.text = @"暂无说明";
    lb_stateLabel.textColor = UICOLOR(135, 135, 135, 1.0);
    [iv_bottomBack addSubview:lb_stateLabel];
    //    iv_bottomBack.hidden = YES;
    
    
    [iv_topBack addSubview:iv_bottomBack];
    
    
    iv_rightBack = [[UIImageView alloc]initWithFrame:CGRectMake(iv_topBack.frame.origin.x+iv_topBack.frame.size.width, 5, 80, iv_topBack.frame.size.height)];
    iv_rightBack.image = [UIImage imageNamed:@"coupon_5"];
    iv_rightBack.userInteractionEnabled = YES;
    [self addSubview:iv_rightBack];
    
    
    lb_distance = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 30)];
    [lb_distance setTextColor:[UIColor whiteColor]];
    lb_distance.textAlignment = NSTextAlignmentCenter;
    lb_distance.backgroundColor = [UIColor clearColor];
    lb_distance.text = @"距离";
    [lb_distance setFont:[UIFont fontWithName:APP_FONT_NAME size:12]];
    [iv_rightBack addSubview:lb_distance];
    
    
    lb_price = [[UILabel alloc]initWithFrame:CGRectMake(0, 15, 80, 35)];
    [lb_price setTextColor:[UIColor whiteColor]];
    lb_price.textAlignment = NSTextAlignmentCenter;
    lb_price.backgroundColor = [UIColor clearColor];
    lb_price.text = [NSString stringWithFormat:@"%@ 50",RMB_SYMBOL];
    [lb_price setFont:[UIFont fontWithName:APP_FONT_NAME size:15]];
    [iv_rightBack addSubview:lb_price];
    
    
    _btn_grab = [[UIButtonEx alloc]initWithFrame:CGRectMake(0, lb_price.frame.origin.y+lb_price.frame.size.height-10, 80, 15)];
    [_btn_grab setTitle:@"抢" forState:UIControlStateNormal];
    [_btn_grab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [_btn_grab.titleLabel setFont:[UIFont systemFontOfSize:15]];
    [_btn_grab addTarget:self action:@selector(btnGrabClick:) forControlEvents:UIControlEventTouchUpInside];
    _btn_grab.backgroundColor = [UIColor clearColor];
    [iv_rightBack addSubview:_btn_grab];
    
    
    
    btn_expansion = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, iv_rightBack.frame.size.width, 17)];
    [btn_expansion addTarget:self action:@selector(btnExpansionClick) forControlEvents:UIControlEventTouchUpInside];
    btn_expansion.backgroundColor = [UIColor clearColor];
    [btn_expansion setImage:[UIImage imageNamed:@"iv_jtDown"] forState:UIControlStateNormal];
    [iv_rightBack addSubview:btn_expansion];
    [iv_rightBack bringSubviewToFront:btn_expansion];

    
//    iv_jtDown = [[UIImageView alloc]initWithFrame:CGRectMake(0, 10, 10, 5)];
//    iv_jtDown.image = [UIImage imageNamed:@"iv_jtDown"];
//    iv_jtDown.center = CGPointMake(btn_expansion.frame.size.width/2, btn_expansion.frame.size.height/2);
//    [btn_expansion addSubview:iv_jtDown];
    
    
    
    
    
    
}

- (void)setCellValue:(NSDictionary*)dicCupon indexPath:(NSIndexPath*)path
{
    if(dicCupon == nil || dicCupon.count <= 0)
    {
        return;
    }
    
    self.type =[dicCupon objectForKey:@"couponType"];
    _btn_grab.object = dicCupon;
    self.row =(int) path.row;
    self.dicCupon = dicCupon;
    NSString *imageName = [NSString stringWithFormat:@"coupon_%@",self.type];
    iv_rightBack.image = [UIImage imageNamed:imageName];
    NSString *distence = [dicCupon objectForKey:@"distance"];
    
    //距离
    if (distence.floatValue/1000 < 1 )
    {
        lb_distance.text = [NSString stringWithFormat:@"%fM",distence.floatValue];
    }
    else if (distence.floatValue/1000 < 10 && distence.floatValue/1000 > 1)
    {
        lb_distance.text = [NSString stringWithFormat:@"%.2fKM",distence.floatValue/1000];
    }
    else
    {
        lb_distance.text = @">10KM";
    }
    
    
    //说明
    if ([dicCupon objectForKey:@"remark"] == NULL || [[dicCupon objectForKey:@"remark"]  isEqual:@""]) {
        
        lb_stateLabel.text = @"暂无说明";
    }
    else
    {
        lb_stateLabel.text = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"remark"]];
    }
    
    
    if (self.type.intValue == 1)
    {
        
        lb_price.text = [NSString stringWithFormat:@"%@ %@",RMB_SYMBOL,[dicCupon objectForKey:@"insteadPrice"]];
        lb_useDes.text = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"function"]];
    }
    else if (self.type.intValue == 3)
    {
        lb_price.text = [NSString stringWithFormat:@"%@ %@",RMB_SYMBOL,[dicCupon objectForKey:@"insteadPrice"]];
        lb_useDes.text = [NSString stringWithFormat:@"满%@元立减%@元",[dicCupon objectForKey:@"availablePrice"],[dicCupon objectForKey:@"insteadPrice"]];
    }
    else if (self.type.intValue == 4)
    {
        lb_price.text = [NSString stringWithFormat:@"%@折",[dicCupon objectForKey:@"discountPercent"]];
        lb_useDes.text = [NSString stringWithFormat:@"满%@元可使用",[dicCupon objectForKey:@"availablePrice"]];
    }
    else
    {
        
        lb_price.text = [NSString stringWithFormat:@"%@ %@",RMB_SYMBOL,[dicCupon objectForKey:@"insteadPrice"]];
        lb_useDes.text = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"function"]];
    }
    
    
    if ([[NSString stringWithFormat:@"%@ - %@",[dicCupon objectForKey:@"dayStartUsingTime"],[dicCupon objectForKey:@"dayEndUsingTime"]]  isEqual:  @"00:00:00 - 00:00:00"])
    {
        lb_couponTime.text = @"全天可用";
    }
    else
    {
        lb_couponTime.text = [NSString stringWithFormat:@"%@ - %@",[dicCupon objectForKey:@"dayStartUsingTime"],[dicCupon objectForKey:@"dayEndUsingTime"]];
    }
    
    //    lb_status.text = [self.status objectAtIndex:self.type.intValue];
    lb_shopName.text = [gloabFunction changeNullToBlank:[dicCupon objectForKey:@"shopName"]];
    
    
    NSString *startTimeString =  [dicCupon objectForKey:@"startUsingTime"] ;
    NSString *expireTimeString = [dicCupon objectForKey:@"expireTime"] ;
    startTimeString = [startTimeString stringByReplacingOccurrencesOfString :@"-" withString:@"."];
    expireTimeString = [startTimeString stringByReplacingOccurrencesOfString :@"-" withString:@"."];
    
    lb_endTime.text = [NSString stringWithFormat:@"%@-%@",startTimeString,expireTimeString];
    
    
    [iv_shopLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicCupon objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    NSString* isMyReceived = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"isMyReceived"]];
    if ([isMyReceived isEqualToString:@"1"])
    {
        _btn_grab.userInteractionEnabled = NO;
        [_btn_grab setTitle:@"已领" forState:UIControlStateNormal];
    }
    else
    {
        [_btn_grab setTitle:@"抢" forState:UIControlStateNormal];
        _btn_grab.userInteractionEnabled = YES;
    }
    
    
    /*
     
     self.row =(int) path.row;
     btn_grab.object = dicCupon;
     btn_expansion.object = dicCupon;
     lb_shopName.text = [gloabFunction changeNullToBlank:[dicCupon objectForKey:@"shopName"]];
     
     NSString* isMyReceived = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"isMyReceived"]];
     if ([isMyReceived isEqualToString:@"1"])
     {
     btn_grab.userInteractionEnabled = NO;
     [btn_grab setTitle:@"已领" forState:UIControlStateNormal];
     }
     else
     {
     [btn_grab setTitle:@"抢" forState:UIControlStateNormal];
     btn_grab.userInteractionEnabled = YES;
     }
     lb_endTime.text = [NSString stringWithFormat:@"使用时间: %@",[dicCupon objectForKey:@"expireTime"]];
     
     
     [iv_shopLogo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dicCupon objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
     
     
     lb_state.text = [NSString stringWithFormat:@"使用说明: %@",[dicCupon objectForKey:@"remark"]];
     
     */
    
}




- (void)btnExpansionClick
{
    btn_expansion.selected = !btn_expansion.selected;
    if (btn_expansion.selected)
    {
        NSString *img = [NSString stringWithFormat:@"coupon_%@",self.type];
        
        CGRect rect = iv_topBack.frame;
        rect.size.height = 147;
        iv_topBack.frame = rect;
        iv_topBack.image = [UIImage imageNamed:img];
        iv_bottomBack.hidden = NO;
        
        rect = iv_rightBack.frame;
        rect.size.height = 147;
        iv_rightBack.frame = rect;
        iv_rightBack.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_long",img]];
        
//        iv_jtDown.image = [UIImage imageNamed:@"iv_jtUp"];
        
    
        btn_expansion.frame =  CGRectMake(0, iv_rightBack.frame.size.height-17, iv_rightBack.frame.size.width, 17);;
    }
    else
    {
        CGRect rect = iv_topBack.frame;
        rect.size.height = 77;
        iv_topBack.frame = rect;
        iv_topBack.image = [UIImage imageNamed:@"coupon_left"];
        iv_bottomBack.hidden = YES;
        
        rect = iv_rightBack.frame;
        rect.size.height = 77;
        iv_rightBack.frame = rect;
        iv_rightBack.image = [UIImage imageNamed:@"iv_time"];
        
//        iv_jtDown.image = [UIImage imageNamed:@"iv_jtDown"];
        
       
        btn_expansion.frame = CGRectMake(0, iv_rightBack.frame.size.height-17, iv_rightBack.frame.size.width, 17);
        
    }
    if (delegate != nil)
    {
        [delegate setCellSelect:btn_expansion.selected indexPath:self.row dic:self.dicCupon];
    }
}

- (void)setCellExpansion:(BOOL)isExpansion
{
    
    NSString *imgStr = [NSString stringWithFormat:@"coupon_%@",self.type];
    btn_expansion.selected = isExpansion;
    if (btn_expansion.selected)
    {
        CGRect rect = iv_topBack.frame;
        rect.size.height = 147;
        iv_topBack.frame = rect;
        iv_topBack.image = [UIImage imageNamed:@"coupon_left"];
        iv_bottomBack.hidden = NO;
        
        rect = iv_rightBack.frame;
        rect.size.height = 147;
        iv_rightBack.frame = rect;
        iv_rightBack.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_long",imgStr]];
        
//        iv_jtDown.image = [UIImage imageNamed:@"iv_jtUp"];
        
       
        btn_expansion.frame =  CGRectMake(0, iv_rightBack.frame.size.height-17, iv_rightBack.frame.size.width, 17);
        //        btn_expansion.transform = CGAffineTransformMakeRotation(-M_PI);
    }
    else
    {
        CGRect rect = iv_topBack.frame;
        rect.size.height = 77;
        iv_topBack.frame = rect;
        iv_topBack.image = [UIImage imageNamed:@"coupon_left"];
        iv_bottomBack.hidden = YES;
        
        rect = iv_rightBack.frame;
        rect.size.height = 77;
        iv_rightBack.frame = rect;
        iv_rightBack.image = [UIImage imageNamed:imgStr];
        
//        iv_jtDown.image = [UIImage imageNamed:@"iv_jtDown"];
        

        btn_expansion.frame =  CGRectMake(0, iv_rightBack.frame.size.height-17, iv_rightBack.frame.size.width, 17);

    }
}

- (void)btnGrabClick:(UIButtonEx*)sender
{
}

@end
