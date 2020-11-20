//
//  BMSQ_CouponQRcodeControllerViewController.m
//  BMSQC
//
//  Created by liuqin on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponQRcodeControllerViewController.h"

#import "UIImageView+WebCache.h"
#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"


#import "SVProgressHUD.h"

@interface BMSQ_CouponQRcodeControllerViewController ()

@property(nonatomic, strong)UIActivityIndicatorView *activeView;


@end

@implementation BMSQ_CouponQRcodeControllerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavTitle:self.myTitle];
//    [self requestQRcode];
 


    _qrcodeImageView = [[UIImageView alloc]initWithFrame:CGRectMake(50, 110, [[UIScreen mainScreen]bounds].size.width-100, 200)];
    _qrcodeImageView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:_qrcodeImageView];
    
    
    self.activeView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    self.activeView.frame = CGRectMake(0, 0,50, 50);
    self.activeView.center = CGPointMake(self.view.center.x, 110+100);
    [self.activeView startAnimating];
    [self.view addSubview:self.activeView];
    
    UILabel *lbl_message = [[UILabel alloc]initWithFrame:CGRectMake(0, _qrcodeImageView.frame.origin.y+_qrcodeImageView.frame.size.height +30, [[UIScreen mainScreen]bounds].size.width, 20)];
    lbl_message.text = @"请让收银员扫描二维码";
    lbl_message.font = [UIFont systemFontOfSize:10];
    lbl_message.backgroundColor = [UIColor clearColor];
    lbl_message.textColor = [UIColor blackColor];
    lbl_message.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:lbl_message];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    button.frame = CGRectMake(20, lbl_message.frame.origin.y+lbl_message.frame.size.height+10, lbl_message.frame.size.width-40, 40);
    button.layer.cornerRadius = 3;
    button.layer.masksToBounds = YES;
    button.backgroundColor = [UIColor colorWithRed:182/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    [button setTitle:@"取消" forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:18.f];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.view addSubview:button];
    [button addTarget:self action:@selector(clickQuit) forControlEvents:UIControlEventTouchUpInside];
    [self createModel:self.consumeCode];
    
}
#pragma mark --请求二维码数据--
-(void)requestQRcode{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
 
    NSString* vcode = [gloabFunction getSign:@"zeroPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"zeroPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSDictionary *dic = (NSDictionary *)responseObject;
        
        
          if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
            wself.consumeCode = [dic objectForKey:@"consumeCode"];
            [wself createModel:wself.consumeCode];
          }else{
              
              NSString *message = [NSString stringWithFormat:@"错误 : %@",[responseObject objectForKey:@"code"]];
              UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:message delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
              [alterView show];
              
          }

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"errr");
        
    }];

    
    
}

#pragma mark --生成二维码--
- (void)createModel:(NSString *)consumeCode
{
       Byte byteaa[] = {0x11, 0x22, 0x4F, 0x58,
        0x88, 0x10, 0x40, 0x38,
        0x28, 0x25, 0x79, 0x51,
        0xCB, 0xDD, 0x55, 0x66,
        0x77, 0x29, 0x74, 0x98,
        0x30, 0x40, 0x36, 0xE2};
    
    NSData *adata = [[NSData alloc] initWithBytes:byteaa length:24];
    NSString *aString = [[NSString alloc] initWithData:adata encoding:NSASCIIStringEncoding];
    
    NSString *qType = @"qr002";
    NSString *shopCode = [gloabFunction getUserCode];
    NSString *sSrc = [shopCode substringWithRange:NSMakeRange(shopCode.length-6,6)];
    NSString *sCode =  [self TripleDES:sSrc encryptOrDecrypt:kCCEncrypt encryptOrDecryptKey:aString withaa:byteaa];
    
    NSDictionary *dic =@{@"sSrc":sSrc,  //对
                         @"sCode":sCode,  //对
                         @"qType":qType,   //对
                         @"ordernbr":consumeCode, //对
                         @"payType":@"0"};
    
    NSError *error = nil;
    ZXMultiFormatWriter *writer = [ZXMultiFormatWriter writer];
    ZXBitMatrix* result = [writer encode:[dic JSONString]
                                  format:kBarcodeFormatQRCode
                                   width:500
                                  height:500
                                   error:&error];
    if (result) {
        CGImageRef imageT = [[ZXImage imageWithMatrix:result] cgimage];
        UIImage* imageF = [UIImage imageWithCGImage: imageT];
        _qrcodeImageView.image = imageF;
        
        [self.activeView stopAnimating];
        
    } else {
        NSString *errorMessage = [error localizedDescription];
        NSLog(@"%@",errorMessage);
    }
    
}

