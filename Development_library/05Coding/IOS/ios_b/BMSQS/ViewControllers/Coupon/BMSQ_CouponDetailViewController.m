//
//  BMSQ_CouponDetailViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponDetailViewController.h"

#import "SVProgressHUD.h"

@interface BMSQ_CouponDetailViewController () <UIAlertViewDelegate>

@property (nonatomic, strong)UIButton *rightbutton;
@property (nonatomic, strong)NSString *isAvailable;
@end

@implementation BMSQ_CouponDetailViewController

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [SVProgressHUD dismiss];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"优惠券详情"];
    
    self.rightbutton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60 ,  20, 40, APP_NAVGATION_HEIGHT)];
    self.rightbutton.backgroundColor = [UIColor clearColor];
    [self.rightbutton setTitle:@"停用" forState:UIControlStateNormal];//0-停用；1-启用
    self.rightbutton.titleLabel.font = [UIFont fontWithName:@"TrebuchetMS-Bold" size:16];
    [self.rightbutton addTarget:self action:@selector(rightButtonClicked) forControlEvents:UIControlEventTouchUpInside];
    [self.rightbutton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self setRight:self.rightbutton];

    [self getCouponInfo];
    
    
    
}
-(void)rightButtonClicked{
    
    if ([self.isAvailable isEqual:@"0" ]) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"你确定要停用优惠券领取吗" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        
        [alertView show];
    }
    else {
        [self changeCouponStatus];
        
    }
    
    
}





- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {

    }else if (buttonIndex == 1){
        [self  changeCouponStatus];
        
    }
    
    
    
}


