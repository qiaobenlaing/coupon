//
//  BMSQ_ModifyPasswordStep2ViewController.m
//  BMSQS
//
//  Created by lxm on 15/9/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ModifyPasswordStep2ViewController.h"
#import "MD5.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"

@interface BMSQ_ModifyPasswordStep2ViewController () {
    
    NSString *mobileNbr;
    NSString *newPaw;
    
}



@end

@implementation BMSQ_ModifyPasswordStep2ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
//    [self.navigationItem setTitle:@"修改密码"];
    [self setNavTitle:@"修改密码"];
    [self setNavBackItem];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 20.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    
    
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15,20,tableView.frame.size.width-30,44)];
    btn.backgroundColor = [UIColor colorWithHexString:@"0xC5000A"];
    btn.layer.cornerRadius = 4.0f;
    btn.titleLabel.font = [UIFont systemFontOfSize:17.f];
    [btn setTitle:NSLocalizedString(@"确定", @"确定") forState:UIControlStateNormal];
    [btn setTitle:NSLocalizedString(@"确定", @"确定") forState:UIControlStateHighlighted];
    [btn addTarget:self action:@selector(setupBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    btn.showsTouchWhenHighlighted = YES;
    [v addSubview:btn];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 130.f;
}

- (void)setupBtnClicked
{
    [self.view endEditing:YES];
    NSString *userStr = _pwdTextField.text;
    if(userStr.length<6||userStr.length>20){
        CSAlert(@"请输入6-20位密码");
        return;
    }else{
        newPaw =_pwdTextField.text;
 
    }
    
    
    [self httpModifyRequest];
}


/**
 *  FixMe
 */
- (void)httpModifyRequest
{
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];

    [self initJsonPrcClient:@"0"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[uD objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];
    [params setObject:[uD objectForKey:@"password"] forKey:@"originalPwd"];
    [params setObject:[MD5 MD5Value:newPaw] forKey:@"newPwd"];
    [params setObject:@"0" forKey:@"type"];
    
    NSString* vcode = [gloabFunction getSign:@"updatePwd" strParams:[uD objectForKey:@"mobileNbr"] ];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

   
    [self.jsonPrcClient invokeMethod:@"updatePwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"修改成功,请重新登录");
                
                UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
                BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
                BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
                
                [self presentViewController:nav animated:YES completion:^{
                    
                }];
                
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 60000:
                CSAlert(@"请输入手机号码");
                break;
            case 60001:
                CSAlert(@"手机号码不正确");
                break;
            case 60010:
                CSAlert(@"请输入密码");
                break;
            case 80011:
                CSAlert(@"验证码错误");
                break;
            case 20207:
                CSAlert(@"用户不存在");
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(@"错误错误");
        
    }];
}



@end
