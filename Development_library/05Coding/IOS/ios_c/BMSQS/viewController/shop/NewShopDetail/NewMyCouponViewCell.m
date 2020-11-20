//
//  NewMyCouponViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/25.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "NewMyCouponViewCell.h"

@interface NewMyCouponViewCell ()

{
    NSString * startUserTime;
    NSString * endUserTime;
    NSString * starUserDay;
    NSString * endUserDay;
    
    
}

@property (nonatomic, strong)UIImageView *typeImage;

@property (nonatomic, strong)UIButton *clickselfButton;

@property (nonatomic, strong)UIImageView * bottomView;
@property (nonatomic, strong)UILabel * couponType;//显示优惠券类型
@property (nonatomic, strong)UILabel * useRangeLb;// 显示使用范围
@property (nonatomic, strong)UIButtonEx * immediatelyBut;//立即
@property (nonatomic, strong)UILabel * surplusLB;// 剩余
@property (nonatomic, strong)UILabel *proBackLable;// 剩余进度
@property (nonatomic, strong)UILabel * titBackLable;// 进度上的字
@property (nonatomic, strong)UILabel * userNoticeLB;//使用须知
@property (nonatomic, strong)UILabel * userRegularLB;//使用规则
@property (nonatomic, strong)UILabel * anyTimeRetreat;// 随时退
@property (nonatomic, strong)UILabel * outDateRetreat;//过期退
@property (nonatomic, strong)UILabel * noAppointment; // 免预约
@property (nonatomic, strong)UIImageView * hookImage1;
@property (nonatomic, strong)UIImageView * hookImage2;
@property (nonatomic, strong)UIImageView * hookImage3;
@property (nonatomic, strong)UIImageView * alreadyImage;// 已领取
@property (nonatomic, strong)UILabel * priceLB;
@property (nonatomic, strong)UILabel * couponLB;


@end

@implementation NewMyCouponViewCell

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
    

    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 80)];
    iv_topBack.image = [UIImage imageNamed:@"粉底"];
    iv_topBack.userInteractionEnabled = YES; // 打开交互性
    [self addSubview:iv_topBack];

    self.couponType = [[UILabel alloc] initWithFrame:CGRectMake(15, 15, 50, 50)];
    _couponType.backgroundColor = [UIColor whiteColor];
    _couponType.textAlignment = NSTextAlignmentCenter;
//    _couponType.text = @"折扣券";
    _couponType.textColor = UICOLOR(255, 98, 122, 1.0);
    _couponType.font = [UIFont systemFontOfSize:13.0];
    _couponType.layer.cornerRadius = 25;
    _couponType.clipsToBounds = YES;
    _couponType.layer.borderColor = [UICOLOR(255, 98, 122, 1.0) CGColor];
    _couponType.layer.borderWidth = 0.5f;
    _couponType.layer.masksToBounds = YES;
    [iv_topBack addSubview:self.couponType];
    
    self.priceLB = [[UILabel alloc] initWithFrame:CGRectMake(65, 20, 70, 40)];
    _priceLB.textColor = UICOLOR(254, 254, 254, 1.0);
    _priceLB.backgroundColor = [UIColor clearColor];
    _priceLB.font = [UIFont systemFontOfSize:22.0];
    _priceLB.numberOfLines = 0;
    [iv_topBack addSubview:self.priceLB];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(135,10, iv_topBack.frame.size.width - 135, 30)];
