//
//  DCPaymentView.m
//  DCPayAlertDemo
//
//  Created by dawnnnnn on 15/12/9.
//  Copyright © 2015年 dawnnnnn. All rights reserved.
//

#import "DCPaymentView.h"
#import "UIColor+Tools.h"


#define TITLE_HEIGHT 46
#define PAYMENT_WIDTH [UIScreen mainScreen].bounds.size.width-80
#define PWD_COUNT 6
#define DOT_WIDTH 10
#define KEYBOARD_HEIGHT 216
#define KEY_VIEW_DISTANCE 100
#define ALERT_HEIGHT 200


@interface DCPaymentView ()<UITextFieldDelegate>
{
    NSMutableArray *pwdIndicatorArr;
}
@property (nonatomic, strong) UIView *paymentAlert, *inputView;
@property (nonatomic, strong) UIButton *closeBtn;
@property (nonatomic, strong) UIButton *backCardBtn;
@property (nonatomic, strong) UIButton *codeBtn;
@property (nonatomic, strong) UILabel *titleLabel, *line, *mobileNumLabel, *amountLabel;
@property (nonatomic, strong) UITextField *pwdTextField;

@end

@implementation DCPaymentView

- (instancetype)init {
    self = [super init];
    if (self) {
        self.frame = [UIScreen mainScreen].bounds;
        self.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:.3f];
        
        [self drawView];
    }
    return self;
}

