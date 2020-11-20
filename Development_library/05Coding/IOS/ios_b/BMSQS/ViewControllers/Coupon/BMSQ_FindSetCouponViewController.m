//
//  BMSQ_FindSetCouponViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/16.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_FindSetCouponViewController.h"

#import "UIImageView+AFNetworking.h"
#import "SVProgressHUD.h"

#import "CouponTypeModel.h"

@interface BMSQ_FindSetCouponViewController ()
{
    
    UILabel* lb_shopName;
    UILabel* lb_useDes;
    UILabel* lb_endTime;
    //    UIButtonEx* btn_expansion;
    UIButton* btn_expansion;
    UILabel* lb_price;
    UILabel* lb_status;
    UIImageView* iv_jtDown;
    UIImageView* iv_topBack;
    UIImageView* iv_rightBack;
    UILabel *lb_couponTime;
    UILabel *lb_state;
    UILabel *lb_stateLabel;
    
    UILabel *lb_distance;
    
}

@property (nonatomic, strong)NSArray *status;
@property (nonatomic, strong)NSArray *coponbackColorS;
@property (nonatomic, strong)NSArray *coponImageS;
@property (nonatomic, strong)NSArray *coponBottomColorS;


@property (nonatomic, strong)UIImageView *typeImage;



@property (nonatomic, strong)UIImageView *shopImgaeLogo;



@end

@implementation BMSQ_FindSetCouponViewController




- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"优惠券预览"];
    
    
    
//    self.status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券"];
//    self.coponbackColorS = @[@"",@"250,96,59",@"",@"235,77,83",@"235,136,22",@"32,198,132",@"111,201,39"];
//    self.coponImageS = @[@"",@"coupon_1",@"",@"coupon_3",@"coupon_4",@"coupon_5",@"coupon_6"];
//    self.coponBottomColorS= @[@"",@"246,30,19",@"",@"218,15,31",@"219,71,10",@"21,152,61",@"53,155,0"];
    
    
    
    
    
    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y +10, APP_VIEW_WIDTH-20-60, 200)];
    iv_topBack.backgroundColor = [UIColor whiteColor];
    iv_topBack.userInteractionEnabled = YES;
    [self.view addSubview:iv_topBack];
    
    self.shopImgaeLogo = [[UIImageView alloc]initWithFrame:CGRectMake(8, 13, 50, 50)];
    self.shopImgaeLogo.layer.borderWidth = 0.5;
    self.shopImgaeLogo.layer.cornerRadius = 5;
    self.shopImgaeLogo.layer.borderColor=[[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1.0]CGColor];
    self.shopImgaeLogo.layer.masksToBounds = YES;
    self.shopImgaeLogo.layer.backgroundColor = [[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1.0]CGColor];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *logourl = [userDefaults objectForKey:@"logoUrl"];

    NSString *url = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,logourl];
    [self.shopImgaeLogo setImageWithURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
    [iv_topBack addSubview:self.shopImgaeLogo];
    
    
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(65, 5, iv_topBack.frame.size.width - 70, 25)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    lb_shopName.text = [userDefaults objectForKey:@"shopName"];
    [lb_shopName setFont:[UIFont systemFontOfSize:14]];
    [iv_topBack addSubview:lb_shopName];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(65, 30, APP_VIEW_WIDTH - 200, 20)];
    lb_useDes.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_useDes.backgroundColor = [UIColor clearColor];
    lb_useDes.text = self.Desctription;
    lb_useDes.font = [UIFont systemFontOfSize:10];
    [iv_topBack addSubview:lb_useDes];
    
    UILabel *userDate = [[UILabel alloc]initWithFrame:CGRectMake(65,50,iv_topBack.frame.size.width-self.shopImgaeLogo.frame.size.width, 20)];
    userDate.textColor = UICOLOR(135, 135, 135, 1.0);
    if (self.useDate.length ==0) {
        userDate.text =@"不限使用时间";

    }else{
        userDate.text =self.useDate;

    }
    
    userDate.font = [UIFont systemFontOfSize:11];
    [iv_topBack  addSubview:userDate];
    
    
    lb_couponTime = [[UILabel alloc]initWithFrame:CGRectMake(10, self.shopImgaeLogo.frame.origin.y+self.shopImgaeLogo.frame.size.height+5, APP_VIEW_WIDTH - 118, 20)];
    lb_couponTime.textColor = UICOLOR(135, 135, 135, 1.0);
    if([self.useTime isEqualToString:@"00:00 - 00:00"]){
        lb_couponTime.text =[NSString stringWithFormat:@"每天使用时间:全天使用"];

    }else{
        lb_couponTime.text =[NSString stringWithFormat:@"每天使用时间:%@",self.useTime];
  
    }
    
    lb_couponTime.font = [UIFont systemFontOfSize:12];
    [iv_topBack  addSubview:lb_couponTime];

