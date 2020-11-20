//
//  BMSQ_VerificationCodeViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_VerificationCodeViewController.h"
#import "BMSQ_VoucherVerifyViewController.h"
#import "BMSQ_ActivityVerifyViewController.h"
@interface BMSQ_VerificationCodeViewController ()<ZBarReaderDelegate, UITextFieldDelegate>
{
    int status; //1 验证券 2 活动 3 扫码
    UIButton* btnCupon;
    UIButton* btnActivity;
    UIButton* btnSweep;
    
    UIButton *doneInKeyboardButton;
    
    NSTimer * timer;
    UIImageView* _line;
    BOOL upOrdown;
    int num;
    
}

@property (nonatomic, strong) UITextField * inputVerifyField;

@end

@implementation BMSQ_VerificationCodeViewController

- (void)viewWillAppear:(BOOL)animated
{
    [self setViewUp];
    status = 1;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"验证码"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    status = 1;
    [self setViewUp];
    
}

- (void)setViewUp
{
    
    UIView* segmentationView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    segmentationView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:segmentationView];
    
    btnCupon = [[UIButton alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH / 2, 30)];
//    btnCupon = [[UIButton alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH / 3, 30)];
    [btnCupon setTitle:@"兑换券/代金券" forState:UIControlStateNormal];
    [btnCupon.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftNoSelect"] forState:UIControlStateNormal];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftSelect"] forState:UIControlStateSelected];
    [btnCupon setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnCupon setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [btnCupon addTarget:self action:@selector(btnCuponClick) forControlEvents:UIControlEventTouchUpInside];
    btnCupon.selected = YES;
    [segmentationView addSubview:btnCupon];
    
    btnActivity = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH / 2, 10, APP_VIEW_WIDTH / 2, 30)];
