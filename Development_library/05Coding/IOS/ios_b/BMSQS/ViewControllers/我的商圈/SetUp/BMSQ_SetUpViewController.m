//
//  BMSQ_SetUpViewController.m
//  BMSQS
//
//  Created by gh on 15/10/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_SetUpViewController.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"

@interface BMSQ_SetUpViewController () {
    UISwitch *sw_message;
    
    NSString *staffCode;
}



@end

@implementation BMSQ_SetUpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setViewUp];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setViewUp {
    
//    [self.navigationController setNavigationBarHidden:NO];
//    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
//    [self.navigationItem setTitle:@"设置"];
    
    
    [self setNavTitle:@"设置"];
    [self setNavBackItem];
    
    CGFloat x = 10;
    CGFloat height = 20;
    
    
    UIView *View1 = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 44)];
    View1.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:View1];
    
    
    UILabel *lb_message = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH/2, 44)];
    lb_message.text = @"接收支付短信";
    lb_message.font = [UIFont systemFontOfSize:13];
    [View1 addSubview:lb_message];
    
    sw_message = [[UISwitch alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 10, 44, 20)];
    [sw_message addTarget:self action:@selector(switchAct:)  forControlEvents:UIControlEventValueChanged];
    [View1 addSubview:sw_message];
    
    
    
    UIButton *btn_exit = [[UIButton alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+74, APP_VIEW_WIDTH, 44)];
    btn_exit.backgroundColor = [UIColor whiteColor];
    [btn_exit addTarget:nil action:@selector(exitAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btn_exit];
    
    UILabel *lb_exit = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH/2, 44)];
    lb_exit.font = [UIFont systemFontOfSize:13];
    lb_exit.text = @"退出登录";
    [btn_exit addSubview:lb_exit];
    
    [self getShopStaffSetting];
    
    
}


#pragma mark - 是否接受支付短信
- (void)switchAct:(id)sender {
    
    NSLog(@"修改开关状态");
    
    UISwitch *switchButton = (UISwitch *)sender;
    
    
    NSString *istake;
    if (switchButton.on) {
        istake = @"1";
    }else {
        istake = @"0";
    }
    [self updateShopStaffSetting:istake];
    
}

- (void)getShopStaffSetting {
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.mobileNbr forKey:@"mobileNbr"];
    
    NSString* vcode = [gloabFunction getSign:@"getShopStaffSetting" strParams:self.mobileNbr];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getShopStaffSetting" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        if  ([responseObject count]>0) {
            staffCode = [responseObject objectForKey:@"staffCode"];
            sw_message.on = [[responseObject objectForKey:@"isSendPayedMsg"] intValue];

        }
        
        NSLog(@"%@",responseObject);
      
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];

}


- (void)updateShopStaffSetting:(NSString *)setting {
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getStaffCode] forKey:@"staffCode"];
    [params setObject:setting forKey:@"setting"];
    
    
    NSString* vcode = [gloabFunction getSign:@"updateShopStaffSetting" strParams:staffCode ];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updateShopStaffSetting" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        [ProgressManage closeProgress];
        NSLog(@"%@",responseObject);
        
        NSString *str = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        if  ( str.intValue == 50000){
            CSAlert(@"修改成功");
            sw_message.on = setting.intValue;
            
        } else if (str.intValue == 20000) {
            sw_message.on = !setting.intValue;
            CSAlert(@"修改失败");
            
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        sw_message.on = !setting.intValue;
        CSAlert(@"修改失败");
        
        
    }];
    
}


#pragma mark - 退出登录
- (void)exitAction {
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"退出惠圈" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    alert.tag = 100;
    alert.delegate = self;
    [alert show];
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(alertView.tag==100){
        if(buttonIndex==1)
        {
            [self initJsonPrcClient:@"0"];
            NSMutableDictionary * dicTemp = [[NSMutableDictionary alloc] init];
            NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
            
            NSString *registrationId = [userDefault objectForKey:RegistratcionID];
            [dicTemp setObject:registrationId forKey:@"registrationId"];
            [dicTemp setObject:@"0" forKey:@"appType"];
            [dicTemp setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
            
            [self.jsonPrcClient invokeMethod:@"logoff" withParameters:dicTemp success:^(AFHTTPRequestOperation *operation, id responseObject) {
                int resCode = [[responseObject objectForKey:@"code"] intValue];
                switch (resCode) {
                        
                        
                    case 50000:{
                        NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
                        [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
                        NSLog(@"%@",[gloabFunction getUserToken]);
                        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
                        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
                        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
                        
                        
                        [self presentViewController:nav animated:YES completion:^{
                            
                        }];
                    }
                        break;
                    case 20000:
                        CSAlert(@"退出失败");
                        break;
                    default:
                        break;
                }
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            }];
            
            
        }
    }
}




@end