- (void)drawView {
    if (!_paymentAlert) {
        _paymentAlert = [[UIView alloc]initWithFrame:CGRectMake(40, [UIScreen mainScreen].bounds.size.height-KEYBOARD_HEIGHT-KEY_VIEW_DISTANCE-ALERT_HEIGHT, [UIScreen mainScreen].bounds.size.width-80, ALERT_HEIGHT)];
        _paymentAlert.layer.cornerRadius = 5.f;
        _paymentAlert.layer.masksToBounds = YES;
        _paymentAlert.backgroundColor = [UIColor colorWithWhite:1. alpha:.95];
        [self addSubview:_paymentAlert];
        
        _titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, PAYMENT_WIDTH, TITLE_HEIGHT)];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        _titleLabel.textColor = [UIColor darkGrayColor];
        _titleLabel.font = [UIFont systemFontOfSize:17];
        [_paymentAlert addSubview:_titleLabel];
        
        _closeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_closeBtn setFrame:CGRectMake(0, 0, TITLE_HEIGHT, TITLE_HEIGHT)];
        [_closeBtn setTitle:@"╳" forState:UIControlStateNormal];
        [_closeBtn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        [_closeBtn addTarget:self action:@selector(dismiss) forControlEvents:UIControlEventTouchUpInside];
        _closeBtn.titleLabel.font = [UIFont systemFontOfSize:15];
        [_paymentAlert addSubview:_closeBtn];
        
        _line = [[UILabel alloc]initWithFrame:CGRectMake(0, TITLE_HEIGHT, PAYMENT_WIDTH, .5f)];
        _line.backgroundColor = [UIColor lightGrayColor];
        [_paymentAlert addSubview:_line];
        
        //
        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, TITLE_HEIGHT, PAYMENT_WIDTH-30, 20)];
        leftLabel.textAlignment = NSTextAlignmentLeft;
        leftLabel.text = @"申请人手机号";
        leftLabel.textColor = [UIColor darkGrayColor];
        leftLabel.font = [UIFont systemFontOfSize:13.f];
        [_paymentAlert addSubview:leftLabel];
        
        //申请人手机号
        _mobileNumLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, TITLE_HEIGHT, PAYMENT_WIDTH-30, 20)];
        _mobileNumLabel.textAlignment = NSTextAlignmentRight;
        _mobileNumLabel.textColor = [UIColor darkGrayColor];
        _mobileNumLabel.font = [UIFont systemFontOfSize:13.f];
        [_paymentAlert addSubview:_mobileNumLabel];
        
        
        //金额
        _amountLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, TITLE_HEIGHT+20, PAYMENT_WIDTH-30, 25)];
        _amountLabel.textAlignment = NSTextAlignmentCenter;
        _amountLabel.textColor = [UIColor darkGrayColor];
        _amountLabel.font = [UIFont systemFontOfSize:33];
        [_paymentAlert addSubview:_amountLabel];
        
        //银行卡
        _backCardBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_backCardBtn setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
        [_backCardBtn setImageEdgeInsets:UIEdgeInsetsMake(0, PAYMENT_WIDTH-20, 0, 0)];
        [_backCardBtn setFrame:CGRectMake(0, TITLE_HEIGHT+20+25, PAYMENT_WIDTH, 20)];
        [_backCardBtn setTitle:@"银行卡号" forState:UIControlStateNormal];
        [_backCardBtn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        [_backCardBtn addTarget:self action:@selector(backCardAction) forControlEvents:UIControlEventTouchUpInside];
        _backCardBtn.titleLabel.font = [UIFont systemFontOfSize:15];
        [_paymentAlert addSubview:_backCardBtn];
        
        
        //验证码Btn
        _codeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_codeBtn setFrame:CGRectMake((APP_VIEW_WIDTH-80)/4, TITLE_HEIGHT+20+25+20, (APP_VIEW_WIDTH-80)/2, 20)];
        [_codeBtn setTitle:@"获取验证码" forState:UIControlStateNormal];
        [_codeBtn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        [_codeBtn addTarget:self action:@selector(btnGetCode) forControlEvents:UIControlEventTouchUpInside];
        _codeBtn.titleLabel.font = [UIFont systemFontOfSize:15];
        _codeBtn.hidden = YES;
        [_paymentAlert addSubview:_codeBtn];

        
        
        _inputView = [[UIView alloc]initWithFrame:CGRectMake(15, _paymentAlert.frame.size.height-(PAYMENT_WIDTH-30)/6-15, PAYMENT_WIDTH-30, (PAYMENT_WIDTH-30)/6)];
        _inputView.backgroundColor = [UIColor whiteColor];
        _inputView.layer.borderWidth = 1.f;
        _inputView.layer.borderColor = [UIColor colorWithRed:.9 green:.9 blue:.9 alpha:1.].CGColor;
        [_paymentAlert addSubview:_inputView];
        
        pwdIndicatorArr = [[NSMutableArray alloc]init];
        _pwdTextField = [[UITextField alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.width, self.frame.size.height)];
        _pwdTextField.hidden = YES;
        _pwdTextField.delegate = self;
        _pwdTextField.keyboardType = UIKeyboardTypeNumberPad;
        [_inputView addSubview:_pwdTextField];
        
        CGFloat width = _inputView.bounds.size.width/PWD_COUNT;
        for (int i = 0; i < PWD_COUNT; i ++) {
            UILabel *dot = [[UILabel alloc]initWithFrame:CGRectMake((width-DOT_WIDTH)/2.f + i*width, (_inputView.bounds.size.height-DOT_WIDTH)/2.f, DOT_WIDTH, DOT_WIDTH)];
            dot.backgroundColor = [UIColor blackColor];
            dot.layer.cornerRadius = DOT_WIDTH/2.;
            dot.clipsToBounds = YES;
            dot.hidden = YES;
            [_inputView addSubview:dot];
            [pwdIndicatorArr addObject:dot];
            
            if (i == PWD_COUNT-1) {
                continue;
            }
            UILabel *line = [[UILabel alloc]initWithFrame:CGRectMake((i+1)*width, 0, .5f, _inputView.bounds.size.height)];
            line.backgroundColor = [UIColor colorWithRed:.9 green:.9 blue:.9 alpha:1.];
            [_inputView addSubview:line];
        }
    }
    
    
}
- (void)showInTheView:(UIView *)view isCode:(BOOL)isCode{
    [view addSubview:self];
    
    
    _codeBtn.hidden = !isCode;
    
    _paymentAlert.transform = CGAffineTransformMakeScale(1.21f, 1.21f);
    _paymentAlert.alpha = 0;
    
    
    [UIView animateWithDuration:.7f delay:0.f usingSpringWithDamping:.7f initialSpringVelocity:1 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        [_pwdTextField becomeFirstResponder];
        _paymentAlert.transform = CGAffineTransformMakeScale(1.0f, 1.0f);
        _paymentAlert.alpha = 1.0;
    } completion:nil];
}

