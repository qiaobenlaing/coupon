//
//  BMSQ_VoucherVerifyViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_VoucherVerifyViewController.h"
#import "VerificationViewCell.h"
#import "ActivityTitleViewCell.h"
#import "StatusViewCell.h"
#import "SVProgressHUD.h"

@interface BMSQ_VoucherVerifyViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSDictionary * dataSource;

@end

@implementation BMSQ_VoucherVerifyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"兑换券/代金券验证"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    [self getCouponInfoByCode];
    

    
}

- (void)rockBottom
{
    UIView * footerView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 255, APP_VIEW_WIDTH, 150)];
    footerView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:footerView];
    
    UILabel * userDateLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 20, 15)];
    userDateLB.font = [UIFont systemFontOfSize:14];
    NSString * startYearStr = [self.dataSource[@"startUsingTime"] substringWithRange:NSMakeRange(0, 4)];
    NSString * startMontStr = [self.dataSource[@"startUsingTime"] substringWithRange:NSMakeRange(5, 2)];
    NSString * startDayStr = [self.dataSource[@"startUsingTime"] substringWithRange:NSMakeRange(8, 2)];
    NSString * endYearStr = [ self.dataSource[@"expireTime"] substringWithRange:NSMakeRange(0, 4)];
    NSString * endMontStr = [self.dataSource[@"expireTime"] substringWithRange:NSMakeRange(5, 2)];
    NSString * endDayStr = [self.dataSource[@"expireTime"] substringWithRange:NSMakeRange(8, 2)];
    userDateLB.text = [NSString stringWithFormat:@"使用日期:%@/%@/%@~%@/%@/%@",startYearStr, startMontStr,startDayStr, endYearStr, endMontStr, endDayStr];
//    userDateLB.text = [NSString stringWithFormat:@"使用日期:%@-%@",self.dataSource[@"startUsingTime"], self.dataSource[@"expireTime"]];
    [footerView addSubview:userDateLB];
    
    UILabel * userTimeLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 35, APP_VIEW_WIDTH - 20, 15)];
    userTimeLB.font = [UIFont systemFontOfSize:14];
    userTimeLB.text = [NSString stringWithFormat:@"每天使用时间:%@-%@",self.dataSource[@"dayStartUsingTime"], self.dataSource[@"dayEndUsingTime"]];
    [footerView addSubview:userTimeLB];
    
    UILabel * userExplainLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 60, 70, 15)];
    userExplainLB.font = [UIFont systemFontOfSize:14];
    userExplainLB.text = @"使用说明:";
    [footerView addSubview:userExplainLB];
    
    UILabel * listExplainLB = [[UILabel alloc] initWithFrame:CGRectMake(25, 85, APP_VIEW_WIDTH - 25, 35)];
    listExplainLB.font = [UIFont systemFontOfSize:14];
    listExplainLB.textColor = UICOLOR(86, 86, 86, 1.0);
    listExplainLB.numberOfLines = 0;
    if ([self.dataSource[@"remark"] isEqualToString:@""]) {
        
        listExplainLB.text = @"暂无说明";
    }else{
        
        listExplainLB.text = [NSString stringWithFormat:@"%@", self.dataSource[@"remark"]];
    }
    [footerView addSubview:listExplainLB];
    
    
}


- (void)setViewUp
{
    
    UIButton * butVerify = [UIButton buttonWithType:UIButtonTypeCustom];
    butVerify.frame = CGRectMake(30, APP_VIEW_ORIGIN_Y + 190, APP_VIEW_WIDTH - 60, 40);
    
    NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
    if (num.intValue == 20) {
        
        butVerify.backgroundColor = UICOLOR(196, 37, 21, 1.0);
        [butVerify addTarget:self action:@selector(butVerifyClick) forControlEvents:UIControlEventTouchUpInside];
        
    }else{
        
         butVerify.backgroundColor = UICOLOR(169, 171, 172, 1.0);
    }
    
        [butVerify setTitle:@"验证" forState:UIControlStateNormal];
        [butVerify setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    
    
    
    [self.view addSubview:butVerify];
    
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 180) style:UITableViewStyleGrouped];
    _baseView.rowHeight = 40;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    _baseView.scrollEnabled = NO;
    [self.view addSubview:self.baseView];
    
    
    
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        static NSString * cell_id = @"ActivityTitleViewCell";
        ActivityTitleViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[ActivityTitleViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;

        cell.titleLB.text = self.dataSource[@"function"];

        
        
        return cell;

    }else if (indexPath.section == 1){
        
        static NSString * cell_id = @"VerificationViewCell";
        VerificationViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[VerificationViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        cell.verifyCodeLB.text = [NSString stringWithFormat:@"验证码: %@", self.dataSource[@"couponCode"]];
        
        return cell;

        
    }else{
        
        static NSString * cell_id = @"StatusViewCell";
        StatusViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[StatusViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;

        int num = [(NSNumber *)self.dataSource[@"status"] intValue];
        if (num == 10) {
            
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"订单未付款"];
        }else if (num == 11){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"已退款"];
        }else if (num == 12){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"申请退款"];
        }else if (num == 20){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"可用"];
        }else if (num == 30){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@",@"已使用"];
        }
        
        return cell;


    }
}



- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 13;
}

#pragma mark ------ getCouponInfoByCode
// 00101883815   00101299753
- (void)getCouponInfoByCode
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getCouponInfoByCode" strParams:self.couponCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [params setObject:self.couponCode forKey:@"couponCode"];// 优惠券验证码
    
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getCouponInfoByCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);

        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            
            self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
            NSString * isExpire = self.dataSource[@"isExpire"];
            if ([isExpire isEqualToString:@"1"]) {
                
                [self setViewUp];
                [self rockBottom];
                
            }else{
                
                CSAlert(@"该券已过期");
            }

        }else{
            
            CSAlert(@"你输入的验证码有误");
        }
        
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"验证错误,请重新输入验证码");
        
        
    }];
    

    
    
}
#pragma mark -----  验证优惠券，用于兑换券，代金券的使用
- (void)useCoupon
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"useCoupon" strParams:self.dataSource[@"userCode"]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:self.dataSource[@"userCode"] forKey:@"userCode"];
    [params setObject:self.dataSource[@"userCouponCode"] forKey:@"userCouponCode"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"useCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
//        NSNumber * code = responseObject[@"code"];
        NSString * code = [NSString stringWithFormat:@"%@", responseObject[@"code"]];
        
        if (code.intValue == 50000) {
            
            CSAlert(@"验证成功");
            
            [self getCouponInfoByCode];
//            [self setViewUp];
            
        }else if (code.intValue == 20000){
            
            CSAlert(@"验证失败");
            
        }else if (code.intValue == 51002){
            
             CSAlert(@"优惠券不可用");
            
        }else if (code.intValue == 80220){
            
             CSAlert(@"优惠券已过期");
            
        }

        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"验证失败");
        
        
    }];

    
    
}





#pragma mark --- butVerifyClick
- (void)butVerifyClick
{
//    NSLog(@"验证");
    
    [self useCoupon];
}

#pragma mark ---- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
