//
//  BMSQ_MyBusinessController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyBusinessController.h"
#import "BMSQ_MyCenterCell.h"
#import "BMSQ_PersonInfoController.h"
#import "BMSQ_SettingController.h"
#import "BMSQ_AboutController.h"
#import "BMSQ_FeedBackViewController.h"
#import "BMSQ_MineIdentViewController.h"
#import "BMSQ_MyBankCardController.h"
#import "BMSQ_MyNoticeController.h"
#import "BMSQ_RedEnvelopeController.h"
#import "BMSQ_PayCardViewController.h"
#import "BMSQ_modifyPwdViewController.h"
#import "BMSQ_MyActivityViewController.h"
#import "SVProgressHUD.h"
#import "BMSQ_MemberChartViewController.h"
#import "BMSQ_MyRecoCodeViewController.h"
#import "BMSQ_setMyCardViewController.h"

#import "BMSQ_modifyPayPwdViewController.h"
#import "MobClick.h"





#import "BMSQ_paymentViewcontroller.h"


#import "UserCenterCell.h"
#import "BMSQ_UserAssetsViewController.h"
#import "BMSQ_PhoneViewController.h"
#import "BMSQ_CourseViewController.h"
#import "BMSQ_TrainViewController.h"


@interface BMSQ_MyBusinessController ()<UserCenterCellDelegate>
{
    
    NSDictionary* m_dicUserInfo;
    
    NSInteger numberMsg;

    
}

@property (nonatomic, strong)UIImage *headImg;
@property (nonatomic, strong)UITableView* m_tableView;
@property (nonatomic, assign)BOOL isShow;  //是否显示推荐得好壕礼


@end

@implementation BMSQ_MyBusinessController

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"MyBusiness"];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
    [self ifShowRecommend];

}

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"我的惠圈"];
    
    m_dicUserInfo = [[NSDictionary alloc]init];
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-APP_TABBAR_HEIGHT)];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.m_tableView];
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    [btnRightBar addTarget:self action:@selector(btnRightClick) forControlEvents:UIControlEventTouchUpInside];
    UIImageView* ivScan = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 44, 44)];
    ivScan.image = [UIImage imageNamed:@"iv_activityRight"];
    [btnRightBar addSubview:ivScan];
    
    [self setNavRightBarItem:btnRightBar];
    
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }
    
    [self getCountAllTypeMsg];
}

- (void)btnRightClick
{
    BMSQ_PersonInfoController* infoCtrl = [[BMSQ_PersonInfoController alloc]init];
    infoCtrl.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:infoCtrl animated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [MobClick beginLogPageView:@"MyBusiness"];
    [self getCountAllTypeMsg];
    [self getUserInfo];
}

- (void)getUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserInfo" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        m_dicUserInfo = [[NSDictionary alloc]initWithDictionary:responseObject];
        NSString *str = [NSString stringWithFormat:@"%@",[m_dicUserInfo objectForKey:@"isUserSetPayPwd"]];
        NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
        [userD setObject:str forKey:APP_USER_FREEVALCODEPAY];
        [userD setObject:[NSString stringWithFormat:@"%@%@",H5_URL,[responseObject objectForKey:@"avatarUrl"]] forKey:APP_USER_HEADIMG];
        str = [NSString stringWithFormat:@"%@",[m_dicUserInfo objectForKey:@"mobileNbr"]];
        userD = [NSUserDefaults standardUserDefaults];
        [userD setObject:str forKey:APP_USER_MOBILENBR];
        [userD synchronize];
        
 
        [self.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}


- (void)getCountAllTypeMsg
{
    [self initJsonPrcClient:@"2"];
//    [SVProgressHUD showWithStatus:@"正在加载"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"countAllTypeMsg" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"countAllTypeMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSNumber *shop = [responseObject objectForKey:@"shop"];
        NSNumber *coupon = [responseObject objectForKey:@"coupon"] ;
//        NSNumber *card = [responseObject objectForKey:@"card"];
        NSNumber *communication = [responseObject objectForKey:@"communication"] ;
        numberMsg = ([shop integerValue] + [coupon integerValue]+ /*[card integerValue] +*/ [communication integerValue]);
        [self.m_tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}

#pragma mark 是否有推荐得壕礼活动
- (void)ifShowRecommend
{
    [self initJsonPrcClient:@"2"];
    

    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"ifShowRecommend" withParameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        

        NSString *str = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"isShow"]];
        
        if ([str intValue]==0) { //不显示
            weakSelf.isShow = NO;
        }else{
            weakSelf.isShow = YES;
        }
        
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:2];
        [weakSelf.m_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"asdfasfd");
        
    }];
}