//    lb_useDes.text = @"满32.2元打8.8折";
    lb_useDes.textColor = UICOLOR(254, 254, 254, 1.0);
    lb_useDes.backgroundColor = [UIColor clearColor];
    lb_useDes.font = [UIFont systemFontOfSize:17.0];
    [iv_topBack addSubview:lb_useDes]; // 优惠券的
    
    self.couponLB = [[UILabel alloc] initWithFrame:CGRectMake(140, 45, 70, 35)];
    _couponLB.textColor = UICOLOR(254, 254, 254, 1.0);
    _couponLB.backgroundColor = [UIColor clearColor];
    _couponLB.font = [UIFont systemFontOfSize:22.0];
    _couponLB.text = @"优惠券";
    [iv_topBack addSubview:self.couponLB];

    lb_price = [[UILabel alloc]initWithFrame:CGRectMake(_couponLB.frame.origin.x + 70, 45, 60, 35)];
    [lb_price setTextColor:[UIColor whiteColor]];
    lb_price.backgroundColor = [UIColor clearColor];
    lb_price.font = [UIFont systemFontOfSize:22.0];
    [iv_topBack addSubview:lb_price];
    
    
    btn_share = [[UIButton alloc]initWithFrame:CGRectMake(iv_topBack.frame.size.width - 40, 30, 40, 40)];
    [btn_share setImage:[UIImage imageNamed:@"icon_share"] forState:UIControlStateNormal];
    btn_share.backgroundColor = [UIColor clearColor];
    [btn_share addTarget:self action:@selector(btnShareClick:) forControlEvents:UIControlEventTouchUpInside];
    [iv_topBack addSubview:btn_share]; // 分享
    
    self.bottomView = [[UIImageView alloc] initWithFrame:CGRectMake(10, iv_topBack.frame.origin.y + 75, APP_VIEW_WIDTH-20, 160)];
    _bottomView.image = [UIImage imageNamed:@"白底"];
    _bottomView.userInteractionEnabled = YES;
    [self addSubview:self.bottomView];
    
    self.useRangeLb = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH - 50, 15)];
    _useRangeLb.font = [UIFont systemFontOfSize:13.0];
    _useRangeLb.text = @"使用范围: 全场通用    单笔金额不限制";
    _useRangeLb.textColor = UICOLOR(135, 135, 137, 1.0);
    [self.bottomView addSubview:self.useRangeLb];
    
    lb_endTime = [[UILabel alloc]initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, 30, _bottomView.frame.size.width - 100, 15)];
//    lb_endTime.text = @"过期时间:2016.02.26";
    lb_endTime.font = [UIFont systemFontOfSize:13.0];
    lb_endTime.backgroundColor = [UIColor clearColor];
    lb_endTime.textColor = UICOLOR(135, 135, 137, 1.0);
    [self.bottomView addSubview:lb_endTime]; // 使用日期
    
    self.hookImage1 = [[UIImageView alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, 50, 10, 10)];
    _hookImage1.image = [UIImage imageNamed:@"粉底对号"];
    _hookImage1.hidden = YES;
    [self.bottomView addSubview:self.hookImage1];
    
    self.anyTimeRetreat = [[UILabel alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x + 12, 50, 30, 10)];
    _anyTimeRetreat.font = [UIFont systemFontOfSize:10.0];
    _anyTimeRetreat.text = @"随时退";
    _anyTimeRetreat.hidden = YES;
    _anyTimeRetreat.textColor = UICOLOR(255, 98, 122, 1.0);
    [self.bottomView addSubview:self.anyTimeRetreat];
    
    self.hookImage2 = [[UIImageView alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x + 47, 50, 10, 10)];
    _hookImage2.hidden = YES;
    _hookImage2.image = [UIImage imageNamed:@"粉底对号"];
    [self.bottomView addSubview:self.hookImage2];
    
    self.outDateRetreat = [[UILabel alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x + 59, 50, 30, 10)];
    _outDateRetreat.font = [UIFont systemFontOfSize:10.0];
    _outDateRetreat.textColor = UICOLOR(255, 98, 122, 1.0);
    _outDateRetreat.text = @"过期退";
    _outDateRetreat.hidden = YES;
    [self.bottomView addSubview:self.outDateRetreat];
    
    self.hookImage3 = [[UIImageView alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x + 94, 50, 10, 10)];
    _hookImage3.image = [UIImage imageNamed:@"粉底对号"];
    _hookImage3.hidden = YES;
    [self.bottomView addSubview:self.hookImage3];
    
    self.noAppointment = [[UILabel alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x + 106, 50, 30, 10)];
    _noAppointment.font = [UIFont systemFontOfSize:10.0];
    _noAppointment.textColor = UICOLOR(255, 98, 122, 1.0);
    _noAppointment.text = @"免预约";
    _noAppointment.hidden = YES;
    [self.bottomView addSubview:self.noAppointment];
    
    self.immediatelyBut = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    _immediatelyBut.frame = CGRectMake(_bottomView.frame.size.width - 100, 30, 90, 35);
    [_immediatelyBut setImage:[UIImage imageNamed:@"立即领取"] forState:UIControlStateNormal];
    [_immediatelyBut addTarget:self action:@selector(click_rightView:) forControlEvents:UIControlEventTouchUpInside];
    _immediatelyBut.backgroundColor = [UIColor clearColor];
    [self.bottomView addSubview:self.immediatelyBut]; // 立即
    
    self.surplusLB = [[UILabel alloc] initWithFrame:CGRectMake(_bottomView.frame.size.width - 105, 70, 100, 15)];
