//
//  BMSQ_SMSaddViewController.m
//  BMSQS
//
//  Created by liuqin on 16/2/24.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_SMSaddViewController.h"

#import "SVProgressHUD.h"
#import "UIColor+Tools.h"
@interface BMSQ_SMSaddViewController ()<UITextFieldDelegate,UIAlertViewDelegate>

@property (nonatomic, strong)UITextField *nameText;//姓名
@property (nonatomic, strong)UITextField *numberText;//手机号
@property (nonatomic, strong)UITextField *yzmText;//验证码
@property (nonatomic, strong)UIButton *yzmButton;//验证码


@end




@implementation BMSQ_SMSaddViewController

-(void)viewDidLoad{
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"短信设置"];
    
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"确定" forState:0];
    btnback.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [btnback addTarget:self action:@selector(clickRight:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btnback];
    
    //第一行
    UIView *firView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH, 50)];
    firView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:firView];
    UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 50, firView.frame.size.height)];
    nameLabel.textColor = APP_TEXTCOLOR;
    nameLabel.font = [UIFont systemFontOfSize:15.f];
//    nameLabel.textAlignment = NSTextAlignmentCenter;
    nameLabel.text = @"姓名";
    [firView addSubview:nameLabel];
    
    self.nameText = [[UITextField alloc]initWithFrame:CGRectMake(nameLabel.frame.origin.x+nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-nameLabel.frame.size.width-20, nameLabel.frame.size.height)];
    self.nameText.placeholder = @"请输入员工姓名";
    self.nameText.textColor = APP_TEXTCOLOR;
    self.nameText.font = [UIFont systemFontOfSize:15.f];
    self.nameText.returnKeyType = UIReturnKeyDone;
    self.nameText.delegate = self;
    [firView addSubview:self.nameText];
    
    
    
    
    UIView *secView = [[UIView alloc]initWithFrame:CGRectMake(0, firView.frame.origin.y+firView.frame.size.height+10, APP_VIEW_WIDTH, 50)];
    secView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:secView];
    UILabel *mobileLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 50, firView.frame.size.height)];
    mobileLabel.textColor = APP_TEXTCOLOR;
    mobileLabel.font = [UIFont systemFontOfSize:15.f];
//    mobileLabel.textAlignment = NSTextAlignmentCenter;
    mobileLabel.text = @"手机号";
    [secView addSubview:mobileLabel];
    self.numberText = [[UITextField alloc]initWithFrame:CGRectMake(nameLabel.frame.origin.x+nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-nameLabel.frame.size.width-20-120, nameLabel.frame.size.height)];
    self.numberText.placeholder = @"请输入员工手机号";
    self.numberText.textColor = APP_TEXTCOLOR;
    self.numberText.font = [UIFont systemFontOfSize:15.f];
    self.numberText.keyboardType = UIKeyboardTypePhonePad;
    [secView addSubview:self.numberText];
    
    self.yzmButton = [[UIButton alloc]initWithFrame:CGRectMake(self.numberText.frame.origin.x+self.numberText.frame.size.width+10, 10, 90, 30)];
    [self.yzmButton setTitle:@"获取验证码" forState:UIControlStateNormal];
    [self.yzmButton setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    self.yzmButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [secView addSubview:self.yzmButton];
    self.yzmButton.layer.borderColor = [APP_TEXTCOLOR CGColor];
    self.yzmButton.layer.borderWidth = 0.3;
    self.yzmButton.layer.cornerRadius = 4;
    self.yzmButton.layer.masksToBounds = YES;
    [self.yzmButton addTarget:self action:@selector(clickYZM) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    UIView *thrView = [[UIView alloc]initWithFrame:CGRectMake(0,10+secView.frame.origin.y+secView.frame.size.height, APP_VIEW_WIDTH, 50)];
    thrView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:thrView];
    
    
    UILabel *yzmLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 50, firView.frame.size.height)];
    yzmLabel.textColor = APP_TEXTCOLOR;
    yzmLabel.font = [UIFont systemFontOfSize:15.f];
