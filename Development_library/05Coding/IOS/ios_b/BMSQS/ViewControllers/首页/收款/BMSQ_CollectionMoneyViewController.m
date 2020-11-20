//
//  BMSQ_CollectionMoneyViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_CollectionMoneyViewController.h"
#import "ConfirmOrderViewController.h"
//#import "ZBarReaderViewController.h"
#import "ScanViewController.h"
//ZBarReaderDelegate
@interface BMSQ_CollectionMoneyViewController () <UINavigationControllerDelegate,UIImagePickerControllerDelegate, ScanViewDelegate>{
    BOOL upOrdown;
    NSTimer * timer;
    UIImageView* _line;
    int num;
    NSDictionary *dic_Ordering;
    
}
//@property (nonatomic, strong)NSString *bankAccountCode;
@property (nonatomic, strong)NSString *scaString;

@property (nonatomic, strong)NSString *orderAmount;
@property (nonatomic, strong)NSString *noDiscountPrice;

@property (nonatomic, strong) UITextField * orderAmountText;// 消费金额
@property (nonatomic, strong) UITextField * noDiscountPriceText;// 不参与优惠金额
@property (nonatomic, strong) UIButton * selectBut;
@property (nonatomic, strong) UIView * underView;
@property (nonatomic, strong) UIButton * sweepBut;// 扫码支付
@property (nonatomic, strong) UILabel * promptLB; //提示

@property (nonatomic, assign) BOOL isSelect;//是否选中不参与金额
@property (nonatomic, assign) BOOL isTag;// 是否取消不参与金额

@end

@implementation BMSQ_CollectionMoneyViewController

