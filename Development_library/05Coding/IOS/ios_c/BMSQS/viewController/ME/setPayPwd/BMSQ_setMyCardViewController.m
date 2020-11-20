//
//  BMSQ_setMyCardViewController.m
//  BMSQC
//
//  Created by liuqin on 15/12/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_setMyCardViewController.h"
#import "BMSQ_InputPwdView.h"
#import "MD5.h"



#import "ZCTradeView.h"



#import "MobClick.h"
#import "ZCTradeKeyboard.h"
#import "ZCTradeInputView.h"

#import "UIColor+Tools.h"


@interface BMSQ_setMyCardViewController ()<ZCTradeInputViewDelegate>

@property (nonatomic,strong)UIButton *button;

@property (nonatomic, strong)BMSQ_InputPwdView *inputPwdView;


/** 键盘 */
@property (nonatomic, strong) ZCTradeKeyboard *keyboard;
/** 输入框 */
@property (nonatomic, strong) ZCTradeInputView *inputView;


/** 键盘 */
@property (nonatomic, strong) ZCTradeKeyboard *thr_keyboard;
/** 输入框 */
@property (nonatomic, strong) ZCTradeInputView *thr_inputView;


@property (nonatomic, strong)UIScrollView *sc;
@property (nonatomic, strong)UIView *fristView;
@property (nonatomic, strong)UIView *secView;
@property (nonatomic, strong)UIView *thrView;



@property (nonatomic, strong)NSString *userMobile;
@property (nonatomic, strong)NSMutableString *pw1;
@property (nonatomic, strong)NSMutableString *pw2;

@property (nonatomic, strong)UIButton *subButton; //验证按扭
@property (nonatomic, strong)UITextField *codeTextField;
@property (nonatomic, strong)UIButton *getButton; //获取按扭

@end

@implementation BMSQ_setMyCardViewController



- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"setMyCard"];// #import "MobClick.h"
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"setMyCard"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:self.myTitle];
    self.view.backgroundColor = UICOLOR(240, 240, 240, 1);
    
    self.pw1 = [[NSMutableString alloc]init];
    self.pw2 = [[NSMutableString alloc]init];

    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(verification:) name:@"overNum" object:nil];

    
    [self n_set];

  

}

-(void)n_set{
    
    
    self.sc = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    [self.view addSubview:self.sc];
    
    self.fristView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, self.sc.frame.size.height)];
    self.fristView.backgroundColor = [UIColor clearColor];
    [self.sc addSubview:self.fristView];
    
    self.secView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, self.sc.frame.size.height)];
    [self.sc addSubview:self.secView];
    self.secView.backgroundColor = [UIColor clearColor];
    
    self.thrView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH*2, 0, APP_VIEW_WIDTH, self.sc.frame.size.height)];
    [self.sc addSubview:self.thrView];
    self.thrView.backgroundColor = [UIColor clearColor];
    
    
    self.sc.contentSize = CGSizeMake(APP_VIEW_WIDTH*3, self.sc.frame.size.height);
    self.sc.scrollEnabled = NO;
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 30)];
    label.font = [UIFont systemFontOfSize:13];
    NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
    self.userMobile = [userD objectForKey:APP_USER_MOBILENBR];
    NSString* tmpPhone= [self.userMobile stringByReplacingCharactersInRange:NSMakeRange(3, 4) withString:@"****"];
    NSMutableAttributedString  *attStr= [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"请输入手机号%@收到的短信验证码",tmpPhone]];
     [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(6,11)];
    label.attributedText = attStr ;
    [self.fristView addSubview:label];
    
    
    self.codeTextField = [[UITextField alloc]initWithFrame:CGRectMake(10, 50, APP_VIEW_WIDTH/2, 30)];
    self.codeTextField.layer.borderColor = [APP_TEXTCOLOR CGColor];
    self.codeTextField.layer.borderWidth = 1;
    self.codeTextField.placeholder = @"输入验证码";
    self.codeTextField.font = [UIFont systemFontOfSize:13];
    self.codeTextField.textAlignment = NSTextAlignmentCenter;
    self.codeTextField.backgroundColor = [UIColor clearColor];
    [self.codeTextField addTarget:self action:@selector(codeTextFieldChange:) forControlEvents:UIControlEventEditingChanged];
    self.codeTextField.keyboardType = UIKeyboardTypeNumberPad;
    [self.fristView addSubview:self.codeTextField];
    
    self.getButton = [[UIButton alloc]initWithFrame:CGRectMake(self.codeTextField.frame.origin.x+self.codeTextField.frame.size.width+20, self.codeTextField.frame.origin.y, APP_VIEW_WIDTH-(self.codeTextField.frame.origin.x+self.codeTextField.frame.size.width+20)-10, 30)];
    self.getButton.backgroundColor = APP_NAVCOLOR;
    [self.getButton setTitle:@"获取验证码" forState:UIControlStateNormal];
    [self.getButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.getButton.titleLabel.font = [UIFont systemFontOfSize:12.f];
    [self.fristView addSubview:self.getButton];
    [self.getButton addTarget:self action:@selector(clickGetButton) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
    self.subButton = [[UIButton alloc]initWithFrame:CGRectMake(10, self.codeTextField.frame.origin.y+50, APP_VIEW_WIDTH-20, 30)];
    [self.subButton setTitle:@"验证" forState:UIControlStateNormal];
    [self.subButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.subButton.backgroundColor = APP_TEXTCOLOR;
    self.subButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [self.fristView addSubview:self.subButton];
    self.subButton.enabled = NO;

    [self.subButton addTarget:self action:@selector(clickSubButton) forControlEvents:UIControlEventTouchUpInside];
    
    UILabel *bottomLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.subButton.frame.origin.x, self.subButton.frame.origin.y+self.subButton.frame.size.height+10, 300, 30)];
    bottomLabel.font = [UIFont systemFontOfSize:12.f];
    bottomLabel.textColor = APP_NAVCOLOR;
    bottomLabel.text = @"*300元内支付免银行短信验证";
    [self.fristView addSubview:bottomLabel];
    
    
    
    
    [self setN_secView];
    
    
    
    


    
}