#pragma mark - UITableView delegate


-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 15)];
    v.backgroundColor = APP_VIEW_BACKCOLOR;
    return v;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==0||section ==1) {
        return 0;
    }
    return 15;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0 ||section == 1)//头像、我的
    {
        return 1;
    }
    else if(section == 2)//我的银行卡, 支付管理 推荐好友得壕礼
    {
        return self.isShow == 1?3:2;
    }
    else if(section == 3)//我的红包、我的活动、我的消息、对惠圈的建议
    {
        return 4;
    }
    else if(section == 4)// 修改密码
    {
        return 1;
    }
    else if(section == 5)//设置、关于
    {
        return 2;
    }else{
        return 1;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 90;
    }else if (indexPath.section ==1){
        return 180;
    }

    return 45;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0)
    {
        static NSString* cellIdentify = @"cellIdentify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIImageView* ivBack = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 90)];
            ivBack.image = [UIImage imageNamed:@"iv_mysqBack"];
            [cell addSubview:ivBack];
            
            UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(20, 15, 60, 60)];
            iv_logo.layer.cornerRadius = 30;
            iv_logo.layer.masksToBounds = YES;
            iv_logo.layer.borderWidth = 0.3;
            iv_logo.layer.borderColor = [[UIColor grayColor]CGColor];
            iv_logo.backgroundColor = [UIColor whiteColor];
            iv_logo.tag = 100;
            [cell addSubview:iv_logo];

            UILabel* lb_name = [[UILabel alloc]initWithFrame:CGRectMake(90, 15, APP_VIEW_WIDTH-100, 20)];
            lb_name.backgroundColor = [UIColor clearColor];
//            lb_name.textAlignment = NSTextAlignmentCenter;
            lb_name.textColor = [UIColor whiteColor];
            lb_name.font = [UIFont systemFontOfSize:13];
            lb_name.tag = 200;
            [cell addSubview:lb_name];
            
            UILabel* lb_phone = [[UILabel alloc]initWithFrame:CGRectMake(90, 35, APP_VIEW_WIDTH-100, 20)];
            lb_phone.backgroundColor = [UIColor clearColor];
