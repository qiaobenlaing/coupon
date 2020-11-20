//
//  ActivityManagementViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ActivityManagementViewController.h"
#import "ReservationlistViewController.h"
#import <ShareSDK/ShareSDK.h>
#import "HQShareUtils.h"
#import "NewActivityViewController.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "RRC_webViewController.h"

#import "NewActivityViewController.h"

@interface ActivityManagementViewController ()<UITableViewDataSource, UITableViewDelegate>

{
    NSString * statusStr;
}


@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSDictionary * dataSource;

@property (nonatomic, strong) UIImageView * topImageView;
@property (nonatomic, strong) UILabel     * activityStateLB;
@property (nonatomic, strong) UILabel     * signUpLB;// 报名
@property (nonatomic, strong) UILabel     * countLB;
@property (nonatomic, strong) UILabel     * readLB;
@property (nonatomic, strong) UILabel     * collectionLB;


@end

@implementation ActivityManagementViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"活动管理"];
    [self setNavBackItem];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    [self sGetActInfo];
    
}

- (void)topView
{
    UIView * vView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_WIDTH * 32.0 / 75 + 30)];
    vView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:vView];
    
    self.topImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH * 32.0 / 75)];
    
    [_topImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.dataSource[@"activityImg"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    _topImageView.userInteractionEnabled=YES;
    [vView addSubview:_topImageView];
    
    UIView * background = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_WIDTH * 32.0 / 75 - 50, APP_VIEW_WIDTH, 50)];
    background.backgroundColor = [UIColor blackColor];
    background.alpha = 0.3;
    [self.topImageView addSubview:background];
    
    
    UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesturClick)];
    
    [_topImageView addGestureRecognizer:tapGesturRecognizer];
    
    
    self.countLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH - 20, 50)];
    _countLB.font = [UIFont systemFontOfSize:14];
    _countLB.numberOfLines = 0;
    _countLB.textColor = [UIColor whiteColor];
    self.countLB.text = [NSString stringWithFormat:@"%@", self.dataSource[@"activityName"]];
    [self.topImageView addSubview:_countLB];
    [background addSubview:_countLB];
    
    self.activityStateLB = [[UILabel alloc] initWithFrame:CGRectMake(10, APP_VIEW_WIDTH * 32.0 / 75 + 5, 125, 20)];
    _activityStateLB.font = [UIFont systemFontOfSize:12];
    _activityStateLB.textAlignment = NSTextAlignmentCenter;
    NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
    
    if (num.intValue == 0) {
        self.activityStateLB.text = @"活动未发布";
    }else if (num.intValue == 1){
        self.activityStateLB.text = @"活动已发布";
    }else if (num.intValue == 2){
        self.activityStateLB.text = @"活动已停止报名";
    }else if (num.intValue == 3){
        self.activityStateLB.text = @"活动已取消";
    }else if (num.intValue == 4){
        self.activityStateLB.text = @"活动已结束";
    }else if (num.intValue == 5){
        self.activityStateLB.text = @"活动已满员";
    }
     _activityStateLB.layer.cornerRadius = 5;
    _activityStateLB.clipsToBounds = YES;
    _activityStateLB.layer.borderColor = [[UIColor grayColor]CGColor];
    _activityStateLB.layer.borderWidth = 0.5f;
    _activityStateLB.layer.masksToBounds = YES;
    
    [vView addSubview:_activityStateLB];
    
    self.signUpLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 120, APP_VIEW_WIDTH * 32.0 / 75 + 5, 110, 20)];
    _signUpLB.font = [UIFont systemFontOfSize:12];
    _signUpLB.textAlignment = NSTextAlignmentCenter;
    
    if ([self.dataSource[@"limitedParticipators"] isEqualToString:@"0"]) {
        
        NSString * maxPay = [NSString stringWithFormat:@"%@", self.dataSource[@"totalPayment"]];
        if (maxPay.intValue == 0) {
            
            self.signUpLB.text = @"免费";
            
        }else{
            self.signUpLB.text = [NSString stringWithFormat:@"已报名:%@", self.dataSource[@"participators"]];
            
        }
        
        
    }else{
        NSString * participators = self.dataSource[@"participators"];
        if ([participators isEqual:[NSNull null]]){
            self.signUpLB.text = [NSString stringWithFormat:@"已报名:%@/%@", @"0", self.dataSource[@"limitedParticipators"]];
        }else{
            self.signUpLB.text = [NSString stringWithFormat:@"已报名:%@/%@", self.dataSource[@"participators"], self.dataSource[@"limitedParticipators"]];
        }
        
    }

    
    _signUpLB.layer.cornerRadius = 5;
    _signUpLB.clipsToBounds = YES;
    _signUpLB.layer.borderColor = [[UIColor grayColor]CGColor];
    _signUpLB.layer.borderWidth = 0.5f;
    _signUpLB.layer.masksToBounds = YES;
    [vView addSubview:_signUpLB];
    
    UIView * readView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + APP_VIEW_WIDTH * 32.0 / 75 + 35, APP_VIEW_WIDTH / 2 - 1, 40)];
    readView.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:readView];
    
    UIImageView * readImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 10, 20, 20)];
    readImage.image = [UIImage imageNamed:@"阅读"];
    [readView addSubview:readImage];
    
    UILabel * readCountLB = [[UILabel alloc] initWithFrame:CGRectMake(50, 10, readView.frame.size.width - 50, 20)];
    readCountLB.text = [NSString stringWithFormat:@"%@ 阅读", self.dataSource[@"pageviews"]];
    [readView addSubview:readCountLB];
    
    UIView * collectionView = [[UIView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH / 2, APP_VIEW_ORIGIN_Y + APP_VIEW_WIDTH * 32.0 / 75 + 35, APP_VIEW_WIDTH / 2 - 1, 40)];
    collectionView.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:collectionView];
    
    UIImageView * collectionImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 10, 20, 20)];
    collectionImage.image = [UIImage imageNamed:@"收藏"];
    [collectionView addSubview:collectionImage];
    
    UILabel * collectionCountLB = [[UILabel alloc] initWithFrame:CGRectMake(50, 10, readView.frame.size.width - 50, 20)];
    collectionCountLB.text = [NSString stringWithFormat:@"%@ 收藏", self.dataSource[@"collectNbr"]];
    [collectionView addSubview:collectionCountLB];
    
    
}