- (void)viewDidLoad {
    [super viewDidLoad];


    
    
    
    [self setNavBackItem];
    [self setNavTitle:@"输入金额"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    self.isSelect = NO;
    
    [self setViewUp];
    [self select];
}

- (void)setViewUp
{
    CGFloat viewY = APP_VIEW_ORIGIN_Y + 10;
    
    self.noDiscountPrice = @"";
    self.orderAmount = @"";
    
    
    UIView * topView = [[UIView alloc] initWithFrame:CGRectMake(20, viewY, APP_VIEW_WIDTH - 40, 40)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    
    UILabel * consumeLB = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 80, 30)];
    consumeLB.text = @"消费金额:";
    consumeLB.font = [UIFont systemFontOfSize:16];
    [topView addSubview:consumeLB];

    self.orderAmountText = [[UITextField alloc] initWithFrame:CGRectMake(topView.frame.size.width/2, 5, topView.frame.size.width/2 - 5 , 30)];
    self.orderAmountText.textAlignment = NSTextAlignmentRight;
    [self.orderAmountText addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
    self.orderAmountText.keyboardType = UIKeyboardTypeDecimalPad;
    [topView addSubview:self.orderAmountText];

    
    viewY  = viewY + 40;
    self.selectBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.selectBut.backgroundColor = [UIColor clearColor];
    _selectBut.frame = CGRectMake(10, viewY, APP_VIEW_WIDTH - 10, 50);
    self.selectBut.imageEdgeInsets = UIEdgeInsetsMake(15, 0, 20, APP_VIEW_WIDTH - 10-15);
    [_selectBut addTarget:self action:@selector(selectButClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_selectBut];
    
    
    UILabel * contentLB = [[UILabel alloc] initWithFrame:CGRectMake(35, APP_VIEW_ORIGIN_Y + 65, APP_VIEW_WIDTH - 30, 15)];
    contentLB.text = @"输入不参与优惠金额(如酒水，海鲜等)";
    contentLB.font = [UIFont systemFontOfSize:14];
//    contentLB.textColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:contentLB];
    
    self.underView = [[UIView alloc] initWithFrame:CGRectMake(20, APP_VIEW_ORIGIN_Y + 90, APP_VIEW_WIDTH - 40, 40)];
    _underView.hidden = YES;
    _underView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:_underView];
    UILabel * noPreferentialLB = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 120, 30)];
    noPreferentialLB.text = @"不参与优惠金额:";
    noPreferentialLB.font = [UIFont systemFontOfSize:16];
    [_underView addSubview:noPreferentialLB];
    
    self.noDiscountPriceText = [[UITextField alloc] initWithFrame:CGRectMake(self.underView.frame.size.width/2, 5, self.underView.frame.size.width/2 - 5, 30)];
    self.noDiscountPriceText.textAlignment = NSTextAlignmentRight;
    [self.noDiscountPriceText addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
    self.noDiscountPriceText.text = @"";
    self.noDiscountPriceText.keyboardType = UIKeyboardTypeDecimalPad;
    [_underView addSubview:self.noDiscountPriceText];
    
    
    
    self.sweepBut = [UIButton buttonWithType:UIButtonTypeCustom];
    _sweepBut.frame = CGRectMake(10, APP_VIEW_ORIGIN_Y + 150, APP_VIEW_WIDTH - 20, 40);
    _sweepBut.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [_sweepBut setTitle:@"扫码支付" forState:UIControlStateNormal];
    [_sweepBut setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [_sweepBut addTarget:self action:@selector(sweepButClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_sweepBut];
    
    self.promptLB = [[UILabel alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 220, APP_VIEW_WIDTH - 20, 30)];
    _promptLB.text = @"＊扫码支付的金额不超过300元";
    _promptLB.textColor = [UIColor redColor];
    _promptLB.font = [UIFont systemFontOfSize:16];
    [self.view addSubview:_promptLB];
    
    

    
}

- (void)select
{
    
    if (self.isSelect) {
        [_noDiscountPriceText becomeFirstResponder];
        _noDiscountPriceText.text = @"";
        _noDiscountPrice = @"";
        [_selectBut setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateNormal];
        _underView.hidden = NO;
        _sweepBut.frame = CGRectMake(_sweepBut.frame.origin.x, APP_VIEW_ORIGIN_Y + 150,  _sweepBut.frame.size.width, _sweepBut.frame.size.height);
        _promptLB.frame = CGRectMake(_promptLB.frame.origin.x, APP_VIEW_ORIGIN_Y + 220, _promptLB.frame.size.width, _promptLB.frame.size.height);
        
    }else{
        [_orderAmountText becomeFirstResponder];
        _noDiscountPriceText.text = @"";
        _noDiscountPrice = @"";
        [_selectBut setImage:[UIImage imageNamed:@"radio_no"] forState:UIControlStateNormal];
         _underView.hidden = YES;
        _sweepBut.frame = CGRectMake(_sweepBut.frame.origin.x, APP_VIEW_ORIGIN_Y + 110,  _sweepBut.frame.size.width, _sweepBut.frame.size.height);
        _promptLB.frame = CGRectMake(_promptLB.frame.origin.x, APP_VIEW_ORIGIN_Y + 180, _promptLB.frame.size.width, _promptLB.frame.size.height);

    }
    
    
}



#pragma mark -  UITextField delegate
-(void)textFieldChange:(UITextField *)textField{
    
    NSLog(@"%ld",(long)textField.text);
    
    if (textField == self.orderAmountText) {  //
        
        if (textField.text.floatValue <= 300) {
            self.orderAmount = textField.text;
            
        }else {
            self.orderAmount = textField.text;
            CSAlert(@"请输入300以下的金额");
        }

    }
    
    if (self.selectBut) {
        if (textField == self.noDiscountPriceText) {
            if (textField.text.floatValue <= self.orderAmount.floatValue) {
                self.noDiscountPrice = textField.text;
                
            }else {
                self.noDiscountPrice = textField.text;
                CSAlert(@"不参与优惠的金额不能大于消费金额");
                
            }
        }
    }else {
        self.noDiscountPrice = @"";
        
    }
    
}


#pragma mark ---- 选择不参与优惠金额
- (void)selectButClick
{
    if (self.isSelect) {
        self.isSelect = NO;
        
    }else{
        self.isSelect = YES;
        
    }
    
    [self select];
}

#pragma mark ----- 扫码支付
- (void)sweepButClick
{
    NSLog(@"扫码支付");
    
    if (self.orderAmount.length == 0 ) {
        
        CSAlert(@"请输入金额");
        return;
    }
    
//    if (self.isSelect == YES) {
//        if (self.noDiscountPrice.length == 0) {
//            CSAlert(@"请输入不参与优惠金额");
//            return;
//        }
//        
//    }
    
    if (self.orderAmount.floatValue > 300.00) {
        CSAlert(@"扫码支付的金额不能超过300元");
        return;
    }
    if (self.noDiscountPrice.floatValue > self.orderAmount.floatValue) {
        CSAlert(@"不参与优惠的金额不能大于消费金额");
        return;
    }
    
    //扫描
    ScanViewController *scanVC = [[ScanViewController alloc] init];

    scanVC.scanDelegate = self;
    [self presentViewController:scanVC animated:YES completion:^{
     
    }];

    
}





#pragma mark ------------------------ 二维码扫描
//- (void)gotoConfirmOrder:(NSString *)scaStringValue {
//    self.scaString = scaStringValue;
//    
//    NSLog(@"二维码信息--->%@<----这是扫描后的代理方法",scaStringValue);


//    NSURL *url = [NSURL URLWithString:@"http://baomi.suanzi.cn/Api/Index/index"];
//    NSURLRequest *request=[NSURLRequest requestWithURL:url];
//    NSURLConnection *connection=[[NSURLConnection alloc]initWithRequest:request
//                                                               delegate:self
//                                                       startImmediately:YES];
//    [connection start];
    
//    [self getServerTime];
    
    
    
//}

//- (void)getServerTime {
//    
//    
//    [self initJsonPrcClient:@"0"];
//    [self.jsonPrcClient invokeMethod:@"getServerTime" withParameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        
//        NSLog(@"responseObject");
//        
//        NSString *time = [responseObject objectForKey:@"time"];
//        
//        //转16进制
//        NSMutableString *hexString = [NSMutableString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%1x",[time intValue]]];
//        
//        //如果小于10位  随机插入G-Z 补齐十位
//        while (hexString.length < 10) {
//            int randomAryIndex = arc4random() % self.randomArray.count;
//            int randomIndex = arc4random() % hexString.length;
//            
//            [hexString insertString:self.randomArray[randomAryIndex] atIndex:randomIndex];
//            
//            
//        }
//        
//        //拼接字符串     网络请求 sweepQrCode
//        NSString *str = [NSString stringWithFormat:@"%@%@",self.scaString, hexString];
//        [self sweepQrCode:str];
//
//        
//        
//        
//    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        NSLog(@"%@",error);
//        
//    }];
//
//    
//    
//}


//- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
//{
//    NSLog(@"response%@",response);
//    
//    
//    NSHTTPURLResponse *httpResponse=(NSHTTPURLResponse *)response;
//    if ([response respondsToSelector:@selector(allHeaderFields)]) {
//        
//        NSDictionary *dic=[httpResponse allHeaderFields];
//        NSString *time=[dic objectForKey:@"Date"];
//        time = [time substringWithRange:NSMakeRange(5, 20)];
//        //设置源时间字符串的格式
//        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
//        [formatter setDateFormat:@"dd MMM yyyy HH:mm:ss"];
//        
//        NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"GMT"];
//        [formatter setTimeZone:timeZone];
//        //需要配置区域，不然会造成模拟器正常，真机日期为null的情况
//        NSLocale *local=[[NSLocale alloc]initWithLocaleIdentifier:@"en_US_POSIX"];
//        [formatter setLocale:local];
//        
//        NSDate* date = [formatter dateFromString:time];
//        //时间戳
//        NSTimeInterval a = [date timeIntervalSince1970];
//        NSString *timeString = [NSString stringWithFormat:@"%.0f", a];
//        int timeInt = [timeString intValue];
//        
//        //转16进制
//        NSMutableString *hexString = [NSMutableString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%1x",timeInt]];
//        
//        //如果小于10位  随机插入G-Z 补齐十位
//        while (hexString.length < 10) {
//            int randomAryIndex = arc4random() % self.randomArray.count;
//            int randomIndex = arc4random() % hexString.length;
//            
//            [hexString insertString:self.randomArray[randomAryIndex] atIndex:randomIndex];
//            
//            
//        }
//        
//        //拼接字符串     网络请求 sweepQrCode
//        NSString *str = [NSString stringWithFormat:@"%@%@",self.scaString, hexString];
//        [self sweepQrCode:str];
//
//    }
//}


//
//-(void)sweepQrCode:(NSString *)sweepString{
//
//    [self initJsonPrcClient:@"1"];
//
//    
//    if ([sweepString hasPrefix:@"payType:"]) {
//        NSRange range =[sweepString rangeOfString:@":"];
//        if (range.length > 0) {
//            sweepString = [sweepString substringFromIndex:range.location+1];
//        }
//    }else {
//        CSAlert(@"验证码错误");
//        return;
//    }
//    
//    
//    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
//
//    [params setObject:sweepString forKey:@"validateString"];
//
//    NSString* vcode = [gloabFunction getSign:@"sweepQrCode" strParams:sweepString];
//    NSLog(@"sweepstring---->%@",sweepString);
//    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
//    [params setObject:vcode forKey:@"vcode"];
//    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
//    __weak typeof(self) weakSelf = self;
//    [self initJsonPrcClient:@"1"];
//    [self.jsonPrcClient invokeMethod:@"sweepQrCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        
//        
//        NSLog(@"%@",responseObject);
//        
//        NSString *str = [responseObject objectForKey:@"code"];
//
//        
//        if (str.intValue == 50000) {
//            self.bankAccountCode = [responseObject objectForKey:@"bankAccountCode"];
//            [self getOptimalPay:responseObject];
//            
////            ConfirmOrderViewController *orderVC = [[ConfirmOrderViewController alloc] init];
////            [self.navigationController pushViewController:orderVC animated:YES];
//            
//        }else {
//            CSAlert([weakSelf errorMessage:[str intValue]]);
//            
//        }
//
//        
//    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        NSLog(@"%@",error);
//        
//    }];
//
//}


//获取最优的支付结果
- (void)getOptimalPay:(NSDictionary *)dic bankCode:(NSString *)bankCode{
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[dic objectForKey:@"userCode"] forKey:@"userCode"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:self.orderAmount forKey:@"orderAmount"];
//    if ()
    [params setObject:self.noDiscountPrice forKey:@"noDiscountPrice"];

    NSString* vcode = [gloabFunction getSign:@"getOptimalPay" strParams:[dic objectForKey:@"userCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"getOptimalPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"%@",responseObject);
        NSString *str = [responseObject objectForKey:@"code"];
        
        if (str.intValue == 50000) {
            NSDictionary *dic = responseObject;
            
            ConfirmOrderViewController *vc = [[ConfirmOrderViewController alloc] init];
            vc.orderData = dic;
            vc.orderAmount = self.orderAmount;
            vc.noDiscountPrice = self.noDiscountPrice;
            vc.bankAccountCode = bankCode;
            [self.navigationController pushViewController:vc animated:YES];
            
        }else {
            NSString *errorStr = [NSString stringWithFormat:@"错误信息->%@",str];
            CSAlert(errorStr);
            
        }
        
        
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        
    }];
    
}

//
////'85001', // 扫描失败
////'85002', // 二维码不安全
////'85003', // 二维码过期
////'85004', // 用户不存在
////'85005', // 用户未绑定该银行卡
////'85006', // 该银行卡解绑或未成功绑定
//-(NSString *)errorMessage:(int)code{
//    
//    switch (code) {
//        case 85001:
//            return @"扫描失败";
//            break;
//        case 85002:
//            return @"二维码不安全";
//            break;
//        case 85003:
//            return @"二维码过期";
//            break;
//        case 85004:
//            return @"用户不存在";
//            break;
//        case 85005:
//            return @"用户未绑定该银行卡";
//            break;
//        case 85006:
//            return @"该银行卡解绑或未成功绑定";
//            break;
//        default:{
//            NSString *str = [NSString stringWithFormat:@"扫码失败,失败原因[%d]",code];
//            return str;
//        }
//            break;
//    }
//    return nil;
//}




#pragma mark ---- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