//            lb_phone.textAlignment = NSTextAlignmentCenter;
            lb_phone.textColor = [UIColor whiteColor];
            lb_phone.font = [UIFont systemFontOfSize:13];
            lb_phone.tag = 300;
            [cell addSubview:lb_phone];
            
            UILabel* lb_lvs = [[UILabel alloc]initWithFrame:CGRectMake(90, 55, APP_VIEW_WIDTH-100, 20)];
            lb_lvs.backgroundColor = [UIColor clearColor];
            //            lb_phone.textAlignment = NSTextAlignmentCenter;
            lb_lvs.textColor = [UIColor whiteColor];
            lb_lvs.font = [UIFont systemFontOfSize:13];
            lb_lvs.tag = 400;
            [cell addSubview:lb_lvs];
        }
       
        UIImageView *iv_logo = [cell viewWithTag:100];
        UILabel *lb_name = [cell viewWithTag:200];
        UILabel *lb_phone = [cell viewWithTag:300];
        UILabel *lb_lvs = [cell viewWithTag:400];


        [iv_logo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[m_dicUserInfo objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            self.headImg = iv_logo.image;

        }];
    
        lb_name.text = [NSString stringWithFormat:@"昵称 : %@",[gloabFunction changeNullToBlank:[m_dicUserInfo objectForKey:@"nickName"]]];
        lb_phone.text = [NSString stringWithFormat:@"手机号 : %@",[gloabFunction changeNullToBlank:[m_dicUserInfo objectForKey:@"mobileNbr"]]];
        lb_lvs.text = @"会员级别 : A";
        
        return cell;
    }
    else if (indexPath.section ==1){
        static NSString *identifier = @"userCenterCell";
        
        UserCenterCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UserCenterCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.userCenterDelegate = self;
        }
        
        return cell;
    }
    else{
        static NSString* cellIdentify = @"cellIdentify2";
        BMSQ_MyCenterCell* cell = (BMSQ_MyCenterCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[BMSQ_MyCenterCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
        }
        
        if (indexPath.section == 2) {
            if (indexPath.row ==0) {
                [cell setCellValue:@"iv_myICBC" title:@"我的银行卡"];
            }else if(indexPath.row == 1){
                [cell setCellValue:@"no_iden_pay" title:@"支付管理"];
                
                NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
                NSString *isFree = [userD objectForKey:APP_USER_FREEVALCODEPAY];
                if ([isFree isEqualToString:@"1"]) {
                    cell.lb_msg.hidden = YES;
                    
                }else{
                    cell.lb_msg.hidden = NO;
                }
            }else{
                 [cell setCellValue:@"iv_recommend" title:@"推荐好友得壕礼"];
            }
            
            
            
        }else if (indexPath.section == 3){
            if (indexPath.row ==0) {
                [cell setCellValue:@"iv_myRedPacket" title:@"我的红包"];
            }else if(indexPath.row ==1){
                [cell setCellValue:@"iv_myActivity" title:@"我的活动"];
            }else if (indexPath.row ==2){
                [cell setCellValue:@"iv_message" title:@"我的消息"];
            }else{
                 [cell setCellValue:@"iv_feedBack" title:@"对惠圈的建议"];
            }
            
            
            
        }
        else if (indexPath.section == 4){
            [cell setCellValue:@"iv_password" title:@"修改密码"];
        }

        else if (indexPath.section == 5){

            if (indexPath.row ==0) {
                [cell setCellValue:@"iv_setting" title:@"设置"];
            }else if(indexPath.row ==1){
               [cell setCellValue:@"iv_about" title:@"关于"];
            }
        
        }
        
        return cell;
    }
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 6;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
     
        
        BMSQ_PersonInfoController* infoCtrl = [[BMSQ_PersonInfoController alloc]init];
        infoCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:infoCtrl animated:YES];
        
    }
    else if (indexPath.section == 1)
    {
//        RRC_webViewController* huibiCtrl = [[RRC_webViewController alloc]init];
//        huibiCtrl.requestUrl = [NSString stringWithFormat:@"%@Browser/huibiIntro",H5_URL];
//        huibiCtrl.hidesBottomBarWhenPushed = YES;
//        huibiCtrl.isHidenNav = YES;
//        [self.navigationController pushViewController:huibiCtrl animated:YES];
        
        BMSQ_paymentViewcontroller *vc = [[BMSQ_paymentViewcontroller alloc]init];
        vc.hidesBottomBarWhenPushed = YES;
        vc.headImage = self.headImg;
        [self.navigationController pushViewController:vc animated:YES];
        [MobClick event:@"myBusiness_Payment"]; // 友盟统计
    }

