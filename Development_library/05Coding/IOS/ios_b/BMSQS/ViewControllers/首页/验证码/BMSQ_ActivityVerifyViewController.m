//
//  BMSQ_ActivityVerifyViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ActivityVerifyViewController.h"
#import "VerificationViewCell.h"
#import "ActivityTitleViewCell.h"
#import "StatusViewCell.h"
#import "SVProgressHUD.h"

@interface BMSQ_ActivityVerifyViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSDictionary * dataSource;

@end

@implementation BMSQ_ActivityVerifyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"活动验证"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self getInfoByActCode];


}



- (void)rockBottom
{
    UIView * footerView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 255, APP_VIEW_WIDTH, 110)];
    footerView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:footerView];
    
    UILabel * userDateLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 20, 25)];
    userDateLB.font = [UIFont systemFontOfSize:16];
    userDateLB.text = [NSString stringWithFormat:@"使用日期:%@",@"2015/12/2-2016/3/4"];
    [footerView addSubview:userDateLB];
    
    UILabel * addressLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 45, APP_VIEW_WIDTH - 20, 25)];
    addressLB.font = [UIFont systemFontOfSize:16];
    addressLB.text = [NSString stringWithFormat:@"%@",self.dataSource[@"activityLocation"]];
    [footerView addSubview:addressLB];
    
    UILabel * contactsLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 80, APP_VIEW_WIDTH - 20, 25)];
    contactsLB.font = [UIFont systemFontOfSize:16];
    contactsLB.text = [NSString stringWithFormat:@"联系人: %@",self.dataSource[@"name"]];
    [footerView addSubview:contactsLB];
    
    
}


- (void)setViewUp
{
    
    UIButton * butVerify = [UIButton buttonWithType:UIButtonTypeCustom];
    butVerify.frame = CGRectMake(30, APP_VIEW_ORIGIN_Y + 190, APP_VIEW_WIDTH - 60, 40);
    
    NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
    if (num.intValue == 4) {
        
        butVerify.backgroundColor = UICOLOR(166, 16, 21, 1.0);
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
        
        cell.titleLB.text = self.dataSource[@"activityName"];
        
        
        
        return cell;
        
    }else if (indexPath.section == 1){
        
        static NSString * cell_id = @"VerificationViewCell";
        VerificationViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[VerificationViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        cell.verifyCodeLB.text = [NSString stringWithFormat:@"验证码: %@", self.couponCode];
        
        return cell;
        
        
    }else{
        
        static NSString * cell_id = @"StatusViewCell";
        StatusViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[StatusViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
        if (num.intValue == 1) {
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"已申请退款"];
        }else if (num.intValue == 2){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"已退款"];
        }else if (num.intValue == 3){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"已验证"];
        }else if (num.intValue == 4){
            cell.statusLB.text = [NSString stringWithFormat:@"状态: %@", @"未验证"];
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


#pragma mark --- butVerifyClick
- (void)butVerifyClick
{
    [self valUserActCode];
    
    
}

#pragma mark ------ getInfoByActCode (actCode)
// 0001272943        0001752483
- (void)getInfoByActCode
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.couponCode forKey:@"actCode"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getInfoByActCode" strParams:self.couponCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getInfoByActCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            
            self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
            [self setViewUp];
            [self rockBottom];
            
        }else{
            CSAlert(@"你输入的验证码有误，请重新输入");
        }

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark ------- valUserActCode  验证用户活动验证码
- (void)valUserActCode
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.couponCode forKey:@"actCode"];
    [params setObject:self.dataSource[@"userActCodeId"] forKey:@"userActCodeId"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"valUserActCode" strParams:self.couponCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"valUserActCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        NSString * code = [NSString stringWithFormat:@"%@", responseObject[@"code"]];
        if (code.intValue == 50000) {
            CSAlert(@"验证成功");
            [self getInfoByActCode];// 验证成功后刷新页面
        }else if (code.intValue == 20000){
            CSAlert(@"验证失败");
        }
        
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"验证失败");
        
        
    }];

}
#pragma MARK ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