- (void)show {
    UIWindow *keyWindow = [UIApplication sharedApplication].keyWindow;
    [keyWindow addSubview:self];
    
    _paymentAlert.transform = CGAffineTransformMakeScale(1.21f, 1.21f);
    _paymentAlert.alpha = 0;

    
    [UIView animateWithDuration:.7f delay:0.f usingSpringWithDamping:.7f initialSpringVelocity:1 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        [_pwdTextField becomeFirstResponder];
        _paymentAlert.transform = CGAffineTransformMakeScale(1.0f, 1.0f);
        _paymentAlert.alpha = 1.0;
    } completion:nil];
}


- (void)backCardAction{
    if (_DCdelegate!=nil) {
        [self.DCdelegate selectBackCard];
    }
    
    
}

- (void)btnGetCode {
    if (_DCdelegate!=nil) {
        [self.DCdelegate selectCode];
//        [self timerFireMethod];
    }
    
    
    
}

- (void)timerFireMethod{
    
    __block int timeoutS = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timers = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timers, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timers, ^{
        if (timeoutS <= 0){
            dispatch_source_cancel(timers);
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:@"再次获取" forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeoutS];
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:strTime forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = NO;
            });
            timeoutS--;
        }
    });
    dispatch_resume(timers);
}



- (void)dismiss {
    [_pwdTextField resignFirstResponder];
    [UIView animateWithDuration:0.3f animations:^{
        _paymentAlert.transform = CGAffineTransformMakeScale(1.21f, 1.21f);
        _paymentAlert.alpha = 0;
        self.alpha = 0;
    } completion:^(BOOL finished) {
            [self removeFromSuperview];
    }];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if (textField.text.length >= PWD_COUNT && string.length) {
        //输入的字符个数大于6，则无法继续输入，返回NO表示禁止输入
        return NO;
    }
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",@"^[0-9]*$"];
    if (![predicate evaluateWithObject:string]) {
        return NO;
    }
    NSString *totalString;
    if (string.length <= 0) {
        totalString = [textField.text substringToIndex:textField.text.length-1];
    }
    else {
        totalString = [NSString stringWithFormat:@"%@%@",textField.text,string];
    }
    [self setDotWithCount:totalString.length];
    
    NSLog(@"_____total %@",totalString);
    if (totalString.length == 6) {
        if (_completeHandle) {
            _completeHandle(totalString);
        }
        [self performSelector:@selector(dismiss) withObject:nil afterDelay:.3f];
    NSLog(@"complete");
    }
    
    return YES;
}

- (void)setDotWithCount:(NSInteger)count {
    for (UILabel *dot in pwdIndicatorArr) {
        dot.hidden = YES;
    }
    
    for (int i = 0; i< count; i++) {
        ((UILabel*)[pwdIndicatorArr objectAtIndex:i]).hidden = NO;
    }
}

#pragma mark - 
- (void)setTitle:(NSString *)title {
    if (_title != title) {
        _title = title;
        _titleLabel.text = _title;
    }
}

- (void)setMobileNum:(NSString *)mobileNum {
    if (_mobileNum != mobileNum) {
        _mobileNum = mobileNum;
        _mobileNumLabel.text = _mobileNum;
    }
}

- (void)setAmount:(float)amount {
    if (_amount != amount) {
        _amount = amount;
        _amountLabel.text = [NSString stringWithFormat:@"￥%.2f  ",amount];
    }
}

- (void)setCardMessage:(NSString *)cardMessage {
    if (_cardMessage != cardMessage) {
        _cardMessage = cardMessage;
        NSString *string = [NSString stringWithFormat:@"银行卡后四位(%@)", cardMessage];
        [_backCardBtn setTitle:string forState:UIControlStateNormal];
    }

}

@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com