-(void)setMyView:(NSDictionary *)couponDic{
    
    if ([[couponDic objectForKey:@"isAvailable"]intValue]==0) { //1  ；0-停用
        [self.rightbutton setTitle:@"启用" forState:UIControlStateNormal];
        self.isAvailable = @"1";
    }else{
        [self.rightbutton setTitle:@"停用" forState:UIControlStateNormal];
        self.isAvailable = @"0";

    }
    
    
    UIScrollView *sc = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    sc.backgroundColor = [UIColor clearColor];
    [self.view addSubview:sc];
    
    
    NSArray *status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券", @"兑换券", @"代金券"];
    int i = [[couponDic objectForKey:@"couponType"] intValue];
    NSString *couponName = (i == 1||i ==5||i==6 )?[status objectAtIndex:i]:@"";
    NSString *remark;
    if (i == 3) {
        couponName = [NSString stringWithFormat:@"满%.2f元立减%.2f元",[[couponDic objectForKey:@"availablePrice"] floatValue],[[couponDic objectForKey:@"insteadPrice"] floatValue]];
        remark = [couponDic objectForKey:@"remark"];
    }else if (i == 4){
        couponName = [NSString stringWithFormat:@"满%.2f元打%.1f折",[[couponDic objectForKey:@"availablePrice"] floatValue],[[couponDic objectForKey:@"discountPercent"] floatValue]];
        remark = [couponDic objectForKey:@"remark"];

    }else if (i == 7){
        couponName = [couponDic objectForKey:@"function"];
        remark =[couponDic objectForKey:@"remark"];
        
        
    }else if (i == 8) {
        
        couponName = [couponDic objectForKey:@"function"];
        remark =[couponDic objectForKey:@"remark"];
        
        
    }else {
        
        remark =[couponDic objectForKey:@"function"];

    }
    NSString *endTakingTime = [couponDic objectForKey:@"endTakingTime"];
    if (endTakingTime.length ==0) {
        endTakingTime = @"无限使用";
    }
    
    if (remark.length ==0) {
        remark = @"暂无说明";
    }
    
    NSString *useDate =[couponDic objectForKey:@"startUsingTime"];
    if (useDate.length ==0) {
        useDate = @"无限期使用";
    }else{
        useDate = [NSString stringWithFormat:@"%@ - %@",[couponDic objectForKey:@"startUsingTime"],[couponDic objectForKey:@"expireTime"]];
    }
    NSString *useTime =[couponDic objectForKey:@"dayStartUsingTime"];
    
    if ([useTime isEqualToString:@"00:00"] && [[couponDic objectForKey:@"dayEndUsingTime"] isEqualToString:@"24:00"] ) {
        useTime = @"全天可用";
    }else{
        useTime = [NSString stringWithFormat:@"%@ - %@",[couponDic objectForKey:@"dayStartUsingTime"],[couponDic objectForKey:@"dayEndUsingTime"]];
    }
    
    NSString *totalVolume = [couponDic objectForKey:@"totalVolume"];
    if  ([totalVolume isEqual:@"-1"]) {
        totalVolume = @"发行数量不限张数";
    }else {
        totalVolume = totalVolume;
    }
    
    
    
    
    NSString *isSend = [couponDic objectForKey:@"isSend"];
    NSString *sendStr;
    NSArray *titleS;
    if ([isSend isEqual:@"1"]) {
        sendStr = [NSString stringWithFormat:@"每满%.2f元送一张优惠券",[[couponDic objectForKey:@"sendRequired"] floatValue]];
        
        titleS = @[[NSString stringWithFormat:@"批次 : %@",[couponDic objectForKey:@"batchNbr"]],
                   [NSString stringWithFormat:@"数量 : %@", totalVolume],
                   [NSString stringWithFormat:@"使用日期 : %@",useDate],
                   [NSString stringWithFormat:@"每天使用时间 : %@ ", useTime],
                   [NSString stringWithFormat:@"%@ : %@", status[i], couponName],
                   [NSString stringWithFormat:@"满就送 : %@", sendStr] ];
        
    }else {
        titleS = @[[NSString stringWithFormat:@"批次 : %@",[couponDic objectForKey:@"batchNbr"]],
                   [NSString stringWithFormat:@"数量 : %@", totalVolume],
                   [NSString stringWithFormat:@"使用日期 : %@",useDate],
                   [NSString stringWithFormat:@"每天使用时间 : %@ ", useTime],
                   [NSString stringWithFormat:@"截止领取 : %@",endTakingTime],
                   [NSString stringWithFormat:@"%@ : %@", status[i], couponName]];
        
    }
    
    
    

    
    for (int i =0; i<titleS.count; i++) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(8, (i*35), APP_VIEW_WIDTH, 35)];
        label.text = [titleS objectAtIndex:i];
        label.font = [UIFont systemFontOfSize:14.f];
        [sc addSubview:label];
        
    }
    
    NSString *remarkStr = [NSString stringWithFormat:@"使用说明 : %@",remark];
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(8, (titleS.count*35), APP_VIEW_WIDTH-16, 35)];
    label.text = remarkStr;
    label.numberOfLines = 0;
    label.font = [UIFont systemFontOfSize:14.f];
    [sc addSubview:label];
    
    
    NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:14.f]};
    CGSize contentSize=[remarkStr boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-16, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
    if (contentSize.height >35) {
        label.frame =CGRectMake(8, (titleS.count*35), APP_VIEW_WIDTH-16, contentSize.height);
    }
    
    
    
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, label.frame.origin.y+label.frame.size.height, APP_VIEW_WIDTH, 0.5)];
    lineView.backgroundColor = [UIColor blackColor];
    [sc addSubview:lineView];
    
    
    CGFloat viewY = lineView.frame.origin.y+ 25;
    float w;
    if  ([[couponDic objectForKey:@"totalVolume"] isEqual:@"-1"]) {
        
    }else {
        UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, viewY, 100, 20)];
        titleLabel.text = @"领取张数";
        titleLabel.font = [UIFont systemFontOfSize:14];
        [sc addSubview:titleLabel];
        
        viewY = viewY+titleLabel.frame.size.height+10;
        UILabel *proBackLable1 = [[UILabel alloc]initWithFrame:CGRectMake(10, viewY,APP_VIEW_WIDTH/2, 10)];
        proBackLable1.layer.masksToBounds = YES;
        proBackLable1.layer.cornerRadius = 8;
        proBackLable1.layer.borderWidth = 0.5;
        proBackLable1.layer.borderColor = [[UIColor grayColor]CGColor];
        [sc addSubview:proBackLable1];
        
        w = [[couponDic objectForKey:@"takenCount"]floatValue]/[[couponDic objectForKey:@"totalVolume"]floatValue]*(proBackLable1.frame.size.width-10);
        UILabel *proBackLable11 = [[UILabel alloc]initWithFrame:CGRectMake(5, 2, w, 6)];
        proBackLable11.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        proBackLable11.layer.masksToBounds = YES;
        proBackLable11.layer.cornerRadius = 6;
        [proBackLable1 addSubview:proBackLable11];
        
        UILabel *proLabel = [[UILabel alloc]initWithFrame:CGRectMake(proBackLable1.frame.origin.x+proBackLable1.frame.size.width+10 , viewY , APP_VIEW_WIDTH/2-30,15)];
        proLabel.font = [UIFont systemFontOfSize:12.f];
        proLabel.backgroundColor = [UIColor clearColor];
        proLabel.text = [NSString stringWithFormat:@"已领取(%@/%@)",[couponDic objectForKey:@"takenCount"],[couponDic objectForKey:@"totalVolume"]];
        [sc addSubview:proLabel];
        
        viewY = viewY + 15+20;
        
    }
    
    
    UILabel *titleLabel1 = [[UILabel alloc]initWithFrame:CGRectMake(10, viewY, 100, 20)];
    titleLabel1.text = @"使用张数";
    titleLabel1.font = [UIFont systemFontOfSize:14];
    [sc addSubview:titleLabel1];
    

    viewY = viewY + +titleLabel1.frame.size.height+10;
    UILabel *proBackLable2 = [[UILabel alloc] initWithFrame:CGRectMake(10, viewY, APP_VIEW_WIDTH/2, 10)];
    proBackLable2.layer.masksToBounds = YES;
    proBackLable2.layer.cornerRadius = 8;
    proBackLable2.layer.borderWidth = 0.5;
    proBackLable2.layer.borderColor = [[UIColor grayColor]CGColor];
    [sc addSubview:proBackLable2];
    
    if ([[couponDic objectForKey:@"takenCount"]intValue]==0) {
        w =0;
    }else{
        w = [[couponDic objectForKey:@"usedCount"]floatValue]/[[couponDic objectForKey:@"takenCount"]floatValue]*(proBackLable2.frame.size.width-10);
  
    }
    
    UILabel *proBackLable21 = [[UILabel alloc]initWithFrame:CGRectMake(5,2, w,6)];
    proBackLable21.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    proBackLable21.layer.masksToBounds = YES;
    proBackLable21.layer.cornerRadius = 6;
    [proBackLable2 addSubview:proBackLable21];
    
    UILabel *proLabel1 = [[UILabel alloc]initWithFrame:CGRectMake(proBackLable2.frame.origin.x+proBackLable2.frame.size.width+10 , proBackLable2.frame.origin.y , APP_VIEW_WIDTH/2-30,15)];
    proLabel1.font = [UIFont systemFontOfSize:12.f];
    proLabel1.backgroundColor = [UIColor clearColor];
    proLabel1.text =[NSString stringWithFormat:@"已使用(%@/%@)",[couponDic objectForKey:@"usedCount"],[couponDic objectForKey:@"takenCount"]];
    [sc addSubview:proLabel1];
    
    sc.contentSize = CGSizeMake(APP_VIEW_WIDTH, proLabel1.frame.origin.y+50);
    
}

#pragma mark - http request
-(void)getCouponInfo{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.batchCouponCode forKey:@"batchCouponCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getCouponInfo" strParams:self.batchCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"getCouponInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [weakSelf setMyView:responseObject];
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"err");
        [SVProgressHUD dismiss];
        
    }];
    
}

- (void)changeCouponStatus {
    
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.batchCouponCode forKey:@"batchCouponCode"];
    [params setObject:self.isAvailable forKey:@"isAvailable"];  //0-停用；1-启用
    
    NSString* vcode = [gloabFunction getSign:@"changeCouponStatus" strParams:self.batchCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"changeCouponStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if ([[responseObject objectForKey:@"code"]intValue]==50000) {
            if ([weakSelf.isAvailable isEqualToString:@"0"]) {
                [weakSelf.rightbutton setTitle:@"启用" forState:UIControlStateNormal];
                weakSelf.isAvailable = @"1";
                
                
            }else{
                [weakSelf.rightbutton setTitle:@"停用" forState:UIControlStateNormal];
                weakSelf.isAvailable = @"0";
                
                
            }
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
    
}



@end
