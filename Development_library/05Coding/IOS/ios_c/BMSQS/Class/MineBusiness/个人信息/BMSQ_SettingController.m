//
//  BMSQ_SettingController.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_SettingController.h"
#import "MobClick.h"
@interface BMSQ_SettingController ()
{
    UITableView* m_tableView;
    
}

@end

@implementation BMSQ_SettingController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"设置"];
    [self setNavBackItem];
    
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 15, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
//    if(section == 1)
//        return 0;
    return 20;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if(section == 1)
        return nil;
    
    UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
    v.backgroundColor = APP_VIEW_BACKCOLOR;
    return v;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//    if (section == 0)
//    {
//        return 3;
//    }
//    else
//    {
        return 1;
//    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 54;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (indexPath.section == 0)
//    {
//        static NSString* cellIdentify = @"cellIdentify";
//        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
//        if (cell == nil)
//        {
//            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
//            cell.selectionStyle = UITableViewCellSelectionStyleNone;
//            
//            UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(10, 54, APP_VIEW_WIDTH, 1)];
//            line0.backgroundColor = APP_CELL_LINE_COLOR;
//            [cell addSubview:line0];
//        }
//        
//        cell.textLabel.font = [UIFont systemFontOfSize:14];
//        
//        if (indexPath.row == 0)
//        {
//            cell.textLabel.text = @"消息提醒声音";
//            UISwitch* sw_notice = [[UISwitch alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 70, 10, 60, 24)];
//            [sw_notice addTarget:self action:@selector(noticeSet:) forControlEvents:UIControlEventValueChanged];
//            [cell addSubview:sw_notice];
//        }
//        else if(indexPath.row == 1)
//        {
//            cell.textLabel.text = @"接收优惠券推送消息";
//            UISwitch* sw_push = [[UISwitch alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 70, 10, 60, 24)];
//            [sw_push addTarget:self action:@selector(pushSet:) forControlEvents:UIControlEventValueChanged];
//            [cell addSubview:sw_push];
//        }
//        else
//        {
//            cell.textLabel.text = @"清除缓存";
//            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
//        }
//        
//        return cell;
//    }
//    else
//    {
        static NSString* cellIdentify = @"cellIdentify3";
        UITableViewCell* cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        UIButton* btnLoginOut = [[UIButton alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 44)];
        [btnLoginOut setTitle:@"退出登录" forState:UIControlStateNormal];
        [btnLoginOut addTarget:self action:@selector(btnLoginoutClikc) forControlEvents:UIControlEventTouchUpInside];
        [btnLoginOut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    btnLoginOut.titleLabel.font=[UIFont systemFontOfSize:15];
        [cell addSubview:btnLoginOut];
        return cell;
//    }
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;//return 2;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        if (indexPath.row == 2)
        {
            UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"确定退出清除缓存?" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"清除", nil];
            alert.tag = 101;
            [alert show];
        }
    }
}

- (void)noticeSet:(UISwitch*)sender
{
    UISwitch *swi2=(UISwitch *)sender;
    int isON = 0;
    if (swi2.isOn)
    {
        isON = 1;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSNumber numberWithInt:isON] forKey:@"isMsgBingOn"];
    
    NSString* vcode = [gloabFunction getSign:@"updateUserSetting" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"updateUserSetting" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (void)pushSet:(UISwitch*)sender
{
    UISwitch *swi2=(UISwitch *)sender;
    int isON = 0;
    if (swi2.isOn)
    {
        isON = 1;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSNumber numberWithInt:isON] forKey:@"isCouponMsgOn"];
    
    NSString* vcode = [gloabFunction getSign:@"updateUserSetting" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"updateUserSetting" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (void)loginOut
{
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *regid = [userDefaults objectForKey:RegistratcionID];

    [self initJsonPrcClient:@"0"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    
    [params setObject:@"1" forKey:@"appType"]; //0B 1C
    [params setObject:regid.length>0?regid:@"" forKey:@"registrationId"];
    
    NSString* vcode = [gloabFunction getSign:@"logoff" strParams:[gloabFunction getUserToken]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak __typeof(self)weakSelf = self;

    [self.jsonPrcClient invokeMethod:@"logoff" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:APP_USER_INFO_KEY];
        [defaults removeObjectForKey:APP_USER_AOCNUM_KEY];
        weakSelf.navigationController.tabBarController.selectedIndex=0;

        [weakSelf.navigationController popToRootViewControllerAnimated:YES];
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:APP_USER_INFO_KEY];
        [defaults removeObjectForKey:APP_USER_AOCNUM_KEY];
        [self.navigationController popToRootViewControllerAnimated:YES];
    }];
    
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"logoff" object:nil];
}

- (void)btnLoginoutClikc
{
    UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"确定退出登录?" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    alert.tag = 100;
    [alert show];
 

}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 100 && buttonIndex == 1)
    {
        [self loginOut];
        [MobClick event:@"myhome_fragment_exit"];// myhome_fragment_exit

    }
    else if(alertView.tag == 101 && buttonIndex == 1)
    {
        
    }
}


@end