//    _surplusLB.textAlignment = NSTextAlignmentCenter;
//    _surplusLB.font = [UIFont systemFontOfSize:10.0];
    _surplusLB.layer.cornerRadius = 9;
    _surplusLB.clipsToBounds = YES;
    _surplusLB.layer.borderColor = [UICOLOR(136, 213, 254, 1.0) CGColor];
    _surplusLB.layer.borderWidth = 0.5f;
    _surplusLB.layer.masksToBounds = YES;
    [self.bottomView addSubview:self.surplusLB]; // 剩余
    
    self.proBackLable = [[UILabel alloc]initWithFrame:CGRectMake(5, 2, 2, 11)];
    _proBackLable.backgroundColor = UICOLOR(162, 222, 254, 1.0);
    _proBackLable.layer.masksToBounds = YES;
    _proBackLable.layer.cornerRadius = 6;
    [self.surplusLB addSubview:self.proBackLable];
    
    self.titBackLable = [[UILabel alloc] initWithFrame:self.surplusLB.bounds];
    _titBackLable.textAlignment = NSTextAlignmentCenter;
    _titBackLable.font = [UIFont systemFontOfSize:10.0];
    [self.surplusLB addSubview:self.titBackLable];
    
    self.userNoticeLB = [[UILabel alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, 70, 100, 10)];
    _userNoticeLB.text = @"使用须知:";
    _userNoticeLB.font = [UIFont systemFontOfSize:10.0];
    _userNoticeLB.textColor = UICOLOR(145, 146, 147, 1.0);
    [self.bottomView addSubview:self.userNoticeLB];// 使用须知
    
    lb_couponTime = [[UILabel alloc]initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, 85, _bottomView.frame.size.width - 100, 10)];
    lb_couponTime.textColor = UICOLOR(145, 146, 147, 1.0);