-(void)timerFireMethod{
    
   
    

    __block int timeoutS = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timers = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timers, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timers, ^{
        if (timeoutS <= 0){
            dispatch_source_cancel(timers);
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.getButton setTitle:@"再次获取" forState:UIControlStateNormal];
                [self.getButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
                self.getButton.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeoutS];
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.getButton setTitle:strTime forState:UIControlStateNormal];
                [self.getButton setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                self.getButton.userInteractionEnabled = NO;
            });
            timeoutS--;
        }
    });
    dispatch_resume(timers);
}


-(void)codeTextFieldChange:(UITextField *)textField{
    if (textField.text.length>=6) {
        textField.text = [textField.text substringWithRange:NSMakeRange(0, 6)];
        
        self.subButton.backgroundColor = APP_NAVCOLOR;
        self.subButton.enabled = YES;
        
    }
}

#pragma mark 获取验证码
-(void)clickGetButton{
  
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"0"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.userMobile forKey:@"mobileNbr"];
    [params setObject:@"spp" forKey:@"action"];
    [params setObject:@"c" forKey:@"appType"];
    
   
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resut = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue];
        if (resut == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"设置成功"];
            [weakSelf timerFireMethod];

            
        }else{
            [SVProgressHUD showErrorWithStatus:@"登录密码不正确"];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
     
 
   
}

-(void)verification:(NSNotification *)notification{
    NSArray *array = notification.object;
    ZCTradeInputView *tradeInputView = [array objectAtIndex:1];
    

    int i =(int) tradeInputView.tag;
    if (i==100) {
        [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveEaseInOut animations:^{
            self.sc.contentOffset = CGPointMake(APP_VIEW_WIDTH*2, 0);
            NSArray *array = tradeInputView.nums;
            [self.pw1 appendString: [array componentsJoinedByString:@""]];
        } completion:^(BOOL finished) {
            [self setN_thrView];
            self.thr_keyboard.frame = CGRectMake(0, APP_VIEW_HEIGHT-self.keyboard.height-APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, self.keyboard.frame.size.height);
        }];
    }else{
        NSArray *array = tradeInputView.nums;
        [self.pw2 appendString: [array componentsJoinedByString:@""]];
        if ([self.pw1 isEqualToString:self.pw2]) {
            
            [self setPayPwd:self.pw1];
        }else{
            
            [SVProgressHUD showErrorWithStatus:@"两次支付密码不相同"];
            [self.pw1 deleteCharactersInRange:NSMakeRange(0, 6)];
            [self.pw2 deleteCharactersInRange:NSMakeRange(0, 6)];
            
            __weak typeof(self) weakSelf =  self;
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.thr_inputView deleteAllNum];
                weakSelf.thr_inputView.title = @"请设置惠圈支付密码,用于支付验证";
                weakSelf.thr_inputView.tag = 100;
            });

            
        }
        
        
    }
    
}