//     btnActivity = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH / 3, 10, APP_VIEW_WIDTH / 3, 30)];
    [btnActivity setTitle:@"活动" forState:UIControlStateNormal];
    [btnActivity.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnActivity setBackgroundImage:[UIImage imageNamed:@"iv_centerNoselect"] forState:UIControlStateNormal];
    [btnActivity setBackgroundImage:[UIImage imageNamed:@"iv_centerSelect"] forState:UIControlStateSelected];
    [btnActivity addTarget:self action:@selector(btnActivityClick) forControlEvents:UIControlEventTouchUpInside];
    [btnActivity setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnActivity setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [segmentationView addSubview:btnActivity];
    
    /*
    btnSweep = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH / 3 * 2, 10, APP_VIEW_WIDTH / 3, 30)];
    [btnSweep setTitle:@"扫码" forState:UIControlStateNormal];
    [btnSweep.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnSweep setBackgroundImage:[UIImage imageNamed:@"iv_centerNoselect"] forState:UIControlStateNormal];
    [btnSweep setBackgroundImage:[UIImage imageNamed:@"iv_centerSelect"] forState:UIControlStateSelected];
    [btnSweep addTarget:self action:@selector(btnSweepClick) forControlEvents:UIControlEventTouchUpInside];
    [btnSweep setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnSweep setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [segmentationView addSubview:btnSweep];
    */
    
    self.inputVerifyField = [[UITextField alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 60, APP_VIEW_WIDTH - 80, 40)];
    _inputVerifyField.delegate = self;
    _inputVerifyField.placeholder = @"请输入验证码";
    _inputVerifyField.borderStyle = UITextBorderStyleRoundedRect;
    _inputVerifyField.textAlignment = NSTextAlignmentCenter;
    _inputVerifyField.keyboardType = UIKeyboardTypeNumberPad;
    [_inputVerifyField addTarget:self action:@selector(fieldTextChange) forControlEvents:UIControlEventEditingChanged];
    
    UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
    [topView setBarStyle:UIBarStyleBlackTranslucent];
    
    UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(2, 5, 50, 25);
    [btn setTitle:@"取消" forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
    [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
    UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
    NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
    [topView setItems:buttonsArray];
    [_inputVerifyField setInputAccessoryView:topView];
    
    [self.view addSubview:_inputVerifyField];
    
    
    UIButton * butEnsure = [UIButton buttonWithType:UIButtonTypeCustom];
    butEnsure.frame = CGRectMake(APP_VIEW_WIDTH - 50, APP_VIEW_ORIGIN_Y + 60, 40, 40);
//    butEnsure.backgroundColor = UICOLOR(101, 102, 103, 1.0);
    [butEnsure setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [butEnsure setTitle:@"确认" forState:UIControlStateNormal];
    [butEnsure setImage:[UIImage imageNamed:@"go"] forState:UIControlStateNormal];
    [butEnsure addTarget:self action:@selector(butEnsureClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:butEnsure];
    
    
    
    
}
#pragma mark ---------  dismissKeyB
- (void)dismissKeyB
{
    [self.inputVerifyField resignFirstResponder];
    
}




#pragma mark ----- 按钮出发方法
//验证券
- (void)btnCuponClick
{
    btnCupon.selected = YES;
    btnActivity.selected = NO;
    btnSweep.selected = NO;
    status = 1;
    
//    [self setViewUp];
    
    
}
//活动
- (void)btnActivityClick
{
    btnCupon.selected = NO;
    btnActivity.selected = YES;
    btnSweep.selected = NO;
    status = 2;
    
//    [self setViewUp];
    
}
#pragma mark ---- 扫码
//扫码
- (void)btnSweepClick
{
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    reader.showsHelpOnFail = NO;
    reader.scanCrop = CGRectMake(0.1, 0.2, 0.8, 0.8);
    
    ZBarImageScanner *scanner = reader.scanner;
    
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    UIView * view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    view.backgroundColor = [UIColor clearColor];
    reader.cameraOverlayView = view;
    
    UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, APP_VIEW_WIDTH - 40, 40)];
    label.text = @"请将扫描的二维码至于下面的框内！";
    label.textColor = [UIColor whiteColor];
    label.textAlignment = 1;
    label.lineBreakMode = 0;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
    [view addSubview:label];
    
    UIImageView * image = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"pick_bg.png"]];
    image.frame = CGRectMake(20, 80, APP_VIEW_WIDTH-40, APP_VIEW_WIDTH - 40);
    [view addSubview:image];
    
    
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, APP_VIEW_WIDTH - 100, 2)];
    _line.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [image addSubview:_line];
    //定时器，设定时间过1.5秒，
    timer = [NSTimer scheduledTimerWithTimeInterval:.01 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    
    
    [self presentViewController:reader animated:YES completion:nil];

    
}

-(void)animation1
{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (2*num == 260) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
    
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
    }];
}

- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    
    [reader dismissViewControllerAnimated:YES completion: nil];
    
    NSString *scanResult=symbol.data;
    if(scanResult==nil||scanResult.length==0){
        return ;
    }
    
     NSLog(@"scanResult ===== %@", scanResult);
    
    BMSQ_VoucherVerifyViewController * voucherVC = [[BMSQ_VoucherVerifyViewController alloc] init];
    
    voucherVC.couponCode = scanResult;
    [self.navigationController pushViewController:voucherVC animated:YES];

    

}

#pragma mark ------ fieldTextChange 监测输入框的值
- (void)fieldTextChange
{
//    NSLog(@"%@", _inputVerifyField.text);
}


#pragma mark --------------  butEnsureClick 确认按钮
- (void)butEnsureClick
{
    if ([self.inputVerifyField.text isEqualToString:@""]) {
        
        CSAlert(@"请输入验证码");
        
    }else{
        
        if (status == 1) {
            
            BMSQ_VoucherVerifyViewController * voucherVC = [[BMSQ_VoucherVerifyViewController alloc] init];
            
            voucherVC.couponCode = self.inputVerifyField.text;
            [self.navigationController pushViewController:voucherVC animated:YES];
            
        }else if (status == 2){
            
            BMSQ_ActivityVerifyViewController * activityVC = [[BMSQ_ActivityVerifyViewController alloc] init];
            
            activityVC.couponCode = self.inputVerifyField.text;
            [self.navigationController pushViewController:activityVC animated:YES];
            
        }

        
    }
    
}



#pragma mark ---- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
