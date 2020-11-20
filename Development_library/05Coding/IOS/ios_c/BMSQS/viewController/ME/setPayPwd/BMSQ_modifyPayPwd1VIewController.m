//
//  BMSQ_modifyPayPwd1VIewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_modifyPayPwd1VIewController.h"

#import "ZCTradeInputView.h"
#import "ZCTradeKeyboard.h"

#import "MobClick.h"
#import "MD5.h"

@interface BMSQ_modifyPayPwd1VIewController ()

@property (nonatomic, strong)NSString *oldPwd;

@property (nonatomic, strong)NSMutableString *pw1;
@property (nonatomic, strong)NSMutableString *pw2;


@property (nonatomic, strong)ZCTradeInputView *thr_inputView;

@end

@implementation BMSQ_modifyPayPwd1VIewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"modifyPayPwd1"];// #import "MobClick.h"
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"modifyPayPwd1"];
}

-(void)viewDidLoad{
    
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"修改支付密码"];
    self.view.backgroundColor = UICOLOR(240, 240, 240, 1);
    self.pw1 = [[NSMutableString alloc]init];
    self.pw2 = [[NSMutableString alloc]init];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(verification:) name:@"overNum" object:nil];
    
    
    self.thr_inputView = [[ZCTradeInputView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 100)];
    self.thr_inputView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.thr_inputView];
    self.thr_inputView.tag = 100;
    
    self.thr_inputView.title = @"请输入原来的惠圈支付密码";
    self.thr_inputView.height = ZCScreenWidth * 0.5625;
    self.thr_inputView.y = APP_VIEW_ORIGIN_Y;
    self.thr_inputView.width = APP_VIEW_WIDTH;
    self.thr_inputView.okBtn.hidden = YES;
    self.thr_inputView.cancleBtn.hidden = YES;
    self.thr_inputView.x = 0;
    
    self.thr_inputView.isModify = YES;
    
    
    ZCTradeKeyboard *thr_keyboard = [[ZCTradeKeyboard alloc]init];
    [self.view addSubview:thr_keyboard];
    thr_keyboard.backgroundColor = [UIColor clearColor];
    
    thr_keyboard.x = 0;
    thr_keyboard.y =APP_VIEW_HEIGHT;
    thr_keyboard.width = ZCScreenWidth;
    thr_keyboard.height = ZCScreenWidth * 0.65;
    
    [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        
       thr_keyboard.frame = CGRectMake(0, APP_VIEW_HEIGHT-ZCScreenWidth * 0.65, APP_VIEW_WIDTH, ZCScreenWidth * 0.65);
        
        
    } completion:^(BOOL finished) {
        
    }];
    
}


#pragma mark 验证 支付码
-(void)validatePayPwd{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    NSString *payPwd = [MD5 MD5Value:self.oldPwd];
    
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:payPwd forKey:@"payPwd"];
    
    NSString* vcode = [gloabFunction getSign:@"validatePayPwd" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf =  self;
    [self.jsonPrcClient invokeMethod:@"validatePayPwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        int resut = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue];
        
        if (resut == 1) {
            [SVProgressHUD dismiss];

            [weakSelf.thr_inputView deleteAllNum];
             weakSelf.thr_inputView.title = @"请输入新的惠圈支付密码";
             weakSelf.thr_inputView.tag = 200;

        }else{
            
            [SVProgressHUD showErrorWithStatus:@"验证码不正确"];
            
            [weakSelf.thr_inputView deleteAllNum];
        
            
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

-(void)verification:(NSNotification *)notification{
    NSArray *array = notification.object;
    ZCTradeInputView *tradeInputView = [array objectAtIndex:1];
    
    int tag = (int)tradeInputView.tag;
    if (tag == 100) {
        NSArray *array = tradeInputView.nums;
        
        self.oldPwd = [array componentsJoinedByString:@""];
        [self validatePayPwd];
      
    }else if(tag == 200){
        __weak typeof(self) weakSelf =  self;
        
            dispatch_async(dispatch_get_main_queue(), ^{
                NSArray *array = tradeInputView.nums;
                [weakSelf.pw1 appendString: [array componentsJoinedByString:@""]];
                [weakSelf.thr_inputView deleteAllNum];
                weakSelf.thr_inputView.title = @"请再输入新的惠圈支付密码";
                weakSelf.thr_inputView.tag = 300;
            });
            
        
    }else if (tag == 300){
        NSArray *array = tradeInputView.nums;
        [self.pw2 appendString: [array componentsJoinedByString:@""]];
        if ([self.pw1 isEqualToString:self.pw2]) {
            
            [self setPayPwd:self.pw2];
        }else{
            [SVProgressHUD showErrorWithStatus:@"两次支付密码不相同"];
            [self.pw1 deleteCharactersInRange:NSMakeRange(0, 6)];
            [self.pw2 deleteCharactersInRange:NSMakeRange(0, 6)];
            
            __weak typeof(self) weakSelf =  self;

                dispatch_async(dispatch_get_main_queue(), ^{
                    [weakSelf.thr_inputView deleteAllNum];
                    weakSelf.thr_inputView.title = @"请再输入新的惠圈支付密码";
                    weakSelf.thr_inputView.tag = 200;
                });
                
        }
        
    }
    
}

#pragma mark 返回
/*
- (void)goBack{
    
    if (self.thr_inputView.tag == 100) {
        [self.navigationController popViewControllerAnimated:YES];
    }else if(self.thr_inputView.tag == 200){
        
        __weak typeof(self) weakSelf =  self;
        dispatch_async(dispatch_get_main_queue(), ^{
            [weakSelf.thr_inputView deleteAllNum];
            weakSelf.thr_inputView.title = @"请输入原来的惠圈支付密码";
            weakSelf.thr_inputView.tag = 100;
        });
        
    }else if (self.thr_inputView.tag ==300){
        
        __weak typeof(self) weakSelf =  self;
        dispatch_async(dispatch_get_main_queue(), ^{
            [weakSelf.thr_inputView deleteAllNum];
            weakSelf.thr_inputView.title = @"请再输入新的惠圈支付密码";
            weakSelf.thr_inputView.tag = 200;
        });
        
    }
    
}
*/

@end
