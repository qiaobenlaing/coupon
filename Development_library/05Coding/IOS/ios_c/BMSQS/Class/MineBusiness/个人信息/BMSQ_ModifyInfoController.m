//
//  BMSQ_ModifyInfoController.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ModifyInfoController.h"

@interface BMSQ_ModifyInfoController ()
{
    UITextField* tx_modifyInfo;
}

@end

@implementation BMSQ_ModifyInfoController

@synthesize insertPage;
@synthesize modifyInfo;
@synthesize mobileNbr;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setNavigationBar];
    [self setNavBackItem];
    
    if (insertPage == 0)
    {
        [self setNavTitle:@"修改昵称"];
    }
    else
    {
        [self setNavTitle:@"修改姓名"];
    }
    
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    
    tx_modifyInfo = [[UITextField alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 15, APP_VIEW_WIDTH, 44)];
    tx_modifyInfo.backgroundColor = [UIColor whiteColor];
    tx_modifyInfo.leftViewMode = UITextFieldViewModeAlways;
    tx_modifyInfo.returnKeyType = UIReturnKeyDone;
    tx_modifyInfo.font = [UIFont systemFontOfSize:14];
    tx_modifyInfo.delegate = self;
    tx_modifyInfo.text = modifyInfo;
    
    UILabel* lb_leftView = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 65, 44)];
    lb_leftView.backgroundColor = [UIColor clearColor];
    lb_leftView.font = [UIFont systemFontOfSize:14];
    UILabel* lb_msg = [[UILabel alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 65, APP_VIEW_WIDTH-30, 30)];
    if (insertPage == 0)
    {
        lb_leftView.text = @"    昵 称";
        
        lb_msg.text = @"4-30个字符，可有中英文字母、数字、“-”组成";
        
    }
    else
    {
        lb_leftView.text = @"    姓 名";
        lb_msg.text = @"不能超过10个汉子或者20个英文字符";
    }
    
    lb_msg.textColor = UICOLOR(140, 140, 140, 1.0);
    lb_msg.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:lb_msg];
    tx_modifyInfo.leftView = lb_leftView;
    [self.view addSubview:tx_modifyInfo];
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    [btnRightBar addTarget:self action:@selector(btnRightClick) forControlEvents:UIControlEventTouchUpInside];
    [btnRightBar setTitle:@"提交" forState:UIControlStateNormal];
    [self setNavRightBarItem:btnRightBar];
    
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

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [tx_modifyInfo resignFirstResponder];
}

- (void)updateUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:mobileNbr forKey:@"mobileNbr"];
    if (insertPage == 0)
    {
        [params setObject:tx_modifyInfo.text forKey:@"nickName"];
    }
    else
    {
        [params setObject:tx_modifyInfo.text forKey:@"realName"];
    }
    NSString* vcode = [gloabFunction getSign:@"updateUserInfo" strParams:mobileNbr];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updateUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([[responseObject objectForKey:@"code"] integerValue] == 50000)
        {
            [self showAlertView:@"修改成功"];
        }
        else
        {
            [self showAlertView:@"修改失败"];
        }
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (void)btnRightClick
{
    if (tx_modifyInfo.text == nil || tx_modifyInfo.text.length <= 0)
    {
        if (insertPage == 0)
        {
            [self showAlertView:@"请输入昵称"];
        }
        else
        {
            [self showAlertView:@"请输入名称"];
        }
        
        return;
    }
    
    [self updateUserInfo];
}

@end