-(NSString *)TripleDES:(NSString *)plainText encryptOrDecrypt:(CCOperation)encryptOrDecrypt encryptOrDecryptKey:(NSString *)encryptOrDecryptKey withaa:(const void *)vkeyaa
{
    
    const void *vplainText;
    size_t plainTextBufferSize;
    
    if (encryptOrDecrypt == kCCDecrypt)//解密
    {
        NSData *EncryptData = [GTMBase64 decodeData:[plainText dataUsingEncoding:NSUTF8StringEncoding]];
        plainTextBufferSize = [EncryptData length];
        vplainText = [EncryptData bytes];
    }
    else //加密
    {
        NSData* data = [plainText dataUsingEncoding:NSUTF8StringEncoding];
        plainTextBufferSize = [data length];
        vplainText = (const void *)[data bytes];
    }
    
    CCCryptorStatus ccStatus;
    uint8_t *bufferPtr = NULL;
    size_t bufferPtrSize = 0;
    size_t movedBytes = 0;
    
    bufferPtrSize = (plainTextBufferSize + kCCBlockSize3DES) & ~(kCCBlockSize3DES - 1);
    bufferPtr = malloc( bufferPtrSize * sizeof(uint8_t));
    memset((void *)bufferPtr, 0x0, bufferPtrSize);
    // memset((void *) iv, 0x0, (size_t) sizeof(iv));
    
    const void *vkey = (const void *)[encryptOrDecryptKey UTF8String];
    // NSString *initVec = @"init Vec";
    //const void *vinitVec = (const void *) [initVec UTF8String];
    //  Byte iv[] = {0x12, 0x34, 0x56, 0x78, 0x90, 0xAB, 0xCD, 0xEF};
    ccStatus = CCCrypt(encryptOrDecrypt,
                       kCCAlgorithm3DES,
                       kCCOptionPKCS7Padding | kCCOptionECBMode,
                       vkeyaa,
                       kCCKeySize3DES,
                       nil,
                       vplainText,
                       plainTextBufferSize,
                       (void *)bufferPtr,
                       bufferPtrSize,
                       &movedBytes);
    //if (ccStatus == kCCSuccess) NSLog(@"SUCCESS");
    /*else if (ccStatus == kCC ParamError) return @"PARAM ERROR";
     else if (ccStatus == kCCBufferTooSmall) return @"BUFFER TOO SMALL";
     else if (ccStatus == kCCMemoryFailure) return @"MEMORY FAILURE";
     else if (ccStatus == kCCAlignmentError) return @"ALIGNMENT";
     else if (ccStatus == kCCDecodeError) return @"DECODE ERROR";
     else if (ccStatus == kCCUnimplemented) return @"UNIMPLEMENTED"; */
    
    NSString *result;
    
    if (encryptOrDecrypt == kCCDecrypt)
    {
        result = [[NSString alloc] initWithData:[NSData dataWithBytes:(const void *)bufferPtr
                                                               length:(NSUInteger)movedBytes]
                                       encoding:NSUTF8StringEncoding];
    }
    else
    {
        NSData *myData = [NSData dataWithBytes:(const void *)bufferPtr length:(NSUInteger)movedBytes];
        result = [GTMBase64 stringByEncodingData:myData];
    }
    
    return result;
}

#pragma mark --取消订单--
-(void)clickQuit{
    
    [self initJsonPrcClient:@"0"];
    [SVProgressHUD showWithStatus:@"正在取消订单"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    
    NSString* vcode = [gloabFunction getSign:@"cancelPay" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"cancelPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([[responseObject objectForKey:@"code"] integerValue] == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"取消订单成功"];
        }
       
        [wself.navigationController popViewControllerAnimated:YES];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"取消订单失败"];
        [wself.navigationController popViewControllerAnimated:YES];

        
    }];

 
}
@end