//
    UILabel *reLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, lb_couponTime.frame.origin.y+lb_couponTime.frame.size.height+5, APP_VIEW_WIDTH - 50-80, 20)];
    reLabel.font = [UIFont systemFontOfSize:12];
    reLabel.text = @"使用说明";
    reLabel.textColor = UICOLOR(135, 135, 135, 1.0);
    reLabel.backgroundColor = [UIColor clearColor];
    reLabel.numberOfLines = 0;
    [iv_topBack addSubview:reLabel];
    
    lb_stateLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, reLabel.frame.origin.y+reLabel.frame.size.height+5, APP_VIEW_WIDTH - 50-80, 20)];
    lb_stateLabel.font = [UIFont systemFontOfSize:12];
    lb_stateLabel.text = self.remark;
    lb_stateLabel.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_stateLabel.backgroundColor = [UIColor clearColor];
    lb_stateLabel.numberOfLines = 0;
    [iv_topBack addSubview:lb_stateLabel];
    
    NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:12.f]};
    CGSize contentSize=[self.remark boundingRectWithSize:CGSizeMake(lb_stateLabel.frame.size.width, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
    if(contentSize.height > 20){
        
        lb_stateLabel.frame = CGRectMake(10, reLabel.frame.origin.y+reLabel.frame.size.height+5, APP_VIEW_WIDTH - 50-80, contentSize.height+10);
        iv_topBack.frame = CGRectMake(10, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH-20-60, lb_stateLabel.frame.origin.y+contentSize.height+10);

    }else{
        iv_topBack.frame = CGRectMake(10, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH-20-60, lb_stateLabel.frame.origin.y+20);
    }
    
    
    NSDictionary *model = [CouponTypeModel createCoupon:self.type];
    
    iv_rightBack = [[UIImageView alloc]initWithFrame:CGRectMake(iv_topBack.frame.origin.x+iv_topBack.frame.size.width, iv_topBack.frame.origin.y, APP_VIEW_WIDTH-20-iv_topBack.frame.size.width, iv_topBack.frame.size.height)];
    iv_rightBack.userInteractionEnabled = YES;
    
    NSString *bgColor = [model objectForKey:@"backColor"];
    NSArray *backColors = [bgColor componentsSeparatedByString:@","];
    iv_rightBack.backgroundColor = UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
    [self.view addSubview:iv_rightBack];
   
//    
    self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40, 40)];
    self.typeImage.backgroundColor = [UIColor clearColor];
    NSString *imageStr = [model objectForKey:@"image"];
    [self.typeImage setImage:[UIImage imageNamed:imageStr]];
    self.typeImage.center = CGPointMake(iv_rightBack.frame.size.width/2, iv_rightBack.frame.size.height/2);
    [iv_rightBack addSubview:self.typeImage];
    
    if ([self.insteadPrice floatValue]>0) {
        lb_price = [[UILabel alloc]initWithFrame:CGRectMake(0, 15, iv_rightBack.frame.size.width, 35)];
        [lb_price setTextColor:[UIColor whiteColor]];
        lb_price.textAlignment = NSTextAlignmentCenter;
        lb_price.backgroundColor = [UIColor clearColor];
        //        lb_price.text = self.disCount;
        lb_price.text = [NSString stringWithFormat:@"￥%@",self.insteadPrice];
        
        [lb_price setFont:[UIFont systemFontOfSize:15]];
        [iv_rightBack addSubview:lb_price];
    }
 //
    lb_status = [[UILabel alloc]initWithFrame:CGRectMake(0, 60, iv_rightBack.frame.size.width, 20)];
    [lb_status setTextColor:[UIColor whiteColor]];
    lb_status.textAlignment = NSTextAlignmentCenter;
    [lb_status setFont:[UIFont boldSystemFontOfSize:14.f]];
    lb_status.text = [model objectForKey:@"status"];
    [iv_rightBack addSubview:lb_status];
    
    UIView *bottomVi = [[UIView alloc]initWithFrame:CGRectMake(0, iv_rightBack.frame.size.height-20, iv_rightBack.frame.size.width, 20)];
    
    bgColor = [model objectForKey:@"bottomColor"];
    backColors = [bgColor componentsSeparatedByString:@","];
    bottomVi.backgroundColor = UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
    [iv_rightBack addSubview:bottomVi];
    
    
    
    UIButton *submitButton = [[UIButton alloc]initWithFrame:CGRectMake(10, iv_topBack.frame.origin.y+iv_topBack.frame.size.height+20, APP_VIEW_WIDTH-20, 40)];
    submitButton.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [submitButton setTitle:@"提交" forState:UIControlStateNormal];
    [submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    submitButton.titleLabel.font = [UIFont boldSystemFontOfSize:17];
    [submitButton addTarget:self action:@selector(clclikSubmit) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:submitButton];
    
    
}
-(void)clclikSubmit{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *timestamp_str = [outputFormatter stringFromDate:[NSDate date]];
    
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];//商店编码
    [params setObject:[NSString stringWithFormat:@"%d",self.type] forKey:@"couponType"]; //优惠券类型
    [params setObject:self.totalVolume forKey:@"totalVolume"]; // 共发多少张
    
    [params setObject:self.startUsingTime forKey:@"startUsingTime"]; //开始使用日期
    [params setObject:self.expireTime forKey:@"expireTime"];         //结束使用日期
    
    [params setObject:self.dayStartUsingTime forKey:@"dayStartUsingTime"];  //每天开始使用时间
    [params setObject:self.dayEndUsingTime forKey:@"dayEndUsingTime"];      //每天结束时间
    
    
    [params setObject:self.startTakingTime forKey:@"startTakingTime"];    //开始领用日期
    [params setObject:self.endTakingTime forKey:@"endTakingTime"]; //截止领用日期
    
    [params setObject:self.isSend forKey:@"isSend"]; //是否满就送
    [params setObject:[self ggNumberWithString:self.sendRequired ] forKey:@"sendRequired"]; //满就送的金额
    
    
    
    [params setObject:self.remark forKey:@"remark"]; //优惠券说明
    
    [params setObject:self.creatorCode forKey:@"creatorCode"];  //用户编码
    
    [params setObject:[self ggNumberWithString:self.discountPercent] forKey:@"discountPercent"];//除折扣券外，其他类型优惠券传0
    
    [params setObject:[self ggNumberWithString:self.insteadPrice] forKey:@"insteadPrice"];   //每张减免多少或者每张面值多少元
    [params setObject:[self ggNumberWithString:self.availablePrice] forKey:@"availablePrice"]; //除抵扣券外其他类型优惠券传0
    [params setObject:self.function forKey:@"function"]; //折扣券、抵扣券、N元购传空字符串
 
    [params setObject:self.limitedNbr.length>0?self.limitedNbr:@"1" forKey:@"limitedNbr"]; //限使用多少张
    [params setObject:self.nbrPerPerson.length>0?self.nbrPerPerson:@"1" forKey:@"nbrPerPerson"]; //没人限领用张数
    [params setObject:self.limitedSendNbr.length>0?self.limitedSendNbr:@"1" forKey:@"limitedSendNbr"]; //满就送 送多少张
    

    if (self.type == 8) {
        [params setObject:self.payPrice forKey:@"payPrice"];
    }else {
        [params setObject:@"" forKey:@"payPrice"]; //得到一张优惠券需要多少钱
        
    }
    
    
    
    NSString* vcode = [gloabFunction getSign:@"addBatchCoupon" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"addBatchCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        if ([[responseObject objectForKey:@"code"]intValue]==50000) {
            [[NSNotificationCenter defaultCenter]postNotificationName:@"refrshCoupon" object:nil];
            NSArray *vcS = self.navigationController.viewControllers;
            [weakSelf.navigationController popToViewController:[vcS objectAtIndex:vcS.count-4] animated:YES];
            
        }else{
            UIAlertView *alter = [[UIAlertView alloc]initWithTitle:nil message:[weakSelf errorMessage:[[responseObject objectForKey:@"code"]intValue]] delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alter show];
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    

}
-(NSString *)errorMessage:(int)code{

    switch (code) {
        case 80229:
            return @"每人可领用数量不能大于优惠券发行总量";
            break;
        case 80228:
            return @"请输入优惠券归属";
            break;
        case 80217:
            return @"是否消费后才可以领取";
            break;
        case 80215:
            return @"每人可领用数量不正确";
            break;
        case 20000:
            return @"失败，请重试";
            break;
        case 80200:
            return @"优惠券名字不正确";
            break;
        case 80201:
            return @"优惠券类型不正确";
            break;
        case 80218:
            return @"优惠券开始使用时间不正确";
            break;
        case 80202:
            return @"创建者编码不正确";
            break;
        case 50314:
            return @"商店编码不正确";
            break;
        case 80204:
            return @"批次号不正确";
            break;
        case 80205:
            return @"总发行量不正确";
            break;
        case 80206:
            return @"备注不正确";
            break;
        case 80207:
            return @"优惠券失效时间不正确";
            break;
        case 80208:
            return @"券样不正确";
            break;
        case 80209:
            return @"最后可领用日期不正确";
            break;
        case 80210:
            return @"打折数额不正确";
            break;
        case 80211:
            return @"所属行业不正确";
            break;
        case 80212:
            return @"抵用金额不正确";
            break;
        case 80213:
            return @"达到多少金额可用不正确";
            break;
        case 80214:
            return @"可用上限不正确";
            break;

        default:
        {
            NSString *str = [NSString stringWithFormat:@"添加优惠券失败,失败原因[%d]",code];
            return str;
        }
            break;
    }
    
    
    
}


- (id)ggNumberWithString:(NSString *)string {
    

    id result;
    NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
    result=[f numberFromString:string];
    if(!(result))
    {
        result=string;
    }
    
    return result;
}


@end