- (void)setViewUp
{
    UIView * footerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 60)];
    footerView.backgroundColor = [UIColor whiteColor];
    UIButton * postActivityBut = [UIButton buttonWithType:UIButtonTypeCustom];
    postActivityBut.frame = CGRectMake((APP_VIEW_WIDTH - 200) / 2, 15, 200, 35);
    
    NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
    if (num.intValue == 0) {
        postActivityBut.backgroundColor = UICOLOR(166, 16, 21, 1.0);
        [postActivityBut addTarget:self action:@selector(postActivityClick:) forControlEvents:UIControlEventTouchUpInside];
    }else{
        
        postActivityBut.backgroundColor = UICOLOR(189, 189, 189, 1.0);
    }
    
    [postActivityBut setTitle:@"发布活动" forState:UIControlStateNormal];
    [postActivityBut setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    [footerView addSubview:postActivityBut];
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + APP_VIEW_WIDTH * 32.0 / 75 + 80, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y - APP_VIEW_WIDTH * 32.0 / 75 - 80) style:UITableViewStyleGrouped];
    _baseView.dataSource = self;
    _baseView.delegate = self;
    _baseView.scrollEnabled = YES;
    _baseView.tableFooterView = footerView;
    [self.view addSubview:self.baseView];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSString * status = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
    
    if (self.totalPayment.intValue == 0) {
        
        if (section == 0) {
            if (status.intValue == 1) {
                return 2;
            }else{
                return 1;
            }
            
        }else if (section == 1){
            if (status.intValue == 0) {
                return 2;
            }else if (status.intValue == 1){
                return 1;
            }else if (status.intValue == 3){
                return 1;
            }else if (status.intValue == 4){
                return 1;
            }
            
        }

        
    }else{
        
        if (section == 0) {
            
            if (status.intValue == 1) {
                return 2;
            }else{
                return 1;
            }
            
        }else if (section == 1){
            if (status.intValue == 0) {
                return 2;
            }else if (status.intValue == 1){
                return 2;
            }else if (status.intValue == 2){
                return 1;
            }else if (status.intValue == 3){
                return 1 ;
            }else if (status.intValue == 4){
                return 1;
            }else if (status.intValue == 5){
                return 1;
            }
            
        }

        
        
    }
    
    
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

        static NSString * cell_id = @"UITableViewCell";
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    /*'DISABLE'  => 0, //未发布
     'ACTIVE'   => 1, //已发布
     'STOP_TO_JOIN'  => 2, //停止报名
     'CANCEL'   => 3, //取消
     'EXPIRED'  => 4, //活动已结束
     'FULL'     => 5, //活动已满员*/
    NSString * status = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
        if (indexPath.section == 0 && indexPath.row == 0) {
            cell.textLabel.text = @"预定名单";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }else if (indexPath.section == 0 && indexPath.row == 1){
            cell.textLabel.text = @"分享给好友";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }else if (indexPath.section == 1 && indexPath.row == 0){
            if (self.totalPayment.intValue == 0){
                if (status.intValue == 0){
                    cell.textLabel.text = @"编辑活动";
                }else if (status.intValue == 1){
                    cell.textLabel.text = @"活动取消";
                }else if (status.intValue == 3){
//                    cell.textLabel.text = @"编辑活动";
                    cell.textLabel.text = @"删除活动";
                }else if (status.intValue == 4){
                    cell.textLabel.text = @"重新发起活动";
                }
                
            }else{
                
                if (status.intValue == 0) {
                    cell.textLabel.text = @"编辑活动";
                }else if (status.intValue == 1){
                    cell.textLabel.text = @"暂停报名";
                }else if (status.intValue == 2){
                    cell.textLabel.text = @"活动开始报名";
                }else if (status.intValue == 3){
                    cell.textLabel.text = @"删除活动";
                }else if (status.intValue == 4){
                    cell.textLabel.text = @"重新发起活动";
                }else if (status.intValue == 5){
                    cell.textLabel.text = @"活动取消";
                }
                
            }
            
            
             cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }else if (indexPath.section == 1 && indexPath.row == 1){
            
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            if (self.totalPayment.intValue == 0){
                if (status.intValue == 0){
                    cell.textLabel.text = @"删除活动";
                }
//                else if (status.intValue == 3){
//                    cell.textLabel.text = @"删除活动";
//                }
                
            }else{
            
            if (status.intValue == 0) {
                cell.textLabel.text = @"删除活动";
            }else if (status.intValue == 1){
                cell.textLabel.text = @"活动取消";
            }else if (status.intValue == 3){
                cell.textLabel.text = @"删除活动";
            }
                
            }
            
            
        }
        
        
        
        return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 0) {
        if (self.totalPayment.intValue == 0) {
            CSAlert(@"该活动为免费的，无预定人员名单");
        }else{
            ReservationlistViewController * reservationVC = [[ReservationlistViewController alloc] init];
            reservationVC.activityCode = self.dataSource[@"activityCode"];
            reservationVC.totalPayment = self.totalPayment;
            [self.navigationController pushViewController:reservationVC animated:YES];
            
        }
       
        
    }else if (indexPath.section == 0 && indexPath.row == 1){
        
        NSLog(@"分享给好友");
        [HQShareUtils shareCouponWithTitle:[NSString stringWithFormat:@"%@活动邀请你一起来",self.dataSource[@"shareArr"][@"title"]] content:[NSString stringWithFormat:@"%@", self.dataSource[@"shareArr"][@"content"]] url:[NSString stringWithFormat:@"%@", self.dataSource[@"shareArr"][@"link"]]];

        
    }else if (indexPath.section == 1 && indexPath.row == 0){
        NSString * status = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
        
        
        if (self.totalPayment.intValue == 0){
            if (status.intValue == 0){
                NSLog(@"->编辑活动--");
                
                NewActivityViewController *vc = [[NewActivityViewController alloc] init];
                vc.activityCode = [NSString stringWithFormat:@"%@", self.dataSource[@"activityCode"]];
                vc.isEditActivity = YES;
                [self.navigationController pushViewController:vc animated:YES];
                
            }else if (status.intValue == 1){
                NSLog(@"活动取消");
                statusStr = @"3";
                [self changeActivityStatus];
            }else if (status.intValue == 3){
                NSLog(@"删除活动");
                [self delActivity];
            }
            else if (status.intValue == 4){
                NSLog(@"重新发起活动");
                NewActivityViewController * newActivityVC = [[NewActivityViewController alloc] init];
                [self.navigationController pushViewController:newActivityVC animated:YES];
            }
            
        }else{
            
            if (status.intValue == 0) {
                NSLog(@"---->编辑活动");
                NewActivityViewController *vc = [[NewActivityViewController alloc] init];
                vc.isEditActivity = YES;
                vc.activityCode = [NSString stringWithFormat:@"%@", self.dataSource[@"activityCode"]];
                [self.navigationController pushViewController:vc animated:YES];
                
            }else if (status.intValue == 1){
                NSLog(@"暂停报名");
                statusStr = @"2";
                [self changeActivityStatus];
            }else if (status.intValue == 2){
                NSLog(@"活动开始报名");
                statusStr = @"1";
                [self changeActivityStatus];
            }else if (status.intValue == 3){
                NSLog(@"删除活动");
                [self delActivity];
            }
            else if (status.intValue == 4){
                NSLog(@"重新发起活动");
                NewActivityViewController * newActivityVC = [[NewActivityViewController alloc] init];
                [self.navigationController pushViewController:newActivityVC animated:YES];
            }else if (status.intValue == 5){
                NSLog(@"活动取消");
                statusStr = @"3";
                [self changeActivityStatus];
            }
            
        }

        
        
    }else if (indexPath.section == 1 && indexPath.row == 1){
        
        NSString * status = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
        if (self.totalPayment.intValue == 0){
            if (status.intValue == 0){
                NSLog(@"删除活动");
                [self delActivity];
            }
//            else if (status.intValue == 3){
//                NSLog(@"删除活动");
//                [self delActivity];
//            }
            
        }else{
            
            if (status.intValue == 0) {
                NSLog(@"删除活动");
                [self delActivity];
            }else if (status.intValue == 1){
                NSLog(@"活动取消");
                statusStr = @"3";
                [self changeActivityStatus];
            }
//            else if (status.intValue == 3){
//                NSLog(@"删除活动");
//                [self delActivity];
//            }
            
        }

        
       
        
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }
    return 5;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 40;
}