//    lb_couponTime.text = @"使用时间:00:00:00 - 00:00:00";
    lb_couponTime.font = [UIFont systemFontOfSize:10.0];
    [self.bottomView addSubview:lb_couponTime];// 使用时间
    
    self.userRegularLB = [[UILabel alloc] initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, 100, 100, 10)];
    _userRegularLB.text = @"使用规则:";
    _userRegularLB.font = [UIFont systemFontOfSize:10.0];
    _userRegularLB.textColor = UICOLOR(145, 146, 147, 1.0);
    [self.bottomView addSubview:self.userRegularLB];// 使用规则
    
    lb_stateLabel = [[UILabel alloc]initWithFrame:CGRectMake(_useRangeLb.frame.origin.x, _userRegularLB.frame.origin.y + 15, _bottomView.frame.size.width - 70, 30)];
    lb_stateLabel.font = [UIFont systemFontOfSize:10];
    lb_stateLabel.textColor = UICOLOR(145, 146, 147, 1.0);
    lb_stateLabel.numberOfLines = 0;
    lb_stateLabel.backgroundColor = [UIColor clearColor]; // 使用规则内容
    lb_stateLabel.tag = 100;
    [self.bottomView addSubview:lb_stateLabel];
    
    self.alreadyImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.bottomView.frame.size.width - 50, self.bottomView.frame.size.height - 50, 50, 50)];
    [self.bottomView addSubview:self.alreadyImage];
    
    
    
    
    
    
}
#pragma mark -----setCellCouponDic:(NSDictionary *)couponDic row:(int)row
- (void)setCellCouponDic:(NSDictionary *)couponDic row:(int)row num:(int)num
{ // num:判断是商家优惠券还是已领取的 num: 1:已领取  2:商家优惠券
    if(couponDic == nil || couponDic.count <= 0)
    {
        return;
    }
    
    self.couponDic = couponDic;
    self.currentRow = row;
    self.type =[couponDic objectForKey:@"couponType"];
    if (self.type.intValue == 1) {
        _couponType.text = @"N元购";
    }else if (self.type.intValue == 3 || self.type.intValue == 32 || self.type.intValue == 33){
        _couponType.text = @"抵扣券";
    }else if (self.type.intValue == 4){
        _couponType.text = @"折扣券";
    }else if (self.type.intValue == 5){
        _couponType.text = @"实物券";
    }else if (self.type.intValue == 6){
        _couponType.text = @"体验券";
    }else if (self.type.intValue == 7){
        _couponType.text = @"兑换券";
        
    }else if (self.type.intValue == 8){
        _couponType.text = @"代金券";
        
    }
    
    
    if (self.type.intValue == 7 || self.type.intValue == 8){
        _hookImage1.hidden = NO;
        _hookImage2.hidden = NO;
        _hookImage3.hidden = NO;
        _anyTimeRetreat.hidden = NO;
        _outDateRetreat.hidden = NO;
        _noAppointment.hidden = NO;
    }
    
    
    //说明
    if ([couponDic objectForKey:@"remark"] == NULL || [[couponDic objectForKey:@"remark"]  isEqual:@""]) {
        
        lb_stateLabel.text = @"    暂无说明";
        self.bottomView.frame = CGRectMake(10, iv_topBack.frame.origin.y + 75, APP_VIEW_WIDTH-20, 160);
        lb_stateLabel.frame = CGRectMake(_useRangeLb.frame.origin.x, _userRegularLB.frame.origin.y + 15, _bottomView.frame.size.width - 70, 30);
        self.alreadyImage.frame = CGRectMake(self.bottomView.frame.size.width - 50, self.bottomView.frame.size.height - 50, 50, 50);
    }
    else{
        lb_stateLabel.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"remark"]];
        CGSize size = [lb_stateLabel.text boundingRectWithSize:CGSizeMake(lb_stateLabel.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: lb_stateLabel.font} context:nil].size;

        self.bottomView.frame = CGRectMake(10, iv_topBack.frame.origin.y + 75, APP_VIEW_WIDTH-20, size.height + 130);
        lb_stateLabel.frame = CGRectMake(_useRangeLb.frame.origin.x, _userRegularLB.frame.origin.y + 15, _bottomView.frame.size.width - 70, size.height);
        self.alreadyImage.frame = CGRectMake(self.bottomView.frame.size.width - 50, self.bottomView.frame.size.height - 50, 50, 50);
    }

    if (num == 1) {
        lb_price.text = [NSString stringWithFormat:@"X%@", couponDic[@"userCount"]];
        [_immediatelyBut setImage:[UIImage imageNamed:@"立即使用"] forState:UIControlStateNormal];
        _immediatelyBut.num = 1;
        
        _alreadyImage.image = [UIImage imageNamed:@"已领取-印章"];
        self.surplusLB.hidden = YES;
        
    }else if (num == 2){
        
        if (self.type.intValue == 7 || self.type.intValue == 8) {
            [_immediatelyBut setImage:[UIImage imageNamed:@"立即抢购"] forState:UIControlStateNormal];
        }else{
            [_immediatelyBut setImage:[UIImage imageNamed:@"立即领取"] forState:UIControlStateNormal];
        }
        _immediatelyBut.num = 2;

        _alreadyImage.image = [UIImage imageNamed:@""];
        lb_price.text = @"";
        self.surplusLB.hidden = NO;
        CGFloat w;
        w = [[couponDic objectForKey:@"remaining"]floatValue]/[[couponDic objectForKey:@"totalVolume"]floatValue]*(_surplusLB.frame.size.width-10);
        self.proBackLable.frame = CGRectMake(_surplusLB.frame.size.width-w, 0, w, 15);
        self.titBackLable.text = [NSString stringWithFormat:@"剩余%@张", couponDic[@"remaining"]];
        
        NSString * VolumeStr = [NSString stringWithFormat:@"%@", couponDic[@"totalVolume"]];
        if (VolumeStr.intValue == -1) {
            self.proBackLable.frame = CGRectMake(_surplusLB.frame.size.width, 0, w, 15);
            self.proBackLable.hidden = YES;
            self.titBackLable.text = @"无限张数";
        }
        
        
    }

    
    
    
   // 使用时间
    NSString *dayStartUsingTime = [couponDic objectForKey:@"dayStartUsingTime"];
    NSString *dayEndUsingTime = [couponDic objectForKey:@"dayEndUsingTime"];
    startUserTime = dayStartUsingTime;
    endUserTime = dayEndUsingTime;
    if (dayStartUsingTime.length==0 && dayEndUsingTime.length ==0) {
        lb_couponTime.text = @"使用时间: 全天使用";
    }else{
        lb_couponTime.text = [NSString stringWithFormat:@"使用时间:%@ - %@",[couponDic objectForKey:@"dayStartUsingTime"],[couponDic objectForKey:@"dayEndUsingTime"]];
    }

    
    
    NSString *startTimeString =  [couponDic objectForKey:@"startUsingTime"] ;
    NSString *expireTimeString = [couponDic objectForKey:@"expireTime"] ;
    
    starUserDay = startTimeString;
    endUserDay = expireTimeString;
    
    if (startTimeString.length==0 && expireTimeString.length ==0) {
        lb_endTime.text = @"不限使用";
    }else{
        lb_endTime.text = [NSString stringWithFormat:@"过期日期:%@",expireTimeString];
    }

    

    
    if (self.type.intValue == 1)
    {
        
        NSString *insteadPrice = [NSString stringWithFormat:@"￥%.1f",[[couponDic objectForKey:@"insteadPrice"] floatValue]];

        lb_useDes.text = [NSString stringWithFormat:@"此券可兑%@商品",insteadPrice];

        self.priceLB.text = [NSString stringWithFormat:@"￥%.1f", [couponDic[@"insteadPrice"] floatValue]];
    }
    else if (self.type.intValue == 3)
    {

        lb_useDes.text = [NSString stringWithFormat:@"满%.1f元立减%.1f元",[[couponDic objectForKey:@"availablePrice"] floatValue],[[couponDic objectForKey:@"insteadPrice"] floatValue]];
        self.priceLB.text = [NSString stringWithFormat:@"￥%.1f", [couponDic[@"insteadPrice"] floatValue]];
        
    }
    else if (self.type.intValue == 4)
    {

        lb_useDes.text = [NSString stringWithFormat:@"满%.1f元打%0.1f折",[[couponDic objectForKey:@"availablePrice"] floatValue],[[couponDic objectForKey:@"discountPercent"]floatValue]];
        
        self.priceLB.text = [NSString stringWithFormat:@"%.1f折", [couponDic[@"discountPercent"] floatValue]];
        
        
    }else if (self.type.intValue == 7){
        
        lb_useDes.text = [NSString stringWithFormat:@"此券可以兑换:%@",[couponDic objectForKey:@"function"]];
        self.priceLB.text = [NSString stringWithFormat:@"￥%.1f", [couponDic[@"payPrice"] floatValue]];
        
    }else if (self.type.intValue == 8){
        
        lb_useDes.text = [NSString stringWithFormat:@"此券价值￥%.1f",[couponDic[@"insteadPrice"] floatValue]];
        self.priceLB.text = [NSString stringWithFormat:@"￥%.1f", [couponDic[@"payPrice"] floatValue]];
        
    }
    else if (self.type.intValue == 5 || self.type.intValue == 6){
        lb_useDes.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];