//    yzmLabel.textAlignment = NSTextAlignmentCenter;
    yzmLabel.text = @"验证码";
    [thrView addSubview:yzmLabel];
    
    self.yzmText = [[UITextField alloc]initWithFrame:CGRectMake(nameLabel.frame.origin.x+nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-nameLabel.frame.size.width-20, nameLabel.frame.size.height)];
    self.yzmText.placeholder = @"请输入收到的验证码";
    self.yzmText.textColor = APP_TEXTCOLOR;
    self.yzmText.font = [UIFont systemFontOfSize:15.f];
    self.yzmText.returnKeyType = UIReturnKeyDone;
    self.yzmText.delegate = self;
    [thrView addSubview:self.yzmText];
    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

#pragma mark --确定添加--
-(void)clickRight:(UIButton *)button{
    if (self.nameText.text.length == 0) {
        UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入姓名" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        alerView.tag = 99;
        [alerView show];
        return;
    }else if(!(self.numberText.text.length == 11)){
        UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"输入的手机号不对" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        alerView.tag = 100;
        [alerView show];
        return;
    }else if (self.yzmText.text.length == 0){
        UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"输入的验证码" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        alerView.tag = 101;
        [alerView show];
        return;
    }
    
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.numberText.text forKey:@"mobileNbr"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:self.nameText.text forKey:@"staffName"];
    [params setObject:self.yzmText.text forKey:@"validateCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];
    
    NSString* vcode = [gloabFunction getSign:@"addMRecipient" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"addMRecipient" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSDictionary *dic = responseObject;
        if ([[dic objectForKey:@"code"]intValue]==50000) {
            [[NSNotificationCenter defaultCenter]postNotificationName:@"refreshContat" object:nil];
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }else{
            NSString *message;
            int code = [[dic objectForKey:@"code"]intValue];
            switch (code) {
                case 60000:
                {
                 message = @"手机号不能为空";
                }
                    break;
                case 60001:
                {
                    message = @"手机号格式错误";
                }
                    break;
                case 60003:
                {
                    message = @"手机号重复";
                }
                    break;
                case 80012:
                {
                    message = @" 验证码不能为空";
                }
                    break;
                case 80011:
                {
                    message = @"验证码错误";
                }
                    break;
                case 87001:
                {
                    message = @"超过设置数量";
                }
                    break;
                    
                default:
                {
                     message = [NSString stringWithFormat:@"添加错误 [code=%d]",code];
                }
                    break;
            }
            
            
            UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:message delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alerView show];

            
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"asdf");
        
    }];
    
}
#pragma mark 获取验证码
-(void)clickYZM{
    
    if (!(self.numberText.text.length == 11)) {
        UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"输入的手机号不对" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        alerView.tag = 100;
        [alerView show];
        return;
    }
    
    
    [self time];
    
    [self initJsonPrcClient:@"0"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.numberText.text forKey:@"mobileNbr"];
    [params setObject:@"mr" forKey:@"action"]; // r 注册、激活 f 找回密码 mr 添加接收短信的人
    [params setObject:@"s" forKey:@"appType"];  // s 商家端 c 顾客端
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode]};
    [params setObject:dic forKey:@"extra"];  //

    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"asdf");
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"asdf");

    }];

}
//更新时间
-(void)time{
    __block int timeout = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timer, ^{
        if (timeout <= 0){
            dispatch_source_cancel(timer);
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.yzmButton setTitle:@"再次获取" forState:UIControlStateNormal];
                [self.yzmButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                self.yzmButton.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeout];
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.yzmButton setTitle:strTime forState:UIControlStateNormal];
                [self.yzmButton setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                self.yzmButton.userInteractionEnabled = NO;
            });
            timeout--;
        }
    });
    dispatch_resume(timer);
}

#pragma mark UIALERVIEWDELEGATE
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag ==99) {
        [self.nameText becomeFirstResponder];
    }else if (alertView.tag ==100){
        self.numberText.text=@"";
        [self.numberText becomeFirstResponder];
    }else if (alertView.tag ==101){
        [self.yzmText becomeFirstResponder];
    }
    
}


@end
