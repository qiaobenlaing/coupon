//
//  ScanViewController.m
//  BMSQS
//
//  Created by gh on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ScanViewController.h"

@interface ScanViewController ()
@property (nonatomic,strong)NSArray *randomArray;
@property (nonatomic,strong)NSString *scaStringValue;

@property (nonatomic, strong)UIView * centerView;

@property (nonatomic, strong)NSString *bankAccountCode; //银行卡code


@end


@implementation ScanViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
//   @"g", @"h", @"i", @"j", @"k", @"l", @"m", @"n", @"o", @"p", @"q", @"r", @"s", @"t", @"u", @"v", @"w", @"x", @"y", @"z",
    self.randomArray = [[NSArray alloc] initWithObjects:@"G", @"H", @"I", @"J", @"K", @"L", @"M", @"N", @"O", @"P", @"Q", @"R", @"S", @"T", @"U", @"V", @"W", @"X", @"Y", @"Z", nil];
    
    [self setNavTitle:@"扫付款码"];
    [self setNavLeftBtn];
    
    
    
    
    
    isFlash = YES;
    
    
    
    
    UIImageView * imageView = [[UIImageView alloc]initWithFrame:CGRectMake(10, 100, 300, 300)];
    imageView.image = [UIImage imageNamed:@"pick_bg"];
    [self.view addSubview:imageView];
    
    self.centerView = [[UIView alloc] initWithFrame:CGRectMake(150/2, 150/2, 300/2, 300/2)];
    self.centerView.backgroundColor = [UIColor grayColor];
    self.centerView.alpha = 0.7;
    
    [imageView addSubview:self.centerView ];
    
    UILabel *label1 = [[UILabel alloc] initWithFrame:CGRectMake(0, (150-50)/2, 150, 50)];
    label1.textAlignment = NSTextAlignmentCenter;
    label1.textColor = [UIColor whiteColor];
    label1.text = @"已扫描，正在处理";
    [self.centerView addSubview:label1];
    
    
    
    UILabel * labIntroudction= [[UILabel alloc] initWithFrame:CGRectMake(0, imageView.frame.size.height + imageView.frame.origin.y, APP_VIEW_WIDTH, 50)];
    labIntroudction.backgroundColor = [UIColor clearColor];
    labIntroudction.numberOfLines=2;
    labIntroudction.textColor=[UIColor whiteColor];
    labIntroudction.textAlignment = NSTextAlignmentCenter;
    labIntroudction.text=@"将二维码放入扫描框内，即可自动扫描";
    [self.view addSubview:labIntroudction];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH/4, labIntroudction.frame.origin.y + labIntroudction.frame.size.height, APP_VIEW_WIDTH/2, 40);
    button.backgroundColor = APP_NAVCOLOR;
    [button addTarget:self action:@selector(btnFlash:) forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"打开闪光灯" forState:UIControlStateNormal];
    [self.view addSubview: button];
    
    
    
    upOrdown = NO;
    num =0;
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(50, 110, 220, 2)];
    _line.image = [UIImage imageNamed:@"line.png"];
    [self.view addSubview:_line];
    
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    
    self.centerView.hidden = YES;
    
    
}