//    else if(section == 2)//我的银行卡, 支付管理 推荐好友得壕礼
//    {
//        return self.isShow == 1?3:2;
//    }
//    else if(section == 3)//我的红包、我的活动、我的消息、对惠圈的建议
//    {
//        return 4;
//    }
//    else if(section == 4)// 修改密码
//    {
//        return 1;
//    }
//    else if(section == 5)//设置、关于
//    {
//        return 2;

    
    else if (indexPath.section == 2)
    {
      
        if (indexPath.row == 0)
        {
            BMSQ_MyBankCardController *vc = [[BMSQ_MyBankCardController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            [MobClick event:@"myBusiness_MyBankCard"]; // 友盟统计
        }else if (indexPath.row == 1)
        {
            BMSQ_MineIdentViewController *vc = [[BMSQ_MineIdentViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            [MobClick event:@"myBusiness_MineIdent"]; // 友盟统计
        }else if (indexPath.row == 2){
            
            NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
            NSString *str = [userD objectForKey:APP_USER_FREEVALCODEPAY];
            if ([str isEqualToString:@"1"]) {
                BMSQ_modifyPayPwdViewController *vc = [[BMSQ_modifyPayPwdViewController alloc]init];
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
            }else{
                BMSQ_setMyCardViewController *vc = [[BMSQ_setMyCardViewController alloc]init];
                vc.formvc = 1;
                vc.myTitle = @"设置支付密码";
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
            }
        
        }
        

    }
    else if(indexPath.section == 3)
    {

        if (indexPath.row == 0)   
        {
            BMSQ_RedEnvelopeController* bankCtrl = [[BMSQ_RedEnvelopeController alloc]init];
            bankCtrl.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:bankCtrl animated:YES];
            [MobClick event:@"myBusiness_RedEnvelope"]; // 友盟统计
        }
        else if(indexPath.row == 1)
        {
            BMSQ_MyActivityViewController *vc = [[BMSQ_MyActivityViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            [MobClick event:@"myBusiness_MyActivity"]; // 友盟统计
        }
        else if (indexPath.row == 2)
        {
            BMSQ_MyNoticeController* noticeCtrl = [[BMSQ_MyNoticeController alloc]init];
            noticeCtrl.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:noticeCtrl animated:YES];
            [MobClick event:@"myBusiness_MyNotice"]; // 友盟统计
        }
        else if (indexPath.row == 3)
        {
            BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
            vc.shopID = APP_HQ_CODE;
            vc.myTitle = @"给惠圈留言";
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            [MobClick event:@"myBusiness_MemberChart"]; // 友盟统计
        }
        
    }
    else if (indexPath.section == 4)
    {
        if (indexPath.row == 0) {
            BMSQ_modifyPwdViewController* modifyPwd = [[BMSQ_modifyPwdViewController alloc] init];
            modifyPwd.userMobile = [m_dicUserInfo objectForKey:@"mobileNbr"];
            modifyPwd.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:modifyPwd animated:YES];
        }
    }
    else if (indexPath.section == 5)
    {
        if (indexPath.row == 0)  // 设置/ 退出登录
        {
            BMSQ_SettingController* setCtrl = [[BMSQ_SettingController alloc]init];
            setCtrl.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:setCtrl animated:YES];
        }
        else  //关于
        {
            BMSQ_AboutController* aboutCtrl = [[BMSQ_AboutController alloc]init];
            aboutCtrl.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:aboutCtrl animated:YES];
            
        }
    }
}


#pragma mark clickCell
-(void)clickUserCenterCell:(int)tag{

    
    
    switch (tag) {
        case 100:
        {
            //付款码
            NSLog(@"付款码");
            
            
            BMSQ_paymentViewcontroller *vc = [[BMSQ_paymentViewcontroller alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            vc.headImage = self.headImg;
            [self.navigationController pushViewController:vc animated:YES];

            
        }
            break;
        case 101:
        {
            //手机充值
            NSLog(@"手机充值");
            
            
            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"敬请期待" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alterView show];
//            BMSQ_PhoneViewController *vc = [[BMSQ_PhoneViewController alloc]init];
//            vc.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:vc animated:YES];
            
        }
            break;
        case 102:
        {
            //培训报名
            NSLog(@"培训报名");
            BMSQ_TrainViewController *vc = [[BMSQ_TrainViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];

            
        }
            break;
        case 103:
        {
            //惠圈资产
            NSLog(@"惠圈资产");
            BMSQ_UserAssetsViewController *vc = [[BMSQ_UserAssetsViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];

        }
            break;
        case 104:
        {
            //惠圈订单
            NSLog(@"惠圈订单");
            
            BMSQ_MineIdentViewController *vc = [[BMSQ_MineIdentViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];


        }
            break;
        case 105:
        {
            //课程表
            NSLog(@"课程表");
            BMSQ_CourseViewController *vc = [[BMSQ_CourseViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            
        }
            break;
            
        default:
            break;
    }
    
}


-(void)gotoSec{
    NSIndexPath *indext =[NSIndexPath indexPathForRow:2 inSection:2];
    [self  tableView:self.m_tableView didSelectRowAtIndexPath:indext];
}

@end