#pragma mark ----- 按钮方法
- (void)postActivityClick:(UIButton *)sender
{
    NSLog(@"发布活动");
    statusStr = @"1";
    [self changeActivityStatus];
    
}
#pragma mark ------- tapGesturClick   图片点击事件
- (void)tapGesturClick
{
    NSLog(@"点击图片");
    RRC_webViewController * webVC = [[RRC_webViewController alloc] init];
    webVC.navtitle = @"活动详情";
    webVC.isHidenNav = YES;
    webVC.requestUrl = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@",APP_SERVERCE_H5_URL, self.dataSource[@"activityCode"]];
    [self.navigationController pushViewController:webVC animated:YES];
    
}

#pragma mark ----- sGetActInfo($activityCode)
- (void)sGetActInfo
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.activityCode forKey:@"activityCode"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetActInfo" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sGetActInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        
        self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
        [self setViewUp];
        [self topView];
        
         NSString * num = [NSString stringWithFormat:@"%@", self.dataSource[@"status"]];
        
        if (num.intValue == 5) {
            
            NSString * buildNameStr =@"您发起的活动";
            NSString * activityName = self.dataSource[@"activityName"];
            NSString * Str1 = [buildNameStr stringByAppendingString:[NSString stringWithFormat:@"%@",activityName]];
            NSString * Str2 = [Str1 stringByAppendingString:@"报名已满员啦"];
            CSAlert(Str2);
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark ---- delActivity($activityCode) 删除活动
- (void)delActivity
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.activityCode forKey:@"activityCode"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"delActivity" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"delActivity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        NSString * code = [NSString stringWithFormat:@"%@", responseObject[@"code"]];
        if (code.intValue == 50000) {
            CSAlert(@"活动删除成功");
            [self.navigationController popViewControllerAnimated:YES];
        }else if (code.intValue == 50221){
            CSAlert(@"活动不能删除");
        }else if (code.intValue == 50220){
            CSAlert(@"活动不存在");
        }else if (code.intValue == 20000){
            CSAlert(@"活动删除失败");
        }
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"删除失败");
        
        
    }];

}

#pragma mark ------ changeActivityStatus($activityCode $status)改变活动状态
- (void)changeActivityStatus
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.activityCode forKey:@"activityCode"];
    [params setObject:statusStr forKey:@"status"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"changeActivityStatus" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"changeActivityStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        NSString * code = [NSString stringWithFormat:@"%@", responseObject[@"code"]];
        if (code.intValue == 50000) {
            
            CSAlert(@"修改成功");
            [self sGetActInfo];
            [self setViewUp];
            [self topView];
        }else if (code.intValue == 50220){
            CSAlert(@"活动不存在");
        }else if (code.intValue == 50221){
            CSAlert(@"活动现在状态不可变更");
        }else if (code.intValue == 50225){
            CSAlert(@"活动状态没有变化");
        }
        
        [self.baseView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"修改失败");
        
        
    }];

}


#pragma mark ----- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
