//
//  BMSQ_AddBankSureController.m
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AddBankSureController.h"
#import "SVProgressHUD.h"
#import "ResCarPotocolViewController.h"

#import "BMSQ_setMyCardViewController.h"

@interface BMSQ_AddBankSureController ()
{
    UIButton *yzmButton;
    UIButton *verificationButton;
    UIButton* btn_agree;
    NSTimer *timer;
    int timeout;
}
@property (nonatomic, strong)UITextField *yzmField;

@property (nonatomic, strong)NSString *valCode;

@end

@implementation BMSQ_AddBankSureController

@synthesize phoneNumber;
@synthesize orderNbr;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"短信验证码"];
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 15, APP_VIEW_WIDTH - 20, 50)];
    label.numberOfLines = 0;
    
    [label setFont:[UIFont systemFontOfSize:13]];
    label.textColor = UICOLOR(87, 87, 87, 1.0);
    NSString* tmpPhone = [phoneNumber stringByReplacingCharactersInRange:NSMakeRange(3, 4) withString:@"****"];
    NSMutableAttributedString *str =[[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"您正在开通工银惠支付并绑定验证,验证码已由95588发送至您预留的手机: %@,请按提示进行操作,可通过工行网上银行设置预留手机号",tmpPhone]];
    [str addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(str.length-16,16)];
    
    label.attributedText = str;
    [self.view addSubview:label];
    
    UIView *smsView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 65, APP_VIEW_WIDTH, 43)];
    smsView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:smsView];
    
    self.yzmField = [[UITextField alloc]initWithFrame:CGRectMake(10, 0,180, 43)];
    self.yzmField.backgroundColor = [UIColor whiteColor];
    self.yzmField.placeholder = @"请输入短信验证码";
    self.yzmField.font = [UIFont systemFontOfSize:16];
    self.yzmField.keyboardType = UIKeyboardTypeNumberPad;
    self.yzmField.delegate = self;
    [smsView addSubview:self.yzmField];
    
    yzmButton = [UIButton buttonWithType:UIButtonTypeCustom];
    yzmButton.enabled = YES;
    yzmButton.backgroundColor = [UIColor whiteColor];
    yzmButton.layer.cornerRadius = 2.0;
    yzmButton.layer.borderWidth = 1.0;
    yzmButton.layer.borderColor = UICOLOR(136, 136, 136, 1.0).CGColor;
    yzmButton.frame = CGRectMake(200, 5, APP_VIEW_WIDTH -210, 33);
    yzmButton.titleLabel.font =[UIFont systemFontOfSize:14];
    [yzmButton setTitle:@"获取验证码" forState:UIControlStateNormal];
    [yzmButton setTitleColor:UICOLOR(79, 79, 79, 1.0) forState:UIControlStateNormal];
    [yzmButton addTarget:self action:@selector(yzmButtonClick) forControlEvents:UIControlEventTouchUpInside];
    [smsView addSubview:yzmButton];
    
    verificationButton = [UIButton buttonWithType:UIButtonTypeCustom];
    verificationButton.frame = CGRectMake(15, APP_VIEW_ORIGIN_Y + 148, APP_VIEW_WIDTH - 30, 44);
    verificationButton.layer.cornerRadius = 3;
    verificationButton.layer.masksToBounds = YES;
    verificationButton.titleLabel.font =[UIFont systemFontOfSize:14];
    [verificationButton setTitle:@"确定" forState:UIControlStateNormal];
    [verificationButton setBackgroundColor:UICOLOR(182, 0, 12, 1.0)];
    [verificationButton addTarget:self action:@selector(verificationButtonClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:verificationButton];
    
    btn_agree = [[UIButton alloc]initWithFrame:CGRectMake(15, APP_VIEW_ORIGIN_Y + 118, 16, 16)];
    [btn_agree setBackgroundImage:[UIImage imageNamed:@"iv_select"] forState:UIControlStateNormal];
    [btn_agree setBackgroundImage:[UIImage imageNamed:@"iv_notSelect"] forState:UIControlStateSelected];
    [btn_agree addTarget:self action:@selector(btnAgreeClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btn_agree];
    
    UIButton* lbMsg = [[UIButton alloc]initWithFrame:CGRectMake(35, APP_VIEW_ORIGIN_Y+115, 250, 20)];
    [lbMsg setTitle:@"《同意开通工银惠支付并绑定银行卡》" forState:UIControlStateNormal];
    [lbMsg addTarget:self action:@selector(potoproclick) forControlEvents:UIControlEventTouchUpInside];
    lbMsg.titleLabel.font = [UIFont systemFontOfSize:13.f];
    [lbMsg setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    [self.view addSubview:lbMsg];
    
    
}
-(void)potoproclick{
    RRC_webViewController *VC = [[RRC_webViewController alloc]init];
    VC.navtitle = @"绑卡协议";
//    VC.requestUrl = @"http://baomi.suanzi.cn/Api/Browser/onlinePayProtocol";
    VC.requestUrl = [NSString stringWithFormat:@"%@/Browser/onlinePayProtocol",H5_URL];
    VC.isHidenNav = NO;
    [self.navigationController pushViewController:VC animated:YES];
    
}
- (void)btnAgreeClick
{
    btn_agree.selected = !btn_agree.selected;
}
#pragma mark --   获取验证码按钮点击  --
-(void)yzmButtonClick
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
   
    [params setObject:self.orderNbr forKey:@"orderNbr"];
    
    NSString* vcode = [gloabFunction getSign:@"getSignCardValCode" strParams:self.orderNbr];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getSignCardValCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
 
        [SVProgressHUD dismiss];
        NSString *str = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if([str intValue] == 50000){
            [wself showAlertView:@"验证码已发送，请查收"];
            wself.valCode = [responseObject objectForKey:@"valCode"];
            wself.yzmField.text = [NSString stringWithFormat:@"%@",wself.valCode];
            
        }else{
            
             [wself showAlertView:[NSString stringWithFormat:@"错误[%@]",[responseObject objectForKey:@"retmsg"]]];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {

        [SVProgressHUD showErrorWithStatus:@"网络错误，请求超时"];
    }];
    
    
    timeout = 60;
    yzmButton.backgroundColor = UICOLOR(245, 245, 245, 1.0);
    [yzmButton setUserInteractionEnabled:NO];
    
    NSDate *scheduledTime = [NSDate dateWithTimeIntervalSinceNow:0];
    timer = [[NSTimer alloc] initWithFireDate:scheduledTime
                                     interval:1
                                       target:self
                                     selector:@selector(countdown)
                                     userInfo:nil
                                      repeats:YES];
    
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
}

-(void)countdown{
    NSString *downSecondStr = [NSString stringWithFormat:@"重新获取(%d)",timeout];
    [yzmButton setTitle:downSecondStr forState:UIControlStateNormal];
    
    if (timeout == 0) {
        [timer invalidate];
        [yzmButton setTitleColor:UICOLOR(180, 180, 180, 1.0) forState:UIControlStateNormal];
        yzmButton.backgroundColor = [UIColor clearColor];//UICOLOR(73, 113, 183, 1.0);
        yzmButton.userInteractionEnabled =YES;
        [yzmButton setTitle:@"获取验证码" forState:UIControlStateNormal];
        return;
    }
    timeout--;
}
#pragma mark --确定--
-(void)verificationButtonClick
{
    if (btn_agree.selected)
    {
        [self showAlertView:@"请先同意开通工银惠支付协议"];
        return;
    }

    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];

    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:orderNbr forKey:@"orderNbr"];
    [params setObject:self.yzmField.text forKey:@"valCode"];   //验证码
    
    NSString* vcode = [gloabFunction getSign:@"signBankAccount" strParams:orderNbr];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [SVProgressHUD showWithStatus:@""];
    [self.jsonPrcClient invokeMethod:@"signBankAccount" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if ([[responseObject objectForKey:@"code"] integerValue] ==50000)
        {

            [SVProgressHUD showSuccessWithStatus:@"绑定银行卡成功"];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"addCard" object:nil];
            [wself getBankAccountList];
            
            
            
        }
        else
        {
            [self showAlertView:[responseObject objectForKey:@"retmsg"]];
        }
        

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {

        [SVProgressHUD showErrorWithStatus:@"网络请求失败"];
    }];
}

 //刷新我的银行卡页面数据
-(void)getBankAccountList{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:@"1" forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getBankAccountList" strParams:[NSString stringWithFormat:@"%@",[gloabFunction getUserCode]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [SVProgressHUD showWithStatus:@""];
    [self.jsonPrcClient invokeMethod:@"getBankAccountList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"bankAccountList"]) {
            NSArray *array = [responseObject objectForKey:@"bankAccountList"];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"refreshMyCard" object:array];
            
        }
//        NSUserDefaults *useD = [NSUserDefaults standardUserDefaults];
//        NSString *str = [useD objectForKey:APP_USER_FREEVALCODEPAY];
        
//        if([str isEqualToString:@"0"]){
//            BMSQ_setMyCardViewController *vc = [[BMSQ_setMyCardViewController alloc]init];
//            vc.formvc = self.formvc;
//            vc.
//            [self.navigationController pushViewController:vc animated:YES];
//
//        }else{
            NSArray *array = self.navigationController.viewControllers;
            [self.navigationController popToViewController:[array objectAtIndex:self.formvc-1] animated:YES];
//        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"网络请求失败"];
    }];
}



-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    return YES;
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    
    if (![self.yzmField isExclusiveTouch]) {
        
        [self.yzmField resignFirstResponder];
    }
}


@end