//        self.priceLB.text = [NSString stringWithFormat:@"￥%@", couponDic[@"payPrice"]];
    }else{
        lb_useDes.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];
    }

    
    
    
    
}




#pragma mark ------ 优惠券分享
- (void)btnShareClick:(UIButton *)sender
{
    if ([self.coupondelegate respondsToSelector:@selector(btnNewShareClick:)]) {
        [self.coupondelegate btnNewShareClick:self.couponDic];

    }
}
#pragma mark ----- 抢购或领取优惠券
-(void)click_rightView:(UIButtonEx *)sender{
    
    if (sender.num == 1) {
        
        [self userCouponAction];
        
    }else{
        
        NSString *couponType =[NSString stringWithFormat:@"%@",[self.couponDic objectForKey:@"couponType"]];
        if ([couponType intValue]==7 ||[couponType intValue]==8) {
            if ([self.coupondelegate respondsToSelector:@selector(grabBuyNewCoupon:)]){
                [self.coupondelegate grabBuyNewCoupon:self.couponDic];
            }
            
            
        }else{
            if ([self.coupondelegate respondsToSelector:@selector(grabNewCupon:currenRow:)]){
                [self.coupondelegate grabNewCupon:self.couponDic currenRow:self.currentRow];
            }
            
            
        }
        
    }
    
    
    
}
#pragma mark ----- 使用优惠券
- (void)userCouponAction
{
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    NSDate *now;
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    NSInteger unitFlags = NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit | NSWeekdayCalendarUnit |
    NSHourCalendarUnit | NSMinuteCalendarUnit | NSSecondCalendarUnit;
    now=[NSDate date];
    comps = [calendar components:unitFlags fromDate:now];
    int month = (int)[comps month];
    int day = (int)[comps day];
    int hour = (int)[comps hour];
    int minute = (int)[comps minute];
    if (starUserDay.length != 0) {
       NSString * startMonth = [starUserDay substringWithRange:NSMakeRange(5, 2)];
       NSString * startDay = [starUserDay substringWithRange:NSMakeRange(8, 2)];
        NSString * startHour = [startUserTime substringWithRange:NSMakeRange(0, 2)];
        NSString * startMinute = [startUserTime substringWithRange:NSMakeRange(2, 2)];
        if (startMonth.intValue > month) {
            CSAlert(@"未到使用时间");
        }else if (startMonth.intValue <= month && startDay.intValue > day){
            CSAlert(@"未到使用时间");
        }else{
            
            if (startHour.intValue > hour) {
                CSAlert(@"未到使用时间");
            }else if (startHour.intValue <= hour && startMinute.intValue > minute){
                CSAlert(@"未到使用时间");
            }else{
                
                if ([self.coupondelegate respondsToSelector:@selector(grabUserCoupon:)]){
                    [self.coupondelegate grabUserCoupon:self.couponDic];
                }
                
            }
            
        }
        
    }else{
        NSString * startHour = [startUserTime substringWithRange:NSMakeRange(0, 2)];
        NSString * startMinute = [startUserTime substringWithRange:NSMakeRange(2, 2)];
        if (startHour.intValue > hour) {
            CSAlert(@"未到使用时间");
        }else if (startHour.intValue <= hour && startMinute.intValue > minute){
            CSAlert(@"未到使用时间");
        }else{
            
            if ([self.coupondelegate respondsToSelector:@selector(grabUserCoupon:)]){
                [self.coupondelegate grabUserCoupon:self.couponDic];
            }
            
        }
    }
    
    
    
    
    

    
}





@end