-(void)animation1
{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(50, 110+2*num, 220, 2);
        if (2*num == 280) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(50, 110+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
    
}
-(void)backAction
{
    
    [self dismissViewControllerAnimated:YES completion:^{
        [timer invalidate];
    }];
}
-(void)viewWillAppear:(BOOL)animated
{
    [self setupCamera];
}
- (void)setupCamera
{
    // Device
    _device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    // Input
    _input = [AVCaptureDeviceInput deviceInputWithDevice:self.device error:nil];
    
    // Output
    _output = [[AVCaptureMetadataOutput alloc]init];
    [_output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    
//    [_output setRectOfInterest : CGRectMake (( 124 )/ APP_VIEW_HEIGHT ,(( APP_VIEW_WIDTH - 220 )/ 2 )/ APP_VIEW_WIDTH , 220 / APP_VIEW_HEIGHT , 220 / APP_VIEW_WIDTH )];
    
    // Session
    _session = [[AVCaptureSession alloc]init];
    [_session setSessionPreset:AVCaptureSessionPresetHigh];
    if ([_session canAddInput:self.input])
    {
        [_session addInput:self.input];
    }
    
    if ([_session canAddOutput:self.output])
    {
        [_session addOutput:self.output];
    }
    
    // 条码类型 AVMetadataObjectTypeQRCode
    _output.metadataObjectTypes =@[AVMetadataObjectTypeQRCode];
    
    // Preview
    _preview =[AVCaptureVideoPreviewLayer layerWithSession:self.session];
    _preview.videoGravity = AVLayerVideoGravityResizeAspectFill;
    _preview.frame =CGRectMake(20,110,280,280);
    [self.view.layer insertSublayer:self.preview atIndex:0];
    

    // Start
    [_session startRunning];
}

#pragma mark - 打开闪光灯
- (void)btnFlash:(UIButton *)btn {
    
    Class captureDeviceClass = NSClassFromString(@"AVCaptureDevice");
    if (captureDeviceClass != nil) {
        AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
        if ([device hasTorch] && [device hasFlash]){
            
            [device lockForConfiguration:nil];
            if (isFlash) {
                [device setTorchMode:AVCaptureTorchModeOn];
                [device setFlashMode:AVCaptureFlashModeOn];
//                torchIsOn = YES;
            } else {
                [device setTorchMode:AVCaptureTorchModeOff];
                [device setFlashMode:AVCaptureFlashModeOff];
//                torchIsOn = NO;
            }
            isFlash = !isFlash;
            [device unlockForConfiguration];
        }
    }
    
    
    
    
    
}


#pragma mark AVCaptureMetadataOutputObjectsDelegate
- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
    

    
    if ([metadataObjects count] >0)
    {
        AVMetadataMachineReadableCodeObject * metadataObject = [metadataObjects objectAtIndex:0];
        self.scaStringValue = metadataObject.stringValue;
    }
    
    [_session stopRunning];
    
    
    [self loadScan];
    
    self.centerView.hidden = NO;
    
    
    
//    [self dismissViewControllerAnimated:YES completion:^{
//        
//        [timer invalidate];
//         NSLog(@"%@",self.scaStringValue);
//
//     }];
    
}

//等待请求数据
- (void)loadScan {
    NSURL *url = [NSURL URLWithString:@"http://baomi.suanzi.cn/Api/Index/index"];
    NSURLRequest *request=[NSURLRequest requestWithURL:url];
    NSURLConnection *connection=[[NSURLConnection alloc]initWithRequest:request
                                                               delegate:self
                                                       startImmediately:YES];
    [connection start];

    
    
}

//请求网络时间
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    NSLog(@"response%@",response);
    
    
    NSHTTPURLResponse *httpResponse=(NSHTTPURLResponse *)response;
    if ([response respondsToSelector:@selector(allHeaderFields)]) {
        
        NSDictionary *dic=[httpResponse allHeaderFields];
        NSString *time=[dic objectForKey:@"Date"];
        time = [time substringWithRange:NSMakeRange(5, 20)];
        //设置源时间字符串的格式
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"dd MMM yyyy HH:mm:ss"];
        
        NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"GMT"];
        [formatter setTimeZone:timeZone];
        //需要配置区域，不然会造成模拟器正常，真机日期为null的情况
        NSLocale *local=[[NSLocale alloc]initWithLocaleIdentifier:@"en_US_POSIX"];
        [formatter setLocale:local];
        
        NSDate* date = [formatter dateFromString:time];
        //时间戳
        NSTimeInterval a = [date timeIntervalSince1970];
        NSString *timeString = [NSString stringWithFormat:@"%.0f", a];
        int timeInt = [timeString intValue];
        
        //转16进制
        NSMutableString *hexString = [NSMutableString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%1x",timeInt]];
        
        //如果小于10位  随机插入G-Z 补齐十位
        while (hexString.length < 10) {
            int randomAryIndex = arc4random() % self.randomArray.count;
            int randomIndex = arc4random() % hexString.length;
            
            [hexString insertString:self.randomArray[randomAryIndex] atIndex:randomIndex];
            
            
        }
        
        //拼接字符串     网络请求 sweepQrCode
        NSString *str = [NSString stringWithFormat:@"%@%@", self.scaStringValue, hexString];
        
        [self sweepQrCode:str];
    }
}

//扫码 API
-(void)sweepQrCode:(NSString *)sweepString{
    
    [self initJsonPrcClient:@"1"];
    
    
    if ([sweepString hasPrefix:@"payType:"]) {
        NSRange range =[sweepString rangeOfString:@":"];
        if (range.length > 0) {
            sweepString = [sweepString substringFromIndex:range.location+1];
        }
    }else {
        CSAlert(@"验证码错误");
        return;
    }
    
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:sweepString forKey:@"validateString"];
    
    NSString* vcode = [gloabFunction getSign:@"sweepQrCode" strParams:sweepString];
    NSLog(@"sweepstring---->%@",sweepString);
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"sweepQrCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        self.centerView.hidden = YES;
        
        NSLog(@"%@",responseObject);
        
        NSString *str = [responseObject objectForKey:@"code"];
        
        
        if (str.intValue == 50000) {
            self.bankAccountCode = [responseObject objectForKey:@"bankAccountCode"];
            
            [self dismissViewControllerAnimated:NO completion:^{
                if (self.scanDelegate != nil) {
                    [self.scanDelegate getOptimalPay:responseObject bankCode:self.bankAccountCode];
                }
                
            } ];
            
            
        }else {
            
            [self dismissViewControllerAnimated:NO completion:^{
                CSAlert([weakSelf errorMessage:[str intValue]]);
                
            } ];
            
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"%@",error);
        
       [ self dismissViewControllerAnimated:NO completion:^{
            CSAlert(@"未知的错误");
        } ];
        
        
    }];
    
}




- (void)setNavLeftBtn {
    self.view.backgroundColor = [UIColor grayColor];

    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - 0), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];


    [self.navigationView addSubview:btnback];
    
    
}


//'85001', // 扫描失败
//'85002', // 二维码不安全
//'85003', // 二维码过期
//'85004', // 用户不存在
//'85005', // 用户未绑定该银行卡
//'85006', // 该银行卡解绑或未成功绑定
-(NSString *)errorMessage:(int)code{
    
    switch (code) {
        case 85001:
            return @"扫描失败";
            break;
        case 85002:
            return @"请重新扫描";
            break;
        case 85003:
            return @"二维码过期";
            break;
        case 85004:
            return @"用户不存在";
            break;
        case 85005:
            return @"用户未绑定该银行卡";
            break;
        case 85006:
            return @"该银行卡解绑或未成功绑定";
            break;
        default:{
            NSString *str = [NSString stringWithFormat:@"扫码失败,失败原因[%d]",code];
            return str;
        }
            break;
    }
    return nil;
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end


