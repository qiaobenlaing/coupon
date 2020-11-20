//
//  BMSQ_ModifyPasswordViewController.m
//  BMSQS
//
//  Created by lxm on 15/9/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ModifyPasswordViewController.h"
#import "MD5.h"
#import "BMSQ_ForgetPwdViewController.h"

@interface BMSQ_ModifyPasswordViewController ()

@end

@implementation BMSQ_ModifyPasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationItem setTitle:@"验证原密码"];
    
    [self setNavTitle:@"验证原密码"];
    [self setNavBackItem];
    
}



-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.section==0){
       if(indexPath.row==0){
            cellIdentifier = @"PwdTableViewCell";
        }
    }
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }
    
    if(indexPath.section==0){
        setCell.selectionStyle = UITableViewCellSelectionStyleNone;
        if(indexPath.row==0){
            _pwdTextField = (UITextField *)[setCell.contentView viewWithTag:102];
            if(_isSeePwd)
                _pwdTextField.secureTextEntry = NO;
            else
                _pwdTextField.secureTextEntry = YES;
        }
        
    }
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    
    UILabel *tipLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 20, tableView.frame.size.width, 20)];
    [tipLabel setText:@"为了您的帐号安全，修改密码前请填写原密码"];
    [tipLabel setFont:[UIFont systemFontOfSize:11.f]];
    [v addSubview:tipLabel];
    [tipLabel setTextAlignment:NSTextAlignmentCenter];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 50.f;
}


- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    
    
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"忘记密码？"];
    [str addAttribute:NSForegroundColorAttributeName value:[UIColor blueColor] range:NSMakeRange(0,str.length)];
    
    UIButton *btnAgree = [[UIButton alloc] initWithFrame:CGRectMake(0,75,tableView.frame.size.width-15,44)];
    btnAgree.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [btnAgree setBackgroundColor:[UIColor clearColor]];
    [btnAgree setTitleColor:[UIColor colorWithHexString:@"0x323232"] forState:UIControlStateNormal];
    [btnAgree setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    [btnAgree setAttributedTitle:str forState:UIControlStateNormal];
    
    [btnAgree addTarget:self action:@selector(forgetPwdBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
    [v addSubview:btnAgree];
    
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

- (void)forgetPwdBtnClicked:(UIButton *)sender
{
    /**
     *  FixMe
     *
     *  @return <#return value description#>
     */
    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
    BMSQ_ForgetPwdViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_ForgetPwd"];
    vc.isShowNav = YES;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)setupBtnClicked
{
    /**
     *  FixMe
     */


    
    
    
    [self.view endEditing:YES];
    NSString *userStr = [_pwdTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(userStr.length < 6 || userStr.length > 20){
        CSAlert(@"请输入6-20位密码");
        return;
    }else{
        
        NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
        
//        NSLog(@"md----->%@",[MD5 MD5Value:_pwdTextField.text]);
//        NSLog(@"ud----->%@",[uD objectForKey:@"password"]);
        
        if ([[MD5 MD5Value:_pwdTextField.text] isEqualToString:[uD objectForKey:@"password"]]) {
            UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"DecorationShop" bundle:nil];
            BMSQ_ModifyPasswordViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_ModifyPasswordStep2"];

            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            
        }else {
            CSAlert(@"密码错误");
            
        }
    }

//    [self httpVerifyRequest];

    
}

- (IBAction)seePwdBtnClicked:(id)sender
{
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.isSelected;
    _isSeePwd = btn.isSelected;
    [_tableView reloadData];
}

/**
 *  FixMe
 */
- (void)httpVerifyRequest
{
    [self initJsonPrcClient:@"0"];
    NSDictionary *dic = @{@"mobileNbr":[_inputDic objectForKey:@"mobileNbr"],
                          @"originalPwd":[MD5 MD5Value:[_inputDic objectForKey:@"originalPwd"]],
                          @"newPwd":[MD5 MD5Value:[_inputDic objectForKey:@"newPwd"]],
                          @"type":@"0"};
    [self.jsonPrcClient invokeMethod:@"updatePwd" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"修改成功");
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
    }];
}




@end