-(void)setN_secView{
    
    
    self.inputView = [[ZCTradeInputView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
    [self.secView addSubview:self.inputView];
    self.inputView.okBtn.hidden = YES;
    self.inputView.cancleBtn.hidden = YES;
    self.inputView.isModify = YES;
    self.inputView.tag = 100;
    
    self.inputView.title = @"请设置惠圈支付密码,用于支付验证";
    self.inputView.height = ZCScreenWidth * 0.5625;
    self.inputView.y = 0;
    self.inputView.width = APP_VIEW_WIDTH;
    self.inputView.x = 0;
    
    
    self.keyboard = [[ZCTradeKeyboard alloc]init];
    [self.secView addSubview:self.keyboard];
    self.keyboard.backgroundColor = [UIColor clearColor];
    
    self.keyboard.x = 0;
    self.keyboard.y =APP_VIEW_HEIGHT;
    self.keyboard.width = ZCScreenWidth;
    self.keyboard.height = ZCScreenWidth * 0.65;
    

    
}

-(void)setN_thrView{
    
    
    self.thr_inputView = [[ZCTradeInputView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
    [self.thrView addSubview:self.thr_inputView];
    self.thr_inputView.okBtn.hidden = YES;
    self.thr_inputView.cancleBtn.hidden = YES;
    self.thr_inputView.isModify = YES;
    self.thr_inputView.tag = 200;
    
    self.thr_inputView.title = @"请再次输入惠圈支付密码";
    self.thr_inputView.height = ZCScreenWidth * 0.5625;
    self.thr_inputView.y = 0;
    self.thr_inputView.width = APP_VIEW_WIDTH;
    self.thr_inputView.x = 0;
    
    
    self.thr_keyboard = [[ZCTradeKeyboard alloc]init];
    [self.thrView addSubview:self.thr_keyboard];
    self.thr_keyboard.backgroundColor = [UIColor clearColor];
    
    self.thr_keyboard.x = 0;
    self.thr_keyboard.y =APP_VIEW_HEIGHT;
    self.thr_keyboard.width = ZCScreenWidth;
    self.thr_keyboard.height = ZCScreenWidth * 0.65;
    
    
}
#pragma mark  验证 验证码
-(void)clickSubButton{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.userMobile forKey:@"mobileNbr"];
    [params setObject:self.codeTextField.text forKey:@"valCode"];
    
    NSString* vcode = [gloabFunction getSign:@"valSSPValCode" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf =  self;
    [self.jsonPrcClient invokeMethod:@"valSSPValCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        int resut = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue];
        [SVProgressHUD dismiss];

        if (resut == 1) {
            [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveEaseInOut animations:^{
                
                [weakSelf.codeTextField resignFirstResponder];
                weakSelf.keyboard.frame = CGRectMake(0, APP_VIEW_HEIGHT-self.keyboard.height-APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, self.keyboard.frame.size.height);
                weakSelf.sc.contentOffset = CGPointMake(APP_VIEW_WIDTH, 0);
                
                
            } completion:^(BOOL finished) {
                
            }];
            
        }else{
            
            [SVProgressHUD showErrorWithStatus:@"验证码不正确"];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
  
    
}

#pragma mark 确认支付密码

-(void)setPayPwd:(NSString *)pw{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    NSString *payPwd = [MD5 MD5Value:pw];
    
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:payPwd forKey:@"payPwd"];
    [params setObject:payPwd forKey:@"confirmPayPwd"];
    
    NSString* vcode = [gloabFunction getSign:@"setPayPwd" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf =  self;
    [self.jsonPrcClient invokeMethod:@"setPayPwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        int resut = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue];
        [SVProgressHUD dismiss];
        
        if (resut == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"设置成功"];
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }else{
            NSString *errMeg = [[NSString alloc]initWithFormat:@"设置失败[code = %d]",resut];
            [SVProgressHUD showErrorWithStatus:errMeg];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
}




@end
